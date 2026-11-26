package at.htl.burgerkonfigurator.exception;

/**
 * Wird geworfen, wenn das Laden oder Speichern der Daten nicht klappt.
 */
public class PersistenceException extends BurgerException {

    /**
     * Erstellt einen Speicherfehler.
     *
     * @param message Fehlermeldung auf Deutsch
     */
    public PersistenceException(String message) {
        super(message);
    }

    /**
     * Erstellt einen Speicherfehler, der eine andere Exception einpackt.
     *
     * @param message Fehlermeldung auf Deutsch
     * @param cause   der urspruengliche Fehler (z. B. eine IOException)
     */
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
