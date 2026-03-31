package dao;

import connectDB.ConnectDB;
import entity.HoaDon;
import entity.KhachHang;
import entity.MonAn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;


public class KhachHang_DAO {


    public ObservableList<KhachHang> getKhachHangList()  {

        ObservableList<KhachHang> list = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.connect();
            String query = "SELECT sDT, tenKH, diemTichLuy, maKH FROM KhachHang";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String sdt = rs.getString("sDT");
                sdt = (sdt == null) ? "Không" : sdt;
                String tenKH = rs.getString("tenKH");
                int diemTichLuy = rs.getInt("diemTichLuy");
                String maKH = rs.getString("maKH");
                KhachHang khachHang = new KhachHang(sdt, tenKH, diemTichLuy, maKH);
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

    public void themKhachHang(KhachHang khachHang) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "INSERT INTO KhachHang (maKH, tenKH, sDT, diemTichLuy) VALUES (?, ?, ?, ?)";

        try {
            // Kết nối đến cơ sở dữ liệu
            conn = ConnectDB.connect();
            stmt = conn.prepareStatement(sql);

            // Thiết lập giá trị cho các tham số
            stmt.setString(1, khachHang.getMaKH());
            stmt.setString(2, khachHang.getTenKH());
            stmt.setString(3, khachHang.getsDT());
            stmt.setInt(4, khachHang.getDiemTichLuy());

            // Thực thi câu lệnh
            stmt.executeUpdate();
            // Thông báo thành công

        } catch (SQLException e) {
            System.err.println("Thêm khách hàng thất bại!");
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Lỗi Lưu Hóa Đơn");
                alert.setContentText("Đã xảy ra lỗi khi lưu hóa đơn. Vui lòng thử lại.");
                alert.showAndWait();
            }
        }
    }

//    public static ObservableList<KhachHang> getDSKH() {
//        ObservableList<KhachHang> list = FXCollections.observableArrayList();
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//
//        try {
//            conn = ConnectDB.connect();
//            String query =   "SELECT DISTINCT maKH FROM KhachHang";
//            stmt = conn.prepareStatement(query);
//            rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                String maKH = rs.getString("maKH");
//                KhachHang kh = new KhachHang(maKH);
//                list.add(kh);
//            }
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
//
//        return list;
//    }
}

