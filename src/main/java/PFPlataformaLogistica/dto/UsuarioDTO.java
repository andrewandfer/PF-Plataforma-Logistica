package PFPlataformaLogistica.dto;

import PFPlataformaLogistica.model.Producto;
import PFPlataformaLogistica.model.Envio;

import java.io.Serializable;
import java.util.LinkedList;

public class UsuarioDTO implements Serializable {

    private String nombre;
    private int edad;
    private String contrasena;
    private String correo;
    private String telefono;
    private String direccion;
    private LinkedList<String> listaDirecciones;
    private LinkedList <Producto> listaProductos  ;
    private LinkedList<Envio> enviosPropios;

    //Constructor vacío
    public UsuarioDTO() {
    }

    // Constructor con parámetros
    public UsuarioDTO(String nombre, int edad, String contrasena, String correo,
                      String telefono, String direccion,
                      LinkedList<String> listaDirecciones, LinkedList <Producto>listaProductos,
                      LinkedList<Envio> enviosPropios) {
        this.nombre = nombre;
        this.edad = edad;
        this.contrasena = contrasena;
        this.correo = correo;
        this.telefono = telefono;
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

}


