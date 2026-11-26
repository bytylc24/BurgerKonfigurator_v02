package at.htl.burgerkonfigurator.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Prueft den BurgerBuilder.
 */
class BurgerBuilderTest {

    @Test
    void buildErstelltBurgerMitStandardwerten() {
        Burger b = new BurgerBuilder().name("Einfach").brot(Brot.BRIOCHE).patty(Patty.VEGGIE).build();
        assertEquals(Kaese.KEIN, b.kaese());
        assertEquals(Sauce.KEINE, b.sauce());
        assertEquals(List.of(), b.gemuese());
        assertNull(b.basisId());
    }

    @Test
    void vonErstelltAbgeleitetenBurger() {
        Burger original = new BurgerBuilder().name("Original").brot(Brot.WEIZEN).patty(Patty.RIND)
                .extras(List.of(Extra.BACON)).build();

        Burger kopie = BurgerBuilder.von(original).name("Kopie").build();

        assertNotEquals(original.id(), kopie.id());
        assertEquals(original.id(), kopie.basisId());
        assertEquals(List.of(Extra.BACON), kopie.extras());
        assertEquals("Original", original.name());
    }
}
