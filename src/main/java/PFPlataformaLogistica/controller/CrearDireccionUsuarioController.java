package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.Utils.SesionManager;
import PFPlataformaLogistica.model.Direccion;
import PFPlataformaLogistica.model.Empresa;
import PFPlataformaLogistica.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.LinkedList;

public class CrearDireccionUsuarioController {
    private Usuario usuarioActual;

    @FXML
    private TextField txtIdDireccion;
    @FXML
    private TextField txtAlias;
    @FXML
    private TextField txtCalle;
    @FXML
    private TextField txtCiudad;
    @FXML
    private TextField txtLatitud;
    @FXML
    private TextField txtLongitud;
    @FXML
    private Button btnCrear;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnVolverAtras;
    @FXML
    private VBox vboxMensaje;
    @FXML
    private Label lblMensaje;

    private Empresa empresa;

    @FXML
    public void initialize() {
        empresa = Empresa.getInstance();
        vboxMensaje.setVisible(false);
        usuarioActual= SesionManager.getUsuarioActual(Usuario.class);
        configurarCampos();

        // Enfocar el primer campo
        txtIdDireccion.requestFocus();
    }

    private void configurarCampos() {
        txtLatitud.setText("0.0");
        txtLongitud.setText("0.0");
        txtLatitud.setPromptText("0.0 (opcional)");
        txtLongitud.setPromptText("0.0 (opcional)");
        txtAlias.setPromptText("Ej: Casa, Trabajo...");
    }

    @FXML
    private void crearDireccion() {
        try {
            // Validar campos obligatorios
            if (!validarCamposObligatorios()) {
                return;
            }

            String idDireccion = txtIdDireccion.getText().trim();

            // Verificar si el ID ya existe
            if (empresa.existeDireccion(idDireccion)) {
                mostrarError("ID duplicado", "Ya existe una dirección con este ID: " + idDireccion);
                txtIdDireccion.requestFocus();
                txtIdDireccion.selectAll();
                return;
            }

            // Crear objeto Direccion usando el Builder
            Direccion nuevaDireccion = new Direccion.DireccionBuilder()
                    .idDireccion(idDireccion)
                    .alias(txtAlias.getText().trim())
                    .calle(txtCalle.getText().trim())
                    .ciudad(txtCiudad.getText().trim())
                    .latitud(obtenerDouble(txtLatitud.getText()))
                    .longitud(obtenerDouble(txtLongitud.getText()))
                    .build();
            usuarioActual.getListaDirecciones().add(nuevaDireccion);


            // Validar coordenadas
            if (!nuevaDireccion.coordenadasValidas()) {
                mostrarError("Coordenadas inválidas",
                        "Las coordenadas deben estar dentro de rangos válidos:\n" +
                                "Latitud: -90 a 90\nLongitud: -180 a 180");
                return;
            }

            // Llamar al método de la empresa para crear la dirección
            boolean creado = empresa.crearDireccion(nuevaDireccion);

            if (creado) {
                // Mostrar mensaje de éxito
                mostrarMensajeExito("✅ Dirección creada exitosamente!\n\n" +
                        "ID: " + nuevaDireccion.getIdDireccion() + "\n" +
                        "Dirección: " + nuevaDireccion.toFormattedString() + "\n" +
                        "Coordenadas: (" + String.format("%.6f", nuevaDireccion.getLatitud()) + ", " +
                        String.format("%.6f", nuevaDireccion.getLongitud()) + ")");
                limpiarFormulario();
            } else {
                mostrarError("Error", "No se pudo crear la dirección. Intente nuevamente.");
            }

        } catch (IllegalStateException e) {
            mostrarError("Error de validación", e.getMessage());
        } catch (Exception e) {
            mostrarError("Error inesperado", "Ocurrió un error al crear la dirección:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void volverAtras(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneManager.cambiarEscena(stage, "DireccionUsuarioManager.fxml");
        } catch (Exception e) {
            mostrarError("Error de navegación", "No se pudo volver atrás: " + e.getMessage());
        }
    }

    @FXML
    private void limpiarFormulario() {
        txtIdDireccion.clear();
        txtAlias.clear();
        txtCalle.clear();
        txtCiudad.clear();
        txtLatitud.setText("0.0");
        txtLongitud.setText("0.0");
        vboxMensaje.setVisible(false);
        txtIdDireccion.requestFocus();
    }

    @FXML
    private void generarCoordenadasAleatorias() {
        double latitud = -34.6037 + (Math.random() * 0.1 - 0.05);
        double longitud = -58.3816 + (Math.random() * 0.1 - 0.05);

        txtLatitud.setText(String.format("%.6f", latitud));
        txtLongitud.setText(String.format("%.6f", longitud));

        mostrarMensajeInfo("Coordenadas generadas", "Se han generado coordenadas aleatorias cerca de Buenos Aires");
    }

    private boolean validarCamposObligatorios() {
        // Validar ID
        if (txtIdDireccion.getText().trim().isEmpty()) {
            mostrarError("Campo obligatorio", "El ID de dirección es obligatorio");
            txtIdDireccion.requestFocus();
            return false;
        }

        // Validar formato ID (puedes personalizar esta validación)
        String id = txtIdDireccion.getText().trim();
        if (!id.matches("^[A-Za-z0-9_-]+$")) {
            mostrarError("Formato inválido",
                    "El ID debe contener solo letras, números, guiones y guiones bajos");
            txtIdDireccion.requestFocus();
            txtIdDireccion.selectAll();
            return false;
        }

        // Validar calle
        if (txtCalle.getText().trim().isEmpty()) {
            mostrarError("Campo obligatorio", "La calle es obligatoria");
            txtCalle.requestFocus();
            return false;
        }

        // Validar ciudad
        if (txtCiudad.getText().trim().isEmpty()) {
            mostrarError("Campo obligatorio", "La ciudad es obligatoria");
            txtCiudad.requestFocus();
            return false;
        }

        // Validar formato de latitud y longitud
        if (!esDoubleValido(txtLatitud.getText())) {
            mostrarError("Formato inválido", "La latitud debe ser un número válido");
            txtLatitud.requestFocus();
            txtLatitud.selectAll();
            return false;
        }

        if (!esDoubleValido(txtLongitud.getText())) {
            mostrarError("Formato inválido", "La longitud debe ser un número válido");
            txtLongitud.requestFocus();
            txtLongitud.selectAll();
            return false;
        }

        return true;
    }

    private boolean esDoubleValido(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return true; // Permitir vacío, se usará 0.0
        }
        try {
            Double.parseDouble(texto.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private double obtenerDouble(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(texto.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void mostrarMensajeExito(String mensaje) {
        lblMensaje.setText(mensaje);
        vboxMensaje.setVisible(true);
        vboxMensaje.setStyle("-fx-background-color: #d4edda; -fx-border-color: #c3e6cb; -fx-border-radius: 5; -fx-padding: 10;");
        lblMensaje.setStyle("-fx-text-fill: #155724;");
    }

    private void mostrarMensajeInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void verMisDirecciones(ActionEvent event) {
    }

    public void consultarDirecciones(ActionEvent event) {
    }

    public void volverAlMenu(ActionEvent event) {
    }


}