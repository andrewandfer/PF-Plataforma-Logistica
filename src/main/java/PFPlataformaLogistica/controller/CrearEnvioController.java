package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.Utils.SesionManager;
import PFPlataformaLogistica.model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class CrearEnvioController implements Initializable {

    @FXML
    private Button btnCrearEnvio;

    @FXML
    private Button btnVolver;

    @FXML
    private ComboBox<String> cbTipoEnvio;

    @FXML
    private CheckBox chkFirmaRequerida;

    @FXML
    private CheckBox chkFragil;

    @FXML
    private CheckBox chkPrioridad;

    @FXML
    private CheckBox chkSeguro;

    @FXML
    private Label lblCostoEstimado;

    @FXML
    private Label lblCostoPeso;

    @FXML
    private Label lblCostoVolumen;

    @FXML
    private Label lblRecargos;

    @FXML
    private Label lblTarifaBase;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private TextField txtDestinoCalle;

    @FXML
    private TextField txtDestinoCiudad;

    @FXML
    private TextField txtOrigenCalle;

    @FXML
    private TextField txtOrigenCiudad;

    @FXML
    private TextField txtPeso;

    @FXML
    private TextField txtVolumen;

    private Empresa empresa;
    private Usuario usuarioActual;
    private Tarifa tarifaCalculada;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        empresa = Empresa.getInstance();
        usuarioActual = obtenerUsuarioActual();
        configurarComboBox();
        btnCrearEnvio.setDisable(true);
    }

    private Usuario obtenerUsuarioActual() {
        Persona persona = SesionManager.getPersonaActual();
        if (persona instanceof Usuario) {
            return (Usuario) persona;
        }
        mostrarAlerta("Error", "No hay sesión de usuario activa", Alert.AlertType.ERROR);
        return null;
    }

    private void configurarComboBox() {
        cbTipoEnvio.setItems(FXCollections.observableArrayList(
                "SEGURO", "ENTREGA", "PRIORITARIA"
        ));
        cbTipoEnvio.setValue("ENTREGA");
    }

    @FXML
    void cotizarTarifa(ActionEvent event) {
        cotizarTarifa();
    }

    private void cotizarTarifa() {
        try {
            if (!validarCamposParaCotizacion()) {
                return;
            }

            // Obtener datos del formulario
            float peso = Float.parseFloat(txtPeso.getText());
            float volumen = Float.parseFloat(txtVolumen.getText());

            // Calcular tarifa base
            float tarifaBase = 5000;
            float costoPorKg = peso * 300;
            float costoPorVolumen = volumen * 100;
            float distancia = calcularDistanciaSimulada();
            float costoPorDistancia = distancia * 500;

            float costoBase = tarifaBase + costoPorDistancia + costoPorKg + costoPorVolumen;

            // Aplicar servicios adicionales usando Decorator
            ServicioEnvio servicio = new EnvioBasico(costoBase, "Servicio de envío estándar");
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

            float costoTotal = servicio.calcularCosto();

            // Crear objeto Tarifa - CORREGIDO: usar constructor correcto
            // Asumiendo que Tarifa tiene campos para: base, peso, volumen, prioridad, recargos
            tarifaCalculada = new Tarifa(tarifaBase, costoPorKg, costoPorVolumen,
                    chkPrioridad.isSelected() ? 1 : 0, recargos);

            // Actualizar interfaz
            actualizarLabelsCotizacion(tarifaBase + costoPorDistancia, costoPorKg,
                    costoPorVolumen, recargos, costoTotal);

            btnCrearEnvio.setDisable(false);

            mostrarAlerta("Cotización Exitosa",
                    servicio.obtenerDescripcion() + "\nCosto Total: $" + String.format("%.2f", costoTotal),
                    Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Por favor ingresa valores numéricos válidos para peso y volumen",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    void crearEnvio(ActionEvent event) {
        crearEnvio();
    }

    private void crearEnvio() {
        try {
            if (tarifaCalculada == null) {
                mostrarAlerta("Error", "Primero debes cotizar la tarifa del envío",
                        Alert.AlertType.WARNING);
                return;
            }

            if (!validarCamposCompletos()) {
                return;
            }

            Envio nuevoEnvio = construirEnvio();

            // Guardar envío en el sistema
            empresa.agregarEnvioAUsuario(usuarioActual, nuevoEnvio);

            mostrarAlerta("Éxito",
                    "Envío creado correctamente!\nID: " + nuevoEnvio.getIdEnvio() +
                            "\nEstado: " + nuevoEnvio.getEstadoEnvio(),
                    Alert.AlertType.INFORMATION);

            limpiarFormulario();

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al crear el envío: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            // En lugar de printStackTrace, usamos logging simple
            System.err.println("Error al crear envío: " + e.getMessage());
        }
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        // Limpiar campos de texto
        txtDescripcion.clear();
        txtPeso.clear();
        txtVolumen.clear();
        txtOrigenCalle.clear();
        txtOrigenCiudad.clear();
        txtDestinoCalle.clear();
        txtDestinoCiudad.clear();

        // Limpiar checkboxes
        chkSeguro.setSelected(false);
        chkFragil.setSelected(false);
        chkFirmaRequerida.setSelected(false);
        chkPrioridad.setSelected(false);

        // Resetear labels
        lblCostoEstimado.setText("$0.00");
        lblTarifaBase.setText("$0.00");
        lblCostoPeso.setText("$0.00");
        lblCostoVolumen.setText("$0.00");
        lblRecargos.setText("$0.00");

        // Deshabilitar botón de crear
        btnCrearEnvio.setDisable(true);
        tarifaCalculada = null;
    }

    @FXML
    void volver(ActionEvent event) {
        volver();
    }

    private void volver() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        SceneManager.cambiarEscena(stage, "EnviosUsuarioView.fxml");
    }

    private boolean validarCamposParaCotizacion() {
        if (txtDescripcion.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "Ingresa una descripción del producto",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (txtPeso.getText().trim().isEmpty() || txtVolumen.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "Ingresa peso y volumen del paquete",
                    Alert.AlertType.WARNING);
            return false;
        }

        try {
            float peso = Float.parseFloat(txtPeso.getText());
            float volumen = Float.parseFloat(txtVolumen.getText());

            if (peso <= 0 || volumen <= 0) {
                mostrarAlerta("Validación", "Peso y volumen deben ser mayores a cero",
                        Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Validación", "Peso y volumen deben ser valores numéricos",
                    Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private boolean validarCamposCompletos() {
        if (txtOrigenCalle.getText().trim().isEmpty() || txtOrigenCiudad.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "Completa la dirección de origen",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (txtDestinoCalle.getText().trim().isEmpty() || txtDestinoCiudad.getText().trim().isEmpty()) {
            mostrarAlerta("Validación", "Completa la dirección de destino",
                    Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private float calcularDistanciaSimulada() {
        return (float) (Math.random() * 45 + 5); // Entre 5 y 50 km
    }

    private void actualizarLabelsCotizacion(float tarifaBase, float costoPeso,
                                            float costoVolumen, float recargos, float costoTotal) {
        lblTarifaBase.setText(String.format("$%.2f", tarifaBase));
        lblCostoPeso.setText(String.format("$%.2f", costoPeso));
        lblCostoVolumen.setText(String.format("$%.2f", costoVolumen));
        lblRecargos.setText(String.format("$%.2f", recargos));
        lblCostoEstimado.setText(String.format("$%.2f", costoTotal));
    }

    private Envio construirEnvio() {
        // Crear producto
        Producto producto = new Producto(
                "PROD-" + System.currentTimeMillis(),
                txtDescripcion.getText(),
                (int) Float.parseFloat(txtPeso.getText())
        );

        LinkedList<Producto> listaProductos = new LinkedList<>();
        listaProductos.add(producto);

        // Crear direcciones
        Direccion direccionOrigen = new Direccion.DireccionBuilder()
                .idDireccion("DIR-ORIG-" + System.currentTimeMillis())
                .alias("Origen")
                .calle(txtOrigenCalle.getText())
                .ciudad(txtOrigenCiudad.getText())
                .build();

        Direccion direccionDestino = new Direccion.DireccionBuilder()
                .idDireccion("DIR-DEST-" + System.currentTimeMillis())
                .alias("Destino")
                .calle(txtDestinoCalle.getText())
                .ciudad(txtDestinoCiudad.getText())
                .build();

        // Preparar fechas
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        String fechaCreacion = hoy.format(formatter);
        String fechaEstimada = hoy.plusDays(3).format(formatter); // 3 días para entrega

        // Obtener tipo de envío
        TipoEnvio tipoEnvio = TipoEnvio.valueOf(cbTipoEnvio.getValue());

        // Calcular costo total como entero - CORREGIDO: usar los valores ya calculados
        int costoTotal = (int)
                (tarifaCalculada.getBase() +
                tarifaCalculada.getPeso() +
                tarifaCalculada.getVolumen() +
                        tarifaCalculada.getPrioridad()+
                tarifaCalculada.getRecargo());

        // Si los métodos anteriores no existen, usar valores directos:
        // int costoTotal = (int) calcularCostoTotalDesdeDecorator();

        // Crear y retornar el envío según tu clase Envio
        return new Envio(
                listaProductos,
                fechaCreacion,
                fechaEstimada,
                "ENV-" + System.currentTimeMillis(), // idEnvio
                (int) Float.parseFloat(txtPeso.getText()), // pesoEnvio
                tipoEnvio,
                EstadoEnvio.SOLICITADO, // estado inicial
                tarifaCalculada,
                null, // repartidor (se asignará después)
                direccionOrigen.getCalle() + ", " + direccionOrigen.getCiudad(), // origen
                direccionDestino.getCalle() + ", " + direccionDestino.getCiudad(), // destino
                costoTotal, // costo
                direccionDestino // dirección de entrega
        );
    }

    // Método alternativo si los getters de Tarifa no existen
    private float calcularCostoTotalDesdeDecorator() {
        float tarifaBase = 5000;
        float peso = Float.parseFloat(txtPeso.getText());
        float volumen = Float.parseFloat(txtVolumen.getText());
        float distancia = calcularDistanciaSimulada();

        float costoBase = tarifaBase + (distancia * 500) + (peso * 300) + (volumen * 100);

        ServicioEnvio servicio = new EnvioBasico(costoBase, "Servicio de envío estándar");

        if (chkSeguro.isSelected()) servicio = new SeguroDecorator(servicio);
        if (chkFragil.isSelected()) servicio = new FragilDecorator(servicio);
        if (chkFirmaRequerida.isSelected()) servicio = new FirmaDecorator(servicio);
        if (chkPrioridad.isSelected()) servicio = new PrioridadDecorator(servicio);

        return servicio.calcularCosto();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}