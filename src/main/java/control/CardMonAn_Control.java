package control;

import entity.MonAn;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;

public class CardMonAn_Control {

    @FXML
    private AnchorPane cardMon;

    @FXML
    private Label giaMonAn;

    @FXML
    private ImageView hinhMonAn;

    @FXML
    private Label tenMonAn;

    private MonAn monAn;
    private Image image;

    public void setData(MonAn monAn) {
        this.monAn = monAn;
        tenMonAn.setText(monAn.getTenMonAn());
        giaMonAn.setText(String.valueOf(monAn.getGia()) + " VNĐ");

        // Xây dựng đường dẫn tài nguyên (bắt đầu bằng / để chỉ đường dẫn tuyệt đối trong resources)
        String imagePath = "/monAnImages/" + monAn.getHinhAnh();

        // Tải hình ảnh từ resource stream
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream != null) {
            image = new Image(imageStream, 150, 109, false, true);
            hinhMonAn.setImage(image);
        }
    }

    public Label getTenMonAn() {
        return tenMonAn;
    }

    @FXML
    public void initialize() {

    }
}


