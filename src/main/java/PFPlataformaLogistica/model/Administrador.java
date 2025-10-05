public class   Administrador extends Persona{
    private int sueldo ;

    public Administrador(int sueldo) {
        super(nombre,id);
        this.sueldo = sueldo;
    }

    public int getSueldo() {
        return sueldo;
    }

    public void setSueldo(int sueldo) {
        this.sueldo = sueldo;
    }
}