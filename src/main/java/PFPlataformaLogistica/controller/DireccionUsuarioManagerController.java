package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.model.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;

import javafx.stage.Stage;

import java.util.ResourceBundle;

public class DireccionUsuarioManagerController {


    public void initialize(URL url, ResourceBundle resourceBundle) {
        colIP.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue().getDireccion().getIdDireccion())));
        colAlias.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDireccion().getAlias()));
        colCiudad.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getDireccion().getCiudad()));
        colCalle.setCellValueFactory((cellData -> new SimpleStringProperty((cellData.getValue().getDireccion().getCalle()))));
    }

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnAyuda;

    @FXML
    private Button btnConsultar;

    @FXML
    private Button btnContacto;

    @FXML
    private Button btnCrear;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnVolver;

    @FXML
    private TableColumn<Usuario, String> colAlias;

    @FXML

    private TableColumn<Usuario, String> colCalle;

    @FXML
    private TableColumn<Usuario, String> colCiudad;

    @FXML
    private TableColumn<Usuario, String> colIP;

    @FXML
    private TableView<Usuario> tablaDirecciones;

    @FXML
    void onActualizar(ActionEvent event) {
        Usuario usuarioSeleccionado = tablaDirecciones.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Actualizar Dirección");
            alert.setHeaderText(null);
            alert.setContentText("Actualizando la dirección: " + usuarioSeleccionado.getDireccion().getAlias());
            alert.showAndWait();
            // Aquí iría la lógica para abrir un formulario con los datos actuales y permitir la edición
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Actualizar Dirección");
            alert.setHeaderText("Ninguna selección");
            alert.setContentText("Por favor, selecciona una dirección de la tabla para actualizar.");
            alert.showAndWait();
        }
    }

    @FXML
    void onAyuda(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ayuda");
        alert.setHeaderText("¿Necesitas ayuda?");
        alert.setContentText("Aquí puedes encontrar información sobre cómo gestionar tus direcciones.");
        alert.showAndWait();
    }

    @FXML
    void onConsultar(ActionEvent event) {
        Usuario usuarioSeleccionado = tablaDirecciones.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Consultar Dirección");
            alert.setHeaderText("Detalles de la dirección:");
            alert.setContentText(
                    "ID: " + usuarioSeleccionado.getDireccion().getIdDireccion() + "\n" +
                            "IP: " + usuarioSeleccionado.getDireccion().getIdDireccion() + "\n" + // Asumiendo que idDireccion es la IP
                            "Calle: " + usuarioSeleccionado.getDireccion().getCalle() + "\n" +
                            "Alias: " + usuarioSeleccionado.getDireccion().getAlias() + "\n" +
                            "Ciudad: " + usuarioSeleccionado.getDireccion().getCiudad()
            );
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Consultar Dirección");
            alert.setHeaderText("Ninguna selección");
            alert.setContentText("Por favor, selecciona una dirección de la tabla para consultar.");
            alert.showAndWait();
        }
    }

    @FXML
    void onContacto(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Contacto");
        alert.setHeaderText("¿Tienes alguna pregunta?");
        alert.setContentText("Puedes escribirnos a soporte@enviorapido.com");
        alert.showAndWait();
    }

    @FXML
    void onCrear(ActionEvent event) {
        // Aquí iría la lógica para crear una nueva dirección
        // Por ejemplo, abrir un diálogo para ingresar datos
        // Supongamos que tienes un usuario actual (por ejemplo, de la sesión)
        // Usuario usuarioActual = ...; // Obtener de la sesión o parámetro

        // Ejemplo de creación con datos de prueba
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Crear Dirección");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidad de Crear aún no implementada.");
        alert.showAndWait();
    }

    @FXML
    void onEliminar(ActionEvent event) {
        Usuario usuarioSeleccionado = tablaDirecciones.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar Dirección");
            alert.setHeaderText("Confirmar eliminación");
            alert.setContentText("¿Estás seguro de que deseas eliminar la dirección '" + usuarioSeleccionado.getDireccion().getAlias() + "'?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                // Aquí iría la lógica para eliminar la dirección de la base de datos o del modelo
                // Por ejemplo, una llamada a un servicio: direccionService.eliminar(usuarioSeleccionado.getDireccion().getIdDireccion());
                // Y luego actualizar la tabla (si no lo hace automáticamente)
                tablaDirecciones.getItems().remove(usuarioSeleccionado);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eliminar Dirección");
            alert.setHeaderText("Ninguna selección");
            alert.setContentText("Por favor, selecciona una dirección de la tabla para eliminar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "EnviosUsuarioView.fxml");
    }

}
