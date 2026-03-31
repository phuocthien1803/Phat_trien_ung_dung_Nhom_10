package entity;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class HoaDon {
	private String maHD;
	private LocalDate ngayTaoHD;
	private TrangThaiHoaDon trangThaiHoaDon;
	private LocalDate ngayDat;
	private Ban ban;
	private NhanVien nhanVien;
	private KhachHang khachHang;
	private double tienCoc;
	private LocalTime gioDatBan;
	private KhuyenMai khuyenMai;
	private HinhThuc hinhThuc;
	private double tongTien;
	private int soLuongKH;
	private boolean checkIn;

	public HoaDon(String maHD, LocalDate ngayTaoHD, TrangThaiHoaDon trangThaiHoaDon, LocalDate ngayDat, Ban ban,
			NhanVien nhanVien, KhachHang khachHang, double tienCoc, LocalTime gioDatBan, KhuyenMai khuyenMai, boolean checkIn) {
		super();
		this.maHD = maHD;
		this.ngayTaoHD = ngayTaoHD;
		this.trangThaiHoaDon = trangThaiHoaDon;
		this.ngayDat = ngayDat;
		this.ban = ban;
		this.nhanVien = nhanVien;
		this.khachHang = khachHang;
		this.tienCoc = tienCoc;
		this.gioDatBan = gioDatBan;
		this.khuyenMai = khuyenMai;
		this.checkIn = checkIn;
	}

	public HoaDon(String maHD) {
		this.maHD = maHD;
	}
	public HoaDon(String maHD, LocalTime gioDatBan) {
		this.maHD = maHD;
		this.gioDatBan = gioDatBan;
	}


	public HoaDon(String maHD,String tenKhachHang, String sdt, LocalTime gioDatBan, String maBan) {
		this.maHD=maHD;
		this.khachHang = new KhachHang();
		this.khachHang.setTenKH(tenKhachHang);
		this.khachHang.setsDT(sdt);
		this.gioDatBan = gioDatBan;
		this.ban = new Ban();
		this.ban.setMaBan(maBan);
	}

    public HoaDon(LocalDate ngayTaoHD, LocalTime gioDatBan, String maBan, double tienCoc) {
		this.ngayTaoHD=ngayTaoHD;
		this.gioDatBan=gioDatBan;
		this.ban=new Ban();
		this.ban.setMaBan(maBan);
		this.tienCoc=tienCoc;
    }

	public HoaDon(String maHD, LocalDate ngayTaoHD, TrangThaiHoaDon trangThaiHoaDon, LocalDate ngayDat, Ban ban,
				  NhanVien nhanVien, KhachHang khachHang, double tienCoc, LocalTime gioDatBan, KhuyenMai khuyenMai, int soLuongKH, HinhThuc hinhThuc, double tongTien) {
		super();
		this.maHD = maHD;
		this.ngayTaoHD = ngayTaoHD;
		this.trangThaiHoaDon = trangThaiHoaDon;
		this.ngayDat = ngayDat;
		this.ban = ban;
		this.nhanVien = nhanVien;
		this.khachHang = khachHang;
		this.tienCoc = tienCoc;
		this.gioDatBan = gioDatBan;
		this.khuyenMai = khuyenMai;
		this.soLuongKH = soLuongKH;
		this.hinhThuc = hinhThuc;
		this.tongTien = tongTien;
	}

	public HoaDon(String maHD, String sdt, String tenKhachHang, LocalDate ngayTaoHD, LocalDate ngayDat, LocalTime gioDatBan,
				  TrangThaiHoaDon trangThaiHoaDon, String maNV, int diemTL, int soLuongKH, HinhThuc hinhThuc, String maBan, Double tongTien, Double tienCoc, boolean checkIn) {
		this.maHD=maHD;
		this.khachHang = new KhachHang();
		this.khachHang.setsDT(sdt);  // Đảm bảo set đúng số điện thoại
		this.khachHang.setTenKH(tenKhachHang);  // Đảm bảo set tên khách hàng đúng thuộc tính
		this.ngayTaoHD = ngayTaoHD;
		this.gioDatBan = gioDatBan;
		this.ngayDat = ngayDat;
		this.trangThaiHoaDon = trangThaiHoaDon;
		this.ban = new Ban();
		this.nhanVien = new NhanVien();
		this.nhanVien.setMaNV(maNV);
		this.khachHang.setDiemTichLuy(diemTL);
		this.hinhThuc = hinhThuc;
		this.soLuongKH = soLuongKH;
		this.ban.setMaBan(maBan);
		this.tongTien = tongTien;
		this.tienCoc = tienCoc;
		this.checkIn = checkIn;
	}

	public HoaDon(double doanhThu, int soHoaDon) {
	}

    public HoaDon(LocalDate ngayDat, double tongTien) {
		this.ngayDat= ngayDat;
		this.tongTien=tongTien;
    }


    public String getTenKH() {
		return khachHang.getTenKH();
	}
	public String getSDT() {
		return khachHang.getsDT();
	}
	public String getMaBan() {
		return ban.getMaBan();
	}

	public HoaDon() {
		super();
	}
    public String getMaHD() {
		return maHD;
	}

	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}

	public LocalDate getNgayTaoHD() {
		return ngayTaoHD;
	}

	public void setNgayTaoHD(LocalDate ngayTaoHD) {
		this.ngayTaoHD = ngayTaoHD;
	}

	public TrangThaiHoaDon getTrangThaiHoaDon() {
		return trangThaiHoaDon;
	}

	public void setTrangThaiHoaDon(TrangThaiHoaDon trangThaiHoaDon) {
		this.trangThaiHoaDon = trangThaiHoaDon;
	}

	public LocalDate getNgayDat() {
		return ngayDat;
	}

	public void setNgayDat(LocalDate ngayDat) {
		this.ngayDat = ngayDat;
	}

	public Ban getBan() {
		return ban;
	}

	public void setBan(Ban ban) {
		this.ban = ban;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}

	public double getTienCoc() {
		return tienCoc;
	}

	public void setTienCoc(double tienCoc) {
		this.tienCoc = tienCoc;
	}

	public LocalTime getGioDatBan() {
		return gioDatBan;
	}

	public void setGioDatBan(LocalTime gioDatBan) {
		this.gioDatBan = gioDatBan;
	}

	public KhuyenMai getKhuyenMai() {
		return khuyenMai;
	}

	public void setKhuyenMai(KhuyenMai khuyenMai) {
		this.khuyenMai = khuyenMai;
	}

	public double getTongTien() {
		return tongTien;
	}

	public void setTongTien(double tongTien) {
		this.tongTien = tongTien;
	}

	public int getSoLuongKH() {
		return soLuongKH;
	}

	public void setSoLuongKH(int soLuongKH) {
		this.soLuongKH = soLuongKH;
	}

	public HinhThuc getHinhThuc() {
		return hinhThuc;
	}

	public void setHinhThuc(HinhThuc hinhThuc) {
		this.hinhThuc = hinhThuc;
	}

	public boolean isCheckIn() {
		return checkIn;
	}

	public void setCheckIn(boolean checkIn) {
		this.checkIn = checkIn;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maHD);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HoaDon other = (HoaDon) obj;
		return Objects.equals(maHD, other.maHD);
	}

	@Override
	public String toString() {
		return "HoaDon [maHD=" + maHD + ", ngayTaoHD=" + ngayTaoHD + ", trangThaiHoaDon=" + trangThaiHoaDon
				+ ", ngayDat=" + ngayDat + ", ban=" + ban + ", nhanVien=" + nhanVien + ", khachHang=" + khachHang
				+ ", tienCoc=" + tienCoc + ", gioDatBan=" + gioDatBan + ", khuyenMai=" + khuyenMai + "]";
	}


	public HoaDon(String maHD,String tenKhachHang, String sdt, LocalDate ngayTaoHD, String maBan,String
			tenNhanVien,double tongTien) {
		this.maHD=maHD;
		this.khachHang = new KhachHang();
		this.khachHang.setTenKH(tenKhachHang);
		this.khachHang.setsDT(sdt);
		this.ngayTaoHD = ngayTaoHD;
		this.ban = new Ban();
		this.ban.setMaBan(maBan);
		this.nhanVien= new NhanVien();
		this.nhanVien.setTenNV(tenNhanVien);
		this.tongTien=tongTien;
	}

	public String getTenNV() {
		return nhanVien.getTenNV();
	}
	public HoaDon(String maHD,String tenKhachHang, String sdt, LocalDate ngayTaoHD, String maBan
			,double tongTien) {
		this.maHD=maHD;
		this.khachHang = new KhachHang();
		this.khachHang.setTenKH(tenKhachHang);
		this.khachHang.setsDT(sdt);
		this.ngayTaoHD = ngayTaoHD;
		this.ban = new Ban();
		this.ban.setMaBan(maBan);
		this.tongTien=tongTien;
	}

	public LocalDateTime getNgayGioDatBan() {
		return LocalDateTime.of(ngayDat, gioDatBan);
	}

	public boolean isQuaHan() {
		return LocalDateTime.now().isAfter(getNgayGioDatBan());
	}

	public String getFormattedNgayGioDatBan() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		return getNgayGioDatBan().format(formatter);
	}

	public String getThoiGianQuaHan() {
		if (!isQuaHan()) {
			return "";
		}
		Duration duration = Duration.between(getNgayGioDatBan(), LocalDateTime.now());
		long hours = duration.toHours();
		long minutes = duration.toMinutes() % 60;
		long seconds = duration.getSeconds() % 60;

		if (hours > 0) {
			return String.format("Quá giờ đặt bàn %d giờ %d phút %d giây", hours, minutes, seconds);
		} else if (minutes > 0) {
			return String.format("Quá giờ đặt bàn %d phút %d giây", minutes, seconds);
		} else {
			return String.format("Quá giờ đặt %d giây", seconds);
		}
	}
}
