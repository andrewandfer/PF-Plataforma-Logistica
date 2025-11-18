package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.Utils.SesionManager;
import PFPlataformaLogistica.model.Repartidor;
import PFPlataformaLogistica.model.EstadoRepartidor;
import PFPlataformaLogistica.model.Envio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;

import java.util.List;
import java.util.ArrayList;

public class GestionRepartidoresController {


    // Componentes del menú lateral
    @FXML private Label lblMiNombre;
    @FXML private Label lblMiEstado;
    @FXML private Label lblMiZona;
    @FXML private Label lblMiEnvios;

    @FXML private Button btnActualizarInfo;
    @FXML private Button btnCambiarEstado;
    @FXML private Button btnVerEnvios;
    @FXML private Button btnCerrarSesion;

    // Componentes de la vista de actualizar información
    @FXML private VBox vboxActualizarInfo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtZona;
    @FXML private TextField txtLocalidad;
    @FXML private Button btnGuardarInfo;
    @FXML private Button btnCancelarInfo;
    @FXML private Label lblMensajeInfo;

    // Componentes de la vista de estado
    @FXML private VBox vboxEstado;
    @FXML private Label lblEstadoActual;
    @FXML private ComboBox<EstadoRepartidor> cbNuevoEstado;
    @FXML private Button btnActualizarEstado;

    // Componentes de la vista de envíos
    @FXML private VBox vboxEnvios;
    @FXML private Label lblTotalEnvios;
    @FXML private TableView<Envio> tableEnvios;
    @FXML private TableColumn<Envio, String> colIdEnvio;
    @FXML private TableColumn<Envio, String> colDestino;
    @FXML private TableColumn<Envio, String> colEstadoEnvio;
    @FXML private TableColumn<Envio, String> colFechaEstimada;
    @FXML private TableColumn<Envio, String> colPeso;

    @FXML private Button btnActualizarLista;
    @FXML private TextField txtIdEnvioCompletar;
    @FXML private Button btnCompletarEnvio;
    @FXML private Label lblMensajeCompletar;

    @FXML private Label lblInfoSesion;

    // Repartidor actual (sesión)
    private Repartidor repartidorActual;
    private Repartidor repartidorOriginal;

    @FXML
    public void initialize() {
        repartidorActual= SesionManager.getUsuarioActual(Repartidor.class);
        cbNuevoEstado.getItems().setAll(EstadoRepartidor.values());
        configurarTablaEnvios();
        mostrarVistaActualizarInfo();
        cargarRepartidorActual();
    }

    private void cargarRepartidorActual() {
        this.repartidorActual = SesionManager.getUsuarioActual(Repartidor.class);
        actualizarInformacionPersonal();
        cargarDatosEnFormulario();
    }


