package PFPlataformaLogistica.model;

import java.util.LinkedList;

public class Administrador extends Persona {
    private int sueldo;
    private LinkedList<Usuario> listaUsuarios;

    // Constructor protegido: recibe el Builder
    protected Administrador(AdministradorBuilder builder) {
        super(builder);
        this.sueldo = builder.sueldo;
        this.listaUsuarios = new LinkedList<>();
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
    }
}
