package control;

import dao.KhachHang_DAO;
import entity.KhachHang;
import entity.MonAn;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
public class ThemKH_Control {


    @FXML
    private AnchorPane paneDatBan;
    @FXML
    private Button btnThem;
    @FXML
    private TextField txtHoTen;

    @FXML
    private TextField txtMaKH;

    @FXML
    private TextField txtSDT;
    // Khởi tạo dữ liệu khi giao diện được load
    public String banID;
    private KhachHang_DAO khachHangDao = new KhachHang_DAO();
    ObservableList<KhachHang> danhSachKH = FXCollections.observableArrayList(khachHangDao.getKhachHangList());
    public void setSDT(String sdt) {
        txtSDT.setText(sdt);
    }
    @FXML
    public void initialize() {
        txtMaKH.setDisable(true);
        String tenKHRegex = "^[A-ZÀ-Ỷ][a-zà-ỹ]+(?: [A-ZÀ-Ỷ][a-zà-ỹ]+)+$";
        String sDTRegex = "^(03|05|07|08|09)\\d{8}$";

        // Kiểm tra tên khách hàng khi thay đổi
        txtHoTen.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {  // Chỉ kiểm tra khi mất focus khỏi ô
                String tenKH = txtHoTen.getText();
                if (!tenKH.matches(tenKHRegex) && !tenKH.isEmpty()) {
                    showAlert("Tên không hợp lệ", "Tên khách hàng phải có ít nhất 2 từ, viết hoa chữ cái đầu và không chứa kí tự đặc biệt.");
                    txtHoTen.requestFocus();
                }
            }
        });

        // Kiểm tra số điện thoại khi thay đổi
        txtSDT.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {  // Chỉ kiểm tra khi mất focus khỏi ô
                String sDT = txtSDT.getText();
                if (!sDT.matches(sDTRegex) && !sDT.isEmpty()) {
                    showAlert("Số điện thoại không hợp lệ", "Số điện thoại phải có 10 số và bắt đầu bằng [03, 05, 07, 08, 09].");
                    txtSDT.requestFocus();

                }
            }
        });
    }
    public void setBanID(String banID) {
        this.banID = banID;
        txtMaKH.setText(phatSinhMaKH());
    }
    @FXML
    private void chonBtnThem() {

        if (txtMaKH.getText().isEmpty() || txtSDT.getText().isEmpty() || txtHoTen.getText().isEmpty()) {
            // Hiển thị thông báo yêu cầu nhập đầy đủ thông tin
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông Báo");
            alert.setHeaderText("Thiếu thông tin");
            alert.setContentText("Vui lòng nhập đầy đủ các thông tin cần thiết.");
            alert.showAndWait();
        } else {
            // Hiển thị hộp thoại xác nhận
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xác Nhận Thêm Khách Hàng");
            confirmAlert.setHeaderText("Bạn có chắc chắn muốn thêm khách hàng?");
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                KhachHang khachHang = findKhachHangBySdt(txtSDT.getText());
                if(khachHang == null) {
                    // Lưu thông tin vào cơ sở dữ liệu
                    String maKH = txtMaKH.getText();
                    String sDT = txtSDT.getText();
                    String tenKH = txtHoTen.getText();
                    int diemTL = 0;
                    KhachHang kh = new KhachHang(maKH, tenKH, sDT, diemTL);
                    khachHangDao.themKhachHang(kh);
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Thành Công");
                    successAlert.setHeaderText("Thêm Khách Hàng Thành Công!");
                    successAlert.setContentText("Thông tin khách hàng đã được lưu.");
                    ButtonType buttonTypeAdd = new ButtonType("Đến đặt bàn");
                    ButtonType buttonTypeClose = new ButtonType("Đóng");
                    successAlert.getButtonTypes().setAll(buttonTypeAdd, buttonTypeClose);
                    successAlert.showAndWait().ifPresent(buttonType -> {
                        if (buttonType == buttonTypeAdd) {
//                clickBtnThemKH();
                            try {
                                System.out.println("hmmm");
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/QuanLyBan.fxml"));
                                Parent DatBanContent = loader.load();

                                // Lấy controller từ FXMLLoader và truyền banID
                                QuanLyBan_Control controller = loader.getController();
                                controller.setBanID(banID);
                                controller.setSDT(txtSDT.getText());
                                paneDatBan.getChildren().clear();
                                paneDatBan.getChildren().add(DatBanContent);
                                paneDatBan.setVisible(true);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            clickBtnThemKH();
                        }
                    });

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Số điện thoại đã tồn tại trong hệ thống.");
                    alert.showAndWait();
                    txtSDT.requestFocus(); // Đưa trỏ chuột trở lại ô nhập số điện thoại
                    return;
                }

            }
        }
    }
    public void dongPane() {
        try {

            Parent DatBanContent = FXMLLoader.load(getClass().getResource("/gui/DatBan.fxml"));
            paneDatBan.getChildren().clear();
            paneDatBan.getChildren().add(DatBanContent);
            paneDatBan.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickBtnHoaDon() {

        try {

            // Tải file FXML và lấy controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/QuanLyHoaDon.fxml"));
            Parent DatBanContent = loader.load();

            // Lấy controller từ FXMLLoader và truyền banID
            QuanLyHoaDon_Control controller = loader.getController();
            controller.setBanID(banID);
            paneDatBan.getChildren().clear();
            paneDatBan.getChildren().add(DatBanContent);
            paneDatBan.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickBtnDatBan() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/QuanLyBan.fxml"));
            Parent DatBanContent = loader.load();

            // Lấy controller từ FXMLLoader và truyền banID
            QuanLyBan_Control controller = loader.getController();
            controller.setBanID(banID);
            paneDatBan.getChildren().clear();
            paneDatBan.getChildren().add(DatBanContent);
            paneDatBan.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void clickBtnThemKH() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ThemKH.fxml"));
            Parent DatBanContent = loader.load();

            // Lấy controller từ FXMLLoader và truyền banID
            ThemKH_Control controller = loader.getController();
            controller.setBanID(banID);
            paneDatBan.getChildren().clear();
            paneDatBan.getChildren().add(DatBanContent);
            paneDatBan.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int demSTT(LocalDate ngay) {
        int stt = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String formattedDate = ngay.format(formatter);
        for(KhachHang kh : danhSachKH) {
            if(kh.getMaKH().contains(formattedDate)) {
                stt = stt + 1;
            }
        }
        return stt;
    }
    private String phatSinhMaKH() {
        // Lấy chữ cái đầu từ banID

        // Lấy ngày hiện tại theo định dạng ddMMyy
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy");
        String currentDate = dateFormat.format(new Date());
        int  stt = demSTT(LocalDate.now()) + 1;

        // Định dạng số thứ tự thành 3 chữ số
        String invoiceSequence = String.format("%03d", stt);

        // Ghép thành mã hóa đơn theo định dạng HDKddmmyyXXX
        return "KH" + currentDate + invoiceSequence;
    }

    private KhachHang findKhachHangBySdt(String sdt) {
        for (KhachHang kh : danhSachKH) {
            if (kh.getsDT().equals(sdt)) {
                return kh;
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }
    // Phương thức hiển thị alert
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
