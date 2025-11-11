package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.Utils.SesionManager;
import PFPlataformaLogistica.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CrearEnvioController implements Initializable {

    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPeso;
    @FXML private TextField txtVolumen;
    @FXML private ComboBox<String> cbTipoEnvio;

    @FXML private TextField txtOrigenCalle;
    @FXML private TextField txtOrigenCiudad;
    @FXML private TextField txtDestinoCalle;
    @FXML private TextField txtDestinoCiudad;

    @FXML private CheckBox chkSeguro;
    @FXML private CheckBox chkFragil;
    @FXML private CheckBox chkFirmaRequerida;
    @FXML private CheckBox chkPrioridad;

    @FXML private Label lblCostoEstimado;
    @FXML private Label lblTarifaBase;
    @FXML private Label lblCostoPeso;
    @FXML private Label lblCostoVolumen;
    @FXML private Label lblRecargos;

    @FXML private Button btnCrearEnvio;
    @FXML private Button btnVolver;

    private Empresa empresa;
    private Usuario usuarioActual;
    private Tarifa tarifaCalculada;

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

        // Configurar ComboBox de tipos de envío
        cbTipoEnvio.setItems(FXCollections.observableArrayList(
                "SEGURO", "ENTREGA", "PRIORITARIA"
        ));
        cbTipoEnvio.setValue("ENTREGA");
    }

    @FXML
    private void cotizarTarifa() {
        try {
            // Validar campos
            if (!validarCampos()) {
                return;
            }

            // Obtener datos
            float peso = Float.parseFloat(txtPeso.getText());
            float volumen = Float.parseFloat(txtVolumen.getText());

            // Calcular distancia simulada (entre 5 y 50 km)
            float distancia = (float) (Math.random() * 45 + 5);

            // Calcular tarifa base
            float tarifaBase = 5000;
            float costoPorKm = distancia * 500;
            float costoPorKg = peso * 300;
            float costoPorVolumen = volumen * 100;

            float costoBase = tarifaBase + costoPorKm + costoPorKg + costoPorVolumen;

            // ========== USAR DECORATOR (NUEVO) ==========
            ServicioEnvio servicio = new EnvioBasico(costoBase, "Envío Estándar");

            // Aplicar decoradores según los checkboxes seleccionados
            float recargos = 0;

            if (chkSeguro.isSelected()) {
                servicio = new SeguroDecorator(servicio);
                recargos += 3000;
            }

            if (chkFragil.isSelected()) {
                servicio = new FragilDecorator(servicio);
                recargos += 2000;
            }

            if (chkFirmaRequerida.isSelected()) {
                servicio = new FirmaDecorator(servicio);
                recargos += 1500;
            }

            if (chkPrioridad.isSelected()) {
                servicio = new PrioridadDecorator(servicio);
                recargos += 5000;
            }

            // Calcular costo total usando el decorator
            float costoTotal = servicio.calcularCosto();

            // Crear objeto Tarifa
            tarifaCalculada = new Tarifa(tarifaBase, costoPorKg, costoPorVolumen,
                    chkPrioridad.isSelected() ? 1 : 0, recargos);

            // Actualizar labels
            lblTarifaBase.setText(String.format("$%.2f", tarifaBase + costoPorKm));
            lblCostoPeso.setText(String.format("$%.2f", costoPorKg));
            lblCostoVolumen.setText(String.format("$%.2f", costoPorVolumen));
            lblRecargos.setText(String.format("$%.2f", recargos));
            lblCostoEstimado.setText(String.format("$%.2f", costoTotal));

            // Habilitar botón de crear envío
            btnCrearEnvio.setDisable(false);

            // Mostrar alerta con descripción completa usando decorator
            mostrarAlerta("Cotización Exitosa",
                    servicio.obtenerDescripcion() + "\n\n" +
                            String.format("Costo Total: $%.2f\nDistancia aproximada: %.1f km",
                                    costoTotal, distancia),
                    Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Por favor ingresa valores numéricos válidos en peso y volumen",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void crearEnvio() {
        try {
            if (tarifaCalculada == null) {
                mostrarAlerta("Error", "Primero debes cotizar la tarifa del envío", Alert.AlertType.WARNING);
                return;
            }

            // Crear direcciones
            Direccion origen = new Direccion.DireccionBuilder()
                    .idDireccion("DIR-" + System.currentTimeMillis())
                    .alias("Origen")
                    .calle(txtOrigenCalle.getText())
                    .ciudad(txtOrigenCiudad.getText())
                    .build();

            Direccion destino = new Direccion.DireccionBuilder()
                    .idDireccion("DIR-" + (System.currentTimeMillis() + 1))
                    .alias("Destino")
                    .calle(txtDestinoCalle.getText())
                    .ciudad(txtDestinoCiudad.getText())
                    .build();

            List<Direccion> direcciones = new ArrayList<>();
            direcciones.add(origen);
            direcciones.add(destino);

            // Crear producto
            String idProducto = "PROD-" + System.currentTimeMillis();
            String nombreProducto = txtDescripcion.getText();
            int pesoProducto = (int) Float.parseFloat(txtPeso.getText());
            Producto producto = new Producto(idProducto, nombreProducto, pesoProducto);

            // Obtener tipo de envío
            TipoEnvio tipoEnvio = TipoEnvio.valueOf(cbTipoEnvio.getValue());

            // Fechas
            LocalDate hoy = LocalDate.now();
            LocalDate estimada = hoy.plusDays(2); // 2 días de entrega
            String fechaCreacion = hoy.format(DateTimeFormatter.ISO_DATE);
            String fechaEstimada = estimada.format(DateTimeFormatter.ISO_DATE);

            // Crear envío
            int peso = (int) Float.parseFloat(txtPeso.getText());
            Envio nuevoEnvio = empresa.crearEnvio(producto, fechaCreacion, fechaEstimada,
                    direcciones, peso, tipoEnvio, tarifaCalculada);

            // Agregar envío al usuario
            empresa.agregarEnvioAUsuario(usuarioActual, nuevoEnvio);

            mostrarAlerta("Éxito",
                    "Envío creado correctamente!\nID: " + nuevoEnvio.getIdEnvio() +
                            "\nEstado: SOLICITADO\nSerá asignado a un repartidor pronto.",
                    Alert.AlertType.INFORMATION);

            // Volver a la vista de envíos
            volver();

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al crear el envío: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void limpiarFormulario() {
        txtDescripcion.clear();
        txtPeso.clear();
        txtVolumen.clear();
        txtOrigenCalle.clear();
        txtOrigenCiudad.clear();
        txtDestinoCalle.clear();
        txtDestinoCiudad.clear();

        chkSeguro.setSelected(false);
        chkFragil.setSelected(false);
        chkFirmaRequerida.setSelected(false);
        chkPrioridad.setSelected(false);

        lblCostoEstimado.setText("$0.00");
        lblTarifaBase.setText("$0.00");
        lblCostoPeso.setText("$0.00");
        lblCostoVolumen.setText("$0.00");
        lblRecargos.setText("$0.00");

        btnCrearEnvio.setDisable(true);
        tarifaCalculada = null;
    }

    @FXML
    private void volver() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "EnviosUsuarioView.fxml");
    }

    private boolean validarCampos() {
        if (txtDescripcion.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "Por favor ingresa una descripción del paquete", Alert.AlertType.WARNING);
            return false;
        }

        if (txtPeso.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "Por favor ingresa el peso del paquete", Alert.AlertType.WARNING);
            return false;
        }

        if (txtVolumen.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "Por favor ingresa el volumen del paquete", Alert.AlertType.WARNING);
            return false;
        }

        if (txtOrigenCalle.getText().trim().isEmpty() || txtOrigenCiudad.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "Por favor completa la dirección de origen", Alert.AlertType.WARNING);
            return false;
        }

        if (txtDestinoCalle.getText().trim().isEmpty() || txtDestinoCiudad.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "Por favor completa la dirección de destino", Alert.AlertType.WARNING);
            return false;
        }

        try {
            float peso = Float.parseFloat(txtPeso.getText());
            float volumen = Float.parseFloat(txtVolumen.getText());

            if (peso <= 0 || volumen <= 0) {
                mostrarAlerta("Validación", "El peso y volumen deben ser mayores a cero", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Validación", "Peso y volumen deben ser valores numéricos", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}