    private void configurarTablaEnvios() {
        colIdEnvio.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getIdEnvio()));

        colDestino.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDestino()));

        colEstadoEnvio.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstadoEnvio().toString()));

        colFechaEstimada.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaEstimada()));

        colPeso.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPesoEnvio())));
    }

    private void actualizarInformacionPersonal() {
        if (repartidorActual != null) {
            lblMiNombre.setText("Nombre: " + repartidorActual.getNombre());
            lblMiEstado.setText("Estado: " + repartidorActual.getEstadoDisponibilidad());
            lblMiZona.setText("Zona: " + repartidorActual.getZonaCobertura());

            int cantidadEnvios = repartidorActual.getEnviosAsignados() != null ?
                    repartidorActual.getEnviosAsignados().size() : 0;
            lblMiEnvios.setText("Envíos asignados: " + cantidadEnvios);

            lblEstadoActual.setText(repartidorActual.getEstadoDisponibilidad().toString());
            lblInfoSesion.setText("Conectado como: " + repartidorActual.getNombre());
        }
    }

    private void cargarDatosEnFormulario() {
        if (repartidorActual != null) {
            txtNombre.setText(repartidorActual.getNombre());
            txtTelefono.setText(repartidorActual.getTelefono());
            txtEmail.setText(repartidorActual.getEmail());
            txtZona.setText(repartidorActual.getZonaCobertura());
            txtLocalidad.setText(repartidorActual.getLocalidad());
        }
    }

    // MÉTODOS DE NAVEGACIÓN
    @FXML
    private void abrirActualizarInfo() {
        mostrarVistaActualizarInfo();
        cargarDatosEnFormulario();
        guardarEstadoOriginal();
    }

    @FXML
    private void abrirCambiarEstado() {
        mostrarVistaEstado();
        cbNuevoEstado.setValue(repartidorActual.getEstadoDisponibilidad());
    }

    @FXML
    private void abrirVerEnvios(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.cambiarEscena(stage, "EnviosAsignados.fxml");
        actualizarTablaEnvios();
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.cambiarEscena(stage, "login.fxml");
    }

    private void mostrarVistaActualizarInfo() {
        vboxActualizarInfo.setVisible(true);
        vboxEstado.setVisible(false);
        vboxEnvios.setVisible(false);
        lblMensajeInfo.setText("");
    }

    private void mostrarVistaEstado() {
        vboxActualizarInfo.setVisible(false);
        vboxEstado.setVisible(true);
        vboxEnvios.setVisible(false);
    }


    // MÉTODOS DE ACTUALIZAR INFORMACIÓN
    @FXML
    private void guardarInformacion() {
        if (validarFormulario()) {
            try {
                repartidorActual.setNombre(txtNombre.getText().trim());
                repartidorActual.setTelefono(txtTelefono.getText().trim());
                repartidorActual.setEmail(txtEmail.getText().trim());
                repartidorActual.setZonaCobertura(txtZona.getText().trim());
                repartidorActual.setLocalidad(txtLocalidad.getText().trim());
                actualizarInformacionPersonal();

                lblMensajeInfo.setText(" Información actualizada correctamente");
                lblMensajeInfo.setStyle("-fx-text-fill: #4CAF50;");

                mostrarAlerta("Información actualizada",
                        "Tus datos personales han sido actualizados correctamente",
                        Alert.AlertType.INFORMATION);

            } catch (Exception e) {
                lblMensajeInfo.setText("Error al actualizar la información");
                lblMensajeInfo.setStyle("-fx-text-fill: #F44336;");
                System.err.println("Error actualizando información: " + e.getMessage());
            }
        }

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                javafx.application.Platform.runLater(() ->
                        lblMensajeInfo.setText(""));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @FXML
    private void cancelarActualizacion() {
        if (repartidorOriginal != null) {
            restaurarDatosOriginales();
        }
        cargarDatosEnFormulario();
        lblMensajeInfo.setText(" Cambios cancelados");
        lblMensajeInfo.setStyle("-fx-text-fill: #FF9800;");

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                javafx.application.Platform.runLater(() ->
                        lblMensajeInfo.setText(""));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void guardarEstadoOriginal() {
        this.repartidorOriginal = SesionManager.getUsuarioActual(Repartidor.class);
    }

    private void restaurarDatosOriginales() {
        if (repartidorOriginal != null) {
            repartidorActual.setNombre(repartidorOriginal.getNombre());
            repartidorActual.setTelefono(repartidorOriginal.getTelefono());
            repartidorActual.setEmail(repartidorOriginal.getEmail());
            repartidorActual.setZonaCobertura(repartidorOriginal.getZonaCobertura());
            repartidorActual.setLocalidad(repartidorOriginal.getLocalidad());
            actualizarInformacionPersonal();
        }
    }

    private boolean validarFormulario() {
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();
        String zona = txtZona.getText().trim();
        String localidad = txtLocalidad.getText().trim();

        if (nombre.isEmpty()) {
            mostrarAlerta("Error de validación", "El nombre es obligatorio", Alert.AlertType.WARNING);
            txtNombre.requestFocus();
            return false;
        }

        if (telefono.isEmpty()) {
            mostrarAlerta("Error de validación", "El teléfono es obligatorio", Alert.AlertType.WARNING);
            txtTelefono.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            mostrarAlerta("Error de validación", "El email es obligatorio", Alert.AlertType.WARNING);
            txtEmail.requestFocus();
            return false;
        }

        if (zona.isEmpty()) {
            mostrarAlerta("Error de validación", "La zona de cobertura es obligatoria", Alert.AlertType.WARNING);
            txtZona.requestFocus();
            return false;
        }

        if (localidad.isEmpty()) {
            mostrarAlerta("Error de validación", "La localidad es obligatoria", Alert.AlertType.WARNING);
            txtLocalidad.requestFocus();
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            mostrarAlerta("Error de validación", "Ingresa un email válido", Alert.AlertType.WARNING);
            txtEmail.requestFocus();
            return false;
        }

        return true;
    }

    // MÉTODOS DE GESTIÓN DE ESTADO
    @FXML
    private void actualizarEstado() {
        EstadoRepartidor nuevoEstado = cbNuevoEstado.getValue();

        if (nuevoEstado != null && repartidorActual != null) {
            repartidorActual.cambiarDisponibilidad(nuevoEstado);
            actualizarInformacionPersonal();

            mostrarAlerta("Estado actualizado",
                    "Tu estado ha sido cambiado a: " + nuevoEstado,
                    Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error",
                    "Selecciona un estado válido",
                    Alert.AlertType.WARNING);
        }
    }

    // MÉTODOS DE GESTIÓN DE ENVÍOS
    @FXML
    private void actualizarListaEnvios() {
        actualizarTablaEnvios();
    }

    private void actualizarTablaEnvios() {
        if (repartidorActual != null) {
            List<Envio> envios = repartidorActual.getEnviosAsignados() != null ?
                    new ArrayList<>(repartidorActual.getEnviosAsignados()) :
                    new ArrayList<>();

            tableEnvios.getItems().setAll(envios);
            lblTotalEnvios.setText("Total de envíos asignados: " + envios.size());
            actualizarInformacionPersonal();
        }
    }

    @FXML
    private void completarEnvio() {
        String idEnvio = txtIdEnvioCompletar.getText().trim();

        if (idEnvio.isEmpty()) {
            mostrarAlerta("Error", "Ingresa el ID del envío", Alert.AlertType.WARNING);
            return;
        }

        if (repartidorActual != null && repartidorActual.getEnviosAsignados() != null) {
            Envio envioEncontrado = null;
            for (Envio envio : repartidorActual.getEnviosAsignados()) {
                if (envio.getIdEnvio().equals(idEnvio)) {
                    envioEncontrado = envio;
                    break;
                }
            }

            if (envioEncontrado != null) {
                repartidorActual.getEnviosAsignados().remove(envioEncontrado);

                if (repartidorActual.getEnviosAsignados().isEmpty()) {
                    repartidorActual.cambiarDisponibilidad(EstadoRepartidor.ACTIVO);
                }

                lblMensajeCompletar.setText(" Envío " + idEnvio + " marcado como entregado");
                lblMensajeCompletar.setStyle("-fx-text-fill: #4CAF50;");
                txtIdEnvioCompletar.clear();
                actualizarTablaEnvios();
                actualizarInformacionPersonal();

                mostrarAlerta("Envío completado",
                        "El envío " + idEnvio + " ha sido marcado como entregado",
                        Alert.AlertType.INFORMATION);

            } else {
                lblMensajeCompletar.setText(" No tienes asignado este envío");
                lblMensajeCompletar.setStyle("-fx-text-fill: #F44336;");
            }
        }

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                javafx.application.Platform.runLater(() ->
                        lblMensajeCompletar.setText(""));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    // MÉTODOS AUXILIARES
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void setRepartidorActual(Repartidor repartidor) {
        this.repartidorActual = repartidor;
        actualizarInformacionPersonal();
        cargarDatosEnFormulario();
        if (vboxEnvios.isVisible()) {
            actualizarTablaEnvios();
        }
    }

    public Repartidor getRepartidorActual() {
        return repartidorActual;
    }
} // <-- Esta es la llave de cierre que faltaba