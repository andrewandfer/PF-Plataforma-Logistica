package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdministradorController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAyuda;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnContacto;

    @FXML
    private Button btnGestionEnvios;

    @FXML
    private Button btnGestionRepartidores;

    @FXML
    private Button btnGestionUsuarios;

    @FXML
    private Button btnGestionIncidentes;

    @FXML
    private Button btnPanelMetricas;

    @FXML
    void onAyuda(ActionEvent event) {
        mostrarInfo("Ayuda",
                "Aquí puedes gestionar usuarios, repartidores, incidentes y ver metricas del sistema.");
    }

    @FXML
    void onContacto(ActionEvent event) {
        mostrarInfo("Contacto",
                "Escríbenos a soporte@enviorapido.com");
    }

    @FXML
    void onGestionEnvios(ActionEvent event) {
        Stage stage = (Stage) btnGestionEnvios.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "GestionEnviosAdministrador.fxml");
    }

    @FXML
    void onGestionRepartidores(ActionEvent event) {
        Stage stage = (Stage) btnGestionRepartidores.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "GestionRepartidoresAdministrador.fxml");
    }

    @FXML
    void onGestionUsuarios(ActionEvent event) {
        Stage stage = (Stage) btnGestionUsuarios.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "GestionUsuariosAdministrador.fxml");
    }

    @FXML
    void onCerrarSesion (ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cerrar Sesión");
            alert.setHeaderText(null);
            alert.setContentText("¿Estás seguro de que quieres cerrar sesión?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                SceneManager.cambiarEscena(stage, "login.fxml");
                System.out.println("Sesión cerrada - Redirigiendo al login...");

                mostrarAlerta("Sesión Cerrada",
                        "Has cerrado sesión correctamente. Redirigiendo al login...",
                        Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            System.err.println("Error al cerrar sesión: " + e.getMessage());
            mostrarAlerta("Error", "No se pudo cerrar la sesión", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onPanelMetricas(ActionEvent event) {
        Stage stage = (Stage) btnPanelMetricas.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "PanelMetricasAdministrador.fxml");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String titulo, String msg) {
        mostrarAlerta(titulo, msg, Alert.AlertType.INFORMATION);
    }
}