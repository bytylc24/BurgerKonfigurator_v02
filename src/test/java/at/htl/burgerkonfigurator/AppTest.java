package at.htl.burgerkonfigurator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Prueft die Fenster-Einstellungen und dass alle FXML-Dateien gefunden werden.
 */
class AppTest {

    @Test
    void fensterHatRichtigenTitelUndGroesse() {
        assertEquals("Burger-Konfigurator", App.TITEL);
        assertEquals(1000, App.BREITE);
        assertEquals(700, App.HOEHE);
    }

    @Test
    void alleFxmlDateienUndCssSindVorhanden() {
        assertNotNull(App.class.getResource(App.MAIN_FXML));
        assertNotNull(App.class.getResource("view/konfigurator.fxml"));
        assertNotNull(App.class.getResource("view/meine-burger.fxml"));
        assertNotNull(App.class.getResource("view/historie.fxml"));
        assertNotNull(App.class.getResource("view/style.css"));
    }
}
