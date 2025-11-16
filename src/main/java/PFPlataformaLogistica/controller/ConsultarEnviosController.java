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
import java.util.List;
import java.util.ResourceBundle;

public class ConsultarEnviosController implements Initializable {

    @FXML private TableView<EnvioData> tableEnvios;
    @FXML private TableColumn<EnvioData, String> colId;
    @FXML private TableColumn<EnvioData, String> colFecha;
    @FXML private TableColumn<EnvioData, String> colOrigen;
    @FXML private TableColumn<EnvioData, String> colDestino;
    @FXML private TableColumn<EnvioData, String> colEstado;
    @FXML private TableColumn<EnvioData, String> colCosto;

    @FXML private ComboBox<String> cbEstado;

    @FXML private Label lblTotal;
    @FXML private Label lblEnTransito;
    @FXML private Label lblEntregados;

    @FXML private Button btnNuevoEnvio;
    @FXML private Button btnRastrear;
    @FXML private Button btnVolver;

    private Empresa empresa;
    private Usuario usuarioActual;
    private ObservableList<EnvioData> enviosData;

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
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("origen"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("destino"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));

        // Estilizar columna de estado con colores
        colEstado.setCellFactory(column -> new TableCell<EnvioData, String>() {
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

    private void cargarEnvios() {
        enviosData = FXCollections.observableArrayList();

        // Obtener envíos del usuario
        if (usuarioActual.getEnviosPropios() != null && !usuarioActual.getEnviosPropios().isEmpty()) {
            for (Envio envio : usuarioActual.getEnviosPropios()) {
                String origen = null;
                String destino = null;
                String costo = calcularCosto(envio);

                enviosData.add(new EnvioData(
                        envio.getIdEnvio(),
                        envio.getFechaCreacion(),
                        origen,
                        destino,
                        envio.getEstadoEnvio().toString(),
                        costo
                ));
            }
        }

        tableEnvios.setItems(enviosData);
    }
/*
    private String obtenerCiudad(Envio envio, int indice) {
        if (envio.getListaDirecciones() != null && envio.getListaDirecciones().size() > indice) {
            Object obj = envio.getListaDirecciones().get(indice);
            if (obj instanceof Direccion) {
                Direccion dir = (Direccion) obj;
                return dir.getCiudad();
            }
        }
        return "N/A";
    }
*/
    private String calcularCosto(Envio envio) {
        if (envio.getTarifa() != null) {
            float total = envio.getTarifa().getBase() +
                    envio.getTarifa().getPeso() +
                    envio.getTarifa().getVolumen() +
                    envio.getTarifa().getRecargo();
            return "$" + String.format("%.0f", total);
        }
        return "N/A";
    }

    @FXML
    private void aplicarFiltros() {
        String estadoSeleccionado = cbEstado.getValue();

        if (estadoSeleccionado.equals("Todos")) {
            cargarEnvios();
        } else {
            enviosData.clear();
            if (usuarioActual.getEnviosPropios() != null) {
                for (Envio envio : usuarioActual.getEnviosPropios()) {
                    if (envio.getEstadoEnvio().toString().equals(estadoSeleccionado)) {
                        String origen = null;
                        String destino = null;
                        String costo = calcularCosto(envio);

                        enviosData.add(new EnvioData(
                                envio.getIdEnvio(),
                                envio.getFechaCreacion(),
                                origen,
                                destino,
                                envio.getEstadoEnvio().toString(),
                                costo
                        ));
                    }
                }
            }
        }

        actualizarEstadisticas();
    }

    @FXML
    private void limpiarFiltros() {
        cbEstado.setValue("Todos");
        cargarEnvios();
        actualizarEstadisticas();
    }

    private void actualizarEstadisticas() {
        int total = enviosData.size();
        int enTransito = 0;
        int entregados = 0;

        for (EnvioData data : enviosData) {
            if (data.getEstado().equals("ENRUTA") || data.getEstado().equals("ASIGNADO")) {
                enTransito++;
            } else if (data.getEstado().equals("ENTREGADO")) {
                entregados++;
            }
        }

        lblTotal.setText(String.valueOf(total));
        lblEnTransito.setText(String.valueOf(enTransito));
        lblEntregados.setText(String.valueOf(entregados));
    }

    @FXML
    private void abrirNuevoEnvio() {
        Stage stage = (Stage) btnNuevoEnvio.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "CrearEnvio.fxml");
    }

    @FXML
    private void abrirRastrear() {
        Stage stage = (Stage) btnRastrear.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "RastrearEnvio.fxml");
    }

    @FXML
    private void volverAlPanel() {
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
    public static class EnvioData {
        private String idEnvio;
        private String fecha;
        private String origen;
        private String destino;
        private String estado;
        private String costo;

        public EnvioData(String idEnvio, String fecha, String origen,
                         String destino, String estado, String costo) {
            this.idEnvio = idEnvio;
            this.fecha = fecha;
            this.origen = origen;
            this.destino = destino;
            this.estado = estado;
            this.costo = costo;
        }

        // Getters
        public String getIdEnvio() { return idEnvio; }
        public String getFecha() { return fecha; }
        public String getOrigen() { return origen; }
        public String getDestino() { return destino; }
        public String getEstado() { return estado; }
        public String getCosto() { return costo; }
    }
}
