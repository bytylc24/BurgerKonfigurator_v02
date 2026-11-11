package at.htl.burgerkonfigurator.model;

import java.math.BigDecimal;

/**
 * Extras fuer den Burger (mehrere moeglich).
 */
public enum Extra implements Zutat {
    BACON("Bacon", "1.50", 120),
    SPIEGELEI("Spiegelei", "1.00", 90),
    AVOCADO("Avocado", "1.50", 80),
    ROESTZWIEBELN("Röstzwiebeln", "0.70", 60);

    private final String bezeichnung;
    private final BigDecimal preis;
    private final int kalorien;

    Extra(String bezeichnung, String preis, int kalorien) {
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
