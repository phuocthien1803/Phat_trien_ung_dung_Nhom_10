package entity;

import java.util.Objects;

public class LoaiMonAn {
	private String maLoai;
	private String tenLoai;

	public LoaiMonAn(String maLoai, String tenLoai) {
		super();
		this.maLoai = maLoai;
		this.tenLoai = tenLoai;
	}

	public LoaiMonAn() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(maLoai);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoaiMonAn other = (LoaiMonAn) obj;
		return Objects.equals(maLoai, other.maLoai);
	}

	public String getMaLoai() {
		return maLoai;
	}

	public void setMaLoai(String maLoai) {
		this.maLoai = maLoai;
	}

	public String getTenLoai() {
		return tenLoai;
	}

	public void setTenLoai(String tenLoai) {
		this.tenLoai = tenLoai;
	}

	@Override
	public String toString() {
		return "LoaiMonAn [maLoai=" + maLoai + ", tenLoai=" + tenLoai + "]";
	}
}
