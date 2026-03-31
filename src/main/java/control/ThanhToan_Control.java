package control;

import dao.ThanhToan_DAO;
import entity.HoaDon;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Time;
import java.time.LocalDate;

public class ThanhToan_Control {
    @FXML
    private TableView<HoaDon> thanhtoan_hoadon;
    @FXML
    private TableColumn<HoaDon, String> colTenKH;
    @FXML
    private TableColumn<HoaDon, String> colSDT;
    @FXML
    private TableColumn<HoaDon, Time> colGioDatBan;
    @FXML
    private TableColumn<HoaDon, String> colMaBan;
    @FXML
    private TextField txt_sdt_tt;
    @FXML
    private TextField txt_tenkh_tt;
    @FXML
    private TextField txt_ban_tt;
    @FXML
    private Button btn_find_tt;
    private String maHoaDon;
    public ThanhToan_Control() {
    }
    @FXML
    public void initialize() {
        btn_find_tt.setOnMouseEntered(e -> btn_find_tt.setStyle("-fx-background-color: #1E88E5; -fx-text-fill: white; -fx-border-radius: 20px; -fx-background-radius: 20px;"));
        btn_find_tt.setOnMouseExited(e -> btn_find_tt.setStyle("-fx-background-color: #00BBFF; -fx-text-fill: white; -fx-border-radius: 20px; -fx-background-radius: 20px;"));
        ObservableList<HoaDon> hoaDonList = ThanhToan_DAO.getHoaDonList();
        // Tạo cột STT
        TableColumn<HoaDon, Integer> colStt = new TableColumn<>("STT");
        colStt.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<HoaDon, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<HoaDon, Integer> param) {
                // Lấy chỉ số từ hàng hiện tại trong TableView
                return new SimpleIntegerProperty(thanhtoan_hoadon.getItems().indexOf(param.getValue()) + 1).asObject();
            }
        });
        colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("sDT"));
        colGioDatBan.setCellValueFactory(new PropertyValueFactory<>("gioDatBan"));
        colMaBan.setCellValueFactory(new PropertyValueFactory<>("maBan"));
        thanhtoan_hoadon.setItems(hoaDonList);
        thanhtoan_hoadon.getColumns().add(0, colStt);
        thanhtoan_hoadon.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                HoaDon selectedHoaDon = thanhtoan_hoadon.getSelectionModel().getSelectedItem();
                if (selectedHoaDon != null) {
                    maHoaDon = selectedHoaDon.getMaHD();
                    openThanhtoan1Scene(selectedHoaDon, maHoaDon, this);
                }
            }
        });
    }

    // mo giao dien chi tiet hd
    private void openThanhtoan1Scene(HoaDon hoaDon, String maHoaDon, ThanhToan_Control thanhToanControl) {
        try {
            // Tải giao diện thanhtoan_1.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ThanhToan_1.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Thanh Toán");
            stage.getIcons().add(new Image("file:src/main/resources/images/payment.png"));
            //truyen bien maHoaDon qua control khac de tiep tuc xu li
            ThanhToan_1_Control controller = loader.getController();
            controller.setMaHoaDon(maHoaDon);
            controller.setThanhToanControl(thanhToanControl);
            System.out.println("Mã hóa đơn đã được truyền: " + maHoaDon);
            // Thiết lập Scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.requestFocus();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }}
    //ham loc theo cac tieu chi
    @FXML
    public void search_tt() {
        ObservableList<HoaDon> hoaDonList = ThanhToan_DAO.getHoaDonList();
        String tenKH = txt_tenkh_tt.getText().toLowerCase();
        String sdt = txt_sdt_tt.getText().toLowerCase();
        String maBan = txt_ban_tt.getText().toLowerCase();
        ObservableList<HoaDon> searchResult = FXCollections.observableArrayList();
        for (HoaDon hd : hoaDonList) {
            boolean matchesTenKH = tenKH.isEmpty() || hd.getTenKH().toLowerCase().contains(tenKH);
            boolean matchesSDT = sdt.isEmpty() || hd.getSDT().toLowerCase().contains(sdt);
            boolean matchesMaBan = maBan.isEmpty() || hd.getMaBan().toLowerCase().contains(maBan);

            if (matchesTenKH && matchesSDT && matchesMaBan) {
                searchResult.add(hd);
            }
        }
        thanhtoan_hoadon.setItems(searchResult);
    }
    //ham load lai du lieu moi khi sql update xong
    public void loadHoaDonData() {
        ObservableList<HoaDon> hoaDonList = ThanhToan_DAO.getHoaDonList();
        thanhtoan_hoadon.setItems(hoaDonList);
    }
}
