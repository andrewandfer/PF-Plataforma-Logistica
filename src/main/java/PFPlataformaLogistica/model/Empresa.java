package PFPlataformaLogistica.model;

import PFPlataformaLogistica.dto.RepartidorDTO;
import PFPlataformaLogistica.dto.UsuarioDTO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Empresa {
    private String nombre;
    private static Empresa instancia;
    private LinkedList<Repartidor> listaRepartidores;
    private LinkedList<Administrador> listaAdministradores;
    private LinkedList<Usuario> listaUsuarios;
    private LinkedList<Paquete> listaPaquetes;
    private LinkedList<Pago> listaPagos;
    private LinkedList<Direccion> listaDirecciones;
    private LinkedList<Envio> listaEnvios;
    private LinkedList<Persona> listaPersonas;


    public Empresa() {
        this.nombre = "EnviosExpress";
        this.listaRepartidores = new LinkedList<>();
        this.listaAdministradores = new LinkedList<>();
        this.listaUsuarios = new LinkedList<>();
        this.listaPaquetes = new LinkedList<>();
        this.listaPagos = new LinkedList<>();
        this.listaDirecciones = new LinkedList<>();
        this.listaEnvios = new LinkedList<>();
        this.listaPersonas = new LinkedList<>();
    }



    public LinkedList<Repartidor> getListaRepartidores() {
        return listaRepartidores;
    }

    public void setListaRepartidores(LinkedList<Repartidor> listaRepartidores) {
        this.listaRepartidores = listaRepartidores;
    }

    public LinkedList<Administrador> getListaAdministradores() {
        return listaAdministradores;
    }

    public void setListaAdministradores(LinkedList<Administrador> listaAdministradores) {
        this.listaAdministradores = listaAdministradores;
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

    public LinkedList<Persona> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(LinkedList<Persona> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    //======================= Metodos Repartidor====================================


    // RF-019: Registrar un nuevo repartidor
    public void registrarRepartidor(Repartidor repartidor) {
        if (listaRepartidores == null) listaRepartidores = new LinkedList<>();

        listaRepartidores.add(repartidor);
        System.out.println("Repartidor registrado correctamente: " + repartidor.getNombre());
    }

    public void crearRepartidor(RepartidorDTO dto) {
        Repartidor nuevo = new Repartidor.RepartidorBuilder()
                .telefono(dto.getTelefono())
                .disponibilidad(dto.getDisponibilidad())
                .zonaCobertura(dto.getZonaCobertura())
                .localidad(dto.getLocalidad())
                .enviosAsignados(dto.getEnviosAsignados())
                .build();

        if (listaRepartidores == null) {
            listaRepartidores = new LinkedList<>();
        }

        listaRepartidores.add(nuevo);
    }

    // RF-019: Eliminar repartidor

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
                repartidor.setDisponibilidad(dto.getDisponibilidad());
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


    public Repartidor actualizarEstadoRepartidor(String Id, EstadoRepartidor nuevaDisponibilidad) {
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

    // RF-019: Consultar un repartidor por su documento
    public Repartidor buscarRepartidor(String documento) {
        if (listaRepartidores != null) {
            for (Repartidor r : listaRepartidores) {
                if (r.getId().equals(documento)) {
                    return r;
                }
            }
        }
        return null;
    }

    // RF-019: Listar todos los repartidores registrados
    public void listarRepartidores() {
        System.out.println(" Lista de repartidores registrados:");
        if (listaRepartidores == null || listaRepartidores.isEmpty()) {
            System.out.println("No hay repartidores registrados.");
        } else {
            listaRepartidores.forEach(r -> System.out.println(" - " + r));
        }
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
            if (usuario.getEmail().equalsIgnoreCase(dto.getEmail())) {
                usuario.setTelefono(dto.getTelefono());
                usuario.setDireccion(dto.getDireccion());
                usuario.setNombre(dto.getNombre());
                usuario.setEdad(dto.getEdad());
                usuario.setEmail(dto.getEmail());

                System.out.println("Usuario actualizado correctamente: " + dto.getEmail());
                return;

            }
            System.out.println("No se encontró ningún usuario con el correo: " + dto.getEmail());
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


    // ======================= MÉTODOS DE ENVÍO ====================================

    // RF-022: Crear nuevo envío
    public Envio crearEnvio(Producto producto, String fechaCreacion, String fechaEstimada,
                            List<Direccion> direcciones, int peso, TipoEnvio tipoEnvio, Tarifa tarifa) {
        String idEnvio = "ENV-" + System.currentTimeMillis();

        Envio nuevoEnvio = new Envio(
                producto,
                fechaCreacion,
                fechaEstimada,
                idEnvio,
                peso,
                tipoEnvio,
                EstadoEnvio.SOLICITADO,
                direcciones,
                tarifa,
                null // Sin repartidor asignado inicialmente
        );

        if (listaEnvios == null) {
            listaEnvios = new LinkedList<>();
        }

        listaEnvios.add(nuevoEnvio);
        System.out.println("Envío creado correctamente: " + idEnvio);
        return nuevoEnvio;
    }


    // RF-023: Actualizar estado del envío
    public boolean cambiarEstadoEnvio(String idEnvio, EstadoEnvio nuevoEstado) {
        if (listaEnvios == null || listaEnvios.isEmpty()) {
            System.out.println("No hay envíos registrados.");
            return false;
        }

        for (Envio envio : listaEnvios) {
            if (envio.getIdEnvio().equals(idEnvio)) {
                envio.setEstadoEnvio(nuevoEstado);
                System.out.println("Estado del envío " + idEnvio + " actualizado a: " + nuevoEstado);
                return true;
            }
        }

        System.out.println("Envío no encontrado: " + idEnvio);
        return false;
    }

    // RF-024: Cancelar envío (solo si no está asignado)
    public boolean cancelarEnvio(String idEnvio) {
        if (listaEnvios == null || listaEnvios.isEmpty()) {
            System.out.println("No hay envíos registrados.");
            return false;
        }

        for (Envio envio : listaEnvios) {
            if (envio.getIdEnvio().equals(idEnvio)) {
                if (envio.getEstadoEnvio() == EstadoEnvio.SOLICITADO) {
                    listaEnvios.remove(envio);
                    System.out.println("Envío cancelado correctamente: " + idEnvio);
                    return true;
                } else {
                    System.out.println("El envío ya fue asignado, no se puede cancelar.");
                    return false;
                }
            }
        }

        System.out.println("Envío no encontrado: " + idEnvio);
        return false;
    }

    // RF-025: Filtrar envíos por fecha, estado o zona
    public List<Envio> filtrarEnvios(String fechaInicio, String fechaFin, EstadoEnvio estado, String ciudad) {
        List<Envio> enviosFiltrados = new LinkedList<>();

        if (listaEnvios == null || listaEnvios.isEmpty()) {
            return enviosFiltrados;
        }

        for (Envio envio : listaEnvios) {
            boolean cumpleFecha = true;
            boolean cumpleEstado = true;
            boolean cumpleCiudad = true;

            // Filtro por fecha (simplificado - comparación de strings)
            if (fechaInicio != null && !fechaInicio.isEmpty()) {
                cumpleFecha = envio.getFechaCreacion().compareTo(fechaInicio) >= 0;
            }
            if (fechaFin != null && !fechaFin.isEmpty() && cumpleFecha) {
                cumpleFecha = envio.getFechaCreacion().compareTo(fechaFin) <= 0;
            }

            // Filtro por estado
            if (estado != null) {
                cumpleEstado = envio.getEstadoEnvio() == estado;
            }

            // Filtro por ciudad (verifica si alguna dirección coincide)
            if (ciudad != null && !ciudad.isEmpty()) {
                cumpleCiudad = false;
                for (Object obj : envio.getListaDirecciones()) {
                    if (obj instanceof Direccion) {
                        Direccion dir = (Direccion) obj;
                        if (dir.getCiudad().equalsIgnoreCase(ciudad)) {
                            cumpleCiudad = true;
                            break;
                        }
                    }
                }
            }

            if (cumpleFecha && cumpleEstado && cumpleCiudad) {
                enviosFiltrados.add(envio);
            }
        }

        return enviosFiltrados;
    }


    // RF-026: Consultar detalle de un envío
    public Envio buscarEnvioPorId(String idEnvio) {
        if (listaEnvios == null || listaEnvios.isEmpty()) {
            return null;
        }

        for (Envio envio : listaEnvios) {
            if (envio.getIdEnvio().equals(idEnvio)) {
                return envio;
            }
        }

        return null;
    }

    // Obtener todos los envíos de un usuario específico
    public List<Envio> obtenerEnviosPorUsuario(Usuario usuario) {
        List<Envio> enviosUsuario = new LinkedList<>();

        if (listaEnvios == null || listaEnvios.isEmpty() || usuario == null) {
            return enviosUsuario;
        }

        // Verificar en enviosPropios del usuario
        if (usuario.getEnviosPropios() != null) {
            return new LinkedList<>(usuario.getEnviosPropios());
        }

        return enviosUsuario;
    }

    // Obtener repartidores disponibles (ACTIVO)
    public List<Repartidor> obtenerRepartidoresDisponibles() {
        List<Repartidor> disponibles = new LinkedList<>();

        if (listaRepartidores == null || listaRepartidores.isEmpty()) {
            return disponibles;
        }

        for (Repartidor repartidor : listaRepartidores) {
            if (repartidor.getEstadoDisponibilidad() == EstadoRepartidor.ACTIVO) {
                disponibles.add(repartidor);
            }
        }

        return disponibles;
    }

    // RF-012: Asignar repartidor a envío y cambiar estado
    public boolean asignarRepartidorAEnvio(String idEnvio, String idRepartidor) {
        Envio envio = buscarEnvioPorId(idEnvio);
        Repartidor repartidor = buscarRepartidor(idRepartidor);

        if (envio == null) {
            System.out.println("Envío no encontrado: " + idEnvio);
            return false;
        }

        if (repartidor == null) {
            System.out.println("Repartidor no encontrado: " + idRepartidor);
            return false;
        }

        if (repartidor.getEstadoDisponibilidad() != EstadoRepartidor.ACTIVO) {
            System.out.println("El repartidor no está disponible.");
            return false;
        }
        // Asignar repartidor al envío
        envio.setRepartidor(repartidor);
        envio.setEstadoEnvio(EstadoEnvio.ASIGNADO);

        // Agregar envío a la lista del repartidor
        if (repartidor.getEnviosAsignados() == null) {
            repartidor.setEnviosAsignados(new LinkedList<>());
        }
        repartidor.getEnviosAsignados().add(envio);

        // Cambiar estado del repartidor
        repartidor.setDisponibilidad(EstadoRepartidor.EN_RUTA);

        System.out.println("Envío " + idEnvio + " asignado al repartidor " + repartidor.getNombre());
        return true;
    }

    // RF-031: Calcular tarifa estimada
    public float calcularTarifaEnvio(float distancia, int peso, int volumen, boolean esPrioritario) {
        float tarifaBase = 5000; // Tarifa base
        float costoPorKm = 500;
        float costoPorKg = 300;
        float costoPorVolumen = volumen * 100;
        float recargoPrioridad = esPrioritario ? 2000 : 0;

        float total = tarifaBase + (distancia * costoPorKm) + (peso * costoPorKg) + costoPorVolumen + recargoPrioridad;

        return total;
    }

    // Agregar envío al usuario
    public void agregarEnvioAUsuario(Usuario usuario, Envio envio) {
        if (usuario.getEnviosPropios() == null) {
            usuario.setEnviosPropios(new LinkedList<>());
        }
        usuario.getEnviosPropios().add(envio);
    }


}













