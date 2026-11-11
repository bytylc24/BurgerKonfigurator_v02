# T2 - Interface Zutat und Zutaten-Enums

## 1. Was wurde gemacht

Wir haben das Interface `Zutat` und sechs Enums fuer die Zutaten angelegt: `Brot`, `Patty`, `Kaese`, `Sauce`, `Gemuese` und `Extra`. Jede Zutat hat einen deutschen Namen, einen Preis und Kalorien. `Kaese` hat den Wert `KEIN` und `Sauce` den Wert `KEINE`, damit man einen Burger auch ohne Kaese oder Sauce bauen kann.

## 2. Neue und geaenderte Dateien

| Datei | Wofuer |
|---|---|
| `model/Zutat.java` | Interface: jede Zutat hat `bezeichnung()`, `preis()`, `kalorien()` |
| `model/Brot.java` | 4 Brotsorten |
| `model/Patty.java` | 4 Patty-Sorten |
| `model/Kaese.java` | 4 Werte, davon `KEIN` |
| `model/Sauce.java` | 5 Werte, davon `KEINE` |
| `model/Gemuese.java` | 5 Gemuesesorten |
| `model/Extra.java` | 4 Extras (Bacon, Spiegelei, ...) |
| `test/.../model/ZutatTest.java` | Prueft alle Werte (Name, Preis >= 0, kcal >= 0, `toString`) |
| `PLAN.md` | T2 abgehakt |
| `docs/erklaerungen/T02-zutaten.md` | Diese Erklaerung |

(`model/...` steht kurz fuer `src/main/java/at/htl/burgerkonfigurator/model/...`.)

## 3. Code erklaert

### Das Interface Zutat

```java
public interface Zutat {
    String bezeichnung();
    BigDecimal preis();
    int kalorien();
}
```
Ein Interface ist ein "Vertrag": Jede Klasse oder jedes Enum, das `implements Zutat` schreibt, muss diese drei Methoden haben. Dadurch kann der Burger spaeter alle Zutaten gleich behandeln, egal ob es Brot oder Kaese ist, z. B. beim Zusammenzaehlen vom Preis.

### Ein Enum mit Werten

```java
public enum Kaese implements Zutat {
    KEIN("Kein Käse", "0.00", 0),
    CHEDDAR("Cheddar", "0.80", 110),
    GOUDA("Gouda", "0.80", 100),
    EMMENTALER("Emmentaler", "0.90", 105);
```
Ein Enum ist eine feste Liste von Werten. Es gibt genau diese vier Kaese, neue kann man zur Laufzeit nicht erzeugen. In den Klammern stehen Name, Preis und Kalorien. Diese Werte gehen an den Konstruktor.

```java
    private final String bezeichnung;
    private final BigDecimal preis;
    private final int kalorien;

    Kaese(String bezeichnung, String preis, int kalorien) {
        this.bezeichnung = bezeichnung;
        this.preis = new BigDecimal(preis);
        this.kalorien = kalorien;
    }
```
- Die Felder sind `final`, sie koennen sich also nie aendern.
- Der Konstruktor eines Enums ist automatisch privat. Java ruft ihn nur einmal pro Wert auf, beim Laden der Klasse.
- Der Preis wird als Text (`"0.80"`) uebergeben und mit `new BigDecimal(...)` umgewandelt. Warum nicht `double`? `double` rechnet ungenau (`0.1 + 0.2` ergibt `0.30000000000000004`). Bei Geld wollen wir exakte Werte, deshalb `BigDecimal`. Mit einem Text statt einer Kommazahl ist der Wert garantiert genau.

```java
    @Override
    public BigDecimal preis() {
        return preis;
    }
```
Das sind die Methoden aus dem Interface. `@Override` sagt dem Compiler: Diese Methode kommt aus `Zutat`. Wenn wir uns vertippen, meldet er einen Fehler.

### toString()

