package dao;

import connectDB.ConnectDB;
import entity.ChiTietHD_MonAn;
import entity.HoaDon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ThongKe_NV_DAO {

    public static ObservableList<ChiTietHD_MonAn> getMonAnList1(int day, int month, int year, String maNV) {
        ObservableList<ChiTietHD_MonAn> list = FXCollections.observableArrayList();

        String query = """
            SELECT m.tenMonAn,
                   SUM(ct.soLuong) AS soLuong,
                   SUM(ct.soLuong * m.gia * (1 + m.VAT)) AS doanhThu
            FROM HoaDon hd
            JOIN ChiTietHD_MonAn ct ON hd.maHD = ct.maHD
            JOIN MonAn m ON ct.maMonAn = m.maMonAn
            WHERE (hd.trangThaiHoaDon = 'DA_THANH_TOAN' 
                   OR (hd.trangThaiHoaDon = 'BI_HUY' AND hd.tienCoc > 0))
              AND DAY(hd.ngayDat) = ?
              AND MONTH(hd.ngayDat) = ?
              AND YEAR(hd.ngayDat) = ?
              AND hd.maNV = ?
            GROUP BY m.tenMonAn;
    """;

        try (Connection conn = ConnectDB.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, day);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            pstmt.setString(4, maNV); // Thiết lập giá trị maNV kiểu String

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String tenMonAn = rs.getString("tenMonAn");
                    int soLuong = rs.getInt("soLuong");
                    double doanhThu = rs.getDouble("doanhThu");

                    ChiTietHD_MonAn chiTietHD_monAn = new ChiTietHD_MonAn(tenMonAn, soLuong, doanhThu);
                    list.add(chiTietHD_monAn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static ObservableList<HoaDon> getListDoanhThu7Day(String maNV) {
        ObservableList<HoaDon> danhSachHoaDon = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();

            // Get the current date
            LocalDate today = LocalDate.now();
            LocalDate sevenDaysAgo = today.minusDays(6);

            String query = """
                SELECT hd.ngayDat,
                       SUM(CASE 
                               WHEN hd.trangThaiHoaDon = 'BI_HUY' THEN hd.tienCoc 
                               ELSE hd.tongTien 
                           END) AS tongTien
                FROM HoaDon hd
                WHERE (hd.trangThaiHoaDon = 'DA_THANH_TOAN' OR hd.trangThaiHoaDon = 'BI_HUY')
                  AND hd.ngayDat BETWEEN ? AND ?
                  AND hd.maNV = ?
                GROUP BY hd.ngayDat
                ORDER BY hd.ngayDat
                """;

            // Prepare the SQL statement
            pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, java.sql.Date.valueOf(sevenDaysAgo));
            pstmt.setDate(2, java.sql.Date.valueOf(today));
            pstmt.setString(3, maNV); // Set maNV for the query

            // Execute the query
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LocalDate ngayTaoHD = rs.getDate("ngayDat").toLocalDate();
                double tongTien = rs.getDouble("tongTien");

                HoaDon hoaDon = new HoaDon(ngayTaoHD, tongTien);
                danhSachHoaDon.add(hoaDon);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return danhSachHoaDon;
    }





    public static double getDoanhThuToday(String maNV) {
        double doanhThu = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();

            // Lấy ngày hiện tại
            LocalDate today = LocalDate.now();

            String query = """
                SELECT SUM(CASE 
                               WHEN trangThaiHoaDon = 'BI_HUY' THEN tienCoc 
                               ELSE tongTien 
                           END) AS doanhThu
                FROM HoaDon
                WHERE DAY(ngayDat) = ?
                  AND MONTH(ngayDat) = ?
                  AND YEAR(ngayDat) = ?
                  AND (trangThaiHoaDon = 'DA_THANH_TOAN' OR trangThaiHoaDon = 'BI_HUY')
                  AND maNV = ?
                """;

            // Chuẩn bị câu lệnh SQL với tham số ngày, tháng, năm hiện tại và mã nhân viên
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, today.getDayOfMonth());
            pstmt.setInt(2, today.getMonthValue());
            pstmt.setInt(3, today.getYear());
            pstmt.setString(4, maNV); // Thêm tham số maNV

            // Thực hiện truy vấn
            rs = pstmt.executeQuery();

            if (rs.next()) {
                doanhThu = rs.getDouble("doanhThu");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return doanhThu;
    }


    public static int getHoaDonToday(String maNV) {
        int hoaDon = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();

            // Lấy ngày hiện tại
            LocalDate today = LocalDate.now();

            String query = """
                SELECT COUNT(maHD) AS soHoaDon
                FROM HoaDon
                WHERE DAY(ngayDat) = ?
                  AND MONTH(ngayDat) = ?
                  AND YEAR(ngayDat) = ?
                  AND (trangThaiHoaDon = 'DA_THANH_TOAN' 
                       OR (trangThaiHoaDon = 'BI_HUY' AND tienCoc > 0))
                  AND maNV = ?
                """;

            // Chuẩn bị câu lệnh SQL với tham số ngày, tháng, năm hiện tại và mã nhân viên
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, today.getDayOfMonth());
            pstmt.setInt(2, today.getMonthValue());
            pstmt.setInt(3, today.getYear());
            pstmt.setString(4, maNV); // Thêm tham số maNV

            // Thực hiện truy vấn
            rs = pstmt.executeQuery();

            if (rs.next()) {
                hoaDon = rs.getInt("soHoaDon");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return hoaDon;
    }


    public static int getMonAnCountToday(String maNV) {
        int totalQuantity = 0;

        String query = """
            SELECT SUM(ct.soLuong) AS totalQuantity
            FROM HoaDon hd
            JOIN ChiTietHD_MonAn ct ON hd.maHD = ct.maHD
            WHERE (hd.trangThaiHoaDon = 'DA_THANH_TOAN' 
                   OR (hd.trangThaiHoaDon = 'BI_HUY' AND hd.tienCoc > 0))
              AND DAY(hd.ngayDat) = ?
              AND MONTH(hd.ngayDat) = ?
              AND YEAR(hd.ngayDat) = ?
              AND hd.maNV = ?;
            """;

        try (Connection conn = ConnectDB.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Lấy ngày hiện tại
            LocalDate today = LocalDate.now();

            // Đặt tham số ngày, tháng, năm hiện tại và mã nhân viên vào câu truy vấn
            pstmt.setInt(1, today.getDayOfMonth());
            pstmt.setInt(2, today.getMonthValue());
            pstmt.setInt(3, today.getYear());
            pstmt.setString(4, maNV);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalQuantity = rs.getInt("totalQuantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalQuantity;
    }


    public static int getNewCustomerCountToday(String maNV) {
        int newCustomerCount = 0;

        String query = """
           SELECT COUNT(DISTINCT hd.maKH) AS newCustomerCount
           FROM HoaDon hd
           WHERE CAST(hd.ngayTaoHD AS DATE) = ?
             AND hd.maKH NOT IN (
                 SELECT DISTINCT maKH
                 FROM HoaDon
                 WHERE CAST(ngayTaoHD AS DATE) < ?
             )
             AND hd.maNV = ?;
    """;

        try (Connection conn = ConnectDB.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Lấy ngày hiện tại
            LocalDate today = LocalDate.now();

            // Đặt tham số ngày hiện tại và mã nhân viên vào câu truy vấn
            pstmt.setDate(1, java.sql.Date.valueOf(today));
            pstmt.setDate(2, java.sql.Date.valueOf(today));
            pstmt.setString(3, maNV);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    newCustomerCount = rs.getInt("newCustomerCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newCustomerCount;
    }


}
