package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.model.Envio;
import PFPlataformaLogistica.model.Repartidor;
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

public class DetallesRepartidorController {

    @FXML
    private Label lblTitulo;
    @FXML
    private TableView<Envio> tablaEnviosAsignados;
    @FXML
    private TableColumn<Envio, String> colIDEnvio;
    @FXML
    private TableColumn<Envio, String> colFechaCreacion;
    @FXML
    private TableColumn<Envio, String> colEstadoEnvio;
    @FXML
    private TableColumn<Envio, String> colOrigen;
    @FXML
    private TableColumn<Envio, String> colDestino;

    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnAyuda;
    @FXML
    private Button btnContacto;

    private Repartidor repartidorActual;

    public void setRepartidor(Repartidor repartidor) {
        this.repartidorActual = repartidor;
        lblTitulo.setText("Detalles del Repartidor: " + repartidor.getTelefono());

        // Cargar envíos asignados
        ObservableList<Envio> envios = FXCollections.observableArrayList(repartidor.getEnviosAsignados());
        tablaEnviosAsignados.setItems(envios);

        colIDEnvio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIdEnvio()));
        colFechaCreacion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFechaCreacion()));
        colEstadoEnvio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty(cellData.getValue().getEstadoEnvio()));
        colOrigen.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOrigen()));
        colDestino.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDestino()));

        // Añadir evento de doble clic a la tabla de envíos
        tablaEnviosAsignados.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Envio envioSeleccionado = tablaEnviosAsignados.getSelectionModel().getSelectedItem();
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
        alert.setContentText("Aquí puedes encontrar información sobre los detalles del repartidor.");
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