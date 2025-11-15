package PFPlataformaLogistica.model;

public class FragilDecorator extends ServicioDecorator {
    private static final float COSTO_FRAGIL = 2000f;

    public FragilDecorator(ServicioEnvio servicioBase) {
        super(servicioBase);
    }

    @Override
    public float calcularCosto() {
        return servicioBase.calcularCosto() + COSTO_FRAGIL;
    }

    @Override
    public String obtenerDescripcion() {
        return servicioBase.obtenerDescripcion() + "\n+ Paquete Frágil ($2,000)";
    }
}