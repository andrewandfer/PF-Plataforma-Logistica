package PFPlataformaLogistica.model;

import java.util.List;

public class Paquete {
    //Atributos
    private int id;
    private double peso;
    private String ciudad;
    private String direccion;
    private Usuario usuario;
    private List<Producto> productos;

    private Paquete (Builder builder){
        this.id = builder.id;
        this.peso = builder.peso;
        this.ciudad = builder.ciudad;
        this.direccion = builder.direccion;
        this.usuario = builder.usuario;
        this.productos = builder.productos;
    }

    public static class Builder{
        private int id;
        private double peso;
        private String ciudad;
        private String direccion;
        private Usuario usuario;
        private List<Producto> productos;

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
        public Builder direccion (String direccion){
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
    }
}
