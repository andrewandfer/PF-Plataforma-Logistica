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
            actualizarChartTiemposEntrega();

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
        pieData.add(new PieChart.Data("Estándar", 50));
        pieData.add(new PieChart.Data("Express", 30));
        pieData.add(new PieChart.Data("Prioritario", 20));

        chartServicios.setData(pieData);
    }

    private void actualizarChartIngresos() {
        chartIngresos.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ingresos");

        // Datos de ejemplo
        series.getData().add(new XYChart.Data<>("Tarjeta", 1500000));
        series.getData().add(new XYChart.Data<>("Efectivo", 800000));
        series.getData().add(new XYChart.Data<>("Transferencia", 1200000));

        chartIngresos.getData().add(series);
    }

    private void actualizarChartIncidencias() {
        chartIncidencias.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Incidencias");

        // Datos de ejemplo
        series.getData().add(new XYChart.Data<>("Norte", 8));
        series.getData().add(new XYChart.Data<>("Sur", 12));
        series.getData().add(new XYChart.Data<>("Este", 5));
        series.getData().add(new XYChart.Data<>("Oeste", 7));

        chartIncidencias.getData().add(series);
    }

    private void actualizarChartTiemposEntrega() {
        chartTiemposEntrega.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Tiempo Promedio (horas)");

        // Datos de ejemplo
        series.getData().add(new XYChart.Data<>("Lun", 2.5));
        series.getData().add(new XYChart.Data<>("Mar", 3.1));
        series.getData().add(new XYChart.Data<>("Mié", 2.8));
        series.getData().add(new XYChart.Data<>("Jue", 3.5));
        series.getData().add(new XYChart.Data<>("Vie", 4.2));

        chartTiemposEntrega.getData().add(series);
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
        // Lógica para volver atrás
        Stage stage = (Stage) btnVolverAtras.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "Administrador.fxml");
    }

    @FXML
    private void volverAlMenu() {
        Stage stage = (Stage) btnVolverMenu.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "Administrador.fxml");
    }
}