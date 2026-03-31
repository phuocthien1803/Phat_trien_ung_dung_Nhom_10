package entity;

import java.util.Objects;

public class KhachHang {
	private String maKH;
	private String tenKH;
	private String sDT;
	private int diemTichLuy;

	public KhachHang(String maKH, String tenKH, String sDT, int diemTichLuy) {
		super();
		this.maKH = maKH;
		this.tenKH = tenKH;
		this.sDT = sDT;
		this.diemTichLuy = diemTichLuy;
	}
	public KhachHang(String sDT, String tenKH, int diemTichLuy, String maKH) {
		this.tenKH = tenKH;
		this.sDT = sDT;
		this.diemTichLuy = diemTichLuy;
		this.maKH = maKH;
	}
	public KhachHang() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(maKH);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhachHang other = (KhachHang) obj;
		return Objects.equals(maKH, other.maKH);
	}

	public String getMaKH() {
		return maKH;
	}

	public void setMaKH(String maKH) {
		this.maKH = maKH;
	}

	public String getTenKH() {
		return tenKH;
	}

	public void setTenKH(String tenKH) {
		this.tenKH = tenKH;
	}

	public String getsDT() {
		return sDT;
	}

	public void setsDT(String sDT) {
		this.sDT = sDT;
	}

	public int getDiemTichLuy() {
		return diemTichLuy;
	}

	public void setDiemTichLuy(int diemTichLuy) {
		this.diemTichLuy = diemTichLuy;
	}

	@Override
	public String toString() {
		return "KhachHang [maKH=" + maKH + ", tenKH=" + tenKH + ", sDT=" + sDT + ", diemTichLuy=" + diemTichLuy + "]";
	}
	public KhachHang(String tenKH,String sDT) {
		super();
		this.tenKH = tenKH;
		this.sDT = sDT;
	}
}
