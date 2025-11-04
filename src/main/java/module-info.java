module com.example.profile {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.prefs;


    opens com.example.profile to javafx.fxml;
    exports com.example.profile;
}