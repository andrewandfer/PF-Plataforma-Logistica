package PFPlataformaLogistica.model;

public class EnvioBasico implements ServicioEnvio {
    private float costoBase;
    private String descripcion;

    public EnvioBasico(float costoBase, String descripcion) {
        this.costoBase = costoBase;
        this.descripcion = descripcion;
    }

    @Override
    public float calcularCosto() {
        return costoBase;
    }

    @Override
    public String obtenerDescripcion() {
        return descripcion;
    }
}
