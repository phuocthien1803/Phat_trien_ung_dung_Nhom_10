package control;

import dao.KhachHang_DAO;
import dao.QuanLyHoaDon_DAO;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DatBan_Control implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private Button khua;
    @FXML
    private Button khub;
    @FXML
    private Button khuc;
    @FXML
    private Pane a;
    @FXML
    private Pane b;
    @FXML
    private Pane c;
    @FXML
    private Pane pnTTDatBan;
    @FXML
    private ImageView A01;

    @FXML
    private ImageView A02;

    @FXML
    private ImageView A03;

    @FXML
    private ImageView A04;

    @FXML
    private ImageView A05;

    @FXML
    private ImageView A06;

    @FXML
    private ImageView A07;

    @FXML
    private ImageView A08;

    @FXML
    private ImageView A09;

    @FXML
    private ImageView A10;

    @FXML
    private ImageView A11;

    @FXML
    private ImageView A12;

    @FXML
    private ImageView A13;

    @FXML
    private ImageView A14;

    @FXML
    private ImageView A15;

    @FXML
    private ImageView B01;

    @FXML
    private ImageView B02;

    @FXML
    private ImageView B03;

    @FXML
    private ImageView B04;

    @FXML
    private ImageView B05;

    @FXML
    private ImageView B06;

    @FXML
    private ImageView B07;

    @FXML
    private ImageView B08;

    @FXML
    private ImageView B09;

    @FXML
    private ImageView B10;

    @FXML
    private ImageView B11;

    @FXML
    private ImageView B12;

    @FXML
    private ImageView B13;

    @FXML
    private ImageView B14;

    @FXML
    private ImageView B15;

    @FXML
    private ImageView C01;

    @FXML
    private ImageView C02;

    @FXML
    private ImageView C03;

    @FXML
    private ImageView C04;

    @FXML
    private ImageView C05;

    @FXML
    private ImageView C06;

    @FXML
    private ImageView C07;

    @FXML
    private ImageView C08;

    @FXML
    private ImageView C09;

    @FXML
    private ImageView C10;

    @FXML
    private ImageView C11;

    @FXML
    private ImageView C12;

    @FXML
    private ImageView C13;

    @FXML
    private ImageView C14;

    @FXML
    private ImageView C15;
    @FXML
    private DatePicker chonNgay;
    @FXML
    private ComboBox<String> chonBuoi;
    @FXML
    private TextField txtSDTKH;
    private QuanLyHoaDon_DAO hoaDonDAO = new QuanLyHoaDon_DAO();
    private Connection conn;
    private Stage trangChuStage;
    private ThongTinNhanVien_Control thongTinNhanVienControl;
    private QuanLyBan_Control controller;
    ObservableList<HoaDon> danhSachAllHD = FXCollections.observableArrayList(hoaDonDAO.getAllHoaDonList());
    private ObservableList<HoaDon> listHoaDon = FXCollections.observableArrayList();
    private ObservableList<HoaDon> listHoaDon2 = FXCollections.observableArrayList();
    private DatBan_Control datBanControl;
    public void setTrangChuStage(Stage trangChuStage) {
        this.trangChuStage = trangChuStage;
    }
    private NhanVien nv = SessionManager.getInstance().getCurrentNhanVien();
    private String maNV = nv.getMaNV();
    private KhachHang_DAO khachHangDao = new KhachHang_DAO();
    ObservableList<KhachHang> danhSachKH = FXCollections.observableArrayList(khachHangDao.getKhachHangList());
    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> buoi = FXCollections.observableArrayList("Buổi sáng", "Buổi tối");
        chonBuoi.setItems(buoi);
