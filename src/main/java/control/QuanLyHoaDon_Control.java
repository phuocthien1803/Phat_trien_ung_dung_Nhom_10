package control;

import dao.*;
import entity.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleObjectProperty;

public class QuanLyHoaDon_Control {


    @FXML
    private ComboBox<String> trangThaiHD;

    @FXML
    private AnchorPane paneDatBan;
    @FXML
    private TableView<HoaDon> tableHoaDon;
    @FXML
    private TableColumn<HoaDon, String> colTenKH;
    @FXML
    private TableColumn<HoaDon, String> colSDT;
    @FXML
    private TableColumn<HoaDon, LocalDate> colNgayTaoHD;
    @FXML
    private TableColumn<HoaDon, Time> colGioDat;
    @FXML
    private TableColumn<HoaDon, LocalDate> colNgayDat;
    @FXML
    private TableColumn<HoaDon, String> colMaHD;
    @FXML
    private TableColumn<HoaDon, Text> colTrangThaiHD;
    // Khởi tạo dữ liệu khi giao diện được load
    @FXML
    private Label maBan;  // Giả sử maBan là một Label trên giao diện
    @FXML
    private BorderPane ttHoaDon;
    @FXML
    private BorderPane dsHoaDon;
    @FXML
    private TextField txtMaHD;

    @FXML
    private TextField txtNhanVien;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtSoLuongKH;

    @FXML
    private TextField txtTenKH;

    @FXML
    private TextField txtTrangThaiHD;
    @FXML
    private TextField txtDiemTL;
    @FXML
    private TextField txtMaBan;
    @FXML
    private ComboBox<Integer> gioDatBan;
    @FXML
    private ComboBox<Integer> phutDatBan;
    @FXML
    private DatePicker ngayDatBan;
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
    private TableView<ChiTietHD_MonAn> tableDatMon;

    @FXML
    private Label lblTienCoc;

    @FXML
    private Label lblTongTien;
    @FXML
    private TableColumn<MonAn, String> colMonAn;
    @FXML
    private TableColumn<MonAn, String> colGia;
    @FXML
    private Button btnCapNhat;

    @FXML
    private Button btnCheckIn;
    @FXML
    private Button btnHuy;
    @FXML
    private FontIcon chuaCheck;
    @FXML
    private DatePicker timNgayDatBan;
    @FXML
    private TextField txtTimKH;

    @FXML
    private TextField txtTimSDT;

    @FXML
    private TextField txtTenMonCanTim;
    @FXML
    private GridPane gridPaneMenu;
    private MonAn_DAO monAnDao = new MonAn_DAO();
    ObservableList<MonAn> danhSachMonAn = FXCollections.observableArrayList(monAnDao.getMonAnList());
    private QuanLyHoaDon_DAO hoaDonDAO = new QuanLyHoaDon_DAO();
    private ObservableList<HoaDon> listHoaDon = FXCollections.observableArrayList();
    private ObservableList<HoaDon> listHoaDon2 = FXCollections.observableArrayList();
    private ObservableList<HoaDon> listHoaDon3 = FXCollections.observableArrayList();

    private String banID;
    private ChiTietHD_MonAn_DAO chiTietHDMonAnDao = new ChiTietHD_MonAn_DAO();
    private KhachHang_DAO khachHangDao = new KhachHang_DAO();

    private ObservableList<KhachHang> danhSachKH = FXCollections.observableArrayList(khachHangDao.getKhachHangList());
    String sdtCu;
    private Ban_DAO banDao = new Ban_DAO();
    private ObservableList<Ban> danhSachBan = FXCollections.observableArrayList(banDao.getBanList());
    private ObservableList<HoaDon> danhSachHD;
    int check = 1;
    // Phương thức này sẽ được gọi từ Control Y để truyền banID
    public void setBanID(String banID) {
        this.banID = banID;

        // Cập nhật giao diện với giá trị banID
        Platform.runLater(() -> {
            maBan.setText(banID);  // Hiển thị banID trên Label
        });
        loadData(); // Gọi hàm loadData sau khi nhận được banID

    }

