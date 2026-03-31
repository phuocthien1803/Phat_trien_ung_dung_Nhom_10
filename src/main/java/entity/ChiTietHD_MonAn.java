package entity;

public class ChiTietHD_MonAn {
	private HoaDon hoaDon;
	private MonAn monAn;
	private int soLuong;
	private double thanhTien;
	private String ghiChu;


	public ChiTietHD_MonAn(HoaDon hoaDon, MonAn monAn, int soLuong, double thanhTien) {
		super();
		this.hoaDon = hoaDon;
		this.monAn = monAn;
		this.soLuong = soLuong;
		this.thanhTien = thanhTien;
	}

	public ChiTietHD_MonAn(String tenMonAn, double gia, int soLuong, double vat, double thanhTien) {
		this.monAn = new MonAn(); // Khởi tạo đối tượng MonAn
		this.monAn.setTenMonAn(tenMonAn);
		this.monAn.setGia(gia);
		this.monAn.setVAT(vat);
		this.soLuong = soLuong;
		this.thanhTien = thanhTien;
	}

	public ChiTietHD_MonAn() {
		super();
	}

	public ChiTietHD_MonAn(HoaDon hoaDon, MonAn monAn, int soLuong, double thanhTien, String ghiChu) {
		super();
		this.hoaDon = hoaDon;
		this.monAn = monAn;
		this.soLuong = soLuong;
		this.thanhTien = thanhTien;
		this.ghiChu = ghiChu;
	}

	public ChiTietHD_MonAn(String tenMon, String ghiChu, double gia, int soLuong, double thanhTien, double vat) {
		this.monAn = new MonAn();
		this.monAn.setTenMonAn(tenMon);
		this.ghiChu = ghiChu;
		this.monAn.setGia(gia);
		this.soLuong = soLuong;
		this.thanhTien = thanhTien;
		this.monAn.setVAT(vat);
	}

	public ChiTietHD_MonAn(String tenMonAn, int soLuong,double tongTien) {
		this.monAn = new MonAn();
		this.monAn.setTenMonAn(tenMonAn);
		this.soLuong = soLuong;
		this.setThanhTien(tongTien);
	}

	public ChiTietHD_MonAn(String tenMon, Double gia, int soLuong, String ghiChu, Double thanhTien, Double vat, String maHD, double tongTien, double tienCoc) {
		this.monAn = new MonAn();
		this.monAn.setTenMonAn(tenMon);
		this.monAn.setGia(gia);
		this.soLuong = soLuong;
		this.ghiChu = ghiChu;
		this.thanhTien = thanhTien;
		this.monAn.setVAT(vat);
		this.hoaDon =	 new HoaDon();
		this.hoaDon.setMaHD(maHD);
		this.hoaDon.setTienCoc(tienCoc);
		this.hoaDon.setTongTien(tongTien);
	}
	public double getTienCoc() {
		return hoaDon.getTienCoc();
	}

	public double getTongTien() {
		return hoaDon.getTongTien();
	}
	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
	public String getTenMonAn() {
		return monAn.getTenMonAn();
	}
	public double getGia() {
		return monAn.getGia();
	}
	public double getVAT() {
		return monAn.getVAT();
	}


	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
	}

	public MonAn getMonAn() {
		return monAn;
	}

	public void setMonAn(MonAn monAn) {
		this.monAn = monAn;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public double getThanhTien() {

		return thanhTien;
	}

	public void setThanhTien(double thanhTien) {
		this.thanhTien = thanhTien;
	}

	@Override
	public String toString() {
		return "ChiTietHD_MonAn [hoaDon=" + hoaDon + ", monAn=" + monAn + ", soLuong=" + soLuong + ", thanhTien="
				+ thanhTien + "]";
	}


	public ChiTietHD_MonAn(String tenMonAn, double gia, int soLuong,double tongTien) {
		this.monAn = new MonAn();
		this.monAn.setTenMonAn(tenMonAn);
		this.monAn.setGia(gia);
		this.soLuong = soLuong;
		this.hoaDon= new HoaDon();
		this.hoaDon.setTongTien(tongTien);
	}

	public double getDoanhThu() {
		return monAn.getGia() * soLuong;
	}

}
