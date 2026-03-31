package dao;

import connectDB.ConnectDB;
import entity.ChiTietHD_MonAn;
import entity.HinhThuc;
import entity.HoaDon;
import entity.TrangThaiHoaDon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;


public class ChiTietHD_MonAn_DAO {

    public void themChiTietHD_MonAn(String maHoaDon, String maMonAn, int soLuong,double thanhTien, String ghiChu) {
        String sql = "INSERT INTO ChiTietHD_MonAn(maHD, maMonAn, soLuong, thanhTien, ghiChu) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon);
            stmt.setString(2, maMonAn);
            stmt.setInt(3, soLuong);
            stmt.setDouble(4, thanhTien);
            stmt.setString(5, ghiChu);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public ObservableList<ChiTietHD_MonAn> getChiTietHD(String maHD) {
        ObservableList<ChiTietHD_MonAn> list = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
                conn = ConnectDB.connect();
        String query =  "SELECT hd.tenMonAn, hd.gia, ct.soLuong, ct.thanhTien, hd.VAT, ct.maHD, ct.ghiChu, h.tongTien, h.tienCoc " +
                "FROM MonAn hd " +
                "JOIN ChiTietHD_MonAn ct ON hd.maMonAn = ct.maMonAn " +
                "JOIN HoaDon h ON ct.maHD = h.maHD " + // Kết hợp với bảng HoaDon
                "WHERE ct.maHD = ?";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, maHD);  // Truyền banID vào câu truy vấn
        rs = stmt.executeQuery();
            while (rs.next()) {
                String tenMon = rs.getString("tenMonAn");
                Double gia = rs.getDouble("gia");
                int soLuong = rs.getInt("soLuong");
                String ghiChu = rs.getString("ghiChu"); // Đảm bảo rằng ghiChu đã được chọn
                Double thanhTien = rs.getDouble("thanhTien");
                Double vat = rs.getDouble("VAT");
                String ma = rs.getString("maHD");
                Double tongTien = rs.getDouble("tongTien"); // Lấy thông tin tổng tiền
                Double tienCoc = rs.getDouble("tienCoc");
                ChiTietHD_MonAn chiTiet = new ChiTietHD_MonAn(tenMon, gia, soLuong, ghiChu, thanhTien, vat, ma, tongTien, tienCoc);
                list.add(chiTiet);
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

    public void deleteAllByMaHD(String maHD) throws SQLException {
        String sql = "DELETE FROM ChiTietHD_MonAn WHERE maHD = ?";
        try (Connection conn = ConnectDB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maHD);
            stmt.executeUpdate();
        }
    }
}
