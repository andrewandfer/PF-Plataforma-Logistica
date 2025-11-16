
package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class GestionRepartidoresController implements Initializable {

    @FXML private TableView<RepartidorTableData> tableRepartidores;
    @FXML private TableColumn<RepartidorTableData, String> colId;
    @FXML private TableColumn<RepartidorTableData, String> colNombre;
    @FXML private TableColumn<RepartidorTableData, String> colTelefono;
    @FXML private TableColumn<RepartidorTableData, String> colEstado;
    @FXML private TableColumn<RepartidorTableData, String> colZona;
    @FXML private TableColumn<RepartidorTableData, String> colLocalidad;
    @FXML private TableColumn<RepartidorTableData, Integer> colEnviosAsignados;

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cbFiltroEstado;

    @FXML private Button btnNuevoRepartidor;
    @FXML private Button btnEditarRepartidor;
    @FXML private Button btnEliminarRepartidor;
    @FXML private Button btnCambiarEstado;
    @FXML private Button btnVerEnvios;
    @FXML private Button btnVolver;

    @FXML private Label lblTotalRepartidores;

    private Empresa empresa;
    private ObservableList<RepartidorTableData> repartidoresData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        empresa = Empresa.getInstance();
        setupTable();
        setupFilters();
        cargarRepartidores();
        actualizarContador();
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colZona.setCellValueFactory(new PropertyValueFactory<>("zonaCobertura"));
        colLocalidad.setCellValueFactory(new PropertyValueFactory<>("localidad"));
        colEnviosAsignados.setCellValueFactory(new PropertyValueFactory<>("enviosAsignados"));

        // Estilizar columna de estado
        colEstado.setCellFactory(column -> new TableCell<RepartidorTableData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "ACTIVO":
                            setStyle("-fx-background-color: #C8E6C9; -fx-text-fill: #2E7D32; -fx-font-weight: bold;");
                            break;
                        case "INACTIVO":
                            setStyle("-fx-background-color: #FFCDD2; -fx-text-fill: #C62828; -fx-font-weight: bold;");
                            break;
                        case "EN_RUTA":
                            setStyle("-fx-background-color: #BBDEFB; -fx-text-fill: #1565C0; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        // Configurar selección única
        tableRepartidores.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void setupFilters() {
        cbFiltroEstado.setItems(FXCollections.observableArrayList(
                "Todos", "ACTIVO", "INACTIVO", "EN_RUTA"
        ));
        cbFiltroEstado.setValue("Todos");
    }

    private void cargarRepartidores() {
        repartidoresData = FXCollections.observableArrayList();

        // Cargar repartidores desde listaPersonas
        for (Persona persona : empresa.getListaPersonas()) {
            if (persona instanceof Repartidor) {
                Repartidor repartidor = (Repartidor) persona;
                repartidoresData.add(crearRepartidorTableData(repartidor));
            }
        }

        tableRepartidores.setItems(repartidoresData);
        actualizarContador();
    }

    private RepartidorTableData crearRepartidorTableData(Repartidor repartidor) {
        return new RepartidorTableData(
                repartidor.getId(),
                repartidor.getNombre(),
                repartidor.getTelefono(),
                repartidor.getEstadoDisponibilidad() != null ? repartidor.getEstadoDisponibilidad().toString() : "INACTIVO",
                repartidor.getZonaCobertura() != null ? repartidor.getZonaCobertura() : "No definida",
                repartidor.getLocalidad() != null ? repartidor.getLocalidad() : "No definida",
                repartidor.getEnviosAsignados() != null ? repartidor.getEnviosAsignados().size() : 0
        );
    }

    private void actualizarContador() {
        if (lblTotalRepartidores != null) {
            lblTotalRepartidores.setText(String.valueOf(repartidoresData.size()));
        }
    }

    @FXML
    private void buscarRepartidores() {
        String textoBusqueda = txtBuscar.getText().toLowerCase();
        String estadoFiltro = cbFiltroEstado.getValue();

        ObservableList<RepartidorTableData> filtrados = FXCollections.observableArrayList();

        for (RepartidorTableData data : repartidoresData) {
            boolean coincideBusqueda = data.getNombre().toLowerCase().contains(textoBusqueda) ||
                    data.getTelefono().contains(textoBusqueda) ||
                    data.getZonaCobertura().toLowerCase().contains(textoBusqueda) ||
                    data.getLocalidad().toLowerCase().contains(textoBusqueda);

            boolean coincideEstado = estadoFiltro.equals("Todos") || data.getEstado().equals(estadoFiltro);

            if (coincideBusqueda && coincideEstado) {
                filtrados.add(data);
            }
        }

        tableRepartidores.setItems(filtrados);
        actualizarContadorFiltrado(filtrados.size());
    }

    private void actualizarContadorFiltrado(int cantidad) {
        if (lblTotalRepartidores != null) {
            lblTotalRepartidores.setText(cantidad + "/" + repartidoresData.size());
        }
    }

    @FXML
    private void limpiarFiltros() {
        txtBuscar.clear();
        cbFiltroEstado.setValue("Todos");
        tableRepartidores.setItems(repartidoresData);
        actualizarContador();
    }

    @FXML
    private void abrirCrearRepartidor() {
        try {
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            SceneManager.cambiarEscena(stage, "view/CrearEditarRepartidorView.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana de creación: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirEditarRepartidor() {
        RepartidorTableData seleccionado = tableRepartidores.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                // ✅ AHORA FUNCIONA: Usar SceneManager.setData()
                SceneManager.setData("repartidorId", seleccionado.getId());
                Stage stage = (Stage) btnVolver.getScene().getWindow();
                SceneManager.cambiarEscena(stage, "view/CrearEditarRepartidorView.fxml");
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo abrir la ventana de edición: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor selecciona un repartidor para editar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void eliminarRepartidor() {
        RepartidorTableData seleccionado = tableRepartidores.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de eliminar este repartidor?");
            confirmacion.setContentText("Repartidor: " + seleccionado.getNombre() + "\nID: " + seleccionado.getId());

            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        empresa.eliminarRepartidor(seleccionado.getId());
                        cargarRepartidores(); // Recargar la tabla
                        mostrarAlerta("Éxito", "Repartidor eliminado correctamente", Alert.AlertType.INFORMATION);
                    } catch (Exception e) {
                        mostrarAlerta("Error", "Error al eliminar repartidor: " + e.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            });
        } else {
            mostrarAlerta("Selección requerida", "Por favor selecciona un repartidor para eliminar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void abrirCambiarEstado() {
        RepartidorTableData seleccionado = tableRepartidores.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            mostrarAlerta("Funcionalidad en desarrollo",
                    "Cambio de estado para: " + seleccionado.getNombre() +
                            "\n\nEstado actual: " + seleccionado.getEstado() +
                            "\n\nEsta funcionalidad estará disponible pronto.",
                    Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Selección requerida", "Por favor selecciona un repartidor para cambiar estado", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void abrirVerEnvios() {
        RepartidorTableData seleccionado = tableRepartidores.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            mostrarAlerta("Funcionalidad en desarrollo",
                    "Ver envíos para: " + seleccionado.getNombre() +
                            "\n\nEnviós asignados: " + seleccionado.getEnviosAsignados() +
                            "\n\nEsta funcionalidad estará disponible pronto.",
                    Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Selección requerida", "Por favor selecciona un repartidor para ver envíos", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void volver() {
        try {
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            SceneManager.cambiarEscena(stage, "view/MenuPrincipalView.fxml"); // Ajusta según tu menú principal
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo volver al menú principal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Clase interna para datos de la tabla
    public static class RepartidorTableData {
        private final String id;
        private final String nombre;
        private final String telefono;
        private final String estado;
        private final String zonaCobertura;
        private final String localidad;
        private final int enviosAsignados;

        public RepartidorTableData(String id, String nombre, String telefono, String estado,
                                   String zonaCobertura, String localidad, int enviosAsignados) {
            this.id = id;
            this.nombre = nombre;
            this.telefono = telefono;
            this.estado = estado;
            this.zonaCobertura = zonaCobertura;
            this.localidad = localidad;
            this.enviosAsignados = enviosAsignados;
        }

        // Getters
        public String getId() { return id; }
        public String getNombre() { return nombre; }
        public String getTelefono() { return telefono; }
        public String getEstado() { return estado; }
        public String getZonaCobertura() { return zonaCobertura; }
        public String getLocalidad() { return localidad; }
        public int getEnviosAsignados() { return enviosAsignados; }
    }
}
