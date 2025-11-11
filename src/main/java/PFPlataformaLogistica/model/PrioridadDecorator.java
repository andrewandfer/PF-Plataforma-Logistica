package PFPlataformaLogistica.model;

public class PrioridadDecorator extends ServicioDecorator {
    private static final float COSTO_PRIORIDAD = 5000f;

    public PrioridadDecorator(ServicioEnvio servicioBase) {
        super(servicioBase);
    }

    @Override
    public float calcularCosto() {
        return servicioBase.calcularCosto() + COSTO_PRIORIDAD;
    }

    @Override
    public String obtenerDescripcion() {
        return servicioBase.obtenerDescripcion() + "\n+ Entrega Prioritaria ($5,000)";
    }
}
