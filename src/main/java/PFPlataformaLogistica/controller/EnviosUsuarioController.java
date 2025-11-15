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

public class EnviosUsuarioController implements Initializable {

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

    @FXML private Button btnNuevoEnvio;
    @FXML private Button btnRastrear;
    @FXML private Button btnVolver;

    private Empresa empresa;
    private Usuario usuarioActual;
    private ObservableList<EnvioTableData> enviosData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        empresa = Empresa.getInstance();

        // Obtener usuario actual
        Persona persona = SesionManager.getPersonaActual();
        if (persona instanceof Usuario) {
            usuarioActual = (Usuario) persona;
        } else {
            mostrarAlerta("Error", "No hay sesión de usuario activa", Alert.AlertType.ERROR);
            return;
        }

        // Configurar tabla
        configurarTabla();

        // Configurar ComboBox de estados
        cbEstado.setItems(FXCollections.observableArrayList(
                "Todos", "SOLICITADO", "ASIGNADO", "ENRUTA", "ENTREGADO", "INCIDENCIA"
        ));
        cbEstado.setValue("Todos");

        // Cargar envíos
        cargarEnvios();
        actualizarEstadisticas();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEnvio"));
        colFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        colFechaEstimada.setCellValueFactory(new PropertyValueFactory<>("fechaEstimada"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("origen"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("destino"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colRepartidor.setCellValueFactory(new PropertyValueFactory<>("repartidor"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));

        // Estilizar columna de estado con colores
        colEstado.setCellFactory(column -> new TableCell<EnvioTableData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
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
                    }
                }
            }
        });
    }

    private void cargarEnvios() {
        enviosData = FXCollections.observableArrayList();

        List<Envio> envios = empresa.obtenerEnviosPorUsuario(usuarioActual);

        if (envios != null && !envios.isEmpty()) {
            for (Envio envio : envios) {
                String origen = obtenerDireccionTexto(envio, 0);
                String destino = obtenerDireccionTexto(envio, 1);
                String repartidor = envio.getRepartidor() != null ?
                        envio.getRepartidor().getNombre() : "Sin asignar";
                String costo = envio.getTarifa() != null ?
                        String.format("$%.2f", calcularCostoTotal(envio.getTarifa())) : "N/A";

                enviosData.add(new EnvioTableData(
                        envio.getIdEnvio(),
                        envio.getFechaCreacion(),
                        envio.getFechaEstimada(),
                        origen,
                        destino,
                        envio.getEstadoEnvio().toString(),
                        repartidor,
                        costo
                ));
            }
        }

        tableEnvios.setItems(enviosData);
    }

    private String obtenerDireccionTexto(Envio envio, int indice) {
        if (envio.getListaDirecciones() != null && envio.getListaDirecciones().size() > indice) {
            Object obj = envio.getListaDirecciones().get(indice);
            if (obj instanceof Direccion) {
                Direccion dir = (Direccion) obj;
                return dir.getCalle() + ", " + dir.getCiudad();
            }
        }
        return "N/A";
    }

    private float calcularCostoTotal(Tarifa tarifa) {
        return tarifa.getBase() + tarifa.getPeso() + tarifa.getVolumen() + tarifa.getRecargo();
    }

    @FXML
    private void aplicarFiltros() {
        String estadoSeleccionado = cbEstado.getValue();
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();

        EstadoEnvio estadoFiltro = null;
        if (estadoSeleccionado != null && !estadoSeleccionado.equals("Todos")) {
            estadoFiltro = EstadoEnvio.valueOf(estadoSeleccionado);
        }

        String fechaInicioStr = fechaInicio != null ? fechaInicio.format(DateTimeFormatter.ISO_DATE) : null;
        String fechaFinStr = fechaFin != null ? fechaFin.format(DateTimeFormatter.ISO_DATE) : null;

        List<Envio> enviosFiltrados = empresa.filtrarEnvios(fechaInicioStr, fechaFinStr, estadoFiltro, null);

        // Filtrar por usuario
        enviosData.clear();
        for (Envio envio : enviosFiltrados) {
            if (usuarioActual.getEnviosPropios().contains(envio)) {
                String origen = obtenerDireccionTexto(envio, 0);
                String destino = obtenerDireccionTexto(envio, 1);
                String repartidor = envio.getRepartidor() != null ?
                        envio.getRepartidor().getNombre() : "Sin asignar";
                String costo = envio.getTarifa() != null ?
                        String.format("$%.2f", calcularCostoTotal(envio.getTarifa())) : "N/A";

                enviosData.add(new EnvioTableData(
                        envio.getIdEnvio(),
                        envio.getFechaCreacion(),
                        envio.getFechaEstimada(),
                        origen,
                        destino,
                        envio.getEstadoEnvio().toString(),
                        repartidor,
                        costo
                ));
            }
        }

        actualizarEstadisticas();
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
        int total = enviosData.size();
        int enTransito = 0;
        int entregados = 0;

        for (EnvioTableData data : enviosData) {
            if (data.getEstado().equals("ENRUTA") || data.getEstado().equals("ASIGNADO")) {
                enTransito++;
            } else if (data.getEstado().equals("ENTREGADO")) {
                entregados++;
            }
        }

        lblTotalEnvios.setText(String.valueOf(total));
        lblEnTransito.setText(String.valueOf(enTransito));
        lblEntregados.setText(String.valueOf(entregados));
    }

    @FXML
    private void abrirCrearEnvio() {
        Stage stage = (Stage) btnNuevoEnvio.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "CrearEnvio.fxml");
    }

    @FXML
    private void abrirRastrearEnvio() {
        Stage stage = (Stage) btnRastrear.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "RastrearEnvio.fxml");
    }

    @FXML
    private void volverAlMenu() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "UsuarioView.fxml");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Clase interna para datos de la tabla
    public static class EnvioTableData {
        private String idEnvio;
        private String fechaCreacion;
        private String fechaEstimada;
        private String origen;
        private String destino;
        private String estado;
        private String repartidor;
        private String costo;

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

