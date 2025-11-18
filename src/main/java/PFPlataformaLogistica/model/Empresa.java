package PFPlataformaLogistica.model;

import PFPlataformaLogistica.dto.RepartidorDTO;
import PFPlataformaLogistica.dto.UsuarioDTO;

import java.util.*;
import java.util.stream.Collectors;

public final class Empresa {
    private String nombre;
    private static Empresa instancia;
    private LinkedList<Repartidor> listaRepartidores;
    private LinkedList<Administrador> listaAdministradores;
    private LinkedList<Usuario> listaUsuarios;
    private LinkedList<Paquete> listaPaquetes;
    private LinkedList<PagoRecord> listaPagos;
    private LinkedList<Direccion> listaDirecciones;
    private LinkedList<Envio> listaEnvios;
    private LinkedList<Persona> listaPersonas;
    private LinkedList<Incidencia> listaIncidencias;


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
        this.listaIncidencias = new LinkedList<>();
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

    public LinkedList<PagoRecord> getListaPagos() {
        return listaPagos;
    }

    public void setListaPagos(LinkedList<PagoRecord> listaPagos) {
        this.listaPagos = listaPagos;
    }

    public LinkedList<Direccion> getListaDirecciones() {
        return listaDirecciones;
    }

    public void setListaDirecciones(LinkedList<Direccion> listaDirecciones) {
        this.listaDirecciones = listaDirecciones;
    }

    public LinkedList<Incidencia> getListaIncidencias() {
        return listaIncidencias;
    }

