package dao;

import connectDB.ConnectDB;
import entity.Ban;
import entity.HoaDon;
import entity.KhachHang;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ThanhToan_DAO {
    public static ObservableList<HoaDon> getHoaDonList() {
        ObservableList<HoaDon> list = FXCollections.observableArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query = "SELECT hd.maHD, " +
                    "ROW_NUMBER() OVER (ORDER BY hd.NgayTaoHD) AS STT, " +
                    "kh.tenKH, kh.SDT, hd.GioDatBan, b.MaBan " +
                    "FROM HoaDon hd " +
                    "JOIN KhachHang kh ON hd.MaKH = kh.MaKH " +
                    "JOIN Ban b ON hd.MaBan = b.MaBan " +
                    "WHERE hd.trangThaiHoaDon = 'CHUA_THANH_TOAN' " +
                    "AND hd.checkIn = 1";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String maHD = rs.getString("maHD");
                int stt = rs.getInt("STT");
                String tenKhachHang = rs.getString("tenKH");
                String sdt = rs.getString("SDT");
                sdt = (sdt == null) ? "Không" : sdt;
                Time gioDatBan = rs.getTime("GioDatBan");
                String maBan = rs.getString("MaBan");
                HoaDon hoaDon = new HoaDon(maHD, tenKhachHang, sdt, gioDatBan.toLocalTime(), maBan);
                list.add(hoaDon);
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
