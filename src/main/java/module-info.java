module cafeteria.pfplataformalogistica {
    requires javafx.controls;
    requires javafx.fxml;


    opens cafeteria.pfplataformalogistica to javafx.fxml;
    exports cafeteria.pfplataformalogistica;
    exports cafeteria.pfplataformalogistica.view;
    opens cafeteria.pfplataformalogistica.view to javafx.fxml;
    exports cafeteria.pfplataformalogistica.controller;
    opens cafeteria.pfplataformalogistica.controller to javafx.fxml;
}