package PFPlataformaLogistica.controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AdministradorController {

    public class AdministradorPrincipalController {

        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private Button btnAyuda;

        @FXML
        private Button btnContacto;

        @FXML
        private Button btnGestionRepartidores;

        @FXML
        private Button btnGestionUsuarios;

        @FXML
        private Button btnMetricas;

        @FXML
        private Button btnCerrarSesion;

        @FXML
        private Button btnIncidentes;

        @FXML
        void onAyuda(ActionEvent event) {

        }

        @FXML
        void onContacto(ActionEvent event) {

        }

        @FXML
        void onGestionRepartidores(ActionEvent event) {

        }

        @FXML
        void onGestionUsuarios(ActionEvent event) {

        }

        @FXML
        void onMetricas(ActionEvent event) {

        }

        @FXML
        void initialize() {
            assert btnAyuda != null : "fx:id=\"btnAyuda\" was not injected: check your FXML file 'prueba.fxml'.";
            assert btnContacto != null : "fx:id=\"btnContacto\" was not injected: check your FXML file 'prueba.fxml'.";
            assert btnGestionRepartidores != null : "fx:id=\"btnGestionRepartidores\" was not injected: check your FXML file 'prueba.fxml'.";
            assert btnGestionUsuarios != null : "fx:id=\"btnGestionUsuarios\" was not injected: check your FXML file 'prueba.fxml'.";
            assert btnMetricas != null : "fx:id=\"btnMetricas\" was not injected: check your FXML file 'prueba.fxml'.";
            assert btnCerrarSesion != null : "fx:id=\"btnMetricas1\" was not injected: check your FXML file 'prueba.fxml'.";
            assert btnIncidentes != null : "fx:id=\"btnMetricas2\" was not injected: check your FXML file 'prueba.fxml'.";

        }

    }

}
