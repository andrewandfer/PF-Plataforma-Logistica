package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.Utils.SceneManager;
import PFPlataformaLogistica.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GestionEnviosAdministradorController implements Initializable {

    // Componentes del header
    @FXML private TextField txtBuscarEnvio;
    @FXML private Button btnVolver;

    // Componentes del menú lateral - RF-012
    @FXML private ComboBox<Repartidor> cbRepartidores;
    @FXML private ComboBox<EstadoEnvio> cbEstados;
    @FXML private ComboBox<String> cbTiposIncidencia;
    @FXML private TextArea txtDescripcionIncidencia;
    @FXML private ComboBox<EstadoEnvio> cbFiltrarEstado;
    @FXML private ComboBox<Repartidor> cbFiltrarRepartidor;

    // Componentes de la tabla
    @FXML private TableView<Envio> tableEnvios;
    @FXML private TableColumn<Envio, String> colId;
    @FXML private TableColumn<Envio, String> colFechaCreacion;
    @FXML private TableColumn<Envio, String> colOrigen;
    @FXML private TableColumn<Envio, String> colDestino;
    @FXML private TableColumn<Envio, String> colEstado;
    @FXML private TableColumn<Envio, String> colRepartidor;
    @FXML private TableColumn<Envio, Integer> colPeso;
    @FXML private TableColumn<Envio, Integer> colCosto;

    // Componentes de estadísticas
    @FXML private Label lblTotalEnvios;
    @FXML private Label lblPendientes;
    @FXML private Label lblIncidencias;

    private ObservableList<Envio> enviosObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.enviosObservableList = FXCollections.observableArrayList();

        inicializarComponentes();
        cargarDatos();
        actualizarEstadisticas();
    }

    private void inicializarComponentes() {
        // Configurar tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("idEnvio"));
        colFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("origen"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("destino"));
        colEstado.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getEstadoEnvio().toString()
                ));
        colRepartidor.setCellValueFactory(cellData -> {
            Repartidor repartidor = cellData.getValue().getRepartidor();
            return new javafx.beans.property.SimpleStringProperty(
                    repartidor != null ? repartidor.getNombre() : "No asignado"
            );
        });
        colPeso.setCellValueFactory(new PropertyValueFactory<>("pesoEnvio"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));

        tableEnvios.setItems(enviosObservableList);

        // Configurar combobox de estados
        cbEstados.setItems(FXCollections.observableArrayList(EstadoEnvio.values()));
        cbFiltrarEstado.setItems(FXCollections.observableArrayList(EstadoEnvio.values()));

        // Configurar tipos de incidencia
        ObservableList<String> tiposIncidencia = FXCollections.observableArrayList(
                "Cliente no encontrado",
                "Paquete dañado",
                "Dirección incorrecta",
                "Cliente no disponible",
                "Problema de pago",
                "Otro"
        );
        cbTiposIncidencia.setItems(tiposIncidencia);

        // CONFIGURAR CÓMO SE MUESTRAN LOS REPARTIDORES EN LOS COMBOBOX
        configurarComboBoxRepartidores();

        // Configurar selección de tabla
        tableEnvios.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> actualizarControlesConSeleccion()
        );
    }

    private void configurarComboBoxRepartidores() {
        // Configurar cómo se muestran los repartidores en el ComboBox principal
        cbRepartidores.setCellFactory(param -> new ListCell<Repartidor>() {
            @Override
            protected void updateItem(Repartidor repartidor, boolean empty) {
                super.updateItem(repartidor, empty);
                if (empty || repartidor == null) {
                    setText(null);
                } else {
                    setText(repartidor.getNombre() + " - " + repartidor.getTelefono() + " (" + repartidor.getEstadoDisponibilidad() + ")");
                }
            }
        });

        cbRepartidores.setButtonCell(new ListCell<Repartidor>() {
            @Override
            protected void updateItem(Repartidor repartidor, boolean empty) {
                super.updateItem(repartidor, empty);
                if (empty || repartidor == null) {
                    setText("Seleccionar repartidor");
                } else {
                    setText(repartidor.getNombre());
                }
            }
        });

        // Configurar cómo se muestran los repartidores en el ComboBox de filtro
        cbFiltrarRepartidor.setCellFactory(param -> new ListCell<Repartidor>() {
            @Override
            protected void updateItem(Repartidor repartidor, boolean empty) {
                super.updateItem(repartidor, empty);
                if (empty || repartidor == null) {
                    setText(null);
                } else {
                    setText(repartidor.getNombre() + " - " + repartidor.getTelefono() + " (" + repartidor.getEstadoDisponibilidad() + ")");
                }
            }
        });

        cbFiltrarRepartidor.setButtonCell(new ListCell<Repartidor>() {
            @Override
            protected void updateItem(Repartidor repartidor, boolean empty) {
                super.updateItem(repartidor, empty);
                if (empty || repartidor == null) {
                    setText("Todos los repartidores");
                } else {
                    setText(repartidor.getNombre());
                }
            }
        });
    }

    private void cargarDatos() {
        Empresa empresa = Empresa.getInstance();

        // DEBUG extensivo
        System.out.println("=== DEBUG INICIO CARGA DATOS ===");

        // Verificar repartidores
        List<Repartidor> todosRepartidores = empresa.getListaRepartidores();
        System.out.println("Total repartidores en empresa: " +
                (todosRepartidores != null ? todosRepartidores.size() : "LISTA NULA"));

        if (todosRepartidores != null) {
            for (Repartidor r : todosRepartidores) {
                System.out.println("Repartidor: " + r.getNombre() +
                        " | ID: " + r.getId() +
                        " | Estado: " + r.getEstadoDisponibilidad() +
                        " | Tel: " + r.getTelefono());
            }
        }

        // Obtener repartidores disponibles
        List<Repartidor> repartidoresDisponibles = empresa.obtenerRepartidoresDisponibles();
        System.out.println("Repartidores disponibles: " + repartidoresDisponibles.size());

        // Cargar en ComboBox
        cbRepartidores.setItems(FXCollections.observableArrayList(repartidoresDisponibles));
        cbFiltrarRepartidor.setItems(FXCollections.observableArrayList(repartidoresDisponibles));

        System.out.println("Items en ComboBox repartidores: " + cbRepartidores.getItems().size());
        System.out.println("Items en ComboBox filtro: " + cbFiltrarRepartidor.getItems().size());
        System.out.println("=== DEBUG FIN CARGA DATOS ===");

        // Cargar envíos
        List<Envio> envios = empresa.getListaEnvios();
        enviosObservableList.setAll(envios);
        actualizarEstadisticas();
    }

    private void actualizarEstadisticas() {
        Empresa empresa = Empresa.getInstance();
        List<Envio> envios = empresa.getListaEnvios();

        int total = envios.size();
        int pendientes = (int) envios.stream()
                .filter(e -> e.getEstadoEnvio() == EstadoEnvio.SOLICITADO)
                .count();
        int conIncidencias = (int) envios.stream()
                .filter(e -> e.getEstadoEnvio() == EstadoEnvio.INCIDENCIA)
                .count();

        lblTotalEnvios.setText(String.valueOf(total));
        lblPendientes.setText(String.valueOf(pendientes));
        lblIncidencias.setText(String.valueOf(conIncidencias));
    }

    private void actualizarControlesConSeleccion() {
        Envio envioSeleccionado = tableEnvios.getSelectionModel().getSelectedItem();
        if (envioSeleccionado != null) {
            // Actualizar combobox de repartidor con el actual
            Repartidor repartidorActual = envioSeleccionado.getRepartidor();
            if (repartidorActual != null) {
                cbRepartidores.getSelectionModel().select(repartidorActual);
            }

            // Actualizar combobox de estado con el actual
            cbEstados.getSelectionModel().select(envioSeleccionado.getEstadoEnvio());
        }
    }

    // ==================== MÉTODOS RF-012 ====================

    @FXML
    private void buscarEnvio() {
        String idEnvio = txtBuscarEnvio.getText().trim();
        if (idEnvio.isEmpty()) {
            mostrarAlerta("Error", "Ingrese un ID de envío para buscar", Alert.AlertType.WARNING);
            return;
        }

        Empresa empresa = Empresa.getInstance();
        Envio envio = empresa.buscarEnvioPorId(idEnvio);
        if (envio != null) {
            enviosObservableList.setAll(envio);
            tableEnvios.getSelectionModel().select(envio);
            mostrarAlerta("Éxito", "Envío encontrado: " + idEnvio, Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "Envío no encontrado: " + idEnvio, Alert.AlertType.ERROR);
            cargarDatos(); // Recargar todos los envíos
        }
    }

    @FXML
    private void asignarRepartidor() {
        Envio envio = tableEnvios.getSelectionModel().getSelectedItem();
        Repartidor repartidor = cbRepartidores.getSelectionModel().getSelectedItem();

        if (envio == null) {
            mostrarAlerta("Error", "Seleccione un envío de la tabla", Alert.AlertType.WARNING);
            return;
        }

        if (repartidor == null) {
            mostrarAlerta("Error", "Seleccione un repartidor", Alert.AlertType.WARNING);
            return;
        }

        Empresa empresa = Empresa.getInstance();
        boolean exito = empresa.asignarRepartidorAEnvio(envio.getIdEnvio(), repartidor.getId());
        if (exito) {
            mostrarAlerta("Éxito", "Repartidor asignado exitosamente", Alert.AlertType.INFORMATION);
            cargarDatos();
            actualizarEstadisticas();
        } else {
            mostrarAlerta("Error", "No se pudo asignar el repartidor", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void reasignarRepartidor() {
        Envio envio = tableEnvios.getSelectionModel().getSelectedItem();
        Repartidor nuevoRepartidor = cbRepartidores.getSelectionModel().getSelectedItem();

        if (envio == null) {
            mostrarAlerta("Error", "Seleccione un envío de la tabla", Alert.AlertType.WARNING);
            return;
        }

        if (nuevoRepartidor == null) {
            mostrarAlerta("Error", "Seleccione un repartidor", Alert.AlertType.WARNING);
            return;
        }

        if (envio.getRepartidor() == null) {
            mostrarAlerta("Información", "Este envío no tiene repartidor asignado. Use 'Asignar' en lugar de 'Reasignar'", Alert.AlertType.INFORMATION);
            return;
        }

        Empresa empresa = Empresa.getInstance();
        boolean exito = empresa.reasignarRepartidorAEnvio(envio.getIdEnvio(), nuevoRepartidor.getId());
        if (exito) {
            mostrarAlerta("Éxito", "Envío reasignado exitosamente", Alert.AlertType.INFORMATION);
            cargarDatos();
            actualizarEstadisticas();
        } else {
            mostrarAlerta("Error", "No se pudo reasignar el envío", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cambiarEstadoEnvio() {
        Envio envio = tableEnvios.getSelectionModel().getSelectedItem();
        EstadoEnvio nuevoEstado = cbEstados.getSelectionModel().getSelectedItem();

        if (envio == null) {
            mostrarAlerta("Error", "Seleccione un envío de la tabla", Alert.AlertType.WARNING);
            return;
        }

        if (nuevoEstado == null) {
            mostrarAlerta("Error", "Seleccione un estado", Alert.AlertType.WARNING);
            return;
        }

        Empresa empresa = Empresa.getInstance();
        boolean exito = empresa.cambiarEstadoEnvio(envio.getIdEnvio(), nuevoEstado);
        if (exito) {
            mostrarAlerta("Éxito", "Estado actualizado exitosamente", Alert.AlertType.INFORMATION);
            cargarDatos();
            actualizarEstadisticas();
        } else {
            mostrarAlerta("Error", "No se pudo actualizar el estado", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void registrarIncidencia() {
        Envio envio = tableEnvios.getSelectionModel().getSelectedItem();
        String tipo = cbTiposIncidencia.getSelectionModel().getSelectedItem();
        String descripcion = txtDescripcionIncidencia.getText().trim();

        if (envio == null) {
            mostrarAlerta("Error", "Seleccione un envío de la tabla", Alert.AlertType.WARNING);
            return;
        }

        if (tipo == null) {
            mostrarAlerta("Error", "Seleccione un tipo de incidencia", Alert.AlertType.WARNING);
            return;
        }

        if (descripcion.isEmpty()) {
            mostrarAlerta("Error", "Ingrese una descripción para la incidencia", Alert.AlertType.WARNING);
            return;
        }

        Empresa empresa = Empresa.getInstance();
        empresa.registrarIncidencia(envio.getIdEnvio(), tipo, descripcion);
        mostrarAlerta("Éxito", "Incidencia registrada exitosamente", Alert.AlertType.INFORMATION);

        // Limpiar campos
        txtDescripcionIncidencia.clear();
        cbTiposIncidencia.getSelectionModel().clearSelection();

        cargarDatos();
        actualizarEstadisticas();
    }

    @FXML
    private void aplicarFiltros() {
        EstadoEnvio estadoFiltro = cbFiltrarEstado.getSelectionModel().getSelectedItem();
        Repartidor repartidorFiltro = cbFiltrarRepartidor.getSelectionModel().getSelectedItem();

        Empresa empresa = Empresa.getInstance();
        List<Envio> enviosFiltrados = empresa.getListaEnvios();

        // Aplicar filtro por estado
        if (estadoFiltro != null) {
            enviosFiltrados = enviosFiltrados.stream()
                    .filter(e -> e.getEstadoEnvio() == estadoFiltro)
                    .toList();
        }

        // Aplicar filtro por repartidor
        if (repartidorFiltro != null) {
            enviosFiltrados = enviosFiltrados.stream()
                    .filter(e -> e.getRepartidor() != null &&
                            e.getRepartidor().getId().equals(repartidorFiltro.getId()))
                    .toList();
        }

        enviosObservableList.setAll(enviosFiltrados);
    }

    @FXML
    private void limpiarFiltros() {
        cbFiltrarEstado.getSelectionModel().clearSelection();
        cbFiltrarRepartidor.getSelectionModel().clearSelection();
        cargarDatos();
    }

    @FXML
    private void volverAlMenu(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.cambiarEscena(stage, "AdministradorView.fxml"); // Ajusta según tu menú principal
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}