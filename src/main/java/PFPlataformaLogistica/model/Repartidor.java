package PFPlataformaLogistica.model;

import java.util.List;

public class Repartidor extends Persona{
    private String telefono;
    private boolean disponibilidad;
    private String zonaCobertura;
    private String localidad;
    private List<Envio>EnviosAsignados;

    public Repartidor(Builder builder) {
        super(PersonaBuilder);
        this.telefono = builder.telefono;
        this.disponibilidad = builder.disponibilidad;
        this.zonaCobertura = builder.zonaCobertura;
        this.localidad = builder.localidad;
        EnviosAsignados = builder.EnviosAsignados;
    }

    public static class Builder {
        private String telefono;
        private boolean disponibilidad;
        private String zonaCobertura;
        private String localidad;
        private List<Envio>EnviosAsignados;
        public Builder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }
        public Builder disponibilidad(boolean disponibilidad) {
            this.disponibilidad = disponibilidad;
            return this;
        }
        public Builder zonaCobertura(String zonaCobertura) {
            this.zonaCobertura = zonaCobertura;
            return this;
        }
        public Builder localidad(String localidad) {
            this.localidad = localidad;
            return this;
        }
        public Builder enviosAsignados(List<Envio> enviosAsignados) {
            this.EnviosAsignados = enviosAsignados;
            return this;
        }
        public Repartidor build() {
            return new Repartidor(this);
        }

    }
}





