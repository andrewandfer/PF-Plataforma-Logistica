module cafeteria.pfplataformalogistica {
    requires javafx.controls;
    requires javafx.fxml;


    opens cafeteria.pfplataformalogistica to javafx.fxml;
    exports cafeteria.pfplataformalogistica;
}