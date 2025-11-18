package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.Utils.SesionManager;
import PFPlataformaLogistica.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class loginController {
    Empresa empresa;

    @FXML
    private Button btnAyuda, btnContacto, btnCrearCuenta, btnLogin;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private TextField txtCorreo;

    @FXML
    private void initialize() {
        empresa = Empresa.getInstance();

        Usuario usuario4 = new Usuario.UsuarioBuilder()
                .telefono("3148676404")
                .nombre("carlos")
                .contrasena("carlitos")
                .email("calixto@gmail.com")
                .build();

        if (empresa.getListaPersonas().stream().noneMatch(p -> p.getEmail().equals("calixto@gmail.com"))) {
            empresa.getListaPersonas().add(usuario4);
        }
    }

    @FXML
    void onCrearCuenta(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.cambiarEscena(stage, "registro.fxml");
    }

    @FXML
    void onIniciarSesion(ActionEvent event) throws IOException {
        System.out.println(Empresa.getInstance().getListaPersonas());
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta("Advertencia", "Debe llenar todos los campos.");
            return;
        }

        for (Persona persona : empresa.getListaPersonas()) {
            if (persona.getEmail() != null && persona.getContrasena() != null &&
                    persona.getEmail().equals(correo) && persona.getContrasena().equals(contrasena)) {

                SesionManager.iniciarSesion(persona);

                String vistaDestino = null;

                if (persona instanceof Administrador) {
                    vistaDestino = "/PFPlataformaLogistica/AdministradorView.fxml";
                } else if (persona instanceof Usuario) {
                    vistaDestino = "/PFPlataformaLogistica/UsuarioView.fxml";
                } else if (persona instanceof Repartidor) {
                    vistaDestino = "/PFPlataformaLogistica/GestionRepartidores.fxml";
                }

                if (vistaDestino != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(vistaDestino));
                    Parent root = loader.load();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                }
                return;
            }
        }

        mostrarAlerta("Error", "Correo o contraseña incorrectos");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void onAyuda(ActionEvent event) {
    }

    public void onContacto(ActionEvent event) {

    }
}
