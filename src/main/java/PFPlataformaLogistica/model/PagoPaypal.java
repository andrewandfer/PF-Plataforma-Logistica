package PFPlataformaLogistica.model;

public class PagoPaypal implements Pago {
    private String correo;

    public PagoPaypal(String correo) {
        this.correo = correo;
    }

    @Override
    public String procesarPago(double monto) {
        return "PAGO EXITOSO nro de cuenta PayPal: " + correo + " por un monto de $" + String.format("%.2f", monto);
    }

}
