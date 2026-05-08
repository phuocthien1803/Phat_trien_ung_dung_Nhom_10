package entity;

import java.time.LocalDate;
import java.util.Objects;

public class NhanVien {
	private String maNV;
	private String tenNV;
	private boolean gioiTinh;
	private String sDT;
	private TrangThaiNhanVien trangThai;
	private String chucVu;
	private LocalDate ngayVaoLam;
	private LocalDate ngayNghiLam;
	private LocalDate ngaySinh;
	private HoaDon hoaDon;
	private double doanhThu;
	private String question;
	private String answer;
	private int soHoaDon;


	public NhanVien(String maNV, String tenNV, boolean gioiTinh, String sDT, TrangThaiNhanVien trangThai, String chucVu, LocalDate ngayVaoLam,
					LocalDate ngayNghiLam, LocalDate ngaySinh, String question ,String answer) {
		super();
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.gioiTinh = gioiTinh;
		this.sDT = sDT;
		this.trangThai = trangThai;
		this.chucVu = chucVu;
		this.ngayVaoLam = ngayVaoLam;
		this.ngayNghiLam = ngayNghiLam;
		this.ngaySinh = ngaySinh;
		this.question = question;
		this.answer = answer;

	}

	public NhanVien(String maNV, String tenNV, boolean gioiTinh, String sDT, TrangThaiNhanVien trangThai, String chucVu, LocalDate ngayVaoLam,
					LocalDate ngayNghiLam, LocalDate ngaySinh) {
		super();
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.gioiTinh = gioiTinh;
		this.sDT = sDT;
		this.trangThai = trangThai;
		this.chucVu = chucVu;
		this.ngayVaoLam = ngayVaoLam;
		this.ngayNghiLam = ngayNghiLam;
		this.ngaySinh = ngaySinh;
	}

	public NhanVien() {
		super();
	}

	public NhanVien(String maNV){
		this.maNV = maNV;
	}

	public String getMaNV() {
		return maNV;
	}

	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}

	public String getTenNV() {
		return tenNV;
	}

	public void setTenNV(String tenNV) {
		this.tenNV = tenNV;
	}

	public boolean isGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(boolean gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public String getSDT() {
		return sDT;
	}

	public void setSDT(String sDT) {
		this.sDT = sDT;
	}

	public TrangThaiNhanVien getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(TrangThaiNhanVien trangThai) {
		this.trangThai = trangThai;
	}

	public String getChucVu() {
		return chucVu;
	}

	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}

	public LocalDate getNgayVaoLam() {
		return ngayVaoLam;
	}

	public void setNgayVaoLam(LocalDate ngayVaoLam) {
		this.ngayVaoLam = ngayVaoLam;
	}

	public LocalDate getNgayNghiLam() {
		return ngayNghiLam;
	}

	public void setNgayNghiLam(LocalDate ngayNghiLam) {
		this.ngayNghiLam = ngayNghiLam;
	}

	public LocalDate getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(LocalDate ngaySinh) {
		this.ngaySinh = ngaySinh;
	}

	public String getCauHoi(){
		return question;
	}

	public void setQuestion(String question){
		this.question = question;
	}

	public String getTraLoi() {
		return answer;
	}

	public void setTraLoi(String answer) {
		this.answer = answer;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maNV);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NhanVien other = (NhanVien) obj;
		return Objects.equals(maNV, other.maNV);
	}

	@Override
	public String toString() {
		return "NhanVien [maNV=" + maNV + ", tenNV=" + tenNV + ", gioiTinh=" + gioiTinh + ", sDT=" + sDT
				+ ", trangThai=" + trangThai + ", ngayVaoLam=" + ngayVaoLam + ", ngayNghiLam=" + ngayNghiLam
				+ ", ngaySinh=" + ngaySinh + "]";
	}


	public String getsDT() {
		return sDT;
	}

	public void setsDT(String sDT) {
		this.sDT = sDT;
	}

	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
	}

	public double getDoanhThu() {
		return doanhThu;
	}

	public void setDoanhThu(double doanhThu) {
		this.doanhThu = doanhThu;
	}
	public NhanVien(String maNV, String tenNV, String sDT, TrangThaiNhanVien trangThai, double doanhThu) {
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.sDT = sDT;
		this.trangThai = trangThai;
		this.doanhThu = doanhThu;
	}


	public NhanVien(String tenNV, int soHoaDon, double doanhThu) {
		this.tenNV = tenNV;
		this.soHoaDon= soHoaDon;
		this.doanhThu= doanhThu;
	}
	public int getSoHoaDon() {
		return soHoaDon;
	}
	public void setSoHoaDon(int soHoaDon) {
		this.soHoaDon = soHoaDon;
	}

}
