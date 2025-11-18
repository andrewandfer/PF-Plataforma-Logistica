package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class DetallesUsuarioController {

    @FXML
    private Label lblTitulo;

    @FXML
    private TableView<Direccion> tablaDirecciones;
    @FXML
    private TableColumn<Direccion, String> colCalle;
    @FXML
    private TableColumn<Direccion, String> colAlias;
    @FXML
    private TableColumn<Direccion, String> colCiudad;

    @FXML
    private TableView<Producto> tablaProductos;
    @FXML
    private TableColumn<Producto, String> colNombreProducto;
    @FXML
    private TableColumn<Producto, String> colPeso;
    @FXML
    private TableColumn<Producto, String> colIDProducto;

    @FXML
    private TableView<Envio> tablaEnvios;
    @FXML
    private TableColumn<Envio, String> colFechaCreacion;
    @FXML
    private TableColumn<Envio, String> colFechaEstimada;
    @FXML
    private TableColumn<Envio, String> colIDEnvio;
    @FXML
    private TableColumn<Envio, Integer> colPesoEnvio;
    @FXML
    private TableColumn<Envio, TipoEnvio> colTipoEnvio;
    @FXML
    private TableColumn<Envio, EstadoEnvio> colEstadoEnvio;
    @FXML
    private TableColumn<Envio, Tarifa> colTarifa;
    @FXML
    private TableColumn<Envio, Repartidor> colRepartidorID;
    @FXML
    private TableColumn<Envio, String> colOrigen;
    @FXML
    private TableColumn<Envio, String> colDestino;
    @FXML
    private TableColumn<Envio, Integer> colCosto;
    @FXML
    private TableColumn<Envio, Direccion> colDireccion;

    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnAyuda;
    @FXML
    private Button btnContacto;

    private Usuario usuarioActual;

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        lblTitulo.setText("Detalles del Usuario: " + usuario.getTelefono());

        // Cargar direcciones
        ObservableList<Direccion> direcciones = FXCollections.observableArrayList(usuario.getListaDirecciones());
        tablaDirecciones.setItems(direcciones);
        colCalle.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCalle()));
        colAlias.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAlias()));
        colCiudad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCiudad()));

        // Cargar productos
        ObservableList<Producto> productos = FXCollections.observableArrayList(usuario.getListaProductos());
        tablaProductos.setItems(productos);
        colNombreProducto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        colPeso.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getPeso())));
        colIDProducto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getId())));

        // Cargar envíos
        ObservableList<Envio> envios = FXCollections.observableArrayList(usuario.getEnviosPropios());
        tablaEnvios.setItems(envios);

        // Configurar columnas de envíos
        colFechaCreacion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFechaCreacion()));
        colFechaEstimada.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFechaEstimada()));
        colIDEnvio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIdEnvio()));
        colPesoEnvio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getPesoEnvio()).asObject());
        colTipoEnvio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTipoEnvio()));
        colEstadoEnvio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getEstadoEnvio()));
        colTarifa.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTarifa()));
        colRepartidorID.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getRepartidor()));
        colOrigen.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOrigen()));
        colDestino.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDestino()));
        colCosto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCosto()).asObject());
        colDireccion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDireccion()));

        // Añadir evento de doble clic a la tabla de envíos
        tablaEnvios.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Envio envioSeleccionado = tablaEnvios.getSelectionModel().getSelectedItem();
                if (envioSeleccionado != null) {
                    abrirVentanaDetallesProductosEnvio(envioSeleccionado);
                }
            }
        });
    }

    private void abrirVentanaDetallesProductosEnvio(Envio envio) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PFPlataformaLogistica/view/DetallesProductosEnvioView.fxml"));
            Parent root = loader.load();

            DetallesProductosEnvioControler controller = loader.getController();
            controller.setEnvio(envio);

            Stage stage = new Stage();
            stage.setTitle("Productos del Envío: " + envio.getIdEnvio());
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo abrir la ventana de productos del envío.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
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
        alert.setContentText("Aquí puedes encontrar información sobre los detalles del usuario.");
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