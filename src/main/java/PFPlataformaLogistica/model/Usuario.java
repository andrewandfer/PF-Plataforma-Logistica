package PFPlataformaLogistica.model;

import java.util.LinkedList;

public class Usuario extends Persona {
    private String telefono;
    private Direccion direccion;
    private LinkedList<String> listaDirecciones;
    private LinkedList<Producto> listaProductos;
    private LinkedList<Envio> enviosPropios;

    // Constructor protegido: solo accesible por el builder
    protected Usuario(UsuarioBuilder builder) {
        super(builder);
        this.telefono = builder.telefono;
        this.direccion = builder.direccion;
        this.listaDirecciones = builder.listaDirecciones;
        this.listaProductos = builder.listaProductos;
        this.enviosPropios = builder.enviosPropios;
    }

    // Getters y setters

    public String getTelefono() {
        return telefono;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public LinkedList<String> getListaDirecciones() {
        return listaDirecciones;
    }

    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }
    public void setListaDirecciones(LinkedList<String> listaDirecciones) { this.listaDirecciones = listaDirecciones; }
    public void setEnviosPropios(LinkedList<Envio> enviosPropios) { this.enviosPropios = enviosPropios; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Builder interno
    public static class UsuarioBuilder extends PersonaBuilder<UsuarioBuilder> {
        private String telefono;
        private Direccion direccion;
        private LinkedList<String> listaDirecciones;
        private LinkedList<Producto> listaProductos;
        private LinkedList<Envio> enviosPropios;

        public UsuarioBuilder telefono(String telefono) {
            this.telefono = telefono;
            return self();
        }

        public UsuarioBuilder direccion(Direccion direccion) {
            this.direccion = direccion;
            return self();
        }

        public UsuarioBuilder listaDirecciones(LinkedList<String> listaDirecciones) {
            this.listaDirecciones = listaDirecciones;
            return self();
        }

        public UsuarioBuilder listaProductos(LinkedList<Producto> listaProductos) {
            this.listaProductos = listaProductos;
            return self();
        }

        public UsuarioBuilder enviosPropios(LinkedList<Envio> enviosPropios) {
            this.enviosPropios = enviosPropios;
            return self();
        }

        @Override
        protected UsuarioBuilder self() {
            return this;
        }

        @Override
        public Usuario build() {
            return new Usuario(this);
        }
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }

    public LinkedList<Envio> getEnviosPropios() {
        return enviosPropios;
    }

    public LinkedList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(LinkedList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }
}
