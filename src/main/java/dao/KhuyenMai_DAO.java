package dao;

import connectDB.ConnectDB;
import entity.ChiTietHD_MonAn;
import entity.KhuyenMai;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class KhuyenMai_DAO {
    public static ObservableList<KhuyenMai> getKhuyenMai() {
        ObservableList<KhuyenMai> list = FXCollections.observableArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();  // Sử dụng lớp ConnectDB để kết nối
            String query = "SELECT km.maKM, km.tenKM, km.ngayBatDau, km.ngayHetHan, km.phanTramGiam " +
                    "FROM KhuyenMai km " +
                    "WHERE km.ngayHetHan > GETDATE()";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String maKM = rs.getString("maKM");
                String tenKM = rs.getString("tenKM");
                LocalDate ngayBatDau = rs.getDate("ngayBatDau").toLocalDate();
                LocalDate ngayHetHan = rs.getDate("ngayHetHan").toLocalDate();
                Double phanTramGiam = rs.getDouble("phanTramGiam");
                KhuyenMai khuyenMai = new KhuyenMai(maKM, tenKM, ngayBatDau, ngayHetHan, phanTramGiam);
                list.add(khuyenMai);
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

    public static ObservableList<KhuyenMai> getKhuyenMaiListBest() {
        ObservableList<KhuyenMai> list = FXCollections.observableArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query = "SELECT maKM, tenKM, phanTramGiam " +
                    "FROM KhuyenMai " +
                    "WHERE ngayHetHan > GETDATE() " +
                    "ORDER BY phanTramGiam DESC";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String maKM = rs.getString("maKM");
                String tenKM = rs.getString("tenKM");
                double phanTramGiam = rs.getDouble("phanTramGiam");
                KhuyenMai khuyenMai = new KhuyenMai(maKM, tenKM, null, null, phanTramGiam);
                list.add(khuyenMai);
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
}
