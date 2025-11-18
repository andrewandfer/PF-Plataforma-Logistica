package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.model.Empresa;
import PFPlataformaLogistica.model.Usuario;
import PFPlataformaLogistica.model.Direccion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class GestionUsuariosAdministradorController {

    Empresa empresa;

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtContrasena;

    @FXML
    private Button btnCrear;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnVolver;
    @FXML
    private Label lblMensaje;

    @FXML
    private TableView<Usuario> tablaUsuarios;
    @FXML
    private TableColumn<Usuario, String> colNombre;
    @FXML
    private TableColumn<Usuario, String> colTelefono;
    @FXML
    private TableColumn<Usuario, String> colDireccion;
    @FXML
    private TableColumn<Usuario, String> colEmail;
    @FXML
    private TableColumn<Usuario, String> colEnvios;

    // Elementos de la sección de detalles
    @FXML
    private VBox vboxDetalles;
    @FXML
    private Label lblTipoUsuario;
    @FXML
    private Label lblDetalleNombre;
    @FXML
    private Label lblDetalleTelefono;
    @FXML
    private Label lblDetalleEmail;
    @FXML
    private Label lblDetalleDireccion;
    @FXML
    private Label lblDetalleEnvios;
    @FXML
    private Label lblDetalleTotalDirecciones;
    @FXML
    private Label lblDetalleProductos;
    @FXML
    private Label lblDetalleIdDireccion;

    private ObservableList<Usuario> listaUsuarios;
    private int contadorDirecciones = 1;

    @FXML
    public void initialize() {
        empresa = Empresa.getInstance();
        listaUsuarios = FXCollections.observableArrayList();

        // CARGAR SOLO USUARIOS ESTÁNDAR DESDE LA EMPRESA
        cargarUsuariosDesdeEmpresa();

        tablaUsuarios.setItems(listaUsuarios);

        // Configurar columnas simplificadas
        colNombre.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            String nombre = (usuario != null && usuario.getNombre() != null) ? usuario.getNombre() : "Sin nombre";
            return new javafx.beans.property.SimpleStringProperty(nombre);
        });

        colTelefono.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            String telefono = (usuario != null && usuario.getTelefono() != null) ? usuario.getTelefono() : "Sin teléfono";
            return new javafx.beans.property.SimpleStringProperty(telefono);
        });

        colDireccion.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            if (usuario != null && usuario.getDireccion() != null) {
                return new javafx.beans.property.SimpleStringProperty(usuario.getDireccion().toString());
            } else {
                return new javafx.beans.property.SimpleStringProperty("Sin dirección");
            }
        });

        colEmail.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            String email = (usuario != null && usuario.getEmail() != null) ? usuario.getEmail() : "Sin email";
            return new javafx.beans.property.SimpleStringProperty(email);
        });

        colEnvios.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            int cantidad = (usuario != null && usuario.getEnviosPropios() != null) ? usuario.getEnviosPropios().size() : 0;
            return new javafx.beans.property.SimpleStringProperty(String.valueOf(cantidad));
        });

        // Listener para cargar datos cuando se selecciona un usuario
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarUsuarioSeleccionado(newSelection);
                mostrarDetallesUsuario(newSelection);
            } else {
                ocultarDetalles();
            }
        });

        // Listener para doble clic - enfatizar la visualización
        tablaUsuarios.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    mostrarDetallesUsuario(seleccionado);
                    // Efecto visual para indicar que se mostraron detalles
                    vboxDetalles.setStyle("-fx-border-color: #2196F3; -fx-border-width: 2;");
                }
            }
        });
    }

    private void cargarUsuariosDesdeEmpresa() {
        try {
            // Obtener la lista de usuarios desde la empresa
            List<Usuario> usuariosEmpresa = empresa.getListaUsuarios();

            if (usuariosEmpresa != null && !usuariosEmpresa.isEmpty()) {
                // Filtrar solo usuarios estándar (no repartidores ni administradores)
                for (Usuario usuario : usuariosEmpresa) {
                    if (usuario != null && usuario.getEmail() != null ) {
                        listaUsuarios.add(usuario);
                        System.out.println("Usuario estándar cargado: " + usuario.getEmail());
                    }
                }
                System.out.println("Usuarios estándar cargados desde empresa: " + listaUsuarios.size());

            } else {
                System.out.println("No hay usuarios en la empresa o la lista está vacía");
            }

        } catch (Exception e) {
            System.err.println("Error al cargar usuarios desde empresa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Verifica si el usuario es un usuario estándar (no repartidor ni administrador)
     */

    private void cargarUsuarioSeleccionado(Usuario usuario) {
        if (usuario == null) return;

        txtNombre.setText(usuario.getNombre() != null ? usuario.getNombre() : "");
        txtTelefono.setText(usuario.getTelefono() != null ? usuario.getTelefono() : "");
        txtEmail.setText(usuario.getEmail() != null ? usuario.getEmail() : "");
        txtContrasena.setText("******");

        if (usuario.getDireccion() != null) {
            txtDireccion.setText(usuario.getDireccion().toString());
        } else {
            txtDireccion.setText("");
        }
    }

    /**
     * Muestra los detalles del usuario en la sección inferior
     */
    private void mostrarDetallesUsuario(Usuario usuario) {
        if (usuario == null) {
            ocultarDetalles();
            return;
        }

        // Mostrar la sección de detalles
        vboxDetalles.setVisible(true);
        vboxDetalles.setStyle("-fx-border-color: #DEE2E6; -fx-border-width: 1;"); // Reset estilo

        // Información básica
        lblDetalleNombre.setText(usuario.getNombre() != null ? usuario.getNombre() : "No disponible");
        lblDetalleTelefono.setText(usuario.getTelefono() != null ? usuario.getTelefono() : "No disponible");
        lblDetalleEmail.setText(usuario.getEmail() != null ? usuario.getEmail() : "No disponible");

        // Información de dirección
        if (usuario.getDireccion() != null) {
            lblDetalleDireccion.setText(usuario.getDireccion().toString());
            lblDetalleIdDireccion.setText(usuario.getDireccion().getIdDireccion() != null ?
                    usuario.getDireccion().getIdDireccion() : "No disponible");
        } else {
            lblDetalleDireccion.setText("No disponible");
            lblDetalleIdDireccion.setText("No disponible");
        }

        // Información de cantidades
        lblDetalleEnvios.setText(String.valueOf(usuario.getEnviosPropios() != null ? usuario.getEnviosPropios().size() : 0));
        lblDetalleTotalDirecciones.setText(String.valueOf(usuario.getListaDirecciones() != null ? usuario.getListaDirecciones().size() : 0));
        lblDetalleProductos.setText(String.valueOf(usuario.getListaProductos() != null ? usuario.getListaProductos().size() : 0));

        // Para usuarios estándar, siempre mostrar como "USUARIO ESTÁNDAR"
        lblTipoUsuario.setText("USUARIO ESTÁNDAR");
        lblTipoUsuario.setStyle("-fx-background-color: #D4EDDA; -fx-text-fill: #155724;");
    }

    /**
     * Oculta la sección de detalles
     */
    private void ocultarDetalles() {
        vboxDetalles.setVisible(false);
    }

    @FXML
    void onCrear(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccionStr = txtDireccion.getText().trim();
        String email = txtEmail.getText().trim();
        String contrasena = txtContrasena.getText().trim();

        if (nombre.isEmpty() || telefono.isEmpty() || direccionStr.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
            lblMensaje.setText("Por favor, completa todos los campos.");
            return;
        }

        if (!email.contains("@")) {
            lblMensaje.setText("Por favor, ingresa un email válido.");
            return;
        }

        try {
            Direccion direccion = new Direccion.DireccionBuilder()
                    .idDireccion(String.valueOf(contadorDirecciones++))
                    .calle(direccionStr)
                    .alias("Principal")
                    .ciudad("Ciudad")
                    .build();

            Usuario nuevo = new Usuario.UsuarioBuilder()
                    .nombre(nombre)
                    .telefono(telefono)
                    .direccion(direccion)
                    .email(email)
                    .contrasena(contrasena)
                    .build();

            boolean agregado = empresa.agregarUsuario(nuevo);
            if (agregado) {
                listaUsuarios.add(nuevo);
                lblMensaje.setText("Usuario creado correctamente.");
                limpiarFormulario();
                ocultarDetalles();
            } else {
                lblMensaje.setText("Error: No se pudo agregar el usuario a la empresa (¿ya existe?).");
            }
        } catch (Exception e) {
            lblMensaje.setText("Error al crear usuario: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    void onActualizar(ActionEvent event) {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            lblMensaje.setText("Por favor, selecciona un usuario de la tabla para actualizar.");
            return;
        }

        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccionStr = txtDireccion.getText().trim();
        String email = txtEmail.getText().trim();
        String contrasena = txtContrasena.getText().trim();

        if (nombre.isEmpty() || telefono.isEmpty() || direccionStr.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
            lblMensaje.setText("Por favor, completa todos los campos.");
            return;
        }

        if (!email.contains("@")) {
            lblMensaje.setText("Por favor, ingresa un email válido.");
            return;
        }

        try {
            Direccion direccion;
            if (seleccionado.getDireccion() != null) {
                direccion = new Direccion.DireccionBuilder()
                        .idDireccion(seleccionado.getDireccion().getIdDireccion())
                        .calle(direccionStr)
                        .alias("Principal")
                        .ciudad("Ciudad")
                        .build();
            } else {
                direccion = new Direccion.DireccionBuilder()
                        .idDireccion(String.valueOf(contadorDirecciones++))
                        .calle(direccionStr)
                        .alias("Principal")
                        .ciudad("Ciudad")
                        .build();
            }

            seleccionado.setNombre(nombre);
            seleccionado.setTelefono(telefono);
            seleccionado.setDireccion(direccion);
            seleccionado.setEmail(email);
            if (!contrasena.equals("******")) {
                seleccionado.setContrasena(contrasena);
            }

            boolean actualizado = empresa.actualizarUsuario(seleccionado);
            if (actualizado) {
                lblMensaje.setText("Usuario actualizado correctamente.");
                tablaUsuarios.refresh();
                mostrarDetallesUsuario(seleccionado); // Actualizar detalles
                limpiarFormulario();
            } else {
                lblMensaje.setText("Error: No se pudo actualizar el usuario en la empresa.");
            }
        } catch (Exception e) {
            lblMensaje.setText("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        empresa.getListaPersonas().add(seleccionado);
    }

    @FXML
    void onEliminar(ActionEvent event) {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            lblMensaje.setText("Por favor, selecciona un usuario de la tabla para eliminar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Usuario");
        alert.setHeaderText("Confirmar eliminación");
        alert.setContentText("¿Estás seguro de que deseas eliminar al usuario '" +
                (seleccionado.getNombre() != null ? seleccionado.getNombre() : "Sin nombre") + "'?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean eliminado = empresa.eliminarUsuario(seleccionado);
            if (eliminado) {
                listaUsuarios.remove(seleccionado);
                lblMensaje.setText("Usuario eliminado correctamente.");
                limpiarFormulario();
                ocultarDetalles();
            } else {
                lblMensaje.setText("Error: No se pudo eliminar el usuario de la empresa.");
            }
        }
    }

    @FXML
    void onLimpiar(ActionEvent event) {
        limpiarFormulario();
        ocultarDetalles();
    }

    @FXML
    void onVolver(ActionEvent event) {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "AdministradorView.fxml");
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtEmail.clear();
        txtContrasena.clear();
        lblMensaje.setText("");
        tablaUsuarios.getSelectionModel().clearSelection();
    }

    @FXML
    void onAyuda(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ayuda");
        alert.setHeaderText("¿Necesitas ayuda?");
        alert.setContentText("Aquí puedes encontrar información sobre cómo gestionar usuarios estándar.");
        alert.showAndWait();
    }

    @FXML
    void onContacto(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Contacto");
        alert.setHeaderText("¿Tienes alguna pregunta?");
        alert.setContentText("Puedes escribirnos a soporte@enviorapido.com");
        alert.showAndWait();
    }

    public void refrescarDatos() {
        listaUsuarios.clear();
        cargarUsuariosDesdeEmpresa();
        ocultarDetalles();
    }
}