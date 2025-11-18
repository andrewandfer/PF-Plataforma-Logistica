package PFPlataformaLogistica.model;

public abstract class ServicioDecorator implements ServicioEnvio {
    protected ServicioEnvio servicioBase;

    public ServicioDecorator(ServicioEnvio servicioBase) {
        this.servicioBase = servicioBase;
    }

    @Override
    public float calcularCosto() {
        return servicioBase.calcularCosto();
    }

    @Override
    public String obtenerDescripcion() {
        return servicioBase.obtenerDescripcion();
    }

    public static class AdministradorView {
    }
}
