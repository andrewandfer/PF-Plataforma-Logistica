module cafeteria.PFPlataformaLogistica {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens PFPlataformaLogistica to javafx.fxml;
    exports PFPlataformaLogistica to javafx.graphics;

    exports PFPlataformaLogistica.controller;
    opens PFPlataformaLogistica.controller to javafx.fxml;
}