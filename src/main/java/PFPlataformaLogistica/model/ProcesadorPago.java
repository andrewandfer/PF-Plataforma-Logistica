package PFPlataformaLogistica.model;

public class ProcesadorPago {
    private Pago metodoPago;

    public void setMetodoPago(Pago metodoPago) { this.metodoPago = metodoPago; }

    public String ejecutarPago(double monto) {
        if (metodoPago == null)
            return "No se seleccionó un método de pago.";
        else {
            return "pago realizado con exito.:";
        }
    }

}