package PFPlataformaLogistica.model;

import java.util.List;

public class Repartidor extends Persona implements IRepartidorComponent{
    private String telefono;
    private  EstadoRepartidor disponibilidad;
    private String zonaCobertura;
    private String localidad;
    private List<Envio> enviosAsignados;

    // Constructor protegido para que solo el Builder lo use
    protected Repartidor(RepartidorBuilder builder) {
        super(builder);
        this.telefono = builder.telefono;
        this.disponibilidad = builder.disponibilidad;
        this.zonaCobertura = builder.zonaCobertura;
        this.localidad = builder.localidad;
        this.enviosAsignados = builder.enviosAsignados;
    }

    // Getters y setters
    public String getTelefono() {
        return telefono;
    }

    public EstadoRepartidor getEstadoDisponibilidad() {
        return disponibilidad;
    }

    public String getZonaCobertura() {
        return zonaCobertura;
    }

    public String getLocalidad() {
        return localidad;
    }

    public List<Envio> getEnviosAsignados() {
        return enviosAsignados;
    }

    public void setEstadoDisponibilidad(EstadoRepartidor estadoRepartidor) {
        this.disponibilidad = estadoRepartidor;
    }
// RF-020: Cambia la disponibilidad del repartidor (Activo/Inactivo/En ruta).

    public void cambiarDisponibilidad(EstadoRepartidor nuevoEstado) {
        this.disponibilidad = nuevoEstado;
        System.out.println("Estado de disponibilidad actualizado a: " + nuevoEstado);
    }
 //RF-021: Muestra los envíos asignados al repartidor.

    public void consultarEnviosAsignados() {
        System.out.println("📦 Envíos asignados a " + getNombre() + ":");
        if (enviosAsignados == null || enviosAsignados.isEmpty()) {
            System.out.println("No hay envíos asignados actualmente.");
        } else {
            enviosAsignados.forEach(envio ->
                    System.out.println(" - " + envio.toString()));
        }
    }
    /**
     * RF-019: Muestra la información completa del repartidor.
     */
    @Override
    public void mostrarInfo() {
        System.out.println(this.toString());
    }




    // Clase Builder que extiende de PersonaBuilder
    public static class RepartidorBuilder extends Persona.PersonaBuilder<RepartidorBuilder> {
        private String telefono;
        private EstadoRepartidor disponibilidad;
        private String zonaCobertura;
        private String localidad;
        private List<Envio> enviosAsignados;

        public RepartidorBuilder telefono(String telefono) {
            this.telefono = telefono;
            return self();
        }

        public RepartidorBuilder disponibilidad(EstadoRepartidor disponibilidad) {
            this.disponibilidad = disponibilidad;
            return self();
        }

        public RepartidorBuilder zonaCobertura(String zonaCobertura) {
            this.zonaCobertura = zonaCobertura;
            return self();
        }

        public RepartidorBuilder localidad(String localidad) {
            this.localidad = localidad;
            return self();
        }

        public RepartidorBuilder enviosAsignados(List<Envio> enviosAsignados) {
            this.enviosAsignados = enviosAsignados;
            return self();
        }

        @Override
        protected RepartidorBuilder self() {
            return this;
        }

        @Override
        public Repartidor build() {
            return new Repartidor(this);
        }

    }





    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDisponibilidad(EstadoRepartidor disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public void setZonaCobertura(String zonaCobertura) {
        this.zonaCobertura = zonaCobertura;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public void setEnviosAsignados(List<Envio> enviosAsignados) {
        this.enviosAsignados = enviosAsignados;
    }

    @Override
    public String toString() {
        return "Repartidor{" +
                "telefono='" + telefono + '\'' +
                ", disponibilidad=" + disponibilidad +
                ", zonaCobertura='" + zonaCobertura + '\'' +
                ", localidad='" + localidad + '\'' +
                ", enviosAsignados=" + enviosAsignados +
                ", email='" + email + '\'' +
                ", contrasena='" + contrasena + '\'' +
                '}';
    }
}






