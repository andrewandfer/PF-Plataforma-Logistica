package PFPlataformaLogistica.Utils;

import PFPlataformaLogistica.model.Persona;

public class SesionManager {
    private static Persona personaActual;

    public static void iniciarSesion(Persona persona) {
        personaActual = persona;
    }

    public static Persona getPersonaActual() {
        return personaActual;
    }

    public static void cerrarSesion() {
        personaActual = null;
    }

    public static boolean haySesionActiva() {
        return personaActual != null;
    }
}


