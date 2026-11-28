package cl.duoc.viviendas.model;

/**
 * Datos comunes de todos los registros de tipo vivienda.
 * <p>
 * El identificador (id) vale 0 mientras el registro no ha sido almacenado.
 * Los métodos set rechazan los datos inválidos con una IllegalArgumentException
 * cuyo mensaje explica el motivo.
 */
public abstract class Vivienda {

    private int id;
    private String codigo;
    private double superficie;
    private int habitaciones;

    protected Vivienda(String codigo, double superficie, int habitaciones) {
        setCodigo(codigo);
        setSuperficie(superficie);
        setHabitaciones(habitaciones);
    }

    /** @return identificador (0 si aún no ha sido almacenado). */
    public int getId() {
        return id;
    }

    /** @param id identificador asignado al almacenar el registro (no negativo). */
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("El identificador no puede ser negativo.");
        }
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código de propiedad no puede estar vacío.");
        }
        this.codigo = codigo.trim();
    }

    public double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(double superficie) {
        if (superficie < 20 || superficie > 500) {
            throw new IllegalArgumentException("La superficie debe estar entre 20 y 500 metros cuadrados.");
        }
        this.superficie = superficie;
    }

    public int getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(int habitaciones) {
        if (habitaciones <= 0) {
            throw new IllegalArgumentException("El número de habitaciones debe ser mayor que cero.");
        }
        this.habitaciones = habitaciones;
    }

    /** @return tipo del registro: "Departamento" o "Casa". */
    public abstract String getTipo();

    /** @return costo de arriendo, en pesos. */
    public abstract double calcularCostoArriendo();
}
