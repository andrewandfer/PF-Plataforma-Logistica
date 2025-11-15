package PFPlataformaLogistica.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Envio {
    private LinkedList<Producto> productosEnvio;
    private String fechaCreacion;
    private String fechaEstimada;
    private String idEnvio;
    private int pesoEnvio;
    private TipoEnvio tipoEnvio;
    private EstadoEnvio estadoEnvio;
    private Tarifa tarifa;
    private Repartidor repartidor;
    private String origen;
    private String destino;
    private int costo;

    public Envio(LinkedList<Producto> productosEnvio, String fechaCreacion, String fechaEstimada, String idEnvio, int pesoEnvio, TipoEnvio tipoEnvio, EstadoEnvio estadoEnvio, Tarifa tarifa, Repartidor repartidor, String origen, String destino, int costo) {
        this.productosEnvio = productosEnvio;
        this.fechaCreacion = fechaCreacion;
        this.fechaEstimada = fechaEstimada;
        this.idEnvio = idEnvio;
        this.pesoEnvio = pesoEnvio;
        this.tipoEnvio = tipoEnvio;
        this.estadoEnvio = estadoEnvio;
        this.tarifa = tarifa;
        this.repartidor = repartidor;
        this.origen = origen;
        this.destino = destino;
        this.costo = costo;
    }

    public LinkedList<Producto> getProductosEnvio() {
        return productosEnvio;
    }

    public void setProductosEnvio(LinkedList<Producto> productosEnvio) {
        this.productosEnvio = productosEnvio;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaEstimada() {
        return fechaEstimada;
    }

    public void setFechaEstimada(String fechaEstimada) {
        this.fechaEstimada = fechaEstimada;
    }

    public String getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(String idEnvio) {
        this.idEnvio = idEnvio;
    }

    public int getPesoEnvio() {
        return pesoEnvio;
    }

    public void setPesoEnvio(int pesoEnvio) {
        this.pesoEnvio = pesoEnvio;
    }

    public TipoEnvio getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(TipoEnvio tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public EstadoEnvio getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(EstadoEnvio estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }
    public Tarifa getTarifa() {
        return tarifa;
    }
    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    @Override
    public String toString() {
        return "Envio{" +
                "productosEnvio=" + productosEnvio +
                ", fechaCreacion='" + fechaCreacion + '\'' +
                ", fechaEstimada='" + fechaEstimada + '\'' +
                ", idEnvio='" + idEnvio + '\'' +
                ", pesoEnvio=" + pesoEnvio +
                ", tipoEnvio=" + tipoEnvio +
                ", estadoEnvio=" + estadoEnvio +
                ", tarifa=" + tarifa +
                ", repartidor=" + repartidor +
                '}';
    }
}
