package PFPlataformaLogistica.model;

import java.util.ArrayList;
import java.util.List;

public class Envio {
    private Producto producto;
    private String fechaCreacion;
    private String fechaEstimada;
    private List listaDirecciones=new ArrayList();
    private String idEnvio;
    private int pesoEnvio;
    private TipoEnvio tipoEnvio;
    private EstadoEnvio estadoEnvio;
    private Tarifa tarifa;

    public Envio(Producto producto, String fechaCreacion, String fechaEstimada, String idEnvio, int pesoEnvio, TipoEnvio tipoEnvio, EstadoEnvio estadoEnvio, List listaDirecciones,Tarifa tarifa) {
        this.producto = producto;
        this.fechaCreacion = fechaCreacion;
        this.fechaEstimada = fechaEstimada;
        this.idEnvio = idEnvio;
        this.pesoEnvio = pesoEnvio;
        this.tipoEnvio = tipoEnvio;
        this.estadoEnvio = estadoEnvio;
        this.listaDirecciones = listaDirecciones;
        this.tarifa=tarifa;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
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

    public List getListaDirecciones() {
        return listaDirecciones;
    }

    public void setListaDirecciones(List listaDirecciones) {
        this.listaDirecciones = listaDirecciones;
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

    @Override
    public String toString() {
        return "Envio{" +
                "producto=" + producto +
                ", fechaCreacion='" + fechaCreacion + '\'' +
                ", fechaEstimada='" + fechaEstimada + '\'' +
                ", listaDirecciones=" + listaDirecciones +
                ", idEnvio='" + idEnvio + '\'' +
                ", pesoEnvio=" + pesoEnvio +
                '}';
    }
}
