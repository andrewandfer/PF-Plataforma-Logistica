package PFPlataformaLogistica.Utils;

import PFPlataformaLogistica.model.Persona;

public class SesionManager {

    private static Object usuarioActual;   // 🔥 ahora puede ser lo que sea

    public static void iniciarSesion(Object usuario) {
        usuarioActual = usuario;
    }

    public static <T> T getUsuarioActual(Class<T> tipo) {
        return tipo.cast(usuarioActual);
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }

    public static boolean haySesionActiva() {
        return usuarioActual != null;
    }
}



