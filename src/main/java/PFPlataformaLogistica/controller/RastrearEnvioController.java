package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RastrearEnvioController implements Initializable {

    @FXML private TextField txtIdEnvio;
    @FXML private Button btnVolver;

    @FXML private VBox vboxResultado;
    @FXML private VBox vboxSinResultado;

    @FXML private Label lblIdEnvio;
    @FXML private Label lblEstadoActual;
    @FXML private Label lblFechaCreacion;
    @FXML private Label lblFechaEstimada;
    @FXML private Label lblOrigen;
    @FXML private Label lblDestino;
    @FXML private Label lblRepartidor;
    @FXML private Label lblPeso;
    @FXML private Label lblTipoEnvio;
    @FXML private Label lblCosto;

    // Timeline labels
    @FXML private Label lblFechaSolicitado;
    @FXML private Label lblFechaAsignado;
    @FXML private Label lblFechaEnRuta;
    @FXML private Label lblFechaEntregado;

    @FXML private HBox hboxSolicitado;
    @FXML private HBox hboxAsignado;
    @FXML private HBox hboxEnRuta;
    @FXML private HBox hboxEntregado;

    private Empresa empresa;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        empresa = Empresa.getInstance();
    }

    @FXML
    private void buscarEnvio() {
        String idEnvio = txtIdEnvio.getText().trim();

        if (idEnvio.isEmpty()) {
            mostrarAlerta("Validación", "Por favor ingresa el ID del envío", Alert.AlertType.WARNING);
            return;
        }

        Envio envio = empresa.buscarEnvioPorId(idEnvio);

        if (envio == null) {
            mostrarAlerta("No Encontrado",
                    "No se encontró ningún envío con el ID: " + idEnvio,
                    Alert.AlertType.WARNING);
            vboxResultado.setVisible(false);
            vboxSinResultado.setVisible(true);
            return;
        }

        // Mostrar resultado
        vboxSinResultado.setVisible(false);
        vboxResultado.setVisible(true);

        // Actualizar información básica
        lblIdEnvio.setText(envio.getIdEnvio());
        lblEstadoActual.setText(envio.getEstadoEnvio().toString());
        lblFechaCreacion.setText(envio.getFechaCreacion());
        lblFechaEstimada.setText(envio.getFechaEstimada());
        lblPeso.setText(envio.getPesoEnvio() + " kg");
        lblTipoEnvio.setText(envio.getTipoEnvio().toString());

        // Direcciones
        if (envio.getListaDirecciones() != null && envio.getListaDirecciones().size() >= 2) {
            Object objOrigen = envio.getListaDirecciones().get(0);
            Object objDestino = envio.getListaDirecciones().get(1);

            if (objOrigen instanceof Direccion) {
                Direccion origen = (Direccion) objOrigen;
                lblOrigen.setText(origen.getCalle() + ", " + origen.getCiudad());
            }

            if (objDestino instanceof Direccion) {
                Direccion destino = (Direccion) objDestino;
                lblDestino.setText(destino.getCalle() + ", " + destino.getCiudad());
            }
        }

        // Repartidor
        if (envio.getRepartidor() != null) {
            lblRepartidor.setText(envio.getRepartidor().getNombre() +
                    " - Tel: " + envio.getRepartidor().getTelefono());
        } else {
            lblRepartidor.setText("Sin asignar");
        }

        // Costo
        if (envio.getTarifa() != null) {
            float costoTotal = envio.getTarifa().getBase() +
                    envio.getTarifa().getPeso() +
                    envio.getTarifa().getVolumen() +
                    envio.getTarifa().getRecargo();
            lblCosto.setText(String.format("$%.2f", costoTotal));
        }

        // Actualizar timeline según el estado
        actualizarTimeline(envio.getEstadoEnvio(), envio.getFechaCreacion());

        // Cambiar color del estado según el tipo
        actualizarColorEstado(envio.getEstadoEnvio());
    }

    private void actualizarTimeline(EstadoEnvio estado, String fecha) {
        // Resetear todos
        resetearTimeline();

        // Activar según el estado
        activarEstadoTimeline(hboxSolicitado, lblFechaSolicitado, fecha);

        if (estado == EstadoEnvio.ASIGNADO || estado == EstadoEnvio.ENRUTA ||
                estado == EstadoEnvio.ENTREGADO) {
            activarEstadoTimeline(hboxAsignado, lblFechaAsignado, fecha);
        }

        if (estado == EstadoEnvio.ENRUTA || estado == EstadoEnvio.ENTREGADO) {
            activarEstadoTimeline(hboxEnRuta, lblFechaEnRuta, fecha);
        }

        if (estado == EstadoEnvio.ENTREGADO) {
            activarEstadoTimeline(hboxEntregado, lblFechaEntregado, fecha);
        }
    }

    private void resetearTimeline() {
        resetearEstadoTimeline(hboxSolicitado, lblFechaSolicitado);
        resetearEstadoTimeline(hboxAsignado, lblFechaAsignado);
        resetearEstadoTimeline(hboxEnRuta, lblFechaEnRuta);
        resetearEstadoTimeline(hboxEntregado, lblFechaEntregado);
    }

    private void resetearEstadoTimeline(HBox hbox, Label lblFecha) {
        if (hbox.getChildren().size() > 0 && hbox.getChildren().get(0) instanceof Label) {
            Label circulo = (Label) hbox.getChildren().get(0);
            circulo.setText("○");
            circulo.setStyle("-fx-font-size: 20px; -fx-text-fill: #BDBDBD;");
        }
        lblFecha.setText("Pendiente");
    }

    private void activarEstadoTimeline(HBox hbox, Label lblFecha, String fecha) {
        if (hbox.getChildren().size() > 0 && hbox.getChildren().get(0) instanceof Label) {
            Label circulo = (Label) hbox.getChildren().get(0);
            circulo.setText("●");
            circulo.setStyle("-fx-font-size: 20px; -fx-text-fill: #4CAF50;");
        }
        lblFecha.setText(fecha);
    }

    private void actualizarColorEstado(EstadoEnvio estado) {
        switch (estado) {
            case SOLICITADO:
                lblEstadoActual.setStyle("-fx-font-size: 24px; -fx-text-fill: #FFA000; -fx-font-weight: bold;");
                break;
            case ASIGNADO:
                lblEstadoActual.setStyle("-fx-font-size: 24px; -fx-text-fill: #2196F3; -fx-font-weight: bold;");
                break;
            case ENRUTA:
                lblEstadoActual.setStyle("-fx-font-size: 24px; -fx-text-fill: #FF6F00; -fx-font-weight: bold;");
                break;
            case ENTREGADO:
                lblEstadoActual.setStyle("-fx-font-size: 24px; -fx-text-fill: #4CAF50; -fx-font-weight: bold;");
                break;
            case INCIDENCIA:
                lblEstadoActual.setStyle("-fx-font-size: 24px; -fx-text-fill: #D32F2F; -fx-font-weight: bold;");
                break;
        }
    }

    @FXML
    private void volver() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "EnviosUsuarioView.fxml");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
