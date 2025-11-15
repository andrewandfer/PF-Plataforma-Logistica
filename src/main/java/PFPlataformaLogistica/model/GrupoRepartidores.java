package PFPlataformaLogistica.model;

import java.util.ArrayList;
import java.util.List;

public class GrupoRepartidores implements IRepartidorComponent {

    private String nombreGrupo;
    private List<IRepartidorComponent> componentes = new ArrayList<>();


    public GrupoRepartidores(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public void agregar(IRepartidorComponent componente) {
        componentes.add(componente);
    }

    public void eliminar(IRepartidorComponent componente) {
        componentes.remove(componente);
    }

    @Override
    public void mostrarInfo() {
        System.out.println("Grupo de repartidores: " + nombreGrupo);
        for (IRepartidorComponent c : componentes) {
            c.mostrarInfo();
        }
    }


}
