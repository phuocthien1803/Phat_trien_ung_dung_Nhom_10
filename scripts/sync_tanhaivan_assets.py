#!/usr/bin/env python3
"""
Sync Tan Hai Van menu images + logo into local JavaFX resources.

Default mode is dry-run (no file write). Use --apply to write files.

Usage examples:
  python scripts/sync_tanhaivan_assets.py
  python scripts/sync_tanhaivan_assets.py --apply --replace-logo
  python scripts/sync_tanhaivan_assets.py --apply --min-score 0.48
"""

from __future__ import annotations

import argparse
import html
import json
import os
import re
import sys
import unicodedata
from dataclasses import dataclass
from difflib import SequenceMatcher
from pathlib import Path
from typing import Dict, List, Optional, Set, Tuple
from urllib.error import HTTPError, URLError
from urllib.parse import urljoin, urlparse
from urllib.request import Request, urlopen

try:
    import requests
except Exception:  # noqa: BLE001
    requests = None


BASE_URL = "https://tanhaivan.com"
USER_AGENT = "Mozilla/5.0 (compatible; TanHaiVanSync/1.0)"

# These are the icon files currently used by the app.
LOGO_TARGETS = [
    "src/main/resources/images/logo.webpg",
    "src/main/resources/images/logo.webp",
    "src/main/resources/images/logo.webp",
]
LOGO_FALLBACKS = [
    "https://bizweb.dktcdn.net/100/448/704/themes/854708/assets/logo.png?1764787373355",
]

MONAN_DIR = Path("src/main/resources/monAnImages")
SQL_PATH = Path(".sql/init_db.sql")
REPORT_PATH = Path("scripts/tanhaivan_sync_report.json")

SEED_PATHS = [
    "/",
    "/dat-mon-online",
    "/dimsum",
    "/hu-tieu-mi",
    "/mon-chinh",
    "/com",
    "/ga-va-vit",
    "/heo-va-bo",
    "/mon-tiem-va-sup",
    "/rau",
    "/lau",
    "/mon-chay",
    "/khai-vi",
]

IMAGE_EXTS = (".jpg", ".jpeg", ".png", ".webp")


@dataclass
class ProductImage:
    name: str
    page_url: str
    image_url: str


@dataclass
class LocalDish:
    code: str
    name: str
    image_file: str


def fetch_text(url: str, timeout: int = 20) -> str:
    if requests is not None:
        r = requests.get(url, timeout=timeout, headers={"User-Agent": USER_AGENT})
        r.raise_for_status()
        r.encoding = r.encoding or "utf-8"
        return r.text
    req = Request(url, headers={"User-Agent": USER_AGENT})
    with urlopen(req, timeout=timeout) as res:
        charset = res.headers.get_content_charset() or "utf-8"
        data = res.read()
    return data.decode(charset, errors="replace")


def fetch_bytes(url: str, timeout: int = 30) -> bytes:
    if requests is not None:
        r = requests.get(url, timeout=timeout, headers={"User-Agent": USER_AGENT})
        r.raise_for_status()
        return r.content
    req = Request(url, headers={"User-Agent": USER_AGENT})
    with urlopen(req, timeout=timeout) as res:
        return res.read()


def normalize_url(base: str, maybe_url: str) -> Optional[str]:
    if not maybe_url:
        return None
    maybe_url = maybe_url.strip()
    if maybe_url.startswith(("javascript:", "mailto:", "tel:", "#")):
        return None
    absolute = urljoin(base, maybe_url)
    parsed = urlparse(absolute)
    if parsed.scheme not in ("http", "https"):
        return None
    if parsed.netloc and "tanhaivan.com" not in parsed.netloc and "bizweb.dktcdn.net" not in parsed.netloc:
        return None
    # Drop URL fragments for dedupe
    cleaned = absolute.split("#", 1)[0]
    return cleaned


def extract_links(html_text: str, base: str) -> Set[str]:
    links: Set[str] = set()
    for m in re.finditer(r"""href\s*=\s*["']([^"']+)["']""", html_text, flags=re.IGNORECASE):
        u = normalize_url(base, html.unescape(m.group(1)))
        if u:
            links.add(u)
    return links


def extract_sources(html_text: str, base: str) -> Set[str]:
    srcs: Set[str] = set()
    for m in re.finditer(r"""src\s*=\s*["']([^"']+)["']""", html_text, flags=re.IGNORECASE):
        u = normalize_url(base, html.unescape(m.group(1)))
        if u:
            srcs.add(u)
    return srcs


