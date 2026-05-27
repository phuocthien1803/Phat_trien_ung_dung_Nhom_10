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

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleObjectProperty;

public class QuanLyHoaDon_Control {

    @FXML private ComboBox<String> trangThaiHD;
    @FXML private AnchorPane paneDatBan;
    @FXML private TableView<HoaDon> tableHoaDon;
    @FXML private TableColumn<HoaDon, String> colTenKH;
    @FXML private TableColumn<HoaDon, String> colSDT;
    @FXML private TableColumn<HoaDon, LocalDate> colNgayTaoHD;
    @FXML private TableColumn<HoaDon, Time> colGioDat;
    @FXML private TableColumn<HoaDon, LocalDate> colNgayDat;
    @FXML private TableColumn<HoaDon, String> colMaHD;
    @FXML private TableColumn<HoaDon, Text> colTrangThaiHD;
    @FXML private Label maBan;
    @FXML private BorderPane ttHoaDon;
    @FXML private BorderPane dsHoaDon;
    @FXML private TextField txtMaHD;
    @FXML private TextField txtNhanVien;
    @FXML private TextField txtSDT;
    @FXML private TextField txtSoLuongKH;
    @FXML private TextField txtTenKH;
    @FXML private TextField txtTrangThaiHD;
    @FXML private TextField txtDiemTL;
    @FXML private TextField txtMaBan;
    @FXML private ComboBox<Integer> gioDatBan;
    @FXML private ComboBox<Integer> phutDatBan;
    @FXML private DatePicker ngayDatBan;
    @FXML private TableColumn<ChiTietHD_MonAn, String> colTenMon;
    @FXML private TableColumn<ChiTietHD_MonAn, Double> colGiaTien;
    @FXML private TableColumn<ChiTietHD_MonAn, String> colGhiChu;
    @FXML private TableColumn<ChiTietHD_MonAn, Double> colThanhTien;
    @FXML private TableColumn<ChiTietHD_MonAn, Integer> colSoLuong;
    @FXML private TableColumn<ChiTietHD_MonAn, Double> colVAT;
    @FXML private TableView<ChiTietHD_MonAn> tableDatMon;
    @FXML private Label lblTienCoc;
    @FXML private Label lblTongTien;
    @FXML private TableColumn<MonAn, String> colMonAn;
    @FXML private TableColumn<MonAn, String> colGia;
    @FXML private Button btnCapNhat;
    @FXML private Button btnCheckIn;
    @FXML private Button btnHuy;
    @FXML private FontIcon chuaCheck;
    @FXML private DatePicker timNgayDatBan;
    @FXML private TextField txtTimKH;
    @FXML private TextField txtTimSDT;
    @FXML private TextField txtTenMonCanTim;
    @FXML private GridPane gridPaneMenu;

    private MonAn_DAO monAnDao = new MonAn_DAO();
    ObservableList<MonAn> danhSachMonAn = FXCollections.observableArrayList(monAnDao.getMonAnList());
    private QuanLyHoaDon_DAO hoaDonDAO = new QuanLyHoaDon_DAO();
    private ObservableList<HoaDon> listHoaDon  = FXCollections.observableArrayList();
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

    // [SỬA LỖI 1a] Lưu Timeline vào field để có thể dừng sau này
    private Timeline timeline;

    public void setBanID(String banID) {
        this.banID = banID;
        Platform.runLater(() -> maBan.setText(banID));
        loadData();
    }

