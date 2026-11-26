# T3 - Exceptions

## 1. Was wurde gemacht

Wir haben drei eigene Exceptions: `BurgerException` als Oberklasse und `ValidationException` und `PersistenceException` als Unterklassen. So kann man mit einem einzigen `catch (BurgerException e)` alle Fehler der Anwendung abfangen. Die Klassen gab es schon, aber die zwei Unterklassen haben direkt von `RuntimeException` geerbt. Das ist jetzt korrigiert, ausserdem gibt es Javadoc und einen Test.

## 2. Neue und geaenderte Dateien

| Datei | Wofuer |
|---|---|
| `exception/BurgerException.java` | Oberklasse aller eigenen Fehler, erbt von `RuntimeException` |
| `exception/ValidationException.java` | Fehler bei ungueltigen Eingaben (z. B. Name leer) |
| `exception/PersistenceException.java` | Fehler beim Laden/Speichern der Datei |
| `test/.../exception/ExceptionTest.java` | Prueft Vererbung, Meldung und Ursache |
| `PLAN.md` | T3 abgehakt |
| `docs/erklaerungen/T03-exceptions.md` | Diese Erklaerung |

(`exception/...` steht kurz fuer `src/main/java/at/htl/burgerkonfigurator/exception/...`.)

## 3. Code erklaert

### Die Vererbung

```
RuntimeException
└── BurgerException
    ├── ValidationException
    └── PersistenceException
```

```java
public class ValidationException extends BurgerException {
    public ValidationException(String message) {
        super(message);
    }
}
```
`extends BurgerException` heisst: eine `ValidationException` IST auch eine `BurgerException`. `super(message)` gibt die Meldung an die Oberklasse weiter, dort wird sie gespeichert. Spaeter holt man sie mit `getMessage()` und zeigt sie in der GUI an.

### Warum RuntimeException?

Eine `RuntimeException` ist "unchecked". Man muss sie nicht bei jeder Methode mit `throws` angeben. Der Code bleibt dadurch kuerzer. Abgefangen werden die Fehler erst in der GUI (Controller), wo eine Fehlermeldung angezeigt wird.

### Die Ursache (cause)

```java
public PersistenceException(String message, Throwable cause) {
    super(message, cause);
}
```
Beim Speichern kann eine `IOException` passieren. Die packen wir in eine `PersistenceException` ein: Der Benutzer bekommt eine deutsche Meldung, und die echte Ursache geht trotzdem nicht verloren (`getCause()`).

### Der Test mit Lambda

```java
assertThrows(BurgerException.class, () -> {
    throw new ValidationException("ungueltig");
});
```
`() -> { ... }` ist ein Lambda: ein kleines Stueck Code ohne Namen, das `assertThrows` selbst ausfuehrt. Der Test ist gruen, wenn dabei eine `BurgerException` (oder Unterklasse) geworfen wird.

## 4. Bezug zum Immutable Pattern

Die `ValidationException` sorgt dafuer, dass ein ungueltiger Burger gar nicht erst entstehen kann: Der Konstruktor vom `Burger`-Record (T5) wirft sie, bevor das Objekt fertig ist. Weil ein Burger danach nie mehr geaendert wird, bleibt er fuer immer gueltig.

## 5. So testest du es

- `mvnw.cmd -q clean test` (Mac: `./mvnw -q clean test`) muss ohne Fehler durchlaufen.
- In IntelliJ `ExceptionTest` oeffnen und auf den gruenen Pfeil klicken: 3 Tests gruen.

## 6. Moegliche Lehrerfragen

- **Warum eine eigene Oberklasse `BurgerException`?** Damit man alle Fehler der App mit einem `catch` abfangen kann und sie von fremden Fehlern unterscheiden kann.
- **Checked oder unchecked?** Unchecked (`RuntimeException`), damit man nicht ueberall `throws` schreiben muss.
- **Wofuer ist der Konstruktor mit `cause`?** Um eine technische Exception (z. B. `IOException`) einzupacken, ohne die Ursache zu verlieren.
- **Wo werden die Exceptions abgefangen?** In den Controllern; dort wird die Meldung in einem `Alert` angezeigt, nie als Stacktrace.
