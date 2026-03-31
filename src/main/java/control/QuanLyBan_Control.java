package control;

import connectDB.ConnectDB;
import dao.*;
import entity.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;

public class QuanLyBan_Control {


    @FXML
    private ComboBox<String> hinhThucDatBan;

    @FXML
    private Pane monAn;

    @FXML
    private AnchorPane paneDatBan;
    @FXML
    private Label maBan;
    @FXML
    private TableColumn<MonAn, String> colMonAn;
    @FXML
    private TableColumn<MonAn, String> colGia;
    @FXML
    private TextField txtDiemTL;

    @FXML
    private TextField txtSdtKH;

    @FXML
    private TextField txtTenKH;
    @FXML
    private TableView<ChiTietHD_MonAn> tableDatMon;
    @FXML
    private TableColumn<ChiTietHD_MonAn, String> colTenMon;

    @FXML
    private TableColumn<ChiTietHD_MonAn, Double> colGiaTien;

    @FXML
    private TableColumn<ChiTietHD_MonAn, String> colGhiChu;

    @FXML
    private TableColumn<ChiTietHD_MonAn, Double> colThanhTien;

    @FXML
    private TableColumn<ChiTietHD_MonAn, Integer> colSoLuong;

    @FXML
    private TableColumn<ChiTietHD_MonAn, Double> colVAT;


    @FXML
    private Label lblTienCoc;

    @FXML
    private Label lblTongTien;
    @FXML
    private ComboBox<Integer> gioDatBan;
    @FXML
    private ComboBox<Integer> phutDatBan;
    @FXML
    private Button btnCoc;
    @FXML
    private ImageView chuaCoc;
    @FXML
    private Button btnDatBan;
    @FXML
    private TextField txtSoLuongKH;
    @FXML
    private DatePicker ngayDatBan;
    @FXML
    private TextField txtTenMonCanTim;
    @FXML
    private GridPane gridPaneMenu;
    @FXML
    private Button btnVangLai;
    // Khởi tạo dữ liệu khi giao diện được load
    public String banID;
    private MonAn_DAO monAnDao = new MonAn_DAO();
    ObservableList<MonAn> danhSachMonAn = FXCollections.observableArrayList(monAnDao.getMonAnListSanCo());
    private KhachHang_DAO khachHangDao = new KhachHang_DAO();
    private QuanLyHoaDon_DAO quanLyHoaDonDao = new QuanLyHoaDon_DAO();
    private ObservableList<KhachHang> danhSachKH = FXCollections.observableArrayList(khachHangDao.getKhachHangList());
    private ObservableList<ChiTietHD_MonAn> dsDatMon = FXCollections.observableArrayList();
    private ChiTietHD_MonAn_DAO chiTietHDMonAnDao = new ChiTietHD_MonAn_DAO();
    private static final Map<String, Integer> dailyInvoiceCount = new HashMap<>();
    private Ban_DAO banDao = new Ban_DAO();
    private ObservableList<Ban> danhSachBan = FXCollections.observableArrayList(banDao.getBanList());
    private ObservableList<HoaDon> listHoaDon = FXCollections.observableArrayList();
    private Stage trangChuStage;
    private String maNV;
    int check = 1;
    private NhanVien nv = SessionManager.getInstance().getCurrentNhanVien();
    public void setMaNV(String maNV) {
        this.maNV = maNV;
        // Bạn có thể sử dụng maNV ở đây
    }
    public void setSDT(String sdt) {
        txtSdtKH.setText(sdt);
    }
    public String getMaNV(){
        return maNV;
    }
    public void setBanID(String banID) {
        this.banID = banID;

        // Cập nhật giao diện với giá trị banID
        Platform.runLater(() -> {
            maBan.setText(banID);  // Hiển thị banID trên Label

        });
    }
    public void setTrangChuStage(Stage trangChuStage) {
        this.trangChuStage = trangChuStage;
    }

