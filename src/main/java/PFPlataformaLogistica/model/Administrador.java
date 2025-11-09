package PFPlataformaLogistica.model;

import PFPlataformaLogistica.dto.RepartidorDTO;

import java.util.LinkedList;
import java.util.List;

public class Administrador extends Persona {
    private int sueldo;
    private LinkedList<Usuario> listaUsuarios;
    private LinkedList<Repartidor> listaRepartidores;
    // administrador también gestiona repartidores (actualizar) me hace falta ese metodo.

    // Constructor protegido: recibe el Builder
    protected Administrador(AdministradorBuilder builder) {
        super(builder);
        this.sueldo = builder.sueldo;
        this.listaUsuarios = new LinkedList<>();
        this.listaRepartidores = new LinkedList<>();
    }

    public int getSueldo() {
        return sueldo;
    }

    public void setSueldo(int sueldo) {
        this.sueldo = sueldo;
    }

    public LinkedList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    //Metodos logicos para el administrador estan añadidos aqui


    public void crearUsuario(Usuario usuario) {
        listaUsuarios.add(usuario);
        System.out.println("Usuario agregado correctamente: " + usuario.getCorreo());
    }

    public void actualizarUsuario(String correo, Usuario nuevoUsuario) {
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getCorreo().equalsIgnoreCase(correo)) {
                int index = listaUsuarios.indexOf(usuario);
                listaUsuarios.set(index, nuevoUsuario);
                System.out.println("Usuario actualizado correctamente: " + correo);
                return;
            }
        }
        System.out.println(" Usuario no encontrado: " + correo);
    }

    public void eliminarUsuario(String correo) {
        Usuario usuarioAEliminar = null;
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getCorreo().equalsIgnoreCase(correo)) {
                usuarioAEliminar = usuario;
                break;
            }
        }
        if (usuarioAEliminar != null) {
            listaUsuarios.remove(usuarioAEliminar);
            System.out.println("Usuario eliminado: " + correo);
        } else {
            System.out.println("Usuario no encontrado: " + correo);
        }
    }

    public void listarUsuarios() {
        if (listaUsuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {
            System.out.println("Lista de usuarios:");
            for (Usuario u : listaUsuarios) {
                System.out.println("- " + u);
            }
        }
    }

    public void crearRepartidor(Repartidor repartidor) {
        listaRepartidores.add(repartidor);
        System.out.println("Repartidor agregado (entidad): " + repartidor.getId());
    }

    public List<Repartidor> getRepartidores() {
        return listaRepartidores;
    }

    public void mostrarRepartidores() {
        System.out.println("\n--- LISTA DE REPARTIDORES ---");
        for (Repartidor repartidores  : listaRepartidores) {
            System.out.println(repartidores);
        }
    }

    public void eliminarRepartidor(String id) {
        if (listaRepartidores == null || listaRepartidores.isEmpty()) {
            System.out.println("No hay repartidores registrados.");
            return;
        }

        Repartidor repartidorAEliminar = null;
        for (Repartidor repartidor : listaRepartidores) {
            if (repartidor.getId().equalsIgnoreCase(id)) {
                repartidorAEliminar = repartidor;
                break;
            }
        }

        if (repartidorAEliminar != null) {
            listaRepartidores.remove(repartidorAEliminar);
            System.out.println("Repartidor eliminado correctamente: " + id);
        } else {
            System.out.println("Repartidor no encontrado: " + id);
        }
    }


    // Builder específico para Administrador
    public static class AdministradorBuilder extends Persona.PersonaBuilder<AdministradorBuilder> {
        private int sueldo;
        private LinkedList<Usuario> listaUsuarios;

        public AdministradorBuilder sueldo(int sueldo) {
            this.sueldo = sueldo;
            this.listaUsuarios = new LinkedList<>();
            return self();
        }


        @Override
        protected AdministradorBuilder self() {
            return this;
        }

        @Override
        public Administrador build() {
            return new Administrador(this);
        }


    }
}
