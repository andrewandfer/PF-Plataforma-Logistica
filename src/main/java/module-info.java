module cafeteria.PFPlataformaLogistica {
    requires javafx.controls;
    requires javafx.fxml;


    opens PFPlataformaLogistica to javafx.fxml;
    exports PFPlataformaLogistica;
    exports PFPlataformaLogistica.view;
    opens PFPlataformaLogistica.view to javafx.fxml;
    exports PFPlataformaLogistica.controller;
    opens PFPlataformaLogistica.controller to javafx.fxml;
}