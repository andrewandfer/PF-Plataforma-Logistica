package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.dto.RepartidorDTO;
import PFPlataformaLogistica.model.Empresa;
import PFPlataformaLogistica.model.EstadoRepartidor;
import PFPlataformaLogistica.model.Repartidor;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CrearEditarRepartidorController implements Initializable {

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox<EstadoRepartidor> cbEstado;
    @FXML private TextField txtZonaCobertura;
    @FXML private TextField txtLocalidad;

    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    @FXML private Label lblTitulo;

    private Empresa empresa;
    private boolean modoEdicion = false;
    private String repartidorId;

    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        empresa = Empresa.getInstance();
        configurarCombobox();

        // Verificar si estamos en modo edición
        repartidorId = (String) SceneManager.getData("repartidorId");
        if (repartidorId != null) {
            modoEdicion = true;
            cargarDatosRepartidor();
            lblTitulo.setText("Editar Repartidor");
        } else {
            lblTitulo.setText("Crear Repartidor");
            generarIdAutomatico();
        }
    }

    private void configurarCombobox() {
        cbEstado.setItems(FXCollections.observableArrayList(
                EstadoRepartidor.ACTIVO,
                EstadoRepartidor.INACTIVO,
                EstadoRepartidor.EN_RUTA
        ));
        cbEstado.setValue(EstadoRepartidor.ACTIVO);
    }

    private void generarIdAutomatico() {
        String nuevoId = "REP-" + System.currentTimeMillis();
        txtId.setText(nuevoId);
    }

    private void cargarDatosRepartidor() {
        Repartidor repartidor = empresa.buscarRepartidor(repartidorId);
        if (repartidor != null) {
            txtId.setText(repartidor.getId());
            txtNombre.setText(repartidor.getNombre());
            txtEmail.setText(repartidor.getEmail());
            txtTelefono.setText(repartidor.getTelefono());
            cbEstado.setValue(repartidor.getEstadoDisponibilidad());
            txtZonaCobertura.setText(repartidor.getZonaCobertura());
            txtLocalidad.setText(repartidor.getLocalidad());

            // En modo edición, el ID no se puede cambiar
            txtId.setEditable(false);
            txtId.setStyle("-fx-background-color: #f0f0f0;");
        } else {
            mostrarAlerta("Error", "No se encontró el repartidor a editar", Alert.AlertType.ERROR);
            volver();
        }
    }

    @FXML
    private void guardarRepartidor() {
        if (!validarCampos()) {
            return;
        }

        try {
            if (modoEdicion) {
                // MODO EDICIÓN: Actualizar repartidor existente
                actualizarRepartidor();
            } else {
                // MODO CREACIÓN: Crear nuevo repartidor
                crearRepartidor();
            }

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar repartidor: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void crearRepartidor() {
        Repartidor nuevoRepartidor = new Repartidor.RepartidorBuilder()
                .id(txtId.getText().trim())
                .nombre(txtNombre.getText().trim())
                .email(txtEmail.getText().trim())
                .telefono(txtTelefono.getText().trim())
                .disponibilidad(cbEstado.getValue())
                .zonaCobertura(txtZonaCobertura.getText().trim())
                .localidad(txtLocalidad.getText().trim())
                .build();

        empresa.registrarRepartidor(nuevoRepartidor);
        mostrarAlerta("Éxito", "Repartidor creado correctamente\nID: " + nuevoRepartidor.getId(), Alert.AlertType.INFORMATION);
        volver();
    }

    private void actualizarRepartidor() {
        RepartidorDTO dto = new RepartidorDTO(
                txtTelefono.getText().trim(),
                cbEstado.getValue(),
                txtZonaCobertura.getText().trim(),
                txtLocalidad.getText().trim(),
                null, // enviosAsignados no se modifica aquí
                txtId.getText().trim()
        );

        empresa.actualizarRepartidor(dto);
        mostrarAlerta("Éxito", "Repartidor actualizado correctamente", Alert.AlertType.INFORMATION);
        volver();
    }

    private boolean validarCampos() {
        // Validar ID
        if (txtId.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "El ID es requerido", Alert.AlertType.WARNING);
            txtId.requestFocus();
            return false;
        }

        // Validar Nombre
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "El nombre es requerido", Alert.AlertType.WARNING);
            txtNombre.requestFocus();
            return false;
        }

        // Validar Email
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            mostrarAlerta("Validación", "El email es requerido", Alert.AlertType.WARNING);
            txtEmail.requestFocus();
            return false;
        }
        if (!email.contains("@")) {
            mostrarAlerta("Validación", "El email debe ser válido", Alert.AlertType.WARNING);
            txtEmail.requestFocus();
            return false;
        }

        // Validar Teléfono
        if (txtTelefono.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "El teléfono es requerido", Alert.AlertType.WARNING);
            txtTelefono.requestFocus();
            return false;
        }

        // Validar Zona de Cobertura
        if (txtZonaCobertura.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "La zona de cobertura es requerida", Alert.AlertType.WARNING);
            txtZonaCobertura.requestFocus();
            return false;
        }

        return true;
    }

    @FXML
    private void cancelar() {
        volver();
    }

    private void volver() {
        try {
            // Limpiar datos de sesión
            SceneManager.removeData("repartidorId");

            Stage stage = (Stage) btnCancelar.getScene().getWindow();
            SceneManager.cambiarEscena(stage, "view/GestionRepartidoresView.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo volver: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
