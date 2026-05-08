package control;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import entity.NhanVien;
import entity.TaiKhoan;
import gui.DangNhap_GUI;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class QuenMatKhau_Control implements Initializable {
    @FXML
    private Pane paneKT;
    @FXML
    private Pane paneDMK;
    @FXML
    private TextField txtTenDN;
    @FXML
    private ComboBox comboCH;
    @FXML
    private TextField txtCTL;
    @FXML
    private Button btnTT;
    @FXML
    private TextField txtMKM;
    @FXML
    private TextField txtKTMK;
    @FXML
    private Button btnXN;
    @FXML
    private Button btnTV;
    @FXML
    private Label lblThongBao;
    @FXML
    private Label lblTB;
    NhanVien nv;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String question[] = {"Màu sắc yêu thích của bạn là gì?", "Món ăn yêu thích của bạn là gì?", "Món uống yêu thích của bạn là gì?"};
        comboCH.setItems(FXCollections.observableArrayList(question));
    }

    public void xacNhanClicked(ActionEvent actionEvent) {
        String mkm = txtMKM.getText();
        String ktmk = txtKTMK.getText();

        if (mkm.isEmpty() || ktmk.isEmpty()) {
            lblTB.setText("Mật khẩu mới hoặc xác nhận mật khẩu không được để trống!");
            return;
        }

        if (!mkm.equalsIgnoreCase(ktmk)) {
            lblTB.setText("Mật khẩu nhập lại không trùng khớp!");
            return;
        }

        if(kiemTraMatKhau(ktmk)){
            TaiKhoan_DAO dao = new TaiKhoan_DAO();
            TaiKhoan tk = dao.timKiemTaiKhoan(nv.getMaNV());
            if (tk == null) {
                lblTB.setText("Không tìm thấy tài khoản để cập nhật mật khẩu!");
                return;
            }

            try {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String hashedPassword = encoder.encode(mkm);
                tk.setMatKhau(hashedPassword);
                dao.doiMatKhau(tk);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Đổi mật khẩu thành công");
                alert.setTitle("Thành công");
                alert.setContentText("Mật khẩu của tài khoản đã được thay đổi.");
                alert.showAndWait();
                ((Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow()).close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/DangNhap.fxml"));
                Parent root = loader.load();
                Stage dangNhapStage = new Stage();
                dangNhapStage.setScene(new Scene(root));
                dangNhapStage.setTitle("Đăng nhập");
                dangNhapStage.getIcons().add(new Image(DangNhap_GUI.class.getResourceAsStream("/images/logo.png")));
                dangNhapStage.show();
            } catch (Exception e) {
                lblThongBao.setText("Có lỗi xảy ra khi đổi mật khẩu. Vui lòng thử lại sau.");
                e.printStackTrace();
            }
        }
    }


    public void troVeClicked(ActionEvent actionEvent) {
        paneDMK.setVisible(false);
        paneKT.setVisible(true);
    }

    public void tiepTucClicked(ActionEvent actionEvent) {
        String ma = txtTenDN.getText();
        String ch = (String) comboCH.getValue();
        String tl = txtCTL.getText();
        TaiKhoan_DAO dao = new TaiKhoan_DAO();
        TaiKhoan tk = dao.timKiemTaiKhoan(ma);
        NhanVien_DAO nv_dao = new NhanVien_DAO();
        nv = nv_dao.timKiemNhanVien1(ma);
        if(tk == null){
            lblThongBao.setText("Không tìm thấy tài khoản!!!");
        }
        else{
            if(!ch.equalsIgnoreCase(nv.getCauHoi())){
                lblThongBao.setText("Câu hỏi không chính xác!!!");
            }else {
                if(!tl.equalsIgnoreCase(nv.getTraLoi())){
                    lblThongBao.setText("Câu trả lời không chính xác!!!");
                }else{
                    paneKT.setVisible(false);
                    paneDMK.setVisible(true);
                }
            }
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public boolean kiemTraMatKhau(String mk){
        if(!mk.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$")){
            showAlert("Lỗi", "Mật khẩu phải có ít nhất 1 ký tự in hoa, 1 ký tự số và 1 ký tự đặc biệt!!!");
            return false;
        }
        return true;
    }
}
