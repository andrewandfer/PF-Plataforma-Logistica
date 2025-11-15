package PFPlataformaLogistica.model;


// Factory para crear métodos de pago

public class PagoFactory {
    public static Pago crearMetodoPago(String tipo, String dato) {
        switch (tipo.toUpperCase()) {
            case "EFECTIVO":
                return new PagoEfectivoAdapter(); // ¡Usa el Adapter!

            case "PAYPAL":
                return new PagoPaypal(dato); // dato = correo

            case "TARJETA":
                return new PagoTarjeta(dato); // dato = número tarjeta

            default:
                throw new IllegalArgumentException("Método de pago no soportado: " + tipo);
        }
    }
}
