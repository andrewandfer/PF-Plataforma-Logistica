package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.model.EstadoRepartidor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import PFPlataformaLogistica.model.Repartidor;

public class RepartidorController {

    @FXML
    private TextField txtIdRepartidor;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtZona;

    @FXML
    private Button btnRegistrar;

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnListar;

    @FXML
    private TableView<Repartidor> tablaRepartidores;

    @FXML
    private TableColumn<Repartidor, String> colId;

    @FXML
    private TableColumn<Repartidor, String> colNombre;

    @FXML
    private TableColumn<Repartidor, String> colTelefono;

    @FXML
    private TableColumn<Repartidor, String> colZona;

    private final ObservableList<Repartidor> listaRepartidores = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar columnas de la tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("idRepartidor"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colZona.setCellValueFactory(new PropertyValueFactory<>("zona"));

        tablaRepartidores.setItems(listaRepartidores);
    }

    @FXML
    private void registrar() {
        String id = txtIdRepartidor.getText();
        String nombre = txtNombre.getText();
        String telefono = txtTelefono.getText();
        String zona = txtZona.getText();

        if (id.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || zona.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        // Usar el patrón Builder de la clase Repartidor
        new Repartidor.RepartidorBuilder()
                .id("001")
                .nombre("Juan")
                .telefono("321...")
                .zonaCobertura("Centro")
                .localidad("Armenia")
                .disponibilidad(EstadoRepartidor.ACTIVO)
                .build();


        mostrarAlerta("Éxito", "Repartidor registrado correctamente.", Alert.AlertType.INFORMATION);
        limpiarCampos();
    }



    @FXML
    private void actualizar() {
        Repartidor seleccionado = tablaRepartidores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Error", "Selecciona un repartidor para actualizar.", Alert.AlertType.ERROR);
            return;
        }

        seleccionado.setNombre(txtNombre.getText());
        seleccionado.setTelefono(txtTelefono.getText());
        seleccionado.setZonaCobertura(txtZona.getText());
        tablaRepartidores.refresh();

        limpiarCampos();
        mostrarAlerta("Éxito", "Repartidor actualizado correctamente.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void eliminar() {
        Repartidor seleccionado = tablaRepartidores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Error", "Selecciona un repartidor para eliminar.", Alert.AlertType.ERROR);
            return;
        }

        listaRepartidores.remove(seleccionado);
        limpiarCampos();
        mostrarAlerta("Éxito", "Repartidor eliminado correctamente.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void listar() {
        tablaRepartidores.setItems(listaRepartidores);
        mostrarAlerta("Información", "Lista actualizada.", Alert.AlertType.INFORMATION);
    }

    private void limpiarCampos() {
        txtIdRepartidor.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtZona.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

