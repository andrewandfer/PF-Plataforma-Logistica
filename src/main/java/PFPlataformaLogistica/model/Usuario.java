package PFPlataformaLogistica.model;
import java.util.LinkedList;

public class Usuario extends Persona{

    private String correo;
    private String telefono;
    private String direccion;
    private LinkedList<String> listaDirecciones;
    private Producto producto   ;

    public Usuario(String nombre, String id, String correo, String telefono, String direccion, LinkedList<String> listaDirecciones, Producto producto) {
        super(nombre, id);
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.listaDirecciones = listaDirecciones;
        this.producto = producto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LinkedList<String> getListaDirecciones() {
        return listaDirecciones;
    }

    public void setListaDirecciones(LinkedList<String> listaDirecciones) {
        this.listaDirecciones = listaDirecciones;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}