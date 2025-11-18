package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.model.Envio;
import PFPlataformaLogistica.model.Repartidor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DetalleEnvioProductoViewController {

    @FXML
    private Label lblTitulo;

    @FXML
    private TableView<Envio> tablaEnvios;
    @FXML
    private TableColumn<Envio, String> colIdEnvio;
    @FXML
    private TableColumn<Envio, String> colEstadoEnvio;
    @FXML
    private TableColumn<Envio, String> colOrigen;
    @FXML
    private TableColumn<Envio, String> colDestino;

    @FXML
    private Button btnRegresar;

    private Repartidor repartidorActual;

    public void setRepartidor(Repartidor repartidor) {
        this.repartidorActual = repartidor;
        lblTitulo.setText("Envíos del Repartidor: " + repartidor.getTelefono());

        ObservableList<Envio> envios = FXCollections.observableArrayList(repartidor.getEnviosAsignados());
        tablaEnvios.setItems(envios);

        colIdEnvio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIdEnvio()));
        colEstadoEnvio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEstadoEnvio().toString()));
        colOrigen.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOrigen()));
        colDestino.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDestino()));
    }

    @FXML
    void onRegresar(ActionEvent event) {
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.close();
    }
}