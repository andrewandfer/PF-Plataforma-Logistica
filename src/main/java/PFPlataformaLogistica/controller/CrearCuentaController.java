package PFPlataformaLogistica.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class CrearCuentaController {

    @FXML
    private Button btnCrearCuenta;

    @FXML
    private Label errorContrasena;

    @FXML
    private Label errorCorreo;

    @FXML
    private Label errorEdad;

    @FXML
    private Label errorNombre;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtNombre;

    @FXML
    void OncrearCuenta(ActionEvent event) {

    }

    @FXML
    void validarCampos(KeyEvent event) {

    }

}

