package PFPlataformaLogistica.model;


public interface UsuarioObserver {
    void onUsuarioActualizado(Usuario usuarioActualizado);
    void onErrorActualizacion(String mensajeError);
}
