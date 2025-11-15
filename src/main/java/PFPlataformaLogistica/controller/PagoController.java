package PFPlataformaLogistica.controller;

import PFPlataformaLogistica.model.Pago;
import PFPlataformaLogistica.model.PagoFactory;
import PFPlataformaLogistica.model.ProcesadorPago;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class PagoController {

    @FXML
    private void procesarPago() {
        double monto = 25000; // Costo del envío

        // Obtener método de pago seleccionado por el usuario
        String metodoSeleccionado = cbMetodoPago.getValue(); // ComboBox
        String dato = txtDatoPago.getText(); // TextField (correo, tarjeta, etc.)

        // ========== USAR ADAPTER CON FACTORY ==========
        try {
            Pago metodoPago = PagoFactory.crearMetodoPago(metodoSeleccionado, dato);

            ProcesadorPago procesador = new ProcesadorPago(metodoPago);
            String resultado = procesador.ejecutarPago(monto);

            mostrarAlerta("Pago Exitoso", resultado, Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo procesar el pago: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String error, String s, Alert.AlertType alertType) {
    }
}
