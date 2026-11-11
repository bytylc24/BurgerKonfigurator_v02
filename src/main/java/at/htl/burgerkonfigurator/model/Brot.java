package at.htl.burgerkonfigurator.model;

import java.math.BigDecimal;

/**
 * Brotsorten fuer den Burger. Jeder Burger braucht genau ein Brot.
 */
public enum Brot implements Zutat {
    WEIZEN("Weizenbrot", "1.00", 150),
    BRIOCHE("Brioche", "1.50", 200),
    VOLLKORN("Vollkornbrot", "1.20", 140),
    GLUTENFREI("Glutenfreies Brot", "1.80", 130);

    private final String bezeichnung;
    private final BigDecimal preis;
    private final int kalorien;

    Brot(String bezeichnung, String preis, int kalorien) {
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
