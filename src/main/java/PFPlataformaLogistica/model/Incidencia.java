package PFPlataformaLogistica.model;

import java.util.Date;

public class Incidencia {
    private String id;
    private Envio envio;
    private String tipo; // "Cliente no encontrado", "Paquete dañado", "Dirección incorrecta"
    private String descripcion;
    private Date fecha;
    private EstadoEnvio estadoAnterior;

    public Incidencia(String id, Envio envio, String tipo, String descripcion) {
        this.id = id;
        this.envio = envio;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = new Date();
        this.estadoAnterior = envio.getEstadoEnvio();
    }

    // Getters
    public String getId() { return id; }
    public Envio getEnvio() { return envio; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public Date getFecha() { return fecha; }
    public EstadoEnvio getEstadoAnterior() { return estadoAnterior; }

    @Override
    public String toString() {
        return "Incidencia{" +
                "id='" + id + '\'' +
                ", envio=" + envio.getIdEnvio() +
                ", tipo='" + tipo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}