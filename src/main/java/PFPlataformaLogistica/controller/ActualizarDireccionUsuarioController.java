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
import java.util.List;

public class ActualizarDireccionUsuarioController {

    @FXML private TextField txtBuscarId;
    @FXML private TextField txtIdDireccion;
    @FXML private TextField txtAlias;
    @FXML private TextField txtCalle;
    @FXML private TextField txtCiudad;
    @FXML private TextField txtLatitud;
    @FXML private TextField txtLongitud;
    @FXML private Button btnBuscar;
    @FXML private Button btnActualizar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnGenerarCoordenadas;
    @FXML private Button btnVolverAtras;
    @FXML private VBox vboxMensaje;
    @FXML private Label lblMensaje;

    private Empresa empresa;
    private Usuario usuarioActual;
    private Direccion direccionActual;

    @FXML
    public void initialize() {
        empresa = Empresa.getInstance();
        // Obtener el usuario actual (debes adaptar esto según tu implementación)
        usuarioActual = SesionManager.getUsuarioActual(Usuario.class);
        vboxMensaje.setVisible(false);
        configurarCampos();
        habilitarFormulario(false);
    }



    private void configurarCampos() {
        txtLatitud.setText("0.0");
        txtLongitud.setText("0.0");
        txtLatitud.setPromptText("0.0 (opcional)");
        txtLongitud.setPromptText("0.0 (opcional)");
        txtAlias.setPromptText("Ej: Casa, Trabajo...");
    }

    private void habilitarFormulario(boolean habilitar) {
        txtAlias.setDisable(!habilitar);
        txtCalle.setDisable(!habilitar);
        txtCiudad.setDisable(!habilitar);
        txtLatitud.setDisable(!habilitar);
        txtLongitud.setDisable(!habilitar);
        btnActualizar.setDisable(!habilitar);
        btnLimpiar.setDisable(!habilitar);
        btnGenerarCoordenadas.setDisable(!habilitar);
    }

