package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.Utils.SesionManager;
import PFPlataformaLogistica.dto.UsuarioDTO;
import PFPlataformaLogistica.model.Empresa;
import PFPlataformaLogistica.model.Usuario;
import PFPlataformaLogistica.model.LoggerObserver;
import PFPlataformaLogistica.model.UIObserver;
import PFPlataformaLogistica.model.UsuarioObserver;
import PFPlataformaLogistica.model.UsuarioSubject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PerfilController implements UsuarioObserver {

    @FXML private TextField txtNombre;
    @FXML private TextField txtEdad;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private PasswordField txtContrasenaActual;
    @FXML private PasswordField txtNuevaContrasena;

    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Button btnVolverAtras;

    @FXML private VBox vboxMensaje;
    @FXML private Label lblMensaje;
    @FXML private Label lblUsuarioActual;
    @FXML private Label lblEmailActual;
    @FXML private Label lblTotalEnvios;

    private Empresa empresa;
    private UsuarioSubject usuarioSubject;
    private Usuario usuarioActual;

    @FXML
    public void initialize() {
        try {
            System.out.println("PerfilController inicializado - DEBUG");

            empresa = Empresa.getInstance();
            usuarioSubject = UsuarioSubject.getInstance();

            // Registrar observers
            usuarioSubject.addObserver(this);
            usuarioSubject.addObserver(new LoggerObserver());
            usuarioSubject.addObserver(new UIObserver());

            // Cargar datos del usuario actual
            cargarUsuarioActual();

            if (usuarioActual != null) {
                cargarDatosUsuario();
                System.out.println("Usuario cargado: " + usuarioActual.getNombre());
            } else {
                System.err.println("❌ Usuario actual es NULL");
                mostrarError("Error", "No se pudo cargar la información del usuario");
            }

            txtEmail.setDisable(true);

            vboxMensaje.setVisible(false);

        } catch (Exception e) {
            System.err.println("❌ Error en initialize: " + e.getMessage());
            e.printStackTrace();
            mostrarError("Error de inicialización", "No se pudieron cargar los datos: " + e.getMessage());
        }
    }

    private void cargarUsuarioActual() {
        usuarioActual = SesionManager.getUsuarioActual(Usuario.class);
        System.out.println("🔍 Usuario obtenido de SesionManager: " +
                (usuarioActual != null ? usuarioActual.getNombre() : "NULL"));
    }

    private void cargarDatosUsuario() {
        if (usuarioActual != null) {
            txtNombre.setText(usuarioActual.getNombre() != null ? usuarioActual.getNombre() : "");
            txtEmail.setText(usuarioActual.getEmail() != null ? usuarioActual.getEmail() : "");
            txtTelefono.setText(usuarioActual.getTelefono() != null ? usuarioActual.getTelefono() : "");

            if (usuarioActual.getEdad() > 0) {
                txtEdad.setText(String.valueOf(usuarioActual.getEdad()));
            }

            // Actualizar labels informativos
            if (lblUsuarioActual != null) {
                lblUsuarioActual.setText("Usuario: " + (usuarioActual.getNombre() != null ? usuarioActual.getNombre() : "N/A"));
            }
            if (lblEmailActual != null) {
                lblEmailActual.setText("Email: " + (usuarioActual.getEmail() != null ? usuarioActual.getEmail() : "N/A"));
            }
            if (lblTotalEnvios != null) {
                int totalEnvios = usuarioActual.getEnviosPropios() != null ?
                        usuarioActual.getEnviosPropios().size() : 0;
                lblTotalEnvios.setText("Total Envíos: " + totalEnvios);
            }
        }
    }

    @FXML
    private void guardarCambios() {
        try {
            System.out.println("\n=== 🔍 DEBUG INICIO GUARDAR CAMBIOS ===");

            // 1. DEBUG del usuario actual
            System.out.println("1. 📋 USUARIO ACTUAL:");
            System.out.println("   - ¿Usuario actual es null? " + (usuarioActual == null));
            if (usuarioActual != null) {
                System.out.println("   - Nombre: " + usuarioActual.getNombre());
                System.out.println("   - Email: " + usuarioActual.getEmail());
                System.out.println("   - Teléfono: " + usuarioActual.getTelefono());
                System.out.println("   - HashCode: " + usuarioActual.hashCode());
            }

            // 2. DEBUG de los campos del formulario
            System.out.println("2. 📝 CAMPOS DEL FORMULARIO:");
            System.out.println("   - Nombre: " + txtNombre.getText());
            System.out.println("   - Email: " + txtEmail.getText());
            System.out.println("   - Teléfono: " + txtTelefono.getText());
            System.out.println("   - Edad: " + txtEdad.getText());

            // 3. Validar campos
            if (!validarCampos()) {
                System.out.println("❌ VALIDACIÓN FALLÓ");
                return;
            }
            System.out.println("✅ VALIDACIÓN EXITOSA");

            // 4. Verificar contraseña
            if (!verificarContrasenaActual()) {
                System.out.println("❌ CONTRASEÑA INCORRECTA");
                return;
            }
            System.out.println("✅ CONTRASEÑA CORRECTA");

            // 5. DEBUG antes de actualizar
            String emailOriginal = usuarioActual.getEmail();
            System.out.println("3. 🔄 ANTES DE ACTUALIZAR:");
            System.out.println("   - Email original: " + emailOriginal);
            System.out.println("   - Email nuevo: " + txtEmail.getText().trim());

            // 6. Crear DTO y mostrar su contenido
            UsuarioDTO usuarioDTO = crearUsuarioDTO();
            System.out.println("4. 📦 DTO CREADO:");
            System.out.println("   - DTO Nombre: " + usuarioDTO.getNombre());
            System.out.println("   - DTO Email: " + usuarioDTO.getEmail());
            System.out.println("   - DTO Teléfono: " + usuarioDTO.getTelefono());
            System.out.println("   - DTO Edad: " + usuarioDTO.getEdad());

            // 7. DEBUG de la lista de usuarios en Empresa
            System.out.println("5. 👥 LISTA USUARIOS EN EMPRESA:");
            if (empresa.getListaUsuarios() != null) {
                System.out.println("   - Total usuarios: " + empresa.getListaUsuarios().size());
                for (int i = 0; i < empresa.getListaUsuarios().size(); i++) {
                    Usuario u = empresa.getListaUsuarios().get(i);
                    System.out.println("   - Usuario " + i + ": " + u.getEmail() + " | " + u.getNombre());
                }
            } else {
                System.out.println("   - Lista de usuarios es NULL");
            }

            // 8. Llamar a actualizar
            System.out.println("6. 🚀 LLAMANDO A EMPRESA.ACTUALIZARUSUARIO()");
            empresa.actualizarUsuario(usuarioDTO);
            System.out.println("   - Llamada completada");

            // 9. DEBUG después de actualizar
            System.out.println("7. 🔍 DESPUÉS DE ACTUALIZAR:");
            if (empresa.getListaUsuarios() != null) {
                for (int i = 0; i < empresa.getListaUsuarios().size(); i++) {
                    Usuario u = empresa.getListaUsuarios().get(i);
                    System.out.println("   - Usuario " + i + ": " + u.getEmail() + " | " + u.getNombre());
                }
            }

            // 10. Verificar si el usuario en SesionManager se actualizó
            System.out.println("8. 🔐 SESION MANAGER:");
            Usuario usuarioSesion = SesionManager.getUsuarioActual(Usuario.class);
            System.out.println("   - ¿Usuario sesion es null? " + (usuarioSesion == null));
            if (usuarioSesion != null) {
                System.out.println("   - Sesion Nombre: " + usuarioSesion.getNombre());
                System.out.println("   - Sesion Email: " + usuarioSesion.getEmail());
                System.out.println("   - Sesion HashCode: " + usuarioSesion.hashCode());
            }

            // 11. Verificar si son el mismo objeto
            System.out.println("9. 🔄 COMPARACIÓN DE OBJETOS:");
            System.out.println("   - ¿Mismo objeto? " + (usuarioActual == usuarioSesion));
            System.out.println("   - ¿Mismo nombre? " +
                    (usuarioActual != null && usuarioSesion != null &&
                            usuarioActual.getNombre().equals(usuarioSesion.getNombre())));

            mostrarMensajeExito("✅ Perfil actualizado exitosamente!");
            limpiarCamposContrasena();

            System.out.println("=== ✅ DEBUG FIN GUARDAR CAMBIOS ===\n");

        } catch (Exception e) {
            System.err.println("❌ ERROR CRÍTICO: " + e.getMessage());
            e.printStackTrace();
            mostrarError("Error", "Ocurrió un error: " + e.getMessage());
        }
    }

    private Usuario buscarUsuarioActualizado(String email) {
        // Buscar usuario actualizado en la lista
        for (Usuario usuario : empresa.getListaUsuarios()) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                return usuario;
            }
        }
        return null;
    }
    private UsuarioDTO crearUsuarioDTO() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        // Establecer el email como identificador (según tu método actualizarUsuario)
        usuarioDTO.setEmail(usuarioActual.getEmail()); // Email original para buscar
        usuarioDTO.setNombre(txtNombre.getText().trim());
        usuarioDTO.setEmail(txtEmail.getText().trim()); // Nuevo email si cambió
        usuarioDTO.setTelefono(txtTelefono.getText().trim());

        // Manejar edad
        try {
            if (!txtEdad.getText().trim().isEmpty()) {
                usuarioDTO.setEdad(Integer.parseInt(txtEdad.getText().trim()));
            } else {
                usuarioDTO.setEdad(0); // O valor por defecto
            }
        } catch (NumberFormatException e) {
            usuarioDTO.setEdad(0);
        }

        // Actualizar contraseña si se proporcionó una nueva
        if (!txtNuevaContrasena.getText().isEmpty()) {
            usuarioDTO.setContrasena(txtNuevaContrasena.getText());
        } else {
            // Mantener la contraseña actual
            usuarioDTO.setContrasena(usuarioActual.getContrasena());
        }

        return usuarioDTO;
    }

    @FXML
    private void cancelarEdicion() {
        System.out.println("Cancelar edición - DEBUG");
        cargarDatosUsuario(); // Recargar datos originales
        limpiarCamposContrasena();
        vboxMensaje.setVisible(false);
        mostrarAlerta("Edición cancelada", "Los cambios no guardados fueron descartados.");
    }

    @FXML
    private void volverAtras(ActionEvent event) {
        try {
            System.out.println("🔙 Volver atrás - DEBUG");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneManager.cambiarEscena(stage, "UsuarioView.fxml");
        } catch (Exception e) {
            mostrarError("Error de navegación", "No se pudo volver atrás: " + e.getMessage());
        }
    }

    @FXML
    private void cambiarContrasena() {
        // Enfocar en campos de contraseña
        txtContrasenaActual.requestFocus();
    }

    @FXML
    private void verHistorialEnvios() {
        mostrarAlerta("Funcionalidad", "Historial de envíos estará disponible próximamente.");
    }

    @FXML
    private void volverAlMenu() {
        mostrarAlerta("Navegación", "Volviendo al menú principal...");
    }

    // ========== IMPLEMENTACIÓN DE USUARIOOBSERVER ==========

    @Override
    public void onUsuarioActualizado(Usuario usuarioActualizado) {
        System.out.println("✅ Observer: Perfil actualizado recibido - " +
                (usuarioActualizado != null ? usuarioActualizado.getNombre() : "N/A"));

        if (usuarioActualizado != null) {
            this.usuarioActual = usuarioActualizado;

            javafx.application.Platform.runLater(() -> {
                cargarDatosUsuario();
                mostrarMensajeExito("Perfil sincronizado correctamente!");
            });
        }
    }

    @Override
    public void onErrorActualizacion(String mensajeError) {
        System.err.println("❌ Observer Error: " + mensajeError);

        javafx.application.Platform.runLater(() -> {
            mostrarError("Error de Sincronización", mensajeError);
        });
    }

    // ========== MÉTODOS PRIVADOS DE VALIDACIÓN ==========

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarError("Campo obligatorio", "El nombre es obligatorio");
            txtNombre.requestFocus();
            return false;
        }

        if (txtEmail.getText().trim().isEmpty()) {
            mostrarError("Campo obligatorio", "El email es obligatorio");
            txtEmail.requestFocus();
            return false;
        }

        if (txtContrasenaActual.getText().isEmpty()) {
            mostrarError("Campo obligatorio", "La contraseña actual es obligatoria");
            txtContrasenaActual.requestFocus();
            return false;
        }

        // Validar formato de email

        return true;
    }

    private boolean verificarContrasenaActual() {
        if (usuarioActual == null) {
            return false;
        }

        String contrasenaIngresada = txtContrasenaActual.getText();
        String contrasenaActual = usuarioActual.getContrasena();

        // Debug
        System.out.println("🔐 Verificando contraseña - Ingresada: " +
                contrasenaIngresada + ", Actual: " + contrasenaActual);

        // Verificación básica - en producción usar hashing
        return contrasenaIngresada.equals(contrasenaActual) ||
                contrasenaIngresada.equals("password123"); // contraseña de prueba
    }

    private void mostrarMensajeExito(String mensaje) {
        if (lblMensaje != null) {
            lblMensaje.setText(mensaje);
        }
        if (vboxMensaje != null) {
            vboxMensaje.setVisible(true);
            vboxMensaje.setStyle("-fx-background-color: #E8F5E9; -fx-border-color: #C8E6C9;");
        }

        // Ocultar mensaje después de 5 segundos
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                javafx.application.Platform.runLater(() -> {
                    if (vboxMensaje != null) {
                        vboxMensaje.setVisible(false);
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void limpiarCamposContrasena() {
        if (txtContrasenaActual != null) {
            txtContrasenaActual.clear();
        }
        if (txtNuevaContrasena != null) {
            txtNuevaContrasena.clear();
        }
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método para limpiar recursos
    public void cleanup() {
        if (usuarioSubject != null) {
            usuarioSubject.removeObserver(this);
        }
    }
}