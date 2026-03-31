package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class QuanLiMonAn_GUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TrangChu_GUI.class.getResource("QuanLiMonAn.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Chương trình quản lý nhà hàng");
        stage.getIcons().add(new Image("file:src/main/resources/images/kktsno10.png"));
        stage.setScene(scene);
        stage.show();
    }

}