    @FXML
    private void buscarDireccion() {
        try {
            if (usuarioActual == null) {
                mostrarError("Usuario no disponible", "No se pudo identificar el usuario actual");
                return;
            }

            String idBuscar = txtBuscarId.getText().trim();

            if (idBuscar.isEmpty()) {
                mostrarError("Campo requerido", "Por favor, ingrese un ID de dirección para buscar");
                txtBuscarId.requestFocus();
                return;
            }

            // Buscar en las direcciones del usuario actual
            direccionActual = buscarDireccionEnUsuario(idBuscar, usuarioActual);

            if (direccionActual == null) {
                mostrarError("Dirección no encontrada",
                        "No se encontró ninguna dirección con el ID: " + idBuscar +
                                "\n\nLa dirección debe pertenecer a su cuenta.");
                limpiarFormulario();
                habilitarFormulario(false);
                return;
            }

            cargarDatosDireccion(direccionActual);
            habilitarFormulario(true);
            mostrarMensajeExito("Dirección encontrada en su cuenta\nPuede modificar los campos necesarios");

        } catch (Exception e) {
            mostrarError("Error en búsqueda", "Ocurrió un error al buscar la dirección: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Busca una dirección por ID en la lista de direcciones del usuario
     */
    private Direccion buscarDireccionEnUsuario(String idDireccion, Usuario usuario) {
        if (usuario == null || idDireccion == null || idDireccion.trim().isEmpty()) {
            return null;
        }

        // Buscar en las direcciones del usuario
        List<Direccion> direccionesUsuario = usuario.getListaDirecciones();
        if (direccionesUsuario != null) {
            for (Direccion direccion : direccionesUsuario) {
                if (direccion.getIdDireccion().equalsIgnoreCase(idDireccion)) {
                    return direccion;
                }
            }
        }

        return null;
    }

    @FXML
    private void actualizarDireccion() {
        try {
            if (usuarioActual == null) {
                mostrarError("Usuario no disponible", "No se pudo identificar el usuario actual");
                return;
            }

            if (!validarCamposObligatorios()) {
                return;
            }

            if (direccionActual == null) {
                mostrarError("Error", "No hay una dirección cargada para actualizar");
                return;
            }

            // Crear objeto Direccion con los datos actualizados
            Direccion direccionActualizada = new Direccion.DireccionBuilder()
                    .idDireccion(txtIdDireccion.getText().trim())
                    .alias(txtAlias.getText().trim())
                    .calle(txtCalle.getText().trim())
                    .ciudad(txtCiudad.getText().trim())
                    .latitud(obtenerDouble(txtLatitud.getText()))
                    .longitud(obtenerDouble(txtLongitud.getText()))
                    .build();

            // Validar coordenadas
            if (!direccionActualizada.coordenadasValidas()) {
                mostrarError("Coordenadas inválidas",
                        "Las coordenadas deben estar dentro de rangos válidos:\nLatitud: -90 a 90\nLongitud: -180 a 180");
                return;
            }

            // Actualizar la dirección en el usuario
            boolean actualizado = actualizarDireccionEnUsuario(direccionActualizada, usuarioActual);

            if (actualizado) {
                direccionActual = direccionActualizada;
                mostrarMensajeExito("Dirección actualizada exitosamente!\nID: " + direccionActualizada.getIdDireccion());
            } else {
                mostrarError("Error", "No se pudo actualizar la dirección en su cuenta");
            }

        } catch (IllegalStateException e) {
            mostrarError("Error de validación", e.getMessage());
        } catch (Exception e) {
            mostrarError("Error inesperado", "Ocurrió un error al actualizar la dirección:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Actualiza una dirección en la lista del usuario
     */
    private boolean actualizarDireccionEnUsuario(Direccion direccionActualizada, Usuario usuario) {
        try {
            if (usuario == null || direccionActualizada == null) {
                return false;
            }

            List<Direccion> direccionesUsuario = usuario.getListaDirecciones();
            if (direccionesUsuario == null) {
                return false;
            }

            // Buscar y actualizar la dirección en la lista del usuario
            for (int i = 0; i < direccionesUsuario.size(); i++) {
                Direccion direccion = direccionesUsuario.get(i);
                if (direccion.getIdDireccion().equalsIgnoreCase(direccionActualizada.getIdDireccion())) {
                    // Actualizar todos los campos
                    direccion.setAlias(direccionActualizada.getAlias());
                    direccion.setCalle(direccionActualizada.getCalle());
                    direccion.setCiudad(direccionActualizada.getCiudad());
                    direccion.setLatitud(direccionActualizada.getLatitud());
                    direccion.setLongitud(direccionActualizada.getLongitud());

                    System.out.println("Dirección actualizada en usuario: " + direccionActualizada.getIdDireccion());
                    return true;
                }
            }

            System.out.println("Dirección no encontrada en usuario: " + direccionActualizada.getIdDireccion());
            return false;

        } catch (Exception e) {
            System.err.println(" Error al actualizar dirección en usuario: " + e.getMessage());
            return false;
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
    private void volverAlMenu() {
        // Implementar navegación al menú principal
        System.out.println("Volviendo al menú principal...");
    }

    @FXML
    private void limpiarFormulario() {
        if (direccionActual != null) {
            cargarDatosDireccion(direccionActual);
        } else {
            txtIdDireccion.clear();
            txtAlias.clear();
            txtCalle.clear();
            txtCiudad.clear();
            txtLatitud.setText("0.0");
            txtLongitud.setText("0.0");
        }
        vboxMensaje.setVisible(false);
    }

    @FXML
    private void generarCoordenadasAleatorias() {
        double latitud = -34.6037 + (Math.random() * 0.1 - 0.05);
        double longitud = -58.3816 + (Math.random() * 0.1 - 0.05);

        txtLatitud.setText(String.format("%.6f", latitud));
        txtLongitud.setText(String.format("%.6f", longitud));
    }

    private void cargarDatosDireccion(Direccion direccion) {
        txtIdDireccion.setText(direccion.getIdDireccion());
        txtAlias.setText(direccion.getAlias() != null ? direccion.getAlias() : "");
        txtCalle.setText(direccion.getCalle());
        txtCiudad.setText(direccion.getCiudad());
        txtLatitud.setText(String.format("%.6f", direccion.getLatitud()));
        txtLongitud.setText(String.format("%.6f", direccion.getLongitud()));
    }

    private boolean validarCamposObligatorios() {
        if (txtCalle.getText().trim().isEmpty()) {
            mostrarError("Campo obligatorio", "La calle es obligatoria");
            txtCalle.requestFocus();
            return false;
        }

        if (txtCiudad.getText().trim().isEmpty()) {
            mostrarError("Campo obligatorio", "La ciudad es obligatoria");
            txtCiudad.requestFocus();
            return false;
        }

        if (!esDoubleValido(txtLatitud.getText())) {
            mostrarError("Formato inválido", "La latitud debe ser un número válido");
            txtLatitud.requestFocus();
            return false;
        }

        if (!esDoubleValido(txtLongitud.getText())) {
            mostrarError("Formato inválido", "La longitud debe ser un número válido");
            txtLongitud.requestFocus();
            return false;
        }

        return true;
    }

    private boolean esDoubleValido(String texto) {
        if (texto == null || texto.trim().isEmpty()) return true;
        try {
            Double.parseDouble(texto.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private double obtenerDouble(String texto) {
        if (texto == null || texto.trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(texto.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void mostrarMensajeExito(String mensaje) {
        lblMensaje.setText(mensaje);
        vboxMensaje.setVisible(true);
        vboxMensaje.setStyle("-fx-background-color: #E8F5E9; -fx-border-color: #C8E6C9;");
        lblMensaje.setStyle("-fx-text-fill: #2E7D32;");
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}