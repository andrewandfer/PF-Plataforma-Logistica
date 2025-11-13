package PFPlataformaLogistica.model;


import org.apache.pdfbox.pdmodel.PDDocument;

import java.util.List;

public class PagoRecord {
    private String idPago;
    private double monto;
    private String fecha;
    private String metodoPago; // "PayPal", "Tarjeta", "Efectivo"
    private String resultado; // "APROBADO" o "RECHAZADO"
    private String idEnvio; // ID del envío asociado
    private String detalles; // Información adicional (número tarjeta, correo, etc.)

    // Constructor completo
    public PagoRecord(String idPago, double monto, String fecha, String metodoPago,
                      String resultado, String idEnvio, String detalles) {
        this.idPago = idPago;
        this.monto = monto;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.resultado = resultado;
        this.idEnvio = idEnvio;
        this.detalles = detalles;
    }

    // Constructor simplificado (sin detalles)
    public PagoRecord(String idPago, double monto, String fecha, String metodoPago,
                      String resultado, String idEnvio) {
        this(idPago, monto, fecha, metodoPago, resultado, idEnvio, "");
    }

    // Getters y Setters
    public String getIdPago() {
        return idPago;
    }

    public void setIdPago(String idPago) {
        this.idPago = idPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(String idEnvio) {
        this.idEnvio = idEnvio;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "PagoRecord{" +
                "idPago='" + idPago + '\'' +
                ", monto=" + monto +
                ", fecha='" + fecha + '\'' +
                ", metodoPago='" + metodoPago + '\'' +
                ", resultado='" + resultado + '\'' +
                ", idEnvio='" + idEnvio + '\'' +
                ", detalles='" + detalles + '\'' +
                '}';
    }

    // Método para generar comprobante en texto
    public String generarComprobante() {
        StringBuilder comprobante = new StringBuilder();
        comprobante.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        comprobante.append("        COMPROBANTE DE PAGO\n");
        comprobante.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        comprobante.append("ID Pago:       ").append(idPago).append("\n");
        comprobante.append("Fecha:         ").append(fecha).append("\n");
        comprobante.append("Monto:         $").append(String.format("%.2f", monto)).append("\n");
        comprobante.append("Método:        ").append(metodoPago).append("\n");
        comprobante.append("Estado:        ").append(resultado).append("\n");
        comprobante.append("Envío:         ").append(idEnvio).append("\n");
        if (detalles != null && !detalles.isEmpty()) {
            comprobante.append("Detalles:      ").append(detalles).append("\n");
        }
        comprobante.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        return comprobante.toString();
    }



    // Método para generar comprobante en PDF
    public String generarComprobantePDF(String rutaCarpeta) {
        try {
            // Crear documento PDF
            PDDocument document = new PDDocument();
            org.apache.pdfbox.pdmodel.PDPage page = new org.apache.pdfbox.pdmodel.PDPage();
            document.addPage(page);

            // Preparar para escribir
            org.apache.pdfbox.pdmodel.PDPageContentStream contentStream =
                    new org.apache.pdfbox.pdmodel.PDPageContentStream(document, page);

            // Configurar fuente
            contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 20);

            // Título
            contentStream.beginText();
            contentStream.newLineAtOffset(150, 750);
            contentStream.showText("COMPROBANTE DE PAGO");
            contentStream.endText();

            // Línea separadora
            contentStream.moveTo(50, 735);
            contentStream.lineTo(550, 735);
            contentStream.stroke();

            // Información de la empresa
            contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 710);
            contentStream.showText("Envios Express - Plataforma de Logistica");
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(50, 695);
            contentStream.showText("www.enviosexpress.com | soporte@enviosexpress.com");
            contentStream.endText();

            // Detalles del pago
            contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 660);
            contentStream.showText("DETALLES DE LA TRANSACCION");
            contentStream.endText();

            contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 12);

            int yPosition = 635;

            // ID Pago
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText("ID de Pago:");
            contentStream.endText();
            contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(200, yPosition);
            contentStream.showText(idPago);
            contentStream.endText();
            yPosition -= 25;

            // Fecha
            contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText("Fecha:");
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(200, yPosition);
            contentStream.showText(fecha);
            contentStream.endText();
            yPosition -= 25;

            // Monto
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText("Monto:");
            contentStream.endText();
            contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(200, yPosition);
            contentStream.showText("$" + String.format("%.2f", monto));
            contentStream.endText();
            yPosition -= 25;

            // Método de pago
            contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText("Metodo de Pago:");
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(200, yPosition);
            contentStream.showText(metodoPago);
            contentStream.endText();
            yPosition -= 25;

            // Estado
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText("Estado:");
            contentStream.endText();

            // Color según estado
            if (resultado.equalsIgnoreCase("APROBADO")) {
                contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 12);
                contentStream.setNonStrokingColor(0, 128, 0); // Verde
            } else {
                contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 12);
                contentStream.setNonStrokingColor(255, 0, 0); // Rojo
            }
            contentStream.beginText();
            contentStream.newLineAtOffset(200, yPosition);
            contentStream.showText(resultado);
            contentStream.endText();
            contentStream.setNonStrokingColor(0, 0, 0); // Volver a negro
            yPosition -= 25;

            // ID Envío
            contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText("ID Envio Asociado:");
            contentStream.endText();
            contentStream.beginText();
            contentStream.newLineAtOffset(200, yPosition);
            contentStream.showText(idEnvio);
            contentStream.endText();
            yPosition -= 25;

            // Detalles adicionales
            if (detalles != null && !detalles.isEmpty()) {
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Detalles:");
                contentStream.endText();
                contentStream.beginText();
                contentStream.newLineAtOffset(200, yPosition);
                contentStream.showText(detalles);
                contentStream.endText();
                yPosition -= 25;
            }

            // Línea separadora inferior
            contentStream.moveTo(50, yPosition - 10);
            contentStream.lineTo(550, yPosition - 10);
            contentStream.stroke();

            // Pie de página
            contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_OBLIQUE, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 50);
            contentStream.showText("Documento generado automaticamente - " + java.time.LocalDateTime.now());
            contentStream.endText();

            contentStream.close();

            // Guardar PDF
            String nombreArchivo = "Comprobante_" + idPago + ".pdf";
            String rutaCompleta = rutaCarpeta + "/" + nombreArchivo;
            document.save(rutaCompleta);
            document.close();

            System.out.println("PDF generado: " + rutaCompleta);
            return rutaCompleta;

        } catch (Exception e) {
            System.err.println("Error al generar PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }




}

