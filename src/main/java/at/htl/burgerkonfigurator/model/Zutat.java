package at.htl.burgerkonfigurator.model;

import java.math.BigDecimal;

/**
 * Gemeinsame Schnittstelle fuer alle Zutaten eines Burgers.
 * Jede Zutat hat einen deutschen Namen, einen Preis und Kalorien.
 */
public interface Zutat {

    /**
     * Liefert den deutschen Namen der Zutat.
     *
     * @return Name, z. B. "Cheddar"
     */
    String bezeichnung();

    /**
     * Liefert den Preis der Zutat.
     *
     * @return Preis in Euro, nie negativ
     */
    BigDecimal preis();

    /**
     * Liefert die Kalorien der Zutat.
     *
     * @return Kalorien in kcal, nie negativ
     */
    int kalorien();
}
