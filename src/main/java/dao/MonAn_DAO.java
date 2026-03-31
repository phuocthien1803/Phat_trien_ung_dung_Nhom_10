package dao;

import connectDB.ConnectDB;
import entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class MonAn_DAO {
    public static ObservableList<MonAn> getMonAnList() {
        ObservableList<MonAn> list = FXCollections.observableArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query = "SELECT * FROM MonAn";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String maMon = rs.getString("maMonAn");
                String tenMon = rs.getString("tenMonAn");
                double gia = rs.getDouble("gia");
                String trangThai = rs.getString("trangThaiMonAn");
                TrangThaiMonAn ttMA = null;
                if(trangThai != null){
                    ttMA = TrangThaiMonAn.valueOf(trangThai);
                }
                double vat = rs.getDouble("VAT");
                String maLoai = rs.getString("maLoai");
                String tenLoai = rs.getString("tenLoai");
                String hinh = rs.getString("hinhAnh");

                LoaiMonAn lm = new LoaiMonAn(maLoai, tenLoai);
                MonAn ma = new MonAn(maMon, tenMon, lm, gia, ttMA, vat, hinh);
                list.add(ma);
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

    public static ObservableList<MonAn> getMonAnListSanCo() {
        ObservableList<MonAn> list = FXCollections.observableArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query = "SELECT * FROM MonAn WHERE trangThaiMonAn = 'SANCO' ";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String maMon = rs.getString("maMonAn");
                String tenMon = rs.getString("tenMonAn");
                double gia = rs.getDouble("gia");
                String trangThai = rs.getString("trangThaiMonAn");
                TrangThaiMonAn ttMA = null;
                if(trangThai != null){
                    ttMA = TrangThaiMonAn.valueOf(trangThai);
                }
                double vat = rs.getDouble("VAT");
                String maLoai = rs.getString("maLoai");
                String tenLoai = rs.getString("tenLoai");
                String hinh = rs.getString("hinhAnh");

                LoaiMonAn lm = new LoaiMonAn(maLoai, tenLoai);
                MonAn ma = new MonAn(maMon, tenMon, lm, gia, ttMA, vat, hinh);
                list.add(ma);
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

    public boolean themMonAn(MonAn ma){
        int n = 0;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            PreparedStatement statement = null;
            String sql = "INSERT INTO MonAn (maMonAn, tenMonAn, gia, trangThaiMonAn, VAT, maLoai, tenLoai, hinhAnh) VALUES(?,?,?,?,?,?,?,?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, ma.getMaMonAn());
            statement.setString(2, ma.getTenMonAn());
            statement.setDouble(3, ma.getGia());
            statement.setString(4,ma.getTrangThaiMonAn().name());
            statement.setDouble(5, ma.getVAT());
            statement.setString(6, ma.getMaLoai());
            statement.setString(7, ma.getTenLoai());
            statement.setString(8, ma.getHinhAnh());
            n = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n>0;
    }
    public static int laySoThuTu(String maLoai) {
        int soThuTu = 1;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String sql = "SELECT COUNT(*) FROM MonAn WHERE maLoai = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, maLoai);
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
    public void capNhatMonAn(MonAn mon){
        String sql = "UPDATE MonAn SET maMonAn = ?, tenMonAn = ?, gia = ?, trangThaiMonAn = ?, VAT = ?, maLoai = ?, tenLoai = ?, hinhAnh = ? WHERE maMonAN = ?";
        try(Connection conn = ConnectDB.connect();
            PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, mon.getMaMonAn());
            statement.setString(2, mon.getTenMonAn());
            statement.setDouble(3, mon.getGia());
            statement.setString(4, mon.getTrangThaiMonAn().name());
            statement.setDouble(5, mon.getVAT());
            statement.setString(6, mon.getMaLoai());
            statement.setString(7, mon.getTenLoai());
            statement.setString(8, mon.getHinhAnh());
            statement.setString(9, mon.getMaMonAn());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Sửa món ăn thành công!");
            } else {
                System.out.println("Không có món ăn nào được cập nhật.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ObservableList<MonAn> timKiemMonAn(String ten){
        ObservableList<MonAn> list = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query = "SELECT maMonAn, tenMonAn, gia, trangThaiMonAn, VAT, maLoai, tenLoai, hinhAnh FROM MonAn WHERE tenMonAn LIKE CONCAT('%',?,'%')";
            statement = conn.prepareStatement(query);
            statement.setString(1, ten);
            rs = statement.executeQuery();
            while (rs.next()) {
                String maMon = rs.getString("maMonAn");
                String tenMon = rs.getString("tenMonAn");
                double gia = rs.getDouble("gia");
                String trangThai = rs.getString("trangThaiMonAn");
                TrangThaiMonAn ttMA = null;
                if(trangThai != null){
                    ttMA = TrangThaiMonAn.valueOf(trangThai);
                }
                double vat = rs.getDouble("VAT");
                String maLoai = rs.getString("maLoai");
                String tenLoai = rs.getString("tenLoai");
                String hinh = rs.getString("hinhAnh");

                LoaiMonAn lm = new LoaiMonAn(maLoai, tenLoai);
                MonAn mon = new MonAn(maMon, tenMon, lm, gia, ttMA, vat, hinh);
                list.add(mon);
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
        return list;
    }
}
