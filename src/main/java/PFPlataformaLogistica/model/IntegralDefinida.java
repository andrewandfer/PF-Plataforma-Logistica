package PFPlataformaLogistica.model;

import java.util.function.Function;

/**
 * Clase para el cálculo de integrales definidas mediante métodos numéricos.
 * Permite calcular el área bajo la curva de una función f(x) en el intervalo [a, b].
 *
 * Métodos disponibles:
 * - Regla del Trapecio
 * - Regla de Simpson
 */
public class IntegralDefinida {

    private final double limiteInferior;
    private final double limiteSuperior;
    private final int subintervalos;

    /**
     * Crea una instancia para calcular integrales definidas.
     *
     * @param limiteInferior límite inferior del intervalo (a)
     * @param limiteSuperior límite superior del intervalo (b)
     * @param subintervalos  cantidad de subintervalos para la aproximación (n)
     * @throws IllegalArgumentException si subintervalos es menor que 1 o si limiteInferior >= limiteSuperior
     */
    public IntegralDefinida(double limiteInferior, double limiteSuperior, int subintervalos) {
        if (subintervalos < 1) {
            throw new IllegalArgumentException("El número de subintervalos debe ser al menos 1");
        }
        if (limiteInferior >= limiteSuperior) {
            throw new IllegalArgumentException("El límite inferior debe ser menor que el límite superior");
        }
        this.limiteInferior = limiteInferior;
        this.limiteSuperior = limiteSuperior;
        this.subintervalos = subintervalos;
    }

    /**
     * Calcula la integral definida usando la regla del trapecio.
     *
     * Fórmula: (h/2) * [f(a) + 2*f(x1) + 2*f(x2) + ... + 2*f(x_{n-1}) + f(b)]
     *
     * @param funcion la función a integrar
     * @return el valor aproximado de la integral definida
     */
    public double calcularPorTrapecio(Function<Double, Double> funcion) {
        double h = (limiteSuperior - limiteInferior) / subintervalos;
        double suma = funcion.apply(limiteInferior) + funcion.apply(limiteSuperior);

        for (int i = 1; i < subintervalos; i++) {
            double x = limiteInferior + i * h;
            suma += 2 * funcion.apply(x);
        }

        return (h / 2.0) * suma;
    }

    /**
     * Calcula la integral definida usando la regla de Simpson.
     *
     * Requiere un número par de subintervalos. Si el número proporcionado es impar,
     * se ajusta automáticamente al siguiente número par.
     *
     * Fórmula: (h/3) * [f(a) + 4*f(x1) + 2*f(x2) + 4*f(x3) + ... + f(b)]
     *
     * @param funcion la función a integrar
     * @return el valor aproximado de la integral definida
     */
    public double calcularPorSimpson(Function<Double, Double> funcion) {
        int n = subintervalos % 2 == 0 ? subintervalos : subintervalos + 1;
        double h = (limiteSuperior - limiteInferior) / n;
        double suma = funcion.apply(limiteInferior) + funcion.apply(limiteSuperior);

        for (int i = 1; i < n; i++) {
            double x = limiteInferior + i * h;
            if (i % 2 == 0) {
                suma += 2 * funcion.apply(x);
            } else {
                suma += 4 * funcion.apply(x);
            }
        }

        return (h / 3.0) * suma;
    }

    public double getLimiteInferior() {
        return limiteInferior;
    }

    public double getLimiteSuperior() {
        return limiteSuperior;
    }

    public int getSubintervalos() {
        return subintervalos;
    }
}
