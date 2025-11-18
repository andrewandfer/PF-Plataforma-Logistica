package PFPlataformaLogistica;

import PFPlataformaLogistica.model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        inicializarDatos();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("login.fxml"));
        Scene scene = new Scene(loader.load(), 700, 500);
        primaryStage.setTitle("EnviosExpress");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static void inicializarDatos() {
        Empresa empresa = Empresa.getInstance();

        Direccion direccion1 = new Direccion.DireccionBuilder()
                .idDireccion("15632489")
                .alias("julianita vargas")
                .calle("centenario")
                .ciudad("Armenia")
                .latitud(20.00)
                .longitud(10.00)
                .build();


        // Crear usuario3
        Usuario usuario3 = new Usuario.UsuarioBuilder()
                .telefono("314572026")
                .nombre("juan")
                .contrasena("1")
                .id("21534689")
                .email("1")
                .edad(48)
                .build();
        usuario3.getListaDirecciones().add(direccion1);


        // Inicializar listas manualmente si es necesario
        if (usuario3.getListaProductos() == null) {
            usuario3.setListaProductos(new LinkedList<>());
        }
        if (usuario3.getEnviosPropios() == null) {
            usuario3.setEnviosPropios(new LinkedList<>());
        }

        empresa.anadirUsuarioListas(usuario3);


        // Crear usuario4
        Usuario usuario4 = new Usuario.UsuarioBuilder()
                .telefono("3148676404")
                .nombre("carlos")
                .contrasena("carlitos")
                .email("calixto@gmail.com")
                .build();

        if (usuario4.getListaProductos() == null) {
            usuario4.setListaProductos(new LinkedList<>());
        }
        if (usuario4.getEnviosPropios() == null) {
            usuario4.setEnviosPropios(new LinkedList<>());
        }

        empresa.anadirUsuarioListas(usuario4);

        // Crear administrador
        Administrador administrador = new Administrador.AdministradorBuilder()
                .nombre("admin")
                .email("2")
                .contrasena("2")
                .build();
        empresa.getListaPersonas().add(administrador);

        // Crear repartidor
        Repartidor repartidor = new Repartidor.RepartidorBuilder()
                .nombre("cyrene")
                .email("3")
                .contrasena("3")
                .telefono("3145077280")
                .zonaCobertura("centro")
                .localidad("armenia")
                .id("23")
                .build();
        empresa.getListaPersonas().add(repartidor);

        // Crear usuario adicional
        Usuario usuario = new Usuario.UsuarioBuilder()
                .email("cyrene")
                .contrasena("cyrene")
                .build();

        if (usuario.getListaProductos() == null) {
            usuario.setListaProductos(new LinkedList<>());
        }
        if (usuario.getEnviosPropios() == null) {
            usuario.setEnviosPropios(new LinkedList<>());
        }

        empresa.anadirUsuarioListas(usuario);

        // Crear y agregar producto
        Producto producto1 = new Producto("1111", "pelota de juguete", 45);
        usuario.getListaProductos().add(producto1);

        // Crear tarifa
        Tarifa tarifa1 = new Tarifa(15000, 5, 15, 5, 15);

        // Crear lista de productos para el envío
        ArrayList<Producto> productosEnvio = new ArrayList<>();
        productosEnvio.add(producto1);

        // Crear envío usando el método crearEnvio de Empresa
        Envio enviojuan = empresa.crearEnvio(
                productosEnvio,
                "2024-12-24",
                "2024-12-25",
                "125634",
                5,
                TipoEnvio.PRIORITARIA,
                EstadoEnvio.ENRUTA,
                tarifa1,
                repartidor,
                "nuevo berlin",
                "jamaica",
                12000,
                direccion1
        );
        usuario3.getEnviosPropios().add(enviojuan);
        empresa.getListaRepartidores().add(repartidor);
    }
}