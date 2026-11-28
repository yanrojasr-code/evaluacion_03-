package cl.duoc.viviendas.model;

/**
 * Departamento.
 * <p>
 * Costo de arriendo: base de $180.000; si el gasto común no está al día, se incrementa en un 15%.
 */
public class Departamento extends Vivienda implements ConEstacionamiento {

    public static final String TIPO = "Departamento";
    private static final double COSTO_BASE = 180000;
    private static final double RECARGO = 0.15;

    private int piso;
    private boolean gastoComunAlDia;
    private boolean estacionamiento;

    public Departamento(String codigo, double superficie, int habitaciones, int piso, boolean gastoComunAlDia, boolean estacionamiento) {
        super(codigo, superficie, habitaciones);
        setPiso(piso);
        this.gastoComunAlDia = gastoComunAlDia;
        this.estacionamiento = estacionamiento;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        if (piso <= 0) {
            throw new IllegalArgumentException("El piso debe ser mayor que cero.");
        }
        this.piso = piso;
    }

    public boolean isGastoComunAlDia() {
        return gastoComunAlDia;
    }

    public void setGastoComunAlDia(boolean gastoComunAlDia) {
        this.gastoComunAlDia = gastoComunAlDia;
    }

    @Override
    public boolean isEstacionamiento() {
        return estacionamiento;
    }

    @Override
    public void setEstacionamiento(boolean estacionamiento) {
        this.estacionamiento = estacionamiento;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public double calcularCostoArriendo() {
        double costo = COSTO_BASE;
        if (!gastoComunAlDia) {
            costo += COSTO_BASE * RECARGO;
        }
        return costo;
    }
}
