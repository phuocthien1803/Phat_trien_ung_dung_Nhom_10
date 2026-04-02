package control;

import java.io.IOException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import entity.NhanVien;
import entity.TaiKhoan;
import gui.DangNhap_GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DangNhap_Control {
    @FXML
    ImageView hide;
    @FXML
    PasswordField password;
    @FXML
    TextField textFieldMK;
    @FXML
    private TextField textFieldDN;
    @FXML
    private Button btnDangNhap;
    @FXML
    private Button btnLamMoi;
    @FXML
    private Label lblErr;
    Image view = new Image(getClass().getResourceAsStream("/images/view.png"));
    Image hidee = new Image(getClass().getResourceAsStream("/images/hide.png"));
    private boolean isPasswordVisible = false;
    public TrangChu_Control trangChuControl;
    private QuenMatKhau_Control quenMatKhauControl;
//    public void loadQLB(String maNV) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/QuanLyBan.fxml"));
//            Parent root = loader.load();
//
//            // Lấy controller của `c.fxml` và truyền `maNV`
//            QuanLyBan_Control cController = loader.getController();
//            cController.setMaNV(maNV);
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void initialize() {
        // Đồng bộ giá trị giữa PasswordField và TextField
        textFieldMK.textProperty().bindBidirectional(password.textProperty());
        textFieldMK.setManaged(false);
        textFieldMK.setVisible(false);
        textFieldDN.setOnAction(this::dangNhap); // Xử lý nhấn Enter trên TextField tên đăng nhập
        password.setOnAction(this::dangNhap); // Xử lý nhấn Enter trên PasswordField

        btnDangNhap.setOnMouseEntered(event -> btnDangNhap.setStyle("-fx-background-color: #A11212; -fx-text-fill: #F5F5F5; -fx-border-color: #D4AF37;"));
        btnDangNhap.setOnMouseExited(event -> btnDangNhap.setStyle("-fx-background-color: #8B0000; -fx-text-fill: #F5F5F5; -fx-border-color: #D4AF37;"));

        btnLamMoi.setOnMouseEntered(event -> btnLamMoi.setStyle("-fx-background-color: #A11212; -fx-text-fill: #F5F5F5; -fx-border-color: #D4AF37;"));
        btnLamMoi.setOnMouseExited(event -> btnLamMoi.setStyle("-fx-background-color: #8B0000; -fx-text-fill: #F5F5F5; -fx-border-color: #D4AF37;"));
    }
    public void hienThiMK() {
        if(isPasswordVisible) {
            hide.setImage(hidee);
            textFieldMK.setVisible(false);
            textFieldMK.setManaged(false);
            password.setVisible(true);
            password.setManaged(true);
        } else
        {
            hide.setImage(view);
             textFieldMK.setVisible(true);
            textFieldMK.setManaged(true);
            password.setVisible(false);
            password.setManaged(false);
        }

        isPasswordVisible = !isPasswordVisible;
    }

    public void lamMoi() {
        password.clear();
        textFieldMK.clear();
        textFieldDN.clear();
    }

    public void dangNhap(ActionEvent actionEvent) {
        String tenDN = textFieldDN.getText();
        String matKhau = textFieldMK.getText();
        NhanVien nv = NhanVien_DAO.timKiemNhanVien1(tenDN);
        TaiKhoan_DAO dao = new TaiKhoan_DAO();
        TaiKhoan tk = dao.timKiemTaiKhoan(tenDN);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        SessionManager.getInstance().setCurrentNhanVien(nv);

        // Khởi tạo BCryptPasswordEncoder để kiểm tra mật khẩu
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(matKhau + "   " + tk.getMatKhau());
        
        if (tk != null) { // So sánh mật khẩu đã nhập với hash đã lưu
            	lblErr.setText("");

            // Mở giao diện trang chủ sau khi đăng nhập thành công
            Stage stage = (Stage) textFieldDN.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/TrangChu.fxml"));
            try {
                Parent root = loader.load();
                TrangChu_Control trangChuControl = loader.getController();
                trangChuControl.setMaNV(tenDN);
                trangChuControl.setNhanvien(nv.getTenNV());
                trangChuControl.setNhanvienData(nv);

                Stage trangChuStage = new Stage();
                trangChuControl.setStageTrangChu(trangChuStage);
                trangChuStage.setScene(new Scene(root));
                trangChuStage.setTitle("Nhà Hàng KKTS");
                trangChuStage.getIcons().add(new Image(DangNhap_GUI.class.getResourceAsStream("/images/logo.png")));
                ((Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow()).close();
                trangChuStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            lblErr.setText("Mật khẩu hoặc tên đăng nhập sai!");
        }
    }

    public void quenMKClicked(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/QuenMatKhau.fxml"));
            Parent root = loader.load();

            quenMatKhauControl = loader.getController();

            Stage quenMatKhauStage = new Stage();
            quenMatKhauStage.setScene(new Scene(root));
            quenMatKhauStage.setTitle("Quên mật khẩu");
            quenMatKhauStage.getIcons().add(new Image(DangNhap_GUI.class.getResourceAsStream("/images/logo.png")));
            ((Stage) ((javafx.scene.Node) mouseEvent.getSource()).getScene().getWindow()).close();
            quenMatKhauStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