    public void loadData() {
        if (banID == null || banID.isEmpty()) {
            System.out.println("banID is null or empty!");
            return;
        }

        listHoaDon.clear();
        listHoaDon.addAll(hoaDonDAO.getHoaDonList(banID));

        // Hủy HĐ quá hạn ngay khi load (không cần chờ Timeline)
        LocalDateTime now = LocalDateTime.now();
        Iterator<HoaDon> it = listHoaDon.iterator();
        while (it.hasNext()) {
            HoaDon hd = it.next();
            // [SỬA LỖI 3] Guard null trước khi dùng atTime()
            if (hd.getNgayDat() == null || hd.getGioDatBan() == null) continue;
            LocalDateTime ngayGioDatBan = hd.getNgayDat().atTime(hd.getGioDatBan());
            if (now.isAfter(ngayGioDatBan.plusMinutes(30)) && !hd.isCheckIn()) {
                try {
                    hoaDonDAO.huyHoaDon(hd.getMaHD());
                    it.remove(); // Xóa khỏi list luôn
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // [SỬA LỖI 2] Clear bảng TRƯỚC khi add để tránh tích lũy bản ghi
        tableHoaDon.getItems().clear();
        for (HoaDon hd : listHoaDon) {
            if (hd.getTrangThaiHoaDon().getDisplayName().equals("Chưa thanh toán")) {
                tableHoaDon.getItems().add(hd);
            }
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
                            rowIndex = (rowIndex != null) ? rowIndex : 0;
                            colIndex = (colIndex != null) ? colIndex : 0;

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
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Thông báo");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Món ăn đã có trong danh sách đặt món!");
                                    alert.showAndWait();
                                } else {
                                    dialog.showAndWait().ifPresent(ghiChu -> {
                                        ChiTietHD_MonAn datMon = new ChiTietHD_MonAn(tenMon, ghiChu, gia, soLuong, gia * soLuong, vat);
                                        tableDatMon.getItems().add(datMon);
                                        tableDatMon.setEditable(true);
                                        tinhTongTienVaTienCoc(datMon, 1);
                                    });
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Cảnh báo");
                                alert.setHeaderText(null);
                                alert.setContentText("Vui lòng CheckIn để đặt món");
                                alert.showAndWait();
                            }
                        }
                    });
                    cardMonAnControl.setData(monAn);

