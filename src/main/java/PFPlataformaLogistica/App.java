package PFPlataformaLogistica;

import PFPlataformaLogistica.dto.UsuarioDTO;
import PFPlataformaLogistica.model.Administrador;
import PFPlataformaLogistica.model.Empresa;
import PFPlataformaLogistica.model.Repartidor;
import PFPlataformaLogistica.model.Usuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        inicializarDatos();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("login.fxml"));
        Scene scene = new Scene(loader.load(), 700, 500);
        primaryStage.setTitle("EnviosExpress™\uFE0F");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    private static void inicializarDatos() {
        Empresa empresa = Empresa.getInstance();
        Usuario usuario3= new Usuario.UsuarioBuilder()
                .telefono("314572026")
                .nombre("juan")
                .contrasena("fuanfokkusu")
                .id("21534689")
                .email("juanfokkusu@gmail.com")
                .build();
        Empresa.getInstance().getListaPersonas().add(usuario3);

        Usuario usuario4= new Usuario.UsuarioBuilder()
                .telefono("3148676404")
                .nombre("carlos")
                .contrasena("carlitos")
                .email("calixto@gmail.com")
                .build();
        Empresa.getInstance().getListaPersonas().add(usuario4);

        Administrador administrador = new Administrador.AdministradorBuilder()
                .nombre("carepicha")
                .email("carepicha@gmail.com")
                .contrasena("carepichacaida")
                .build();
        Empresa.getInstance().getListaPersonas().add(administrador);

        Repartidor repartidor = new Repartidor.RepartidorBuilder()
                .nombre("cyrene")
                .email("cyrene@gmail.com")
                .contrasena("cyreneida")
                .build();
        Empresa.getInstance().getListaPersonas().add(repartidor);

        Usuario usuario= new Usuario.UsuarioBuilder()
                .email("cyrene")
                .contrasena("cyrene")
                .build();
        Empresa.getInstance().getListaPersonas().add(usuario);
    }



}