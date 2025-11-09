module cafeteria.PFPlataformaLogistica {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    // Paquete donde están tus FXML, imágenes y otros recursos
    opens PFPlataformaLogistica to javafx.fxml;

    // Paquete de tus controladores
    opens PFPlataformaLogistica.controller to javafx.fxml;

    // Exportar para que el módulo principal pueda usar las clases
    exports PFPlataformaLogistica;
    exports PFPlataformaLogistica.controller;
}
