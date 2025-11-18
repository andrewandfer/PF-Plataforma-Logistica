package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.Utils.SesionManager;
import PFPlataformaLogistica.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RastrearEnvioController implements Initializable {
    Usuario usuarioActual;
    private Empresa empresa;
    private ObservableList<Envio> enviosObservable;

    @FXML private Button btnVolver;
    @FXML private Button btnNuevoEnvio;

    // Campos de búsqueda de rastreo
    @FXML private TextField txtBuscarEnvio; // Header
    @FXML private TextField txtBuscarEnvioSidebar; // Sidebar
    @FXML private TextField txtBuscarEnvioMain; // Main area

    // Filtros
    @FXML private ComboBox<String> cbEstado;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;

    // Estadísticas
    @FXML private Label lblTotalEnvios;
    @FXML private Label lblEnTransito;
    @FXML private Label lblEntregados;

    // Tabla
    @FXML private TableView<Envio> tableEnvios;
    @FXML private TableColumn<Envio, String> colId;
    @FXML private TableColumn<Envio, String> colFechaCreacion;
    @FXML private TableColumn<Envio, String> colFechaEstimada;
    @FXML private TableColumn<Envio, String> colOrigen;
    @FXML private TableColumn<Envio, String> colDestino;
    @FXML private TableColumn<Envio, String> colEstado;
    @FXML private TableColumn<Envio, String> colRepartidor;
    @FXML private TableColumn<Envio, String> colCosto;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        empresa = Empresa.getInstance();
        usuarioActual = SesionManager.getUsuarioActual(Usuario.class);

        inicializarComponentes();
        cargarEnviosUsuario();
        configurarTabla();
        actualizarEstadisticas();
    }

    private void inicializarComponentes() {
        // Inicializar combo box de estados
        if (cbEstado != null) {
            cbEstado.getItems().addAll("Todos", "Solicitado", "Asignado", "En Ruta", "Entregado", "Incidencia");

            // Listener para filtrado en tiempo real
            cbEstado.valueProperty().addListener((obs, oldVal, newVal) -> {
                aplicarFiltros();
            });
        }
    }

    private void cargarEnviosUsuario() {
        if (usuarioActual != null && usuarioActual.getEnviosPropios() != null) {
            enviosObservable = FXCollections.observableArrayList(usuarioActual.getEnviosPropios());
            tableEnvios.setItems(enviosObservable);

            // Mostrar mensaje en pantalla si no hay envíos
            if (usuarioActual.getEnviosPropios().isEmpty()) {
                mostrarAlerta("Información", "No tienes envíos registrados.", Alert.AlertType.INFORMATION);
            }
        } else {
            enviosObservable = FXCollections.observableArrayList();
            tableEnvios.setItems(enviosObservable);
            mostrarAlerta("Información", "No se encontraron envíos para tu cuenta.", Alert.AlertType.INFORMATION);
        }
    }

    private void configurarTabla() {
        // Configurar las columnas de la tabla
        colId.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getIdEnvio() != null ?
                        cellData.getValue().getIdEnvio() : "N/A"));

        colFechaCreacion.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaCreacion() != null ?
                        cellData.getValue().getFechaCreacion() : "N/A"));

        colFechaEstimada.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaEstimada() != null ?
                        cellData.getValue().getFechaEstimada() : "N/A"));

        colOrigen.setCellValueFactory(cellData -> {
            Envio envio = cellData.getValue();
            String origen = "N/A";
            if (envio.getOrigen() != null && !envio.getOrigen().isEmpty()) {
                origen = envio.getOrigen();
            } else if (envio.getDireccion() != null) {
                origen = envio.getDireccion().getCiudad() != null ? envio.getDireccion().getCiudad() : "N/A";
            }
            return new SimpleStringProperty(origen);
        });

        colDestino.setCellValueFactory(cellData -> {
            Envio envio = cellData.getValue();
            String destino = "N/A";
            if (envio.getDestino() != null && !envio.getDestino().isEmpty()) {
                destino = envio.getDestino();
            } else if (envio.getDireccion() != null) {
                destino = envio.getDireccion().getCiudad() != null ? envio.getDireccion().getCiudad() : "N/A";
            }
            return new SimpleStringProperty(destino);
        });

        colEstado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstadoEnvio() != null ?
                        obtenerTextoEstadoTabla(cellData.getValue().getEstadoEnvio()) : "N/A"));

        colRepartidor.setCellValueFactory(cellData -> {
            Repartidor repartidor = cellData.getValue().getRepartidor();
            String textoRepartidor = "Sin asignar";
            if (repartidor != null && repartidor.getNombre() != null) {
                textoRepartidor = repartidor.getNombre();
            }
            return new SimpleStringProperty(textoRepartidor);
        });

        colCosto.setCellValueFactory(cellData -> {
            int costo = cellData.getValue().getCosto();
            String textoCosto = costo > 0 ? String.format("$%,d", costo) : "N/A";
            return new SimpleStringProperty(textoCosto);
        });

        // Personalizar las celdas de estado con colores
        colEstado.setCellFactory(column -> new TableCell<Envio, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    // Aplicar color según el estado
                    Envio envio = getTableView().getItems().get(getIndex());
                    if (envio != null && envio.getEstadoEnvio() != null) {
                        setStyle("-fx-text-fill: " + obtenerColorEstado(envio.getEstadoEnvio()) + "; -fx-font-weight: bold;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }

    private void actualizarEstadisticas() {
        if (usuarioActual == null || usuarioActual.getEnviosPropios() == null) {
            if (lblTotalEnvios != null) lblTotalEnvios.setText("0");
            if (lblEnTransito != null) lblEnTransito.setText("0");
            if (lblEntregados != null) lblEntregados.setText("0");
            return;
        }

        int total = usuarioActual.getEnviosPropios().size();
        int enTransito = 0;
        int entregados = 0;

        for (Envio envio : usuarioActual.getEnviosPropios()) {
            if (envio.getEstadoEnvio() == EstadoEnvio.ENRUTA || envio.getEstadoEnvio() == EstadoEnvio.ASIGNADO) {
                enTransito++;
            } else if (envio.getEstadoEnvio() == EstadoEnvio.ENTREGADO) {
                entregados++;
            }
        }

        if (lblTotalEnvios != null) lblTotalEnvios.setText(String.valueOf(total));
        if (lblEnTransito != null) lblEnTransito.setText(String.valueOf(enTransito));
        if (lblEntregados != null) lblEntregados.setText(String.valueOf(entregados));
    }

    private String obtenerTextoEstadoTabla(EstadoEnvio estado) {
        return switch (estado) {
            case SOLICITADO -> "Solicitado";
            case ASIGNADO -> "Asignado";
            case ENRUTA -> "En Ruta";
            case ENTREGADO -> "Entregado";
            case INCIDENCIA -> "Incidencia";
            default -> "Desconocido";
        };
    }

    // MÉTODO PRINCIPAL DE RASTREO - funciona desde cualquier campo
    @FXML
    private void rastrearEnvio() {
        String id = "";

        // Determinar de qué campo viene la búsqueda
        if (txtBuscarEnvioMain != null && !txtBuscarEnvioMain.getText().trim().isEmpty()) {
            id = txtBuscarEnvioMain.getText().trim();
        } else if (txtBuscarEnvio != null && !txtBuscarEnvio.getText().trim().isEmpty()) {
            id = txtBuscarEnvio.getText().trim();
        } else if (txtBuscarEnvioSidebar != null && !txtBuscarEnvioSidebar.getText().trim().isEmpty()) {
            id = txtBuscarEnvioSidebar.getText().trim();
        }

        if (id.isEmpty()) {
            mostrarAlerta("Validación", "Por favor ingresa el ID del envío", Alert.AlertType.WARNING);
            return;
        }

        // Buscar en los envíos reales del usuario primero
        Envio envioEncontrado = buscarEnvioEnListaUsuario(id);

        if (envioEncontrado != null) {
            mostrarInformacionEnvioReal(envioEncontrado);
        } else {
            mostrarInformacionEnvioRandom(id);
        }

        // Limpiar campos después de buscar
        if (txtBuscarEnvioMain != null) txtBuscarEnvioMain.clear();
        if (txtBuscarEnvio != null) txtBuscarEnvio.clear();
        if (txtBuscarEnvioSidebar != null) txtBuscarEnvioSidebar.clear();
    }

    private Envio buscarEnvioEnListaUsuario(String id) {
        if (usuarioActual != null && usuarioActual.getEnviosPropios() != null) {
            for (Envio envio : usuarioActual.getEnviosPropios()) {
                if (envio.getIdEnvio() != null && envio.getIdEnvio().equalsIgnoreCase(id)) {
                    return envio;
                }
            }
        }
        return null;
    }

    private void mostrarInformacionEnvioReal(Envio envio) {
        // Crear alert personalizado con información REAL del envío
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("📦 Rastreo de Envío");
        alert.setHeaderText("Información del Envío: " + envio.getIdEnvio());

        // Crear contenido personalizado
        VBox contenido = new VBox(12);

        // Estado con color
        Label lblEstado = new Label("🚚 " + obtenerTextoEstado(envio.getEstadoEnvio()));
        lblEstado.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + obtenerColorEstado(envio.getEstadoEnvio()) + ";");

        // Información de ruta
        String origen = envio.getOrigen() != null ? envio.getOrigen() :
                (envio.getDireccion() != null ? envio.getDireccion().getCiudad() : "N/A");
        String destino = envio.getDestino() != null ? envio.getDestino() :
                (envio.getDireccion() != null ? envio.getDireccion().getCiudad() : "N/A");

        Label lblRuta = new Label("📍 Ruta: " + origen + " → " + destino);
        lblRuta.setStyle("-fx-font-size: 14px;");

        // Fechas
        Label lblFechas = new Label("📅 Creado: " + envio.getFechaCreacion() + "   •   Estimado: " + envio.getFechaEstimada());
        lblFechas.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");

        // Repartidor
        String repartidorInfo = "Sin asignar";
        if (envio.getRepartidor() != null) {
            Repartidor rep = envio.getRepartidor();
            repartidorInfo = rep.getNombre() + " - Tel: " + (rep.getTelefono() != null ? rep.getTelefono() : "No disponible");
        }
        Label lblRepartidor = new Label("👤 " + repartidorInfo);
        lblRepartidor.setStyle("-fx-font-size: 13px;");

        // Barra de progreso simbólica
        ProgressBar progressBar = new ProgressBar(obtenerProgresoEstado(envio.getEstadoEnvio()));
        progressBar.setPrefWidth(300);
        progressBar.setStyle("-fx-accent: " + obtenerColorEstado(envio.getEstadoEnvio()) + ";");

        // Etiqueta de progreso
        Label lblProgreso = new Label(obtenerTextoProgreso(envio.getEstadoEnvio()));
        lblProgreso.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        contenido.getChildren().addAll(lblEstado, lblRuta, lblFechas, lblRepartidor, progressBar, lblProgreso);

        alert.getDialogPane().setContent(contenido);
        alert.getDialogPane().setPrefSize(400, 250);
        alert.showAndWait();
    }

    private void mostrarInformacionEnvioRandom(String idBuscado) {
        // Estados posibles
        EstadoEnvio[] estados = EstadoEnvio.values();
        EstadoEnvio estadoRandom = estados[(int) (Math.random() * estados.length)];

        // Ciudades random para origen y destino
        String[] ciudades = {"Bogotá", "Medellín", "Cali", "Barranquilla", "Cartagena", "Bucaramanga", "Pereira", "Armenia"};
        String origenRandom = ciudades[(int) (Math.random() * ciudades.length)];
        String destinoRandom = ciudades[(int) (Math.random() * ciudades.length)];

        // Asegurar que origen y destino sean diferentes
        while (destinoRandom.equals(origenRandom)) {
            destinoRandom = ciudades[(int) (Math.random() * ciudades.length)];
        }

        // Fechas random
        String fechaCreacion = generarFechaRandom();
        String fechaEstimada = generarFechaFuturaRandom();

        // Repartidor random
        String[] repartidores = {"Carlos López - Tel: 3101234567",
                "Ana Martínez - Tel: 3157654321",
                "Pedro Gómez - Tel: 3209876543",
                "Miguel Torres - Tel: 3115558888"};
        String repartidorRandom = repartidores[(int) (Math.random() * repartidores.length)];

        // Crear alert personalizado
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("📦 Rastreo de Envío");
        alert.setHeaderText("Información de Ejemplo para: " + idBuscado.toUpperCase());

        // Crear contenido personalizado
        VBox contenido = new VBox(12);

        // Estado con color
        Label lblEstado = new Label("🚚 " + obtenerTextoEstado(estadoRandom));
        lblEstado.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + obtenerColorEstado(estadoRandom) + ";");

        // Información de ruta
        Label lblRuta = new Label("📍 Ruta: " + origenRandom + " → " + destinoRandom);
        lblRuta.setStyle("-fx-font-size: 14px;");

        // Fechas
        Label lblFechas = new Label("📅 Creado: " + fechaCreacion + "   •   Estimado: " + fechaEstimada);
        lblFechas.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");

        // Repartidor
        Label lblRepartidor = new Label("👤 " + repartidorRandom);
        lblRepartidor.setStyle("-fx-font-size: 13px;");

        // Barra de progreso simbólica
        ProgressBar progressBar = new ProgressBar(obtenerProgresoEstado(estadoRandom));
        progressBar.setPrefWidth(300);
        progressBar.setStyle("-fx-accent: " + obtenerColorEstado(estadoRandom) + ";");

        // Etiqueta de progreso
        Label lblProgreso = new Label(obtenerTextoProgreso(estadoRandom));
        lblProgreso.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        // Mensaje informativo
        Label lblInfo = new Label("💡 Este es un ejemplo. El ID " + idBuscado + " no fue encontrado en tus envíos.");
        lblInfo.setStyle("-fx-font-size: 11px; -fx-text-fill: #FF9800; -fx-font-weight: bold;");

        contenido.getChildren().addAll(lblEstado, lblRuta, lblFechas, lblRepartidor, progressBar, lblProgreso, lblInfo);

        alert.getDialogPane().setContent(contenido);
        alert.getDialogPane().setPrefSize(400, 280);
        alert.showAndWait();
    }

    // MÉTODOS DE FILTRADO
    @FXML
    private void aplicarFiltros() {
        if (enviosObservable == null || usuarioActual == null || usuarioActual.getEnviosPropios() == null) return;

        ObservableList<Envio> enviosFiltrados = FXCollections.observableArrayList();

        // Obtener criterios de filtrado
        String estadoFiltro = cbEstado.getValue();

        // Aplicar filtros
        for (Envio envio : usuarioActual.getEnviosPropios()) {
            boolean coincide = true;

            // Filtro por estado
            if (estadoFiltro != null && !estadoFiltro.equals("Todos")) {
                String estadoEnvio = obtenerTextoEstadoTabla(envio.getEstadoEnvio());
                if (!estadoEnvio.equalsIgnoreCase(estadoFiltro)) {
                    coincide = false;
                }
            }

            if (coincide) {
                enviosFiltrados.add(envio);
            }
        }

        tableEnvios.setItems(enviosFiltrados);

        // Mostrar resultado del filtrado
        if (enviosFiltrados.isEmpty()) {
            mostrarAlerta("Filtros", "No se encontraron envíos con los criterios seleccionados.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        if (cbEstado != null) cbEstado.setValue(null);
        if (dpFechaInicio != null) dpFechaInicio.setValue(null);
        if (dpFechaFin != null) dpFechaFin.setValue(null);

        // Restaurar lista completa
        cargarEnviosUsuario();
        mostrarAlerta("Filtros", "Todos los filtros han sido limpiados.", Alert.AlertType.INFORMATION);
    }

    // MÉTODOS AUXILIARES
    private String obtenerTextoEstado(EstadoEnvio estado) {
        return switch (estado) {
            case SOLICITADO -> "Solicitado 📝";
            case ASIGNADO -> "Asignado a Repartidor ✅";
            case ENRUTA -> "En Camino 🚛";
            case ENTREGADO -> "Entregado con Éxito 🎉";
            case INCIDENCIA -> "Incidencia Reportada ⚠️";
            default -> "Estado Desconocido";
        };
    }

    private String obtenerColorEstado(EstadoEnvio estado) {
        return switch (estado) {
            case SOLICITADO -> "#FFA000"; // Amber
            case ASIGNADO -> "#2196F3";   // Blue
            case ENRUTA -> "#FF6F00";     // Deep Orange
            case ENTREGADO -> "#4CAF50";  // Green
            case INCIDENCIA -> "#D32F2F"; // Red
            default -> "#666666";
        };
    }

    private double obtenerProgresoEstado(EstadoEnvio estado) {
        return switch (estado) {
            case SOLICITADO -> 0.25;
            case ASIGNADO -> 0.5;
            case ENRUTA -> 0.75;
            case ENTREGADO -> 1.0;
            case INCIDENCIA -> 0.4;
            default -> 0.1;
        };
    }

    private String obtenerTextoProgreso(EstadoEnvio estado) {
        return switch (estado) {
            case SOLICITADO -> "En proceso de asignación...";
            case ASIGNADO -> "Repartidor en camino al punto de recogida";
            case ENRUTA -> "En ruta hacia el destino";
            case ENTREGADO -> "Paquete entregado satisfactoriamente";
            case INCIDENCIA -> "Se reportó una incidencia - En revisión";
            default -> "Procesando información...";
        };
    }

    private String generarFechaRandom() {
        int dia = 1 + (int) (Math.random() * 28);
        int mes = 1 + (int) (Math.random() * 12);
        return String.format("%02d/%02d/2025", dia, mes);
    }

    private String generarFechaFuturaRandom() {
        int dia = 1 + (int) (Math.random() * 28);
        int mes = 1 + (int) (Math.random() * 3); // Próximos 3 meses
        return String.format("%02d/%02d/2025", dia, mes);
    }

    // MÉTODOS DE NAVEGACIÓN
    @FXML
    private void volverAlMenu(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.cambiarEscena(stage, "ConsultarEnvioUsuario.fxml");
    }

    @FXML
    private void abrirCrearEnvio(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.cambiarEscena(stage, "CrearEnvio.fxml");
    }

    @FXML
    private void abrirRastrearEnvio(ActionEvent event) {
        if (txtBuscarEnvioMain != null) {
            txtBuscarEnvioMain.requestFocus();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}