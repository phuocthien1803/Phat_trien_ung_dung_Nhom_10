package entity;

import java.util.Objects;

public class MonAn {
	private String maMonAn;
	private String tenMonAn;
	private LoaiMonAn loaiMonAn;
	private double gia;
	private TrangThaiMonAn trangThaiMonAn;
	private double VAT;
	private String hinhAnh;
	private ChiTietHD_MonAn chiTietHD_monAn;


	public MonAn(String maMonAn, String tenMonAn, LoaiMonAn loaiMonAn, double gia, TrangThaiMonAn trangThaiMonAn,
			double vAT, String hinhAnh) {
		super();
		this.maMonAn = maMonAn;
		this.tenMonAn = tenMonAn;
		this.loaiMonAn = loaiMonAn;
		this.gia = gia;
		this.trangThaiMonAn = trangThaiMonAn;
		this.VAT = vAT;
		this.hinhAnh = hinhAnh;
	}
	public MonAn(String tenMonAn,int soLuong, double thanhTien) {
		this.tenMonAn = tenMonAn;
		this.chiTietHD_monAn= new ChiTietHD_MonAn();
		this.chiTietHD_monAn.setSoLuong(soLuong);
		this.chiTietHD_monAn.setThanhTien(thanhTien);
	}
	public MonAn() {
		super();
	}


	public String getMaMonAn() {
		return maMonAn;
	}

	public void setMaMonAn(String maMonAn) {
		this.maMonAn = maMonAn;
	}

	public String getTenMonAn() {
		return tenMonAn;
	}

	public void setTenMonAn(String tenMonAn) {
		this.tenMonAn = tenMonAn;
	}

	public LoaiMonAn getLoaiMonAn() {
		return loaiMonAn;
	}

	public void setLoaiMonAn(LoaiMonAn loaiMonAn) {
		this.loaiMonAn = loaiMonAn;
	}

	public double getGia() {
		return gia;
	}

	public void setGia(double gia) {
		this.gia = gia;
	}

	public TrangThaiMonAn getTrangThaiMonAn() {
		return trangThaiMonAn;
	}

	public void setTrangThaiMonAn(TrangThaiMonAn trangThaiMonAn) {
		this.trangThaiMonAn = trangThaiMonAn;
	}

	public double getVAT() {
		return VAT;
	}

	public void setVAT(double vAT) {
		VAT = vAT;
	}

	public String getHinhAnh() {
		return hinhAnh;
	}

	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}

	public String getMaLoai(){
		return loaiMonAn != null ? loaiMonAn.getMaLoai() : "";
	}

	public String getTenLoai(){
		return loaiMonAn != null ? loaiMonAn.getTenLoai() : "";
	}

	@Override
	public int hashCode() {
		return Objects.hash(maMonAn);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MonAn other = (MonAn) obj;
		return Objects.equals(maMonAn, other.maMonAn);
	}

	@Override
	public String toString() {
		return "MonAn [maMonAn=" + maMonAn + ", tenMonAn=" + tenMonAn + ", loaiMonAn=" + loaiMonAn + ", gia=" + gia
				+ ", trangThaiMonAn=" + trangThaiMonAn + ", VAT=" + VAT + ", hinhAnh=" + hinhAnh +"]";
	}

	public ChiTietHD_MonAn getChiTietHD_monAn() {
		return chiTietHD_monAn;
	}

	public void setChiTietHD_monAn(ChiTietHD_MonAn chiTietHD_monAn) {
		this.chiTietHD_monAn = chiTietHD_monAn;
	}



}