def strip_tags(raw: str) -> str:
    no_tags = re.sub(r"<[^>]+>", " ", raw)
    return html.unescape(re.sub(r"\s+", " ", no_tags)).strip()


def extract_h1(html_text: str) -> str:
    m = re.search(r"<h1[^>]*>(.*?)</h1>", html_text, flags=re.IGNORECASE | re.DOTALL)
    if not m:
        return ""
    return strip_tags(m.group(1))


def is_image_url(url: str) -> bool:
    p = urlparse(url).path.lower()
    return p.endswith(IMAGE_EXTS)


def looks_like_logo(url: str) -> bool:
    u = url.lower()
    return any(k in u for k in ("logo", "favicon", "icon"))


def looks_like_product_page(url: str, html_text: str) -> bool:
    path = urlparse(url).path.lower()
    norm = remove_accents(html_text).lower()
    # Stricter product heuristic to avoid category/news pages.
    has_product_note = "hinh anh chi mang tinh chat minh hoa" in norm or "gia chua bao gom vat" in norm
    has_add_to_cart = "them vao gio hang" in norm
    has_price = bool(re.search(r"\d{1,3}(?:\.\d{3})+\s*₫", html_text))
    if has_product_note and has_add_to_cart and has_price:
        return True
    # Fallback for old templates.
    if path.endswith("-tan-hai-van") and has_add_to_cart and has_price:
        return True
    return False


def remove_accents(s: str) -> str:
    nkfd = unicodedata.normalize("NFKD", s)
    return "".join(ch for ch in nkfd if not unicodedata.combining(ch))


def normalize_name(s: str) -> str:
    s = remove_accents(s).lower()
    s = re.sub(r"[^a-z0-9\s]+", " ", s)
    s = re.sub(r"\s+", " ", s).strip()
    return s


def score_match(local_name: str, remote_name: str) -> float:
    a = normalize_name(local_name)
    b = normalize_name(remote_name)
    if not a or not b:
        return 0.0
    ratio = SequenceMatcher(None, a, b).ratio()
    ta = set(a.split())
    tb = set(b.split())
    overlap = len(ta & tb) / max(1, len(ta))
    return 0.72 * ratio + 0.28 * overlap


def choose_best_image_url(candidates: Set[str]) -> Optional[str]:
    if not candidates:
        return None
    scored: List[Tuple[int, str]] = []
    for u in candidates:
        score = 0
        lu = u.lower()
        if "bizweb.dktcdn.net" in lu:
            score += 6
        if is_image_url(u):
            score += 5
        if "product" in lu or "thumb" in lu or "files" in lu:
            score += 2
        if looks_like_logo(u):
            score -= 8
        scored.append((score, u))
    scored.sort(key=lambda x: x[0], reverse=True)
    for _, u in scored:
        if is_image_url(u):
            return u
    return scored[0][1] if scored else None


def choose_best_product_image(name: str, page_url: str, candidates: Set[str]) -> Optional[str]:
    path_slug = urlparse(page_url).path.strip("/").lower()
    path_slug = path_slug.replace("-tan-hai-van", "")
    name_norm = normalize_name(name)
    name_tokens = [t for t in name_norm.split() if len(t) >= 3]

    img_candidates = [u for u in candidates if is_image_url(u)]
    if not img_candidates:
        return None

    ranked: List[Tuple[float, str]] = []
    for u in img_candidates:
        lu = u.lower()
        score = 0.0
        if "bizweb.dktcdn.net" in lu:
            score += 4.0
        if "/products/" in lu:
            score += 4.0
        if "thumb/grande" in lu:
            score += 1.5
        if path_slug and path_slug in lu:
            score += 7.0
        if looks_like_logo(lu):
            score -= 8.0
        if any(k in lu for k in ("banner", "favicon", "icon", "placeholder", "dang-cap-nhat")):
            score -= 5.0

        token_hits = sum(1 for tk in name_tokens if tk in lu)
        score += token_hits * 0.9
        ranked.append((score, u))

    ranked.sort(key=lambda x: x[0], reverse=True)
    if ranked and ranked[0][0] > 0:
        return ranked[0][1]
    return choose_best_image_url(set(img_candidates))


