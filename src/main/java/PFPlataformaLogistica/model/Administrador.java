package PFPlataformaLogistica.model;

public class   Administrador extends Persona{
    private int sueldo ;

    public Administrador(String nombre, String id, int sueldo, String) {
        super(PersonaBuilder); 
        this.sueldo = sueldo;
    }

    public int getSueldo() {
        return sueldo;
    }

    public void setSueldo(int sueldo) {
        this.sueldo = sueldo;
    }
}