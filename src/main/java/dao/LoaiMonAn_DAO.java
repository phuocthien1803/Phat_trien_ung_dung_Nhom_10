package dao;

import connectDB.ConnectDB;
import entity.LoaiMonAn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class LoaiMonAn_DAO {
    public static ObservableList<LoaiMonAn> getLoaiMonAnList() {
        ObservableList<LoaiMonAn> list = FXCollections.observableArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query = "SELECT * FROM LoaiMonAn"; // giả sử bạn có bảng LoaiMonAn
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String maLoai = rs.getString("maLoai");
                String tenLoai = rs.getString("tenLoai");
                LoaiMonAn loaiMonAn = new LoaiMonAn(maLoai, tenLoai);
                list.add(loaiMonAn);
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

    public static String getMaLoai(String tenLoai) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String ma = null;

        try {
            conn = ConnectDB.connect();
            String sql = "SELECT maLoai FROM LoaiMonAn WHERE tenLoai = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tenLoai);  // Thiết lập giá trị cho tham số

            rs = stmt.executeQuery();
            if (rs.next()) {
                ma = rs.getString("maLoai");
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

        return ma;
    }
}