def crawl_products(max_pages: int = 260) -> List[ProductImage]:
    queue = [urljoin(BASE_URL, p) for p in SEED_PATHS]
    visited: Set[str] = set()
    products: Dict[str, ProductImage] = {}

    while queue and len(visited) < max_pages:
        url = queue.pop(0)
        if url in visited:
            continue
        visited.add(url)

        try:
            page = fetch_text(url)
        except (HTTPError, URLError, TimeoutError, Exception):
            continue

        # Expand crawl only for tanhaivan pages
        for link in extract_links(page, url):
            parsed = urlparse(link)
            if "tanhaivan.com" in parsed.netloc and link not in visited:
                if parsed.path and not parsed.path.startswith("/search"):
                    queue.append(link)

        if not looks_like_product_page(url, page):
            continue

        name = extract_h1(page).strip()
        if not name:
            continue

        # Try to collect best image from src/href candidates + og:image.
        candidates = set()
        candidates.update(extract_sources(page, url))
        candidates.update(extract_links(page, url))

        og = re.search(
            r"""<meta\s+property=["']og:image["']\s+content=["']([^"']+)["']""",
            page,
            flags=re.IGNORECASE,
        )
        if og:
            u = normalize_url(url, html.unescape(og.group(1)))
            if u:
                candidates.add(u)

        best = choose_best_product_image(name, url, candidates)
        if best:
            key = normalize_name(name)
            products[key] = ProductImage(name=name, page_url=url, image_url=best)

    return list(products.values())


def parse_local_dishes(sql_path: Path) -> List[LocalDish]:
    if not sql_path.exists():
        raise FileNotFoundError(f"Cannot find SQL seed file: {sql_path}")
    text = sql_path.read_text(encoding="utf-8", errors="replace")
    # Matches insert lines in current schema.
    pattern = re.compile(
        r"INSERT\s+\[dbo\]\.\[MonAn\]\s+\(\[maMonAn\],\s+\[tenMonAn\].*?\)\s+VALUES\s+\(N'([^']+)',\s+N'([^']+)',.*?,\s+N'([^']+)'\)\s*",
        flags=re.IGNORECASE,
    )
    items: List[LocalDish] = []
    for m in pattern.finditer(text):
        code = m.group(1).strip()
        ten = m.group(2).strip()
        image_file = m.group(3).strip()
        # Last captured group includes hinhAnh in current SQL line order.
        if not image_file.lower().endswith((".png", ".jpg", ".jpeg", ".webp")):
            continue
        items.append(LocalDish(code=code, name=ten, image_file=image_file))
    # dedupe by code
    seen = set()
    deduped = []
    for it in items:
        if it.code in seen:
            continue
        seen.add(it.code)
        deduped.append(it)
    return deduped


def select_logo_image(home_html: str, base: str) -> Optional[str]:
    candidates: Set[str] = set()
    candidates.update(extract_sources(home_html, base))
    candidates.update(extract_links(home_html, base))

    for m in re.finditer(
        r"""<meta\s+property=["']og:image["']\s+content=["']([^"']+)["']""",
        home_html,
        flags=re.IGNORECASE,
    ):
        u = normalize_url(base, html.unescape(m.group(1)))
        if u:
            candidates.add(u)

    logo_like = [u for u in candidates if is_image_url(u) and looks_like_logo(u)]
    if logo_like:
        # Prefer real logo files over menu icons.
        logo_like.sort(
            key=lambda u: (
                "/assets/logo.png" in u.lower(),
                "/logo." in u.lower() or "logo_share" in u.lower(),
                "logo_share" not in u.lower(),
                "icon-menu" not in u.lower(),
                "bizweb.dktcdn.net" in u.lower(),
                len(u),
            ),
            reverse=True,
        )
        return logo_like[0]

    any_img = [u for u in candidates if is_image_url(u)]
    if any_img:
        return any_img[0]
    return None


def ensure_parent(path: Path) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)


def write_binary(path: Path, data: bytes, apply: bool) -> None:
    if not apply:
        return
    ensure_parent(path)
    path.write_bytes(data)


