package PFPlataformaLogistica.dto;
import java.io.Serializable;

public class AdministradorDTO implements Serializable {
    private String nombre;
    private int sueldo;

    // Constructor vacío
    public AdministradorDTO() {
    }

    // Constructor con parámetros
    public AdministradorDTO(String nombre, int sueldo) {
        this.nombre = nombre;
        this.sueldo = sueldo;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getSueldo() {
        return sueldo;
    }
    public void setSueldo(int sueldo) {
        this.sueldo = sueldo;
    }


}

