package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.model.*;
import PFPlataformaLogistica.model.Usuario;
import PFPlataformaLogistica.App;
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

import javax.swing.*;
import java.io.IOException;

public class loginController {
    Empresa empresa;

    @FXML
    private Button btnAyuda;

    @FXML
    private Button btnContacto;

    @FXML
    private Button btnCrearCuenta;

    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private TextField txtCorreo;

    @FXML
    private void initialize() {
        empresa = Empresa.getInstance();
        Usuario usuario4= new Usuario.UsuarioBuilder()
                .telefono("3148676404")
                .nombre("carlos")
                .contrasena("carlitos")
                .email("calixto@gmail.com")
                .build();
        Empresa.getInstance().getListaPersonas().add(usuario4);
        System.out.println(empresa.getListaPersonas());
    }


        @FXML
    void onAyuda(ActionEvent event) {



    }

    @FXML
    void onContacto(ActionEvent event) {

    }

    @FXML
    void onCrearCuenta(ActionEvent event) {


    }

    @FXML
    void onIniciarSesion(ActionEvent event) throws IOException {
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();

        if (correo.isEmpty() && contrasena.isEmpty()) {
            mostrarAlerta("Advertencia", "Debe llenar todos los campos.");
            return;
        }

        if (correo.isEmpty()) {
            mostrarAlerta("Advertencia", "El campo correo está vacío.");
            return;
        }

        if (contrasena.isEmpty()) {
            mostrarAlerta("Advertencia", "El campo contraseña está vacío.");
            return;
        }
        for (Persona persona: empresa.getListaPersonas()) {
            if (persona.getEmail() != null && persona.getContrasena() != null) {
                if (persona.getEmail().equals(correo) && persona.getContrasena().equals(contrasena)) {
                    if (persona instanceof Administrador) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PFPlataformaLogistica/prueba.fxml"
                        ));
                        Parent root = loader.load();
                        // Obtener la ventana actual (Stage) y reemplazar la escena
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                        return;

                    }
                    if (persona instanceof Usuario) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PFPlataformaLogistica/prueba.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                        return;
                    }
                    if (persona instanceof Repartidor) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PFPlataformaLogistica/prueba.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                        return;
                    }
                }
            }
        }


    }


    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}



