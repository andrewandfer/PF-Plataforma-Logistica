package PFPlataformaLogistica.dto;

import PFPlataformaLogistica.model.Envio;

import java.util.List;

public class RepartidorDTO {

    private String telefono;
    private boolean disponibilidad;
    private String zonaCobertura;
    private String localidad;
    private List<Envio> EnviosAsignados;

}
