module com.example.countdown {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.countdown to javafx.fxml;
    exports com.example.countdown;
}