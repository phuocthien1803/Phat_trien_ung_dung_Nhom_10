package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import connectDB.ConnectDB;
import entity.ChiTietHD_MonAn;
import entity.HoaDon;
import entity.NhanVien;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ThongKe_DAO {

    //Tab tổng quan
    public static ObservableList<ChiTietHD_MonAn> getMonAnList(int month, int year) {
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
                AND MONTH(hd.ngayDat) = ?
                AND YEAR(hd.ngayDat) = ?
                GROUP BY m.tenMonAn;
        """;


        try (Connection conn = ConnectDB.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, month);
            pstmt.setInt(2, year);

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


//    public static ObservableList<NhanVien> getListNhanVien(int month, int year) {
//        ObservableList<NhanVien> list = FXCollections.observableArrayList();
//        Connection conn = null;
//        Statement stmt = null;
//        ResultSet rs = null;
//
//        try {
//            conn = ConnectDB.connect();
//
//            // Cập nhật câu truy vấn với điều kiện tháng và năm
//            String query = """
//            SELECT nv.tenNV,
//                   COUNT(hd.maHD) AS soHoaDon,
//                   SUM(hd.tongTien) AS doanhThu
//            FROM NhanVien nv
//            LEFT JOIN HoaDon hd ON nv.maNV = hd.maNV
//            WHERE (hd.trangThaiHoaDon = 'DA_THANH_TOAN'
//                   OR (hd.trangThaiHoaDon = 'BI_HUY' AND hd.tienCoc > 0))
//            AND MONTH(hd.ngayDat) = ?
//            AND YEAR(hd.ngayDat) = ?
//            GROUP BY nv.tenNV;
//        """;
//
//
//            // Chuẩn bị câu lệnh SQL với tham số tháng và năm
//            PreparedStatement pstmt = conn.prepareStatement(query);
//            pstmt.setInt(1, month);
//            pstmt.setInt(2, year);
//
//            rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                String tenNV = rs.getString("tenNV");
//                int soHoaDon = rs.getInt("soHoaDon");
//                double doanhThu = rs.getDouble("doanhThu");
//
//                NhanVien nhanVien= new NhanVien(tenNV,soHoaDon,doanhThu);
//
//                list.add(nhanVien);
//            }
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (stmt != null) stmt.close();
//                if (conn != null) conn.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return list;
//    }


    public static ObservableList<NhanVien> getListNhanVien(int month, int year) {
        ObservableList<NhanVien> list = FXCollections.observableArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();

            // Cập nhật câu truy vấn với điều kiện tháng và năm
            String query = """
        SELECT TOP 3 nv.tenNV,
               COUNT(hd.maHD) AS soHoaDon,
               CASE
                   WHEN COALESCE(SUM(hd.tongTien), 0) = 0 THEN 1
                   ELSE COALESCE(SUM(hd.tongTien), 0)
               END AS doanhThu
        FROM NhanVien nv
        LEFT JOIN HoaDon hd ON nv.maNV = hd.maNV
          AND (hd.trangThaiHoaDon = 'DA_THANH_TOAN' OR (hd.trangThaiHoaDon = 'BI_HUY' AND hd.tienCoc > 0))
          AND MONTH(hd.ngayDat) = ?
          AND YEAR(hd.ngayDat) = ?
        WHERE nv.chucVu = 'Nhân viên'
        GROUP BY nv.tenNV
        ORDER BY doanhThu desc
        """;

            // Chuẩn bị câu lệnh SQL với tham số tháng và năm
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                String tenNV = rs.getString("tenNV");
                int soHoaDon = rs.getInt("soHoaDon");
                double doanhThu = rs.getDouble("doanhThu");

                // Nếu doanh thu == 0, thay thế thành 1
                if (doanhThu == 0) {
                    doanhThu = 1;
                }

                // Tạo đối tượng NhanVien và thêm vào danh sách
                NhanVien nhanVien = new NhanVien(tenNV, soHoaDon, doanhThu);
                System.out.println(tenNV);
                System.out.println(doanhThu);
                list.add(nhanVien);
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





    public static ObservableList<HoaDon> getListDoanhThuNam(int year) {
        ObservableList<HoaDon> danhSachHoaDon = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();

            String query = "SELECT ngayDat, " +
                    "       CASE " +
                    "           WHEN trangThaiHoaDon = 'BI_HUY' THEN tienCoc " +
                    "           ELSE tongTien " +
                    "       END AS tongTien " +
                    "FROM HoaDon " +
                    "WHERE YEAR(ngayDat) = ? " +
                    "  AND (trangThaiHoaDon = 'DA_THANH_TOAN' OR trangThaiHoaDon = 'BI_HUY')";


            // Chuẩn bị câu lệnh SQL với tham số năm
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, year);

            // Thực hiện truy vấn
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Lấy dữ liệu từ ResultSet và tạo đối tượng HoaDon
                LocalDate ngayDat = rs.getDate("ngayDat").toLocalDate();
                double tongTien = rs.getDouble("tongTien");

                HoaDon hoaDon = new HoaDon(ngayDat, tongTien);

                // Thêm đối tượng HoaDon vào danh sách
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


    public static double getDoanhThuThang(int month, int year) {
        double doanhThu = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();

            String query = "SELECT SUM(CASE " +
                    "              WHEN trangThaiHoaDon = 'BI_HUY' THEN tienCoc " +
                    "              ELSE tongTien " +
                    "           END) AS doanhThu " +
                    "FROM HoaDon " +
                    "WHERE MONTH(ngayDat) = ? " +
                    "  AND YEAR(ngayDat) = ? " +
                    "  AND (trangThaiHoaDon = 'DA_THANH_TOAN' OR trangThaiHoaDon = 'BI_HUY')";

            // Chuẩn bị câu lệnh SQL với tham số tháng và năm
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);

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



    public static int getHoaDonThang(int month, int year) {
        int hoaDon = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();

            String query = """
            SELECT count(maHD) AS soHoaDon 
            FROM HoaDon 
            WHERE (MONTH(ngayDat) = ? 
                   AND YEAR(ngayDat) = ? 
                   AND trangThaiHoaDon = 'DA_THANH_TOAN')
               OR (trangThaiHoaDon = 'BI_HUY' AND tienCoc > 0);
        """;


            // Chuẩn bị câu lệnh SQL với tham số tháng và năm
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);

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

    public static double getDoanhThuNam( int year) {
        double doanhThu = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();

            String query = "SELECT SUM(CASE " +
                    "              WHEN trangThaiHoaDon = 'BI_HUY' THEN tienCoc " +
                    "              ELSE tongTien " +
                    "           END) AS doanhThu " +
                    "FROM HoaDon " +
                    "WHERE YEAR(ngayDat) = ? " +
                    "  AND (trangThaiHoaDon = 'DA_THANH_TOAN' OR trangThaiHoaDon = 'BI_HUY')";


            // Chuẩn bị câu lệnh SQL với tham số tháng và năm
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, year);

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

    // Tab cụ thể
    public static ObservableList<ChiTietHD_MonAn> getMonAnList1(LocalDate date) {
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
                GROUP BY m.tenMonAn;
        """;


        try (Connection conn = ConnectDB.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, date.getDayOfMonth());
            pstmt.setInt(2, date.getMonthValue());
            pstmt.setInt(3, date.getYear());

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

    public static ObservableList<HoaDon> getListDoanhThu7Day(LocalDate day) {
        ObservableList<HoaDon> danhSachHoaDon = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();


            LocalDate sevenDaysAgo = day.minusDays(6);

            String query = "SELECT hd.ngayDat,  " +
                    "       SUM(CASE  " +
                    "               WHEN hd.trangThaiHoaDon = 'BI_HUY' THEN hd.tienCoc  " +
                    "               ELSE hd.tongTien  " +
                    "           END) AS tongTien " +
                    "FROM HoaDon hd " +
                    "WHERE (hd.trangThaiHoaDon = 'DA_THANH_TOAN' OR hd.trangThaiHoaDon = 'BI_HUY') " +
                    "  AND hd.ngayDat BETWEEN ? AND ? " +
                    "GROUP BY hd.ngayDat " +
                    "ORDER BY hd.ngayDat";


            // Prepare the SQL statement
            pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, Date.valueOf(sevenDaysAgo));
            pstmt.setDate(2, Date.valueOf(day));

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




    public static double getDoanhThuDay(LocalDate date) {
        double doanhThu = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();



            String query = "SELECT SUM(CASE " +
                    "              WHEN trangThaiHoaDon = 'BI_HUY' THEN tienCoc " +
                    "              ELSE tongTien " +
                    "           END) AS doanhThu " +
                    "FROM HoaDon " +
                    "WHERE DAY(ngayDat) = ? " +
                    "  AND MONTH(ngayDat) = ? " +
                    "  AND YEAR(ngayDat) = ? " +
                    "  AND (trangThaiHoaDon = 'DA_THANH_TOAN' OR trangThaiHoaDon = 'BI_HUY')";


            // Chuẩn bị câu lệnh SQL với tham số ngày, tháng và năm hiện tại
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, date.getDayOfMonth());
            pstmt.setInt(2, date.getMonthValue());
            pstmt.setInt(3, date.getYear());

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
    public static int getHoaDonDay(LocalDate date) {
        int hoaDon = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();



            String query = "SELECT COUNT(maHD) AS soHoaDon " +
                    "FROM HoaDon " +
                    "WHERE DAY(ngayDat) = ? " +
                    "  AND MONTH(ngayDat) = ? " +
                    "  AND YEAR(ngayDat) = ? " +
                    "  AND (trangThaiHoaDon = 'DA_THANH_TOAN' " +
                    "       OR (trangThaiHoaDon = 'BI_HUY' AND tienCoc > 0))";


            // Chuẩn bị câu lệnh SQL với tham số ngày, tháng và năm hiện tại
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, date.getDayOfMonth());
            pstmt.setInt(2, date.getMonthValue());
            pstmt.setInt(3, date.getYear());

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

    public static int getMonAnCountDay(LocalDate date) {
        int totalQuantity = 0;

        String query = """
            SELECT SUM(ct.soLuong) AS totalQuantity
            FROM HoaDon hd
            JOIN ChiTietHD_MonAn ct ON hd.maHD = ct.maHD
            WHERE (hd.trangThaiHoaDon = 'DA_THANH_TOAN' 
                   OR (hd.trangThaiHoaDon = 'BI_HUY' AND hd.tienCoc > 0))
            AND DAY(hd.ngayDat) = ?
            AND MONTH(hd.ngayDat) = ?
            AND YEAR(hd.ngayDat) = ?;
    """;


        try (Connection conn = ConnectDB.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {



            // Đặt tham số ngày, tháng, và năm hiện tại vào câu truy vấn
            pstmt.setInt(1, date.getDayOfMonth());
            pstmt.setInt(2, date.getMonthValue());
            pstmt.setInt(3, date.getYear());

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

    public static int getNewCustomerCountDay(LocalDate date) {
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
    """;

        try (Connection conn = ConnectDB.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {



            // Đặt tham số ngày hiện tại vào câu truy vấn
            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setDate(2, Date.valueOf(date));

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
