package at.htl.burgerkonfigurator.exception;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests fuer die eigenen Exceptions.
 */
class ExceptionTest {

    @Test
    void validationExceptionIstBurgerException() {
        ValidationException e = new ValidationException("Name fehlt");
        assertInstanceOf(BurgerException.class, e);
        assertEquals("Name fehlt", e.getMessage());
    }

    @Test
    void persistenceExceptionMerktSichUrsache() {
        IOException ursache = new IOException("Datei gesperrt");
        PersistenceException e = new PersistenceException("Speichern fehlgeschlagen", ursache);
        assertInstanceOf(BurgerException.class, e);
        assertSame(ursache, e.getCause());
    }

    @Test
    void burgerExceptionKannGefangenWerden() {
        assertThrows(BurgerException.class, () -> {
            throw new ValidationException("ungueltig");
        });
    }
}
