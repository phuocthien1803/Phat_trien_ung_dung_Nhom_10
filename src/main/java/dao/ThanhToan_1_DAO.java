package dao;

import connectDB.ConnectDB;
import entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ThanhToan_1_DAO {
    public static ObservableList<ChiTietHD_MonAn> getHoaDonList1(String maHoaDon) {
        ObservableList<ChiTietHD_MonAn> list = FXCollections.observableArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();  // Sử dụng lớp ConnectDB để kết nối
            String query = "SELECT ROW_NUMBER() OVER (ORDER BY hd.NgayTaoHD) AS STT, " +
                    "m.TenMonAn, m.Gia, ct.SoLuong, m.VAT, ct.ThanhTien " +
                    "FROM HoaDon hd " +
                    "JOIN ChiTietHD_MonAn ct ON hd.MaHD = ct.MaHD " +
                    "JOIN MonAn m ON ct.MaMonAn = m.MaMonAn " +
                    "WHERE hd.MaHD = '" + maHoaDon + "'"; // Thêm điều kiện lọc theo maHoaDon
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                int stt = rs.getInt("STT");
                String tenMonAn = rs.getString("tenMonAn");
                double gia = rs.getDouble("gia");
                int soLuong = rs.getInt("soLuong");
                double vat = rs.getDouble("vAT");
                double thanhTien = rs.getDouble("thanhTien");
                ChiTietHD_MonAn chiTiet = new ChiTietHD_MonAn(tenMonAn, gia, soLuong, vat, thanhTien);
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
    public static HoaDon getHoaDonDetailsByMa(String maHoaDon) {
        HoaDon hoaDon = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query = "SELECT hd.ngayTaoHD, hd.GioDatBan, b.MaBan, hd.TienCoc " +
                    "FROM HoaDon hd " +
                    "JOIN Ban b ON hd.MaBan = b.MaBan " +
                    "WHERE hd.MaHD = '" + maHoaDon + "'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Date ngayTaoHD = rs.getDate("NgayTaoHD");
                Time gioDatBan = rs.getTime("GioDatBan");
                String maBan = rs.getString("MaBan");
                double tienCoc = rs.getDouble("TienCoc");
                hoaDon = new HoaDon(ngayTaoHD.toLocalDate(), gioDatBan.toLocalTime(), maBan,tienCoc);
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
        return hoaDon;
    }
    public static ObservableList<KhachHang> getKhachHangInfo(String maHoaDon) {
        ObservableList<KhachHang> list = FXCollections.observableArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectDB.connect(); // Sử dụng lớp ConnectDB để kết nối
            String query = "SELECT kh.maKH, kh.diemTichLuy " +
                    "FROM HoaDon hd " +
                    "JOIN KhachHang kh ON hd.maKH = kh.maKH " +
                    "WHERE hd.MaHD = '" + maHoaDon + "'"; // Điều kiện lọc theo maHoaDon
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String maKH = rs.getString("maKH");
                int diemTichLuy = rs.getInt("diemTichLuy");
                KhachHang khachHang = new KhachHang(maKH,null,null, diemTichLuy);
                list.add(khachHang);
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
    public static void updateHoaDon(String maHoaDon, double tongTien, int diemTichLuy, String maKhachHang, String maBan, String maKM) {
        Connection conn = null;
        PreparedStatement updateHoaDonStmt = null;
        PreparedStatement updateKhachHangStmt = null;
        PreparedStatement updateBanStmt = null;
        try {
            conn = ConnectDB.connect();
            conn.setAutoCommit(false); // set transsaction 1 cai ko dat dieu kien cung phai huy
            // Update TongTien and TrangThaiHD in HoaDon table
            String updateHoaDonQuery = "UPDATE HoaDon SET TongTien = ?, TrangThaiHoaDon = 'DA_THANH_TOAN', maKM=? WHERE MaHD = ?";
            updateHoaDonStmt = conn.prepareStatement(updateHoaDonQuery);
            updateHoaDonStmt.setDouble(1, tongTien);
            updateHoaDonStmt.setString(2, maKM);
            updateHoaDonStmt.setString(3, maHoaDon);
            updateHoaDonStmt.executeUpdate();

            // Update DiemTichLuy in KhachHang table
            String updateKhachHangQuery = "UPDATE KhachHang " +
                    "SET DiemTichLuy = CASE " +
                    "    WHEN MaKH = 'KH000' THEN 0 " +
                    "    WHEN DiemTichLuy + ? > 500 THEN 500 " +
                    "    ELSE DiemTichLuy + ? " +
                    "END " +
                    "WHERE MaKH = ?";
            updateKhachHangStmt = conn.prepareStatement(updateKhachHangQuery);
            updateKhachHangStmt.setInt(1, diemTichLuy);
            updateKhachHangStmt.setInt(2, diemTichLuy);
            updateKhachHangStmt.setString(3, maKhachHang);
            updateKhachHangStmt.executeUpdate();

            // Update TrangThaiBan in Ban table
            String updateBanQuery = "UPDATE Ban SET TrangThaiBan = 'TRONG' WHERE MaBan = ?";
            updateBanStmt = conn.prepareStatement(updateBanQuery);
            updateBanStmt.setString(1, maBan);
            updateBanStmt.executeUpdate();
            conn.commit(); // ket thuc trans
            System.out.println("Hóa đơn, trạng thái bàn và thông tin liên quan đã được cập nhật thành công.");
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();  // Rollback transaction on failure
                    System.out.println("Giao dịch cập nhật hóa đơn đã bị hủy.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
