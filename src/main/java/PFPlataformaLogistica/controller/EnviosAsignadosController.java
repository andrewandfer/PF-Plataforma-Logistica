package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.model.*;
import PFPlataformaLogistica.Utils.SesionManager;
import PFPlataformaLogistica.Utils.SceneManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EnviosAsignadosController implements Initializable {

    @FXML private TableView<Envio> tablaEnvios;
    @FXML private TableColumn<Envio, String> colId;
    @FXML private TableColumn<Envio, EstadoEnvio> colEstado;
    @FXML private TableColumn<Envio, String> colFechaCreacion;
    @FXML private TableColumn<Envio, String> colFechaEstimada;
    @FXML private TableColumn<Envio, String> colOrigen;
    @FXML private TableColumn<Envio, String> colDestino;
    @FXML private TableColumn<Envio, Integer> colPeso;
    @FXML private TableColumn<Envio, TipoEnvio> colTipo;
    @FXML private TableColumn<Envio, Integer> colCosto;
    @FXML private TableColumn<Envio, String> colProductos;
    @FXML private TableColumn<Envio, String> colDireccion;

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<EstadoEnvio> comboFiltrarEstado;
    @FXML private VBox vboxDetalles;

    // Labels para detalles
    @FXML private Label lblDetalleId;
    @FXML private Label lblDetalleEstado;
    @FXML private Label lblDetalleOrigen;
    @FXML private Label lblDetalleDestino;
    @FXML private Label lblDetalleFechaCreacion;
    @FXML private Label lblDetalleFechaEstimada;
    @FXML private Label lblDetallePeso;
    @FXML private Label lblDetalleCosto;
    @FXML private Label lblDetalleTipo;
    @FXML private Label lblDetalleProductos;

    // Labels informativos
    @FXML private Label lblInfoRepartidor;
    @FXML private Label lblTotalEnvios;
    @FXML private Label lblEnviosPendientes;
    @FXML private Label lblEnviosEntregados;
    @FXML private Label lblUsuario;

    private ObservableList<Envio> enviosObservable;
    private FilteredList<Envio> enviosFiltrados;
    private Repartidor repartidorActual;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Obtener el repartidor actual de la sesión
            repartidorActual = SesionManager.getUsuarioActual(Repartidor.class);

            if (repartidorActual == null) {
                mostrarError("No se pudo obtener la información del repartidor");
                return;
            }

            // Configurar interfaz
            configurarTabla();
            configurarFiltros();
            cargarEnviosDelRepartidor();
            actualizarEstadisticas();
            actualizarInformacionUsuario();

        } catch (Exception e) {
            mostrarError("Error al inicializar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarTabla() {
        // Configurar las columnas de la tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("idEnvio"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoEnvio"));
        colFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        colFechaEstimada.setCellValueFactory(new PropertyValueFactory<>("fechaEstimada"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("origen"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("destino"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("pesoEnvio"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoEnvio"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));

        // CORRECCIÓN: Usar cellValueFactory personalizado en lugar de PropertyValueFactory para campos complejos
        colProductos.setCellValueFactory(cellData -> {
            Envio envio = cellData.getValue();
            if (envio != null && envio.getListaProductos() != null && !envio.getListaProductos().isEmpty()) {
                return new SimpleStringProperty(envio.getListaProductos().size() + " productos");
            } else {
                return new SimpleStringProperty("0 productos");
            }
        });

        colDireccion.setCellValueFactory(cellData -> {
            Envio envio = cellData.getValue();
            if (envio != null && envio.getDireccion() != null) {
                return new SimpleStringProperty(envio.getDireccion().toString());
            } else {
                return new SimpleStringProperty("Sin dirección");
            }
        });

        // CORRECCIÓN: Eliminar las cellFactory problemáticas y usar solo cellValueFactory
        // Las cellFactory ya no son necesarias porque el cellValueFactory ya devuelve el String formateado

        // Personalizar columna de estado con colores (esta sí necesita cellFactory)
        colEstado.setCellFactory(column -> new TableCell<Envio, EstadoEnvio>() {
            @Override
            protected void updateItem(EstadoEnvio estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(estado.toString());
                    switch (estado) {
                        case SOLICITADO:
                            setStyle("-fx-text-fill: #FF9800; -fx-font-weight: bold;");
                            break;
                        case ASIGNADO:
                            setStyle("-fx-text-fill: #2196F3; -fx-font-weight: bold;");
                            break;
                        case ENRUTA:
                            setStyle("-fx-text-fill: #9C27B0; -fx-font-weight: bold;");
                            break;
                        case ENTREGADO:
                            setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
                            break;
                        case INCIDENCIA:
                            setStyle("-fx-text-fill: #F44336; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        // Listener para mostrar detalles cuando se selecciona un envío
        tablaEnvios.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetallesEnvio(newValue));
    }

    private void configurarFiltros() {
        // Configurar combo box de estados
        comboFiltrarEstado.setItems(FXCollections.observableArrayList(EstadoEnvio.values()));
        comboFiltrarEstado.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> aplicarFiltros());
    }

    private void cargarEnviosDelRepartidor() {
        // Respetando SOLID: El controller adapta los datos del dominio a las necesidades de la UI
        if (repartidorActual == null) {
            enviosObservable = FXCollections.observableArrayList();
        } else {
            // Obtener datos del dominio y adaptarlos a ObservableList
            java.util.List<Envio> enviosDelDominio = repartidorActual.getEnviosAsignados();
            if (enviosDelDominio != null) {
                enviosObservable = FXCollections.observableArrayList(enviosDelDominio);
            } else {
                enviosObservable = FXCollections.observableArrayList();
            }
        }

        // Configurar filtros
        enviosFiltrados = new FilteredList<>(enviosObservable);
        SortedList<Envio> enviosOrdenados = new SortedList<>(enviosFiltrados);
        enviosOrdenados.comparatorProperty().bind(tablaEnvios.comparatorProperty());

        tablaEnvios.setItems(enviosOrdenados);

        // Aplicar filtro de búsqueda
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros());
    }

    private void aplicarFiltros() {
        if (enviosFiltrados == null) return;

        enviosFiltrados.setPredicate(envio -> {
            if (envio == null) return false;

            // Filtro por texto de búsqueda
            String textoBusqueda = txtBuscar.getText();
            if (textoBusqueda != null && !textoBusqueda.isEmpty()) {
                String textoLower = textoBusqueda.toLowerCase();
                boolean coincide = (envio.getIdEnvio() != null && envio.getIdEnvio().toLowerCase().contains(textoLower)) ||
                        (envio.getOrigen() != null && envio.getOrigen().toLowerCase().contains(textoLower)) ||
                        (envio.getDestino() != null && envio.getDestino().toLowerCase().contains(textoLower));
                if (!coincide) {
                    return false;
                }
            }

            // Filtro por estado
            EstadoEnvio estadoFiltro = comboFiltrarEstado.getValue();
            if (estadoFiltro != null && envio.getEstadoEnvio() != estadoFiltro) {
                return false;
            }

            return true;
        });

        actualizarEstadisticas();
    }

    private void actualizarEstadisticas() {
        if (enviosObservable == null) return;

        int total = enviosObservable.size();
        long pendientes = enviosObservable.stream()
                .filter(e -> e.getEstadoEnvio() == EstadoEnvio.SOLICITADO ||
                        e.getEstadoEnvio() == EstadoEnvio.ASIGNADO)
                .count();
        long entregados = enviosObservable.stream()
                .filter(e -> e.getEstadoEnvio() == EstadoEnvio.ENTREGADO)
                .count();

        lblTotalEnvios.setText("Total Envíos: " + total);
        lblEnviosPendientes.setText("Pendientes: " + pendientes);
        lblEnviosEntregados.setText("Entregados: " + entregados);
    }

    private void actualizarInformacionUsuario() {
        if (repartidorActual != null) {
            lblInfoRepartidor.setText("Repartidor: " + repartidorActual.getNombre());
            lblUsuario.setText("Usuario: " + repartidorActual.getNombre() + " | " + repartidorActual.getEmail());
        }
    }

    private void mostrarDetallesEnvio(Envio envio) {
        if (envio == null) {
            vboxDetalles.setVisible(false);
            return;
        }

        // Llenar los labels con la información del envío
        lblDetalleId.setText(envio.getIdEnvio() != null ? envio.getIdEnvio() : "N/A");
        lblDetalleEstado.setText(envio.getEstadoEnvio() != null ? envio.getEstadoEnvio().toString() : "N/A");
        lblDetalleOrigen.setText(envio.getOrigen() != null ? envio.getOrigen() : "N/A");
        lblDetalleDestino.setText(envio.getDestino() != null ? envio.getDestino() : "N/A");
        lblDetalleFechaCreacion.setText(envio.getFechaCreacion() != null ? envio.getFechaCreacion() : "N/A");
        lblDetalleFechaEstimada.setText(envio.getFechaEstimada() != null ? envio.getFechaEstimada() : "N/A");
        lblDetallePeso.setText(envio.getPesoEnvio() + " kg");
        lblDetalleCosto.setText("$" + envio.getCosto());
        lblDetalleTipo.setText(envio.getTipoEnvio() != null ? envio.getTipoEnvio().toString() : "N/A");

        // Información de productos
        if (envio.getListaProductos() != null && !envio.getListaProductos().isEmpty()) {
            lblDetalleProductos.setText(envio.getListaProductos().size() + " productos");
        } else {
            lblDetalleProductos.setText("0 productos");
        }

        vboxDetalles.setVisible(true);
    }

    // Métodos de acción
    @FXML
    private void actualizarLista(ActionEvent event) {
        refrescarDatos();
        mostrarAlerta("Éxito", "Lista de envíos actualizada correctamente");
    }

    @FXML
    private void cambiarEstadoEnvio(ActionEvent event) {
        Envio envioSeleccionado = tablaEnvios.getSelectionModel().getSelectedItem();

        if (envioSeleccionado == null) {
            mostrarAlerta("Error", "Selecciona un envío para cambiar su estado");
            return;
        }

        // Crear diálogo para cambiar estado
        ChoiceDialog<EstadoEnvio> dialog = new ChoiceDialog<>(
                envioSeleccionado.getEstadoEnvio(),
                EstadoEnvio.values()
        );
        dialog.setTitle("Cambiar Estado del Envío");
        dialog.setHeaderText("Cambiar estado del envío: " + envioSeleccionado.getIdEnvio());
        dialog.setContentText("Selecciona el nuevo estado:");

        dialog.showAndWait().ifPresent(nuevoEstado -> {
            // Actualizar en el dominio
            envioSeleccionado.setEstadoEnvio(nuevoEstado);

            // Actualizar UI
            tablaEnvios.refresh();
            actualizarEstadisticas();
            mostrarDetallesEnvio(envioSeleccionado);
            mostrarAlerta("Éxito", "Estado del envío actualizado a: " + nuevoEstado);
        });
    }

    @FXML
    private void verDetallesEnvio(ActionEvent event) {
        Envio envioSeleccionado = tablaEnvios.getSelectionModel().getSelectedItem();

        if (envioSeleccionado == null) {
            mostrarAlerta("Error", "Selecciona un envío para ver sus detalles");
            return;
        }

        mostrarDetallesEnvio(envioSeleccionado);
    }

    @FXML
    private void buscarEnvios(ActionEvent event) {
        aplicarFiltros();
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        txtBuscar.clear();
        comboFiltrarEstado.getSelectionModel().clearSelection();
        aplicarFiltros();
    }

    @FXML
    private void volverAtras(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.cambiarEscena(stage, "GestionRepartidores.fxml");
    }

    @FXML
    private void volverAlMenu(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.cambiarEscena(stage, "login.fxml");
    }

    // Método público para refrescar datos desde otras partes de la aplicación
    public void refrescarDatos() {
        cargarEnviosDelRepartidor();
        actualizarEstadisticas();
        actualizarInformacionUsuario();
    }

    // Método para notificar que se agregó un nuevo envío
    public void notificarNuevoEnvio(Envio nuevoEnvio) {
        if (enviosObservable != null && nuevoEnvio != null) {
            enviosObservable.add(nuevoEnvio);
            aplicarFiltros();
            mostrarAlerta("Éxito", "Nuevo envío agregado: " + nuevoEnvio.getIdEnvio());
        }
    }

    // Método para obtener el repartidor actual (útil para otras clases)
    public Repartidor getRepartidorActual() {
        return repartidorActual;
    }

    // Métodos utilitarios
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}