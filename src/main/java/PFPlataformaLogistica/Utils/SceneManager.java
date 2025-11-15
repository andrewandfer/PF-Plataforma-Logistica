package PFPlataformaLogistica.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {

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
}

