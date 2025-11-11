package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.Utils.SesionManager;
import PFPlataformaLogistica.model.Persona;
import PFPlataformaLogistica.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PerfilController {

    private Persona usuarioActual;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnActualizar;


    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;

    @FXML
    private void initialize() {
        usuarioActual = SesionManager.getPersonaActual();

        if (usuarioActual != null) {
            txtNombre.setText(usuarioActual.getNombre());
            txtEmail.setText(usuarioActual.getEmail());

            if (usuarioActual.getEdad() > 0) {
                txtEdad.setText(String.valueOf(usuarioActual.getEdad()));
            }
        } else {
            System.out.println("⚠ No hay sesión activa");
        }
    }

    @FXML
    void OnActualizarUsuario(ActionEvent event) {
        if (usuarioActual != null) {
            if (!txtNombre.getText().isEmpty()) usuarioActual.setNombre(txtNombre.getText());
            if (!txtEmail.getText().isEmpty()) usuarioActual.setEmail(txtEmail.getText());
            if (usuarioActual instanceof Usuario) {
                if (!txtTelefono.getText().isEmpty())((Usuario) usuarioActual).setTelefono(txtTelefono.getText());
            }
            try {
                int edad = Integer.parseInt(txtEdad.getText());
                usuarioActual.setEdad(edad);
            } catch (NumberFormatException e) {
                System.out.println("⚠ Edad no válida o vacía, no se actualizó");
            }

            System.out.println("✅ Datos actualizados para " + usuarioActual.getNombre());
        } else {
            System.out.println("⚠ No hay usuario logueado");
        }
    }

    @FXML

    void OnBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.cambiarEscena(stage, "UsuarioView.fxml");
    }

}
