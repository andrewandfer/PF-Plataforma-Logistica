package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.model.Empresa;
import PFPlataformaLogistica.model.Repartidor;
import PFPlataformaLogistica.model.EstadoRepartidor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class GestionRepartidoresFormularioController {

    private Empresa empresa;
    private ObservableList<Repartidor> listaRepartidores;

    // Campos del formulario - AGREGADO txtNombre
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtTelefono;
    @FXML
    private ComboBox<EstadoRepartidor> cbDisponibilidad;
    @FXML
    private TextField txtZonaCobertura;
    @FXML
    private TextField txtLocalidad;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtContrasena;

    // Botones
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

    // Tabla - AGREGADA colNombre
    @FXML
    private TableView<Repartidor> tablaRepartidores;
    @FXML
    private TableColumn<Repartidor, String> colNombre;
    @FXML
    private TableColumn<Repartidor, String> colTelefono;
    @FXML
    private TableColumn<Repartidor, String> colDisponibilidad;
    @FXML
    private TableColumn<Repartidor, String> colZonaCobertura;
    @FXML
    private TableColumn<Repartidor, String> colLocalidad;
    @FXML
    private TableColumn<Repartidor, String> colEmail;

    @FXML
    public void initialize() {
        // Obtener instancia de la empresa
        empresa = Empresa.getInstance();

        // Inicializar lista observable con los repartidores de la empresa
        listaRepartidores = FXCollections.observableArrayList(empresa.getListaRepartidores());
        tablaRepartidores.setItems(listaRepartidores);

        // Configurar las columnas de la tabla
        configurarColumnasTabla();

        // Llenar el ComboBox con los valores del enum
        cbDisponibilidad.getItems().setAll(EstadoRepartidor.values());

        // Configurar listeners - CORREGIDO para evitar ventana emergente en doble click
        configurarListeners();
    }

    private void configurarColumnasTabla() {
        // NUEVA columna para Nombre
        colNombre.setCellValueFactory(cellData -> {
            Repartidor repartidor = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(repartidor.getNombre());
        });

        colTelefono.setCellValueFactory(cellData -> {
            Repartidor repartidor = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(repartidor.getTelefono());
        });

        colDisponibilidad.setCellValueFactory(cellData -> {
            Repartidor repartidor = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(repartidor.getEstadoDisponibilidad().toString());
        });

        colZonaCobertura.setCellValueFactory(cellData -> {
            Repartidor repartidor = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(repartidor.getZonaCobertura());
        });

        colLocalidad.setCellValueFactory(cellData -> {
            Repartidor repartidor = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(repartidor.getLocalidad());
        });

        colEmail.setCellValueFactory(cellData -> {
            Repartidor repartidor = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(repartidor.getEmail());
        });
    }

    private void configurarListeners() {
        // Listener para selección en la tabla - SOLO simple click
        tablaRepartidores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarRepartidorSeleccionado(newSelection);
            }
        });

        // CORREGIDO: Eliminado el listener de doble click que causaba ventanas emergentes
        // Si necesitas funcionalidad de doble click, descomenta y ajusta:
        /*
        tablaRepartidores.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Repartidor seleccionado = tablaRepartidores.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    // Solo ejecutar si realmente quieres una acción en doble click
                    // abrirVentanaDetallesEnvios(seleccionado);
                }
            }
        });
        */
    }

    private void cargarRepartidorSeleccionado(Repartidor repartidor) {
        // AGREGADO campo nombre
        txtNombre.setText(repartidor.getNombre());
        txtTelefono.setText(repartidor.getTelefono());
        cbDisponibilidad.setValue(repartidor.getEstadoDisponibilidad());
        txtZonaCobertura.setText(repartidor.getZonaCobertura());
        txtLocalidad.setText(repartidor.getLocalidad());
        txtEmail.setText(repartidor.getEmail());
        txtContrasena.setText("******"); // No mostrar la contraseña real
    }

    // Método opcional si necesitas la funcionalidad de doble click
    private void abrirVentanaDetallesEnvios(Repartidor repartidor) {
        try {
            // Solo ejecutar si realmente quieres abrir otra ventana
            // FXMLLoader loader = new FXMLLoader(getClass().getResource("/PFPlataformaLogistica/view/DetallesEnviosRepartidorView.fxml"));
            // Parent root = loader.load();
            // ... resto del código
        } catch (Exception e) {
            mostrarError("Error", "No se pudo abrir la ventana de detalles.", e.getMessage());
        }
    }

    @FXML
    void onCrear(ActionEvent event) {
        try {
            if (!validarFormulario()) return;

            // Verificar si ya existe un repartidor con el mismo teléfono
            String telefono = txtTelefono.getText().trim();
            if (existeRepartidorConTelefono(telefono)) {
                mostrarMensajeAdvertencia("Ya existe un repartidor con el teléfono: " + telefono);
                return;
            }

            // AGREGADO campo nombre en la creación
            Repartidor nuevoRepartidor = new Repartidor.RepartidorBuilder()
                    .nombre(txtNombre.getText().trim()) // NUEVO
                    .telefono(telefono)
                    .disponibilidad(cbDisponibilidad.getValue())
                    .zonaCobertura(txtZonaCobertura.getText().trim())
                    .localidad(txtLocalidad.getText().trim())
                    .email(txtEmail.getText().trim())
                    .contrasena(txtContrasena.getText().trim())
                    .build();

            // Agregar a la lista local y a la empresa
            listaRepartidores.add(nuevoRepartidor);
            Empresa.getInstance().getListaPersonas().add(nuevoRepartidor); // Asegurar que también se agrega a la lista de personas
            empresa.registrarRepartidor(nuevoRepartidor);

            mostrarMensajeExito("Repartidor creado correctamente.");
            limpiarFormulario();

        } catch (Exception e) {
            mostrarError("Error al crear", "No se pudo crear el repartidor.", e.getMessage());
        }

    }

    @FXML
    void onActualizar(ActionEvent event) {
        Repartidor seleccionado = tablaRepartidores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensajeAdvertencia("Por favor, selecciona un repartidor de la tabla para actualizar.");
            return;
        }

        if (!validarFormulario()) return;

        try {
            String telefono = txtTelefono.getText().trim();

            // Verificar si el teléfono ya existe en otro repartidor
            if (!seleccionado.getTelefono().equals(telefono) && existeRepartidorConTelefono(telefono)) {
                mostrarMensajeAdvertencia("Ya existe otro repartidor con el teléfono: " + telefono);
                return;
            }

            // Actualizar los datos del repartidor - AGREGADO nombre
            seleccionado.setNombre(txtNombre.getText().trim()); // NUEVO
            seleccionado.setTelefono(telefono);
            seleccionado.setDisponibilidad(cbDisponibilidad.getValue());
            seleccionado.setZonaCobertura(txtZonaCobertura.getText().trim());
            seleccionado.setLocalidad(txtLocalidad.getText().trim());
            seleccionado.setEmail(txtEmail.getText().trim());

            // Solo actualizar contraseña si no es el placeholder
            if (!txtContrasena.getText().equals("******")) {
                seleccionado.setContrasena(txtContrasena.getText().trim());
            }

            // Refrescar la tabla
            tablaRepartidores.refresh();
            mostrarMensajeExito("Repartidor actualizado correctamente.");
            limpiarFormulario();

        } catch (Exception e) {
            mostrarError("Error al actualizar", "No se pudo actualizar el repartidor.", e.getMessage());
        }
    }

    @FXML
    void onEliminar(ActionEvent event) {
        Repartidor seleccionado = tablaRepartidores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensajeAdvertencia("Por favor, selecciona un repartidor de la tabla para eliminar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Repartidor");
        alert.setHeaderText("Confirmar eliminación");
        alert.setContentText("¿Estás seguro de que deseas eliminar al repartidor '" + seleccionado.getNombre() + "'?\nEsta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Eliminar de la lista local y de la empresa
            listaRepartidores.remove(seleccionado);
            empresa.eliminarRepartidor(seleccionado.getTelefono());

            mostrarMensajeExito("Repartidor eliminado correctamente.");
            limpiarFormulario();
        }
    }

    @FXML
    void onLimpiar(ActionEvent event) {
        limpiarFormulario();
    }

    @FXML
    void onVolver(ActionEvent event) {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "AdministradorView.fxml");
    }

    @FXML
    void onAyuda(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ayuda - Gestión de Repartidores");
        alert.setHeaderText("Instrucciones de Uso");
        alert.setContentText("""
            GESTIÓN DE REPARTIDORES - INSTRUCCIONES:

            1. CREAR REPARTIDOR:
               • Complete todos los campos del formulario
               • Haga clic en 'Crear Repartidor'

            2. ACTUALIZAR REPARTIDOR:
               • Seleccione un repartidor de la tabla
               • Modifique los campos necesarios
               • Haga clic en 'Actualizar Repartidor'

            3. ELIMINAR REPARTIDOR:
               • Seleccione un repartidor de la tabla
               • Haga clic en 'Eliminar Repartidor'

            4. LIMPIAR FORMULARIO:
               • Haga clic en 'Limpiar Campos' para vaciar el formulario

            CAMPOS OBLIGATORIOS:
            • Nombre, Teléfono, Disponibilidad, Zona de Cobertura, Localidad, Email, Contraseña""");
        alert.showAndWait();
    }

    @FXML
    void onContacto(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Contacto - Soporte Técnico");
        alert.setHeaderText("¿Necesitas ayuda?");
        alert.setContentText("""
            Para asistencia técnica contacte a:

            📧 Email: soporte@enviorapido.com
            📞 Teléfono: +34 900 123 456
            🕒 Horario: Lunes a Viernes 9:00 - 18:00

            Estaremos encantados de ayudarte con cualquier 
            duda o problema técnico que puedas tener.""");
        alert.showAndWait();
    }

    // MÉTODOS DE VALIDACIÓN Y UTILIDAD
    private boolean validarFormulario() {
        // AGREGADA validación para nombre
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarMensajeAdvertencia("El campo Nombre es obligatorio.");
            txtNombre.requestFocus();
            return false;
        }

        if (txtTelefono.getText().trim().isEmpty()) {
            mostrarMensajeAdvertencia("El campo Teléfono es obligatorio.");
            txtTelefono.requestFocus();
            return false;
        }

        if (cbDisponibilidad.getValue() == null) {
            mostrarMensajeAdvertencia("El campo Disponibilidad es obligatorio.");
            cbDisponibilidad.requestFocus();
            return false;
        }

        if (txtZonaCobertura.getText().trim().isEmpty()) {
            mostrarMensajeAdvertencia("El campo Zona de Cobertura es obligatorio.");
            txtZonaCobertura.requestFocus();
            return false;
        }

        if (txtLocalidad.getText().trim().isEmpty()) {
            mostrarMensajeAdvertencia("El campo Localidad es obligatorio.");
            txtLocalidad.requestFocus();
            return false;
        }

        if (txtEmail.getText().trim().isEmpty()) {
            mostrarMensajeAdvertencia("El campo Email es obligatorio.");
            txtEmail.requestFocus();
            return false;
        }

        if (txtContrasena.getText().trim().isEmpty()) {
            mostrarMensajeAdvertencia("El campo Contraseña es obligatorio.");
            txtContrasena.requestFocus();
            return false;
        }

        // Validación adicional de email
        if (!validarEmail(txtEmail.getText().trim())) {
            mostrarMensajeAdvertencia("Por favor, ingrese un email válido.");
            txtEmail.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validarEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private boolean existeRepartidorConTelefono(String telefono) {
        return listaRepartidores.stream()
                .anyMatch(repartidor -> repartidor.getTelefono().equals(telefono));
    }

    private void limpiarFormulario() {
        // AGREGADO limpiar nombre
        txtNombre.clear();
        txtTelefono.clear();
        cbDisponibilidad.setValue(null);
        txtZonaCobertura.clear();
        txtLocalidad.clear();
        txtEmail.clear();
        txtContrasena.clear();
        lblMensaje.setText("");
        tablaRepartidores.getSelectionModel().clearSelection();
    }

    private void mostrarMensajeExito(String mensaje) {
        lblMensaje.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
        lblMensaje.setText("✓ " + mensaje);
    }

    private void mostrarMensajeAdvertencia(String mensaje) {
        lblMensaje.setStyle("-fx-text-fill: #FF9800; -fx-font-weight: bold;");
        lblMensaje.setText("⚠ " + mensaje);
    }

    private void mostrarError(String titulo, String header, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    // Método para obtener la lista de repartidores (útil para otros controladores)
    public ObservableList<Repartidor> getListaRepartidores() {
        return listaRepartidores;
    }

    // Método para actualizar la vista cuando hay cambios externos
    public void actualizarVista() {
        listaRepartidores.setAll(empresa.getListaRepartidores());
        tablaRepartidores.refresh();
    }
}