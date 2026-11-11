package at.htl.burgerkonfigurator.model;

import java.math.BigDecimal;

/**
 * Kaesesorten fuer den Burger. KEIN bedeutet: Burger ohne Kaese.
 */
public enum Kaese implements Zutat {
    KEIN("Kein Käse", "0.00", 0),
    CHEDDAR("Cheddar", "0.80", 110),
    GOUDA("Gouda", "0.80", 100),
    EMMENTALER("Emmentaler", "0.90", 105);

    private final String bezeichnung;
    private final BigDecimal preis;
    private final int kalorien;

    Kaese(String bezeichnung, String preis, int kalorien) {
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