        private void loadData(String maMon, ObservableList<MonAn> dsMonAn) {

        int row = 0;
        int column = 0;

        gridPaneMenu.getChildren().clear();
        gridPaneMenu.getRowConstraints().clear();
        gridPaneMenu.getColumnConstraints().clear();

            for (MonAn monAn : dsMonAn) {
                if (monAn.getMaMonAn().contains(maMon)) {
//                    tableMonAn.getItems().add(monAn);
                    try {
                        FXMLLoader load = new FXMLLoader();
                        load.setLocation(getClass().getResource("/gui/CardMonAn.fxml"));
                        AnchorPane pane = load.load();
                        CardMonAn_Control cardMonAnControl = load.getController();

                        // Thiết lập sự kiện nhấp chuột cho mỗi ô
                        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                Integer rowIndex = GridPane.getRowIndex(pane);
                                Integer colIndex = GridPane.getColumnIndex(pane);

                                // Nếu rowIndex hoặc colIndex là null, thiết lập mặc định là 0
                                rowIndex = (rowIndex != null) ? rowIndex : 0;
                                colIndex = (colIndex != null) ? colIndex : 0;

                                // Hiển thị dòng và cột của ô đã nhấp
//                                System.out.println(cardMonAnControl.getTenMonAn().getText());
                                String tenMon = cardMonAnControl.getTenMonAn().getText();
                                MonAn mon = findMonAn(tenMon);
                                double gia = mon.getGia();
                                double vat = mon.getVAT();
                                int soLuong = 1;
                                TextInputDialog dialog = new TextInputDialog();
                                dialog.setTitle("Ghi chú");
                                dialog.setHeaderText("Nhập ghi chú cho món " + tenMon);
                                dialog.setContentText("Ghi chú:");
                                if ("Đặt bàn trước".equals(hinhThucDatBan.getValue())) {
                                    boolean exists = tableDatMon.getItems().stream()
                                            .anyMatch(item -> item.getTenMonAn().equals(mon.getTenMonAn()));
                                    if (exists) {
                                        // Hiển thị thông báo nếu món đã tồn tại
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Thông báo");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Món ăn đã có trong danh sách đặt món!");
                                        alert.showAndWait();
                                    } else {
                                        dialog.showAndWait().ifPresent(ghiChu -> {
                                            // Thêm dòng mới vào tableDatMon
                                            ChiTietHD_MonAn datMon = new ChiTietHD_MonAn(tenMon, ghiChu, gia, soLuong, gia*soLuong, vat); // Số lượng mặc định là 1
                                            // Thêm dữ liệu vào bảng 2
                                            tableDatMon.getItems().add(datMon);
                                            tableDatMon.setEditable(true);
                                            tinhTongTienVaTienCoc(datMon, 1);
                                            updateButtonState();
                                        }); }
                                } else {
                                    // Hiển thị thông báo nếu chưa chọn "Có"
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("Cảnh báo");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Vui lòng chọn hình thức 'Đặt bàn trước' để đặt món");
                                    alert.showAndWait();
                                }
                            }
                        });

                        cardMonAnControl.setData(monAn);

                        if(column == 3) {
                            column = 0;
                            row+=1;
                        }

