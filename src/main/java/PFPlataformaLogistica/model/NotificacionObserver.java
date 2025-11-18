package PFPlataformaLogistica.model;


public class NotificacionObserver implements UsuarioObserver {

    @Override
    public void onUsuarioActualizado(Usuario usuarioActualizado) {
        System.out.println("📧 NOTIFICACIÓN: Se envió confirmación a " +
                usuarioActualizado.getEmail());
        // Aquí iría la lógica para enviar email/notificación push
    }

    @Override
    public void onErrorActualizacion(String mensajeError) {
        System.err.println("❌ NOTIFICACIÓN ERROR: " + mensajeError);
    }
}
