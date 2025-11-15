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
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminEnviosController implements Initializable {

    @FXML private TableView<EnvioAdminData> tableEnvios;
    @FXML private TableColumn<EnvioAdminData, String> colId;
    @FXML private TableColumn<EnvioAdminData, String> colFechaCreacion;
    @FXML private TableColumn<EnvioAdminData, String> colOrigen;
    @FXML private TableColumn<EnvioAdminData, String> colDestino;
    @FXML private TableColumn<EnvioAdminData, String> colEstado;
    @FXML private TableColumn<EnvioAdminData, String> colRepartidor;
    @FXML private TableColumn<EnvioAdminData, String> colPeso;

    @FXML private ComboBox<String> cbFiltroEstado;
    @FXML private ComboBox<String> cbRepartidores;
    @FXML private ComboBox<String> cbNuevoEstado;

    @FXML private Label lblEnvioSeleccionado;
    @FXML private Label lblEnvioSeleccionado2;
    @FXML private Label lblTotalEnvios;
    @FXML private Label lblSolicitados;
    @FXML private Label lblAsignados;
    @FXML private Label lblEnRuta;
    @FXML private Label lblEntregados;
    @FXML private Label lblIncidencias;

    @FXML private Button btnActualizar;
    @FXML private Button btnVolver;

    private Empresa empresa;
    private ObservableList<EnvioAdminData> enviosData;
    private Envio envioSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        empresa = Empresa.getInstance();

        // Configurar tabla
        configurarTabla();

        // Configurar ComboBoxes
        cbFiltroEstado.setItems(FXCollections.observableArrayList(
                "Todos", "SOLICITADO", "ASIGNADO", "ENRUTA", "ENTREGADO", "INCIDENCIA"
        ));
        cbFiltroEstado.setValue("Todos");

        cbNuevoEstado.setItems(FXCollections.observableArrayList(
                "SOLICITADO", "ASIGNADO", "ENRUTA", "ENTREGADO", "INCIDENCIA"
        ));

        // Cargar datos
        cargarRepartidores();
        cargarEnvios();
        actualizarEstadisticas();

        // Listener para selección de envío
        tableEnvios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                envioSeleccionado = buscarEnvioReal(newSelection.getIdEnvio());
                lblEnvioSeleccionado.setText(newSelection.getIdEnvio());
                lblEnvioSeleccionado2.setText(newSelection.getIdEnvio());
            }
        });
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEnvio"));
        colFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("origen"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("destino"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colRepartidor.setCellValueFactory(new PropertyValueFactory<>("repartidor"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));

        // Estilizar columna de estado
        colEstado.setCellFactory(column -> new TableCell<EnvioAdminData, String>() {
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
                            setStyle("-fx-background-color: #FFF9C4; -fx-text-fill: #F57F17; -fx-font-weight: bold;");
                            break;
                        case "ASIGNADO":
                            setStyle("-fx-background-color: #BBDEFB; -fx-text-fill: #1565C0; -fx-font-weight: bold;");
                            break;
                        case "ENRUTA":
                            setStyle("-fx-background-color: #FFE0B2; -fx-text-fill: #E65100; -fx-font-weight: bold;");
                            break;
                        case "ENTREGADO":
                            setStyle("-fx-background-color: #C8E6C9; -fx-text-fill: #2E7D32; -fx-font-weight: bold;");
                            break;
                        case "INCIDENCIA":
                            setStyle("-fx-background-color: #FFCDD2; -fx-text-fill: #C62828; -fx-font-weight: bold;");
                            break;
                    }
                }
            }
        });
    }

    private void cargarRepartidores() {
        List<Repartidor> repartidores = empresa.obtenerRepartidoresDisponibles();
        ObservableList<String> items = FXCollections.observableArrayList();

        for (Repartidor r : repartidores) {
            items.add(r.getId() + " - " + r.getNombre() + " (" + r.getZonaCobertura() + ")");
        }

        cbRepartidores.setItems(items);
    }

    private void cargarEnvios() {
        enviosData = FXCollections.observableArrayList();
        LinkedList<Envio> envios = empresa.getListaEnvios();

        if (envios != null && !envios.isEmpty()) {
            for (Envio envio : envios) {
                String origen = obtenerDireccionTexto(envio, 0);
                String destino = obtenerDireccionTexto(envio, 1);
                String repartidor = envio.getRepartidor() != null ?
                        envio.getRepartidor().getNombre() : "Sin asignar";

                enviosData.add(new EnvioAdminData(
                        envio.getIdEnvio(),
                        envio.getFechaCreacion(),
                        origen,
                        destino,
                        envio.getEstadoEnvio().toString(),
                        repartidor,
                        String.valueOf(envio.getPesoEnvio())
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
                return dir.getCiudad();
            }
        }
        return "N/A";
    }

    @FXML
    private void aplicarFiltros() {
        String estadoSeleccionado = cbFiltroEstado.getValue();

        if (estadoSeleccionado.equals("Todos")) {
            cargarEnvios();
        } else {
            enviosData.clear();
            LinkedList<Envio> envios = empresa.getListaEnvios();

            if (envios != null) {
                for (Envio envio : envios) {
                    if (envio.getEstadoEnvio().toString().equals(estadoSeleccionado)) {
                        String origen = obtenerDireccionTexto(envio, 0);
                        String destino = obtenerDireccionTexto(envio, 1);
                        String repartidor = envio.getRepartidor() != null ?
                                envio.getRepartidor().getNombre() : "Sin asignar";

                        enviosData.add(new EnvioAdminData(
                                envio.getIdEnvio(),
                                envio.getFechaCreacion(),
                                origen,
                                destino,
                                envio.getEstadoEnvio().toString(),
                                repartidor,
                                String.valueOf(envio.getPesoEnvio())
                        ));
                    }
                }
            }
        }

        actualizarEstadisticas();
    }

    @FXML
    private void limpiarFiltros() {
        cbFiltroEstado.setValue("Todos");
        cargarEnvios();
        actualizarEstadisticas();
    }

    @FXML
    private void asignarRepartidor() {
        if (envioSeleccionado == null) {
            mostrarAlerta("Validación", "Por favor selecciona un envío de la tabla", Alert.AlertType.WARNING);
            return;
        }

        if (cbRepartidores.getValue() == null) {
            mostrarAlerta("Validación", "Por favor selecciona un repartidor", Alert.AlertType.WARNING);
            return;
        }

        // Extraer ID del repartidor del ComboBox
        String seleccion = cbRepartidores.getValue();
        String idRepartidor = seleccion.split(" - ")[0];

        boolean exito = empresa.asignarRepartidorAEnvio(envioSeleccionado.getIdEnvio(), idRepartidor);

        if (exito) {
            mostrarAlerta("Éxito",
                    "Repartidor asignado correctamente al envío " + envioSeleccionado.getIdEnvio(),
                    Alert.AlertType.INFORMATION);
            actualizarTabla();
        } else {
            mostrarAlerta("Error", "No se pudo asignar el repartidor", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cambiarEstado() {
        if (envioSeleccionado == null) {
            mostrarAlerta("Validación", "Por favor selecciona un envío de la tabla", Alert.AlertType.WARNING);
            return;
        }

        if (cbNuevoEstado.getValue() == null) {
            mostrarAlerta("Validación", "Por favor selecciona un nuevo estado", Alert.AlertType.WARNING);
            return;
        }

        EstadoEnvio nuevoEstado = EstadoEnvio.valueOf(cbNuevoEstado.getValue());
        boolean exito = empresa.cambiarEstadoEnvio(envioSeleccionado.getIdEnvio(), nuevoEstado);

        if (exito) {
            mostrarAlerta("Éxito",
                    "Estado del envío actualizado a: " + nuevoEstado,
                    Alert.AlertType.INFORMATION);
            actualizarTabla();
        } else {
            mostrarAlerta("Error", "No se pudo cambiar el estado del envío", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void actualizarTabla() {
        cargarRepartidores();
        cargarEnvios();
        actualizarEstadisticas();
    }

    private void actualizarEstadisticas() {
        int total = enviosData.size();
        int solicitados = 0, asignados = 0, enRuta = 0, entregados = 0, incidencias = 0;

        for (EnvioAdminData data : enviosData) {
            switch (data.getEstado()) {
                case "SOLICITADO": solicitados++; break;
                case "ASIGNADO": asignados++; break;
                case "ENRUTA": enRuta++; break;
                case "ENTREGADO": entregados++; break;
                case "INCIDENCIA": incidencias++; break;
            }
        }

        lblTotalEnvios.setText(String.valueOf(total));
        lblSolicitados.setText(String.valueOf(solicitados));
        lblAsignados.setText(String.valueOf(asignados));
        lblEnRuta.setText(String.valueOf(enRuta));
        lblEntregados.setText(String.valueOf(entregados));
        lblIncidencias.setText(String.valueOf(incidencias));
    }

    private Envio buscarEnvioReal(String idEnvio) {
        LinkedList<Envio> envios = empresa.getListaEnvios();
        if (envios != null) {
            for (Envio e : envios) {
                if (e.getIdEnvio().equals(idEnvio)) {
                    return e;
                }
            }
        }
        return null;
    }

    @FXML
    private void volver() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        // Cambiar a la vista principal del admin (debes tenerla)
        SceneManager.cambiarEscena(stage, "login.fxml"); // Cambiar por tu vista de admin
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Clase interna para datos de la tabla
    public static class EnvioAdminData {
        private String idEnvio;
        private String fechaCreacion;
        private String origen;
        private String destino;
        private String estado;
        private String repartidor;
        private String peso;

        public EnvioAdminData(String idEnvio, String fechaCreacion, String origen,
                              String destino, String estado, String repartidor, String peso) {
            this.idEnvio = idEnvio;
            this.fechaCreacion = fechaCreacion;
            this.origen = origen;
            this.destino = destino;
            this.estado = estado;
            this.repartidor = repartidor;
            this.peso = peso;
        }

        // Getters
        public String getIdEnvio() { return idEnvio; }
        public String getFechaCreacion() { return fechaCreacion; }
        public String getOrigen() { return origen; }
        public String getDestino() { return destino; }
        public String getEstado() { return estado; }
        public String getRepartidor() { return repartidor; }
        public String getPeso() { return peso; }
    }
}