//        for (HoaDon hd : danhSachAllHD) {
//            LocalDateTime ngayGioDatBan = hd.getNgayDat().atTime(hd.getGioDatBan());
//            LocalDateTime ngayGioHienTai = LocalDateTime.now();
//            listHoaDon2.clear();
//            listHoaDon2.addAll(hoaDonDAO.getHoaDonList4(hd.getMaHD()));
//            if (ngayGioHienTai.isAfter(ngayGioDatBan.plusMinutes(30)) && listHoaDon2.size() == 0) {
//                try {
//                    hoaDonDAO.huyHoaDon(hd.getMaHD());
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
        ListView<String> listView = new ListView<>();
        for (HoaDon hd : danhSachAllHD) {
            LocalDateTime ngayGioDatBan = hd.getNgayDat().atTime(hd.getGioDatBan());
            LocalDateTime ngayGioHienTai = LocalDateTime.now();
            if (ngayGioHienTai.isAfter(ngayGioDatBan.plusMinutes(30)) && !hd.isCheckIn()) {
                try {
                    hoaDonDAO.huyHoaDon(hd.getMaHD());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        txtSDTKH.setOnAction(event -> {
            String soDienThoai = txtSDTKH.getText().trim();

            // Kiểm tra định dạng số điện thoại
            if (!soDienThoai.matches("^(03|05|07|08|09)\\d{8}$")) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại không hợp lệ!");
                return;
            }
            listHoaDon2.clear();
            for (HoaDon hd : danhSachAllHD) {
                if(hd.getSDT().equals(soDienThoai) && hd.getTrangThaiHoaDon().getDisplayName().equals("Chưa thanh toán")) {
                    listHoaDon2.add(hd);
                }
            }
            KhachHang khachHang = findKhachHangBySdt(txtSDTKH.getText());
            if(khachHang == null) {
                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Số điện thoại chưa có trong hệ thống!");
            } else
            if (listHoaDon2.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Số điện thoại không có hóa đơn!");
            } else {
                // Hiển thị danh sách hóa đơn
                StringBuilder hoaDonMessage = new StringBuilder("Các hóa đơn của số điện thoại: " + soDienThoai +"\n");
                for (HoaDon hoaDon : listHoaDon2) {
                    hoaDonMessage.append("Mã: " + hoaDon.getMaHD() +  ", Bàn: " + hoaDon.getMaBan() + ", Ngày đặt: " + hoaDon.getNgayDat()).append("\n");
                }
                showAlert(Alert.AlertType.INFORMATION, "Danh sách hóa đơn", hoaDonMessage.toString());
            }
        });
        a.setVisible(true);
        b.setVisible(false);
        c.setVisible(false);
        pnTTDatBan.setVisible(false);
        chonNgay.setValue(LocalDate.now());
        List<String> maBan = new ArrayList<>();

//         Tạo các mã từ A01 đến A15, B01 đến B15, C01 đến C15
        for (char prefix = 'A'; prefix <= 'C'; prefix++) {
            for (int i = 1; i <= 15; i++) {
                maBan.add(String.format("%c%02d", prefix, i));
            }
        }

        // In tất cả các mã trong maBan
        for (String ma : maBan) {

            int[] thongKeHoaDon = hoaDonDAO.demHoaDonChuaThanhToanTheoMaBanNgay(ma, Date.valueOf(chonNgay.getValue()));
            int soHoaDonCheckInChuaThanhToan = thongKeHoaDon[0];
            int soHoaDonChuaCheckInChuaThanhToan = thongKeHoaDon[1];

            if (soHoaDonChuaCheckInChuaThanhToan > 0 && soHoaDonCheckInChuaThanhToan == 0) {
                Image banDaDat = new Image(getClass().getResourceAsStream("/images/banDaDat.png"));
                switch (ma) {
                    case "A01":
                        A01.setImage(banDaDat);
                        break;
                    case "A02":
                        A02.setImage(banDaDat);
                        break;
                    case "A03":
                        A03.setImage(banDaDat);
                        break;
                    case "A04":
                        A04.setImage(banDaDat);
                        break;
                    case "A05":
                        A05.setImage(banDaDat);
                        break;
                    case "A06":
                        A06.setImage(banDaDat);
                        break;
                    case "A07":
                        A07.setImage(banDaDat);
                        break;
                    case "A08":
                        A08.setImage(banDaDat);
                        break;
                    case "A09":
                        A09.setImage(banDaDat);
                        break;
                    case "A10":
                        A10.setImage(banDaDat);
                        break;
                    case "A11":
                        A11.setImage(banDaDat);
                        break;
                    case "A12":
                        A12.setImage(banDaDat);
                        break;
                    case "A13":
                        A13.setImage(banDaDat);
                        break;
                    case "A14":
                        A14.setImage(banDaDat);
                        break;
                    case "A15":
                        A15.setImage(banDaDat);
                        break;
                    case "B01":
                        B01.setImage(banDaDat);
                        break;
                    case "B02":
                        B02.setImage(banDaDat);
                        break;
                    case "B03":
                        B03.setImage(banDaDat);
                        break;
                    case "B04":
                        B04.setImage(banDaDat);
                        break;
                    case "B05":
                        B05.setImage(banDaDat);
                        break;
                    case "B06":
                        B06.setImage(banDaDat);
                        break;
                    case "B07":
                        B07.setImage(banDaDat);
                        break;
                    case "B08":
                        B08.setImage(banDaDat);
                        break;
                    case "B09":
                        B09.setImage(banDaDat);
                        break;
                    case "B10":
                        B10.setImage(banDaDat);
                        break;
                    case "B11":
                        B11.setImage(banDaDat);
                        break;
                    case "B12":
                        B12.setImage(banDaDat);
                        break;
                    case "B13":
                        B13.setImage(banDaDat);
                        break;
                    case "B14":
                        B14.setImage(banDaDat);
                        break;
                    case "B15":
                        B15.setImage(banDaDat);
                        break;

                    case "C01":
                        C01.setImage(banDaDat);
                        break;
                    case "C02":
                        C02.setImage(banDaDat);
                        break;
                    case "C03":
                        C03.setImage(banDaDat);
                        break;
                    case "C04":
                        C04.setImage(banDaDat);
                        break;
                    case "C05":
                        C05.setImage(banDaDat);
                        break;
                    case "C06":
                        C06.setImage(banDaDat);
                        break;
                    case "C07":
                        C07.setImage(banDaDat);
                        break;
                    case "C08":
                        C08.setImage(banDaDat);
                        break;
                    case "C09":
                        C09.setImage(banDaDat);
                        break;
                    case "C10":
                        C10.setImage(banDaDat);
                        break;
                    case "C11":
                        C11.setImage(banDaDat);
                        break;
                    case "C12":
                        C12.setImage(banDaDat);
                        break;
                    case "C13":
                        C13.setImage(banDaDat);
                        break;
                    case "C14":
                        C14.setImage(banDaDat);
                        break;
                    case "C15":
                        C15.setImage(banDaDat);
                        break;
                }
            } else  if (soHoaDonCheckInChuaThanhToan > 0) {
                Image banDangPV = new Image(getClass().getResourceAsStream("/images/banDangPV.png"));
                switch (ma) {
                    case "A01":
                        A01.setImage(banDangPV);
                        break;
                    case "A02":
                        A02.setImage(banDangPV);
                        break;
                    case "A03":
                        A03.setImage(banDangPV);
                        break;
                    case "A04":
                        A04.setImage(banDangPV);
                        break;
                    case "A05":
                        A05.setImage(banDangPV);
                        break;
                    case "A06":
                        A06.setImage(banDangPV);
                        break;
                    case "A07":
                        A07.setImage(banDangPV);
                        break;
                    case "A08":
                        A08.setImage(banDangPV);
                        break;
                    case "A09":
                        A09.setImage(banDangPV);
                        break;
                    case "A10":
                        A10.setImage(banDangPV);
                        break;
                    case "A11":
                        A11.setImage(banDangPV);
                        break;
                    case "A12":
                        A12.setImage(banDangPV);
                        break;
                    case "A13":
                        A13.setImage(banDangPV);
                        break;
                    case "A14":
                        A14.setImage(banDangPV);
                        break;
                    case "A15":
                        A15.setImage(banDangPV);
                        break;
                    case "B01":
                        B01.setImage(banDangPV);
                        break;
                    case "B02":
                        B02.setImage(banDangPV);
                        break;
                    case "B03":
                        B03.setImage(banDangPV);
                        break;
                    case "B04":
                        B04.setImage(banDangPV);
                        break;
                    case "B05":
                        B05.setImage(banDangPV);
                        break;
                    case "B06":
                        B06.setImage(banDangPV);
                        break;
                    case "B07":
                        B07.setImage(banDangPV);
                        break;
                    case "B08":
                        B08.setImage(banDangPV);
                        break;
                    case "B09":
                        B09.setImage(banDangPV);
                        break;
                    case "B10":
                        B10.setImage(banDangPV);
                        break;
                    case "B11":
                        B11.setImage(banDangPV);
                        break;
                    case "B12":
                        B12.setImage(banDangPV);
                        break;
                    case "B13":
                        B13.setImage(banDangPV);
                        break;
                    case "B14":
                        B14.setImage(banDangPV);
                        break;
                    case "B15":
                        B15.setImage(banDangPV);
                        break;

                    case "C01":
                        C01.setImage(banDangPV);
                        break;
                    case "C02":
                        C02.setImage(banDangPV);
                        break;
                    case "C03":
                        C03.setImage(banDangPV);
                        break;
                    case "C04":
                        C04.setImage(banDangPV);
                        break;
                    case "C05":
                        C05.setImage(banDangPV);
                        break;
                    case "C06":
                        C06.setImage(banDangPV);
                        break;
                    case "C07":
                        C07.setImage(banDangPV);
                        break;
                    case "C08":
                        C08.setImage(banDangPV);
                        break;
                    case "C09":
                        C09.setImage(banDangPV);
                        break;
                    case "C10":
                        C10.setImage(banDangPV);
                        break;
                    case "C11":
                        C11.setImage(banDangPV);
                        break;
                    case "C12":
                        C12.setImage(banDangPV);
                        break;
                    case "C13":
                        C13.setImage(banDangPV);
                        break;
                    case "C14":
                        C14.setImage(banDangPV);
                        break;
                    case "C15":
                        C15.setImage(banDangPV);
                        break;

                }
            }


        }
//
//            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {

//            Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(5), event -> {

//                if (!pnTTDatBan.isVisible()) {
//                huyHoaDonQuaHan30Phut(); }
//
//            }));
//            timeline.setCycleCount(Timeline.INDEFINITE);
//            timeline.play();

    }
    public void KhuAClicked(ActionEvent actionEvent) {
        a.setVisible(true);
        b.setVisible(false);
        c.setVisible(false);
    }

    public void KhuBClicked(ActionEvent actionEvent) {
        a.setVisible(false);
        b.setVisible(true);
        c.setVisible(false);
    }

    public void KhuCClicked(ActionEvent actionEvent) {
        a.setVisible(false);
        b.setVisible(false);
        c.setVisible(true);
    }

    public void chonBan(MouseEvent mouseEvent) {
        ImageView selectedImageView = (ImageView) mouseEvent.getSource();

        // Lấy ID của ImageView đó
        String banID = selectedImageView.getId();

        // In ra để kiểm tra (hoặc thực hiện hành động khác)
        System.out.println("Người dùng đã chọn bàn: " + banID);

        try {
            // Tải file FXML và lấy controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/QuanLyBan.fxml"));
            Parent DatBanContent = loader.load();

            // Lấy controller từ FXMLLoader và truyền banID
            controller = loader.getController();
            controller.setBanID(banID);
            controller.setMaNV(maNV);
            System.out.println("Nhân viên chọn bàn có mã" + controller.getMaNV());
            // Thay đổi giao diện
            pnTTDatBan.getChildren().clear();
            pnTTDatBan.getChildren().add(DatBanContent);
            pnTTDatBan.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void locTheoNgay() {
        List<String> maBan = new ArrayList<>();
        System.out.println(chonNgay.getValue());
        //         Tạo các mã từ A01 đến A15, B01 đến B15, C01 đến C15
        for (char prefix = 'A'; prefix <= 'C'; prefix++) {
            for (int i = 1; i <= 15; i++) {
                maBan.add(String.format("%c%02d", prefix, i));
            }
        }
        // In tất cả các mã trong maBan
        for (String ma : maBan) {
            String selectedBuoi = chonBuoi.getValue();
            int soHoaDonCheckInChuaThanhToan;
            int soHoaDonChuaCheckInChuaThanhToan;
            if (selectedBuoi != null) {
                int startHour = selectedBuoi.equals("Buổi sáng") ? 0 : 12;
                int endHour = selectedBuoi.equals("Buổi sáng") ? 11 : 23;


                int[] thongKeHoaDon = hoaDonDAO.demHoaDonChuaThanhToanTheoMaBanNgayGio(ma, Date.valueOf(chonNgay.getValue()), startHour, endHour);
                soHoaDonCheckInChuaThanhToan = thongKeHoaDon[0];
                soHoaDonChuaCheckInChuaThanhToan = thongKeHoaDon[1];
            } else {
                int[] thongKeHoaDon = hoaDonDAO.demHoaDonChuaThanhToanTheoMaBanNgay(ma, Date.valueOf(chonNgay.getValue()));
                soHoaDonCheckInChuaThanhToan = thongKeHoaDon[0];
                soHoaDonChuaCheckInChuaThanhToan = thongKeHoaDon[1];
            }

            if (soHoaDonChuaCheckInChuaThanhToan > 0 && soHoaDonCheckInChuaThanhToan == 0) {
                Image banDaDat = new Image(getClass().getResourceAsStream("/images/banDaDat.png"));
                switch (ma) {
                    case "A01":
                        A01.setImage(banDaDat);
                        break;
                    case "A02":
                        A02.setImage(banDaDat);
                        break;
                    case "A03":
                        A03.setImage(banDaDat);
                        break;
                    case "A04":
                        A04.setImage(banDaDat);
                        break;
                    case "A05":
                        A05.setImage(banDaDat);
                        break;
                    case "A06":
                        A06.setImage(banDaDat);
                        break;
                    case "A07":
                        A07.setImage(banDaDat);
                        break;
                    case "A08":
                        A08.setImage(banDaDat);
                        break;
                    case "A09":
                        A09.setImage(banDaDat);
                        break;
                    case "A10":
                        A10.setImage(banDaDat);
                        break;
                    case "A11":
                        A11.setImage(banDaDat);
                        break;
                    case "A12":
                        A12.setImage(banDaDat);
                        break;
                    case "A13":
                        A13.setImage(banDaDat);
                        break;
                    case "A14":
                        A14.setImage(banDaDat);
                        break;
                    case "A15":
                        A15.setImage(banDaDat);
                        break;

                    case "B01":
                        B01.setImage(banDaDat);
                        break;
                    case "B02":
                        B02.setImage(banDaDat);
                        break;
                    case "B03":
                        B03.setImage(banDaDat);
                        break;
                    case "B04":
                        B04.setImage(banDaDat);
                        break;
                    case "B05":
                        B05.setImage(banDaDat);
                        break;
                    case "B06":
                        B06.setImage(banDaDat);
                        break;
                    case "B07":
                        B07.setImage(banDaDat);
                        break;
                    case "B08":
                        B08.setImage(banDaDat);
                        break;
                    case "B09":
                        B09.setImage(banDaDat);
                        break;
                    case "B10":
                        B10.setImage(banDaDat);
                        break;
                    case "B11":
                        B11.setImage(banDaDat);
                        break;
                    case "B12":
                        B12.setImage(banDaDat);
                        break;
                    case "B13":
                        B13.setImage(banDaDat);
                        break;
                    case "B14":
                        B14.setImage(banDaDat);
                        break;
                    case "B15":
                        B15.setImage(banDaDat);
                        break;

                    case "C01":
                        C01.setImage(banDaDat);
                        break;
                    case "C02":
                        C02.setImage(banDaDat);
                        break;
                    case "C03":
                        C03.setImage(banDaDat);
                        break;
                    case "C04":
                        C04.setImage(banDaDat);
                        break;
                    case "C05":
                        C05.setImage(banDaDat);
                        break;
                    case "C06":
                        C06.setImage(banDaDat);
                        break;
                    case "C07":
                        C07.setImage(banDaDat);
                        break;
                    case "C08":
                        C08.setImage(banDaDat);
                        break;
                    case "C09":
                        C09.setImage(banDaDat);
                        break;
                    case "C10":
                        C10.setImage(banDaDat);
                        break;
                    case "C11":
                        C11.setImage(banDaDat);
                        break;
                    case "C12":
                        C12.setImage(banDaDat);
                        break;
                    case "C13":
                        C13.setImage(banDaDat);
                        break;
                    case "C14":
                        C14.setImage(banDaDat);
                        break;
                    case "C15":
                        C15.setImage(banDaDat);
                        break;
                }
            } else  if (soHoaDonCheckInChuaThanhToan > 0) {
                Image banDangPV = new Image(getClass().getResourceAsStream("/images/banDangPV.png"));
                switch (ma) {
                    case "A01":
                        A01.setImage(banDangPV);
                        break;
                    case "A02":
                        A02.setImage(banDangPV);
                        break;
                    case "A03":
                        A03.setImage(banDangPV);
                        break;
                    case "A04":
                        A04.setImage(banDangPV);
                        break;
                    case "A05":
                        A05.setImage(banDangPV);
                        break;
                    case "A06":
                        A06.setImage(banDangPV);
                        break;
                    case "A07":
                        A07.setImage(banDangPV);
                        break;
                    case "A08":
                        A08.setImage(banDangPV);
                        break;
                    case "A09":
                        A09.setImage(banDangPV);
                        break;
                    case "A10":
                        A10.setImage(banDangPV);
                        break;
                    case "A11":
                        A11.setImage(banDangPV);
                        break;
                    case "A12":
                        A12.setImage(banDangPV);
                        break;
                    case "A13":
                        A13.setImage(banDangPV);
                        break;
                    case "A14":
                        A14.setImage(banDangPV);
                        break;
                    case "A15":
                        A15.setImage(banDangPV);
                        break;

                    case "B01":
                        B01.setImage(banDangPV);
                        break;
                    case "B02":
                        B02.setImage(banDangPV);
                        break;
                    case "B03":
                        B03.setImage(banDangPV);
                        break;
                    case "B04":
                        B04.setImage(banDangPV);
                        break;
                    case "B05":
                        B05.setImage(banDangPV);
                        break;
                    case "B06":
                        B06.setImage(banDangPV);
                        break;
                    case "B07":
                        B07.setImage(banDangPV);
                        break;
                    case "B08":
                        B08.setImage(banDangPV);
                        break;
                    case "B09":
                        B09.setImage(banDangPV);
                        break;
                    case "B10":
                        B10.setImage(banDangPV);
                        break;
                    case "B11":
                        B11.setImage(banDangPV);
                        break;
                    case "B12":
                        B12.setImage(banDangPV);
                        break;
                    case "B13":
                        B13.setImage(banDangPV);
                        break;
                    case "B14":
                        B14.setImage(banDangPV);
                        break;
                    case "B15":
                        B15.setImage(banDangPV);
                        break;

                    case "C01":
                        C01.setImage(banDangPV);
                        break;
                    case "C02":
                        C02.setImage(banDangPV);
                        break;
                    case "C03":
                        C03.setImage(banDangPV);
                        break;
                    case "C04":
                        C04.setImage(banDangPV);
                        break;
                    case "C05":
                        C05.setImage(banDangPV);
                        break;
                    case "C06":
                        C06.setImage(banDangPV);
                        break;
                    case "C07":
                        C07.setImage(banDangPV);
                        break;
                    case "C08":
                        C08.setImage(banDangPV);
                        break;
                    case "C09":
                        C09.setImage(banDangPV);
                        break;
                    case "C10":
                        C10.setImage(banDangPV);
                        break;
                    case "C11":
                        C11.setImage(banDangPV);
                        break;
                    case "C12":
                        C12.setImage(banDangPV);
                        break;
                    case "C13":
                        C13.setImage(banDangPV);
                        break;
                    case "C14":
                        C14.setImage(banDangPV);
                        break;
                    case "C15":
                        C15.setImage(banDangPV);
                        break;
                }
            } else {
                Image banTrong = new Image(getClass().getResourceAsStream("/images/banTrong.png"));
                switch (ma) {
                    case "A01":
                        A01.setImage(banTrong);
                        break;
                    case "A02":
                        A02.setImage(banTrong);
                        break;
                    case "A03":
                        A03.setImage(banTrong);
                        break;
                    case "A04":
                        A04.setImage(banTrong);
                        break;
                    case "A05":
                        A05.setImage(banTrong);
                        break;
                    case "A06":
                        A06.setImage(banTrong);
                        break;
                    case "A07":
                        A07.setImage(banTrong);
                        break;
                    case "A08":
                        A08.setImage(banTrong);
                        break;
                    case "A09":
                        A09.setImage(banTrong);
                        break;
                    case "A10":
                        A10.setImage(banTrong);
                        break;
                    case "A11":
                        A11.setImage(banTrong);
                        break;
                    case "A12":
                        A12.setImage(banTrong);
                        break;
                    case "A13":
                        A13.setImage(banTrong);
                        break;
                    case "A14":
                        A14.setImage(banTrong);
                        break;
                    case "A15":
                        A15.setImage(banTrong);
                        break;

                    case "B01":
                        B01.setImage(banTrong);
                        break;
                    case "B02":
                        B02.setImage(banTrong);
                        break;
                    case "B03":
                        B03.setImage(banTrong);
                        break;
                    case "B04":
                        B04.setImage(banTrong);
                        break;
                    case "B05":
                        B05.setImage(banTrong);
                        break;
                    case "B06":
                        B06.setImage(banTrong);
                        break;
                    case "B07":
                        B07.setImage(banTrong);
                        break;
                    case "B08":
                        B08.setImage(banTrong);
                        break;
                    case "B09":
                        B09.setImage(banTrong);
                        break;
                    case "B10":
                        B10.setImage(banTrong);
                        break;
                    case "B11":
                        B11.setImage(banTrong);
                        break;
                    case "B12":
                        B12.setImage(banTrong);
                        break;
                    case "B13":
                        B13.setImage(banTrong);
                        break;
                    case "B14":
                        B14.setImage(banTrong);
                        break;
                    case "B15":
                        B15.setImage(banTrong);
                        break;

                    case "C01":
                        C01.setImage(banTrong);
                        break;
                    case "C02":
                        C02.setImage(banTrong);
                        break;
                    case "C03":
                        C03.setImage(banTrong);
                        break;
                    case "C04":
                        C04.setImage(banTrong);
                        break;
                    case "C05":
                        C05.setImage(banTrong);
                        break;
                    case "C06":
                        C06.setImage(banTrong);
                        break;
                    case "C07":
                        C07.setImage(banTrong);
                        break;
                    case "C08":
                        C08.setImage(banTrong);
                        break;
                    case "C09":
                        C09.setImage(banTrong);
                        break;
                    case "C10":
                        C10.setImage(banTrong);
                        break;
                    case "C11":
                        C11.setImage(banTrong);
                        break;
                    case "C12":
                        C12.setImage(banTrong);
                        break;
                    case "C13":
                        C13.setImage(banTrong);
                        break;
                    case "C14":
                        C14.setImage(banTrong);
                        break;
                    case "C15":
                        C15.setImage(banTrong);
                        break;
                }
            }

        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private KhachHang findKhachHangBySdt(String sdt) {
        for (KhachHang kh : danhSachKH) {
            if (kh.getsDT().equals(sdt)) {
                return kh;
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }
}