                    if (column == 3) {
                        column = 0;
                        row += 1;
                    }
                    gridPaneMenu.add(pane, column++, row);
                    GridPane.setMargin(pane, new Insets(5.5));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // initialize
    @FXML
    public void initialize() {
        ObservableList<String> hinhThucList = FXCollections.observableArrayList("Chưa thanh toán", "Đã thanh toán");
        trangThaiHD.setItems(hinhThucList);
        trangThaiHD.setValue("Chưa thanh toán");
        timHoaDon();

        dsHoaDon.setVisible(true);
        ttHoaDon.setVisible(false);

        colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("sDT"));
        colNgayTaoHD.setCellValueFactory(new PropertyValueFactory<>("ngayTaoHD"));
        colGioDat.setCellValueFactory(new PropertyValueFactory<>("gioDatBan"));
        colMaHD.setCellValueFactory(new PropertyValueFactory<>("maHD"));
        colNgayDat.setCellValueFactory(new PropertyValueFactory<>("ngayDat"));
        colTrangThaiHD.setCellValueFactory(data -> {
            HoaDon hoaDon = data.getValue();
            Text statusText = new Text();
            if (hoaDon.isCheckIn()) {
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
            return new SimpleObjectProperty<>(statusText);
        });

        if (banID != null) {
            loadData();
        }

        // [SỬA LỖI 1a] Lưu vào field thay vì biến local
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            tableHoaDon.refresh();
            huyHoaDonQuaHan30Phut();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // [SỬA LỖI 1b] Dừng timeline khi controller bị gỡ khỏi scene
        // Khi paneDatBan (root của controller này) bị xóa khỏi scene graph
        // (vì một controller khác được load vào), scene sẽ trở thành null.
        // Đây là thời điểm dừng Timeline để không tiếp tục chạy vô ích.
        tableHoaDon.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) {
                stopTimeline();
            }
        });

        loadDataMonAn("FO", danhSachMonAn);

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
            if (soLuong != null && soLuong > 0) {
                chiTietHDMonAn.setSoLuong(soLuong);
                double giaTien = chiTietHDMonAn.getGia();
                chiTietHDMonAn.setThanhTien(giaTien * soLuong);
                tinhTongTienVaTienCoc(chiTietHDMonAn, soLuong - soLuongCu);
                tableDatMon.refresh();
            } else if (soLuong != null && soLuong == 0) {
                tableDatMon.getItems().remove(chiTietHDMonAn);
                tinhTongTienVaTienCoc(chiTietHDMonAn, soLuong - soLuongCu);
            } else {
                tableDatMon.refresh();
            }
        });

        colTenMon.setCellValueFactory(new PropertyValueFactory<>("tenMonAn"));
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        colGiaTien.setCellValueFactory(new PropertyValueFactory<>("gia"));
        colThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        colVAT.setCellValueFactory(new PropertyValueFactory<>("VAT"));
        colVAT.setCellFactory(col -> new TableCell<ChiTietHD_MonAn, Double>() {
            @Override
            protected void updateItem(Double vat, boolean empty) {
                super.updateItem(vat, empty);
                if (empty || vat == null) {
                    setText(null);
                } else {
                    setText(vat + "%");
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
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));

        txtSoLuongKH.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String text = txtSoLuongKH.getText();
                Ban ban = findBanByMa(txtMaBan.getText());
                if (text.isEmpty() || Integer.parseInt(text) > ban.getSoLuongGhe() || Integer.parseInt(text) == 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Số lượng không được lớn hơn số ghế của bàn và không được để trống.");
                    alert.showAndWait();
                    txtSoLuongKH.clear();
                }
            }
        });

        txtMaBan.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String text = txtMaBan.getText();
                Ban ban = findBanByMa(text);
                if (text.isEmpty() || ban == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Bàn không tồn tại!");
                    alert.showAndWait();
                    txtMaBan.clear();
                } else {
                    txtSoLuongKH.clear();
                }
            }
        });

        ngayDatBan.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                LocalDate selectedDate = ngayDatBan.getValue();
                LocalDate today = LocalDate.now();
                if (selectedDate == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ngày không hợp lệ");
                    alert.setHeaderText(null);
                    alert.setContentText("Vui lòng chọn ngày đặt bàn.");
                    alert.showAndWait();
                    ngayDatBan.setValue(null);
                } else {
                    long daysBetween = today.until(selectedDate).getDays();
                    if (daysBetween < 1 || daysBetween > 7) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ngày không hợp lệ");
                        alert.setHeaderText(null);
                        alert.setContentText("Vui lòng chọn ngày từ 1 đến 7 ngày kể từ ngày hiện tại.");
                        alert.showAndWait();
                        ngayDatBan.setValue(null);
                    }
                }
            }
        });

        gioDatBan.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && (newValue == 20 || newValue == 11)) {
                phutDatBan.setValue(0);
                phutDatBan.setDisable(true);
            } else if (!gioDatBan.isDisable()) {
                phutDatBan.setDisable(false);
            }
        });
    }

    // Dừng Timeline
    /**
     * [SỬA LỖI 1] Dừng Timeline khi controller không còn được dùng.
     * Được gọi tự động qua scene listener, hoặc có thể gọi thủ công khi cần.
     */
    public void stopTimeline() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }

    // Menu tabs
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

    // Navigation
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/QuanLyBan.fxml"));
            Parent DatBanContent = loader.load();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/QuanLyHoaDon.fxml"));
            Parent DatBanContent = loader.load();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/QuanLyHoaDon.fxml"));
            Parent DatBanContent = loader.load();
            QuanLyHoaDon_Control controller = loader.getController();
            controller.setBanID(banID);
            paneDatBan.getChildren().clear();
            paneDatBan.getChildren().add(DatBanContent);
            paneDatBan.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hiển thị thông tin hóa đơn
    private void hienThiThongTinHoaDon(HoaDon hoaDon) {
        if (hoaDon.getTrangThaiHoaDon().name().equals("DA_THANH_TOAN")) {
            btnCapNhat.setDisable(true);
            btnHuy.setDisable(true);
            btnCheckIn.setDisable(true);
        } else {
            btnCapNhat.setDisable(false);
            btnHuy.setDisable(false);
            btnCheckIn.setDisable(false);
        }
        if (hoaDon.getHinhThuc().name().equals("DAT_BAN")) {
            btnCheckIn.setText("Đã CheckIn");
            chuaCheck.setIconLiteral("fas-check");
        } else {
            btnCheckIn.setText("Chưa CheckIn");
            chuaCheck.setIconLiteral("fas-times");
        }
        if (hoaDon.isCheckIn()) {
            btnCheckIn.setText("Đã CheckIn");
            chuaCheck.setIconLiteral("fas-check");
        }
        if (btnCheckIn.getText().equals("Đã CheckIn")) {
            ngayDatBan.setDisable(true);
            gioDatBan.setDisable(true);
            phutDatBan.setDisable(true);
            txtSDT.setDisable(true);
        } else {
            ngayDatBan.setDisable(false);
            gioDatBan.setDisable(false);
            phutDatBan.setDisable(false);
            txtSDT.setDisable(false);
        }
        if (LocalDate.now().plusDays(1).isAfter(hoaDon.getNgayDat())) {
            ngayDatBan.setDisable(true);
            gioDatBan.setDisable(true);
            phutDatBan.setDisable(true);
        }
        txtMaHD.setText(hoaDon.getMaHD());
        txtMaHD.setDisable(true);
        txtSDT.setText(hoaDon.getKhachHang().getsDT());
        txtTrangThaiHD.setText(hoaDon.getTrangThaiHoaDon().getDisplayName());
        txtTrangThaiHD.setDisable(true);
        txtTenKH.setText(hoaDon.getKhachHang().getTenKH());
        txtTenKH.setDisable(true);
        txtNhanVien.setText(hoaDon.getNhanVien().getMaNV());
        txtNhanVien.setDisable(true);
        txtDiemTL.setText(hoaDon.getKhachHang().getDiemTichLuy() + "");
        txtDiemTL.setDisable(true);
        txtSoLuongKH.setText(hoaDon.getSoLuongKH() + "");
        txtMaBan.setText(hoaDon.getMaBan());
        ngayDatBan.setValue(hoaDon.getNgayDat());
        phutDatBan.setItems(FXCollections.observableArrayList(
                0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55));
        int[] gioChon = {8, 9, 10, 11, 18, 19, 20};
        for (int gio : gioChon) {
            gioDatBan.getItems().add(gio);
        }
        gioDatBan.setValue(hoaDon.getGioDatBan().getHour());
        phutDatBan.setValue(hoaDon.getGioDatBan().getMinute());
        ObservableList<ChiTietHD_MonAn> chiTietList = chiTietHDMonAnDao.getChiTietHD(hoaDon.getMaHD());
        tableDatMon.setItems(chiTietList);
        lblTienCoc.setText(hoaDon.getTienCoc() + " VNĐ");
        lblTongTien.setText(hoaDon.getTongTien() + " VNĐ");
    }

    // Tính tiền
    private void tinhTongTienVaTienCoc(ChiTietHD_MonAn datMon, int soLuong) {
        String textTT = lblTongTien.getText().replace("VNĐ", "").trim();
        double valueTT = Double.parseDouble(textTT);
        valueTT = valueTT + (soLuong * datMon.getGia() + soLuong * datMon.getGia() * datMon.getVAT() * 0.01);
        lblTongTien.setText(valueTT + " VNĐ");
    }

    // CheckIn
    @FXML
    private void chonCheckIn() {
        if (btnCheckIn.getText().equals("Chưa CheckIn")) {
            if (!LocalDate.now().equals(ngayDatBan.getValue())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Chưa đến ngày để Check-in!");
                alert.showAndWait();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận");
            alert.setHeaderText("Bạn có chắc chắn CheckIn không?");
            alert.setContentText("Lưu ý sau khi CheckIn sẽ không được sửa đổi thông tin đặt bàn!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                btnCheckIn.setText("Đã CheckIn");
                chuaCheck.setIconLiteral("fas-check");
                ngayDatBan.setDisable(true);
                gioDatBan.setDisable(true);
                phutDatBan.setDisable(true);
                txtSDT.setDisable(true);
                hoaDonDAO.updateCheckIn(txtMaHD.getText(), true);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Hóa đơn này đã được Check-in!");
            alert.showAndWait();
        }
    }

    // Hủy hóa đơn
    @FXML
    private void chonHuy() {
        String maHoaDon = txtMaHD.getText();
        if (btnCheckIn.getText().equals("Chưa CheckIn")) {
            HoaDon hd = findHoaDonByMa(maHoaDon);
            LocalDate today = LocalDate.now();
            if (hd.getTienCoc() > 0 && today.plusDays(1).isAfter(hd.getNgayDat())) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Xác nhận hủy hóa đơn");
                confirmAlert.setHeaderText("Bạn có chắc chắn muốn hủy hóa đơn này không?");
                confirmAlert.setContentText("Hóa đơn này đã quá ngày nên không thể hoàn cọc");
                Optional<ButtonType> result = confirmAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
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
                        showLoi();
                    }
                }
            } else {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Xác nhận hủy hóa đơn");
                confirmAlert.setHeaderText("Bạn có chắc chắn muốn hủy hóa đơn này không?");
                Optional<ButtonType> result = confirmAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
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
                        showLoi();
                    }
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Hóa đơn này đã được Check-in nên không thể hủy!");
            alert.showAndWait();
        }
    }

    // Cập nhật hóa đơn
    public void chonCapNhat() {
        if (txtSoLuongKH.getText().isEmpty() || txtMaBan.getText().isEmpty() || ngayDatBan.getValue() == null) {
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
                    listHoaDon3.clear();
                    listHoaDon3.addAll(hoaDonDAO.getHoaDonList2(txtMaBan.getText(),
                            java.sql.Date.valueOf(ngayDatBan.getValue())));
                    for (HoaDon hd : listHoaDon3) {
                        if (((hd.getGioDatBan().getHour() <= 11 && gioDatBan.getValue() <= 11)
                                || (hd.getGioDatBan().getHour() >= 18 && gioDatBan.getValue() >= 18))
                                && !hd.getMaHD().equals(txtMaHD.getText())) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Lỗi");
                            alert.setHeaderText("Bàn " + txtMaBan.getText() + " lúc này không trống!");
                            alert.showAndWait();
                            return;
                        }
                    }

                    KhachHang kh = findKhachHangBySdt(txtSDT.getText());
                    if (kh != null) {
                        String maHoaDon = txtMaHD.getText();
                        int soLuongKH = Integer.parseInt(txtSoLuongKH.getText());
                        LocalDate ngayDat = ngayDatBan.getValue();
                        LocalTime gioDatBanValue = LocalTime.of(gioDatBan.getValue(), phutDatBan.getValue());
                        double valueTT = Double.parseDouble(lblTongTien.getText().replace("VNĐ", "").trim());
                        hoaDonDAO.capNhatHoaDon(maHoaDon, kh.getMaKH(), txtMaBan.getText(),
                                soLuongKH, ngayDat, gioDatBanValue, valueTT);
                        chiTietHDMonAnDao.deleteAllByMaHD(maHoaDon);
                        saveChiTietHD_MonAn(maHoaDon);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Cập nhật thành công");
                        alert.setHeaderText(null);
                        alert.setContentText("Hóa đơn đã được cập nhật thành công!");
                        alert.showAndWait();
                        if (!txtMaBan.getText().equals(banID)) quayLai();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText("Lỗi Cập Nhật Hóa Đơn");
                        alert.setContentText("Số điện thoại khách hàng không chính xác!");
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showLoi();
                }
            }
        }
    }

    // Tìm kiếm
    @FXML
    private void timHoaDon() {
        // [SỬA LỖI 4] Guard: không truy vấn DB khi banID chưa được set
        if (banID == null || banID.isEmpty()) return;

        listHoaDon.clear();
        listHoaDon.addAll(hoaDonDAO.getHoaDonList(banID));

        String sdt = txtTimSDT.getText().toLowerCase();
        String tenKH = txtTimKH.getText().toLowerCase();
        LocalDate ngayDatBanFilter = timNgayDatBan.getValue();
        String selectedTrangThai = trangThaiHD.getValue();

        ObservableList<HoaDon> searchResult = FXCollections.observableArrayList();
        for (HoaDon hd : listHoaDon) {
            boolean matchesTenKH  = tenKH.isEmpty()           || hd.getTenKH().toLowerCase().contains(tenKH);
            boolean matchesSDT    = sdt.isEmpty()             || hd.getSDT().toLowerCase().contains(sdt);
            boolean matchesNgay   = ngayDatBanFilter == null  || hd.getNgayTaoHD().isEqual(ngayDatBanFilter);
            boolean matchesTrangThai = selectedTrangThai == null || selectedTrangThai.isEmpty()
                    || hd.getTrangThaiHoaDon().getDisplayName().contains(selectedTrangThai);
            if (matchesTenKH && matchesSDT && matchesNgay && matchesTrangThai) {
                searchResult.add(hd);
            }
        }
        tableHoaDon.setItems(searchResult);
    }

    @FXML
    private void timMonAn() {
        String tenMon = txtTenMonCanTim.getText().toLowerCase();
        ObservableList<MonAn> searchResult = FXCollections.observableArrayList();
        for (MonAn mon : danhSachMonAn) {
            if (tenMon.isEmpty() || mon.getTenMonAn().toLowerCase().contains(tenMon)) {
                searchResult.add(mon);
            }
        }
        String ma = (check == 1) ? "FO" : (check == 2) ? "DR" : "AC";
        loadDataMonAn(ma, searchResult);
    }

    // Hủy hóa đơn quá hạn 30 phút (chạy trong Timeline)
    /**
     * [SỬA LỖI 1c] Không gọi clickBtnHoaDon() nữa.
     *
     * Logic cũ: hủy HĐ → clickBtnHoaDon() → load controller mới → Timeline mới
     *           → sau 1 giây lại gọi huyHoaDonQuaHan30Phut() trên CÙNG LÚC với
     *              Timeline cũ vẫn đang chạy → số Timeline tăng theo cấp số nhân.
     *
     * Logic mới: hủy HĐ → cập nhật listHoaDon và tableHoaDon ngay tại chỗ.
     *   - Thu thập mã HĐ cần hủy (tránh ConcurrentModificationException khi
     *     xóa khỏi list đang duyệt).
     *   - Gọi DB hủy từng HĐ.
     *   - Dùng Platform.runLater để xóa khỏi UI trên JavaFX thread.
     */
    private void huyHoaDonQuaHan30Phut() {
        if (banID == null || banID.isEmpty()) return;

        LocalDateTime now = LocalDateTime.now();

        // Thu thập mã HĐ cần hủy TRƯỚC (tránh sửa list đang duyệt)
        List<String> maHDCanHuy = new ArrayList<>();
        for (HoaDon hd : listHoaDon) {
            // [SỬA LỖI 3] Null-guard cho ngayDat và gioDatBan
            if (hd.getNgayDat() == null || hd.getGioDatBan() == null) continue;
            LocalDateTime ngayGioDatBan = hd.getNgayDat().atTime(hd.getGioDatBan());
            if (now.isAfter(ngayGioDatBan.plusMinutes(30)) && !hd.isCheckIn()) {
                maHDCanHuy.add(hd.getMaHD());
            }
        }

        if (maHDCanHuy.isEmpty()) return; // Không có gì để hủy → thoát sớm

        // Hủy trong DB
        for (String maHD : maHDCanHuy) {
            try {
                hoaDonDAO.huyHoaDon(maHD);
            } catch (SQLException e) {
                System.err.println("[HuyHD] Lỗi khi hủy hóa đơn " + maHD + ": " + e.getMessage());
            }
        }

        // Cập nhật UI trên JavaFX thread (Timeline đã chạy trên FX thread nhưng
        // dùng Platform.runLater để đảm bảo an toàn khi cần thiết)
        Platform.runLater(() -> {
            listHoaDon.removeIf(hd -> maHDCanHuy.contains(hd.getMaHD()));
            tableHoaDon.getItems().removeIf(hd -> maHDCanHuy.contains(hd.getMaHD()));
            tableHoaDon.refresh();
        });
    }

    // Save chi tiết hóa đơn
    private void saveChiTietHD_MonAn(String maHoaDon) {
        for (ChiTietHD_MonAn ct : tableDatMon.getItems()) {
            MonAn monAn = findMonAn(colTenMon.getCellObservableValue(ct).getValue());
            chiTietHDMonAnDao.themChiTietHD_MonAn(maHoaDon, monAn.getMaMonAn(),
                    colSoLuong.getCellObservableValue(ct).getValue(),
                    colThanhTien.getCellObservableValue(ct).getValue(),
                    colGhiChu.getCellObservableValue(ct).getValue());
        }
    }

    // Helpers
    private MonAn findMonAn(String tenMon) {
        for (MonAn ma : danhSachMonAn) {
            if (ma.getTenMonAn().equals(tenMon)) return ma;
        }
        return null;
    }

    private HoaDon findHoaDonByMa(String ma) {
        danhSachHD = FXCollections.observableArrayList(hoaDonDAO.getHoaDonList(txtMaBan.getText()));
        for (HoaDon hd : danhSachHD) {
            if (hd.getMaHD().equals(ma)) return hd;
        }
        return null;
    }

    private KhachHang findKhachHangBySdt(String sdt) {
        for (KhachHang kh : danhSachKH) {
            if (kh.getsDT().equals(sdt)) return kh;
        }
        return null;
    }

    private Ban findBanByMa(String ma) {
        for (Ban ban : danhSachBan) {
            if (ban.getMaBan().equals(ma)) return ban;
        }
        return null;
    }

    private void showAlertForAddCustomer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông Báo");
        alert.setHeaderText(null);
        alert.setContentText("Chưa có thông tin của khách hàng hoặc là khách vãng lai");
        ButtonType buttonTypeAdd   = new ButtonType("Thêm Khách Hàng");
        ButtonType buttonTypeClose = new ButtonType("Đóng");
        alert.getButtonTypes().setAll(buttonTypeAdd, buttonTypeClose);
        alert.showAndWait().ifPresent(btn -> {
            if (btn == buttonTypeAdd) clickBtnThemKH();
        });
    }

    private void showLoi() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText("Đã xảy ra lỗi");
        alert.setContentText("Vui lòng kiểm tra lại thông tin.");
        alert.showAndWait();
    }
}