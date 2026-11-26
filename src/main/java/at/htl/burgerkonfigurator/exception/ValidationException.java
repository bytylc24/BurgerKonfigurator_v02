package at.htl.burgerkonfigurator.exception;

/**
 * Wird geworfen, wenn eingegebene Daten ungueltig sind (z. B. Name leer).
 */
public class ValidationException extends BurgerException {

    /**
     * Erstellt einen Validierungsfehler.
     *
     * @param message Fehlermeldung auf Deutsch, wird dem Benutzer angezeigt
     */
    public ValidationException(String message) {
        super(message);
    }
}
