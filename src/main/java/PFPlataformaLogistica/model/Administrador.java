package PFPlataformaLogistica.model;

public class Administrador extends Persona {
    private int sueldo;

    // Constructor protegido: recibe el Builder
    protected Administrador(AdministradorBuilder builder) {
        super(builder);
        this.sueldo = builder.sueldo;
    }

    public int getSueldo() {
        return sueldo;
    }

    public void setSueldo(int sueldo) {
        this.sueldo = sueldo;
    }

    // Builder específico para Administrador
    public static class AdministradorBuilder extends Persona.PersonaBuilder<AdministradorBuilder> {
        private int sueldo;

        public AdministradorBuilder sueldo(int sueldo) {
            this.sueldo = sueldo;
            return self();
        }

        @Override
        protected AdministradorBuilder self() {
            return this;
        }

        @Override
        public Administrador build() {
            return new Administrador(this);
        }
    }
}
