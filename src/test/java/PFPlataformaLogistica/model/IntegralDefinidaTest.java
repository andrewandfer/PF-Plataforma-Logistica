package PFPlataformaLogistica.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class IntegralDefinidaTest {

    @Test
    @DisplayName("Integral de función constante f(x)=5 en [0,10] debe ser 50")
    void calcularPorTrapecio_FuncionConstante() {
        IntegralDefinida integral = new IntegralDefinida(0, 10, 100);
        double resultado = integral.calcularPorTrapecio(x -> 5.0);
        assertEquals(50.0, resultado, 0.0001, "La integral de f(x)=5 en [0,10] debe ser 50");
    }

    @Test
    @DisplayName("Integral de f(x)=x en [0,1] debe ser 0.5 (Trapecio)")
    void calcularPorTrapecio_FuncionLineal() {
        IntegralDefinida integral = new IntegralDefinida(0, 1, 1000);
        double resultado = integral.calcularPorTrapecio(x -> x);
        assertEquals(0.5, resultado, 0.0001, "La integral de f(x)=x en [0,1] debe ser 0.5");
    }

    @Test
    @DisplayName("Integral de f(x)=x^2 en [0,1] debe ser 1/3 (Trapecio)")
    void calcularPorTrapecio_FuncionCuadratica() {
        IntegralDefinida integral = new IntegralDefinida(0, 1, 1000);
        double resultado = integral.calcularPorTrapecio(x -> x * x);
        assertEquals(1.0 / 3.0, resultado, 0.001, "La integral de f(x)=x^2 en [0,1] debe ser 1/3");
    }

    @Test
    @DisplayName("Integral de f(x)=x en [0,1] debe ser 0.5 (Simpson)")
    void calcularPorSimpson_FuncionLineal() {
        IntegralDefinida integral = new IntegralDefinida(0, 1, 100);
        double resultado = integral.calcularPorSimpson(x -> x);
        assertEquals(0.5, resultado, 0.0001, "La integral de f(x)=x en [0,1] debe ser 0.5");
    }

    @Test
    @DisplayName("Integral de f(x)=x^2 en [0,1] debe ser 1/3 (Simpson)")
    void calcularPorSimpson_FuncionCuadratica() {
        IntegralDefinida integral = new IntegralDefinida(0, 1, 100);
        double resultado = integral.calcularPorSimpson(x -> x * x);
        assertEquals(1.0 / 3.0, resultado, 0.0001, "La integral de f(x)=x^2 en [0,1] debe ser 1/3");
    }

    @Test
    @DisplayName("Simpson es más preciso que Trapecio para f(x)=x^3")
    void calcularPorSimpson_MasPrecisoQueTrapecio() {
        IntegralDefinida integral = new IntegralDefinida(0, 1, 10);
        double valorExacto = 0.25; // integral de x^3 en [0,1] = 1/4
        double resultadoTrapecio = integral.calcularPorTrapecio(x -> x * x * x);
        double resultadoSimpson = integral.calcularPorSimpson(x -> x * x * x);

        double errorTrapecio = Math.abs(valorExacto - resultadoTrapecio);
        double errorSimpson = Math.abs(valorExacto - resultadoSimpson);

        assertTrue(errorSimpson <= errorTrapecio,
                "Simpson debería ser al menos tan preciso como Trapecio");
    }

    @Test
    @DisplayName("Subintervalos menores a 1 deben lanzar excepción")
    void constructor_SubintervalosInvalidos_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            new IntegralDefinida(0, 1, 0);
        });
    }

    @Test
    @DisplayName("Límite inferior mayor o igual que superior debe lanzar excepción")
    void constructor_LimitesInvalidos_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            new IntegralDefinida(5, 3, 100);
        });
    }

    @Test
    @DisplayName("Verificar getters devuelven valores correctos")
    void getters_DeberianDevolverValoresCorrectos() {
        IntegralDefinida integral = new IntegralDefinida(2.0, 8.0, 50);
        assertEquals(2.0, integral.getLimiteInferior());
        assertEquals(8.0, integral.getLimiteSuperior());
        assertEquals(50, integral.getSubintervalos());
    }

    @Test
    @DisplayName("Simpson ajusta subintervalos impares a pares automáticamente")
    void calcularPorSimpson_SubintervalosImpares_AjustaAutomaticamente() {
        IntegralDefinida integral = new IntegralDefinida(0, 1, 99);
        double resultado = integral.calcularPorSimpson(x -> x * x);
        assertEquals(1.0 / 3.0, resultado, 0.001,
                "Simpson debe funcionar correctamente incluso con subintervalos impares");
    }
}
