package ejemplo.model;

public class Persona {
    private final long id;
    private final String nombre;
    private final String correo;

    public Persona(long id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }

    public long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
}