package at.htl.burgerkonfigurator.model;

import java.math.BigDecimal;

/**
 * Patty-Sorten fuer den Burger. Jeder Burger braucht genau ein Patty.
 */
public enum Patty implements Zutat {
    RIND("Rindfleisch", "3.50", 250),
    HUHN("Hühnerfleisch", "3.00", 200),
    VEGGIE("Gemüse-Patty", "3.00", 180),
    FALAFEL("Falafel", "2.80", 220);

    private final String bezeichnung;
    private final BigDecimal preis;
    private final int kalorien;

    Patty(String bezeichnung, String preis, int kalorien) {
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
