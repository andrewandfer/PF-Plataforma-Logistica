public class Producto{
    private String id:
    private String nombre;
    private int peso;

   public Producto(String id, String nombre,int peso){
       this.id=id;
       this.nombre=nombre;
       this.peso=peso;
   }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
}
