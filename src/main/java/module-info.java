module cafeteria.pfplataformalogistica {
    requires javafx.controls;
    requires javafx.fxml;


    opens pfplataformalogistica to javafx.fxml;
    exports pfplataformalogistica;
    exports pfplataformalogistica.view;
    opens pfplataformalogistica.view to javafx.fxml;
    exports pfplataformalogistica.controller;
    opens pfplataformalogistica.controller to javafx.fxml;
}