package PFPlataformaLogistica.dto;

import PFPlataformaLogistica.model.Envio;

import java.io.Serializable;
import java.util.List;

public class RepartidorDTO implements Serializable {

    private  String telefono;
    private boolean disponibilidad;
    private String zonaCobertura;
    private String localidad;
    private List<Envio> EnviosAsignados;
    private String id;

    public RepartidorDTO() {
    }

    public RepartidorDTO(String telefono, boolean disponibilidad, String zonaCobertura, String localidad, List<Envio> enviosAsignados, String id) {
        this.telefono = telefono;
        this.disponibilidad = disponibilidad;
        this.zonaCobertura = zonaCobertura;
        this.localidad = localidad;
        EnviosAsignados = enviosAsignados;
        this.id = id;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getZonaCobertura() {
        return zonaCobertura;
    }

    public void setZonaCobertura(String zonaCobertura) {
        this.zonaCobertura = zonaCobertura;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public List<Envio> getEnviosAsignados() {
        return EnviosAsignados;
    }

    public void setEnviosAsignados(List<Envio> enviosAsignados) {
        EnviosAsignados = enviosAsignados;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
