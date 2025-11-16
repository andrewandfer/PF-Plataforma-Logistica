package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.Utils.SesionManager;
import PFPlataformaLogistica.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EnviosUsuarioController implements Initializable {

    // UI Components
    @FXML private TableView<EnvioTableData> tableEnvios;
    @FXML private TableColumn<EnvioTableData, String> colId;
    @FXML private TableColumn<EnvioTableData, String> colFechaCreacion;
    @FXML private TableColumn<EnvioTableData, String> colFechaEstimada;
    @FXML private TableColumn<EnvioTableData, String> colOrigen;
    @FXML private TableColumn<EnvioTableData, String> colDestino;
    @FXML private TableColumn<EnvioTableData, String> colEstado;
    @FXML private TableColumn<EnvioTableData, String> colRepartidor;
    @FXML private TableColumn<EnvioTableData, String> colCosto;

    @FXML private ComboBox<String> cbEstado;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;

    @FXML private Label lblTotalEnvios;
    @FXML private Label lblEnTransito;
    @FXML private Label lblEntregados;

    @FXML private Button btnVolver;

    // Dependencies
    private Empresa empresa;
    private Usuario usuarioActual;
    private ObservableList<EnvioTableData> enviosData;
    private final EnvioTableDataMapper tableDataMapper = new EnvioTableDataMapper();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeDependencies();
        setupUIComponents();
        loadUserData();
    }

    private void initializeDependencies() {
        empresa = Empresa.getInstance();
        usuarioActual = obtenerUsuarioActual();
        enviosData = FXCollections.observableArrayList();
    }

    private Usuario obtenerUsuarioActual() {
        Persona persona = SesionManager.getPersonaActual();
        if (persona instanceof Usuario) {
            return (Usuario) persona;
        }
        mostrarErrorSesion();
        return null;
    }

    private void mostrarErrorSesion() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("No hay sesión de usuario activa");
        alert.showAndWait();
    }

    private void setupUIComponents() {
        configureTable();
        configureFilters();
    }

    private void loadUserData() {
        if (usuarioActual != null) {
            cargarEnvios();
            actualizarEstadisticas();
        }
    }

    private void configureTable() {
        setupCellValueFactories();
        setupEstadoCellFactory();
    }

    private void setupCellValueFactories() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEnvio"));
        colFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        colFechaEstimada.setCellValueFactory(new PropertyValueFactory<>("fechaEstimada"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("origen"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("destino"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colRepartidor.setCellValueFactory(new PropertyValueFactory<>("repartidor"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));
    }

    private void setupEstadoCellFactory() {
        colEstado.setCellFactory(column -> new EstadoStyledTableCell());
    }

    private void configureFilters() {
        cbEstado.setItems(FXCollections.observableArrayList(
                "Todos", "SOLICITADO", "ASIGNADO", "ENRUTA", "ENTREGADO", "INCIDENCIA"
        ));
        cbEstado.setValue("Todos");
    }

    private void cargarEnvios() {
        List<Envio> envios = empresa.obtenerEnviosPorUsuario(usuarioActual);
        enviosData.setAll(convertToTableData(envios));
        tableEnvios.setItems(enviosData);
    }

    private List<EnvioTableData> convertToTableData(List<Envio> envios) {
        return envios.stream()
                .map(tableDataMapper::toTableData)
                .collect(Collectors.toList());
    }

    @FXML
    private void aplicarFiltros() {
        FiltroEnvios filtro = buildFiltro();
        List<Envio> enviosFiltrados = filtrarEnviosLocalmente(filtro);

        enviosData.setAll(convertToTableData(enviosFiltrados));
        actualizarEstadisticas();
    }

    private List<Envio> filtrarEnviosLocalmente(FiltroEnvios filtro) {
        List<Envio> todosEnvios = empresa.obtenerEnviosPorUsuario(usuarioActual);

        return todosEnvios.stream()
                .filter(envio -> filtrarPorEstado(envio, filtro.estado()))
                .filter(envio -> filtrarPorFecha(envio, filtro.fechaInicio(), filtro.fechaFin()))
                .collect(Collectors.toList());
    }

    private boolean filtrarPorEstado(Envio envio, EstadoEnvio estadoFiltro) {
        if (estadoFiltro == null) {
            return true;
        }
        return envio.getEstadoEnvio() == estadoFiltro;
    }

    private boolean filtrarPorFecha(Envio envio, String fechaInicio, String fechaFin) {
        if (fechaInicio == null && fechaFin == null) {
            return true;
        }

        String fechaCreacion = envio.getFechaCreacion();
        if (fechaCreacion == null) {
            return false;
        }

        boolean pasaFiltro = true;

        if (fechaInicio != null) {
            pasaFiltro = fechaCreacion.compareTo(fechaInicio) >= 0;
        }

        if (fechaFin != null) {
            pasaFiltro = pasaFiltro && fechaCreacion.compareTo(fechaFin) <= 0;
        }

        return pasaFiltro;
    }

    private FiltroEnvios buildFiltro() {
        String estadoSeleccionado = cbEstado.getValue();
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();

        EstadoEnvio estadoFiltro = null;
        if (estadoSeleccionado != null && !estadoSeleccionado.equals("Todos")) {
            estadoFiltro = EstadoEnvio.valueOf(estadoSeleccionado);
        }

        String fechaInicioStr = fechaInicio != null ? fechaInicio.format(DateTimeFormatter.ISO_DATE) : null;
        String fechaFinStr = fechaFin != null ? fechaFin.format(DateTimeFormatter.ISO_DATE) : null;

        return new FiltroEnvios(fechaInicioStr, fechaFinStr, estadoFiltro);
    }

    @FXML
    private void limpiarFiltros() {
        cbEstado.setValue("Todos");
        dpFechaInicio.setValue(null);
        dpFechaFin.setValue(null);
        cargarEnvios();
        actualizarEstadisticas();
    }

    private void actualizarEstadisticas() {
        EstadisticasEnvios estadisticas = calcularEstadisticas();
        actualizarLabelsEstadisticas(estadisticas);
    }

    private EstadisticasEnvios calcularEstadisticas() {
        int total = enviosData.size();
        int enTransito = (int) enviosData.stream()
                .filter(data -> data.getEstado().equals("ENRUTA") || data.getEstado().equals("ASIGNADO"))
                .count();
        int entregados = (int) enviosData.stream()
                .filter(data -> data.getEstado().equals("ENTREGADO"))
                .count();

        return new EstadisticasEnvios(total, enTransito, entregados);
    }

    private void actualizarLabelsEstadisticas(EstadisticasEnvios estadisticas) {
        lblTotalEnvios.setText(String.valueOf(estadisticas.total()));
        lblEnTransito.setText(String.valueOf(estadisticas.enTransito()));
        lblEntregados.setText(String.valueOf(estadisticas.entregados()));
    }

    @FXML
    private void abrirCrearEnvio() {
        navigateToScene("CrearEnvio.fxml");
    }

    @FXML
    private void abrirRastrearEnvio() {
        navigateToScene("RastrearEnvio.fxml");
    }

    @FXML
    private void volverAlMenu() {
        navigateToScene("UsuarioView.fxml");
    }

    private void navigateToScene(String fxmlFile) {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        SceneManager.cambiarEscena(stage, fxmlFile);
    }

    // Records para inmutabilidad y claridad
    private record FiltroEnvios(String fechaInicio, String fechaFin, EstadoEnvio estado) {}
    private record EstadisticasEnvios(int total, int enTransito, int entregados) {}

    // Mapper para convertir Envio a EnvioTableData
    private class EnvioTableDataMapper {
        public EnvioTableData toTableData(Envio envio) {
            return new EnvioTableData(
                    envio.getIdEnvio(),
                    envio.getFechaCreacion(),
                    envio.getFechaEstimada(),
                    obtenerOrigen(envio),
                    obtenerDestino(envio),
                    envio.getEstadoEnvio().toString(),
                    obtenerNombreRepartidor(envio),
                    formatearCosto(envio)
            );
        }

        private String obtenerOrigen(Envio envio) {
            return envio.getOrigen() != null ? envio.getOrigen() : "N/A";
        }

        private String obtenerDestino(Envio envio) {
            return envio.getDestino() != null ? envio.getDestino() : "N/A";
        }

        private String obtenerNombreRepartidor(Envio envio) {
            return envio.getRepartidor() != null ? envio.getRepartidor().getNombre() : "Sin asignar";
        }

        private String formatearCosto(Envio envio) {
            return envio.getCosto() > 0 ? String.format("$%,d", envio.getCosto()) : "N/A";
        }
    }

    // TableCell personalizada para estilizar estados
    private class EstadoStyledTableCell extends TableCell<EnvioTableData, String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setStyle("");
            } else {
                setText(item);
                aplicarEstiloEstado(item);
            }
        }

        private void aplicarEstiloEstado(String estado) {
            switch (estado) {
                case "SOLICITADO":
                    setStyle("-fx-background-color: #FFF9C4; -fx-text-fill: #F57F17;");
                    break;
                case "ASIGNADO":
                    setStyle("-fx-background-color: #BBDEFB; -fx-text-fill: #1565C0;");
                    break;
                case "ENRUTA":
                    setStyle("-fx-background-color: #FFE0B2; -fx-text-fill: #E65100;");
                    break;
                case "ENTREGADO":
                    setStyle("-fx-background-color: #C8E6C9; -fx-text-fill: #2E7D32;");
                    break;
                case "INCIDENCIA":
                    setStyle("-fx-background-color: #FFCDD2; -fx-text-fill: #C62828;");
                    break;
                default:
                    setStyle("");
            }
        }
    }

    // Clase interna para datos de la tabla
    public static class EnvioTableData {
        private final String idEnvio;
        private final String fechaCreacion;
        private final String fechaEstimada;
        private final String origen;
        private final String destino;
        private final String estado;
        private final String repartidor;
        private final String costo;

        public EnvioTableData(String idEnvio, String fechaCreacion, String fechaEstimada,
                              String origen, String destino, String estado, String repartidor, String costo) {
            this.idEnvio = idEnvio;
            this.fechaCreacion = fechaCreacion;
            this.fechaEstimada = fechaEstimada;
            this.origen = origen;
            this.destino = destino;
            this.estado = estado;
            this.repartidor = repartidor;
            this.costo = costo;
        }

        // Getters
        public String getIdEnvio() { return idEnvio; }
        public String getFechaCreacion() { return fechaCreacion; }
        public String getFechaEstimada() { return fechaEstimada; }
        public String getOrigen() { return origen; }
        public String getDestino() { return destino; }
        public String getEstado() { return estado; }
        public String getRepartidor() { return repartidor; }
        public String getCosto() { return costo; }
    }
}