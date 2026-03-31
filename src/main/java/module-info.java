module com.example.nhahangkkts_nhom10 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires itextpdf;
    requires datetime.picker.javafx;
    requires spring.security.crypto;
    opens gui to javafx.fxml;
    exports gui;
    exports control;
    opens control to javafx.fxml;
    opens entity to javafx.base;
}