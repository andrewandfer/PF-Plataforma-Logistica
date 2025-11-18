
package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.model.Direccion;
import PFPlataformaLogistica.model.Empresa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.LinkedList;

public class ConsultarDireccionesUsuarioController {

    @FXML private TableView<Direccion> tableDirecciones;
    @FXML private TableColumn<Direccion, String> colIdDireccion;
    @FXML private TableColumn<Direccion, String> colAlias;
    @FXML private TableColumn<Direccion, String> colCalle;
    @FXML private TableColumn<Direccion, String> colCiudad;
    @FXML private TableColumn<Direccion, Double> colLatitud;
    @FXML private TableColumn<Direccion, Double> colLongitud;
    @FXML private TableColumn<Direccion, Void> colAcciones;

    @FXML private ComboBox<String> cbCriterioBusqueda;
    @FXML private TextField txtBusqueda;

    @FXML private VBox vboxDetalles;
    @FXML private Label lblDetalleId;
    @FXML private Label lblDetalleAlias;
    @FXML private Label lblDetalleCalle;
    @FXML private Label lblDetalleCiudad;
    @FXML private Label lblDetalleCoordenadas;

    @FXML private Label lblTotalDirecciones;
    @FXML private Label lblDireccionesCiudad;

    @FXML private Button btnVolverAtras;
    @FXML private Button btnRefrescar;
    @FXML private Button btnCerrarDetalles;

    private Empresa empresa;
    private ObservableList<Direccion> direccionesObservable;

    @FXML
    public void initialize() {
        empresa = Empresa.getInstance();
        direccionesObservable = FXCollections.observableArrayList();

        // Configurar tabla
        configurarTabla();

        // Cargar datos iniciales
        cargarDirecciones();

        // Configurar combo box
        if (cbCriterioBusqueda != null) {
            cbCriterioBusqueda.getSelectionModel().selectFirst();
        }

        // Ocultar panel de detalles inicialmente
        if (vboxDetalles != null) {
            vboxDetalles.setVisible(false);
        }
    }

    private void configurarTabla() {
        // Configurar cell value factories
        colIdDireccion.setCellValueFactory(new PropertyValueFactory<>("idDireccion"));
        colAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        colCalle.setCellValueFactory(new PropertyValueFactory<>("calle"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colLatitud.setCellValueFactory(new PropertyValueFactory<>("latitud"));
        colLongitud.setCellValueFactory(new PropertyValueFactory<>("longitud"));

        // Configurar columna de acciones
        colAcciones.setCellFactory(new Callback<TableColumn<Direccion, Void>, TableCell<Direccion, Void>>() {
            @Override
            public TableCell<Direccion, Void> call(TableColumn<Direccion, Void> param) {
                return new TableCell<Direccion, Void>() {
                    private final Button btnVerDetalles = new Button("👁️ Ver");

                    {
                        btnVerDetalles.setStyle("-fx-background-color: #4FC3F7; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 6; " +
                                "-fx-font-size: 11px; " +
                                "-fx-cursor: hand;");

                        btnVerDetalles.setOnAction(event -> {
                            Direccion direccion = getTableView().getItems().get(getIndex());
                            mostrarDetallesDireccion(direccion);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnVerDetalles);
                        }
                    }
                };
            }
        });

        tableDirecciones.setItems(direccionesObservable);
    }

    private void cargarDirecciones() {
        LinkedList<Direccion> listaDirecciones = empresa.getListaDirecciones();
        direccionesObservable.clear();

        if (listaDirecciones != null && !listaDirecciones.isEmpty()) {
            direccionesObservable.addAll(listaDirecciones);
            actualizarEstadisticas();
        } else {
            mostrarAlerta("Información", "No hay direcciones registradas en el sistema.");
        }
    }

    @FXML
    private void buscarDirecciones() {
        String criterio = cbCriterioBusqueda.getValue();
        String textoBusqueda = txtBusqueda.getText().trim().toLowerCase();

        if (textoBusqueda.isEmpty() && !"Todas".equals(criterio)) {
            mostrarAlerta("Búsqueda", "Por favor ingrese un texto para buscar.");
            return;
        }

        LinkedList<Direccion> todasDirecciones = empresa.getListaDirecciones();
        direccionesObservable.clear();

        if (todasDirecciones == null || todasDirecciones.isEmpty()) {
            mostrarAlerta("Información", "No hay direcciones registradas.");
            return;
        }

        if ("Todas".equals(criterio) || textoBusqueda.isEmpty()) {
            direccionesObservable.addAll(todasDirecciones);
        } else {
            for (Direccion direccion : todasDirecciones) {
                boolean coincide = false;

                switch (criterio) {
                    case "ID Dirección":
                        coincide = direccion.getIdDireccion().toLowerCase().contains(textoBusqueda);
                        break;
                    case "Alias":
                        if (direccion.getAlias() != null) {
                            coincide = direccion.getAlias().toLowerCase().contains(textoBusqueda);
                        }
                        break;
                    case "Ciudad":
                        coincide = direccion.getCiudad().toLowerCase().contains(textoBusqueda);
                        break;
                    case "Calle":
                        coincide = direccion.getCalle().toLowerCase().contains(textoBusqueda);
                        break;
                }

                if (coincide) {
                    direccionesObservable.add(direccion);
                }
            }
        }

        actualizarEstadisticas();

        if (direccionesObservable.isEmpty()) {
            mostrarAlerta("Búsqueda", "No se encontraron direcciones que coincidan con los criterios de búsqueda.");
        }
    }

    @FXML
    private void mostrarTodasDirecciones() {
        txtBusqueda.clear();
        cbCriterioBusqueda.getSelectionModel().select("Todas");
        cargarDirecciones();
    }

    @FXML
    private void refrescarTabla() {
        cargarDirecciones();
        mostrarAlerta("Actualización", "Lista de direcciones actualizada.");
    }

    private void mostrarDetallesDireccion(Direccion direccion) {
        lblDetalleId.setText(direccion.getIdDireccion());
        lblDetalleAlias.setText(direccion.getAlias() != null ? direccion.getAlias() : "Sin alias");
        lblDetalleCalle.setText(direccion.getCalle());
        lblDetalleCiudad.setText(direccion.getCiudad());
        lblDetalleCoordenadas.setText(String.format("Lat: %.6f, Long: %.6f",
                direccion.getLatitud(), direccion.getLongitud()));

        vboxDetalles.setVisible(true);
    }

    @FXML
    private void cerrarDetalles() {
        vboxDetalles.setVisible(false);
    }

    private void actualizarEstadisticas() {
        int total = direccionesObservable.size();
        lblTotalDirecciones.setText("Total: " + total + " direcciones");

        long ciudadesUnicas = direccionesObservable.stream()
                .map(Direccion::getCiudad)
                .distinct()
                .count();
        lblDireccionesCiudad.setText("Ciudades: " + ciudadesUnicas);
    }

    @FXML
    private void volverAtras() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PFPlataformaLogistica/view/DireccionUsuarioManager.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnVolverAtras.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la interfaz anterior: " + e.getMessage());
        }
    }

    @FXML
    private void irACrearDireccion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PFPlataformaLogistica/view/CrearDireccion.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnVolverAtras.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la interfaz de crear dirección: " + e.getMessage());
        }
    }

    @FXML
    private void verMisDirecciones() {
        mostrarAlerta("Funcionalidad", "Esta funcionalidad estará disponible próximamente.");
    }

    @FXML
    private void volverAlMenu() {
        mostrarAlerta("Navegación", "Volviendo al menú principal...");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}