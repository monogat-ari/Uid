module com.example.profile {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.profile to javafx.fxml;
    exports com.example.profile;
}