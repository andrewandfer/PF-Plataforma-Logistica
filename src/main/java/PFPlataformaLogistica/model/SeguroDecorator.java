package PFPlataformaLogistica.model;

public class SeguroDecorator extends ServicioDecorator {
    private static final float COSTO_SEGURO = 3000f;

    public SeguroDecorator(ServicioEnvio servicioBase) {
        super(servicioBase);
    }

    @Override
    public float calcularCosto() {
        return servicioBase.calcularCosto() + COSTO_SEGURO;
    }

    @Override
    public String obtenerDescripcion() {
        return servicioBase.obtenerDescripcion() + "\n+ Seguro ($3,000)";
    }
}