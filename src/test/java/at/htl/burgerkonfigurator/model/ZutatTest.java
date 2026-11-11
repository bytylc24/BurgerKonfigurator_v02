package at.htl.burgerkonfigurator.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prueft, dass alle Zutaten sinnvolle Werte haben.
 */
class ZutatTest {

    @Test
    void brotHatGueltigeWerte() {
        pruefe(Brot.values());
    }

    @Test
    void pattyHatGueltigeWerte() {
        pruefe(Patty.values());
    }

    @Test
    void kaeseHatGueltigeWerte() {
        pruefe(Kaese.values());
    }

    @Test
    void sauceHatGueltigeWerte() {
        pruefe(Sauce.values());
    }

    @Test
    void gemueseHatGueltigeWerte() {
        pruefe(Gemuese.values());
    }

    @Test
    void extraHatGueltigeWerte() {
        pruefe(Extra.values());
    }

    @Test
    void keinKaeseUndKeineSauceKostenNichts() {
        assertEquals(0, Kaese.KEIN.preis().compareTo(BigDecimal.ZERO));
        assertEquals(0, Kaese.KEIN.kalorien());
        assertEquals(0, Sauce.KEINE.preis().compareTo(BigDecimal.ZERO));
        assertEquals(0, Sauce.KEINE.kalorien());
    }

    @Test
    void toStringLiefertDeutschenNamen() {
        assertEquals("Cheddar", Kaese.CHEDDAR.toString());
        assertEquals("Kein Käse", Kaese.KEIN.toString());
    }

    /** Prueft alle Werte eines Enums: 3 bis 6 Werte, Name, Preis >= 0, kcal >= 0. */
    private void pruefe(Zutat[] werte) {
        assertTrue(werte.length >= 3 && werte.length <= 6, "3 bis 6 Werte erwartet");
        for (Zutat zutat : werte) {
            assertFalse(zutat.bezeichnung().isBlank(), "Name fehlt");
            assertTrue(zutat.preis().compareTo(BigDecimal.ZERO) >= 0, "Preis negativ: " + zutat);
            assertTrue(zutat.kalorien() >= 0, "kcal negativ: " + zutat);
            assertEquals(zutat.bezeichnung(), zutat.toString());
        }
    }
}
