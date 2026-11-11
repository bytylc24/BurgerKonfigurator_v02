package at.htl.burgerkonfigurator.model;

import java.math.BigDecimal;

/**
 * Gemuese, das man auf den Burger legen kann (mehrere moeglich).
 */
public enum Gemuese implements Zutat {
    SALAT("Salat", "0.30", 5),
    TOMATE("Tomate", "0.40", 10),
    ZWIEBEL("Zwiebel", "0.30", 10),
    GURKE("Essiggurke", "0.30", 5),
    JALAPENOS("Jalapeños", "0.50", 5);

    private final String bezeichnung;
    private final BigDecimal preis;
    private final int kalorien;

    Gemuese(String bezeichnung, String preis, int kalorien) {
        this.bezeichnung = bezeichnung;
        this.preis = new BigDecimal(preis);
        this.kalorien = kalorien;
    }

    /** {@inheritDoc} */
    @Override
    public String bezeichnung() {
        return bezeichnung;
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal preis() {
        return preis;
    }

    /** {@inheritDoc} */
    @Override
    public int kalorien() {
        return kalorien;
    }

    /**
     * Liefert den deutschen Namen, damit ComboBoxen und CheckBoxen ihn anzeigen.
     *
     * @return der deutsche Name der Zutat
     */
    @Override
    public String toString() {
        return bezeichnung;
    }
}
