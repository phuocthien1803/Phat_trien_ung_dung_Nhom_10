package dao;

import connectDB.ConnectDB;
import entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Ban_DAO {

    public static ObservableList<Ban> getBanList() {
        ObservableList<Ban> list = FXCollections.observableArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query = "SELECT * FROM Ban";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String maBan = rs.getString("maBan");
                int soLuongGhe = rs.getInt("soLuongGhe");
                String trangThai = rs.getString("trangThaiBan");
                TrangThaiBan trangThaiBan = TrangThaiBan.valueOf(trangThai);
                Ban ban = new Ban(maBan, soLuongGhe, trangThaiBan);
                list.add(ban);
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
