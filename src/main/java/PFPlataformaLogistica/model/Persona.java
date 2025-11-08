package PFPlataformaLogistica.model;

public class Persona {
    protected String nombre;
    protected int edad;
    protected String contrasena;
    protected String id;

    // Constructor protegido para que solo el builder pueda usarlo
    protected Persona(PersonaBuilder<?> builder) {
        this.nombre = builder.nombre;
        this.edad = builder.edad;
        this.contrasena = builder.contrasena;
        this.id= builder.id;
    }

    // Builder genérico (usa self-type para herencia fluida)
    public static class PersonaBuilder<T extends PersonaBuilder<T>> {
        private String nombre;
        private int edad;
        private String contrasena;
        private String id;

        public T nombre(String nombre) {
            this.nombre = nombre;
            return self();
        }

        public T edad(int edad) {
            this.edad = edad;
            return self();
        }

        public T contrasena(String contrasena) {
            this.contrasena = contrasena;
            return self();
        }
        public T id(String id) {
            this.id = id;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