    public void setListaIncidencias(LinkedList<Incidencia> listaIncidencias) {
        this.listaIncidencias = listaIncidencias;
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
        if (listaRepartidores != null) {
            for (Repartidor repartidor : listaRepartidores) {
                if (repartidor.getId().equalsIgnoreCase(id)) {
                    repartidorAEliminar = repartidor;
                    break;
                }
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
        if (listaRepartidores == null || listaRepartidores.isEmpty()) {
            return null;
        }

        if (documento == null || documento.trim().isEmpty()) {
            return null;
        }

        for (Repartidor r : listaRepartidores) {
            // VERIFICAR QUE EL ID NO SEA NULL ANTES DE COMPARAR
            if (r.getId() != null && r.getId().equals(documento)) {
                return r;
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


    // ==================== MÉTODOS DE MÉTRICAS RF-013 ====================

    // 1. Tiempos promedio de entrega
    public Map<String, Object> calcularTiemposEntrega(String fechaInicio, String fechaFin) {
        Map<String, Object> metricas = new HashMap<>();

        // Filtrar envíos entregados en el rango de fechas
        List<Envio> enviosEntregados = listaEnvios.stream()
                .filter(e -> e.getEstadoEnvio() == EstadoEnvio.ENTREGADO)
                .filter(e -> estaEnRangoFechas(e, fechaInicio, fechaFin))
                .collect(Collectors.toList());

        if (enviosEntregados.isEmpty()) {
            metricas.put("promedioGeneral", 0.0);
            metricas.put("totalEnvios", 0);
            return metricas;
        }

        // Calcular promedio (aquí simulo el cálculo - adapta según tus fechas reales)
        double promedio = enviosEntregados.stream()
                .mapToInt(e -> 3) // Ejemplo: 3 días promedio
                .average()
                .orElse(0.0);

        metricas.put("promedioGeneral", promedio);
        metricas.put("totalEnvios", enviosEntregados.size());

        return metricas;
    }

    // 2. Servicios adicionales más usados (tipos de envío)
    public Map<String, Object> obtenerServiciosPopulares() {
        Map<String, Object> metricas = new HashMap<>();

        // Contar por tipo de envío
        Map<TipoEnvio, Long> conteo = listaEnvios.stream()
                .collect(Collectors.groupingBy(
                        Envio::getTipoEnvio,
                        Collectors.counting()
                ));

        // Ordenar por popularidad
        List<Map.Entry<TipoEnvio, Long>> topServicios = conteo.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(5)
                .collect(Collectors.toList());

        metricas.put("topServicios", topServicios);
        metricas.put("totalTipos", conteo.size());

        return metricas;
    }

    // 3. Ingresos por periodo
    public Map<String, Object> calcularIngresosPeriodo(String periodo) {
        Map<String, Object> metricas = new HashMap<>();

        double ingresosTotales = listaPagos.stream()
                .filter(p -> "APROBADO".equalsIgnoreCase(p.getResultado()))
                .mapToDouble(PagoRecord::getMonto)
                .sum();

        // Ingresos por método de pago
        Map<String, Double> porMetodo = listaPagos.stream()
                .filter(p -> "APROBADO".equalsIgnoreCase(p.getResultado()))
                .collect(Collectors.groupingBy(
                        PagoRecord::getMetodoPago,
                        Collectors.summingDouble(PagoRecord::getMonto)
                ));

        metricas.put("ingresosTotales", ingresosTotales);
        metricas.put("ingresosPorMetodo", porMetodo);
        metricas.put("totalPagos", listaPagos.size());
        metricas.put("periodo", periodo);

        return metricas;
    }

    // 4. Incidencias por zona
    public Map<String, Object> obtenerIncidenciasPorZona() {
        Map<String, Object> metricas = new HashMap<>();

        // Agrupar incidencias por destino del envío
        Map<String, Long> porZona = listaIncidencias.stream()
                .collect(Collectors.groupingBy(
                        incidencia -> {
                            Envio envio = incidencia.getEnvio();
                            return envio != null ? envio.getDestino() : "Desconocido";
                        },
                        Collectors.counting()
                ));

        // Tipos de incidencia más comunes
        Map<String, Long> porTipo = listaIncidencias.stream()
                .collect(Collectors.groupingBy(
                        Incidencia::getTipo,
                        Collectors.counting()
                ));

        metricas.put("incidenciasPorZona", porZona);
        metricas.put("incidenciasPorTipo", porTipo);
        metricas.put("totalIncidencias", listaIncidencias.size());

        return metricas;
    }

    // 5. Métricas generales del sistema
    public Map<String, Object> obtenerMetricasGenerales() {
        Map<String, Object> metricas = new HashMap<>();

        metricas.put("totalEnvios", listaEnvios.size());
        metricas.put("totalUsuarios", listaUsuarios.size());
        metricas.put("totalRepartidores", listaRepartidores.size());
        metricas.put("totalIncidencias", listaIncidencias.size());
        metricas.put("totalPagos", listaPagos.size());

        // Envíos por estado
        Map<EstadoEnvio, Long> enviosPorEstado = listaEnvios.stream()
                .collect(Collectors.groupingBy(
                        Envio::getEstadoEnvio,
                        Collectors.counting()
                ));

        metricas.put("enviosPorEstado", enviosPorEstado);

        return metricas;
    }

    // Método auxiliar para filtrar por fechas
    private boolean estaEnRangoFechas(Envio envio, String fechaInicio, String fechaFin) {
        if ((fechaInicio == null || fechaInicio.isEmpty()) &&
                (fechaFin == null || fechaFin.isEmpty())) {
            return true;
        }

        // Implementa la lógica real de comparación de fechas según tu formato
        return true; // Simplificado por ahora
    }



    public boolean reasignarRepartidorAEnvio(String idEnvio, String nuevoIdRepartidor) {
        Envio envio = buscarEnvioPorId(idEnvio);
        Repartidor nuevoRepartidor = buscarRepartidor(nuevoIdRepartidor);

        if (envio == null) {
            System.out.println(" Envío no encontrado: " + idEnvio);
            return false;
        }

        if (nuevoRepartidor == null) {
            System.out.println(" Nuevo repartidor no encontrado: " + nuevoIdRepartidor);
            return false;
        }

        if (nuevoRepartidor.getEstadoDisponibilidad() != EstadoRepartidor.ACTIVO) {
            System.out.println(" Nuevo repartidor no disponible: " + nuevoRepartidor.getNombre());
            return false;
        }

        // Remover del repartidor anterior
        Repartidor repartidorAnterior = envio.getRepartidor();
        if (repartidorAnterior != null) {
            repartidorAnterior.getEnviosAsignados().remove(envio);
            System.out.println("Envío removido del repartidor anterior: " + repartidorAnterior.getNombre());
        }

        // Asignar al nuevo repartidor
        envio.setRepartidor(nuevoRepartidor);

        if (nuevoRepartidor.getEnviosAsignados() == null) {
            nuevoRepartidor.setEnviosAsignados(new LinkedList<>());
        }
        nuevoRepartidor.getEnviosAsignados().add(envio);

        System.out.println("Envío " + idEnvio + " REASIGNADO a " + nuevoRepartidor.getNombre());
        return true;
    }


    // RF-012: REGISTRAR INCIDENCIAS
    public void registrarIncidencia(String idEnvio, String tipo, String descripcion) {
        Envio envio = buscarEnvioPorId(idEnvio);

        if (envio == null) {
            System.out.println(" Envío no encontrado: " + idEnvio);
            return;
        }

        String idIncidencia = "INC-" + System.currentTimeMillis();
        Incidencia incidencia = new Incidencia(idIncidencia, envio, tipo, descripcion);

        if (listaIncidencias == null) {
            listaIncidencias = new LinkedList<>();
        }

        listaIncidencias.add(incidencia);
        envio.setEstadoEnvio(EstadoEnvio.INCIDENCIA);

        System.out.println("⚠️ INCIDENCIA REGISTRADA:");
        System.out.println("   ID: " + idIncidencia);
        System.out.println("   Envío: " + envio.getIdEnvio());
        System.out.println("   Tipo: " + tipo);
        System.out.println("   Descripción: " + descripcion);
    }


    public List<Incidencia> obtenerIncidenciasPorEnvio(String idEnvio) {
        List<Incidencia> incidenciasEnvio = new LinkedList<>();

        if (listaIncidencias == null || listaIncidencias.isEmpty()) {
            return incidenciasEnvio;
        }

        for (Incidencia incidencia : listaIncidencias) {
            if (incidencia.getEnvio().getIdEnvio().equals(idEnvio)) {
                incidenciasEnvio.add(incidencia);
            }
        }

        return incidenciasEnvio;
    }

    public List<Envio> obtenerEnviosPorEstado(EstadoEnvio estado) {
        List<Envio> enviosFiltrados = new LinkedList<>();

        if (listaEnvios == null || listaEnvios.isEmpty()) {
            return enviosFiltrados;
        }

        for (Envio envio : listaEnvios) {
            if (envio.getEstadoEnvio() == estado) {
                enviosFiltrados.add(envio);
            }
        }

        return enviosFiltrados;
    }



    public List<Envio> obtenerEnviosPorRepartidor(String idRepartidor) {
        List<Envio> enviosRepartidor = new LinkedList<>();

        if (listaEnvios == null || listaEnvios.isEmpty()) {
            return enviosRepartidor;
        }

        for (Envio envio : listaEnvios) {
            if (envio.getRepartidor() != null &&
                    envio.getRepartidor().getId().equals(idRepartidor)) {
                enviosRepartidor.add(envio);
            }
        }

        return enviosRepartidor;
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
        if (listaUsuarios != null) {
            for (Usuario usuario : listaUsuarios) {
                if (usuario.getId().equalsIgnoreCase(id)) {
                    usuarioAEliminar = usuario;
                    break;
                }
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

    public boolean actualizarUsuario(UsuarioDTO dto) {
        if (dto == null || dto.getEmail() == null) {
            return false;
        }

        if (listaUsuarios == null) {
            return false;
        }

        for (Usuario usuario : listaUsuarios) {
            if (usuario.getEmail().equalsIgnoreCase(dto.getEmail())) {
                usuario.setTelefono(dto.getTelefono());
                usuario.setDireccion(dto.getDireccion());
                usuario.setNombre(dto.getNombre());
                usuario.setEdad(dto.getEdad());
                usuario.setEmail(dto.getEmail());
                return true; // Usuario encontrado y actualizado
            }
        }
        return false; // Usuario no encontrado
    }


    //Requerimientos sobre Entidades: Direccion

    // En la clase Empresa - Métodos corregidos para Dirección
    public boolean crearDireccion(Direccion direccion) {
        try {
            if (direccion == null) {
                System.out.println("Error: La dirección no puede ser nula");
                return false;
            }

            // Validar que el ID no esté vacío
            if (direccion.getIdDireccion() == null || direccion.getIdDireccion().trim().isEmpty()) {
                System.out.println("Error: El ID de dirección no puede estar vacío");
                return false;
            }

            // Validar que el ID no exista
            if (existeDireccion(direccion.getIdDireccion())) {
                System.out.println("Error: Ya existe una dirección con ID: " + direccion.getIdDireccion());
                return false;
            }

            if (listaDirecciones == null) {
                listaDirecciones = new LinkedList<>();
            }

            listaDirecciones.add(direccion);
            System.out.println("Dirección creada correctamente: " + direccion.getIdDireccion());
            System.out.println("   Detalles: " + direccion.toFormattedString());
            return true;

        } catch (Exception e) {
            System.err.println("Error al crear dirección: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeDireccion(String idDireccion) {
        if (listaDirecciones == null || listaDirecciones.isEmpty()) {
            return false;
        }

        for (Direccion dir : listaDirecciones) {
            if (dir.getIdDireccion() != null && dir.getIdDireccion().equalsIgnoreCase(idDireccion)) {
                return true;
            }
        }
        return false;
    }

    // En la clase Empresa - Método actualizarDireccion mejorado
    public boolean actualizarDireccion(Direccion direccionActualizada) {
        try {
            if (direccionActualizada == null) {
                System.out.println("Error: La dirección a actualizar no puede ser nula");
                return false;
            }

            if (listaDirecciones == null || listaDirecciones.isEmpty()) {
                System.out.println("No hay direcciones registradas para actualizar.");
                return false;
            }

            String idDireccion = direccionActualizada.getIdDireccion();

            for (Direccion direccion : listaDirecciones) {
                if (direccion.getIdDireccion().equalsIgnoreCase(idDireccion)) {
                    // Actualizar todos los campos
                    direccion.setAlias(direccionActualizada.getAlias());
                    direccion.setCalle(direccionActualizada.getCalle());
                    direccion.setCiudad(direccionActualizada.getCiudad());
                    direccion.setLatitud(direccionActualizada.getLatitud());
                    direccion.setLongitud(direccionActualizada.getLongitud());

                    System.out.println(" Dirección actualizada correctamente: " + idDireccion);
                    System.out.println("   Nuevos datos: " + direccionActualizada.toFormattedString());
                    return true;
                }
            }

            System.out.println(" No se encontró ninguna dirección con el id: " + idDireccion);
            return false;

        } catch (Exception e) {
            System.err.println(" Error al actualizar dirección: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Direccion buscarDireccionPorId(String idDireccion) {
        if (listaDirecciones == null || listaDirecciones.isEmpty()) {
            return null;
        }

        for (Direccion direccion : listaDirecciones) {
            if (direccion.getIdDireccion().equalsIgnoreCase(idDireccion)) {
                return direccion;
            }
        }
        return null;
    }

    public List<Direccion> obtenerTodasDirecciones() {
        if (listaDirecciones == null) {
            listaDirecciones = new LinkedList<>();
        }
        return new ArrayList<>(listaDirecciones); // Usar ArrayList para mejor rendimiento
    }

// En la clase Empresa.java - métodos corregidos para manejar null

    /**
     * Agrega un nuevo usuario a la empresa
     */
    public boolean agregarUsuario(Usuario usuario) {
        try {
            if (usuario == null) {
                System.out.println("Error: Usuario nulo");
                return false;
            }

            // Verificar si el usuario ya existe (por email o teléfono)
            for (Usuario u : listaUsuarios) {
                // Manejar teléfono nulo
                String telefonoExistente = u.getTelefono();
                String telefonoNuevo = usuario.getTelefono();

                boolean telefonoCoincide = (telefonoExistente != null && telefonoNuevo != null)
                        ? telefonoExistente.equals(telefonoNuevo)
                        : false;

                boolean emailCoincide = (u.getEmail() != null && usuario.getEmail() != null)
                        ? u.getEmail().equals(usuario.getEmail())
                        : false;

                if (telefonoCoincide || emailCoincide) {
                    System.out.println("Error: Usuario ya existe con ese email o teléfono");
                    return false;
                }
            }

            // Agregar el usuario a la lista
            listaUsuarios.add(usuario);
            listaPersonas.add(usuario);
            System.out.println("Usuario agregado exitosamente: " + usuario.getEmail());
            return true;

        } catch (Exception e) {
            System.err.println("Error al agregar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza un usuario existente en la empresa
     */
    public boolean actualizarUsuario(Usuario usuarioActualizado) {
        try {
            if (usuarioActualizado == null) {
                System.out.println("Error: Usuario actualizado es nulo");
                return false;
            }

            // Buscar el usuario por email (manejando null)
            String emailBuscado = usuarioActualizado.getEmail();
            if (emailBuscado == null) {
                System.out.println("Error: Email del usuario a actualizar es nulo");
                return false;
            }

            for (int i = 0; i < listaUsuarios.size(); i++) {
                Usuario usuarioExistente = listaUsuarios.get(i);
                String emailExistente = usuarioExistente.getEmail();

                // Comparar por email (manejando null)
                if (emailExistente != null && emailExistente.equals(emailBuscado)) {
                    // Actualizar los datos del usuario
                    listaUsuarios.set(i, usuarioActualizado);
                    System.out.println("Usuario actualizado exitosamente: " + emailBuscado);
                    return true;
                }
            }

            System.out.println("Error: Usuario no encontrado para actualizar");
            return false;

        } catch (Exception e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un usuario de la empresa
     */
    public boolean eliminarUsuario(Usuario usuarioEliminar) {
        try {
            if (usuarioEliminar == null) {
                System.out.println("Error: Usuario a eliminar es nulo");
                return false;
            }

            // Buscar el usuario por email (manejando null)
            String emailBuscado = usuarioEliminar.getEmail();
            if (emailBuscado == null) {
                System.out.println("Error: Email del usuario a eliminar es nulo");
                return false;
            }

            for (int i = 0; i < listaUsuarios.size(); i++) {
                Usuario usuarioExistente = listaUsuarios.get(i);
                String emailExistente = usuarioExistente.getEmail();

                // Comparar por email (manejando null)
                if (emailExistente != null && emailExistente.equals(emailBuscado)) {
                    // Eliminar el usuario
                    listaUsuarios.remove(i);
                    System.out.println("Usuario eliminado exitosamente: " + emailBuscado);
                    return true;
                }
            }

            System.out.println("Error: Usuario no encontrado para eliminar");
            return false;

        } catch (Exception e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Versión alternativa de eliminar por email
     */
    public boolean eliminarUsuarioPorEmail(String email) {
        try {
            if (email == null || email.isEmpty()) {
                System.out.println("Error: Email vacío o nulo");
                return false;
            }

            for (int i = 0; i < listaUsuarios.size(); i++) {
                Usuario usuario = listaUsuarios.get(i);
                if (usuario.getEmail().equals(email)) {
                    listaUsuarios.remove(i);
                    System.out.println("Usuario eliminado exitosamente: " + email);
                    return true;
                }
            }

            System.out.println("Error: Usuario no encontrado con email: " + email);
            return false;

        } catch (Exception e) {
            System.err.println("Error al eliminar usuario por email: " + e.getMessage());
            return false;
        }
    }

    /**
     * Versión alternativa de eliminar por teléfono
     */
    public boolean eliminarUsuarioPorTelefono(String telefono) {
        try {
            if (telefono == null || telefono.isEmpty()) {
                System.out.println("Error: Teléfono vacío o nulo");
                return false;
            }

            for (int i = 0; i < listaUsuarios.size(); i++) {
                Usuario usuario = listaUsuarios.get(i);
                if (usuario.getTelefono().equals(telefono)) {
                    listaUsuarios.remove(i);
                    System.out.println("Usuario eliminado exitosamente: " + telefono);
                    return true;
                }
            }

            System.out.println("Error: Usuario no encontrado con teléfono: " + telefono);
            return false;

        } catch (Exception e) {
            System.err.println("Error al eliminar usuario por teléfono: " + e.getMessage());
            return false;
        }
    }

    /**
     * Buscar usuario por email (útil para verificar existencia)
     */
    public Usuario buscarUsuarioPorEmail(String email) {
        try {
            if (email == null || email.isEmpty()) {
                return null;
            }

            for (Usuario usuario : listaUsuarios) {
                if (usuario.getEmail().equals(email)) {
                    return usuario;
                }
            }

            return null;

        } catch (Exception e) {
            System.err.println("Error al buscar usuario por email: " + e.getMessage());
            return null;
        }
    }

    /**
     * Buscar usuario por teléfono (útil para verificar existencia)
     */
    public Usuario buscarUsuarioPorTelefono(String telefono) {
        try {
            if (telefono == null || telefono.isEmpty()) {
                return null;
            }

            for (Usuario usuario : listaUsuarios) {
                if (usuario.getTelefono().equals(telefono)) {
                    return usuario;
                }
            }

            return null;

        } catch (Exception e) {
            System.err.println("Error al buscar usuario por teléfono: " + e.getMessage());
            return null;
        }
    }

    // ======================= MÉTODOS DE ENVÍO ==================================
    // ==

    // RF-022: Crear nuevo envío
    public Envio crearEnvio(ArrayList<Producto> listaProductos, String fechaCreacion, String fechaEstimada, String idEnvio, int pesoEnvio, TipoEnvio tipoEnvio, EstadoEnvio estadoEnvio, Tarifa tarifa, Repartidor repartidor, String origen, String destino, int costo, Direccion direccion) {

        idEnvio = "ENV-" + System.currentTimeMillis();

        Envio nuevoEnvio = new Envio(
                listaProductos,
                fechaCreacion,
                fechaEstimada,
                idEnvio,
                pesoEnvio,
                tipoEnvio,
                EstadoEnvio.SOLICITADO,
                tarifa,
                null,
                origen
                , destino
                , costo
                , direccion
        );

        if (listaEnvios == null) {
            listaEnvios = new LinkedList<>();
        }

        listaEnvios.add(nuevoEnvio);
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
            System.out.println("DEBUG: Lista de repartidores está vacía");
            return disponibles;
        }

        System.out.println("DEBUG: Total repartidores en sistema: " + listaRepartidores.size());

        for (Repartidor repartidor : listaRepartidores) {
            System.out.println("DEBUG: Repartidor " + repartidor.getNombre() +
                    " - Estado: " + repartidor.getEstadoDisponibilidad());

            if (repartidor.getEstadoDisponibilidad() == EstadoRepartidor.ACTIVO) {
                disponibles.add(repartidor);
                System.out.println("Repartidor ACTIVO agregado: " + repartidor.getNombre());
            }
        }

        System.out.println("DEBUG: Repartidores disponibles encontrados: " + disponibles.size());
        return disponibles;
    }

    // RF-012: Asignar repartidor a envío y cambiar estado
    public boolean asignarRepartidorAEnvio(String idEnvio, String idRepartidor) {
        // Verificar parámetros primero
        if (idEnvio == null || idEnvio.trim().isEmpty()) {
            return false;
        }

        if (idRepartidor == null || idRepartidor.trim().isEmpty()) {
            return false;
        }

        Envio envio = buscarEnvioPorId(idEnvio);
        Repartidor repartidor = buscarRepartidor(idRepartidor);

        if (envio == null) {
            return false;
        }

        if (repartidor == null) {
            return false;
        }

        if (repartidor.getEstadoDisponibilidad() != EstadoRepartidor.ACTIVO) {
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

    // ==================Metodos de pago =========================================

    public PagoRecord registrarPago(Envio envio, Pago metodoPago, double monto, String detalles) {
        // Validaciones
        if (envio == null) {
            System.out.println(" Error: El envío no puede ser nulo");
            return null;
        }

        if (metodoPago == null) {
            System.out.println(" Error: El método de pago no puede ser nulo");
            return null;
        }

        if (monto <= 0) {
            System.out.println(" Error: El monto debe ser mayor a cero");
            return null;
        }

        // Procesar el pago usando el ProcesadorPago
        ProcesadorPago procesador = new ProcesadorPago(metodoPago);
        String resultadoProcesamiento = procesador.ejecutarPago(monto);

        // Determinar si fue aprobado o rechazado
        String resultado = resultadoProcesamiento.contains("exitoso") ||
                resultadoProcesamiento.contains("EXITOSO") ||
                resultadoProcesamiento.contains("aprobado") ||
                resultadoProcesamiento.contains("APROBADO") ?
                "APROBADO" : "RECHAZADO";

        // Crear el registro del pago
        String idPago = "PAGO-" + System.currentTimeMillis();
        String fecha = java.time.LocalDate.now().toString();
        String tipoMetodo = metodoPago.getClass().getSimpleName();

        PagoRecord pagoRecord = new PagoRecord(
                idPago,
                monto,
                fecha,
                tipoMetodo,
                resultado,
                envio.getIdEnvio(),
                detalles
        );

        // Agregar a la lista de pagos
        if (listaPagos == null) {
            listaPagos = new LinkedList<>();
        }

        listaPagos.add(pagoRecord);

        System.out.println("Pago registrado: " + idPago);
        return pagoRecord;
    }

    // Sobrecarga sin detalles
    public PagoRecord registrarPago(Envio envio, Pago metodoPago, double monto) {
        return registrarPago(envio, metodoPago, monto, "");
    }

    // RF-034: Consultar comprobantes de pago
    public PagoRecord consultarComprobantePago(String idPago) {
        if (listaPagos == null || listaPagos.isEmpty()) {
            System.out.println("No hay pagos registrados en el sistema");
            return null;
        }

        for (PagoRecord pago : listaPagos) {
            if (pago.getIdPago().equals(idPago)) {
                System.out.println("Comprobante encontrado:");
                System.out.println(pago.generarComprobante());
                return pago;
            }
        }

        System.out.println(" No se encontró ningún pago con ID: " + idPago);
        return null;
    }

    // Consultar todos los pagos de un envío
    public List<PagoRecord> consultarPagosPorEnvio(String idEnvio) {
        List<PagoRecord> pagosEnvio = new LinkedList<>();

        if (listaPagos == null || listaPagos.isEmpty()) {
            System.out.println("No hay pagos registrados en el sistema");
            return pagosEnvio;
        }

        for (PagoRecord pago : listaPagos) {
            if (pago.getIdEnvio().equals(idEnvio)) {
                pagosEnvio.add(pago);
            }
        }

        if (pagosEnvio.isEmpty()) {
            System.out.println("No se encontraron pagos para el envío: " + idEnvio);
        } else {
            System.out.println("Se encontraron " + pagosEnvio.size() + " pago(s) para el envío " + idEnvio);
        }

        return pagosEnvio;
    }


    // RF-035: Listar pagos por rango de fechas
    public List<PagoRecord> listarPagosPorFechas(String fechaInicio, String fechaFin) {
        List<PagoRecord> pagosFiltrados = new LinkedList<>();

        if (listaPagos == null || listaPagos.isEmpty()) {
            System.out.println(" No hay pagos registrados en el sistema");
            return pagosFiltrados;
        }

        System.out.println("Buscando pagos entre " + fechaInicio + " y " + fechaFin);

        for (PagoRecord pago : listaPagos) {
            String fechaPago = pago.getFecha();

            // Comparación de fechas (formato: yyyy-MM-dd)
            boolean dentroDeFechas = true;

            if (fechaInicio != null && !fechaInicio.isEmpty()) {
                dentroDeFechas = fechaPago.compareTo(fechaInicio) >= 0;
            }

            if (fechaFin != null && !fechaFin.isEmpty() && dentroDeFechas) {
                dentroDeFechas = fechaPago.compareTo(fechaFin) <= 0;
            }

            if (dentroDeFechas) {
                pagosFiltrados.add(pago);
            }
        }

        System.out.println("Se encontraron " + pagosFiltrados.size() + " pago(s) en el rango de fechas");
        return pagosFiltrados;
    }


    // Listar todos los pagos
    public List<PagoRecord> listarTodosPagos() {
        if (listaPagos == null) {
            listaPagos = new LinkedList<>();
        }
        return new LinkedList<>(listaPagos);
    }


    // Obtener pagos por método
    public List<PagoRecord> listarPagosPorMetodo(String metodoPago) {
        List<PagoRecord> pagosPorMetodo = new LinkedList<>();

        if (listaPagos == null || listaPagos.isEmpty()) {
            System.out.println(" No hay pagos registrados en el sistema");
            return pagosPorMetodo;
        }

        for (PagoRecord pago : listaPagos) {
            if (pago.getMetodoPago().equalsIgnoreCase(metodoPago)) {
                pagosPorMetodo.add(pago);
            }
        }

        System.out.println("Se encontraron " + pagosPorMetodo.size() + " pago(s) con método: " + metodoPago);
        return pagosPorMetodo;
    }

    // Obtener pagos por resultado (APROBADO/RECHAZADO)
    public List<PagoRecord> listarPagosPorResultado(String resultado) {
        List<PagoRecord> pagosPorResultado = new LinkedList<>();

        if (listaPagos == null || listaPagos.isEmpty()) {
            System.out.println(" No hay pagos registrados en el sistema");
            return pagosPorResultado;
        }

        for (PagoRecord pago : listaPagos) {
            if (pago.getResultado().equalsIgnoreCase(resultado)) {
                pagosPorResultado.add(pago);
            }
        }

        System.out.println(" Se encontraron " + pagosPorResultado.size() + " pago(s) con resultado: " + resultado);
        return pagosPorResultado;
    }


    public PagoRecord procesarPagoConMetodo(String tipoPago, String datoPago,
                                            Envio envio, double monto, String detalles) {
        try {
            // Usar tu PagoFactory existente para crear el método de pago
            Pago metodoPago = PagoFactory.crearMetodoPago(tipoPago, datoPago);

            ProcesadorPago procesador = new ProcesadorPago(metodoPago);
            String resultadoProcesamiento = procesador.ejecutarPago(monto);

            String resultado = "RECHAZADO";
            if (tipoPago.equalsIgnoreCase("EFECTIVO")) {
                resultado = "APROBADO";
            }
            else {
                String resultadoLower = resultadoProcesamiento.toLowerCase();
                if (resultadoLower.contains("aprobado") ||
                        resultadoLower.contains("exitoso") ||
                        resultadoLower.contains("recibido") ||
                        resultadoLower.contains("exitosa")) {
                    resultado = "APROBADO";
                }
            }


            String idPago = "PAGO-" + System.currentTimeMillis();
            String fecha = java.time.LocalDate.now().toString();

            PagoRecord pagoRecord = new PagoRecord(
                    idPago,
                    monto,
                    fecha,
                    tipoPago.toUpperCase(),
                    resultado,
                    envio.getIdEnvio(),
                    detalles
            );

            // Agregar a la lista de pagos
            if (listaPagos == null) {
                listaPagos = new LinkedList<>();
            }
            listaPagos.add(pagoRecord);

            System.out.println("Pago procesado: " + resultado + " - " + idPago);
            return pagoRecord;

        } catch (Exception e) {
            System.err.println("Error procesando pago: " + e.getMessage());
            return null;
        }
    }


    // Calcular ingresos totales
    public double calcularIngresosTotales() {
        double total = 0;

        if (listaPagos == null || listaPagos.isEmpty()) {
            return total;
        }

        for (PagoRecord pago : listaPagos) {
            if (pago.getResultado().equalsIgnoreCase("APROBADO")) {
                total += pago.getMonto();
            }
        }

        return total;
    }

    // Calcular ingresos por periodo
    public double calcularIngresosPorPeriodo(String fechaInicio, String fechaFin) {
        List<PagoRecord> pagosPeriodo = listarPagosPorFechas(fechaInicio, fechaFin);
        double total = 0;

        for (PagoRecord pago : pagosPeriodo) {
            if (pago.getResultado().equalsIgnoreCase("APROBADO")) {
                total += pago.getMonto();
            }
        }

        return total;
    }

    public void anadirUsuarioListas(Usuario usuarioNuevo) {

        if (usuarioNuevo == null)
            throw new IllegalArgumentException("El usuario no puede ser null");

        if (listaUsuarios == null) listaUsuarios = new LinkedList<>();
        if (listaPersonas == null) listaPersonas = new LinkedList<>();

        if (!listaUsuarios.contains(usuarioNuevo)) {
            listaUsuarios.add(usuarioNuevo);
        }

        if (!listaPersonas.contains(usuarioNuevo)) {
            listaPersonas.add(usuarioNuevo);
        }
    }

}