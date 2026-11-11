package at.htl.burgerkonfigurator.model;

import java.math.BigDecimal;

/**
 * Saucen fuer den Burger. KEINE bedeutet: Burger ohne Sauce.
 */
public enum Sauce implements Zutat {
    KEINE("Keine Sauce", "0.00", 0),
    KETCHUP("Ketchup", "0.30", 20),
    MAYONNAISE("Mayonnaise", "0.40", 90),
    BBQ("BBQ-Sauce", "0.50", 40),
    BURGERSAUCE("Burgersauce", "0.50", 70);

    private final String bezeichnung;
    private final BigDecimal preis;
    private final int kalorien;

    Sauce(String bezeichnung, String preis, int kalorien) {
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
