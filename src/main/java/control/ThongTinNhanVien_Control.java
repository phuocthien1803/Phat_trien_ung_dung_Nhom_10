package control;

import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import entity.NhanVien;
import entity.TaiKhoan;
import gui.DangNhap_GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;


public class ThongTinNhanVien_Control implements Initializable {

    @FXML private TextField txtMNV;
    @FXML private TextField txtTNV;
    @FXML private TextField txtGT;
    @FXML private TextField txtSDT;
    @FXML private TextField txtNS;
    @FXML private TextField txtCV;
    @FXML private TextField txtMKCu;
    @FXML private TextField txtMKMoi;
    @FXML private TextField txtChkMKMoi;
    @FXML private Pane paneTTNV;
    @FXML private Pane paneDMK;
    @FXML private Label lblTB;

    private Stage stageTrangChu;

    public void setStageTrangChu(Stage stageTrangChu) {
        this.stageTrangChu = stageTrangChu;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paneTTNV.setVisible(true);
        paneDMK.setVisible(false);
    }

   
    public void setNhanVien(NhanVien nv) {
        txtMNV.setText(nv.getMaNV());
        txtTNV.setText(nv.getTenNV());
        txtGT.setText(nv.isGioiTinh() ? "Nam" : "Nữ");
        txtSDT.setText(nv.getSDT());
        txtNS.setText(nv.getNgaySinh().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtCV.setText(nv.getChucVu());
    }

   
    public void doiMatKhau(ActionEvent actionEvent) {
        paneDMK.setVisible(true);
        paneTTNV.setVisible(false);
    }

    public void xacNhanCilcked(ActionEvent actionEvent) {
        String ma       = txtMNV.getText();
        String mkcu     = txtMKCu.getText();
        String mkmoi    = txtMKMoi.getText();
        String mkmoilai = txtChkMKMoi.getText();

        if (ma.isEmpty() || mkcu.isEmpty() || mkmoi.isEmpty() || mkmoilai.isEmpty()) {
            lblTB.setText("Vui lòng điền đầy đủ tất cả các trường mật khẩu.");
            return;
        }

        TaiKhoan_DAO dao = new TaiKhoan_DAO();
        TaiKhoan tk      = dao.timKiemTaiKhoan(ma);
        String mkDB      = dao.TimKiemMK(ma);

        if (tk == null || mkDB == null) {
            lblTB.setText("Không tìm thấy tài khoản.");
            return;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Kiểm tra mật khẩu cũ — encoder.matches() xử lý đúng cả BCrypt lẫn edge case
        if (!encoder.matches(mkcu, mkDB)) {
            showAlert("Thất bại", "Mật khẩu cũ không đúng. Vui lòng kiểm tra lại.");
            return;
        }

        // sửa Dùng equals() thay vì equalsIgnoreCase()
        // Mật khẩu PHẢI phân biệt chữ hoa/thường
        if (!mkmoi.equals(mkmoilai)) {
            lblTB.setText("Phần nhập lại mật khẩu không trùng khớp!");
            return;
        }

        if (!kiemTraMatKhau(mkmoi)) {
            lblTB.setText("Mật khẩu phải có ít nhất 1 ký tự in hoa, 1 ký tự số và 1 ký tự đặc biệt!");
            return;
        }

        // [SỬA LỖI 3] Xóa comment sai "// Hash mật khẩu mặc định '123'"
        String hashedPassword = encoder.encode(mkmoi);
        tk.setMatKhau(hashedPassword);
        dao.doiMatKhau(tk);

        lblTB.setText("");
        paneTTNV.setVisible(true);
        paneDMK.setVisible(false);

        showAlert("Thành công", "Mật khẩu của tài khoản đã được thay đổi thành công.");
    }

    public void quayLaiClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Hủy tiến trình đổi mật khẩu");
        alert.setHeaderText("Bạn có chắc chắn muốn hủy không?");
        ButtonType buttonTypeYes = new ButtonType("Có");
        ButtonType buttonTypeNo  = new ButtonType("Không");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            txtMKCu.clear();
            txtMKMoi.clear();
            txtChkMKMoi.clear();
            lblTB.setText("");
            paneTTNV.setVisible(true);
            paneDMK.setVisible(false);
        }
    }

    // Đăng xuất 
    public void dangXuat(ActionEvent actionEvent) {
        if (hienThiXacNhan("Xác nhận đăng xuất", "Bạn có chắc chắn muốn đăng xuất không?")) {
            // [SỬA LỖI 2] Xóa session TRƯỚC khi mở màn hình đăng nhập
            SessionManager.getInstance().clear();
            chuyenGiaoDienDangNhap(actionEvent);
            dongTrangChu(actionEvent);
        }
    }

    private boolean hienThiXacNhan(String tieuDe, String noiDung) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tieuDe);
        alert.setHeaderText(noiDung);
        alert.getButtonTypes().setAll(new ButtonType("Có"), new ButtonType("Không"));
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get().getText().equals("Có");
    }

    private void chuyenGiaoDienDangNhap(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/DangNhap.fxml"));
            Parent dangNhapRoot = loader.load();
            Stage dangNhapStage = new Stage();
            dangNhapStage.setScene(new Scene(dangNhapRoot));
            dangNhapStage.setTitle("Đăng nhập");
            dangNhapStage.getIcons().add(
                    new Image(DangNhap_GUI.class.getResourceAsStream("/images/logo.png")));
            dangNhapStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Lỗi tải giao diện Đăng nhập", e);
        }
    }

    private void dongTrangChu(ActionEvent actionEvent) {
        if (stageTrangChu != null) {
            stageTrangChu.close();
        }
        ((Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    // Helpers
    public String getMaNhanVien() {
        return txtMNV.getText();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Kiểm tra mật khẩu phải có ít nhất:
     *   - 1 chữ hoa
     *   - 1 chữ số
     *   - 1 ký tự đặc biệt trong tập: @$!%*?&
     */
    public boolean kiemTraMatKhau(String mk) {
        return mk.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$");
    }
}