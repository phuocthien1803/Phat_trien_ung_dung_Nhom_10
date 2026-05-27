package control;

import java.io.IOException;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class DangNhap_Control {

    @FXML ImageView hide;
    @FXML PasswordField password;
    @FXML TextField textFieldMK;
    @FXML private TextField textFieldDN;
    @FXML private Button btnDangNhap;
    @FXML private Button btnLamMoi;
    @FXML private Label lblErr;

    Image view  = new Image(getClass().getResourceAsStream("/images/view.png"));
    Image hidee = new Image(getClass().getResourceAsStream("/images/hide.png"));

    private boolean isPasswordVisible = false;
    public TrangChu_Control trangChuControl;
    private QuenMatKhau_Control quenMatKhauControl;

    // BCrypt encoder dùng chung trong class
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public void initialize() {
        textFieldMK.textProperty().bindBidirectional(password.textProperty());
        textFieldMK.setManaged(false);
        textFieldMK.setVisible(false);
        textFieldDN.setOnAction(this::dangNhap);
        password.setOnAction(this::dangNhap);

        btnDangNhap.setOnMouseEntered(e -> btnDangNhap.setStyle(
                "-fx-background-color: #A11212; -fx-text-fill: #F5F5F5; -fx-border-color: #D4AF37;"));
        btnDangNhap.setOnMouseExited(e -> btnDangNhap.setStyle(
                "-fx-background-color: #8B0000; -fx-text-fill: #F5F5F5; -fx-border-color: #D4AF37;"));
        btnLamMoi.setOnMouseEntered(e -> btnLamMoi.setStyle(
                "-fx-background-color: #A11212; -fx-text-fill: #F5F5F5; -fx-border-color: #D4AF37;"));
        btnLamMoi.setOnMouseExited(e -> btnLamMoi.setStyle(
                "-fx-background-color: #8B0000; -fx-text-fill: #F5F5F5; -fx-border-color: #D4AF37;"));
    }

    public void hienThiMK() {
        if (isPasswordVisible) {
            hide.setImage(hidee);
            textFieldMK.setVisible(false);
            textFieldMK.setManaged(false);
            password.setVisible(true);
            password.setManaged(true);
        } else {
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
        lblErr.setText("");
    }

    // PHƯƠNG THỨC ĐĂNG NHẬP
    public void dangNhap(ActionEvent actionEvent) {
        String tenDN   = textFieldDN.getText().trim();
        String matKhau = textFieldMK.getText();

        // Kiểm tra trường trống trước khi truy vấn DB
        if (tenDN.isEmpty() || matKhau.isEmpty()) {
            lblErr.setText("Vui lòng nhập tên đăng nhập và mật khẩu.");
            return;
        }

        TaiKhoan_DAO tkDao  = new TaiKhoan_DAO();
        TaiKhoan    tk      = tkDao.timKiemTaiKhoan(tenDN);
        NhanVien    nv      = NhanVien_DAO.timKiemNhanVien1(tenDN);

        // [SỬA LỖI 1 + 4] Kiểm tra null TRƯỚC mọi thao tác
        if (tk == null || nv == null) {
            lblErr.setText("Mật khẩu hoặc tên đăng nhập sai!");
            return;
        }

        // [SỬA LỖI 2] Xác minh mật khẩu đúng cách
        boolean matKhauDung = xacMinhMatKhau(matKhau, tk.getMatKhau(), tenDN, tkDao, tk);

        if (!matKhauDung) {
            lblErr.setText("Mật khẩu hoặc tên đăng nhập sai!");
            return;
        }

        // [SỬA LỖI 3 + 5] Chỉ set session SAU KHI xác thực thành công
        lblErr.setText("");
        SessionManager.getInstance().setCurrentNhanVien(nv);

        // Mở giao diện trang chủ
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/TrangChu.fxml"));
            Parent root = loader.load();

            TrangChu_Control trangChuController = loader.getController();
            trangChuController.setMaNV(tenDN);
            trangChuController.setNhanvien(nv.getTenNV());
            trangChuController.setNhanvienData(nv);

            Stage trangChuStage = new Stage();
            trangChuController.setStageTrangChu(trangChuStage);
            trangChuStage.setScene(new Scene(root));
            trangChuStage.setTitle("Nhà Hàng KKTS");
            trangChuStage.getIcons().add(
                    new Image(DangNhap_GUI.class.getResourceAsStream("/images/logo.png")));

            // Đóng cửa sổ đăng nhập CHỈ SAU KHI đã load TrangChu thành công
            ((Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow()).close();
            trangChuStage.show();

        } catch (IOException e) {
            // Load thất bại → không đóng cửa sổ, hiển thị lỗi thay vì crash
            lblErr.setText("Lỗi hệ thống: không thể mở trang chủ. Vui lòng thử lại.");
            SessionManager.getInstance().clear(); // Hủy session vừa set nếu UI lỗi
            e.printStackTrace();
        }
    }

    /**
     * Xác minh mật khẩu người dùng nhập với mật khẩu lưu trong DB.
     *
     * Xử lý 2 trường hợp:
     *   - Mật khẩu BCrypt (bắt đầu bằng "$2a$" hoặc "$2b$"): dùng encoder.matches()
     *   - Mật khẩu plain text cũ (tạo thủ công trong DB): so sánh trực tiếp,
     *     nếu đúng thì TỰ ĐỘNG MIGRATE sang BCrypt để những lần sau dùng BCrypt.
     *
     * @param matKhauNhap  Mật khẩu người dùng vừa nhập
     * @param matKhauDB    Mật khẩu lưu trong DB
     * @param tenDN        Tên đăng nhập (dùng khi cần migrate)
     * @param tkDao        DAO dùng để cập nhật mật khẩu khi migrate
     * @param tk           Đối tượng TaiKhoan (dùng khi migrate)
     * @return true nếu mật khẩu khớp, false nếu sai
     */
    private boolean xacMinhMatKhau(String matKhauNhap, String matKhauDB,
                                   String tenDN, TaiKhoan_DAO tkDao, TaiKhoan tk) {

        // Trường hợp 1: mật khẩu đã được hash bằng BCrypt
        if (matKhauDB.startsWith("$2a$") || matKhauDB.startsWith("$2b$")) {
            return encoder.matches(matKhauNhap, matKhauDB);
        }

        // Trường hợp 2: mật khẩu plain text cũ (tài khoản tạo thủ công trong DB)
        // So sánh trực tiếp, nếu đúng thì migrate sang BCrypt ngay
        if (matKhauNhap.equals(matKhauDB.trim())) {
            migrateSangBCrypt(matKhauNhap, tenDN, tkDao, tk);
            return true;
        }

        return false;
    }

    /**
     * Tự động nâng cấp mật khẩu plain text lên BCrypt.
     * Được gọi đúng một lần duy nhất cho mỗi tài khoản cũ,
     * vào lần đầu tiên họ đăng nhập thành công sau khi hệ thống cập nhật.
     */
    private void migrateSangBCrypt(String matKhauPlainText, String tenDN,
                                   TaiKhoan_DAO tkDao, TaiKhoan tk) {
        try {
            String matKhauHash = encoder.encode(matKhauPlainText);
            tk.setMatKhau(matKhauHash);
            tkDao.doiMatKhau(tk);
            System.out.println("[Migration] Tài khoản '" + tenDN
                    + "' đã được tự động nâng cấp từ plain text sang BCrypt.");
        } catch (Exception e) {
            // Lỗi migrate không ngăn đăng nhập — chỉ log để xử lý sau
            System.err.println("[Migration] Không thể migrate tài khoản '" + tenDN + "': " + e.getMessage());
        }
    }

    // QUÊN MẬT KHẨU
    public void quenMKClicked(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/QuenMatKhau.fxml"));
            Parent root = loader.load();
            quenMatKhauControl = loader.getController();

            Stage quenMatKhauStage = new Stage();
            quenMatKhauStage.setScene(new Scene(root));
            quenMatKhauStage.setTitle("Quên mật khẩu");
            quenMatKhauStage.getIcons().add(
                    new Image(DangNhap_GUI.class.getResourceAsStream("/images/logo.png")));

            ((Stage) ((javafx.scene.Node) mouseEvent.getSource()).getScene().getWindow()).close();
            quenMatKhauStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}