```java
    @Override
    public String toString() {
        return bezeichnung;
    }
```
Normalerweise liefert `toString()` bei einem Enum den Konstantennamen, also `CHEDDAR`. Eine JavaFX-ComboBox zeigt genau diesen Text an. Wir geben stattdessen den schoenen deutschen Namen zurueck ("Cheddar", "Kein Käse"). Die Namen in der Oberflaeche haben echte Umlaute, die Konstanten im Code nicht (`KAESE`), weil Umlaute in Namen im Code unueblich sind.

### Der Test

```java
private void pruefe(Zutat[] werte) {
    assertTrue(werte.length >= 3 && werte.length <= 6, "3 bis 6 Werte erwartet");
    for (Zutat zutat : werte) {
        assertFalse(zutat.bezeichnung().isBlank(), "Name fehlt");
        assertTrue(zutat.preis().compareTo(BigDecimal.ZERO) >= 0, "Preis negativ: " + zutat);
        ...
    }
}
```
- `Brot.values()` liefert ein Array mit allen Brotsorten. Weil `Brot` das Interface `Zutat` implementiert, darf man ein `Brot[]` an einen Parameter vom Typ `Zutat[]` uebergeben. So reicht eine Pruefmethode fuer alle sechs Enums.
- `compareTo` vergleicht zwei `BigDecimal`: Ergebnis `>= 0` heisst "groesser oder gleich". Mit `equals` vergleicht man BigDecimal nicht, weil `0.00` und `0` dort als verschieden gelten.
- Der Text am Ende jeder `assert`-Zeile wird angezeigt, wenn der Test fehlschlaegt.

In T2 gibt es noch keine Lambdas, Methodenreferenzen, Records oder Streams. Die kommen in T5 mit dem `Burger`.

## 4. Bezug zum Immutable Pattern

Enums sind von Natur aus unveraenderlich: Es gibt jeden Wert nur einmal, und alle Felder sind `final`. Das ist wichtig, weil der `Burger` (T5) diese Zutaten enthaelt. Wenn sich eine Zutat aendern koennte, waere der Burger nicht mehr wirklich unveraenderlich.

## 5. So testest du es

1. `mvnw.cmd clean test` (macOS/Linux: `./mvnw clean test`). Erwartet: `BUILD SUCCESS`, und in `ZutatTest` laufen 8 Tests ohne Fehler.
2. In IntelliJ: `ZutatTest.java` oeffnen und auf den gruenen Pfeil neben der Klasse klicken. Alle 8 Tests sollen gruen sein.
3. In der Oberflaeche aendert sich noch nichts. Die Zutaten werden erst ab T9 im Konfigurator angezeigt.

## 6. Moegliche Lehrerfragen

**Warum ein Enum und keine Klasse fuer die Zutaten?**
Die Zutaten sind eine feste Auswahl. Ein Enum garantiert, dass es nur genau diese Werte gibt, und man kann sie mit `values()` einfach fuer die ComboBoxen auflisten.

**Wozu brauchen wir das Interface Zutat?**
Damit alle sechs Enums gleich behandelt werden koennen. Der Burger kann z. B. den Preis aller Zutaten zusammenzaehlen, ohne zu wissen, ob es Brot oder Kaese ist.

**Warum BigDecimal statt double fuer den Preis?**
`double` speichert Kommazahlen binaer und rechnet bei Geld ungenau. `BigDecimal` rechnet exakt.

**Warum gibt es `KEIN` und `KEINE` statt einfach `null`?**
Mit `null` muesste man ueberall pruefen, ob ein Wert fehlt, sonst gibt es eine `NullPointerException`. `KEIN` ist ein normaler Wert mit Preis 0 und 0 kcal, man kann ganz normal damit rechnen.

**Warum ueberschreiben wir `toString()`?**
Die ComboBox zeigt `toString()` an. Ohne Ueberschreiben wuerde dort `CHEDDAR` stehen statt "Cheddar".
