package cl.duoc.viviendas.model;

/**
 * Casa.
 * <p>
 * Costo de arriendo: base de $250.000; si tiene patio, se incrementa en un 10%.
 */
public class Casa extends Vivienda {

    public static final String TIPO = "Casa";
    private static final double COSTO_BASE = 250000;
    private static final double RECARGO = 0.1;

    private boolean patio;

    public Casa(String codigo, double superficie, int habitaciones, boolean patio) {
        super(codigo, superficie, habitaciones);
        this.patio = patio;
    }

    public boolean isPatio() {
        return patio;
    }

    public void setPatio(boolean patio) {
        this.patio = patio;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public double calcularCostoArriendo() {
        double costo = COSTO_BASE;
        if (patio) {
            costo += COSTO_BASE * RECARGO;
        }
        return costo;
    }
}
