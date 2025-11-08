package PFPlataformaLogistica.model;

import java.util.List;
import java.util.LinkedList;

public class Paquete {
    //Atributos
    private int id;
    private double peso;
    private String ciudad;
    private Direccion direccion;
    private Usuario usuario;


    private Paquete (Builder builder){
        this.id = builder.id;
        this.peso = builder.peso;
        this.ciudad = builder.ciudad;
        this.direccion = builder.direccion;
        this.usuario = builder.usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public static class Builder{
        private int id;
        private double peso;
        private String ciudad;
        private Direccion direccion;
        private Usuario usuario;

        public Builder id (int id){
            this.id = id;
            return this;
        }
        public Builder peso (double peso){
            this.peso = peso;
            return this;
        }
        public Builder ciudad (String ciudad){
            this.ciudad = ciudad;
            return this;
        }
        public Builder direccion (Direccion direccion){
            this.direccion = direccion;
            return this;
        }
        public Builder usuario (Usuario usuario){
            this.usuario = usuario;
            return this;
        }
        public Paquete build(){
            return new Paquete(this);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCiudad() {
            return ciudad;
        }

        public void setCiudad(String ciudad) {
            this.ciudad = ciudad;
        }

        public double getPeso() {
            return peso;
        }

        public void setPeso(double peso) {
            this.peso = peso;
        }

        public Direccion getDireccion() {
            return direccion;
        }

        public void setDireccion(Direccion direccion) {
            this.direccion = direccion;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }
    }
}
