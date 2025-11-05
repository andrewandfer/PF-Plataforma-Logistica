package PFPlataformaLogistica.model;

import PFPlataformaLogistica.dto.RepartidorDTO;
import PFPlataformaLogistica.dto.UsuarioDTO;

import java.util.LinkedList;
import java.util.List;

public final class Empresa {
    private String nombre;
    private static Empresa instancia;
    private LinkedList<Repartidor> listaRepartidores;
    private LinkedList<Administrador> listareAdministradores;
    private LinkedList<Usuario> listaUsuarios;
    private LinkedList<Paquete> listaPaquetes;
    private LinkedList<Pago> listaPagos;
    private LinkedList<Direccion> listaDirecciones;
    private LinkedList<Envio> listaEnvios;


    public Empresa() {
        this.nombre = "EnviosExpress";
        this.listaRepartidores = new LinkedList<>();
        this.listareAdministradores = new LinkedList<>();
        this.listaUsuarios = new LinkedList<>();
        this.listaPaquetes = new LinkedList<>();
        this.listaPagos = new LinkedList<>();
        this.listaDirecciones = new LinkedList<>();
        this.listaEnvios = new LinkedList<>();
    }

    public static Empresa getInstancia() {
        return instancia;
    }

    public static void setInstancia(Empresa instancia) {
        Empresa.instancia = instancia;
    }

    public LinkedList<Repartidor> getListaRepartidores() {
        return listaRepartidores;
    }

    public void setListaRepartidores(LinkedList<Repartidor> listaRepartidores) {
        this.listaRepartidores = listaRepartidores;
    }

    public LinkedList<Administrador> getListareAdministradores() {
        return listareAdministradores;
    }

    public void setListareAdministradores(LinkedList<Administrador> listareAdministradores) {
        this.listareAdministradores = listareAdministradores;
    }

