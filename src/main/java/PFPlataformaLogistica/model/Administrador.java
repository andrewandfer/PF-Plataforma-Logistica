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

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Obtiene el sueldo del administrador.
     * @return el sueldo del administrador.
     */
/* <<<<<<<<<<  7eef90a7-9c76-4b39-ace2-51e682df0dff  >>>>>>>>>>> */
    public int getSueldo() {
        return sueldo;
    }

    public void setSueldo(int sueldo) {
        this.sueldo = sueldo;
    }

    /**
     * Obtiene la lista de usuarios registrados en la empresa.
     *
     * @return la lista de usuarios registrados en la empresa
     */
    public LinkedList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    //Metodos logicos para el administrador estan añadidos aqui


    /**
     * Agrega un usuario a la lista de usuarios registrados en la empresa.
     * @param usuario el usuario a agregar
     */
    public void crearUsuario(Usuario usuario) {
        listaUsuarios.add(usuario);
        System.out.println("Usuario agregado correctamente: " + usuario.getCorreo());
    }

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Actualiza un usuario en la lista de usuarios registrados en la empresa.
     * @param correo El correo del usuario a actualizar.
     * @param nuevoUsuario El nuevo usuario a agregar en la lista.
     */
/* <<<<<<<<<<  7961ec38-ad89-4861-ac5c-5d7f2eb5b329  >>>>>>>>>>> */
    public void actualizarUsuario(String correo, Usuario nuevoUsuario) {
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getid().equalsIgnoreCase(correo)) {
                int index = listaUsuarios.indexOf(usuario);
                listaUsuarios.set(index, nuevoUsuario);
                System.out.println("Usuario actualizado correctamente: " + correo);
                return;
            }
        }
        System.out.println(" Usuario no encontrado: " + correo);
    }

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Elimina un usuario de la lista de usuarios registrados en la empresa.
     * Si el usuario no existe, se muestra un mensaje indicando esto.
     * @param correo correo del usuario a eliminar
     */
/* <<<<<<<<<<  6729f8ad-560f-470a-a312-a05f72ec263a  >>>>>>>>>>> */
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

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Muestra la lista de usuarios registrados en la empresa.
     * Si no hay usuarios registrados, se muestra un mensaje indicando esto.
     */
/* <<<<<<<<<<  4d9f071c-bffb-4897-b294-9d1595cdc49b  >>>>>>>>>>> */
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

    /**
     * Agrega un repartidor a la lista de repartidores de la empresa.
     * @param repartidor el repartidor a agregar
     */
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
