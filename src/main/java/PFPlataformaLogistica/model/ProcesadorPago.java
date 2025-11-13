package PFPlataformaLogistica.model;

// Uso adpater y stregy

public class ProcesadorPago {
    private Pago metodoPago;

    public ProcesadorPago(Pago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public void setMetodoPago(Pago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String ejecutarPago(double monto) {
        if (metodoPago == null) {
            return " Error: No hay método de pago configurado";
        }

        System.out.println(" Procesando pago por: $" + String.format("%.2f", monto));
        String resultado = metodoPago.procesarPago(monto);
        System.out.println("" + resultado);
        return resultado;
    }
}