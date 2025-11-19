package PFPlataformaLogistica.model;

import PFPlataformaLogistica.dto.RepartidorDTO;
import PFPlataformaLogistica.dto.UsuarioDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EmpresaTest {

    private Empresa empresa;
    private Repartidor repartidor;

    @BeforeEach
    void setUp() {
        // Reiniciar la instancia singleton para cada test
        empresa = new Empresa();

        // Crear repartidor de prueba usando el builder
        repartidor = new Repartidor.RepartidorBuilder()
                .id("REP-001")
                .nombre("Juan Pérez")
                .telefono("123456789")
                .email("juan@empresa.com")
                .disponibilidad(EstadoRepartidor.ACTIVO)
                .zonaCobertura("Zona Norte")
                .localidad("Localidad A")
                .build();
    }

    @Test
    @DisplayName("Registrar repartidor exitosamente en lista existente")
    void registrarRepartidor() {
        // Arrange
        int tamañoInicial = empresa.getListaRepartidores().size();

        // Act
        empresa.registrarRepartidor(repartidor);

        // Assert
        List<Repartidor> repartidores = empresa.getListaRepartidores();
        assertEquals(tamañoInicial + 1, repartidores.size(), "Debería aumentar el tamaño de la lista");
        assertTrue(repartidores.contains(repartidor), "La lista debería contener el repartidor registrado");
    }

    @Test
    @DisplayName("Eliminar repartidor existente")
    void eliminarRepartidor() {
        // Arrange
        empresa.registrarRepartidor(repartidor);
        assertEquals(1, empresa.getListaRepartidores().size(), "Debería tener un repartidor inicialmente");

        // Act
        empresa.eliminarRepartidor("REP-001");

        // Assert
        assertEquals(0, empresa.getListaRepartidores().size(), "Debería eliminar el repartidor");
        assertTrue(empresa.getListaRepartidores().isEmpty(), "La lista debería estar vacía");
    }

    @Test
    @DisplayName("Eliminar repartidor inexistente")
    void eliminarRepartidor_Inexistente_NoDeberiaLanzarExcepcion() {
        // Arrange - lista vacía
        assertTrue(empresa.getListaRepartidores().isEmpty());

        // Act & Assert - No debería lanzar excepción
        assertDoesNotThrow(() -> {
            empresa.eliminarRepartidor("ID_INEXISTENTE");
        });
    }

    @Test
    @DisplayName("Eliminar repartidor con case insensitive")
    void eliminarRepartidor_CaseInsensitive_DeberiaFuncionar() {
        // Arrange
        empresa.registrarRepartidor(repartidor);
        assertEquals(1, empresa.getListaRepartidores().size());

        // Act - Usar minúsculas
        empresa.eliminarRepartidor("rep-001");

        // Assert
        assertEquals(0, empresa.getListaRepartidores().size(), "Debería eliminar con case insensitive");
    }

    @Test
    @DisplayName("Eliminar repartidor de lista con múltiples elementos")
    void eliminarRepartidor_ListaMultiple_DeberiaEliminarSoloUno() {
        // Arrange
        Repartidor repartidor1 = new Repartidor.RepartidorBuilder()
                .id("REP-001")
                .nombre("Primero")
                .telefono("111")
                .build();

        Repartidor repartidor2 = new Repartidor.RepartidorBuilder()
                .id("REP-002")
                .nombre("Segundo")
                .telefono("222")
                .build();

        empresa.registrarRepartidor(repartidor1);
        empresa.registrarRepartidor(repartidor2);
        assertEquals(2, empresa.getListaRepartidores().size());

        empresa.eliminarRepartidor("REP-001");


        assertEquals(1, empresa.getListaRepartidores().size(), "Debería eliminar solo un repartidor");
        assertFalse(empresa.getListaRepartidores().contains(repartidor1), "No debería contener el repartidor eliminado");
        assertTrue(empresa.getListaRepartidores().contains(repartidor2), "Debería mantener el otro repartidor");
    }

    @Test
    @DisplayName("Eliminar repartidor cuando lista es nula")
    void eliminarRepartidor_ListaNula_NoDeberiaLanzarExcepcion() {

        empresa.setListaRepartidores(null);
        assertNull(empresa.getListaRepartidores(), "La lista debería ser nula");


        assertDoesNotThrow(() -> {
            empresa.eliminarRepartidor("REP-001");
        });
    }

    @Test
    @DisplayName("Eliminar repartidor con ID nulo")
    void eliminarRepartidor_IdNulo_NoDeberiaLanzarExcepcion() {

        empresa.registrarRepartidor(repartidor);

        assertDoesNotThrow(() -> {
            empresa.eliminarRepartidor(null);
        });
    }

    @Test
    @DisplayName("Intentar eliminar mismo repartidor dos veces")
    void eliminarRepartidor_EliminarDosVeces_DeberiaManejarCorrectamente() {
        empresa.registrarRepartidor(repartidor);
        assertEquals(1, empresa.getListaRepartidores().size());

        empresa.eliminarRepartidor("REP-001");
        assertEquals(0, empresa.getListaRepartidores().size());
        empresa.eliminarRepartidor("REP-001");

        assertEquals(0, empresa.getListaRepartidores().size(), "Debería mantenerse vacía");
    }
}