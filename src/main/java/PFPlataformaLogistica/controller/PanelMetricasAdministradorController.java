package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.model.Empresa;
import PFPlataformaLogistica.model.PagoRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class PanelMetricasAdministradorController implements Initializable {

    @FXML private PieChart chartServicios;
    @FXML private BarChart<String, Number> chartIngresos;
    @FXML private BarChart<String, Number> chartIncidencias;
    @FXML private LineChart<String, Number> chartTiemposEntrega;

    @FXML private Label lblTotalEnvios, lblIngresosTotales, lblIncidencias, lblRepartidores;
    @FXML private ComboBox<String> cbPeriodo;
    @FXML private Button btnActualizarMetricas, btnVolverAtras, btnVolverMenu;
    @FXML private VBox vboxMensaje;
    @FXML private Label lblMensaje;

    private Empresa empresa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        empresa = Empresa.getInstance();
        configurarComboBox();
        actualizarTodasLasMetricas();
    }

    private void configurarComboBox() {
        // Llenar el ComboBox desde el controlador (más confiable)
        ObservableList<String> periodos = FXCollections.observableArrayList(
                "Última semana",
                "Último mes",
                "Último trimestre",
                "Todo el período"
        );
        cbPeriodo.setItems(periodos);
        cbPeriodo.setValue("Último mes");
    }

    @FXML
    public void actualizarTodasLasMetricas() {
        try {
            // 1. Métricas generales
            actualizarResumenGeneral();

            // 2. Gráficos
            actualizarChartServicios();
            actualizarChartIngresos();
            actualizarChartIncidencias();

            // Mostrar mensaje de éxito
            mostrarMensajeExito("Métricas actualizadas correctamente");

        } catch (Exception e) {
            System.err.println("Error actualizando métricas: " + e.getMessage());
            mostrarMensajeError("Error al cargar las métricas");
        }
    }

    @FXML
    private void filtrarPorPeriodo() {
        actualizarTodasLasMetricas();
    }

    private void actualizarResumenGeneral() {
        Map<String, Object> metricas = empresa.obtenerMetricasGenerales();

        lblTotalEnvios.setText("• Total Envíos: " + metricas.get("totalEnvios"));
        lblIncidencias.setText("• Incidencias: " + metricas.get("totalIncidencias"));
        lblRepartidores.setText("• Repartidores: " + metricas.get("totalRepartidores"));

        // Calcular ingresos totales aprobados
        double ingresosTotales = empresa.getListaPagos().stream()
                .filter(p -> p.getResultado() != null && "APROBADO".equalsIgnoreCase(p.getResultado()))
                .mapToDouble(PagoRecord::getMonto)
                .sum();

        lblIngresosTotales.setText(String.format("• Ingresos: $%,.2f", ingresosTotales));
    }

    private void actualizarChartServicios() {
        chartServicios.getData().clear();

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        // Usar datos de ejemplo - puedes reemplazar con tus datos reales
        pieData.add(new PieChart.Data("Estándar", empresa.obtenerCantidadServicios("estandar")));
        pieData.add(new PieChart.Data("Express", empresa.obtenerCantidadServicios("express")));
        pieData.add(new PieChart.Data("Prioritario", empresa.obtenerCantidadServicios("prioritaria")));

        chartServicios.setData(pieData);
    }

    private void actualizarChartIngresos() {
        chartIngresos.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ingresos");

        // Datos de ejemplo
        series.getData().add(new XYChart.Data<>("Tarjeta", empresa.obtenerPagosTipo("TARJETA")));
        series.getData().add(new XYChart.Data<>("Efectivo", empresa.obtenerPagosTipo("EFECTIVO")));
        series.getData().add(new XYChart.Data<>("PayPal", empresa.obtenerPagosTipo("PAYPAL")));

        chartIngresos.getData().add(series);
    }

    private void actualizarChartIncidencias() {
        chartIncidencias.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Zonas De Incidencia");

        // Datos de ejemplo
        series.getData().add(new XYChart.Data<>("Norte", empresa.obtenerIncidenciasZona("Norte")));
        series.getData().add(new XYChart.Data<>("Sur", empresa.obtenerIncidenciasZona("Sur")));
        series.getData().add(new XYChart.Data<>("Centro", empresa.obtenerIncidenciasZona("Centro")));

        chartIncidencias.getData().add(series);
    }

    private void mostrarMensajeExito(String mensaje) {
        lblMensaje.setText(mensaje);
        vboxMensaje.setStyle("-fx-background-color: #E8F5E9; -fx-border-color: #C8E6C9;");
        lblMensaje.setStyle("-fx-text-fill: #2E7D32;");
        vboxMensaje.setVisible(true);
    }

    private void mostrarMensajeError(String mensaje) {
        lblMensaje.setText(mensaje);
        vboxMensaje.setStyle("-fx-background-color: #FFEBEE; -fx-border-color: #FFCDD2;");
        lblMensaje.setStyle("-fx-text-fill: #C62828;");
        vboxMensaje.setVisible(true);
    }

    @FXML
    private void volverAtras() {
        Stage stage = (Stage) btnVolverAtras.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "AdministradorView.fxml");
    }

    @FXML
    private void volverAlMenu() {
        Stage stage = (Stage) btnVolverMenu.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "AdministradorView.fxml");
    }
}