package PFPlataformaLogistica.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private static final Map<String, Object> sharedData = new HashMap<>();

    public static void cambiarEscena(Stage stage, String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/PFPlataformaLogistica/" + rutaFXML));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al cambiar de escena: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ NUEVO: Método para guardar datos entre escenas
    public static void setData(String key, Object value) {
        sharedData.put(key, value);
    }

    // ✅ NUEVO: Método para obtener datos entre escenas
    public static Object getData(String key) {
        return sharedData.get(key);
    }

    // ✅ NUEVO: Método para remover datos específicos
    public static void removeData(String key) {
        sharedData.remove(key);
    }

    // ✅ NUEVO: Método para limpiar todos los datos
    public static void clearAllData() {
        sharedData.clear();
    }
}

