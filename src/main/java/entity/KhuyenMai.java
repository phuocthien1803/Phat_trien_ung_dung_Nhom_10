package entity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

public class KhuyenMai {
	private String maKM;
	private String tenKM;
	private LocalDate ngayHetHan;
	private LocalDate ngayBatDau;
	private double phanTramGiam;

	public KhuyenMai(String maKM, String tenKM, LocalDate ngayBatDau, LocalDate ngayHetHan, double phanTramGiam) {
		super();
		this.maKM = maKM;
		this.tenKM = tenKM;
		this.ngayHetHan = ngayHetHan;
		this.ngayBatDau = ngayBatDau;
		this.phanTramGiam = phanTramGiam;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maKM);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhuyenMai other = (KhuyenMai) obj;
		return Objects.equals(maKM, other.maKM);
	}

	public String getMaKM() {
		return maKM;
	}

	public void setMaKM(String maKM) {
		this.maKM = maKM;
	}

	public String getTenKM() {
		return tenKM;
	}

	public void setTenKM(String tenKM) {
		this.tenKM = tenKM;
	}

	public LocalDate getNgayHetHan() {
		return ngayHetHan;
	}

	public void setNgayHetHan(LocalDate ngayHetHan) {
		this.ngayHetHan = ngayHetHan;
	}

	public LocalDate getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(LocalDate ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}

	public double getPhanTramGiam() {
		return phanTramGiam;
	}

	public void setPhanTramGiam(double phanTramGiam) {
		this.phanTramGiam = phanTramGiam;
	}

	@Override
	public String toString() {
		return "KhuyenMai [maKM=" + maKM + ", tenKM=" + tenKM + ", ngayHetHan=" + ngayHetHan + ", ngayBatDau="
				+ ngayBatDau + ", phanTramGiam=" + phanTramGiam + "]";
	}

}
