package PFPlataformaLogistica.model;

public class Pago {

  private String idPago;
  private String fecha;
  private int monto;

    public String getIdPago() {
        return idPago;
    }

    public void setIdPago(String idPago) {
        this.idPago = idPago;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public Pago(String idPago, String fecha, int monto) {

        this.idPago = idPago;
        this.fecha = fecha;
        this.monto = monto;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "idPago='" + idPago + '\'' +
                ", fecha='" + fecha + '\'' +
                ", monto=" + monto +
                '}';
    }
}
