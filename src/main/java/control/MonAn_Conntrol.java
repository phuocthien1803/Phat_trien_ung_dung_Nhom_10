package control;

import dao.LoaiMonAn_DAO;
import dao.MonAn_DAO;
import entity.LoaiMonAn;
import entity.MonAn;
import entity.TrangThaiMonAn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class MonAn_Conntrol implements Initializable {
    @FXML
    private TextField txtHinhAnh;
    @FXML
    private ComboBox comboTrangThai;
    @FXML
    private ComboBox comboLoaiMon;
    @FXML
    private TableView tblMonAn;
    @FXML
    private TableColumn cellMaMon;
    @FXML
    private TableColumn cellTenMon;
    @FXML
    private TableColumn cellGia;
    @FXML
    private TableColumn cellVAT;
    @FXML
    private TableColumn cellMaLoai;
    @FXML
    private TableColumn cellTenLoai;
    @FXML
    private TableColumn cellTrangThai;
    @FXML
    private TableColumn cellHinhAnh;
    @FXML
    private TextField txtMaMon;
    @FXML
    private TextField txtTenMon;
    @FXML
    private TextField txtGia;
    @FXML
    private TextField txtTimMon;

    private FileChooser fileChooser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtGia.setText("0");
        ObservableList<LoaiMonAn> loaiMonAnList = LoaiMonAn_DAO.getLoaiMonAnList();
        comboLoaiMon.setConverter(new StringConverter<LoaiMonAn>() {
            @Override
            public String toString(LoaiMonAn loaiMonAn) {
                return loaiMonAn != null ? loaiMonAn.getTenLoai() : "";
            }

            @Override
            public LoaiMonAn fromString(String string) {
                return null; // Không cần từ chuỗi về LoaiMonAn
            }
        });
        comboLoaiMon.setItems(loaiMonAnList);
        // Thiết lập mặc định
        if(!loaiMonAnList.isEmpty()){
            comboLoaiMon.setValue(loaiMonAnList.get(0));
        }
        String trangThaiNV[] = {"SANCO", "TAMHET", "NGUNGBAN"};
        comboTrangThai.setItems(FXCollections.observableArrayList(trangThaiNV));
        comboTrangThai.setValue("SANCO");
        // Khởi tạo FileChooser một lần duy nhất khi initialize
        fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image File", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
        ObservableList<MonAn> monAnList = MonAn_DAO.getMonAnList();
        cellMaMon.setCellValueFactory(new PropertyValueFactory<>("maMonAn"));
        cellTenMon.setCellValueFactory(new PropertyValueFactory<>("tenMonAn"));
        cellGia.setCellValueFactory(new PropertyValueFactory<>("gia"));
        cellTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThaiMonAn"));
        cellVAT.setCellValueFactory(new PropertyValueFactory<>("VAT"));
        cellMaLoai.setCellValueFactory(new PropertyValueFactory<>("maLoai"));
        cellTenLoai.setCellValueFactory(new PropertyValueFactory<>("tenLoai"));
        cellHinhAnh.setCellValueFactory(new PropertyValueFactory<>("hinhAnh"));

        tblMonAn.setItems(monAnList);
    }

    public void chonAnh(ActionEvent actionEvent) {
        Stage stage = (Stage) txtHinhAnh.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            // Hiển thị tên file lên TextField txtHinhAnh
            txtHinhAnh.setText(file.getName());
            // Lưu đường dẫn đầy đủ của tệp đã chọn vào biến UserData để dùng sau
            Path sourcePath = file.toPath();
            txtHinhAnh.setUserData(sourcePath);

            System.out.println("File được chọn: " + sourcePath);
        } else {
            showAlert("Lỗi", "Không có ảnh nào được chọn.");
        }
    }

    public void themMonAn(ActionEvent actionEvent) {
        MonAn_DAO dao = new MonAn_DAO();
        String ten = txtTenMon.getText();
        double gia = Double.parseDouble(txtGia.getText());
        TrangThaiMonAn tTMA = null;
        String tt = (String) comboTrangThai.getValue();
        if (tt != null) {
            tTMA = TrangThaiMonAn.valueOf(tt);
        }
        LoaiMonAn loai = (LoaiMonAn) comboLoaiMon.getValue();
        if (loai != null) {
            String maLoai = loai.getMaLoai();
            String tenLoai = loai.getTenLoai();
        }
        String hinh = txtHinhAnh.getText();
        String maMA = taoMaHoaDonTuDong(loai.getTenLoai());
        double vat = (loai.getMaLoai().equalsIgnoreCase("FO") || loai.getMaLoai().equalsIgnoreCase("DR")) ? 8.0 :
                (loai.getMaLoai().equalsIgnoreCase("AC") ? 10.0 : 0.0);
        // Sử dụng đường dẫn từ UserData
        Path sourcePath = (Path) txtHinhAnh.getUserData();
        if (sourcePath == null) {
            showAlert("Lỗi", "Vui lòng chọn một hình ảnh trước khi thêm món ăn.");
            return;
        }

        // Đường dẫn đích trong thư mục resources
        Path destinationDir = Paths.get("src/main/resources/monAnImages/");
        Path destinationFile = destinationDir.resolve(hinh);

        try {
            if (!Files.exists(destinationDir)) {
                Files.createDirectories(destinationDir);
            }
            Files.copy(sourcePath, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Ảnh được sao chép đến: " + destinationFile);
        } catch (IOException e) {
            showAlert("Lỗi", "Không thể lưu ảnh. Vui lòng thử lại.");
            System.err.println("Lỗi lưu ảnh: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        MonAn ma = new MonAn(maMA, ten, loai, gia, tTMA, vat, hinh);
        if (ma.getTenMonAn().isEmpty() || ma.getGia() == 0) {
            showAlert("Thông báo", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        boolean isAdd = dao.themMonAn(ma);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (isAdd) {
            alert.setTitle("Thành công");
            alert.setHeaderText("Thêm món ăn thành công");
            alert.setContentText("Món ăn " + ma.getTenMonAn() + " đã được thêm vào hệ thống.");
        } else {
            alert.setTitle("Thất bại");
            alert.setHeaderText("Thêm món ăn không thành công");
            alert.setContentText("Đã xảy ra lỗi khi thêm món ăn. Vui lòng thử lại.");
        }
        alert.showAndWait();

        // Cập nhật bảng món ăn
        ObservableList<MonAn> monAnList = MonAn_DAO.getMonAnList();
        tblMonAn.setItems(monAnList);
    }


    private String taoMaHoaDonTuDong(String tenLoai) {
        String ml = LoaiMonAn_DAO.getMaLoai(tenLoai);

        int soThuTu = MonAn_DAO.laySoThuTu(ml);

        // Tạo mã số thứ tự với định dạng 2 chữ số
        String soThuTuFormat = (soThuTu < 10) ? String.format("0%d", soThuTu) : String.valueOf(soThuTu);

        // Tạo mã nhân viên hoàn chỉnh
        return ml + soThuTuFormat;
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void rowClicked(MouseEvent mouseEvent) {
        MonAn SelectedMonAn = (MonAn) tblMonAn.getSelectionModel().getSelectedItem();
        if (SelectedMonAn != null) {
            txtMaMon.setText(SelectedMonAn.getMaMonAn());
            txtTenMon.setText(SelectedMonAn.getTenMonAn());
            txtGia.setText(String.valueOf(SelectedMonAn.getGia()));
            LoaiMonAn selectedLoai = null;
            ObservableList<LoaiMonAn> loaiMonAnList = LoaiMonAn_DAO.getLoaiMonAnList();
            for (LoaiMonAn loai : loaiMonAnList) {
                if (loai.getTenLoai().equals(SelectedMonAn.getTenLoai())) {
                    selectedLoai = loai;
                    break;
                }
            }
            comboLoaiMon.setValue(selectedLoai); // Thiết lập loại món tương ứng
            comboTrangThai.setValue(SelectedMonAn.getTrangThaiMonAn().toString());
            txtHinhAnh.setText(SelectedMonAn.getHinhAnh());
        }
    }

    public void capNhatMonAn(ActionEvent actionEvent) {
        MonAn_DAO dao = new MonAn_DAO();
        String maMA = txtMaMon.getText();
        String ten = txtTenMon.getText();
        double gia = Double.parseDouble(txtGia.getText());
        TrangThaiMonAn tTMA = null;
        String tt = (String) comboTrangThai.getValue();
        if (tt != null) {
            tTMA = TrangThaiMonAn.valueOf(tt);
        }
        LoaiMonAn loai = (LoaiMonAn) comboLoaiMon.getValue();
        if (loai != null) {
            String maLoai = loai.getMaLoai();
            String tenLoai = loai.getTenLoai();
        }
        if (loai != null && loai.getTenLoai().equalsIgnoreCase(comboLoaiMon.getValue().toString())) {
            String maMAMon = taoMaHoaDonTuDong(loai.getTenLoai());
            maMA = maMAMon;
        }
        String hinh = txtHinhAnh.getText();
        double vat = (loai.getMaLoai().equalsIgnoreCase("FO") || loai.getMaLoai().equalsIgnoreCase("DR")) ? 8.0 :
                (loai.getMaLoai().equalsIgnoreCase("AC") ? 10.0 : 0.0);
        // Sử dụng đường dẫn từ UserData
        Path sourcePath = (Path) txtHinhAnh.getUserData();
        if (sourcePath == null) {
            showAlert("Lỗi", "Vui lòng chọn một hình ảnh trước khi cập nhật món ăn.");
            return;
        }

        // Đường dẫn đích trong thư mục resources
        Path destinationDir = Paths.get("src/main/resources/monAnImages/");
        Path destinationFile = destinationDir.resolve(hinh);

        try {
            if (!Files.exists(destinationDir)) {
                Files.createDirectories(destinationDir);
            }
            Files.copy(sourcePath, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Ảnh được sao chép đến: " + destinationFile);
        } catch (IOException e) {
            showAlert("Lỗi", "Không thể lưu ảnh. Vui lòng thử lại.");
            System.err.println("Lỗi lưu ảnh: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        MonAn ma = new MonAn(maMA, ten, loai, gia, tTMA, vat, hinh);
        if (ma.getTenMonAn().isEmpty() || ma.getGia() == 0) {
            showAlert("Thông báo", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        // Gọi hàm DAO để cập nhật món ăn
        dao.capNhatMonAn(ma);

        // Hiển thị thông báo cập nhật thành công
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText("Cập nhật món ăn thành công");
        alert.setContentText("Thông tin món " + ma.getTenMonAn() + " đã được cập nhật vào hệ thống.");
        alert.showAndWait();

        // Cập nhật bảng món ăn
        tblMonAn.getItems().clear();
        ObservableList<MonAn> monAnList = MonAn_DAO.getMonAnList();
        tblMonAn.setItems(monAnList);
    }


    public void timKiemMonAn(ActionEvent actionEvent) {
        String ten = txtTimMon.getText();
        MonAn_DAO dao = new MonAn_DAO();
        if(ten.equalsIgnoreCase("")){
            ObservableList<MonAn> monAnList = MonAn_DAO.getMonAnList();
            tblMonAn.getItems().clear();
            tblMonAn.setItems(monAnList);
        }else {
            ObservableList<MonAn> monAnList = dao.timKiemMonAn(ten);
            if(monAnList == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thất bại");
                alert.setHeaderText("Tìm kiếm không thành công");
                alert.setContentText("Không tìm thấy món ăn với tên" + ten);
                alert.showAndWait();
            }else{
                tblMonAn.getItems().clear();
                tblMonAn.setItems(monAnList);
            }
        }
    }
}
