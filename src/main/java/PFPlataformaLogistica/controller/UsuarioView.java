package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.Utils.SesionManager;
import PFPlataformaLogistica.model.Persona;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UsuarioView {

    Persona usuario = SesionManager.getPersonaActual();

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnDirecciones;

    @FXML
    private Button btnEnvios;

    @FXML
    private Button btnMetodosPago;

    @FXML
    private Button btnPerfil;

    @FXML
    private Label lblTituloSeccion;

    @FXML
    void OnCerrarSesion(ActionEvent event) {

    }

    @FXML
    void OnConsultarEnvios(ActionEvent event) {

    }

    @FXML
    void OnDireccionesFrecuentes(ActionEvent event) {

    }

    @FXML
    void OnMetodosDePago(ActionEvent event) {

    }

    @FXML
    void OnModificarPerfil(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.cambiarEscena(stage, "ModificarPerfilUsuario.fxml");


    }

}
