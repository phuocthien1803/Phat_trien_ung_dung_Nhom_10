package control;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.ResourceBundle;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import entity.NhanVien;
import entity.TaiKhoan;
import entity.TrangThaiNhanVien;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class NhanVien_Control implements Initializable {

    @FXML private TextField  txtMaNV;
    @FXML private TextField  txtSDT;
    @FXML private TextField  txtTenNV;
    @FXML private TextField  txtEmail;
    @FXML private ComboBox   comboChucVu;
    @FXML private ComboBox   comboTrangThai;
    @FXML private DatePicker ngaySinh;
    @FXML private RadioButton radNam;
    @FXML private RadioButton radNu;
    @FXML private ToggleGroup genderGroup;
    @FXML private TableView<NhanVien>             tblNhanVien;
    @FXML private TableColumn<NhanVien, String>   cellMaNV;
    @FXML private TableColumn<NhanVien, String>   cellTenNV;
    @FXML private TableColumn<NhanVien, String>   cellGioiTinh;
    @FXML private TableColumn<NhanVien, String>   cellSDT;
    @FXML private TableColumn<NhanVien, Date>     cellNgaySinh;
    @FXML private TableColumn<NhanVien, Date>     cellNgayVaoLam;
    @FXML private TableColumn<NhanVien, Date>     cellNgayNghiLam;
    @FXML private TableColumn<NhanVien, String>   cellChucVu;
    @FXML private TableColumn<NhanVien, String>   cellTrangThai;
    @FXML private TableColumn<NhanVien, String>   cellEmail;
    @FXML private TextField  txtMaTimKiem;
    @FXML private ComboBox   comboCauHoi;
    @FXML private TextField  txtCauHoi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderGroup = new ToggleGroup();
        radNam.setSelected(true);
        radNam.setToggleGroup(genderGroup);
        radNu.setToggleGroup(genderGroup);

        String[] chucVuNV = {"Nhân viên", "Quản lý"};
        comboChucVu.setItems(FXCollections.observableArrayList(chucVuNV));
        comboChucVu.setValue("Nhân viên");

        String[] trangThaiNV = {"NGHI", "DANG_LAM", "NGHI_DAI_HAN"};
        comboTrangThai.setItems(FXCollections.observableArrayList(trangThaiNV));
        comboTrangThai.setValue("DANG_LAM");

        String[] question = {
            "Màu sắc yêu thích của bạn là gì?",
            "Món ăn yêu thích của bạn là gì?",
            "Món uống yêu thích của bạn là gì?"
        };
        comboCauHoi.setItems(FXCollections.observableArrayList(question));

        ObservableList<NhanVien> nhanVienList = NhanVien_DAO.getNhanVienList();
        cellMaNV.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        cellTenNV.setCellValueFactory(new PropertyValueFactory<>("tenNV"));
        cellGioiTinh.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isGioiTinh() ? "Nam" : "Nữ"));
        cellNgaySinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        cellSDT.setCellValueFactory(new PropertyValueFactory<>("sDT"));
        cellNgayVaoLam.setCellValueFactory(new PropertyValueFactory<>("ngayVaoLam"));
        cellNgayNghiLam.setCellValueFactory(new PropertyValueFactory<>("ngayNghiLam"));
        cellChucVu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));
        cellTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        tblNhanVien.setItems(nhanVienList);
    }

    // rowClicked 
    public void rowClicked(MouseEvent mouseEvent) {
        NhanVien selectedNhanVien = tblNhanVien.getSelectionModel().getSelectedItem();

        // [SỬA LỖI 1] Kiểm tra null TRƯỚC khi làm bất cứ điều gì
        // Người dùng có thể click vào vùng trống trong bảng → getSelectedItem() = null
        if (selectedNhanVien == null) return;

        // Bây giờ an toàn để gọi DAO
        NhanVien_DAO dao = new NhanVien_DAO();
        NhanVien nv = dao.timKiemNhanVien1(selectedNhanVien.getMaNV());

        txtMaNV.setText(selectedNhanVien.getMaNV());
        txtTenNV.setText(selectedNhanVien.getTenNV());
        txtSDT.setText(selectedNhanVien.getSDT());

        if (selectedNhanVien.isGioiTinh()) {
            radNam.setSelected(true);
        } else {
            radNu.setSelected(true);
        }

        if (selectedNhanVien.getNgaySinh() != null) {
            ngaySinh.setValue(selectedNhanVien.getNgaySinh());
        } else {
            ngaySinh.setValue(null);
        }

        comboChucVu.setValue(selectedNhanVien.getChucVu().toString());
        comboTrangThai.setValue(selectedNhanVien.getTrangThai().toString());

        // nv có thể null nếu timKiemNhanVien1 không tìm thấy — guard an toàn
        if (nv != null) {
            comboCauHoi.setValue(nv.getCauHoi() != null ? nv.getCauHoi() : null);
            txtCauHoi.setText(nv.getTraLoi() != null ? nv.getTraLoi() : "");
        }
    }

    // Thêm nhân viên
    public NhanVien RevertNhanVien() {
        String ma   = txtMaNV.getText();
        String ten  = txtTenNV.getText();
        String sdt  = txtSDT.getText();
        LocalDate ngaySinhSelected = ngaySinh.getValue();
        boolean gt = true;
        if (genderGroup.getSelectedToggle() != null) {
            gt = ((RadioButton) genderGroup.getSelectedToggle()).getText().equals("Nam");
        }
        LocalDate ngayVaoLamSelected  = LocalDate.now();
        LocalDate ngayNghiLamSelected = null;
        String cv = (String) comboChucVu.getValue();
        TrangThaiNhanVien tTNV = null;
        String tt = (String) comboTrangThai.getValue();
        if (tt != null) tTNV = TrangThaiNhanVien.valueOf(tt);
        return new NhanVien(ma, ten, gt, sdt, tTNV, cv, ngayVaoLamSelected, ngayNghiLamSelected, ngaySinhSelected);
    }

    public void themNhanVien(ActionEvent actionEvent) {
        NhanVien_DAO dao = new NhanVien_DAO();
        String ten  = txtTenNV.getText();
        String sdt  = txtSDT.getText();
        LocalDate ngaySinhSelected = ngaySinh.getValue();
        boolean gt = true;
        if (genderGroup.getSelectedToggle() != null) {
            gt = ((RadioButton) genderGroup.getSelectedToggle()).getText().equals("Nam");
        }
        LocalDate ngayVaoLamSelected  = LocalDate.now();
        LocalDate ngayNghiLamSelected = null;
        String cv       = (String) comboChucVu.getValue();
        TrangThaiNhanVien tTNV = null;
        String tt       = (String) comboTrangThai.getValue();
        String question = (String) comboCauHoi.getValue();
        String answer   = txtCauHoi.getText();
        if (tt != null) tTNV = TrangThaiNhanVien.valueOf(tt);

        String ma = taoMaNhanVienTuDong(cv, ngayVaoLamSelected, dao.laySoThuTu(ngayVaoLamSelected));

        if (kiemTraThongTin(ten, sdt, ngaySinhSelected)) {
            NhanVien nv = new NhanVien(ma, ten, gt, sdt, tTNV, cv,
                    ngayVaoLamSelected, ngayNghiLamSelected, ngaySinhSelected, question, answer);
            if (nv.getTenNV().isEmpty() || nv.getNgaySinh() == null) {
                showAlert("Thông báo", "Vui lòng điền đầy đủ thông tin.");
                return;
            }

            boolean isAdd = dao.themNhanVien(nv);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (isAdd) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String hashedPassword = encoder.encode("123");
                TaiKhoan_DAO tk_Dao = new TaiKhoan_DAO();
                TaiKhoan tk = new TaiKhoan(nv, hashedPassword);
                boolean isAddTK = tk_Dao.taoTaiKhoan(tk);
                if (isAddTK) {
                    alert.setTitle("Thành công");
                    alert.setHeaderText("Thêm nhân viên thành công");
                    alert.setContentText("Nhân viên " + nv.getTenNV() + " đã được thêm vào hệ thống.");
                }
            } else {
                alert.setTitle("Thất bại");
                alert.setHeaderText("Thêm nhân viên không thành công");
                alert.setContentText("Đã xảy ra lỗi khi thêm nhân viên. Vui lòng thử lại.");
            }
            alert.showAndWait();
            tblNhanVien.setItems(NhanVien_DAO.getNhanVienList());
        }
    }

    // Cập nhật nhân viên
    public void capNhatNhanVien(ActionEvent actionEvent) {
        NhanVien_DAO dao = new NhanVien_DAO();
        String ma  = txtMaNV.getText();
        String ten = txtTenNV.getText();
        String sdt = txtSDT.getText();
        LocalDate ngaySinhSelected = ngaySinh.getValue();
        boolean gt = true;
        if (genderGroup.getSelectedToggle() != null) {
            gt = ((RadioButton) genderGroup.getSelectedToggle()).getText().equals("Nam");
        }

        NhanVien nv1 = dao.timKiemNhanVien1(ma);
        LocalDate ngayVaoLamSelected  = nv1.getNgayVaoLam();
        LocalDate ngayNghiLamSelected = null;
        String cv       = (String) comboChucVu.getValue();
        String question = (String) comboCauHoi.getValue();
        String answer   = txtCauHoi.getText();
        TrangThaiNhanVien tTNV = null;
        String tt = (String) comboTrangThai.getValue();
        if (tt != null) tTNV = TrangThaiNhanVien.valueOf(tt);
        if (tt.equalsIgnoreCase("NGHI")) ngayNghiLamSelected = LocalDate.now();

        if (kiemTraThongTin(ten, sdt, ngaySinhSelected)) {
            NhanVien nv = new NhanVien(ma, ten, gt, sdt, tTNV, cv,
                    ngayVaoLamSelected, ngayNghiLamSelected, ngaySinhSelected, question, answer);
            if (nv.getTenNV().isEmpty() || nv.getNgaySinh() == null) {
                showAlert("Thông báo", "Vui lòng điền đầy đủ thông tin.");
                return;
            }
            dao.capNhatNhanVien(nv);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công");
            alert.setHeaderText("Cập nhật nhân viên thành công");
            alert.setContentText("Thông tin nhân viên " + nv.getTenNV() + " đã được cập nhật.");
            alert.showAndWait();
            tblNhanVien.getItems().clear();
            tblNhanVien.setItems(NhanVien_DAO.getNhanVienList());
        }
    }

    // Tìm kiếm
    public void timKiemNhanVien(ActionEvent actionEvent) {
        String ma = txtMaTimKiem.getText();
        NhanVien_DAO dao = new NhanVien_DAO();
        if (ma.equalsIgnoreCase("")) {
            tblNhanVien.getItems().clear();
            tblNhanVien.setItems(NhanVien_DAO.getNhanVienList());
        } else {
            ObservableList<NhanVien> nhanVienList = dao.timKiemNhanVien(ma);
            if (nhanVienList == null) {
                showAlert("Thất bại", "Không tìm thấy nhân viên với mã: " + ma);
            } else {
                tblNhanVien.getItems().clear();
                tblNhanVien.setItems(nhanVienList);
            }
        }
    }

    // Helpers
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String taoMaNhanVienTuDong(String chucVu, LocalDate ngayVaoLam, int soThuTu) {
        String ngay = ngayVaoLam.format(java.time.format.DateTimeFormatter.ofPattern("ddMMyy"));
        String cv   = chucVu.equalsIgnoreCase("Quản lý") ? "QL" : "TN";
        return "NV" + cv + ngay + String.format("%02d", soThuTu);
    }

    public boolean kiemTraThongTin(String tenNV, String sDT, LocalDate ngaySinh) {
        if (tenNV == null || tenNV.trim().isEmpty()) {
            showAlert("Lỗi", "Tên nhân viên không được để trống.");
            return false;
        }
        if (!tenNV.matches("[\\p{L} ]+")) {
            showAlert("Lỗi", "Tên nhân viên không được chứa ký tự đặc biệt hoặc số.");
            return false;
        }
        if (!tenNV.matches("([A-ZÀ-Ỷ][a-zà-ỹ]+\\s?)+")) {
            showAlert("Lỗi", "Tên nhân viên phải viết hoa chữ cái đầu của mỗi từ và có khoảng trắng giữa các từ.");
            return false;
        }
        if (sDT == null || sDT.trim().isEmpty()) {
            showAlert("Lỗi", "Số điện thoại không được để trống.");
            return false;
        }
        if (!sDT.matches("^(03|05|07|08|09)\\d{8}$")) {
            showAlert("Lỗi", "Số điện thoại phải có 10 chữ số và bắt đầu bằng 03, 05, 07, 08, 09.");
            return false;
        }
        if (ngaySinh == null) {
            showAlert("Lỗi", "Ngày sinh không được để trống.");
            return false;
        }
        int tuoi = Period.between(ngaySinh, LocalDate.now()).getYears();
        if (tuoi < 18) {
            showAlert("Lỗi", "Nhân viên phải từ 18 tuổi trở lên.");
            return false;
        }
        if (tuoi >= 60) {
            showAlert("Lỗi", "Nhân viên phải nhỏ hơn 60 tuổi.");
            return false;
        }
        return true;
    }
}