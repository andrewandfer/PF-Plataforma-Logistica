package PFPlataformaLogistica.model;

public class Direccion {
    private String idDireccion;
    private String alias;
    private String calle;
    private String ciudad;
    private double latitud;
    private double longitud;

    public Direccion() {} // Constructor vacío para compatibilidad

    private Direccion(DireccionBuilder builder) {
        this.idDireccion = builder.idDireccion;
        this.alias = builder.alias;
        this.calle = builder.calle;
        this.ciudad = builder.ciudad;
        this.latitud = builder.latitud;
        this.longitud = builder.longitud;
    }

    // Getters y Setters
    public String getIdDireccion() { return idDireccion; }
    public String getAlias() { return alias; }
    public String getCalle() { return calle; }
    public String getCiudad() { return ciudad; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }

    public void setIdDireccion(String idDireccion) { this.idDireccion = idDireccion; }
    public void setAlias(String alias) { this.alias = alias; }
    public void setCalle(String calle) { this.calle = calle; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public void setLatitud(double latitud) { this.latitud = latitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    // Builder
    public static class DireccionBuilder {
        private String idDireccion;
        private String alias;
        private String calle;
        private String ciudad;
        private double latitud;
        private double longitud;

        public DireccionBuilder idDireccion(String idDireccion) { this.idDireccion = idDireccion; return this; }
        public DireccionBuilder alias(String alias) { this.alias = alias; return this; }
        public DireccionBuilder calle(String calle) { this.calle = calle; return this; }
        public DireccionBuilder ciudad(String ciudad) { this.ciudad = ciudad; return this; }
        public DireccionBuilder latitud(double latitud) { this.latitud = latitud; return this; }
        public DireccionBuilder longitud(double longitud) { this.longitud = longitud; return this; }

        public Direccion build() {
            if (idDireccion == null || idDireccion.isEmpty())
                throw new IllegalStateException("El ID de dirección es obligatorio");
            if (ciudad == null || ciudad.isEmpty())
                throw new IllegalStateException("La ciudad es obligatoria");
            if (calle == null || calle.isEmpty())
                throw new IllegalStateException("La calle es obligatoria");

            if (latitud == 0 && longitud == 0) {
                latitud = Math.random() * 90;
                longitud = Math.random() * 180;
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
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}
