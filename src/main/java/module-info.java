module com.example.platformerfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens com.example.platformerfx to javafx.fxml;
    exports com.example.platformerfx;
}