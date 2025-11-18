package PFPlataformaLogistica.model;
public class LoggerObserver implements UsuarioObserver {

    @Override
    public void onUsuarioActualizado(Usuario usuarioActualizado) {
        System.out.println("📝 LOG: Perfil actualizado - Usuario: " +
                usuarioActualizado.getNombre() + ", Email: " +
                usuarioActualizado.getEmail());
    }

    @Override
    public void onErrorActualizacion(String mensajeError) {
        System.err.println("LOG ERROR: " + mensajeError);
    }
}