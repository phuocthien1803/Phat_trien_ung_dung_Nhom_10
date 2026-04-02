package control;

import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.draw.LineSeparator;
import dao.KhuyenMai_DAO;
import dao.ThanhToan_1_DAO;
import entity.*;
import com.itextpdf.text.Image;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.awt.*;
import java.awt.Font;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
public class ThanhToan_1_Control {
    @FXML
    private Button btn_apdungkm;
    private ThanhToan_Control thanhToanControl;
    @FXML
    private Button btnHoanTac_tt;
    @FXML
    private Button btn_500k;
    @FXML
    private Button btn_100k;
    @FXML
    private Button btn_10k;
    @FXML
    private Button btn_equal;
    @FXML
    private Button btn_thanhtoan;
    @FXML
    private TableView<ChiTietHD_MonAn> table_chitiet;
    @FXML
    private TableColumn<ChiTietHD_MonAn, String> colTenMonAn;
    @FXML
    private TableColumn<ChiTietHD_MonAn, Double> colGia;
    @FXML
    private TableColumn<ChiTietHD_MonAn, Integer> colSL;
    @FXML
    private TableColumn<ChiTietHD_MonAn, Double> colVAT;
    @FXML
    private TableColumn<ChiTietHD_MonAn, Double> colThanhTien;
    @FXML
    private javafx.scene.control.TextField txt_khachtra;
    @FXML
    private javafx.scene.control.TextField txt_mahoadon;
    @FXML
    private javafx.scene.control.TextField txt_tongtien;
    @FXML
    private javafx.scene.control.TextField txt_tralai;
    @FXML
    private Label txt_giamgia;
    @FXML
    private Button btn_1;
    @FXML
    private Button btn_2;
    @FXML
    private Button btn_5;
    @FXML
    private Button btn_10;
    @FXML
    private Button btn_20;
    @FXML
    private Button btn_50;
    @FXML
    private Button btn_100;
    @FXML
    private Button btn_200;
    @FXML
    private Button btn_500;
    @FXML
    private Button btn_reset;
    @FXML
    private Button btn_Xuathd_tt;
    @FXML
    private Button btn_apdung;
    @FXML
    private javafx.scene.control.TextField txt_phaitra;
    @FXML
    private javafx.scene.control.TextField txt_tiencoc;
    @FXML
    private Label txt_diemtichluy;
    private String maHoaDon;
    private NhanVien nv;
    private DecimalFormat decimalFormat = new DecimalFormat("#,##0");
    //bang 0 la chua ap dung =1 la dang ap dung
    int checkgiamgiatichluy=0;
    public void setThanhToanControl(ThanhToan_Control thanhToanControl) {
        this.thanhToanControl = thanhToanControl;
    }
    //ham xoa dinh dang viet trong in hoa don xai cho tien
    private double parseDouble(String value) {
        return Double.parseDouble(value.replace(",", ""));
    }
    //hoa don truyen tu giao dien truoc qua
    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
        txt_mahoadon.setText(maHoaDon);
        txt_mahoadon.setEditable(false);
        loadData();
    }
    // dinh dang 1,000,000 VND
    private String formatNumber(double number) {
        return decimalFormat.format(number) + " VNĐ";
    }
    //main
    public void loadData() {
        txt_giamgia.setVisible(false);
        txt_khachtra.setEditable(false);
        ObservableList<KhachHang> khachHangInfo = ThanhToan_1_DAO.getKhachHangInfo(maHoaDon);
        KhachHang firstKhachHang = khachHangInfo.get(0);
        int diemTichLuy = firstKhachHang.getDiemTichLuy();
        txt_diemtichluy.setText(diemTichLuy+"/500");
        btn_thanhtoan.setOnMouseEntered(e -> btn_thanhtoan.setStyle("-fx-background-color: #A11212; -fx-text-fill: #F5F5F5; -fx-border-color: #D4AF37;"));
        btn_thanhtoan.setOnMouseExited(e -> btn_thanhtoan.setStyle("-fx-background-color: #8B0000; -fx-text-fill: #F5F5F5; -fx-border-color: #D4AF37;"));
        btnHoanTac_tt.setOnMouseEntered(e -> btnHoanTac_tt.setStyle("-fx-background-color: #5A0A0A; -fx-text-fill: #F5F5F5; -fx-border-color: #D4AF37;"));
        btnHoanTac_tt.setOnMouseExited(e -> btnHoanTac_tt.setStyle("-fx-background-color: #A11212; -fx-text-fill: #F5F5F5; -fx-border-color: #D4AF37;"));
        btn_Xuathd_tt.setOnMouseEntered(e -> btn_Xuathd_tt.setStyle("-fx-background-color: #D4AF37; -fx-text-fill: #121212; -fx-border-color: #FFD700;"));
        btn_Xuathd_tt.setOnMouseExited(e -> btn_Xuathd_tt.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #121212; -fx-border-color: #D4AF37;"));
        ObservableList<ChiTietHD_MonAn> hoaDonList1 = ThanhToan_1_DAO.getHoaDonList1(maHoaDon);
        TableColumn<ChiTietHD_MonAn, Integer> colStt = new TableColumn<>("STT");
        //load du lieu len chitiethd
        colStt.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ChiTietHD_MonAn, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ChiTietHD_MonAn, Integer> param) {
                // Tính toán STT dựa trên vị trí trong danh sách
                int index = hoaDonList1.indexOf(param.getValue());
                return new SimpleIntegerProperty(index + 1).asObject();
            }
        });
        colTenMonAn.setCellValueFactory(new PropertyValueFactory<>("tenMonAn"));
        colGia.setCellValueFactory(new PropertyValueFactory<>("gia"));
        colGia.setCellFactory(col -> new TableCell<ChiTietHD_MonAn, Double>() {
            @Override
            protected void updateItem(Double gia, boolean empty) {
                super.updateItem(gia, empty);
                if (empty || gia == null) {
                    setText(null);
                } else {
                    setText(decimalFormat.format(gia)); // Định dạng giá trị của cột Giá
                }
            }
        });
        colSL.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
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
        colThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        colThanhTien.setCellFactory(col -> new TableCell<ChiTietHD_MonAn, Double>() {
            @Override
            protected void updateItem(Double thanhTien, boolean empty) {
                super.updateItem(thanhTien, empty);
                if (empty || thanhTien == null) {
                    setText(null);
                } else {
                    setText(decimalFormat.format(thanhTien)); // Định dạng giá trị thành tiền
                }
            }
        });
        // Gắn dữ liệu vào TableView
        table_chitiet.setItems(hoaDonList1);
        table_chitiet.getColumns().add(0, colStt);
        //su kien an phim f1 2 3 4
        btn_Xuathd_tt.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                   if (event.getCode() == KeyCode.F1) {
                        handleThanhToan();
                    }
                    else if (event.getCode() == KeyCode.F3) {
                        handleHoanTac();
                    } else if (event.getCode() == KeyCode.F2) {
                        handleXuatHD();
                    }
                });
            }
        });
        table_chitiet.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F2) {
                event.consume();
                handleXuatHD();
            }
        });
         //su kien an ban phim (goi xuong may ham duoi xu li)
        btn_1.setOnAction(event -> handleAddValue(1000));
        btn_2.setOnAction(event -> handleAddValue(2000));
        btn_5.setOnAction(event -> handleAddValue(5000));
        btn_10.setOnAction(event -> handleAddValue(10000));
        btn_20.setOnAction(event -> handleAddValue(20000));
        btn_50.setOnAction(event -> handleAddValue(50000));
        btn_100.setOnAction(event -> handleAddValue(100000));
        btn_200.setOnAction(event -> handleAddValue(200000));
        btn_500.setOnAction(event -> handleAddValue(500000));
        btn_reset.setOnAction(event -> handleReset());
        //su kien an nut thanh toan
        btn_thanhtoan.setOnAction(event -> {
            handleThanhToan();
        });
        //su kien an nut xai diem tich luy
        btn_apdung.setOnAction(event -> {
            if (checkgiamgiatichluy == 0) {
                handleApplyButtonClick();
            } else if (checkgiamgiatichluy == 1) {
                handleDiscountCancellation();
            }
        });
        //su kien an nut goi y tian mat
        btn_equal.setOnAction(event -> {
            String tongTienText = txt_phaitra.getText();
            if (tongTienText != null && !tongTienText.isEmpty()) {
                String formattedTongTien = tongTienText.replace(" VNĐ", "").replace(",", "").trim();
                long tongTien = Long.parseLong(formattedTongTien);
                if (tongTien < 1000) {
                    tongTien = 1000;
                } else {
                    if (tongTien % 1000 != 0) {
                        tongTien = (long) (Math.ceil(tongTien / 1000.0) * 1000);
                    }
                }
                String formattedTongTienVND = decimalFormat.format(tongTien) + " VNĐ";
                txt_khachtra.setText(formattedTongTienVND);
                double currenttongTien = Double.parseDouble(formattedTongTien);
                double traLai = tongTien-currenttongTien;
                String currentTraLaiText = txt_tralai.getText().replace(" VNĐ", "").replace(",", "").trim();
                double currentTraLai = currentTraLaiText.isEmpty() ? 0 : Double.parseDouble(currentTraLaiText);
                double updatedTraLai = Math.max(0, currentTraLai + traLai);
                txt_tralai.setText(formatNumber(Math.max(0, updatedTraLai)));
                String formattedTongTienForBtn = decimalFormat.format(tongTien);
                btn_equal.setText(formattedTongTienForBtn);
            }
        });
        btn_10k.setOnAction(event -> {
            String tongTienText = txt_phaitra.getText();
            if (tongTienText != null && !tongTienText.isEmpty()) {
                String formattedTongTien = tongTienText.replace(" VNĐ", "").replace(",", "").trim();
                    long tongTien = Long.parseLong(formattedTongTien);
                    // Nếu giá trị nhỏ hơn 10,000, hiển thị 10,000
                    if (tongTien < 10000) {
                        tongTien = 10000;
                    } else {
                        // Nếu giá trị lớn hơn hoặc bằng 10,000, làm tròn lên bội số của 10,000
                        if (tongTien % 10000 != 0) {
                            tongTien = (long) (Math.ceil(tongTien / 10000.0) * 10000); // Làm tròn lên bội số của 10,000
                        } else {
                            tongTien += 10000; // Nếu đã chia hết cho 10,000, cộng thêm 10,000
                        }
                    }
                    String formattedTongTienVND = decimalFormat.format(tongTien) + " VNĐ";
                    txt_khachtra.setText(formattedTongTienVND);
                    // Tính tiền thối lại: txt_khachtra - txt_tongtien
                    double currenttongTien = Double.parseDouble(formattedTongTien);
                    double traLai = tongTien-currenttongTien;
                    String currentTraLaiText = txt_tralai.getText().replace(" VNĐ", "").replace(",", "").trim();
                    double currentTraLai = currentTraLaiText.isEmpty() ? 0 : Double.parseDouble(currentTraLaiText);
                    double updatedTraLai = Math.max(0, currentTraLai + traLai);
                    txt_tralai.setText(formatNumber(Math.max(0, updatedTraLai)));
                    // Cập nhật giá trị vào btn_10k
                    String formattedTongTienForBtn = decimalFormat.format(tongTien);
                    btn_10k.setText(formattedTongTienForBtn);

                }
        });
        btn_100k.setOnAction(event -> {
            String tongTienText = txt_phaitra.getText();
            if (tongTienText != null && !tongTienText.isEmpty()) {
                String formattedTongTien = tongTienText.replace(" VNĐ", "").replace(",", "").trim();
                    long tongTien = Long.parseLong(formattedTongTien);
                    if (tongTien < 100000) {
                        tongTien = 100000;
                    } else {
                        if (tongTien % 100000 != 0) {
                            tongTien = (long) (Math.ceil(tongTien / 100000.0) * 100000);
                        } else {
                            tongTien += 100000;
                        }
                    }
                    String formattedTongTienVND = decimalFormat.format(tongTien) + " VNĐ";
                    txt_khachtra.setText(formattedTongTienVND);
                    double currenttongTien = Double.parseDouble(formattedTongTien);
                    double traLai = tongTien-currenttongTien;  // Sửa phép tính ở đây (txt_khachtra - txt_tongtien)
                    String currentTraLaiText = txt_tralai.getText().replace(" VNĐ", "").replace(",", "").trim();
                    double currentTraLai = currentTraLaiText.isEmpty() ? 0 : Double.parseDouble(currentTraLaiText);
                    double updatedTraLai = Math.max(0, currentTraLai + traLai);
                    txt_tralai.setText(formatNumber(Math.max(0, updatedTraLai)));
                    String formattedTongTienForBtn = decimalFormat.format(tongTien);  // Định dạng cho btn_10k
                    btn_100k.setText(formattedTongTienForBtn);
                }
        });
        btn_500k.setOnAction(event -> {
            String tongTienText = txt_phaitra.getText();
            if (tongTienText != null && !tongTienText.isEmpty()) {
                String formattedTongTien = tongTienText.replace(" VNĐ", "").replace(",", "").trim();
                    long tongTien = Long.parseLong(formattedTongTien);
                    if (tongTien < 500000) {
                        tongTien = 500000;
                    } else {
                        if (tongTien % 500000 != 0) {
                            tongTien = (long) (Math.ceil(tongTien / 500000.0) * 500000);
                        } else {
                            tongTien += 500000;
                        }
                    }
                    String formattedTongTienVND = decimalFormat.format(tongTien) + " VNĐ";
                    txt_khachtra.setText(formattedTongTienVND);
                    double currenttongTien = Double.parseDouble(formattedTongTien);
                    double traLai = tongTien-currenttongTien;  // Sửa phép tính ở đây (txt_khachtra - txt_tongtien)
                    String currentTraLaiText = txt_tralai.getText().replace(" VNĐ", "").replace(",", "").trim();
                    double currentTraLai = currentTraLaiText.isEmpty() ? 0 : Double.parseDouble(currentTraLaiText);
                    double updatedTraLai = Math.max(0, currentTraLai + traLai);
                    txt_tralai.setText(formatNumber(Math.max(0, updatedTraLai)));
                    String formattedTongTienForBtn = decimalFormat.format(tongTien);  // Định dạng cho btn_10k
                    btn_500k.setText(formattedTongTienForBtn);
                }
        });
        //an nut xem tt khuyen mai
        btn_apdungkm.setOnAction(event -> {
            ObservableList<KhuyenMai> khuyenMaiListBest = KhuyenMai_DAO.getKhuyenMaiListBest();
            if (khuyenMaiListBest != null && !khuyenMaiListBest.isEmpty()) {
                // Nếu có khuyến mãi, lấy thông tin khuyến mãi tốt nhất
                KhuyenMai khuyenMaiBest = khuyenMaiListBest.get(0);
                String maKM = khuyenMaiBest.getMaKM();
                String tenKM = khuyenMaiBest.getTenKM();
                double phanTramGiam = khuyenMaiBest.getPhanTramGiam();
                // Hiển thị thông báo thông tin khuyến mãi
                Alert confirmDialog = new Alert(Alert.AlertType.INFORMATION);
                confirmDialog.setTitle("Thông tin Khuyến Mãi");
                confirmDialog.setHeaderText("Chương trình khuyến mãi tốt nhất đang áp dụng:");
                confirmDialog.setContentText(
                        "Mã khuyến mãi: " + maKM + "\n" +
                                "Tên khuyến mãi: " + tenKM + "\n" +
                                "Phần trăm giảm: " + (phanTramGiam * 100) + "%");
                ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                confirmDialog.getButtonTypes().setAll(okButton);
                confirmDialog.showAndWait();
            } else {
                // Nếu không có khuyến mãi, hiển thị thông báo khác
                Alert noPromotionDialog = new Alert(Alert.AlertType.WARNING);
                noPromotionDialog.setTitle("Thông báo");
                noPromotionDialog.setHeaderText("Không có chương trình khuyến mãi");
                noPromotionDialog.setContentText("Hiện không có chương trình khuyến mãi nào được áp dụng.");
                ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                noPromotionDialog.getButtonTypes().setAll(okButton);
                noPromotionDialog.showAndWait();
            }
        });

        tinhTongTien();
        tinhCoc();
        tinhTienPhaiTra();
        //ap dung che do khuyen mai cao nhat
        ApplyKhuyenMai();
        //hien thi so tien goi y
        setupSuggestButtons();
    }
    //ham cap nhat cac nut goi y tien mat
    private void updateSuggestMoney(String tongTienText, long stepValue, Button button) {
        if (tongTienText != null && !tongTienText.isEmpty()) {
            String formattedTongTien = tongTienText.replace(" VNĐ", "").replace(",", "").trim();
                long tongTien = Long.parseLong(formattedTongTien);
                if (tongTien < stepValue) {
                    tongTien = stepValue;
                } else {
                    // Nếu giá trị lớn hơn hoặc bằng stepValue, làm tròn lên bội số của stepValue
                    if (tongTien % stepValue != 0) {
                        tongTien = (long) (Math.ceil(tongTien / (double) stepValue) * stepValue);
                    } else {
                        tongTien += stepValue; // euqal ko co
                    }
                }
                String formattedTongTienVND = decimalFormat.format(tongTien);
                button.setText(formattedTongTienVND);
            }
    }
    private void updateSuggestMoneyEqual(String tongTienText, long stepValue, Button button) {
        if (tongTienText != null && !tongTienText.isEmpty()) {
            String formattedTongTien = tongTienText.replace(" VNĐ", "").replace(",", "").trim();
            long tongTien = Long.parseLong(formattedTongTien);
            if (tongTien < stepValue) {
                tongTien = stepValue;
            } else if (tongTien % stepValue != 0) {
                tongTien = (long) (Math.ceil(tongTien / (double) stepValue) * stepValue);
            }
            String formattedTongTienForBtn = decimalFormat.format(tongTien);
            button.setText(formattedTongTienForBtn);
        }
    }
    private void setupSuggestButtons() {
        updateSuggestMoneyEqual(txt_phaitra.getText(), 1000, btn_equal);
        updateSuggestMoney(txt_phaitra.getText(), 10000, btn_10k);
        updateSuggestMoney(txt_phaitra.getText(), 100000, btn_100k);
        updateSuggestMoney(txt_phaitra.getText(), 500000, btn_500k);
    }

    private void handleThanhToan() {
        if (!checkThanhToan()) {
            return; // Ngừng thực hiện nếu kiểm tra thất bại
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thanh toán");
        alert.setHeaderText("Bạn có chắc chắn muốn thanh toán không?");
        alert.setContentText("Hãy chọn Có hoặc Không.");
        // Hiển thị hộp thoại và chờ người dùng chọn
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String maKhachHang = null;
                String maBan=null;
                int diemTichLuy = 0;
                //lay tong ten
                String totalText = txt_tongtien.getText().replace(" VNĐ", "").replace(",", "").trim();
                double currentTotal = Double.parseDouble(totalText);
                //lay ma ban
                HoaDon hoaDonDetails = ThanhToan_1_DAO.getHoaDonDetailsByMa(maHoaDon);
                if (hoaDonDetails != null) {
                    maBan = hoaDonDetails.getMaBan();
                }
                //lay diem tich luy va ma kh
                ObservableList<KhachHang> khachHangInfo = ThanhToan_1_DAO.getKhachHangInfo(maHoaDon);
                //lay thong tin tu ben khach hang
                if (!khachHangInfo.isEmpty()) {
                    KhachHang firstKhachHang = khachHangInfo.get(0);
                    maKhachHang = firstKhachHang.getMaKH();
                    double currentDiem=firstKhachHang.getDiemTichLuy();
                    diemTichLuy = calculateDiemTichLuy(currentTotal, currentDiem);
                }
                String maKM=null;
                ObservableList<KhuyenMai> khuyenMaiListBest = KhuyenMai_DAO.getKhuyenMaiListBest();
                if (khuyenMaiListBest != null && !khuyenMaiListBest.isEmpty()) {
                    KhuyenMai khuyenMaiBest = khuyenMaiListBest.get(0);
                    maKM = khuyenMaiBest.getMaKM();
                }
                System.out.println("Diem tich luy cong them:"+diemTichLuy);
                System.out.println("Ma kh can cong diem tich luy:"+maKhachHang);
                System.out.println("Tong tien them vao hoa don:"+currentTotal);
                System.out.println("Cap nhat ma ban:"+maBan);
                ThanhToan_1_DAO.updateHoaDon(maHoaDon, currentTotal, diemTichLuy, maKhachHang, maBan, maKM);
                Alert completionAlert = new Alert(Alert.AlertType.INFORMATION);
                completionAlert.setTitle("Thông báo");
                completionAlert.setHeaderText(null);
                completionAlert.setContentText("Giao dịch thành công!");
                completionAlert.showAndWait();
                //lua chon hien thi hoa don
                Alert printAlert = new Alert(Alert.AlertType.CONFIRMATION);
                printAlert.setTitle("In hóa đơn");
                printAlert.setHeaderText(null);
                printAlert.setContentText("Bạn có muốn hiển thị hóa đơn không?");
                printAlert.showAndWait().ifPresent(printResponse -> {
                    if (printResponse == ButtonType.OK) {
                        printInvoice(maHoaDon, 0, 1);
                    } else {
                        printInvoice(maHoaDon, 0, 0);
                        }
                    });
                //reload lai table ben ds hoa don
                thanhToanControl.loadHoaDonData();
                // Đóng giao diện hiện tại sau khi xác nhận hoàn tất
                Stage currentStage = (Stage) txt_khachtra.getScene().getWindow();
                currentStage.close();
            } else {
                alert.close();
            }
        });
    }
    private void handleDiscountCancellation() {
        // Show a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Discount");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có muốn hủy chế độ giảm giá này không?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    ObservableList<KhuyenMai> khuyenMaiListBest = KhuyenMai_DAO.getKhuyenMaiListBest();
                    double phanTramGiam=0.0;
                    if (khuyenMaiListBest != null && !khuyenMaiListBest.isEmpty()) {
                        KhuyenMai khuyenMaiBest = khuyenMaiListBest.get(0);
                        phanTramGiam = khuyenMaiBest.getPhanTramGiam();
                    }
                    phanTramGiam*=100;
                    String giamGiaText = txt_giamgia.getText();
                    String giamGiaValue = giamGiaText.replace("%", "");
                    int giamGia = Integer.parseInt(giamGiaValue);
                    // Lấy giá trị tổng tiền và loại bỏ " VNĐ"
                    String totalText = txt_phaitra.getText().replace(" VNĐ", "").replace(",", "").trim();
                    double currentTotal = Double.parseDouble(totalText);
                    double discount = giamGia-phanTramGiam;
                    String tongCongText = txt_tongtien.getText().replaceAll("[^\\d]", ""); //
                    double tongCong = Double.parseDouble(tongCongText);
                    double updatedNum = tongCong/100*discount;
                    System.out.println("Day la so can cong"+ updatedNum);
                    double updatedTotal = currentTotal+updatedNum;
                    txt_phaitra.setText(formatNumber(updatedTotal));
                    if (discount == giamGia) {
                        txt_giamgia.setText("");
                        txt_giamgia.setVisible(false);
                    } else {
                        int discountRounded = (int) Math.round(giamGia - discount);
                        txt_giamgia.setText(discountRounded + "%");
                        txt_giamgia.setVisible(true);
                    }
                    ObservableList<KhachHang> khachHangInfo = ThanhToan_1_DAO.getKhachHangInfo(maHoaDon);
                    KhachHang firstKhachHang = khachHangInfo.get(0);
                    int diemTichLuy = firstKhachHang.getDiemTichLuy();
                    txt_diemtichluy.setText(diemTichLuy + "/500");
                    checkgiamgiatichluy=0;
                    txt_tralai.setText("");
                    txt_khachtra.setText("");
                    setupSuggestButtons();

                } catch (NumberFormatException e) {
                    showAlert("Thông báo", "Lỗi định dạng khi cập nhật tổng tiền!", Alert.AlertType.ERROR);
                }
            }
        });
    }
    //cap nhat tong tien sau khi chon diem tich luy
    private void handleApplyButtonClick() {
        if (checkgiamgiatichluy==1) {
            showAlert("Thông báo", "Đã áp dụng điểm tích luỹ!", Alert.AlertType.INFORMATION);
            return ;
        }
        try {
            // Tạo hộp thoại nhập điểm
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nhập điểm tích lũy");
            dialog.setHeaderText("Vui lòng nhập điểm tích lũy (50 - 500, bội số của 50):");
            dialog.setContentText("Điểm tích lũy:");

            // Hiển thị hộp thoại và chờ người dùng nhập
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String diemInput = result.get();
                if (diemInput.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Điểm tích lũy không được để trống! Vui lòng nhập giá trị hợp lệ.");
                    alert.showAndWait();
                    return;
                }
                // Kiểm tra tính hợp lệ của điểm tích lũy
                int diemNhap = Integer.parseInt(diemInput.trim());
                String diemText = txt_diemtichluy.getText().split("/")[0]; // Lấy số điểm hiện tại trước dấu "/"
                int diemHienTai = Integer.parseInt(diemText.trim());
                if (diemNhap < 50 || diemNhap > 500 || diemNhap % 50 != 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Điểm tích lũy không hợp lệ! Vui lòng nhập giá trị từ 50 đến 500 và là bội số của 50.");
                    alert.showAndWait();
                } else if (diemNhap > diemHienTai) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Bạn không đủ điểm tích lũy để sử dụng khuyến mãi.");
                    alert.showAndWait();
                } else {
                    // Áp dụng khuyến mãi theo mức điểm
                    double giamGia = 0.0;
                    if (diemNhap == 500) {
                        giamGia = 0.30;
                    } else if (diemNhap >= 450) {
                        giamGia = 0.27;
                    } else if (diemNhap >= 400) {
                        giamGia = 0.24;
                    } else if (diemNhap >= 350) {
                        giamGia = 0.21;
                    } else if (diemNhap >= 300) {
                        giamGia = 0.18;
                    } else if (diemNhap >= 250) {
                        giamGia = 0.15;
                    } else if (diemNhap >= 200) {
                        giamGia = 0.12;
                    } else if (diemNhap >= 150) {
                        giamGia = 0.09;
                    } else if (diemNhap >= 100) {
                        giamGia = 0.06;
                    } else if (diemNhap >= 50) {
                        giamGia = 0.03;
                    }
                    // Tính toán giá trị giảm giá
                    double tongTienGoc = Double.parseDouble(txt_tongtien.getText().replace(",", "").replace(" VNĐ", ""));
                    double tienGiam = tongTienGoc * giamGia;
                    // Cập nhật điểm tích lũy mới
                    int diemMoi = diemHienTai - diemNhap;
                    txt_diemtichluy.setText(diemMoi + "/500");
                    checkgiamgiatichluy=1;
                    // Hiển thị thông báo thành công
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Áp dụng khuyến mãi thành công! Bạn được giảm " + (int) (giamGia * 100) + "%.\n" +
                            "Số tiền được giảm: " + formatNumber(tienGiam) + "\n" +
                            "Điểm tích lũy còn lại: " + diemMoi);
                    alert.showAndWait();
                    // Cập nhật giao diện
                    txt_giamgia.setVisible(true);
                    String currentText = txt_giamgia.getText().replace("%", "").trim();
                    int currentDiscount = (currentText.equals("Label") || currentText.isEmpty()) ? 0 : Integer.parseInt(currentText);
                    int newDiscount = currentDiscount + (int) (giamGia * 100);
                    txt_giamgia.setText(newDiscount + "%");
                    double currentPhaiTra = Double.parseDouble(txt_phaitra.getText().replace(",", "").replace(" VNĐ", ""));
                    double newPhaiTra = currentPhaiTra - tienGiam;
                    txt_phaitra.setText(formatNumber(newPhaiTra)); // Cập nhật tổng tiền sau giảm
                    txt_khachtra.setText("");
                    txt_tralai.setText("");
                    setupSuggestButtons();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void handleReset() {
        txt_khachtra.setText("");
        txt_tralai.setText("");
    }
    private void handleAddValue(int valueToAdd) {
        // Lấy giá trị hiện tại của txt_khachtra và chuyển đổi sang double
        String currentText = txt_khachtra.getText().replace(",", "").replace("VNĐ", "").trim();
        double currentValue = 0.0;
        if (!currentText.isEmpty()) {
            currentValue = parseDouble(currentText);
        }
        // Cập nhật giá trị mới
        double newValue = currentValue + valueToAdd;
        txt_khachtra.setText(decimalFormat.format(newValue) + " VNĐ");
        // Lấy giá trị của txt_phaitra và chuyển đổi sang double
        String tongTienText = txt_phaitra.getText().replace(",", "").replace("VNĐ", "").trim();
        double tongTien = 0.0;
        if (!tongTienText.isEmpty()) {
            tongTien = parseDouble(tongTienText);
        }

        // Kiểm tra nếu newValue (khách trả) >= tongTien (phải trả)
        if (newValue >= tongTien) {
            // Tính giá trị trả lại
            double traLai = newValue - tongTien;

            // Cập nhật txt_tralai
            txt_tralai.setText(decimalFormat.format(traLai) + " VNĐ");
        }
    }

    private void tinhTienPhaiTra() {
        try {
            String tongTienStr = txt_tongtien.getText().replaceAll("[^\\d.]", "");
            double tongTien = Double.parseDouble(tongTienStr);
            String tienCocStr = txt_tiencoc.getText().replaceAll("[^\\d.]", "");
            double tienCoc = Double.parseDouble(tienCocStr);
            // Tính tiền phải trả
            double tienPhaiTra = tongTien - tienCoc;
            // Định dạng và gán kết quả vào txt_phaitra
            txt_phaitra.setText(formatNumber(tienPhaiTra));
        } catch (NumberFormatException e) {
            System.out.println("Lỗi trong tính tiền phải trả");
        }
    }
    public double tinhTongGiaTri() {
        double tongGiaTri = 0.0;
        ObservableList<ChiTietHD_MonAn> items = table_chitiet.getItems();
        for (ChiTietHD_MonAn chiTiet : items) {
            tongGiaTri += chiTiet.getThanhTien();
        }
        System.out.println(tongGiaTri+"day la tong gia tri");
        return tongGiaTri;
    }
    public void tinhCoc() {
        HoaDon hoaDon = ThanhToan_1_DAO.getHoaDonDetailsByMa(maHoaDon);
        if (hoaDon != null) {
            double tienCoc = hoaDon.getTienCoc();
            txt_tiencoc.setText(decimalFormat.format(tienCoc) + " VNĐ");
        }else {
            txt_tiencoc.setText("0 VNĐ");
        }
    }
    public double tinhThue() {
        double thue = 0.0;
        // Lấy danh sách các chi tiết hóa đơn từ TableView
        ObservableList<ChiTietHD_MonAn> items = table_chitiet.getItems();
        // Duyệt qua từng dòng và tính tổng của cột 'ThanhTien'
        for (ChiTietHD_MonAn chiTiet : items) {
            thue += chiTiet.getThanhTien()*chiTiet.getVAT()/100;
        }
        System.out.println(thue+"day la thue");
        return thue;
    }
    public void tinhTongTien() {
        double tongGiaTri = tinhTongGiaTri(); // Gọi hàm tính tổng giá trị
        double thue = tinhThue(); // Gọi hàm tính thuế
        double tongTien = tongGiaTri + thue;
        txt_tongtien.setText(formatNumber(tongTien));
    }
    private boolean checkThanhToan() {
        String currentText = txt_khachtra.getText().replace(",", "").replace(" VNĐ", "").trim();
        if (currentText.isEmpty()) {
            showAlert("Thông báo", "Số tiền không đủ!", Alert.AlertType.WARNING);
            return false;
        }
        double tienKhachTra = Double.parseDouble(currentText);
        double tongTien = parseDouble(txt_phaitra.getText().replace(" VNĐ", "").replace(",", "").trim());
        if (tienKhachTra < tongTien) {
            showAlert("Thông báo", "Số tiền không đủ!", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    //ham tao dialog cho cai ham o tren
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        txt_khachtra.requestFocus();
    }
    private void ApplyKhuyenMai(){
        String tongTienStr = txt_tongtien.getText().replaceAll("[^\\d.]", "");
        double tongTien = Double.parseDouble(tongTienStr);
        ObservableList<KhuyenMai> khuyenMaiListBest = KhuyenMai_DAO.getKhuyenMaiListBest();
        if (khuyenMaiListBest.isEmpty()) {
            return;
        }
        KhuyenMai khuyenMaiBest = khuyenMaiListBest.get(0);
        double phanTramGiam = khuyenMaiBest.getPhanTramGiam();
        System.out.println(phanTramGiam+"phan tram giam la");
        double tongTienSauGiam = tongTien * phanTramGiam;
        System.out.println(tongTienSauGiam+"tong tien sau giam la");
        String tienCocStr = txt_tiencoc.getText().replaceAll("[^\\d.]", "");
        double tienCoc = Double.parseDouble(tienCocStr);
        double tienPhaiTra = tongTien - tienCoc - tongTienSauGiam;
        System.out.println(tienPhaiTra + " tien phai tra la");
        txt_phaitra.setText(formatNumber(tienPhaiTra));
        int giamGiaInt = (int) (phanTramGiam * 100);
        String giamGiaStr = giamGiaInt + "%";
        txt_giamgia.setText(giamGiaStr);
        txt_giamgia.setVisible(true);
        setupSuggestButtons();
    }
//    private void setTxtTraLai() {
//        HoaDon hoaDon = ThanhToan_1_DAO.getHoaDonDetailsByMa(maHoaDon);
//        if (hoaDon != null) {
//            double tienCoc = hoaDon.getTienCoc();
//            txt_tralai.setText(decimalFormat.format(tienCoc) + " VNĐ");
//        }else {
//            txt_tralai.setText("0 VNĐ");
//        }
//    }
    private void printInvoice(String maHoaDon,int kiemtra,int show) {
        //kiemtra =1 la phieu tam tinh, 0 la hoa don
        // show =1 show len man show=0 ko show
        DecimalFormat noDecimalFormat = new DecimalFormat("#,###");
        nv = SessionManager.getInstance().getCurrentNhanVien();
        Document document = new Document();
        try {
            PdfWriter writer;
            String dest;
            Path destPath;
            if (kiemtra == 0) {
                // Hoa don file path
                destPath = Paths.get("hoadon/hoadon_" + maHoaDon + ".pdf");
            } else {
                // Temporary invoice path for 'phieu tam tinh'
                destPath = Paths.get("hoadon/temp_hoadon_" + UUID.randomUUID().toString() + ".pdf");
            }
            if (!Files.exists(destPath.getParent())) {
                Files.createDirectories(destPath.getParent());
            }
            writer = PdfWriter.getInstance(document, new FileOutputStream(destPath.toFile()));
            document.open();
            //them background
//            try {
//                String imagePath = "src/main/resources/images/dinner.png";
//                Image background = Image.getInstance(imagePath);
//                background.setAbsolutePosition(0, 0); // Bottom-left corner of the page
//                background.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
//                PdfContentByte canvas = writer.getDirectContentUnder(); // Get the content layer beneath text
//                PdfGState gState = new PdfGState();
//                gState.setFillOpacity(0.3f);
//                canvas.setGState(gState);
//                canvas.addImage(background); // Add the image with transparency
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            // Edit nội dung hóa đơn
            Paragraph restaurantName = new Paragraph("Nha Hang Tan Hai Van", FontFactory.getFont("Times New Roman", 16, Font.BOLD));
            restaurantName.setAlignment(Element.ALIGN_CENTER);
            document.add(restaurantName);
            if (kiemtra==1){
                Paragraph tenPhieu = new Paragraph("PHIEU TAM TINH", FontFactory.getFont("Times New Roman", 12, Font.BOLD));
                tenPhieu.setAlignment(Element.ALIGN_CENTER);
                document.add(tenPhieu);
            }else{
                Paragraph tenPhieu = new Paragraph("HOA DON THANH TOAN", FontFactory.getFont("Times New Roman", 12, Font.BOLD));
                tenPhieu.setAlignment(Element.ALIGN_CENTER);
                document.add(tenPhieu);
            }

            Paragraph soHD = new Paragraph("Ma: " + maHoaDon, FontFactory.getFont("Times New Roman", 12, Font.BOLD));
            soHD.setAlignment(Element.ALIGN_CENTER);
            document.add(soHD);

            // Liên kết với DAO để lấy dữ liệu hóa đơn
            ThanhToan_1_DAO hoaDonDAO = new ThanhToan_1_DAO();
            HoaDon hoaDon = hoaDonDAO.getHoaDonDetailsByMa(maHoaDon);
            // lay gio hien tai
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String currentTime = now.format(formatter);

            Paragraph ngay = new Paragraph(
                    "Ngay: " + hoaDon.getNgayTaoHD() + " " + hoaDon.getGioDatBan() + " - " + currentTime,
                    FontFactory.getFont("Times New Roman", 12, Font.BOLD)
            );
            document.add(ngay);

            Paragraph ban = new Paragraph("Ban: " + hoaDon.getMaBan(), FontFactory.getFont("Times New Roman", 12, Font.BOLD));
            document.add(ban);

            Paragraph nhanvien = new Paragraph("Nhan vien: " + removeDiacritics(nv.getTenNV()),
                    FontFactory.getFont("Times New Roman", 12, Font.BOLD));
            document.add(nhanvien);

            document.add(new Paragraph(" ")); // blank line for spacing

            // Tạo bảng chi tiết món ăn
            PdfPTable table;
            String[] headers;
                table = new PdfPTable(6);
                table.setWidths(new int[]{1, 3, 2, 1, 1, 2});
                table.setWidthPercentage(100);
                headers = new String[]{"STT", "Ten mon an", "Gia", "So luong", "VAT", "Thanh tien"};
            // Add headers with no vertical borders
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont("Times New Roman", 12, Font.BOLD)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                cell.setBorderWidthLeft(0);  // Remove left border
                cell.setBorderWidthRight(0); // Remove right border
                table.addCell(cell);
            }

            // Thêm dữ liệu món ăn từ TableView với no vertical borders
            ObservableList<ChiTietHD_MonAn> items = table_chitiet.getItems();
            int index = 1;
            double tongTienThue=0;
            for (ChiTietHD_MonAn item : items) {
                // STT cell
                PdfPCell sttCell = new PdfPCell(new Phrase(String.valueOf(index++)));
                sttCell.setBorderWidthLeft(0);
                sttCell.setBorderWidthRight(0);
                sttCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(sttCell);

                // Ten mon an cell
                PdfPCell tenMonAnCell = new PdfPCell(new Phrase(removeDiacritics(item.getMonAn().getTenMonAn())));
                tenMonAnCell.setBorderWidthLeft(0);
                tenMonAnCell.setBorderWidthRight(0);
                tenMonAnCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(tenMonAnCell);

                // Gia cell
                PdfPCell giaCell = new PdfPCell(new Phrase(noDecimalFormat.format(item.getGia())));
                giaCell.setBorderWidthLeft(0);
                giaCell.setBorderWidthRight(0);
                giaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(giaCell);

                // So luong cell
                PdfPCell soLuongCell = new PdfPCell(new Phrase(String.valueOf(item.getSoLuong())));
                soLuongCell.setBorderWidthLeft(0);
                soLuongCell.setBorderWidthRight(0);
                soLuongCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(soLuongCell);
                // tinh tong thue
                double vatPercent = item.getMonAn().getVAT();
                double tienThue = item.getGia() * item.getSoLuong() * (vatPercent / 100);
                tongTienThue += tienThue;

                // VAT cell
                PdfPCell vatCell = new PdfPCell(new Phrase(item.getMonAn().getVAT() + "%"));
                vatCell.setBorderWidthLeft(0);
                vatCell.setBorderWidthRight(0);
                vatCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(vatCell);
                // Thanh tien cell
                PdfPCell thanhTienCell = new PdfPCell(new Phrase(noDecimalFormat.format(item.getThanhTien())));
                thanhTienCell.setBorderWidthLeft(0);
                thanhTienCell.setBorderWidthRight(0);
                thanhTienCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(thanhTienCell);
            }
            document.add(table);
            //tong ket
            document.add(new Paragraph(" "));
            double tienCoc= parseDouble(txt_tiencoc.getText().replace(",", "").replace(" VNĐ", ""));
            double tongGiaTri = parseDouble(txt_tongtien.getText().replace(",", "").replace(" VNĐ", ""));
            double khachtra;
            if (txt_khachtra.getText().isEmpty()) khachtra=0;
            else{
                khachtra = parseDouble(txt_khachtra.getText().replace(",", "").replace(" VNĐ", ""));
            }
            double thoilai;
            if (txt_tralai.getText().isEmpty()) thoilai=0;
            else{
                thoilai = parseDouble(txt_tralai.getText().replace(",", "").replace(" VNĐ", ""));
            }
           String khuyenMai;
            if (txt_giamgia.getText().equals("Label")||txt_giamgia.getText().isEmpty()) khuyenMai="Khong ap dung";
            else {
                String giamGiaText = txt_giamgia.getText().replace(" VNĐ", "").replace("%", "");
                double giamGia = Double.parseDouble(giamGiaText) / 100;
                double ketQuaGiamGia = giamGia * tongGiaTri;
                khuyenMai=decimalFormat.format(ketQuaGiamGia) + " VND";
            }

            double tongThanhtoan = parseDouble(txt_phaitra.getText().replace(",", "").replace(" VNĐ", ""));
            PdfPTable totalAmountTable = new PdfPTable(2);
            totalAmountTable.setWidthPercentage(100); // Chiếm toàn bộ chiều rộng

            PdfPCell leftCell = new PdfPCell(new Phrase("Tong tien:", FontFactory.getFont("Times New Roman", 12, Font.BOLD | Font.ITALIC)));
            leftCell.setBorder(Rectangle.NO_BORDER); // Bỏ border của ô
            leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalAmountTable.addCell(leftCell);

            PdfPCell rightCell = new PdfPCell(new Phrase(decimalFormat.format(tongGiaTri) + " VND", FontFactory.getFont("Times New Roman", 12, Font.BOLD | Font.ITALIC)));
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalAmountTable.addCell(rightCell);
            // tien coc
            PdfPTable totalAmountTableCoc = new PdfPTable(2);
            totalAmountTableCoc.setWidthPercentage(100);

            PdfPCell leftCellCoc = new PdfPCell(new Phrase("Tien coc:", FontFactory.getFont("Times New Roman", 12, Font.ITALIC)));
            leftCellCoc.setBorder(Rectangle.NO_BORDER);
            leftCellCoc.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalAmountTableCoc.addCell(leftCellCoc);

            PdfPCell rightCellCoc = new PdfPCell(new Phrase("- "+ decimalFormat.format(tienCoc) + " VND", FontFactory.getFont("Times New Roman", 12, Font.ITALIC)));
            rightCellCoc.setBorder(Rectangle.NO_BORDER);
            rightCellCoc.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalAmountTableCoc.addCell(rightCellCoc);
            // khuyen mai
            PdfPTable totalAmountTable1 = new PdfPTable(2);
            totalAmountTable1.setWidthPercentage(100);

            PdfPCell leftCell1 = new PdfPCell(new Phrase("Khuyen mai:", FontFactory.getFont("Times New Roman", 12, Font.ITALIC)));
            leftCell1.setBorder(Rectangle.NO_BORDER);
            leftCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalAmountTable1.addCell(leftCell1);

            PdfPCell rightCell1 = new PdfPCell(new Phrase("- " + khuyenMai, FontFactory.getFont("Times New Roman", 12, Font.ITALIC)));
            rightCell1.setBorder(Rectangle.NO_BORDER);
            rightCell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalAmountTable1.addCell(rightCell1);
            //tong thanh toan
            PdfPTable totalAmountTable2 = new PdfPTable(2);
            totalAmountTable2.setWidthPercentage(100);

            PdfPCell leftCell2 = new PdfPCell(new Phrase("Tong thanh toan:", FontFactory.getFont("Times New Roman", 16, Font.BOLD)));
            leftCell2.setBorder(Rectangle.NO_BORDER);
            leftCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalAmountTable2.addCell(leftCell2);

            PdfPCell rightCell2 = new PdfPCell(new Phrase(decimalFormat.format(tongThanhtoan) + " VND", FontFactory.getFont("Times New Roman", 16, Font.BOLD)));
            rightCell2.setBorder(Rectangle.NO_BORDER);
            rightCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalAmountTable2.addCell(rightCell2);

// Thêm bảng vào tài liệu
            document.add(totalAmountTable);
            document.add(totalAmountTableCoc);
            document.add(totalAmountTable1);
            document.add(totalAmountTable2);
            LineSeparator line = new LineSeparator(); // duong thang
            line.setLineWidth(1); // Độ dày của đường thẳng
            document.add(new Chunk(line));

            PdfPTable totalAmountTablekhach = new PdfPTable(2);
            totalAmountTablekhach.setWidthPercentage(100);

            PdfPCell leftCellkhach = new PdfPCell(new Phrase("Tien khach tra:", FontFactory.getFont("Times New Roman", 12, Font.BOLD)));
            leftCellkhach.setBorder(Rectangle.NO_BORDER);
            leftCellkhach.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalAmountTablekhach.addCell(leftCellkhach);

            PdfPCell rightCellkhach = new PdfPCell(new Phrase(decimalFormat.format(khachtra) + " VND", FontFactory.getFont("Times New Roman", 12, Font.BOLD)));
            rightCellkhach.setBorder(Rectangle.NO_BORDER);
            rightCellkhach.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalAmountTablekhach.addCell(rightCellkhach);

            PdfPTable totalAmountTabletra = new PdfPTable(2);
            totalAmountTabletra.setWidthPercentage(100);

            PdfPCell leftCelltra = new PdfPCell(new Phrase("Tien thoi lai:", FontFactory.getFont("Times New Roman", 12, Font.ITALIC)));
            leftCelltra.setBorder(Rectangle.NO_BORDER);
            leftCelltra.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalAmountTabletra.addCell(leftCelltra);

            PdfPCell rightCelltra = new PdfPCell(new Phrase(decimalFormat.format(thoilai) + " VND", FontFactory.getFont("Times New Roman", 12, Font.ITALIC)));
            rightCelltra.setBorder(Rectangle.NO_BORDER);
            rightCelltra.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalAmountTabletra.addCell(rightCelltra);
            if (kiemtra==0){
                document.add(totalAmountTablekhach);
                document.add(totalAmountTabletra);
                document.add(new Chunk(line));
            }
            if (kiemtra==1){
                Paragraph note1 = new Paragraph("Vui long kiem tra ky noi dung truoc khi thanh toan!", FontFactory.getFont("Times New Roman", 12, Font.ITALIC));
                note1.setAlignment(Element.ALIGN_CENTER);
                document.add(note1);
            }
            if (kiemtra==0){
                Paragraph note = new Paragraph("Cam on va hen gap lai lan sau!", FontFactory.getFont("Times New Roman", 12, Font.ITALIC));
                note.setAlignment(Element.ALIGN_CENTER);
                document.add(note);
            }
            System.out.println("Đã in hóa đơn thành công!");
            // Hiển thị hóa đơn ngay sau khi tạo
            if (show == 1) {
                // Create the File object from the relative path
                File pdfFile = destPath.toFile();

                // Check if the file exists
                if (pdfFile.exists()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.err.println("PDF file does not exist: " + pdfFile.getAbsolutePath());
                }
                // Delete temporary file on exit if it's a temporary invoice (phieu tam tinh)
                if (kiemtra == 1) {
                    pdfFile.deleteOnExit(); // The file will be deleted when the JVM exits
                }
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @FXML
    private void handleHoanTac() {
        Stage currentStage = (Stage) btnHoanTac_tt.getScene().getWindow();
        currentStage.close(); // Đóng cửa sổ hiện tại
    }
    @FXML
    private void handleXuatHD() {
        printInvoice(maHoaDon,1,1);
    }
    //ham xoa dau tieng viet khi in hoa don
    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(normalized).replaceAll("");
    }
    private int calculateDiemTichLuy(double currentTotal, double currentDiem) {
        int diemTichLuy = 0;

        while (currentTotal > 0) {
            if (currentDiem < 100) {
                // Tính điểm trong khoảng 1-100
                double neededForNextPoint = 100000;
                double maxPointsInRange = Math.min((100 - currentDiem), currentTotal / neededForNextPoint);
                diemTichLuy += maxPointsInRange;
                currentTotal -= maxPointsInRange * neededForNextPoint;
                currentDiem += maxPointsInRange;
            } else if (currentDiem < 300) {
                // Tính điểm trong khoảng 101-300
                double neededForNextPoint = 300000;
                double maxPointsInRange = Math.min((300 - currentDiem), currentTotal / neededForNextPoint);
                diemTichLuy += maxPointsInRange;
                currentTotal -= maxPointsInRange * neededForNextPoint;
                currentDiem += maxPointsInRange;
            } else if (currentDiem < 500) {
                // Tính điểm trong khoảng 301-500
                double neededForNextPoint = 500000;
                double maxPointsInRange = Math.min((500 - currentDiem), currentTotal / neededForNextPoint);
                diemTichLuy += maxPointsInRange;
                currentTotal -= maxPointsInRange * neededForNextPoint;
                currentDiem += maxPointsInRange;
            } else {
                // Nếu đã đạt hoặc vượt 500 điểm, không cộng thêm
                break;
            }
        }
        return diemTichLuy;
    }

}
