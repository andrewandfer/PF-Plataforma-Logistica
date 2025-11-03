package PFPlataformaLogistica.model;

public class Persona {
    protected String nombre;
    protected int edad;
    protected String contrasena;

    // Constructor protegido para que solo el builder pueda usarlo
    protected Persona(PersonaBuilder<?> builder) {
        this.nombre = builder.nombre;
        this.edad = builder.edad;
        this.contrasena = builder.contrasena;
    }

    // Builder genérico (usa self-type para herencia fluida)
    public static class PersonaBuilder<T extends PersonaBuilder<T>> {
        private String nombre;
        private int edad;
        private String contrasena;

        public T nombre(String nombre) {
            this.nombre = nombre;
            return self();
        }

        public T edad(int edad) {
            this.edad = edad;
            return self();
        }

        public T contrasena(String contrasena) {
            this.edad = edad;
            return self();
        }

        //método protegido para devolver el tipo correcto
        protected T self() {
            return (T) this;
        }

        public Persona build() {
            return new Persona(this);
        }
    }
}
