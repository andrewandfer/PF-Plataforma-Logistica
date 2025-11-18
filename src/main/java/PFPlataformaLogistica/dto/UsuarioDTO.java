package PFPlataformaLogistica.dto;

import PFPlataformaLogistica.model.Direccion;
import PFPlataformaLogistica.model.Producto;
import PFPlataformaLogistica.model.Envio;

import java.io.Serializable;
import java.util.LinkedList;

public class UsuarioDTO implements Serializable {

    private String nombre;
    private int edad;
    private String contrasena;
    private String telefono;
    private String email;
    private Direccion direccion;
    private LinkedList<Direccion> listaDirecciones;
    private LinkedList <Producto> listaProductos  ;
    private LinkedList<Envio> enviosPropios;

    //Constructor vacío
    public UsuarioDTO() {
    }

    // Constructor con parámetros
    public UsuarioDTO(String nombre, int edad, String contrasena,
                      String telefono,String email, Direccion direccion,
                      LinkedList<Direccion> listaDirecciones, LinkedList <Producto>listaProductos,
                      LinkedList<Envio> enviosPropios) {
        this.nombre = nombre;
        this.edad = edad;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.listaDirecciones = new LinkedList<>(listaDirecciones);
        this.listaProductos = new LinkedList<>();
        this.enviosPropios = enviosPropios;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public LinkedList<Direccion> getListaDirecciones() {
        return listaDirecciones;
    }

    public void setListaDirecciones(LinkedList<Direccion> listaDirecciones) {
        this.listaDirecciones = listaDirecciones;
    }

    public LinkedList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(LinkedList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public LinkedList<Envio> getEnviosPropios() {
        return enviosPropios;
    }

    public void setEnviosPropios(LinkedList<Envio> enviosPropios) {
        this.enviosPropios = enviosPropios;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


