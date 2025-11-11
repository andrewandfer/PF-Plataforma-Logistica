package PFPlataformaLogistica.model;

public class FirmaDecorator extends ServicioDecorator {
    private static final float COSTO_FIRMA = 1500f;

    public FirmaDecorator(ServicioEnvio servicioBase) {
        super(servicioBase);
    }

    @Override
    public float calcularCosto() {
        return servicioBase.calcularCosto() + COSTO_FIRMA;
    }

    @Override
    public String obtenerDescripcion() {
        return servicioBase.obtenerDescripcion() + "\n+ Firma Requerida ($1,500)";
    }
}
