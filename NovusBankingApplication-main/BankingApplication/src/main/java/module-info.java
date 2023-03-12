module com.example.bankingapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.bankingapplication to javafx.fxml;
    opens com.example.bankingapplication.GUI.Controllers to javafx.fxml;
    exports com.example.bankingapplication;
}
