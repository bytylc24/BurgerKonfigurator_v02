package at.htl.burgerkonfigurator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Einfacher Test, der zeigt, dass das Projekt baut und die Tests laufen.
 */
class AppTest {

    @Test
    void fensterHatRichtigenTitelUndGroesse() {
        assertEquals("Burger-Konfigurator", App.TITEL);
        assertEquals(1000, App.BREITE);
        assertEquals(700, App.HOEHE);
    }
}
