package control;

import dao.NhanVien_DAO;
import entity.NhanVien;
import gui.DangNhap_GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TrangChu_Control implements Initializable {
    @FXML
    private Button btnDatBan;
    @FXML
    private Button btnThanhToan;
    @FXML
    private Button btnNhanVien;
    @FXML
    private Button btnMonAn;
    @FXML
    private  Button btnThongKe;
    @FXML
    private Pane showPane;
    private Button selectedButton = null;
    @FXML
    private Button btnTenNV;

    private Stage stageTrangChu;
    private NhanVien nv;
    private ThongTinNhanVien_Control thongTinNhanVienControl;
    public void setStageTrangChu(Stage stage) {
        this.stageTrangChu = stage;
    }
    private DatBan_Control datBanControl;
    private String maNV;

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
    public void setNhanvien(String tenNV){
     //   Image view = new Image(getClass().getResourceAsStream("/images/nvdaden.png"));
        Image image = new Image(getClass().getResourceAsStream("/images/nvdaden.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(35); // Điều chỉnh kích thước ảnh (rộng)
        imageView.setFitHeight(35); // Điều chỉnh kích thước ảnh (cao)
        btnTenNV.setGraphic(imageView);
        btnTenNV.setText(tenNV);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnDatBan.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ffffff; -fx-text-fill: #00b6f3;-fx-border-radius: 10;-fx-background-radius: 10");
        nv = SessionManager.getInstance().getCurrentNhanVien();
        if(nv != null){
            System.out.println("Tên nhân viên là: " + nv.getTenNV());
        }else {
            System.out.println("Không tìm thấy nhân viên");
        }
        // Hiển thị panel Thanh Toán và tải nội dung từ ThanhToan.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/DatBan.fxml"));
            Parent DatBanContent = loader.load();
            datBanControl = loader.getController();

            datBanControl.setMaNV(maNV);
            datBanControl.setTrangChuStage(this.stageTrangChu);
            // Xóa các phần tử hiện tại trong pnThanhToan và thêm nội dung mới
            showPane.getChildren().clear();  // Xóa phần tử cũ
            showPane.getChildren().add(DatBanContent);  // Thêm phần tử mới
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Ma nhan vien la:" + maNV);
    }
    @FXML
    public void handleDatBanClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/DatBan.fxml"));
            Parent DatBanContent = loader.load();
            datBanControl = loader.getController();

            datBanControl.setMaNV(maNV);
            datBanControl.setTrangChuStage(this.stageTrangChu);
            // Xóa các phần tử hiện tại trong pnThanhToan và thêm nội dung mới
            showPane.getChildren().clear();  // Xóa phần tử cũ
            showPane.getChildren().add(DatBanContent);  // Thêm phần tử mới
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //check mã quản lý
    public void handleThongKeClick() {
        String ma = nv.getMaNV(); // Lấy mã nhân viên trực tiếp từ đối tượng nv
        NhanVien nvien = NhanVien_DAO.timKiemNhanVien1(ma);

        if (nvien.getChucVu().equalsIgnoreCase("Quản lý")) {
            try {
                Parent cc = FXMLLoader.load(getClass().getResource("/gui/ThongKe.fxml"));
                showPane.getChildren().clear();
                showPane.getChildren().add(cc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Parent cc = FXMLLoader.load(getClass().getResource("/gui/ThongKe_NV.fxml"));
                showPane.getChildren().clear();
                showPane.getChildren().add(cc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleThanhToanClick() {
        try {
            Parent ThongKeContent = FXMLLoader.load(getClass().getResource("/gui/ThanhToan.fxml"));
            showPane.getChildren().clear();
            showPane.getChildren().add(ThongKeContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleMonAnClick() {
        if (nv == null) {Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText("Không có thông tin nhân viên");
            alert.setContentText("Vui lòng đăng nhập lại để tải thông tin nhân viên.");
            alert.showAndWait();
            return;
        }

        String ma = nv.getMaNV(); // Lấy mã nhân viên trực tiếp từ đối tượng nv
        NhanVien nvien = NhanVien_DAO.timKiemNhanVien1(ma);

        if (nvien.getChucVu().equalsIgnoreCase("Quản lý")) {
            try {
                Parent cc = FXMLLoader.load(getClass().getResource("/gui/MonAn.fxml"));
                showPane.getChildren().clear();
                showPane.getChildren().add(cc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lỗi!");
            alert.setHeaderText("Không thể chọn mục này!");
            alert.setContentText("Chỉ quản lý mới có thể vào mục này");
            alert.showAndWait();
        }
    }
    public void handleNhanVienClick() {
        if (nv == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText("Không có thông tin nhân viên");
            alert.setContentText("Vui lòng đăng nhập lại để tải thông tin nhân viên.");
            alert.showAndWait();
            return;
        }

        String ma = nv.getMaNV(); // Lấy mã nhân viên trực tiếp từ đối tượng nv
        NhanVien nvien = NhanVien_DAO.timKiemNhanVien1(ma);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (nvien.getChucVu().equalsIgnoreCase("Quản lý")) {
            try {
                Parent ThongKeContent = FXMLLoader.load(getClass().getResource("/gui/NhanVien.fxml"));
                showPane.getChildren().clear();
                showPane.getChildren().add(ThongKeContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            alert.setTitle("Lỗi!");
            alert.setHeaderText("Không thể chọn mục này!");alert.setContentText("Chỉ quản lý mới cho thể vào mục này");
            alert.showAndWait();
        }
    }

    public void tenNhanVienClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ThongTinNhanVien.fxml"));
            Parent root = loader.load();

            // Lấy controller của ThongTinNhanVien_Control
            thongTinNhanVienControl = loader.getController();
            thongTinNhanVienControl.setStageTrangChu(this.stageTrangChu);

            // Truyền thông tin nhân viên hiện tại vào ThongTinNhanVien_Control
            thongTinNhanVienControl.setNhanVien(this.nv); // truyền `nv`

            Stage thongTinNhanVienStage = new Stage();
            thongTinNhanVienStage.setScene(new Scene(root));
            thongTinNhanVienStage.setTitle("Thông Tin Nhân Viên");
            thongTinNhanVienStage.getIcons().add(new Image(DangNhap_GUI.class.getResourceAsStream("/images/officer.png")));
            thongTinNhanVienStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setNhanvienData(NhanVien nv) {
        this.nv = nv;
        if (thongTinNhanVienControl != null) {
            thongTinNhanVienControl.setNhanVien(nv);
        }
    }
    @FXML
    private void handleMouseEntered(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color: #ffffff; -fx-border-color:#FFFFFF; -fx-text-fill: #00b6f3; -fx-border-radius: 10; -fx-background-radius: 10");
    }

    @FXML
    private void handleMouseExited(MouseEvent event) {
        Button button = (Button) event.getSource();

        // Chỉ quay về màu mặc định nếu nút không phải là nút đã chọn
        if (button != selectedButton) {
            button.setStyle("-fx-background-color: #00BFFF; -fx-border-color: #ffffff; -fx-text-fill: #ffffff; -fx-border-radius: 10;-fx-background-radius: 10");
        }
    }
    @FXML
    private void handleButtonClick(javafx.event.ActionEvent actionEvent) {
        Button clickedButton = (Button) actionEvent.getSource();

        if (selectedButton != null) {
            selectedButton.setStyle("-fx-background-color: #00bfff; -fx-border-color: #ffffff; -fx-text-fill: #fafafa;-fx-border-radius: 10;-fx-background-radius: 10"); // Màu mặc định
        }

        // Đặt màu cho nút hiện tại và gán nó làm `selectedButton`
        clickedButton.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ffffff; -fx-text-fill: #00b6f3;-fx-border-radius: 10;-fx-background-radius: 10");
        selectedButton = clickedButton;

        if (clickedButton == btnMonAn) {
            handleMonAnClick();
        } else if (clickedButton == btnNhanVien) {
            handleNhanVienClick();
        } else if (clickedButton == btnThanhToan) {
            handleThanhToanClick();
        } else if (clickedButton == btnThongKe) {
            handleThongKeClick();
        } else if (clickedButton == btnDatBan) {
            handleDatBanClick();
        }
    }
}