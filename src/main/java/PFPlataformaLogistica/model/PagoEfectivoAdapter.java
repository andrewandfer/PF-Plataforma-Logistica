package PFPlataformaLogistica.model;

public class PagoEfectivoAdapter implements Pago {
    private PagoEfectivo pagoEfectivo;

    public PagoEfectivoAdapter() {
        this.pagoEfectivo = new PagoEfectivo();
    }

    @Override
    public String procesarPago(double monto) {
        // Adapta la llamada a PagoEfectivo
        return pagoEfectivo.procesarPago(monto);
    }
}
