package at.htl.burgerkonfigurator.model;

import at.htl.burgerkonfigurator.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Prueft den Burger-Record: Validierung, Unveraenderlichkeit, Preis und kcal.
 */
class BurgerTest {

    /** Ein gueltiger Burger: Weizen, Rind, Cheddar, Ketchup, Salat, Bacon. */
    private Burger cheeseBacon() {
        return new BurgerBuilder()
                .name("Cheese Bacon")
                .brot(Brot.WEIZEN)
                .patty(Patty.RIND)
                .kaese(Kaese.CHEDDAR)
                .sauce(Sauce.KETCHUP)
                .gemuese(List.of(Gemuese.SALAT))
                .extras(List.of(Extra.BACON))
                .build();
    }

    @Test
    void gueltigerBurgerHatAlleWerte() {
        Burger b = cheeseBacon();
        assertEquals("Cheese Bacon", b.name());
        assertEquals(List.of(Gemuese.SALAT), b.gemuese());
        assertNull(b.basisId());
    }

    @Test
    void preisUndKalorienWerdenZusammengezaehlt() {
        // 1.00 + 3.50 + 0.80 + 0.30 + 0.30 + 1.50 = 7.40
        Burger b = cheeseBacon();
        assertEquals(0, new BigDecimal("7.40").compareTo(b.preis()));
        // 150 + 250 + 110 + 20 + 5 + 120 = 655
        assertEquals(655, b.kalorien());
    }

    @Test
    void ohneBrotIstUngueltig() {
        BurgerBuilder builder = BurgerBuilder.von(cheeseBacon()).brot(null);
        assertThrows(ValidationException.class, builder::build);
    }

    @Test
    void ohnePattyIstUngueltig() {
        BurgerBuilder builder = BurgerBuilder.von(cheeseBacon()).patty(null);
        assertThrows(ValidationException.class, builder::build);
    }

    @Test
    void leererNameIstUngueltig() {
        assertThrows(ValidationException.class, () -> cheeseBacon().mitName("   "));
        assertThrows(ValidationException.class, () -> cheeseBacon().mitName(null));
    }

    @Test
    void zuLangerNameIstUngueltig() {
        assertThrows(ValidationException.class, () -> cheeseBacon().mitName("x".repeat(41)));
        assertEquals(40, cheeseBacon().mitName("x".repeat(40)).name().length());
    }

    @Test
    void doppeltesGemueseIstUngueltig() {
        assertThrows(ValidationException.class, () -> cheeseBacon().mitGemuese(Gemuese.SALAT));
    }

    @Test
    void doppeltesExtraIstUngueltig() {
        assertThrows(ValidationException.class, () -> cheeseBacon().mitExtra(Extra.BACON));
    }

    @Test
    void mehrAlsFuenfGemueseIstUngueltig() {
        List<Gemuese> sechs = new ArrayList<>(List.of(Gemuese.values()));
        sechs.add(Gemuese.SALAT);
        BurgerBuilder builder = BurgerBuilder.von(cheeseBacon()).gemuese(sechs);
        assertThrows(ValidationException.class, builder::build);
    }

    @Test
    void mehrAlsFuenfExtrasIstUngueltig() {
        List<Extra> sechs = new ArrayList<>(List.of(Extra.values()));
        sechs.add(Extra.BACON);
        sechs.add(Extra.AVOCADO);
        BurgerBuilder builder = BurgerBuilder.von(cheeseBacon()).extras(sechs);
        assertThrows(ValidationException.class, builder::build);
    }

    @Test
    void ohneKaeseUndSauceWirdKeinUndKeine() {
        Burger b = BurgerBuilder.von(cheeseBacon()).kaese(null).sauce(null).build();
        assertEquals(Kaese.KEIN, b.kaese());
        assertEquals(Sauce.KEINE, b.sauce());
    }

    @Test
    void zurueckgegebeneListenSindUnveraenderlich() {
        Burger b = cheeseBacon();
        assertThrows(UnsupportedOperationException.class, () -> b.gemuese().add(Gemuese.TOMATE));
        assertThrows(UnsupportedOperationException.class, () -> b.extras().clear());
    }

    @Test
    void aenderungDerUrsprungslisteAendertBurgerNicht() {
        List<Gemuese> liste = new ArrayList<>(List.of(Gemuese.SALAT));
        Burger b = new Burger(null, null, "Test", Brot.WEIZEN, Patty.RIND, null, null, liste, null, null);
        liste.add(Gemuese.TOMATE);
        assertEquals(List.of(Gemuese.SALAT), b.gemuese());
    }

    @Test
    void mitGemueseLaesstOriginalUnveraendert() {
        Burger a = cheeseBacon();
        Burger b = a.mitGemuese(Gemuese.ZWIEBEL);

        assertEquals(List.of(Gemuese.SALAT), a.gemuese());
        assertEquals(List.of(Gemuese.SALAT, Gemuese.ZWIEBEL), b.gemuese());
        assertNotEquals(a.id(), b.id());
        assertEquals(a.id(), b.basisId());
    }

    @Test
    void ohneExtraLaesstOriginalUnveraendert() {
        Burger a = cheeseBacon();
        Burger b = a.ohneExtra(Extra.BACON);

        assertEquals(List.of(Extra.BACON), a.extras());
        assertEquals(List.of(), b.extras());
        assertEquals(a.id(), b.basisId());
    }

    @Test
    void ohneGemueseUndMitExtraUndMitName() {
        Burger a = cheeseBacon();
        assertEquals(List.of(), a.ohneGemuese(Gemuese.SALAT).gemuese());
        assertEquals(List.of(Extra.BACON, Extra.SPIEGELEI), a.mitExtra(Extra.SPIEGELEI).extras());
        assertEquals("Neu", a.mitName("Neu").name());
        assertEquals("Cheese Bacon", a.name());
    }
}