                        gridPaneMenu.add(pane, column++, row);
                        GridPane.setMargin(pane, new Insets(5.5));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    @FXML
    public void initialize(){
        ObservableList<String> hinhThucList = FXCollections.observableArrayList("Đặt bàn", "Đặt bàn trước");
        hinhThucDatBan.setValue("Đặt bàn");
        ngayDatBan.setValue(LocalDate.now());
        gioDatBan.setDisable(true);
        phutDatBan.setDisable(true);
        ngayDatBan.setDisable(true);
        ngayDatBan.setValue(LocalDate.now());
        gioDatBan.setValue(LocalTime.now().getHour());
        phutDatBan.setValue(LocalTime.now().getMinute());
        int[] gioChon = {8, 9, 10, 11, 18, 19, 20}; // Các giờ mong muốn
        for (int gio : gioChon) {
            gioDatBan.getItems().add(gio); // Thêm các giờ vào ComboBox
        }
        phutDatBan.setItems(FXCollections.observableArrayList(
                0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55
        ));
        hinhThucDatBan.setItems(hinhThucList);
        txtDiemTL.setDisable(true);
        txtTenKH.setDisable(true);
//        colMonAn.setCellValueFactory(new PropertyValueFactory<>("tenMonAn"));
//        colGia.setCellValueFactory(new PropertyValueFactory<>("gia"));


    loadData("FO", danhSachMonAn);

        txtSdtKH.setOnAction(event -> {
            String sdt = txtSdtKH.getText();
            KhachHang kh = findKhachHangBySdt(sdt);
            if (kh != null) {
                if(kh.getsDT().equals("Không")) {
                    hinhThucDatBan.setValue("Đặt bàn");
                    hinhThucDatBan.setDisable(true);
                }
                txtTenKH.setText(kh.getTenKH());
                txtDiemTL.setText(String.valueOf(kh.getDiemTichLuy()));
                hinhThucDatBan.setDisable(false);
            } else
                if (!sdt.matches("^(03|05|07|08|09)\\d{8}$")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Số điện thoại không hợp lệ!");
                alert.showAndWait();
                txtDiemTL.clear();
                txtTenKH.clear();
                txtSdtKH.clear();
            }
            else {
                showAlertForAddCustomer(txtSdtKH.getText());
                txtDiemTL.clear();
                txtTenKH.clear();
                txtSdtKH.clear();
            }
        });

        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colSoLuong.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colSoLuong.setOnEditCommit(event -> {
            ChiTietHD_MonAn chiTietHDMonAn = event.getRowValue();
            int soLuongCu = chiTietHDMonAn.getSoLuong();
            Integer soLuong = event.getNewValue();
            if (soLuong != null && soLuong > 0) { // Kiểm tra giá trị là số dương
                chiTietHDMonAn.setSoLuong(soLuong);
                double giaTien = chiTietHDMonAn.getGia();
                chiTietHDMonAn.setThanhTien(giaTien*soLuong);
//                System.out.println("soLuongcu" + soLuongCu + "soluongmoi" + soLuong);
                tinhTongTienVaTienCoc(chiTietHDMonAn, soLuong - soLuongCu);
                tableDatMon.refresh();
            } else if (soLuong != null && soLuong == 0) {
                tableDatMon.getItems().remove(chiTietHDMonAn); // Xóa dòng nếu Số Lượng là 0
                tinhTongTienVaTienCoc(chiTietHDMonAn, soLuong - soLuongCu);
                updateButtonState();

            } else {
                // Nếu nhập không hợp lệ, thiết lập lại giá trị cũ
                tableDatMon.refresh();
            }
        });
        colThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        colGiaTien.setCellValueFactory(new PropertyValueFactory<>("gia"));
        colTenMon.setCellValueFactory(new PropertyValueFactory<>("tenMonAn"));
        colVAT.setCellValueFactory(new PropertyValueFactory<>("VAT"));
        //lam cho cot vat co hien thi % tren table view
        colVAT.setCellFactory(col -> new TableCell<ChiTietHD_MonAn, Double>() {
            @Override
            protected void updateItem(Double vat, boolean empty) {
                super.updateItem(vat, empty);
                if (empty || vat == null) {
                    setText(null);
                } else {
                    setText(vat + "%"); // Định dạng giá trị VAT
                }
            }
        });
        if (tableDatMon.getColumns().isEmpty()) {
            tableDatMon.getColumns().addAll(colTenMon, colGhiChu, colGiaTien, colSoLuong, colThanhTien, colVAT);
            tableDatMon.setItems(dsDatMon);
        }

        hinhThucDatBan.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("Đặt bàn".equals(newValue)) {
                // Xóa tất cả dữ liệu trong TableView
                tableDatMon.getItems().clear();
                lblTienCoc.setText("0 VNĐ");
                lblTongTien.setText("0 VNĐ");
                updateButtonState();
                ngayDatBan.setDisable(true);
                gioDatBan.setDisable(true);
                phutDatBan.setDisable(true);
                LocalTime now = LocalTime.now();
                ngayDatBan.setValue(LocalDate.now());
                gioDatBan.setValue(now.getHour());
                phutDatBan.setValue(now.getMinute());
            } else {
                ngayDatBan.setDisable(false);
                ngayDatBan.setValue(null);
                gioDatBan.setDisable(false);
                phutDatBan.setDisable(false);
                gioDatBan.setValue(null);
                phutDatBan.setValue(null);
            }
        });

        txtSoLuongKH.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) { // Kiểm tra xem chuỗi mới có phải là số hay không
                return change;
            }
            return null; // Không cho phép nhập nếu không phải là số
        }));

        // Lắng nghe thay đổi giá trị để kiểm tra xem số có nhỏ hơn hoặc bằng 10 hay không
        txtSoLuongKH.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    int soLuong = Integer.parseInt(newValue);
                    Ban ban = findBanByMa(maBan.getText());
                    if (soLuong > ban.getSoLuongGhe() || soLuong == 0) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Thông báo");
                        alert.setHeaderText(null);
                        alert.setContentText("Số lượng không được lớn hơn số ghế của bàn và không được là 0");
                        alert.showAndWait();
                        txtSoLuongKH.setText(oldValue); // Nếu lớn hơn 10, quay lại giá trị trước đó
                    }
                } catch (NumberFormatException e) {
                    txtSoLuongKH.setText(oldValue); // Nếu có lỗi, quay lại giá trị trước đó
                }
            }
        });

        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Đặt ngày tối thiểu và tối đa có thể chọn
        LocalDate minDate = today.plusDays(1); // Ngày tối thiểu là ngày hôm sau
        LocalDate maxDate = today.plusDays(7); // Ngày tối đa là 7 ngày kể từ hôm nay

        // Sử dụng AtomicReference để lưu giá trị cũ
        AtomicReference<LocalDate> previousValue = new AtomicReference<>(ngayDatBan.getValue());

        // Kiểm tra khi người dùng thay đổi ngày
        ngayDatBan.setOnAction(event -> {
            if(hinhThucDatBan.getValue() == null) {

                return;
            }
            LocalDate selectedDate = ngayDatBan.getValue();

            // Nếu ngày không hợp lệ (trước ngày hiện tại hoặc sau 7 ngày)
            if (!hinhThucDatBan.getValue().equals("Đặt bàn") && selectedDate != null && (selectedDate.isBefore(minDate) || selectedDate.isAfter(maxDate))) {
                // Hiển thị thông báo lỗi
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ngày trong khoảng từ 1 đến 7 ngày kể từ ngày hiện tại.");
                alert.showAndWait();

                // Đặt lại giá trị về giá trị cũ nếu ngày không hợp lệ
                ngayDatBan.setValue(null);
            } else {
                // Lưu giá trị hợp lệ để sử dụng cho lần kiểm tra sau
                previousValue.set(selectedDate);
            }
        });

        gioDatBan.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && (newValue == 20 || newValue == 11)) {
                phutDatBan.setValue(0); // Đặt phút mặc định là 0
                phutDatBan.setDisable(true);
            }
            else if(!hinhThucDatBan.getValue().equals("Đặt bàn")){
                phutDatBan.setDisable(false);
            }
        });
    }


    @FXML
    public void chonTrangDoUong() {
        loadData("DR", danhSachMonAn);
        check = 2;
    }

    @FXML
    public void chonTrangMonAn() {
        loadData("FO", danhSachMonAn);
        check = 1;
    }

    @FXML
    public void chonTrangRuouBia() {
//        tableMonAn.getItems().clear(); // Xóa các món ăn hiện tại
//        for (MonAn monAn : danhSachMonAn) {
//            if (monAn.getMaMonAn().contains("AC")) {
//                tableMonAn.getItems().add(monAn);
//            }
//        }
        loadData("AC", danhSachMonAn);
        check = 3;
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

    private KhachHang findKhachHangBySdt(String sdt) {
        for (KhachHang kh : danhSachKH) {
            if (kh.getsDT().equals(sdt)) {
                return kh;
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }
    private Ban findBanByMa(String ma) {
        for (Ban ban : danhSachBan) {
            if (ban.getMaBan().equals(ma)) {
                return ban;
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }
    private MonAn findMonAn(String sdt) {
        for (MonAn kh : danhSachMonAn) {
            if (kh.getTenMonAn().equals(sdt)) {
                return kh;
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }
    private void showAlertForAddCustomer(String sdt) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông Báo");
        alert.setHeaderText(null);
        alert.setContentText("Chưa có thông tin của khách hàng");

        ButtonType buttonTypeAdd = new ButtonType("Thêm Khách Hàng");
        ButtonType buttonTypeClose = new ButtonType("Đóng");
        hinhThucDatBan.setDisable(false);
        alert.getButtonTypes().setAll(buttonTypeAdd, buttonTypeClose);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeAdd) {
//                clickBtnThemKH();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ThemKH.fxml"));
                    Parent DatBanContent = loader.load();

                    // Lấy controller từ FXMLLoader và truyền banID
                    ThemKH_Control controller = loader.getController();
                    controller.setBanID(banID);
                    controller.setSDT(sdt);
                    paneDatBan.getChildren().clear();
                    paneDatBan.getChildren().add(DatBanContent);
                    paneDatBan.setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void tinhTongTienVaTienCoc(ChiTietHD_MonAn datMon, int soLuong) {

        // Lấy giá trị từ Label và chuyển đổi thành double
        String textTT = lblTongTien.getText().replace("VNĐ", "").trim(); // Bỏ "VNĐ" và khoảng trắng
            double valueTT = Double.parseDouble(textTT);
        valueTT = valueTT + (soLuong* datMon.getGia() + soLuong * datMon.getGia() * datMon.getVAT() * 0.01);
        String textTC = lblTienCoc.getText().replace("VNĐ", "").trim(); // Bỏ "VNĐ" và khoảng trắng
        double valueTC = Double.parseDouble(textTC);

        valueTC = valueTC +  soLuong * datMon.getGia() * 0.5;

        // Cập nhật Label
        lblTongTien.setText(valueTT + " VNĐ");
        lblTienCoc.setText(valueTC + " VNĐ");
    }

    @FXML
    private void chonBtnCoc() {

        if (btnCoc.getText().equals("Chưa cọc")) {
            if (lblTienCoc.getText().equals("0 VNĐ") || lblTienCoc.getText().equals("0.0 VNĐ")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Bàn đặt này không cần phải cọc!");
                alert.setContentText("Tiền tọc của bàn đặt là 0 VNĐ");
                // Hiển thị hộp thoại và chờ người dùng chọn
                Optional<ButtonType> result = alert.showAndWait();
            } else {
            // Tạo hộp thoại xác nhận
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận");
            alert.setHeaderText("Bạn có chắc chắn cọc không?");
            alert.setContentText("Lưu ý sau khi cọc thì không thể đặt thêm món!");
            // Hiển thị hộp thoại và chờ người dùng chọn
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                btnCoc.setText("Đã cọc");
                Image newImage = new Image(getClass().getResourceAsStream("/images/check.png"));
                chuaCoc.setImage(newImage);
                btnDatBan.setDisable(false);
                gridPaneMenu.setDisable(true);
                btnVangLai.setDisable(true);
                hinhThucDatBan.setDisable(true);
                for (Node node : gridPaneMenu.getChildren()) {
                    if (node instanceof AnchorPane) {
                        node.setDisable(true); // Vô hiệu hóa từng ô (AnchorPane) trong GridPane
                    }
                }
            }
        }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Bàn đặt này đã được đặt cọc!");
            Optional<ButtonType> result = alert.showAndWait();

        }
        }
    private void updateButtonState() {
        // Lấy giá trị từ lblTienCoc

        String textTC = lblTienCoc.getText().replace("VNĐ", "").trim(); // Xóa phần VNĐ
        double tienCoc = Double.parseDouble(textTC); // Chuyển đổi sang double
        // Kiểm tra giá trị và thay đổi trạng thái nút
        if (tienCoc > 0) {
            btnDatBan.setDisable(true); // Khóa nút nếu tiền cọc > 0
        } else {
            btnDatBan.setDisable(false); // Mở khóa nút nếu tiền cọc = 0
        }
    }
    @FXML
    private void chonBtnDatBan() {

        if (txtSdtKH.getText().isEmpty() || txtTenKH.getText().isEmpty() ||
                txtDiemTL.getText().isEmpty() || txtSoLuongKH.getText().isEmpty() ||
                hinhThucDatBan.getValue() == null || ngayDatBan.getValue() == null ||
                gioDatBan.getValue() == null || phutDatBan.getValue() == null) {

            // Hiển thị thông báo yêu cầu nhập đầy đủ thông tin
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông Báo");
            alert.setHeaderText("Thiếu thông tin");
            alert.setContentText("Vui lòng nhập đầy đủ các thông tin cần thiết.");
            alert.showAndWait();
        } else {
            // Hiển thị hộp thoại xác nhận
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xác Nhận Đặt Bàn");
            confirmAlert.setHeaderText("Bạn có chắc chắn muốn đặt bàn?");
            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Lưu thông tin vào cơ sở dữ liệu
                String maHoaDon = phatSinhMaHD(quanLyHoaDonDao.laySoThuTu(LocalDate.now()));
                saveHoaDonData(maHoaDon);
                saveChiTietHD_MonAn(maHoaDon);
                clickBtnDatBan();
            }
        }
    }
    private String phatSinhMaHD(int stt) {
        // Lấy chữ cái đầu từ banID
        char K = maBan.getText().charAt(0);

        // Lấy ngày hiện tại theo định dạng ddMMyy
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy");
        String currentDate = dateFormat.format(new Date());

        // Tạo khóa ngày để kiểm tra và tăng số thứ tự trong ngày
        String dateKey = K + currentDate;
//        int invoiceNumber = dailyInvoiceCount.getOrDefault(dateKey, 0) + 1;
//        dailyInvoiceCount.put(dateKey, invoiceNumber);

        // Định dạng số thứ tự thành 3 chữ số
        String invoiceSequence = String.format("%03d", stt);

        // Ghép thành mã hóa đơn theo định dạng HDKddmmyyXXX
        return "HD" + K + currentDate + invoiceSequence;
    }

    private void saveHoaDonData(String maHoaDon) {

        listHoaDon.clear();  // Xóa dữ liệu cũ trước khi tải dữ liệu mới
        listHoaDon.addAll(quanLyHoaDonDao.getHoaDonList2(maBan.getText(), java.sql.Date.valueOf(ngayDatBan.getValue()))); // Lấy dữ liệu từ DAO
        if(listHoaDon.size() > 0) {
            for(HoaDon hd : listHoaDon) {
                if((hd.getGioDatBan().getHour() <= 11 && gioDatBan.getValue() <= 11) || (hd.getGioDatBan().getHour() >=18 && gioDatBan.getValue() >= 18)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("Bàn " + maBan.getText() + " lúc này không trống!");
                    alert.showAndWait();
                    return;
                }
            }
        }
        // Ngày tạo hóa đơn lấy ngày hiện tại
        LocalDate ngayTaoHD = LocalDate.now();

        // Trạng thái hóa đơn
        String trangThaiHD = "CHUA_THANH_TOAN";

        // Các thông tin khác
        LocalDate ngayDat = ngayDatBan.getValue();
        String ban = maBan.getText();
        String tienCocText = lblTienCoc.getText().replaceAll("[^\\d.]", ""); // Chỉ giữ lại chữ số và dấu thập phân
        double tienCoc = Double.parseDouble(tienCocText);
        String tongTienText = lblTongTien.getText().replaceAll("[^\\d.]", ""); // Chỉ giữ lại chữ số và dấu thập phân
        double tongTien = Double.parseDouble(tongTienText);
        int soLuongKH = Integer.parseInt(txtSoLuongKH.getText());

        // Lấy giờ đặt bàn dưới dạng LocalTime
        int gioDat = gioDatBan.getValue();
        int phutDat = phutDatBan.getValue();
        LocalTime gioDatBan = LocalTime.of(gioDat, phutDat);
        String maNV = nv.getMaNV();
//        String maNV = nv.getMaNV();
        KhachHang kh = findKhachHangBySdt(txtSdtKH.getText());
        if(kh == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Lỗi Lưu Hóa Đơn");
            alert.setContentText("Số điện thoại khách hàng không chính xác!");
            alert.showAndWait();
            return;
        }
        String maKH = kh.getMaKH();
        String hinhThucD = "";
        Boolean checkIn = true;
        if(hinhThucDatBan.getValue().equals("Đặt bàn")) {
            hinhThucD = "DAT_BAN";
            checkIn = true;
        }
        if(hinhThucDatBan.getValue().equals("Đặt bàn trước")) {
            hinhThucD = "DAT_BAN_TRUOC";
            checkIn = false;
        }
            quanLyHoaDonDao.themHoaDon(maHoaDon, ngayTaoHD, trangThaiHD, ngayDat, ban, maNV, maKH, tienCoc,
                gioDatBan, tongTien, soLuongKH, hinhThucD, checkIn);

    }

    private void saveChiTietHD_MonAn(String maHoaDon) {
        ObservableList<ChiTietHD_MonAn> monAnList = tableDatMon.getItems();
        for(ChiTietHD_MonAn chiTietHDMonAn : monAnList) {
            MonAn monAn = findMonAn(colTenMon.getCellObservableValue(chiTietHDMonAn).getValue());
            String maMonAn = monAn.getMaMonAn();
            int soLuong = colSoLuong.getCellObservableValue(chiTietHDMonAn).getValue();
            double thanhTien = colThanhTien.getCellObservableValue(chiTietHDMonAn).getValue();;
            String ghiChu = colGhiChu.getCellObservableValue(chiTietHDMonAn).getValue();
            chiTietHDMonAnDao.themChiTietHD_MonAn(maHoaDon, maMonAn, soLuong, thanhTien, ghiChu);
        }
    }
        @FXML
        private void chonBtnVangLai () {
        if(txtSdtKH.getText().equals("Không")) {
            txtSdtKH.clear();
            txtTenKH.clear();
            txtDiemTL.clear();
            hinhThucDatBan.setDisable(false);
        } else {
            txtSdtKH.setText("Không");
            txtTenKH.setText("Khách vãng lai");
            txtDiemTL.setText("0");
            hinhThucDatBan.setValue("Đặt bàn");
            hinhThucDatBan.setDisable(true);
        }

        }

        @FXML
    private void timMonAn() {

            String tenMon = txtTenMonCanTim.getText().toLowerCase();

            // Tạo danh sách kết quả tìm kiếm
            ObservableList<MonAn> searchResult = FXCollections.observableArrayList();
            // Lọc dữ liệu từ danh sách hoaDonList dựa trên thông tin tìm kiếm
            for (MonAn mon : danhSachMonAn) {
                boolean matchesTenMon = tenMon.isEmpty() || mon.getTenMonAn().toLowerCase().contains(tenMon);
                if (matchesTenMon) {
                    searchResult.add(mon);
                }
            }
            // Hiển thị kết quả tìm kiếm trong bảng
//            tableMonAn.setItems(searchResult);
            String ma;
            if(check == 1) {
                ma = "FO";
            } else if(check == 2) {
                ma = "DR";
            } else {
                ma = "AC";
            }

            int row = 0;
            int column = 0;

          loadData(ma, searchResult);

        }
    }