    public LinkedList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(LinkedList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LinkedList<Paquete> getListaPaquetes() {
        return listaPaquetes;
    }

    public void setListaPaquetes(LinkedList<Paquete> listaPaquetes) {
        this.listaPaquetes = listaPaquetes;
    }

    public LinkedList<Pago> getListaPagos() {
        return listaPagos;
    }

    public void setListaPagos(LinkedList<Pago> listaPagos) {
        this.listaPagos = listaPagos;
    }

    public LinkedList<Direccion> getListaDirecciones() {
        return listaDirecciones;
    }

    public void setListaDirecciones(LinkedList<Direccion> listaDirecciones) {
        this.listaDirecciones = listaDirecciones;
    }

    public static Empresa getInstance() {
        if (instancia == null) {
            instancia = new Empresa();
        }
        return instancia;
    }

    public LinkedList<Envio> getListaEnvios() {
        return listaEnvios;
    }

    public void setListaEnvios(LinkedList<Envio> listaEnvios) {
        this.listaEnvios = listaEnvios;
    }

    // Metodos Repartidor
    public void crearRepartidor(RepartidorDTO dto) {
        Repartidor nuevo = new Repartidor.RepartidorBuilder()
                .telefono(dto.getTelefono())
                .disponibilidad(dto.isDisponibilidad())
                .zonaCobertura(dto.getZonaCobertura())
                .localidad(dto.getLocalidad())
                .enviosAsignados(dto.getEnviosAsignados())
                .build();

        if (listaRepartidores == null) {
            listaRepartidores = new LinkedList<>();
        }

        listaRepartidores.add(nuevo);
    }

    public void eliminarRepartidor(String id) {
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

    public void actualizarRepartidor(RepartidorDTO dto) {
        if (listaRepartidores == null || listaRepartidores.isEmpty()) {
            System.out.println("No hay repartidores registrados.");
            return;
        }
        for (Repartidor repartidor : listaRepartidores) {
            if (repartidor.getId().equalsIgnoreCase(dto.getId())) {
                repartidor.setDisponibilidad(dto.isDisponibilidad());
                repartidor.setZonaCobertura(dto.getZonaCobertura());
                repartidor.setLocalidad(dto.getLocalidad());
                repartidor.setEnviosAsignados(dto.getEnviosAsignados());

                System.out.println("Repartidor actualizado correctamente: " + dto.getTelefono());
                return;
            }
        }
        System.out.println("Repartidor no encontrado con teléfono: " + dto.getTelefono());
    }

    public List<Repartidor> obtenerRepartidores() {
        if (listaRepartidores == null) {
            listaRepartidores = new LinkedList<>();
        }
        return new LinkedList<>(listaRepartidores);
    }


    public Repartidor actualizarEstadoRepartidor(String Id, boolean nuevaDisponibilidad) {
        if (listaRepartidores == null || listaRepartidores.isEmpty()) {
            System.out.println("No hay repartidores registrados.");
            return null;
        }

        for (Repartidor repartidor : listaRepartidores) {
            if (repartidor.getId().equalsIgnoreCase(Id)) {
                repartidor.setDisponibilidad(nuevaDisponibilidad);
                System.out.println("Disponibilidad actualizada correctamente para: " + Id);
                return repartidor;
            }
        }

        System.out.println("No se encontró un repartidor con el teléfono: " + Id);
        return null;
    }


    //Metodos Administrador

    public void asignarEnvioARepartidor(String idRepartidor, Envio envio) {
        if (listaRepartidores == null || listaRepartidores.isEmpty()) {
            System.out.println("No hay repartidores registrados.");
            return;
        }

        for (Repartidor repartidor : listaRepartidores) {
            if (repartidor.getId().equalsIgnoreCase(idRepartidor)) {

                // Asegurarse de que la lista no sea nula
                if (repartidor.getEnviosAsignados() == null) {
                    repartidor.setEnviosAsignados(new LinkedList<>());
                }

                // Evitar asignar el mismo envío dos veces
                if (repartidor.getEnviosAsignados().contains(envio)) {
                    System.out.println("Este envío ya está asignado a este repartidor.");
                    return;
                }

                repartidor.getEnviosAsignados().add(envio);
                System.out.println("Envío asignado correctamente al repartidor: " + idRepartidor);
                return;
            }
        }

        System.out.println("Repartidor no encontrado con teléfono: " + idRepartidor);
    }


    //Metodos usuario
    public void crearUsuario(UsuarioDTO dto) {
        Usuario nuevo = new Usuario.UsuarioBuilder()
                .telefono(dto.getTelefono())
                .correo(dto.getCorreo())
                .direccion(dto.getDireccion())
                .listaDirecciones(dto.getListaDirecciones())
                .listaProductos(dto.getListaProductos())
                .enviosPropios(dto.getEnviosPropios())
                .nombre(dto.getNombre())
                .edad(dto.getEdad())
                .build();

        if (listaUsuarios == null) {
            listaUsuarios = new LinkedList<>();
        }

        listaUsuarios.add(nuevo);
    }

    public void eliminarUsuario(String id) {
        Usuario usuarioAEliminar = null;
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getId().equalsIgnoreCase(id)) {
                usuarioAEliminar = usuario;
                break;
            }
        }
        if (usuarioAEliminar != null) {
            listaUsuarios.remove(usuarioAEliminar);
            System.out.println("Usuario eliminado correctamente: " + id);
        } else {
            System.out.println("Usuario no encontrado: " + id);
        }
    }

