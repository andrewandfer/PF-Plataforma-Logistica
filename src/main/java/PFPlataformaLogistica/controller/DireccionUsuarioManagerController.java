package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.Utils.SesionManager;
import PFPlataformaLogistica.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DireccionUsuarioManagerController {

    @FXML private Button btnActualizar;
    @FXML private Button btnAyuda;
    @FXML private Button btnConsultar;
    @FXML private Button btnContacto;
    @FXML private Button btnCrear;
    @FXML private Button btnEliminar;
    @FXML private Button btnVolver;

    @FXML private TableColumn<Direccion, String> colAlias;
    @FXML private TableColumn<Direccion, String> colCalle;
    @FXML private TableColumn<Direccion, String> colCiudad;
    @FXML private TableColumn<Direccion, String> colIP;

    @FXML private TableView<Direccion> tablaDirecciones;

    private Usuario usuarioActual;
    private ObservableList<Direccion> direccionesData;

    // ------------------ INITIALIZE ------------------

    @FXML
    private void initialize() {

        Persona persona = SesionManager.getUsuarioActual(Usuario.class);

        if (persona instanceof Usuario) {
            usuarioActual = (Usuario) persona;
        } else {
            mostrarAlerta("Error", "No hay sesión de usuario activa", Alert.AlertType.ERROR);
            return;
        }

        configurarColumnas();
        cargarDirecciones();
    }

    // ------------------ CONFIGURAR TABLA ------------------

    private void configurarColumnas() {
        colIP.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getIdDireccion()));

        colAlias.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getAlias()));

        colCiudad.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCiudad()));

        colCalle.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCalle()));
    }

    // ------------------ CARGAR DATOS ------------------

    private void cargarDirecciones() {
        direccionesData = FXCollections.observableArrayList();


        if (usuarioActual.getListaDirecciones() != null) {
            direccionesData.addAll(usuarioActual.getListaDirecciones());
        }

        tablaDirecciones.setItems(direccionesData);
    }

    // ------------------ BOTONES ------------------

    @FXML
    void onActualizar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.cambiarEscena(stage, "ActualizarDireccionUsuario.fxml");

    }

    @FXML
    void onAyuda(ActionEvent event) {
        mostrarInfo("Ayuda",
                "Aquí puedes gestionar tus direcciones.");
    }

    @FXML
    void onConsultar(ActionEvent event) {
        Direccion d = tablaDirecciones.getSelectionModel().getSelectedItem();

        if (d != null) {
            mostrarInfo("Detalles de la dirección",
                    "ID: " + d.getIdDireccion() + "\n" +
                            "Calle: " + d.getCalle() + "\n" +
                            "Alias: " + d.getAlias() + "\n" +
                            "Ciudad: " + d.getCiudad());
        } else {
            mostrarAdvertencia("Selecciona una dirección para consultar.");
        }
    }

    @FXML
    void onContacto(ActionEvent event) {
        mostrarInfo("Contacto",
                "Escríbenos a soporte@enviorapido.com");
    }

    @FXML
    void onCrear(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.cambiarEscena(stage, "CrearDireccionUsuario.fxml");
    }

    @FXML
    void onEliminar(ActionEvent event) {
        Direccion d = tablaDirecciones.getSelectionModel().getSelectedItem();

        if (d != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar Dirección");
            alert.setHeaderText("Confirmar eliminación");
            alert.setContentText("¿Eliminar '" + d.getAlias() + "'?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                direccionesData.remove(d);
            }
        } else {
            mostrarAdvertencia("Selecciona una dirección para eliminar.");
        }
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "EnviosUsuario.fxml");
    }

    // ------------------ ALERTAS ------------------

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String titulo, String msg) {
        mostrarAlerta(titulo, msg, Alert.AlertType.INFORMATION);
    }

    private void mostrarAdvertencia(String msg) {
        mostrarAlerta("Advertencia", msg, Alert.AlertType.WARNING);
    }

    public void OnTermino(MouseEvent mouseEvent) {
    }

    public void volver(ActionEvent event) {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "UsuarioView.fxml");
    }
}

