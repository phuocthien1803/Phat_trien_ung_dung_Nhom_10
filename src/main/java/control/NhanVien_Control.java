package control;

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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.ResourceBundle;

public class NhanVien_Control implements Initializable {
    @FXML
    private TextField txtMaNV;
    @FXML
    private TextField txtSDT;
    @FXML
    private TextField txtTenNV;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox comboChucVu;
    @FXML
    private ComboBox comboTrangThai;
    @FXML
    private DatePicker ngaySinh;
    @FXML
    private RadioButton radNam;
    @FXML
    private RadioButton radNu;
    @FXML
    private ToggleGroup genderGroup;
    @FXML
    private TableView<NhanVien> tblNhanVien;
    @FXML
    private TableColumn<NhanVien, String> cellMaNV;
    @FXML
    private TableColumn<NhanVien, String> cellTenNV;
    @FXML
    private TableColumn<NhanVien, String> cellGioiTinh;
    @FXML
    private TableColumn<NhanVien, String> cellSDT;
    @FXML
    private TableColumn<NhanVien, Date> cellNgaySinh;
    @FXML
    private TableColumn<NhanVien, Date> cellNgayVaoLam;
    @FXML
    private TableColumn<NhanVien, Date> cellNgayNghiLam;
    @FXML
    private TableColumn<NhanVien, String> cellChucVu;
    @FXML
    private TableColumn<NhanVien, String> cellTrangThai;
    @FXML
    private TableColumn<NhanVien, String> cellEmail;
    @FXML
    private TextField txtMaTimKiem;
    @FXML
    private ComboBox comboCauHoi;
    @FXML
    private TextField txtCauHoi;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderGroup = new ToggleGroup();
        radNam.setSelected(true);
        radNam.setToggleGroup(genderGroup);
        radNu.setToggleGroup(genderGroup);
        String chucVuNV[] = {"Nhân viên", "Quản lý"};
        comboChucVu.setItems(FXCollections.observableArrayList(chucVuNV));
        comboChucVu.setValue("Nhân viên");
        String trangThaiNV[] = {"NGHI", "DANG_LAM", "NGHI_DAI_HAN"};
        comboTrangThai.setItems(FXCollections.observableArrayList(trangThaiNV));
        comboTrangThai.setValue("DANG_LAM");
        String cauHoi[] = {"Màu sắc yêu thích của bạn là gì?", "Món ăn yêu thích của bạn là gì?", "Món uống yêu thích của bạn là gì?"};
        comboCauHoi.setItems(FXCollections.observableArrayList(cauHoi));
        ObservableList<NhanVien> nhanVienList = NhanVien_DAO.getNhanVienList();
        cellMaNV.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        cellTenNV.setCellValueFactory(new PropertyValueFactory<>("tenNV"));
        cellGioiTinh.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isGioiTinh() ? "Nam" : "Nữ")
        );
        cellNgaySinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        cellSDT.setCellValueFactory(new PropertyValueFactory<>("sDT"));
        cellNgayVaoLam.setCellValueFactory(new PropertyValueFactory<>("ngayVaoLam"));
        cellNgayNghiLam.setCellValueFactory(new PropertyValueFactory<>("ngayNghiLam"));
        cellChucVu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));
        cellTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        // Gắn dữ liệu vào TableView
        tblNhanVien.setItems(nhanVienList);
    }
    public NhanVien RevertNhanVien(){

        String ma = txtMaNV.getText();
        String ten = txtTenNV.getText();
        String sdt = txtSDT.getText();
        LocalDate ngaySinhSelected = ngaySinh.getValue();
        boolean gt = true;
        if(genderGroup.getSelectedToggle() != null){
            RadioButton SelectedGender = (RadioButton) genderGroup.getSelectedToggle();
            gt = SelectedGender.getText().equals("Nam");
        }
        LocalDate ngayVaoLamSelected = LocalDate.now();
        LocalDate ngayNghiLamSelected = null;
        String cv = (String) comboChucVu.getValue();
        TrangThaiNhanVien tTNV = null;
        String tt = (String) comboTrangThai.getValue();
        if(tt != null){
            tTNV = TrangThaiNhanVien.valueOf(tt);
        }
        NhanVien nv = new NhanVien(ma, ten, gt, sdt, tTNV, cv, ngayVaoLamSelected, ngayNghiLamSelected, ngaySinhSelected);
        return nv;
    }
    public void themNhanVien(ActionEvent actionEvent) {
        NhanVien_DAO dao = new NhanVien_DAO();
        String ten = txtTenNV.getText();
        String sdt = txtSDT.getText();
        LocalDate ngaySinhSelected = ngaySinh.getValue();
        boolean gt = true;
        if (genderGroup.getSelectedToggle() != null) {
            RadioButton selectedGender = (RadioButton) genderGroup.getSelectedToggle();
            gt = selectedGender.getText().equals("Nam");
        }
        LocalDate ngayVaoLamSelected = LocalDate.now();
        LocalDate ngayNghiLamSelected = null;
        String cv = (String) comboChucVu.getValue();
        TrangThaiNhanVien tTNV = null;
        String tt = (String) comboTrangThai.getValue();
        String question = (String) comboCauHoi.getValue();
        String answer = txtCauHoi.getText();
        if (tt != null) {
            tTNV = TrangThaiNhanVien.valueOf(tt);
        }
        String ma = taoMaNhanVienTuDong(cv, ngayVaoLamSelected, dao.laySoThuTu(ngayVaoLamSelected));

        if (kiemTraThongTin(ten, sdt, ngaySinhSelected)) {
            NhanVien nv = new NhanVien(ma, ten, gt, sdt, tTNV, cv, ngayVaoLamSelected, ngayNghiLamSelected, ngaySinhSelected, question, answer);
            if (nv.getTenNV().isEmpty() || nv.getNgaySinh() == null) {
                showAlert("Thông báo", "Vui lòng điền đầy đủ thông tin.");
                return;
            }
            boolean isAdd = dao.themNhanVien(nv);
            // Hiển thị thông báo cho người dùng
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (isAdd) {
                // Khởi tạo mã hóa mật khẩu
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String hashedPassword = encoder.encode("123"); // Hash mật khẩu mặc định "123"

                TaiKhoan_DAO tk_Dao = new TaiKhoan_DAO();
                TaiKhoan tk = new TaiKhoan(nv, hashedPassword); // Sử dụng mật khẩu đã hash
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
            // Cập nhật bảng nhân viên nếu cần
            ObservableList<NhanVien> nhanVienList = NhanVien_DAO.getNhanVienList();
            tblNhanVien.setItems(nhanVienList);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private String taoMaNhanVienTuDong(String chucVu, LocalDate ngayVaoLam, int soThuTu) {
        // Định dạng ngày thành chuỗi ddMMyy
        String ngay = ngayVaoLam.format(java.time.format.DateTimeFormatter.ofPattern("ddMMyy"));

        // Xác định phần CV (TN cho nhân viên thường, QL cho quản lý)
        String cv;
        if (chucVu.equalsIgnoreCase("Quản lý")) {
            cv = "QL";
        } else {
            cv = "TN";
        }

        // Tạo mã số thứ tự với định dạng 2 chữ số
        String soThuTuFormat = String.format("%02d", soThuTu);

        // Tạo mã nhân viên hoàn chỉnh
        return "NV" + cv + ngay + soThuTuFormat;
    }

    public void rowClicked(MouseEvent mouseEvent) {
        NhanVien SelectedNhanVien = tblNhanVien.getSelectionModel().getSelectedItem();
        NhanVien_DAO dao = new NhanVien_DAO();
        NhanVien nv = dao.timKiemNhanVien1(SelectedNhanVien.getMaNV());
        if(SelectedNhanVien != null){
            txtMaNV.setText(SelectedNhanVien.getMaNV());
            txtTenNV.setText(SelectedNhanVien.getTenNV());
            txtSDT.setText(SelectedNhanVien.getSDT());
            if(SelectedNhanVien.isGioiTinh()){
                radNam.setSelected(true);
            }
            else {
                radNu.setSelected(true);
            }
            if(SelectedNhanVien.getNgaySinh() != null){
                ngaySinh.setValue(SelectedNhanVien.getNgaySinh());
            }
            else {
                ngaySinh.setValue(null);
            }
            comboChucVu.setValue(SelectedNhanVien.getChucVu().toString());
            comboTrangThai.setValue(SelectedNhanVien.getTrangThai().toString());
            if (nv.getCauHoi() != null) {
                comboCauHoi.setValue(nv.getCauHoi().toString());
            } else {
                comboCauHoi.setValue(null);
            }
            txtCauHoi.setText(nv.getTraLoi() != null ? nv.getTraLoi() : "");
        }
    }

    public void capNhatNhanVien(ActionEvent actionEvent) {
        NhanVien_DAO dao = new NhanVien_DAO();
        String ma = txtMaNV.getText();
        String ten = txtTenNV.getText();
        String sdt = txtSDT.getText();
        LocalDate ngaySinhSelected = ngaySinh.getValue();
        boolean gt = true;
        if(genderGroup.getSelectedToggle() != null){
            RadioButton SelectedGender = (RadioButton) genderGroup.getSelectedToggle();
            gt = SelectedGender.getText().equals("Nam");
        }
        NhanVien nv1 = dao.timKiemNhanVien1(ma);
        LocalDate ngayVaoLamSelected = nv1.getNgayVaoLam();
        LocalDate ngayNghiLamSelected = null;
        String cv = (String) comboChucVu.getValue();
        String question = (String) comboCauHoi.getValue();
        String answer = txtCauHoi.getText();
        TrangThaiNhanVien tTNV = null;
        String tt = (String) comboTrangThai.getValue();
        if(tt != null){
            tTNV = TrangThaiNhanVien.valueOf(tt);
        }
        if(tt.equalsIgnoreCase("NGHI")){
            ngayNghiLamSelected = LocalDate.now();
        }
        if(kiemTraThongTin(ten, sdt, ngaySinhSelected)){
            NhanVien nv = new NhanVien(ma, ten, gt, sdt, tTNV, cv, ngayVaoLamSelected, ngayNghiLamSelected, ngaySinhSelected, question, answer);
            if(nv.getTenNV().isEmpty() || nv.getNgaySinh() == null){
                showAlert("Thông báo", "Vui lòng điền đầy đủ thông tin.");
                return;
            }
            dao.capNhatNhanVien(nv);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công");
            alert.setHeaderText("Cập nhật nhân viên thành công");
            alert.setContentText("Thông tin nhân viên " + nv.getTenNV() + " đã được cập nhật vào hệ thống.");
            alert.showAndWait();
            tblNhanVien.getItems().clear();
            // Cập nhật bảng nhân viên nếu cần
            ObservableList<NhanVien> nhanVienList = NhanVien_DAO.getNhanVienList();
            tblNhanVien.setItems(nhanVienList);
        }
    }

    public void timKiemNhanVien(ActionEvent actionEvent) {
        String ma = txtMaTimKiem.getText();
        NhanVien_DAO dao = new NhanVien_DAO();
        if(ma.equalsIgnoreCase("")){
            ObservableList<NhanVien> nhanVienList = NhanVien_DAO.getNhanVienList();
            tblNhanVien.getItems().clear();
            tblNhanVien.setItems(nhanVienList);
        }else {
            ObservableList<NhanVien> nhanVienList = dao.timKiemNhanVien(ma);
            if(nhanVienList == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thất bại");
                alert.setHeaderText("Tìm kiếm không thành công");
                alert.setContentText("Không tìm thấy nhân viên với ma" + ma);
                alert.showAndWait();
            }else{
                tblNhanVien.getItems().clear();
                tblNhanVien.setItems(nhanVienList);
            }
        }
    }
    public boolean kiemTraThongTin(String tenNV, String sDT, LocalDate ngaySinh) {
        // Kiểm tra tên nhân viên
        if (tenNV == null || tenNV.trim().isEmpty()) {
            showAlert("Lỗi", "Tên nhân viên không được để trống.");
            return false;
        }
        // Tên không chứa ký tự đặc biệt hoặc số
        if (!tenNV.matches("[\\p{L} ]+")) {  // Chỉ cho phép ký tự chữ và khoảng trắng
            showAlert("Lỗi", "Tên nhân viên không được chứa ký tự đặc biệt hoặc số.");
            return false;
        }
        // Tên phải viết hoa chữ cái đầu của mỗi từ và có khoảng trắng giữa các từ
        if (!tenNV.matches("([A-ZÀ-Ỷ][a-zà-ỹ]+\\s?)+")) {
            showAlert("Lỗi", "Tên nhân viên phải viết hoa chữ cái đầu của mỗi từ và có khoảng trắng giữa các từ.");
            return false;
        }
        // Kiểm tra số điện thoại
        if (sDT == null || sDT.trim().isEmpty()) {
            showAlert("Lỗi", "Số điện thoại không được để trống.");
            return false;
        }
        // Số điện thoại phải có 10 chữ số và bắt đầu bằng các cặp số [03, 05, 07, 08, 09]
        if (!sDT.matches("^(03|05|07|08|09)\\d{8}$")) {
            showAlert("Lỗi", "Số điện thoại phải có 10 chữ số và bắt đầu bằng các cặp số 03, 05, 07, 08, 09.");
            return false;
        }
        // Kiểm tra ngày sinh: phải từ 18 đến dưới 60 tuổi
        if (ngaySinh == null) {
            showAlert("Lỗi", "Ngày sinh không được để trống.");
            return false;
        }
        LocalDate now = LocalDate.now();
        int tuoi = Period.between(ngaySinh, now).getYears();
        if (tuoi < 18) {
            showAlert("Lỗi", "Nhân viên phải từ 18 tuổi trở lên.");
            return false;
        } else if (tuoi >= 60) {
            showAlert("Lỗi", "Nhân viên phải nhỏ hơn 60 tuổi.");
            return false;
        }
        return true;// Nếu mọi thứ đều hợp lệ
    }

}
