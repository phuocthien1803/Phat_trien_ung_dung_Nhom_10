package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.TrangThaiNhanVien;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NhanVien_DAO {
    public static ObservableList<NhanVien> getNhanVienList() {
        ObservableList<NhanVien> list = FXCollections.observableArrayList();  // Tạo một ObservableList để chứa danh sách nhân viên
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query = "SELECT maNV, tenNV, gioiTinh, ngaySinh, sDT, ngayVaoLam, ngayNghiLam, chucVu, trangThai FROM NhanVien";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String maNV = rs.getString("maNV");
                String tenNV = rs.getString("tenNV");
                boolean gioiTinh = rs.getBoolean("gioiTinh");
                Date ngaySinh = rs.getDate("ngaySinh");
                String sDT = rs.getString("sDT");
                Date ngayVaoLam = rs.getDate("NgayVaoLam");
                Date ngayNghiLam = rs.getDate("ngayNghiLam");
                String chucVu = rs.getString("chucVu");
                String trangThai = rs.getString("trangThai");
                TrangThaiNhanVien ttnv = null;
                if(trangThai != null){
                    ttnv = TrangThaiNhanVien.valueOf(trangThai);
                }
                // Kiểm tra nếu ngayNghiLam là null
                LocalDate localNgayNghiLam = null;
                LocalDate localNgaySinh= null;
                if (ngayNghiLam != null) {
                    localNgayNghiLam = ngayNghiLam.toLocalDate();
                }
                if (ngaySinh != null) {
                    localNgaySinh = ngaySinh.toLocalDate();
                }
                NhanVien nv = new NhanVien(maNV, tenNV, gioiTinh, sDT, ttnv, chucVu,ngayVaoLam.toLocalDate(), localNgayNghiLam, localNgaySinh);
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    public boolean themNhanVien(NhanVien nv){
        int n = 0;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            PreparedStatement statement = null;
            String sql = "INSERT INTO NhanVien (maNV, tenNV, gioiTinh, ngaySinh, sDT, ngayVaoLam, ngayNghiLam, chucVu, trangThai, question, answer) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, nv.getMaNV());
            statement.setString(2, nv.getTenNV());
            statement.setBoolean(3, nv.isGioiTinh());
            if (nv.getNgaySinh() != null) {
                statement.setDate(4, java.sql.Date.valueOf(nv.getNgaySinh())); //
            } else {
                statement.setNull(4, Types.DATE); // Nếu ngày sinh là null, thì set null cho cột này
            }
            statement.setString(5, nv.getSDT());
            if (nv.getNgayVaoLam() != null) {
                statement.setDate(6, java.sql.Date.valueOf(nv.getNgayVaoLam()));
            } else {
                statement.setNull(6, Types.DATE); // Nếu ngày sinh là null, thì set null cho cột này
            }
            if (nv.getNgayNghiLam() != null) {
                statement.setDate(7, java.sql.Date.valueOf(nv.getNgayNghiLam()));
            } else {
                statement.setNull(7, Types.DATE); // Nếu ngày sinh là null, thì set null cho cột này
            }
            statement.setString(8, nv.getChucVu());
            statement.setString(9, nv.getTrangThai().name());
            statement.setString(10, nv.getCauHoi());
            statement.setString(11, nv.getTraLoi());
            n = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n>0;
    }
    public int laySoThuTu(LocalDate ngayVaoLam) {
        int soThuTu = 1;

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String sql = "SELECT COUNT(*) FROM NhanVien WHERE ngayVaoLam = ?";
            statement = conn.prepareStatement(sql);
            statement.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            rs = statement.executeQuery(); 
            if (rs.next()) {
                soThuTu = rs.getInt(1) + 1; //Lấy nhân viên trong ngày tăng thêm 1;
            }
        } catch (SQLException e) {  
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return soThuTu;
    }
    public void capNhatNhanVien(NhanVien nv){
        String sql = "UPDATE NhanVien SET maNV = ?, tenNV = ?, gioiTinh = ?, ngaySinh = ?, sDT = ?, ngayVaoLam = ?, ngayNghiLam = ?, chucVu = ?, trangThai = ?, question = ?, answer = ? WHERE maNV = ?";
        try(Connection conn = ConnectDB.connect();
            PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, nv.getMaNV());
            statement.setString(2, nv.getTenNV());
            statement.setBoolean(3, nv.isGioiTinh());
            if (nv.getNgaySinh() != null) {
                statement.setDate(4, java.sql.Date.valueOf(nv.getNgaySinh()));
            } else {
                statement.setNull(4, Types.DATE); // Nếu ngày sinh là null, thì set null cho cột này
            }
            statement.setString(5, nv.getSDT());
            if (nv.getNgayVaoLam() != null) {
                statement.setDate(6, java.sql.Date.valueOf(nv.getNgayVaoLam()));
            } else {
                statement.setNull(6, Types.DATE); // Nếu ngày sinh là null, thì set null cho cột này
            }
            if (nv.getNgayNghiLam() != null) {
                statement.setDate(7, java.sql.Date.valueOf(nv.getNgayNghiLam()));
            } else {
                statement.setNull(7, Types.DATE); // Nếu ngày sinh là null, thì set null cho cột này
            }
            statement.setString(8,nv.getChucVu());
            statement.setString(9, nv.getTrangThai().name());
            statement.setString(10, nv.getCauHoi());
            statement.setString(11, nv.getTraLoi());
            statement.setString(12, nv.getMaNV());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Sửa nhân viên thành công!");
            } else {
                System.out.println("Không có nhân viên nào được cập nhật.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static NhanVien timKiemNhanVien1(String ma){
        NhanVien nv = null;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = ConnectDB.connect();
            String query = "SELECT maNV, tenNV, gioiTinh, ngaySinh, sDT, ngayVaoLam, ngayNghiLam, chucVu, trangThai, question, answer FROM NhanVien WHERE maNV = ?";
            statement = conn.prepareStatement(query);
            statement.setString(1, ma);
            rs = statement.executeQuery();
            if (rs.next()) {
                String maNV = rs.getString("maNV");
                String tenNV = rs.getString("tenNV");
                boolean gioiTinh = rs.getBoolean("gioiTinh");
                Date ngaySinh = rs.getDate("ngaySinh");
                String sDT = rs.getString("sDT");
                Date ngayVaoLam = rs.getDate("ngayVaoLam");
                Date ngayNghiLam = rs.getDate("ngayNghiLam");
                String chucVu = rs.getString("chucVu");
                String trangThai = rs.getString("trangThai");
                String question = rs.getString("question");
                String answer = rs.getString("answer");
                TrangThaiNhanVien ttnv = null;
                if(trangThai != null){
                    ttnv = TrangThaiNhanVien.valueOf(trangThai);
                }
                // Kiểm tra nếu ngayNghiLam là null
                LocalDate localNgayNghiLam = null;
                LocalDate localNgaySinh= null;
                if (ngayNghiLam != null) {
                    localNgayNghiLam = ngayNghiLam.toLocalDate();
                }
                if (ngaySinh != null) {
                    localNgaySinh = ngaySinh.toLocalDate();
                }
                nv = new NhanVien(maNV, tenNV, gioiTinh, sDT, ttnv, chucVu,ngayVaoLam.toLocalDate(), localNgayNghiLam, localNgaySinh, question, answer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return nv;
    }
//    public void capNhatNgayNghi(NhanVien nv){
//        String sql = "UPDATE NhanVien SET ngayNghiLam = ? WHERE maNV = ?";
//        try(Connection conn = ConnectDB.connect();
//            PreparedStatement statement = conn.prepareStatement(sql)){
//            statement.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
//            statement.setString(2, nv.getMaNV());
//            int rowsUpdated = statement.executeUpdate();
//            if (rowsUpdated > 0) {
//                System.out.println("Sửa ngày nghỉ làm thành công!");
//            } else {
//                System.out.println("Không có nhân viên nào được cập nhật.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
