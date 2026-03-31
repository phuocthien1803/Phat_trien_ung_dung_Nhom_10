package dao;

import connectDB.ConnectDB;
import entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.nio.DoubleBuffer;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class QuanLyHoaDon_DAO {

    public static ObservableList<HoaDon> getHoaDonList(String banID) {
        ObservableList<HoaDon> list = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query =  "SELECT hd.maHD, " +
                    "kh.SDT, kh.tenKH, hd.NgayTaoHD, hd.NgayDat, hd.GioDatBan, hd.TrangThaiHoaDon, hd.MaNV, kh.diemTichLuy, hd.soLuongKH, hd.hinhThuc, hd.maBan, hd.TongTien, hd.TienCoc, hd.checkIn " +
                    "FROM HoaDon hd " +
                    "JOIN KhachHang kh ON hd.MaKH = kh.MaKH " +
                    "WHERE hd.maBan = ? AND hd.trangThaiHoaDon <> 'BI_HUY'";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, banID);  // Truyền banID vào câu truy vấn

            rs = stmt.executeQuery();

            while (rs.next()) {
                String maHD = rs.getString("maHD");
                String sdt = rs.getString("SDT");
                String tenKhachHang = rs.getString("tenKH");
                sdt = (sdt == null) ? "Không" : sdt;
                Date ngayTaoHD = rs.getDate("NgayTaoHD");
                Date ngayDat = rs.getDate("NgayDat");
                Time gioDatBan = rs.getTime("GioDatBan");
                String trangThaiHoaDonStr = rs.getString("TrangThaiHoaDon");
                TrangThaiHoaDon trangThaiHoaDon = TrangThaiHoaDon.valueOf(trangThaiHoaDonStr);
                String maNV = rs.getString("maNV");
                int diemTL = rs.getInt("diemTichLuy");
                int soLuong = rs.getInt("soLuongKH");
                String hinhThucStr = rs.getString("hinhThuc");
                HinhThuc hinhThuc = HinhThuc.valueOf(hinhThucStr);
                String maBan = rs.getString("maBan");
                Double tongTien = rs.getDouble("tongTien");
                Double tienCoc = rs.getDouble("tienCoc");
                Boolean checkIn = rs.getBoolean("checkIn");
                HoaDon hoaDon = new HoaDon(maHD, sdt, tenKhachHang, ngayTaoHD.toLocalDate(), ngayDat.toLocalDate(), gioDatBan.toLocalTime(),
                        trangThaiHoaDon, maNV, diemTL, soLuong, hinhThuc, maBan, tongTien, tienCoc,checkIn);
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

    public static ObservableList<HoaDon> getAllHoaDonList() {
        ObservableList<HoaDon> list = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query = "SELECT hd.maHD, " +
                    "kh.SDT, kh.tenKH, hd.NgayTaoHD, hd.NgayDat, hd.GioDatBan, hd.TrangThaiHoaDon, hd.MaNV, kh.diemTichLuy, hd.soLuongKH, hd.hinhThuc, hd.maBan, hd.TongTien, hd.TienCoc, hd.checkIn " +
                    "FROM HoaDon hd " +
                    "JOIN KhachHang kh ON hd.MaKH = kh.MaKH " +
                    "WHERE hd.trangThaiHoaDon <> 'BI_HUY'";
            stmt = conn.prepareStatement(query);

            rs = stmt.executeQuery();

            while (rs.next()) {
                String maHD = rs.getString("maHD");
                String sdt = rs.getString("SDT");
                String tenKhachHang = rs.getString("tenKH");
                sdt = (sdt == null) ? "Không" : sdt;
                Date ngayTaoHD = rs.getDate("NgayTaoHD");
                Date ngayDat = rs.getDate("NgayDat");
                Time gioDatBan = rs.getTime("GioDatBan");
                String trangThaiHoaDonStr = rs.getString("TrangThaiHoaDon");
                TrangThaiHoaDon trangThaiHoaDon = TrangThaiHoaDon.valueOf(trangThaiHoaDonStr);
                String maNV = rs.getString("maNV");
                int diemTL = rs.getInt("diemTichLuy");
                int soLuong = rs.getInt("soLuongKH");
                String hinhThucStr = rs.getString("hinhThuc");
                HinhThuc hinhThuc = HinhThuc.valueOf(hinhThucStr);
                String maBan = rs.getString("maBan");
                Double tongTien = rs.getDouble("tongTien");
                Double tienCoc = rs.getDouble("tienCoc");
                Boolean checkIn = rs.getBoolean("checkIn");
                HoaDon hoaDon = new HoaDon(maHD, sdt, tenKhachHang, ngayTaoHD.toLocalDate(), ngayDat.toLocalDate(), gioDatBan.toLocalTime(),
                        trangThaiHoaDon, maNV, diemTL, soLuong, hinhThuc, maBan, tongTien, tienCoc, checkIn);
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


    public void themHoaDon(String maHoaDon, LocalDate ngayTaoHD, String trangThaiHD, LocalDate ngayDat, String ban, String maNV, String maKH,double tienCoc,
                              LocalTime gioDatBan, double tongTien, int soLuongKH, String hinhThuc, boolean checkIn) {
        String sql = "INSERT INTO HoaDon (maHD, ngayTaoHD, trangThaiHoaDon, ngayDat, maBan, maNV, maKH, tienCoc, gioDatBan, tongTien, soLuongKH, hinhThuc, checkIn) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon);
            stmt.setObject(2, ngayTaoHD);
            stmt.setString(3, trangThaiHD);
            stmt.setObject(4, ngayDat);
            stmt.setString(5, ban);
            stmt.setString(6, maNV);
            stmt.setString(7, maKH);
            stmt.setDouble(8, tienCoc);
            stmt.setObject(9, gioDatBan);
            stmt.setDouble(10, tongTien);
            stmt.setInt(11, soLuongKH);
            stmt.setString(12, hinhThuc);
            stmt.setBoolean(13, checkIn);

            stmt.executeUpdate();

            // Thông báo thành công
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành Công");
            alert.setHeaderText("Đặt bàn thành công!");
            alert.setContentText("Thông tin đăt bàn đã được lưu.");
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();

//            // Thông báo lỗi
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Lỗi Lưu Hóa Đơn");
            alert.setContentText("Đã xảy ra lỗi khi lưu hóa đơn. Vui lòng thử lại.");
            alert.showAndWait();
        }
    }

    public int laySoThuTu(LocalDate ngay) {
        int soThuTu = 1;

        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String sql = "SELECT COUNT(*) FROM HoaDon WHERE ngayTaoHD = ?";
            statement = conn.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(ngay));
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
        System.out.println(soThuTu);
        return soThuTu;
    }


    public static ObservableList<HoaDon> getHoaDonList2(String banID, Date ngayDatBan) {
        ObservableList<HoaDon> list = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query =   "SELECT maHD, gioDatBan FROM HoaDon WHERE maBan = ? AND  trangThaiHoaDon = 'CHUA_THANH_TOAN' AND ngayDat = ? ";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, banID);  // Truyền banID vào câu truy vấn
            stmt.setDate(2, ngayDatBan);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String maHD = rs.getString("maHD");
                Time gio = rs.getTime("gioDatBan");
                HoaDon hoaDon = new HoaDon(maHD, gio.toLocalTime());
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

    public void capNhatHoaDon(String maHoaDon, String maKH, String maBan, int soLuongKH, LocalDate ngayDat, LocalTime gioDatBan, double tongTien) {
        String sql = "UPDATE HoaDon SET maKH = ?, maBan = ?, soLuongKH = ?, ngayDat = ?, gioDatBan = ?, tongTien = ? WHERE maHD = ?";

        try (Connection conn = ConnectDB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gán giá trị cho các tham số trong câu lệnh SQL
            stmt.setString(1, maKH);
            stmt.setString(2, maBan);
            stmt.setInt(3, soLuongKH);
            stmt.setDate(4, Date.valueOf(ngayDat));
            stmt.setTime(5, Time.valueOf(gioDatBan));
            stmt.setDouble(6, tongTien);
            stmt.setString(7, maHoaDon);

            // Thực thi câu lệnh UPDATE
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // Có thể ném ngoại lệ nếu cần thiết
            throw new RuntimeException("Lỗi khi cập nhật hóa đơn: " + e.getMessage());
        }
    }

    public static ObservableList<HoaDon> getHoaDonList3(String banID, Date ngayDatBan) {
        ObservableList<HoaDon> list = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query =   "SELECT DISTINCT maHD FROM HoaDon WHERE maBan = ? AND  trangThaiHoaDon = 'CHUA_THANH_TOAN' AND checkIn = 1 AND ngayDat = ? ";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, banID);  // Truyền banID vào câu truy vấn
            stmt.setDate(2, ngayDatBan);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String maHD = rs.getString("maHD");
                HoaDon hoaDon = new HoaDon(maHD);
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

    public void updateCheckIn(String maHD, boolean checkInStatus) {
        String sql = "UPDATE HoaDon SET checkIn = ? WHERE maHD = ?";
        try (Connection conn = ConnectDB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, checkInStatus);
            stmt.setString(2, maHD);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<HoaDon> getHoaDonList4(String ma) {
        ObservableList<HoaDon> list = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query =   "SELECT DISTINCT maHD FROM HoaDon WHERE maHD = ? AND trangThaiHoaDon <> 'BI_HUY' AND checkIn = 1";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, ma);  // Truyền banID vào câu truy vấn

            rs = stmt.executeQuery();

            while (rs.next()) {
                String maHD = rs.getString("maHD");
                HoaDon hoaDon = new HoaDon(maHD);
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

    public void huyHoaDon(String maHoaDon) throws SQLException {
        String sql = "UPDATE HoaDon SET trangThaiHoaDon = ? WHERE maHD = ?";
        try (Connection conn = ConnectDB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "BI_HUY");
            stmt.setString(2, maHoaDon);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Lỗi khi hủy hóa đơn: " + maHoaDon);
        }
    }

    public void capNhatHoaDonBiHuy(String maHD) {
        String sql = "UPDATE HoaDon SET tienCoc = ? WHERE maHD = ?";

        try (Connection conn = ConnectDB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Gán giá trị cho các tham số trong câu lệnh SQL
            stmt.setDouble(1, 0);
            stmt.setString(2, maHD);

            // Thực thi câu lệnh UPDATE
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // Có thể ném ngoại lệ nếu cần thiết
            throw new RuntimeException("Lỗi khi cập nhật hóa đơn: " + e.getMessage());
        }
    }

    public static ObservableList<HoaDon> getHoaDonList5(String banID, Date ngayDatBan, int startHour, int endHour) {
        ObservableList<HoaDon> list = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query =   "SELECT maHD, gioDatBan FROM HoaDon WHERE maBan = ? AND  trangThaiHoaDon = 'CHUA_THANH_TOAN' AND ngayDat = ? AND DATEPART(HOUR, gioDatBan) >= ? AND DATEPART(HOUR, gioDatBan) <= ? ";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, banID);  // Truyền banID vào câu truy vấn
            stmt.setDate(2, ngayDatBan);
            stmt.setInt(3, startHour);
            stmt.setInt(4, endHour);

            rs = stmt.executeQuery();

            while (rs.next()) {
                String maHD = rs.getString("maHD");
                Time gio = rs.getTime("gioDatBan");
                HoaDon hoaDon = new HoaDon(maHD, gio.toLocalTime());
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

    public static ObservableList<HoaDon> getHoaDonList6(String banID, Date ngayDatBan, int startHour, int endHour) {
        ObservableList<HoaDon> list = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query =   "SELECT DISTINCT maHD FROM HoaDon WHERE maBan = ? AND  trangThaiHoaDon = 'CHUA_THANH_TOAN' AND checkIn = 1 AND ngayDat = ? AND DATEPART(HOUR, gioDatBan) >= ? AND DATEPART(HOUR, gioDatBan) <= ? ";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, banID);  // Truyền banID vào câu truy vấn
            stmt.setDate(2, ngayDatBan);
            stmt.setInt(3, startHour);
            stmt.setInt(4, endHour);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String maHD = rs.getString("maHD");
                HoaDon hoaDon = new HoaDon(maHD);
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

    public static int[] demHoaDonChuaThanhToanTheoMaBanNgay(String maBan, Date ngayDat) {
        int soHoaDonCheckInChuaThanhToan = 0;
        int soHoaDonChuaCheckInChuaThanhToan = 0;

        String sql = """
            SELECT 
                SUM(CASE WHEN checkIn = 1 AND trangThaiHoaDon = 'CHUA_THANH_TOAN' THEN 1 ELSE 0 END) AS soHoaDonCheckInChuaThanhToan,
                SUM(CASE WHEN checkIn = 0 AND trangThaiHoaDon = 'CHUA_THANH_TOAN' THEN 1 ELSE 0 END) AS soHoaDonChuaCheckInChuaThanhToan
            FROM HoaDon
            WHERE maBan = ? AND ngayDat = ?
        """;

        try (Connection conn = ConnectDB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maBan);
            stmt.setDate(2, ngayDat);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    soHoaDonCheckInChuaThanhToan = rs.getInt("soHoaDonCheckInChuaThanhToan");
                    soHoaDonChuaCheckInChuaThanhToan = rs.getInt("soHoaDonChuaCheckInChuaThanhToan");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new int[]{soHoaDonCheckInChuaThanhToan, soHoaDonChuaCheckInChuaThanhToan};
    }

    public static int[] demHoaDonChuaThanhToanTheoMaBanNgayGio(String maBan, Date ngayDat, int startHour, int endHour) {
        int soHoaDonCheckInChuaThanhToan = 0;
        int soHoaDonChuaCheckInChuaThanhToan = 0;

        String sql = """
            SELECT 
                SUM(CASE WHEN checkIn = 1 AND trangThaiHoaDon = 'CHUA_THANH_TOAN' THEN 1 ELSE 0 END) AS soHoaDonCheckInChuaThanhToan,
                SUM(CASE WHEN checkIn = 0 AND trangThaiHoaDon = 'CHUA_THANH_TOAN' THEN 1 ELSE 0 END) AS soHoaDonChuaCheckInChuaThanhToan
            FROM HoaDon
            WHERE maBan = ? AND ngayDat = ? AND DATEPART(HOUR, gioDatBan) >= ? AND DATEPART(HOUR, gioDatBan) <= ?
        """;

        try (Connection conn = ConnectDB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maBan);
            stmt.setDate(2, ngayDat);
            stmt.setInt(3, startHour);
            stmt.setInt(4, endHour);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    soHoaDonCheckInChuaThanhToan = rs.getInt("soHoaDonCheckInChuaThanhToan");
                    soHoaDonChuaCheckInChuaThanhToan = rs.getInt("soHoaDonChuaCheckInChuaThanhToan");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new int[]{soHoaDonCheckInChuaThanhToan, soHoaDonChuaCheckInChuaThanhToan};
    }
}
