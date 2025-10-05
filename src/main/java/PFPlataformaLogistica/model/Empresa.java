package PFPlataformaLogistica.model;

public final class Empresa {
    private String nombre;
    private static Empresa instancia;
    private Empresa() {
        this.nombre = "MiEmpresa";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public static Empresa getInstance() {
        if (instancia == null) {
            instancia = new Empresa();
        }
        return instancia;
    }
}