def main() -> int:
    parser = argparse.ArgumentParser(description="Sync Tan Hai Van menu images and logo.")
    parser.add_argument("--apply", action="store_true", help="Write files (default is dry-run).")
    parser.add_argument("--replace-logo", action="store_true", help="Also overwrite app logo files.")
    parser.add_argument("--max-pages", type=int, default=260, help="Crawler page budget.")
    parser.add_argument("--min-score", type=float, default=0.58, help="Min fuzzy score for dish-image match.")
    parser.add_argument("--code-prefix", type=str, default="FO", help="Filter local dish code prefix (e.g. FO, DR, AC, ALL).")
    parser.add_argument("--fill-unmatched", action="store_true", help="Assign remaining dishes with unused Tan Hai Van images.")
    args = parser.parse_args()

    print(f"[info] mode={'APPLY' if args.apply else 'DRY-RUN'}")
    print(f"[info] transport={'requests' if requests is not None else 'urllib'}")
    print("[info] crawling tanhaivan.com ...")
    products = crawl_products(max_pages=args.max_pages)
    print(f"[info] collected product pages with images: {len(products)}")
    if not products:
        print("[error] no product image found from tanhaivan.com")
        return 2

    local_dishes = parse_local_dishes(SQL_PATH)
    prefix = args.code_prefix.upper().strip()
    if prefix and prefix != "ALL":
        local_dishes = [d for d in local_dishes if d.code.upper().startswith(prefix)]
    print(f"[info] local dishes from SQL: {len(local_dishes)}")

    # Build quick index for matching
    matches = []
    product_names = [p.name for p in products]

    used_remote_images: Set[str] = set()
    for dish in local_dishes:
        best: Optional[ProductImage] = None
        best_score = -1.0
        for p in products:
            s = score_match(dish.name, p.name)
            if s > best_score:
                best = p
                best_score = s
        if best and best_score >= args.min_score:
            used_remote_images.add(best.image_url)
            matches.append(
                {
                    "code": dish.code,
                    "local_name": dish.name,
                    "local_file": dish.image_file,
                    "remote_name": best.name,
                    "remote_page": best.page_url,
                    "remote_image": best.image_url,
                    "score": round(best_score, 4),
                }
            )
        else:
            matches.append(
                {
                    "code": dish.code,
                    "local_name": dish.name,
                    "local_file": dish.image_file,
                    "remote_name": None,
                    "remote_page": None,
                    "remote_image": None,
                    "score": round(max(0.0, best_score), 4),
                }
            )

    if args.fill_unmatched:
        product_pool = [p for p in products if p.image_url not in used_remote_images]
        pool_idx = 0
        for row in matches:
            if row["remote_image"]:
                continue
            if pool_idx >= len(product_pool):
                break
            p = product_pool[pool_idx]
            pool_idx += 1
            row["remote_name"] = f"{p.name} (auto-fill)"
            row["remote_page"] = p.page_url
            row["remote_image"] = p.image_url
            row["score"] = row["score"]

    replaced = 0
    failed: List[Dict[str, str]] = []
    for row in matches:
        image_url = row["remote_image"]
        if not image_url:
            continue
        target = MONAN_DIR / row["local_file"]
        try:
            data = fetch_bytes(image_url)
            # Keep original extension in file name used by app/DB.
            write_binary(target, data, apply=args.apply)
            replaced += 1
        except Exception as ex:  # noqa: BLE001
            failed.append({"file": str(target), "url": image_url, "error": str(ex)})

    logo_result: Dict[str, Optional[str]] = {"source": None, "status": None}
    if args.replace_logo:
        try:
            home = fetch_text(BASE_URL)
            logo_url = select_logo_image(home, BASE_URL)
            if not logo_url:
                for fb in LOGO_FALLBACKS:
                    try:
                        _ = fetch_bytes(fb, timeout=15)
                        logo_url = fb
                        break
                    except Exception:  # noqa: BLE001
                        pass
            logo_result["source"] = logo_url
            if logo_url:
                data = fetch_bytes(logo_url)
                for rel in LOGO_TARGETS:
                    write_binary(Path(rel), data, apply=args.apply)
                logo_result["status"] = "replaced"
            else:
                logo_result["status"] = "not_found"
        except Exception as ex:  # noqa: BLE001
            logo_result["status"] = f"error: {ex}"

    report = {
        "mode": "apply" if args.apply else "dry-run",
        "products_found": len(products),
        "local_dishes": len(local_dishes),
        "replaced_images": replaced,
        "failed_downloads": failed,
        "logo": logo_result,
        "min_score": args.min_score,
        "matches": matches,
        "remote_product_names_sample": product_names[:30],
    }
    ensure_parent(REPORT_PATH)
    REPORT_PATH.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")

    print(f"[info] image entries matched (>= threshold): {sum(1 for m in matches if m['remote_image'])}")
    print(f"[info] replaced image files: {replaced}")
    if failed:
        print(f"[warn] failed downloads: {len(failed)}")
    print(f"[info] report written: {REPORT_PATH}")
    if not args.apply:
        print("[hint] run with --apply --replace-logo to write files.")

    return 0


if __name__ == "__main__":
    sys.exit(main())
