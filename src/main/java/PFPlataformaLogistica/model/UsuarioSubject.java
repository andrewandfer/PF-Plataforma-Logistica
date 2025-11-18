package PFPlataformaLogistica.model;
import java.util.ArrayList;
import java.util.List;

public class UsuarioSubject {
    private static UsuarioSubject instance;
    private List<UsuarioObserver> observers;

    private UsuarioSubject() {
        this.observers = new ArrayList<>();
    }

    public static UsuarioSubject getInstance() {
        if (instance == null) {
            instance = new UsuarioSubject();
        }
        return instance;
    }

    public void addObserver(UsuarioObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(UsuarioObserver observer) {
        observers.remove(observer);
    }

    public void notificarActualizacionExitosa(Usuario usuario) {
        for (UsuarioObserver observer : observers) {
            observer.onUsuarioActualizado(usuario);
        }
    }

    public void notificarError(String mensajeError) {
        for (UsuarioObserver observer : observers) {
            observer.onErrorActualizacion(mensajeError);
        }
    }
}
