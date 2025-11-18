package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.Utils.SesionManager;
import PFPlataformaLogistica.model.Persona;
import PFPlataformaLogistica.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Optional;

public class UsuarioViewController {

    Persona usuario = SesionManager.getUsuarioActual(Usuario.class);

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnDirecciones;

    @FXML
    private Button btnEnvios;


    @FXML
    private Button btnPerfil;

    @FXML
    private Label lblTituloSeccion;

    @FXML
    void OnCerrarSesion(ActionEvent event) {
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
    private void OnConsultarEnvios() {
        Stage stage = (Stage) btnEnvios.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "ConsultarEnvioUsuario.fxml");
    }

    @FXML
    void OnDireccionesFrecuentes(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.cambiarEscena(stage, "DireccionUsuarioManager.fxml");
    }

    @FXML
    void OnModificarPerfil(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.cambiarEscena(stage, "ModificarPerfilUsuario.fxml");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
