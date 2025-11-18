package PFPlataformaLogistica.model;

public class Direccion {
    private String idDireccion;
    private String alias;
    private String calle;
    private String ciudad;
    private double latitud;
    private double longitud;

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

        public DireccionBuilder idDireccion(String idDireccion) {
            this.idDireccion = idDireccion;
            return this;
        }

        public DireccionBuilder alias(String alias) {
            this.alias = alias;
            return this;
        }

        public DireccionBuilder calle(String calle) {
            this.calle = calle;
            return this;
        }

        public DireccionBuilder ciudad(String ciudad) {
            this.ciudad = ciudad;
            return this;
        }

        public DireccionBuilder latitud(double latitud) {
            this.latitud = latitud;
            return this;
        }

        public DireccionBuilder longitud(double longitud) {
            this.longitud = longitud;
            return this;
        }

        public Direccion build() {
            // Validaciones obligatorias
            if (idDireccion == null || idDireccion.trim().isEmpty()) {
                throw new IllegalStateException("El ID de dirección es obligatorio");
            }
            if (calle == null || calle.trim().isEmpty()) {
                throw new IllegalStateException("La calle es obligatoria");
            }
            if (ciudad == null || ciudad.trim().isEmpty()) {
                throw new IllegalStateException("La ciudad es obligatoria");
            }

            // Generar coordenadas aleatorias si no se proporcionaron
            if (latitud == 0.0 && longitud == 0.0) {
                this.latitud = -34.6037 + (Math.random() * 0.1); // Coordenadas alrededor de Buenos Aires
                this.longitud = -58.3816 + (Math.random() * 0.1);
            }

            return new Direccion(this);
        }
    }

    @Override
    public String toString() {
        return String.format("Direccion{ID: '%s', Alias: '%s', Calle: '%s', Ciudad: '%s', Coord: (%.6f, %.6f)}",
                idDireccion, alias, calle, ciudad, latitud, longitud);
    }

    // Método para mostrar dirección formateada
    public String toFormattedString() {
        if (alias != null && !alias.isEmpty()) {
            return String.format("%s - %s, %s", alias, calle, ciudad);
        } else {
            return String.format("%s, %s", calle, ciudad);
        }
    }

    // Método para validar coordenadas
    public boolean coordenadasValidas() {
        return latitud >= -90 && latitud <= 90 && longitud >= -180 && longitud <= 180;
    }
}