package PFPlataformaLogistica.model;

import java.util.List;

public class Repartidor extends Persona {
    private String telefono;
    private boolean disponibilidad;
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

    public boolean isDisponibilidad() {
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

    // Clase Builder que extiende de PersonaBuilder
    public static class RepartidorBuilder extends Persona.PersonaBuilder<RepartidorBuilder> {
        private String telefono;
        private boolean disponibilidad;
        private String zonaCobertura;
        private String localidad;
        private List<Envio> enviosAsignados;

        public RepartidorBuilder telefono(String telefono) {
            this.telefono = telefono;
            return self();
        }

        public RepartidorBuilder disponibilidad(boolean disponibilidad) {
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

    public void setDisponibilidad(boolean disponibilidad) {
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