    //Metodos usuario
    public void actualizarUsuario(UsuarioDTO dto) {
        if (listaUsuarios == null || listaUsuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getCorreo().equalsIgnoreCase(dto.getCorreo())) {
                usuario.setTelefono(dto.getTelefono());
                usuario.setDireccion(dto.getDireccion());
                usuario.setListaDirecciones(dto.getListaDirecciones());
                usuario.setListaProductos(dto.getListaProductos());
                usuario.setEnviosPropios(dto.getEnviosPropios());
                usuario.setNombre(dto.getNombre());
                usuario.setEdad(dto.getEdad());

                System.out.println("Usuario actualizado correctamente: " + dto.getCorreo());
                return;

            }
            System.out.println("No se encontró ningún usuario con el correo: " + dto.getCorreo());
        }
    }

    public List<Usuario> obtenerUsuarios() {
        if (listaUsuarios == null) {
            listaUsuarios = new LinkedList<>();
        }
        return new LinkedList<>(listaUsuarios);
    }


    //Requerimientos sobre Entidades: Direccion

    public void crearDireccion(Direccion dto) {
        Direccion nueva = new Direccion.DireccionBuilder()
                .idDireccion(dto.getIdDireccion())
                .alias(dto.getAlias())
                .calle(dto.getCalle())
                .ciudad(dto.getCiudad())
                .build();

        if (listaDirecciones == null) {
            listaDirecciones = new LinkedList<>();
        }

        listaDirecciones.add(nueva);

    }


    public void actualizarDireccion(Direccion dto) {
        if (listaDirecciones == null || listaDirecciones.isEmpty()) {
            System.out.println("No hay direcciones registradas.");
            return;
        }
        for (Direccion direccion : listaDirecciones) {
            if (direccion.getIdDireccion().equalsIgnoreCase(dto.getIdDireccion())) {
                direccion.setAlias(dto.getAlias());
                direccion.setCalle(dto.getCalle());
                direccion.setCiudad(dto.getCiudad());

                System.out.println("Direccion actualizada correctamente: " + dto.getIdDireccion());
                return;

            }
            System.out.println("No se encontró ninguna direccion con el id: " + dto.getIdDireccion());
        }
    }

    public void eliminarDireccion(String idDireccion) {
        Direccion direccionAEliminar = null;

        for (Direccion direccion : listaDirecciones) {
            if (direccion.getIdDireccion().equalsIgnoreCase(idDireccion)) {
                direccionAEliminar = direccion;
                break;
            }
        }
        if (direccionAEliminar != null) {
            listaDirecciones.remove(direccionAEliminar);
            System.out.println("Dirección eliminada correctamente: " + idDireccion);
        } else {
            System.out.println("Dirección no encontrada: " + idDireccion);
        }
    }

    public Direccion obtenerDireccionMasFrecuentePorUsuario(Usuario usuario) {
        if (listaEnvios == null || listaEnvios.isEmpty()) {
            return null;
        }

        List<Direccion> direccionesUsuario = new LinkedList<>();

        //  1. Recorrer los envíos
        for (Envio envio : listaEnvios) {
            if (envio.getListaPaquetes() == null) continue;

            // 🔹 2. Recorrer los paquetes dentro del envío
            for (Paquete paquete : envio.getListaPaquetes()) {
                if (paquete.getUsuario() != null && paquete.getUsuario().equals(usuario)) {
                    Direccion direccion = paquete.getDireccion();
                    if (direccion != null) {
                        direccionesUsuario.add(direccion);
                    }
                }
            }
        }

        // Si el usuario no tiene paquetes registrados
        if (direccionesUsuario.isEmpty()) {
            return null;
        }

        // 🔹 3. Buscar la dirección más repetida
        Direccion direccionMasFrecuente = null;
        int maxConteo = 0;

        for (int i = 0; i < direccionesUsuario.size(); i++) {
            Direccion actual = direccionesUsuario.get(i);
            int conteo = 0;

            for (int j = 0; j < direccionesUsuario.size(); j++) {
                if (actual.equals(direccionesUsuario.get(j))) {
                    conteo++;
                }
            }

            if (conteo > maxConteo) {
                maxConteo = conteo;
                direccionMasFrecuente = actual;
            }
        }

        return direccionMasFrecuente;

    }

    public void consultarDetalleDireccion(String idDireccion) {
        boolean encontrada = false;
        for (Direccion direccion : listaDirecciones) {
            if (direccion.getIdDireccion().equalsIgnoreCase(idDireccion)) {
                System.out.println("Detalles de la dirección:");
                System.out.println("ID: " + direccion.getIdDireccion());
                System.out.println("Alias: " + direccion.getAlias());
                System.out.println("Calle: " + direccion.getCalle());
                System.out.println("Ciudad: " + direccion.getCiudad());
                encontrada = true;
                break;
            }
        }

    }

                //inicializar datos

    Usuario usuario1= new Usuario.UsuarioBuilder()
            .telefono("314572026")
            .nombre("juan")
            .direccion("puerto espejo,mz 11,casa18")
            .contrasena("fuanfokkusu")
            .id("21534689")
            .correo("juanfokkusu@gmail.com")
            .build();

}













