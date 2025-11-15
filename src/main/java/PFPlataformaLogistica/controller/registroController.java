package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.model.Empresa;
import PFPlataformaLogistica.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class registroController {
    Empresa empresa;

    @FXML
    private Button btnCrearCuenta;

    @FXML
    private Label btnTerminoYCondiciones;

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
    private TextField txtEmail;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtNombre;

    @FXML
    private void initialize() {
        empresa = Empresa.getInstance();
    }

    @FXML
    void OnCrearCuenta(ActionEvent event) {
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();
        String contrasena = txtContrasena.getText();
        String edad = txtEdad.getText();

        // Validación básica antes de crear el usuario
        if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty() || edad.isEmpty()) {
            validarCampos(null); // Llama la validación visual
            System.out.println("Por favor, completa todos los campos.");
            return;
        }

        Usuario usuarioNuevo = new Usuario.UsuarioBuilder()
                .nombre(nombre)
                .email(email)
                .contrasena(contrasena)
                .edad(Integer.parseInt(edad))
                .build();
        Empresa.getInstance().getListaPersonas().add(usuarioNuevo);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PFPlataformaLogistica/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void validarCampos(KeyEvent event) {
        validarCampo(txtNombre, errorNombre);
        validarCampo(txtEmail, errorCorreo);
        validarCampo(txtContrasena, errorContrasena);
        validarCampo(txtEdad, errorEdad);
    }

    private void validarCampo(TextField campo, Label error) {
        if (campo.getText().trim().isEmpty()) {
            campo.setStyle("-fx-border-color: red; -fx-background-color: #F5F5F5; -fx-background-radius: 8;");
            error.setText("Campo obligatorio");
        } else {
            campo.setStyle("-fx-border-color: #EBEBEB; -fx-background-color: #F5F5F5; -fx-background-radius: 8;");
            error.setText("");
        }
    }

}


