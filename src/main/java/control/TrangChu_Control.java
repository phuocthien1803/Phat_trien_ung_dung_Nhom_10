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
    private static final String MENU_BUTTON_DEFAULT_STYLE =
            "-fx-background-color: #8B0000; -fx-border-color: #D4AF37; -fx-text-fill: #F5F5F5; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10";
    private static final String MENU_BUTTON_ACTIVE_STYLE =
            "-fx-background-color: #F5F5F5; -fx-border-color: #D4AF37; -fx-text-fill: #8B0000; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10";
    
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
    
    // Khai báo thêm 2 nút mới
    @FXML
    private Button btnHoTro;
    @FXML
    private Button btnChatBot;

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
        Image image = new Image(getClass().getResourceAsStream("/images/nvdaden.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(35); // Điều chỉnh kích thước ảnh (rộng)
        imageView.setFitHeight(35); // Điều chỉnh kích thước ảnh (cao)
        btnTenNV.setGraphic(imageView);
        btnTenNV.setText(tenNV);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMenuButtonDefault(btnDatBan);
        setMenuButtonDefault(btnThanhToan);
        setMenuButtonDefault(btnThongKe);
        setMenuButtonDefault(btnNhanVien);
        setMenuButtonDefault(btnMonAn);
        
        // Thêm màu mặc định cho 2 nút mới
        setMenuButtonDefault(btnHoTro);
        setMenuButtonDefault(btnChatBot);

        setMenuButtonActive(btnDatBan);
        selectedButton = btnDatBan;
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
            showPane.getChildren().clear(); 
            showPane.getChildren().add(DatBanContent); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //check mã quản lý
    public void handleThongKeClick() {
        String ma = nv.getMaNV(); 
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
        if (nv == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText("Không có thông tin nhân viên");
            alert.setContentText("Vui lòng đăng nhập lại để tải thông tin nhân viên.");
            alert.showAndWait();
            return;
        }

        String ma = nv.getMaNV(); 
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

        String ma = nv.getMaNV(); 
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
            alert.setHeaderText("Không thể chọn mục này!");
            alert.setContentText("Chỉ quản lý mới cho thể vào mục này");
            alert.showAndWait();
        }
    }

    // Xử lý nút Hỗ trợ
    public void handleHoTroClick() {
        try {
            Parent hoTroContent = FXMLLoader.load(getClass().getResource("/gui/HoTro.fxml"));
            showPane.getChildren().clear();
            showPane.getChildren().add(hoTroContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Xử lý nút Chatbot
    public void handleChatBotClick() {
        try {
            Parent chatBotContent = FXMLLoader.load(getClass().getResource("/gui/ChatBot.fxml"));
            showPane.getChildren().clear();
            showPane.getChildren().add(chatBotContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tenNhanVienClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ThongTinNhanVien.fxml"));
            Parent root = loader.load();

            thongTinNhanVienControl = loader.getController();
            thongTinNhanVienControl.setStageTrangChu(this.stageTrangChu);
            thongTinNhanVienControl.setNhanVien(this.nv); 

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
        setMenuButtonActive((Button) event.getSource());
    }

    @FXML
    private void handleMouseExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        if (button != selectedButton) {
            setMenuButtonDefault(button);
        }
    }
    
    @FXML
    private void handleButtonClick(javafx.event.ActionEvent actionEvent) {
        Button clickedButton = (Button) actionEvent.getSource();

        if (selectedButton != null) {
            setMenuButtonDefault(selectedButton);
        }

        setMenuButtonActive(clickedButton);
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
        } else if (clickedButton == btnHoTro) {
            handleHoTroClick();
        } else if (clickedButton == btnChatBot) {
            handleChatBotClick();
        }
    }

    private void setMenuButtonDefault(Button button) {
        button.setStyle(MENU_BUTTON_DEFAULT_STYLE);
    }

    private void setMenuButtonActive(Button button) {
        button.setStyle(MENU_BUTTON_ACTIVE_STYLE);
    }
}