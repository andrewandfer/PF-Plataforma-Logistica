package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.model.Envio;
import PFPlataformaLogistica.model.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class DetallesProductosEnvioControler {

    @FXML
    private Label lblTitulo;
    @FXML
    private TableView<Producto> tablaProductos;
    @FXML
    private TableColumn<Producto, String> colNombreProducto;
    @FXML
    private TableColumn<Producto, String> colPeso;
    @FXML
    private TableColumn<Producto, String> colIDProducto;

    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnAyuda;
    @FXML
    private Button btnContacto;
    private Envio envioActual;

    public void setEnvio(Envio envio) {
        this.envioActual = envio;

        if (envio == null) {
            lblTitulo.setText("Envío no válido");
            tablaProductos.setItems(FXCollections.observableArrayList());
            return;
        }

        // Obtener ID del envío (ajusta según tu modelo)
        String idEnvio = "ID no disponible";
        try {
            idEnvio = envio.getIdEnvio(); // Cambia a envio.getId() si tu modelo usa ese nombre
        } catch (Exception e) {
            idEnvio = "Error al obtener ID";
        }

        lblTitulo.setText("Productos del Envío: " + idEnvio);

        // Obtener la lista de productos (ajusta según tu modelo)
        List<Producto> listaProductos = null;
        try {
            listaProductos = envio.getListaProductos(); // Ajusta el nombre del método
        } catch (Exception e) {
            listaProductos = null;
        }

        ObservableList<Producto> productos = FXCollections.observableArrayList();

        if (listaProductos != null) {
            productos.setAll(listaProductos);
        }

        tablaProductos.setItems(productos);

        // Configurar columnas con manejo de errores
        colNombreProducto.setCellValueFactory(cellData -> {
            Producto p = cellData.getValue();
            return p != null ? new javafx.beans.property.SimpleStringProperty(p.getNombre()) : new javafx.beans.property.SimpleStringProperty("");
        });
        colPeso.setCellValueFactory(cellData -> {
            Producto p = cellData.getValue();
            return p != null ? new javafx.beans.property.SimpleStringProperty(String.valueOf(p.getPeso())) : new javafx.beans.property.SimpleStringProperty("");
        });
        colIDProducto.setCellValueFactory(cellData -> {
            Producto p = cellData.getValue();
            return p != null ? new javafx.beans.property.SimpleStringProperty(String.valueOf(p.getId())) : new javafx.beans.property.SimpleStringProperty("");
        });
    }

    @FXML
    void onRegresar(ActionEvent event) {
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onAyuda(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ayuda");
        alert.setHeaderText("¿Necesitas ayuda?");
        alert.setContentText("Aquí puedes encontrar información sobre los productos del envío.");
        alert.showAndWait();
    }

    @FXML
    void onContacto(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Contacto");
        alert.setHeaderText("¿Tienes alguna pregunta?");
        alert.setContentText("Puedes escribirnos a soporte@enviorapido.com");
        alert.showAndWait();
    }
}