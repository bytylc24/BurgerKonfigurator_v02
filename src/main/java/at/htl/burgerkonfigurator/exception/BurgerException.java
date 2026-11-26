package at.htl.burgerkonfigurator.exception;

/**
 * Oberklasse aller eigenen Fehler im Burger-Konfigurator.
 * Ist eine RuntimeException, darum muss man sie nicht mit "throws" angeben.
 */
public class BurgerException extends RuntimeException {

    /**
     * Erstellt einen Fehler mit einer Meldung.
     *
     * @param message Fehlermeldung auf Deutsch
     */
    public BurgerException(String message) {
        super(message);
    }

    /**
     * Erstellt einen Fehler mit einer Meldung und der urspruenglichen Ursache.
     *
     * @param message Fehlermeldung auf Deutsch
     * @param cause   der urspruengliche Fehler (z. B. eine IOException)
     */
    public BurgerException(String message, Throwable cause) {
        super(message, cause);
    }
}
