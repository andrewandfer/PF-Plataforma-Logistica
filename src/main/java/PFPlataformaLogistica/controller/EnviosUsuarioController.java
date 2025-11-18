package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.Utils.SesionManager;
import PFPlataformaLogistica.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EnviosUsuarioController implements Initializable {
    Usuario usuarioActual;
    private Empresa empresa;

    @FXML private Button btnVolver;
    @FXML private TextField txtBuscarEnvio; // Header
    @FXML private TextField txtBuscarEnvioSidebar; // Sidebar
    @FXML private TextField txtBuscarEnvioMain; // Main area

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        empresa = Empresa.getInstance();
        usuarioActual = SesionManager.getUsuarioActual(Usuario.class);
    }

    // MÉTODO ÚNICO para rastrear desde cualquier campo
    @FXML
    private void rastrearEnvio() {
        String id = "";

        // Determinar de qué campo viene la búsqueda
        if (txtBuscarEnvioMain != null && !txtBuscarEnvioMain.getText().trim().isEmpty()) {
            id = txtBuscarEnvioMain.getText().trim();
        } else if (txtBuscarEnvio != null && !txtBuscarEnvio.getText().trim().isEmpty()) {
            id = txtBuscarEnvio.getText().trim();
        } else if (txtBuscarEnvioSidebar != null && !txtBuscarEnvioSidebar.getText().trim().isEmpty()) {
            id = txtBuscarEnvioSidebar.getText().trim();
        }

        if (id.isEmpty()) {
            mostrarAlerta("Validación", "Por favor ingresa el ID del envío", Alert.AlertType.WARNING);
            return;
        }

        mostrarInformacionEnvioRandom(id);

        // Limpiar campos después de buscar
        if (txtBuscarEnvioMain != null) txtBuscarEnvioMain.clear();
        if (txtBuscarEnvio != null) txtBuscarEnvio.clear();
        if (txtBuscarEnvioSidebar != null) txtBuscarEnvioSidebar.clear();
    }

    private void mostrarInformacionEnvioRandom(String idBuscado) {
        // (Mantener el mismo código del método anterior que te pasé)
        // ... código del método de generación random ...
    }

    // Mantener los demás métodos existentes...
    @FXML
    private void volverAlMenu(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.cambiarEscena(stage, "MenuUsuario.fxml");
    }

    @FXML
    private void abrirCrearEnvio(ActionEvent event) {
        // Tu código existente...
    }

    @FXML
    private void abrirRastrearEnvio(ActionEvent event) {
        // Ya no necesitas cambiar de escena, se hace en la misma pantalla
        if (txtBuscarEnvioMain != null) {
            txtBuscarEnvioMain.requestFocus();
        }
    }

    @FXML
    private void aplicarFiltros(ActionEvent event) {
        // Tu código existente...
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        // Tu código existente...
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}