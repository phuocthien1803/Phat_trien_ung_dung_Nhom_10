package dao;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.TaiKhoan;
import entity.TrangThaiNhanVien;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class TaiKhoan_DAO {
    public boolean taoTaiKhoan(TaiKhoan tk){
        int n = 0;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            PreparedStatement statement = null;
            String sql = "INSERT INTO TaiKhoan (tenDangNhap, matKhau) VALUES(?,?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, tk.getTenDangNhap().getMaNV());
            statement.setString(2, tk.getMatKhau());
            n = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n>0;
    }
    public TaiKhoan timKiemTaiKhoan(String ma){
        TaiKhoan tk = null;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ?";
            statement = conn.prepareStatement(query);
            statement.setString(1, ma);
            rs = statement.executeQuery();
            if (rs.next()) {
                String ten = rs.getString("tenDangNhap");
                String mk = rs.getString("matKhau");
                NhanVien nv = new NhanVien(ten);
                tk = new TaiKhoan(nv,mk);
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
        return tk;
    }
    public void doiMatKhau(TaiKhoan tk){
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectDB.connect();
            String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE tenDangNhap = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tk.getMatKhau());
            stmt.setString(2, tk.getTenDangNhap().getMaNV());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Thành công!");
            } else {
                System.out.println("Thất bại!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String TimKiemMK(String ma){
        String mk = "";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = ConnectDB.connect();
            String sql = "SELECT matKhau FROM TaiKhoan WHERE tenDangNhap = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, ma);
            rs = statement.executeQuery();
            if(rs.next()){
                mk = rs.getString("matKhau");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mk;
    }
}
