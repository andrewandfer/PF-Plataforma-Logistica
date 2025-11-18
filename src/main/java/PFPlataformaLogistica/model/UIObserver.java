package PFPlataformaLogistica.model;

public class UIObserver implements UsuarioObserver {

    @Override
    public void onUsuarioActualizado(Usuario usuarioActualizado) {
        // Aquí podrías actualizar elementos de la UI en tiempo real
        System.out.println(" UI: Perfil actualizado - " + usuarioActualizado.getNombre());
        // Ej: Actualizar barra de navegación, header, etc.
    }

    @Override
    public void onErrorActualizacion(String mensajeError) {
        System.err.println("UI ERROR: " + mensajeError);
        // Ej: Mostrar notificación de error en UI
    }
}
