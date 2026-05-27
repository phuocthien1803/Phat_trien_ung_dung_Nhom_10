package control;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class HoTro_Control implements Initializable {

    @FXML private TableView<QuyDinh> tableQuyDinh;
    @FXML private TableColumn<QuyDinh, String> colQuyDinh;
    @FXML private TableColumn<QuyDinh, String> colNoiDung;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cấu hình cột cho TableView
        colQuyDinh.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTenQuyDinh()));
        colNoiDung.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChiTiet()));

        // Nạp dữ liệu
        ObservableList<QuyDinh> data = FXCollections.observableArrayList(
                new QuyDinh("Không sửa mã đặt bàn", "Chỉ được đổi thời gian/bàn."),
                new QuyDinh("Check-in mới được thanh toán", "Tránh tạo hóa đơn ảo trên hệ thống."),
                new QuyDinh("Không đổi món khi bếp đã làm", "Tuân thủ theo quy định hủy trả món của nhà hàng."),
                new QuyDinh("Khuyến mãi tối đa 50%", "Không được phép cộng dồn voucher vượt mức quy định.")
        );
        tableQuyDinh.setItems(data);
    }

    // Class nội bộ hỗ trợ hiển thị dữ liệu bảng
    public static class QuyDinh {
        private final String tenQuyDinh;
        private final String chiTiet;

        public QuyDinh(String tenQuyDinh, String chiTiet) {
            this.tenQuyDinh = tenQuyDinh;
            this.chiTiet = chiTiet;
        }
        public String getTenQuyDinh() { return tenQuyDinh; }
        public String getChiTiet() { return chiTiet; }
    }
}