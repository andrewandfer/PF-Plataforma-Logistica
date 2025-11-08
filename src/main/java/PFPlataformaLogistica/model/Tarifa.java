package PFPlataformaLogistica.model;

public class Tarifa {
    private float base;
    private float peso;
    private float volumen;
    private int prioridad;
    private float recargo;
    public Tarifa(float base, float peso, float volumen, int prioridad, float recargo) {
        this.base = base;
        this.peso = peso;
        this.volumen = volumen;
        this.prioridad = prioridad;
        this.recargo = recargo;
    }

    public float getBase() {
        return base;
    }

    public void setBase(float base) {
        this.base = base;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getVolumen() {
        return volumen;
    }

    public void setVolumen(float volumen) {
        this.volumen = volumen;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public float getRecargo() {
        return recargo;
    }

    public void setRecargo(float recargo) {
        this.recargo = recargo;
    }
}
