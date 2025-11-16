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

    @FXML private Label lblIdEnvio, lblEstadoActual, lblFechaCreacion, lblFechaEstimada;
    @FXML private Label lblOrigen, lblDestino, lblRepartidor, lblPeso, lblTipoEnvio, lblCosto;

    // Timeline labels
    @FXML private Label lblFechaSolicitado, lblFechaAsignado, lblFechaEnRuta, lblFechaEntregado;

    @FXML private HBox hboxSolicitado, hboxAsignado, hboxEnRuta, hboxEntregado;

    private Empresa empresa;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        empresa = Empresa.getInstance();
        // Asegurar que los VBox estén inicializados correctamente
        vboxResultado.setVisible(false);
        vboxSinResultado.setVisible(true);
    }

    // ============================================================
    // EVENTOS
    // ============================================================

    @FXML
    private void buscarEnvio() {
        String id = txtIdEnvio.getText().trim();

        if (id.isEmpty()) {
            mostrarAlertaValidacion("Por favor ingresa el ID del envío");
            return;
        }

        Envio envio = empresa.buscarEnvioPorId(id);

        if (envio == null) {
            mostrarEnvioNoEncontrado(id);
            return;
        }

        mostrarEnvioEncontrado(envio);
    }

    @FXML
    private void volver() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "EnviosUsuarioView.fxml");
    }

    // ============================================================
    // LÓGICA PRINCIPAL
    // ============================================================

    private void mostrarEnvioNoEncontrado(String id) {
        mostrarAlertaAdvertencia("No se encontró ningún envío con el ID: " + id);
        vboxResultado.setVisible(false);
        vboxSinResultado.setVisible(true);
    }

    private void mostrarEnvioEncontrado(Envio envio) {
        vboxSinResultado.setVisible(false);
        vboxResultado.setVisible(true);

        cargarDatosBasicos(envio);
        cargarDirecciones(envio);
        cargarRepartidor(envio);
        cargarCosto(envio);

        actualizarTimeline(envio.getEstadoEnvio(), envio.getFechaCreacion());
        actualizarColorEstado(envio.getEstadoEnvio());
    }

    // ============================================================
    // SECCIONES DEL ENVÍO
    // ============================================================

    private void cargarDatosBasicos(Envio envio) {
        lblIdEnvio.setText(envio.getIdEnvio());
        lblEstadoActual.setText(envio.getEstadoEnvio().toString());
        lblFechaCreacion.setText(envio.getFechaCreacion());
        lblFechaEstimada.setText(envio.getFechaEstimada());
        lblPeso.setText(envio.getPesoEnvio() + " kg");
        lblTipoEnvio.setText(envio.getTipoEnvio().toString());
    }

    private void cargarDirecciones(Envio envio) {
        // CORRECCIÓN: Usar los campos origen y destino de la clase Envio
        String origen = envio.getOrigen();
        String destino = envio.getDestino();
        Direccion direccionEntrega = envio.getDireccion();

        if (origen != null && !origen.isEmpty()) {
            lblOrigen.setText(origen);
        } else {
            lblOrigen.setText("No especificado");
        }

        if (destino != null && !destino.isEmpty()) {
            lblDestino.setText(destino);
        } else if (direccionEntrega != null) {
            // Usar la dirección de entrega como fallback
            lblDestino.setText(direccionEntrega.getCalle() + ", " + direccionEntrega.getCiudad());
        } else {
            lblDestino.setText("No especificado");
        }
    }

    private void cargarRepartidor(Envio envio) {
        if (envio.getRepartidor() == null) {
            lblRepartidor.setText("Sin asignar");
            return;
        }

        Repartidor repartidor = envio.getRepartidor();
        String telefono = repartidor.getTelefono() != null ? repartidor.getTelefono() : "No disponible";
        lblRepartidor.setText(repartidor.getNombre() + " - Tel: " + telefono);
    }

    private void cargarCosto(Envio envio) {
        if (envio.getTarifa() == null) {
            lblCosto.setText("No disponible");
            return;
        }

        // CORRECCIÓN: Usar el costo directo del envío en lugar de calcularlo
        int costo = envio.getCosto();
        if (costo > 0) {
            lblCosto.setText(String.format("$%,d", costo));
        } else {
            // Fallback: intentar calcular desde la tarifa si el costo es 0
            float total = calcularCostoDesdeTarifa(envio.getTarifa());
            lblCosto.setText(String.format("$%.2f", total));
        }
    }

    private float calcularCostoDesdeTarifa(Tarifa tarifa) {
        // Método auxiliar para calcular costo desde tarifa si es necesario
        try {
            return tarifa.getBase() + tarifa.getPeso() + tarifa.getVolumen() + tarifa.getRecargo();
        } catch (Exception e) {
            return 0f;
        }
    }

    // ============================================================
    // TIMELINE
    // ============================================================

    private void actualizarTimeline(EstadoEnvio estado, String fechaCreacion) {
        resetearTimeline();
        activarEstadoTimeline(hboxSolicitado, lblFechaSolicitado, fechaCreacion);

        if (estado.ordinal() >= EstadoEnvio.ASIGNADO.ordinal()) {
            activarEstadoTimeline(hboxAsignado, lblFechaAsignado, fechaCreacion);
        }

        if (estado.ordinal() >= EstadoEnvio.ENRUTA.ordinal()) {
            activarEstadoTimeline(hboxEnRuta, lblFechaEnRuta, fechaCreacion);
        }

        if (estado == EstadoEnvio.ENTREGADO) {
            activarEstadoTimeline(hboxEntregado, lblFechaEntregado, fechaCreacion);
        }
    }

    private void resetearTimeline() {
        resetearEstadoTimeline(hboxSolicitado, lblFechaSolicitado);
        resetearEstadoTimeline(hboxAsignado, lblFechaAsignado);
        resetearEstadoTimeline(hboxEnRuta, lblFechaEnRuta);
        resetearEstadoTimeline(hboxEntregado, lblFechaEntregado);
    }

    private void resetearEstadoTimeline(HBox hbox, Label labelFecha) {
        cambiarIconoTimeline(hbox, "○", "#BDBDBD");
        labelFecha.setText("Pendiente");
    }

    private void activarEstadoTimeline(HBox hbox, Label labelFecha, String fecha) {
        cambiarIconoTimeline(hbox, "●", "#4CAF50");
        labelFecha.setText(fecha);
    }

    private void cambiarIconoTimeline(HBox hbox, String simbolo, String color) {
        if (!hbox.getChildren().isEmpty() && hbox.getChildren().getFirst() instanceof Label icono) {
            icono.setText(simbolo);
            icono.setStyle("-fx-font-size: 20px; -fx-text-fill: " + color + ";");
        }
    }

    // ============================================================
    // ESTILO
    // ============================================================

    private void actualizarColorEstado(EstadoEnvio estado) {
        String color = switch (estado) {
            case SOLICITADO -> "#FFA000";
            case ASIGNADO -> "#2196F3";
            case ENRUTA -> "#FF6F00";
            case ENTREGADO -> "#4CAF50";
            case INCIDENCIA -> "#D32F2F";
            default -> "#666666";
        };

        lblEstadoActual.setStyle(String.format("""
                -fx-font-size: 24px;
                -fx-text-fill: %s;
                -fx-font-weight: bold;
                """, color));
    }

    // ============================================================
    // UTILIDADES - MÉTODOS DE ALERTA MEJORADOS
    // ============================================================

    private void mostrarAlertaValidacion(String mensaje) {
        mostrarAlerta("Validación", mensaje, Alert.AlertType.WARNING);
    }

    private void mostrarAlertaAdvertencia(String mensaje) {
        mostrarAlerta("No Encontrado", mensaje, Alert.AlertType.WARNING);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}