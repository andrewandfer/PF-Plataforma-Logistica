package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.model.Empresa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.swing.*;

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
    void onIniciarSesion(ActionEvent event) {
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


    }


    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}



