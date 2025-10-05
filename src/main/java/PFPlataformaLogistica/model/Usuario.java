public class Usuario extends Persona{

    private String correo;
    private String telefono;
    private String direccion;
    private Linkedlist<String> listaDirecciones;
    private Producto producto   ;

    public Usuario(String correo, String telefono, String direccion,Producto producto) {
        super(nonbre,id );
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.listaDirecciones = new LinkedList<>();
        this.producto = producto;

    }
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Linkedlist<String> getListaDirecciones() {
        return listaDirecciones;
    }

    public void setListaDirecciones(Linkedlist<String> listaDirecciones) {
        this.listaDirecciones = listaDirecciones;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}