    public void loadData() {
        if (banID != null && !banID.isEmpty()) {
            listHoaDon.clear();  // Xóa dữ liệu cũ trước khi tải dữ liệu mới
            listHoaDon.addAll(hoaDonDAO.getHoaDonList(banID)); // Lấy dữ liệu từ DAO

            for(HoaDon hd : listHoaDon) {
                LocalDateTime ngayGioDatBan = hd.getNgayDat().atTime(hd.getGioDatBan());
                LocalDateTime ngayGioHienTai = LocalDateTime.now();

                if(ngayGioHienTai.isAfter(ngayGioDatBan.plusMinutes(30)) && !hd.isCheckIn()){
                    try{
                        hoaDonDAO.huyHoaDon(hd.getMaHD());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }
//            tableHoaDon.setItems(listHoaDon); // Gán dữ liệu cho bảng
            for(HoaDon hd: listHoaDon) {
                if(hd.getTrangThaiHoaDon().getDisplayName().equals("Chưa thanh toán")) {
                    tableHoaDon.getItems().add(hd);
                }
            }
        } else {
            System.out.println("banID is null or empty!");
        }
    }

    public void loadDataMonAn(String maMon, ObservableList<MonAn> dsMonAn) {
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
                            if ("Đã CheckIn".equals(btnCheckIn.getText())) {
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
                                    }); }
                            } else {
                                // Hiển thị thông báo nếu chưa chọn "Có"
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Cảnh báo");
                                alert.setHeaderText(null);
                                alert.setContentText("Vui lòng CheckIn để đặt món");
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
    public void initialize() {
        ObservableList<String> hinhThucList = FXCollections.observableArrayList("Chưa thanh toán", "Đã thanh toán");
        trangThaiHD.setItems(hinhThucList);
        trangThaiHD.setValue("Chưa thanh toán");
        timHoaDon();

        dsHoaDon.setVisible(true);
        ttHoaDon.setVisible(false);
//        ObservableList<HoaDon> hoaDonList = QuanLyHoaDon_DAO.getHoaDonList(banID);

        // Hiển thị kết quả tìm kiếm trong bảng
//        colTrangThaiHD.setCellValueFactory(new PropertyValueFactory<>("trangThaiHoaDon"));
        colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("sDT"));
        colNgayTaoHD.setCellValueFactory(new PropertyValueFactory<>("ngayTaoHD"));
        colGioDat.setCellValueFactory(new PropertyValueFactory<>("gioDatBan"));
        colMaHD.setCellValueFactory(new PropertyValueFactory<>("maHD"));
        colNgayDat.setCellValueFactory(new PropertyValueFactory<>("ngayDat"));
        colTrangThaiHD.setCellValueFactory(data -> {
            HoaDon hoaDon = data.getValue();
            Text statusText = new Text();
            if(hoaDon.isCheckIn()) {
                statusText.setText("Đã Check-in");
                statusText.setStyle("-fx-fill: green; ");
            } else {
                if (hoaDon.isQuaHan()) {
                    statusText.setText(hoaDon.getThoiGianQuaHan());
                    statusText.setStyle("-fx-fill: red; ");
                } else {
                    statusText.setText("Chưa Check-in");
                }
            }

            return new javafx.beans.property.SimpleObjectProperty<>(statusText);
        });


        if (banID != null) {
            loadData();
        }

        // Timeline cập nhật trạng thái theo thời gian thực
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {

            tableHoaDon.refresh();
            huyHoaDonQuaHan30Phut();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        loadDataMonAn("FO", danhSachMonAn);
        // Gán dữ liệu vào TableView
//     tableHoaDon.setItems(hoaDonList);

        tableHoaDon.setRowFactory(tv -> {
            TableRow<HoaDon> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    dsHoaDon.setVisible(false);
                    ttHoaDon.setVisible(true);
                    HoaDon selectedHoaDon = row.getItem();
                    hienThiThongTinHoaDon(selectedHoaDon);
                    sdtCu = selectedHoaDon.getSDT();
                }
            });
            return row;
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
            } else {
                // Nếu nhập không hợp lệ, thiết lập lại giá trị cũ
                tableDatMon.refresh();
            }
        });
        colTenMon.setCellValueFactory(new PropertyValueFactory<>("tenMonAn"));
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        colGiaTien.setCellValueFactory(new PropertyValueFactory<>("gia"));

        colThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
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

        txtSDT.setOnAction(event -> {
            String sdt = txtSDT.getText();
            KhachHang kh = findKhachHangBySdt(sdt);
            if (kh != null && !kh.getsDT().equals("Không")) {
                txtTenKH.setText(kh.getTenKH());
                txtDiemTL.setText(String.valueOf(kh.getDiemTichLuy()));
                sdtCu = txtSDT.getText();
            } else {
                txtSDT.setText(sdtCu);
                showAlertForAddCustomer();
            }
        });

        txtSoLuongKH.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) { // Kiểm tra xem chuỗi mới có phải là số hay không
                return change;
            }
            return null; // Không cho phép nhập nếu không phải là số
        }));


        txtSoLuongKH.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // Kiểm tra khi mất tiêu điểm (con trỏ ra ngoài TextField)
            if (!newValue) {
                String text = txtSoLuongKH.getText();
                Ban ban = findBanByMa(txtMaBan.getText());
                // Kiểm tra nếu giá trị trống hoặc không phải số hoặc số lớn hơn 10
                if (text.isEmpty() || Integer.parseInt(text) > ban.getSoLuongGhe() || Integer.parseInt(text) == 0) {
                    // Hiển thị thông báo lỗi
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Số lượng không được lớn hơn số ghế của bàn và không được để trống.");
                    alert.showAndWait();

                    // Trả lại con trỏ vào trường txtSoLuongKH
                    txtSoLuongKH.clear();
                }
            }
        });

        txtMaBan.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // Kiểm tra khi mất tiêu điểm (con trỏ ra ngoài TextField)
            if (!newValue) {
                String text = txtMaBan.getText();
                Ban ban = findBanByMa(text);
                // Kiểm tra nếu giá trị trống hoặc không phải số hoặc số lớn hơn 10
                if (text.isEmpty() || ban == null) {
                    // Hiển thị thông báo lỗi
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Bàn không tồn tại!");
                    alert.showAndWait();

                    // Trả lại con trỏ vào trường txtSoLuongKH
                    txtMaBan.clear();
                } else {
                    txtSoLuongKH.clear();
                }
            }
        });

        ngayDatBan.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Nếu DatePicker mất tiêu điểm
                LocalDate selectedDate = ngayDatBan.getValue();
                LocalDate today = LocalDate.now();

                if (selectedDate == null) {
                    // Hiển thị thông báo lỗi nếu để trống
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ngày không hợp lệ");
                    alert.setHeaderText(null);
                    alert.setContentText("Vui lòng chọn ngày đặt bàn.");
                    alert.showAndWait();
                    ngayDatBan.setValue(null);

                } else {
                    long daysBetween = today.until(selectedDate).getDays();

                    if (daysBetween < 1 || daysBetween > 7) {
                        // Hiển thị thông báo lỗi
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ngày không hợp lệ");
                        alert.setHeaderText(null);
                        alert.setContentText("Vui lòng chọn ngày từ 1 đến 7 ngày kể từ ngày hiện tại.");
                        alert.showAndWait();

                        // Đặt lại giá trị của DatePicker về rỗng
                        ngayDatBan.setValue(null);
                    }
                }
            }
        });

        gioDatBan.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && (newValue == 20 || newValue == 11)) {
                phutDatBan.setValue(0); // Đặt phút mặc định là 0
                phutDatBan.setDisable(true);
            }
            else if(!gioDatBan.isDisable()){
                phutDatBan.setDisable(false);
            }
        });
    }

    @FXML
    public void chonTrangDoUong() {
       loadDataMonAn("DR", danhSachMonAn);
       check = 2;
    }

    @FXML
    public void chonTrangMonAn() {
        loadDataMonAn("FO", danhSachMonAn);
        check = 1;
    }

    @FXML
    public void chonTrangRuouBia() {
        loadDataMonAn("AC", danhSachMonAn);
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

    public void clickBtnDatBan() {

        try {
            // Tải file FXML và lấy controller
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

    public void quayLai() {
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

    private void hienThiThongTinHoaDon(HoaDon hoaDon) {
        if(hoaDon.getTrangThaiHoaDon().name().equals("DA_THANH_TOAN")) {
            btnCapNhat.setDisable(true);
            btnHuy.setDisable(true);
            btnCheckIn.setDisable(true);
        } else
        {
            btnCapNhat.setDisable(false);
            btnHuy.setDisable(false);
            btnCheckIn.setDisable(false);
        }
        if(hoaDon.getHinhThuc().name().equals("DAT_BAN")) {
            btnCheckIn.setText("Đã CheckIn");
            chuaCheck.setIconLiteral("fas-check");
        } else
        {
            btnCheckIn.setText("Chưa CheckIn");
            chuaCheck.setIconLiteral("fas-times");
        }

        if(hoaDon.isCheckIn()) {
            btnCheckIn.setText("Đã CheckIn");
            chuaCheck.setIconLiteral("fas-check");
        }
        if(btnCheckIn.getText().equals("Đã CheckIn")) {
            ngayDatBan.setDisable(true);
            gioDatBan.setDisable(true);
            phutDatBan.setDisable(true);
            txtSDT.setDisable(true);
        }  else {
            ngayDatBan.setDisable(false);
            gioDatBan.setDisable(false);
            phutDatBan.setDisable(false);
            txtSDT.setDisable(false);
        }
        if(LocalDate.now().plusDays(1).isAfter(hoaDon.getNgayDat())) {
            ngayDatBan.setDisable(true);
            gioDatBan.setDisable(true);
            phutDatBan.setDisable(true);
        }
        // Điền thông tin từ hóa đơn vào các textfield, combobox, và datepicker
        txtMaHD.setText(hoaDon.getMaHD());
        txtMaHD.setDisable(true);
        txtSDT.setText(hoaDon.getKhachHang().getsDT());
        txtTrangThaiHD.setText(hoaDon.getTrangThaiHoaDon().getDisplayName());
        txtTrangThaiHD.setDisable(true);
        txtTenKH.setText(hoaDon.getKhachHang().getTenKH());
        txtTenKH.setDisable(true);
       txtNhanVien.setText(hoaDon.getNhanVien().getMaNV());
       txtNhanVien.setDisable(true);
       txtDiemTL.setText(hoaDon.getKhachHang().getDiemTichLuy() +"");
       txtDiemTL.setDisable(true);
       txtSoLuongKH.setText(hoaDon.getSoLuongKH() +"");
       txtMaBan.setText(hoaDon.getMaBan());
       ngayDatBan.setValue(hoaDon.getNgayDat());
        phutDatBan.setItems(FXCollections.observableArrayList(
                0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55
        ));
        int[] gioChon = {8, 9, 10, 11, 18, 19, 20}; // Các giờ mong muốn
        for (int gio : gioChon) {
            gioDatBan.getItems().add(gio); // Thêm các giờ vào ComboBox
        }
        gioDatBan.setValue(hoaDon.getGioDatBan().getHour());
        phutDatBan.setValue(hoaDon.getGioDatBan().getMinute());
        ObservableList<ChiTietHD_MonAn> chiTietList = chiTietHDMonAnDao.getChiTietHD(hoaDon.getMaHD());
        tableDatMon.setItems(chiTietList);
//        System.out.println(hoaDon);
        lblTienCoc.setText(hoaDon.getTienCoc() + " VNĐ");
        lblTongTien.setText(hoaDon.getTongTien() + " VNĐ");
    }

    private void tinhTongTienVaTienCoc(ChiTietHD_MonAn datMon, int soLuong) {

        // Lấy giá trị từ Label và chuyển đổi thành double
        String textTT = lblTongTien.getText().replace("VNĐ", "").trim(); // Bỏ "VNĐ" và khoảng trắng
        double valueTT = Double.parseDouble(textTT);
        valueTT = valueTT + (soLuong* datMon.getGia() + soLuong * datMon.getGia() * datMon.getVAT() * 0.01);


        // Cập nhật Label
        lblTongTien.setText(valueTT + " VNĐ");
    }
@FXML
    private void chonCheckIn() {
        if(btnCheckIn.getText().equals("Chưa CheckIn")) {
            if(!LocalDate.now().equals(ngayDatBan.getValue())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Chưa đến ngày để Check-in!");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận");
            alert.setHeaderText("Bạn có chắc chắn CheckIn không?");
            alert.setContentText("Lưu ý sau khi CheckIn sẽ không được sửa đổi thông tin đặt bàn!");
            // Hiển thị hộp thoại và chờ người dùng chọn
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                btnCheckIn.setText("Đã CheckIn");
                chuaCheck.setIconLiteral("fas-check");
                ngayDatBan.setDisable(true);
                gioDatBan.setDisable(true);
                phutDatBan.setDisable(true);
                txtSDT.setDisable(true);
                String maHD = txtMaHD.getText();
                hoaDonDAO.updateCheckIn(maHD, true);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Hóa đơn này đã được Check-in!");
            Optional<ButtonType> result = alert.showAndWait();
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

    private void showAlertForAddCustomer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông Báo");
        alert.setHeaderText(null);
        alert.setContentText("Chưa có thông tin của khách hàng hoặc là khách vãng lai");

        ButtonType buttonTypeAdd = new ButtonType("Thêm Khách Hàng");
        ButtonType buttonTypeClose = new ButtonType("Đóng");
        alert.getButtonTypes().setAll(buttonTypeAdd, buttonTypeClose);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeAdd) {
                clickBtnThemKH();
            }
        });
    }
    public void chonCapNhat() {
        if(txtSoLuongKH.getText().isEmpty() || txtMaBan.getText().isEmpty() || ngayDatBan.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông Báo");
            alert.setHeaderText("Thiếu thông tin");
            alert.setContentText("Vui lòng nhập đầy đủ các thông tin cần thiết.");
            alert.showAndWait();
        } else {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xác nhận cập nhật hóa đơn");
            confirmAlert.setHeaderText("Bạn có chắc chắn muốn cập nhật hóa đơn này không?");

            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {

                    listHoaDon3.clear();  // Xóa dữ liệu cũ trước khi tải dữ liệu mới
                    listHoaDon3.addAll(hoaDonDAO.getHoaDonList2(txtMaBan.getText(), java.sql.Date.valueOf(ngayDatBan.getValue()))); // Lấy dữ liệu từ DAO
                    if(listHoaDon3.size() > 0) {
                        for(HoaDon hd : listHoaDon3) {
                            if(((hd.getGioDatBan().getHour() <= 11 && gioDatBan.getValue() <= 11) || (hd.getGioDatBan().getHour() >=18 && gioDatBan.getValue() >= 18)) && !hd.getMaHD().equals(txtMaHD.getText())) {
                                System.out.println(hd.getMaHD());
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Lỗi");
                                alert.setHeaderText("Bàn " + txtMaBan.getText() + " lúc này không trống!");
                                alert.showAndWait();
                                return;
                            }
                        }
                    }

                    String sdt = txtSDT.getText();
                    KhachHang kh = findKhachHangBySdt(sdt);

                    if (kh != null ) {
                        String maHoaDon = txtMaHD.getText();
                        String maKH = kh.getMaKH();
                        String maBan = txtMaBan.getText();
                        int soLuongKH = Integer.parseInt(txtSoLuongKH.getText());
                        LocalDate ngayDat = ngayDatBan.getValue();
                        int gio = gioDatBan.getValue();
                        int phut = phutDatBan.getValue();
                        LocalTime gioDatBan = LocalTime.of(gio, phut);
                        String textTT = lblTongTien.getText().replace("VNĐ", "").trim(); // Bỏ "VNĐ" và khoảng trắng
                        double valueTT = Double.parseDouble(textTT);
                        hoaDonDAO.capNhatHoaDon(maHoaDon, maKH, maBan, soLuongKH, ngayDat, gioDatBan, valueTT);

                        chiTietHDMonAnDao.deleteAllByMaHD(maHoaDon);
                        saveChiTietHD_MonAn(maHoaDon);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Cập nhật thành công");
                        alert.setHeaderText(null);
                        alert.setContentText("Hóa đơn đã được cập nhật thành công!");
                        alert.showAndWait();
                        if(!txtMaBan.getText().equals(banID)) {
                            quayLai();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText("Lỗi Cập Nhật Hóa Đơn");
                        alert.setContentText("Số điện thoại khách hàng không chính xác!");
                        alert.showAndWait();
                        return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("Đã xảy ra lỗi");
                    alert.setContentText("Vui lòng kiểm tra lại thông tin.");
                    alert.showAndWait();
                }
            }
        }
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

    private MonAn findMonAn(String sdt) {
        for (MonAn kh : danhSachMonAn) {
            if (kh.getTenMonAn().equals(sdt)) {
                return kh;
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }

    private HoaDon findHoaDonByMa(String ma) {
        danhSachHD = FXCollections.observableArrayList(hoaDonDAO.getHoaDonList(txtMaBan.getText()));
        for (HoaDon hd : danhSachHD) {
            if (hd.getMaHD().equals(ma)) {
                return hd;
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }
@FXML
    private void chonHuy() {
    String maHoaDon = txtMaHD.getText();
    // Hiển thị thông báo xác nhận
    if (btnCheckIn.getText().equals("Chưa CheckIn")) {
        HoaDon hd = findHoaDonByMa(txtMaHD.getText());
        LocalDate today = LocalDate.now();
        if(hd.getTienCoc() > 0 && today.plusDays(1).isAfter(hd.getNgayDat())) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xác nhận hủy hóa đơn");
            confirmAlert.setHeaderText("Bạn có chắc chắn muốn hủy hóa đơn này không?");
            confirmAlert.setContentText("Hóa đơn này đã quá ngày nên không thể hoàn cọc");

            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Nếu người dùng chọn "OK", tiến hành hủy hóa đơn
                try {
                    hoaDonDAO.huyHoaDon(maHoaDon);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Hủy thành công");
                    alert.setHeaderText(null);
                    alert.setContentText("Hóa đơn đã bị hủy!");
                    alert.showAndWait();
                    quayLai();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("Đã xảy ra lỗi");
                    alert.setContentText("Vui lòng kiểm tra lại thông tin.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xác nhận hủy hóa đơn");
            confirmAlert.setHeaderText("Bạn có chắc chắn muốn hủy hóa đơn này không?");
            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Nếu người dùng chọn "OK", tiến hành hủy hóa đơn
                try {
                    hoaDonDAO.capNhatHoaDonBiHuy(maHoaDon);
                    hoaDonDAO.huyHoaDon(maHoaDon);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Hủy thành công");
                    alert.setHeaderText(null);
                    alert.setContentText("Hóa đơn đã bị hủy!");
                    alert.showAndWait();
                    quayLai();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("Đã xảy ra lỗi");
                    alert.setContentText("Vui lòng kiểm tra lại thông tin.");
                    alert.showAndWait();
                }
            }
        }
    } else {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Hóa đơn này đã được Check-in nên không thể hủy!");
        Optional<ButtonType> result = alert.showAndWait();
    }
}

@FXML
    private void timHoaDon() {
    listHoaDon.clear();  // Xóa dữ liệu cũ trước khi tải dữ liệu mới
    listHoaDon.addAll(hoaDonDAO.getHoaDonList(maBan.getText())); // Lấy dữ liệu từ DAO

    String sdt = txtTimSDT.getText().toLowerCase();
    String tenKH = txtTimKH.getText().toLowerCase();
    LocalDate ngayDatBan = timNgayDatBan.getValue();
    String selectedTrangThai = trangThaiHD.getValue();


    // Tạo danh sách kết quả tìm kiếm
    ObservableList<HoaDon> searchResult = FXCollections.observableArrayList();
    // Lọc dữ liệu từ danh sách hoaDonList dựa trên thông tin tìm kiếm
    if (selectedTrangThai == null || selectedTrangThai.isEmpty()) {
        for (HoaDon hd : listHoaDon) {

            boolean matchesTenKH = tenKH.isEmpty() || hd.getTenKH().toLowerCase().contains(tenKH);
            boolean matchesSDT = sdt.isEmpty() || hd.getSDT().toLowerCase().contains(sdt);
            boolean matchesNgayTao = ngayDatBan == null || hd.getNgayTaoHD().isEqual(ngayDatBan);
            if (matchesTenKH && matchesSDT && matchesNgayTao  ) {
                searchResult.add(hd);
            }
        }
        // Hiển thị kết quả tìm kiếm trong bảng
        tableHoaDon.setItems(searchResult);
    } else {
    for (HoaDon hd : listHoaDon) {

        boolean matchesTenKH = tenKH.isEmpty() || hd.getTenKH().toLowerCase().contains(tenKH);
        boolean matchesSDT = sdt.isEmpty() || hd.getSDT().toLowerCase().contains(sdt);
        boolean matchesNgayTao = ngayDatBan == null || hd.getNgayTaoHD().isEqual(ngayDatBan);
        boolean matchesTrangThaiHD = selectedTrangThai.isEmpty() || hd.getTrangThaiHoaDon().getDisplayName().contains(selectedTrangThai);
        if (matchesTenKH && matchesSDT && matchesNgayTao && matchesTrangThaiHD    ) {
            searchResult.add(hd);
        }
    }
    // Hiển thị kết quả tìm kiếm trong bảng
    tableHoaDon.setItems(searchResult); }
}

    private Ban findBanByMa(String ma) {
        for (Ban ban : danhSachBan) {
            if (ban.getMaBan().equals(ma)) {
                return ban;
            }
        }
        return null; // Trả về null nếu không tìm thấy
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

        loadDataMonAn(ma, searchResult);
    }

    private void huyHoaDonQuaHan30Phut() {
        listHoaDon.clear();  // Xóa dữ liệu cũ trước khi tải dữ liệu mới
        listHoaDon.addAll(hoaDonDAO.getHoaDonList(banID)); // Lấy dữ liệu từ DAO

        for(HoaDon hd : listHoaDon) {
            LocalDateTime ngayGioDatBan = hd.getNgayDat().atTime(hd.getGioDatBan());
            LocalDateTime ngayGioHienTai = LocalDateTime.now();

            if(ngayGioHienTai.isAfter(ngayGioDatBan.plusMinutes(30)) && !hd.isCheckIn()){
                try{
                    hoaDonDAO.huyHoaDon(hd.getMaHD());
                    
                    clickBtnHoaDon();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
