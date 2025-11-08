package PFPlataformaLogistica.model;

public class Direccion {
    private String idDireccion;
    private String alias;
    private String calle;
    private String ciudad;

    public String getIdDireccion() {
        return idDireccion;
    }

    public String getAlias() {
        return alias;
    }

    public String getCalle() {
        return calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setIdDireccion(String idDireccion) {
        this.idDireccion = idDireccion;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    private Direccion(DireccionBuilder builder) {
        this.idDireccion = builder.idDireccion;
        this.alias = builder.alias;
        this.calle = builder.calle;
        this.ciudad = builder.ciudad;
    }

    public static class DireccionBuilder {
        private String idDireccion;
        private String alias;
        private String calle;
        private String ciudad;

        public DireccionBuilder  idDireccion(String idDireccion) {
            this.idDireccion = idDireccion;
            return this;
        }

        public DireccionBuilder  alias(String alias) {
            this.alias = alias;
            return this;
        }

        public DireccionBuilder  calle(String calle) {
            this.calle = calle;
            return this;
        }

        public DireccionBuilder  ciudad(String ciudad) {
            this.ciudad = ciudad;
            return this;
        }

        // Método para construir el objeto final
        public Direccion build() {
            if (idDireccion == null || idDireccion.isEmpty()) {
                throw new IllegalStateException("El ID de dirección es obligatorio");
            }
            return new Direccion(this);
        }
    }


    @Override
    public String toString() {
        return "Direccion{" +
                "idDireccion='" + idDireccion + '\'' +
                ", alias='" + alias + '\'' +
                ", calle='" + calle + '\'' +
                ", ciudad='" + ciudad + '\'' +
                '}';
    }
}
