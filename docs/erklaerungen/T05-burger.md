# T5 - Burger-Record und BurgerBuilder

## 1. Was wurde gemacht

Wir haben den `Burger` als Java-`record` gebaut. Ein Burger kann nach dem Erstellen nie mehr geaendert werden (Immutable Pattern). Methoden wie `mitGemuese(...)` liefern einen NEUEN Burger, das Original bleibt gleich. Dazu gibt es den `BurgerBuilder`, einen kleinen veraenderbaren Helfer fuer die GUI, der am Ende mit `build()` den fertigen Burger erstellt.

## 2. Neue und geaenderte Dateien

| Datei | Wofuer |
|---|---|
| `model/Burger.java` | Record: Daten, Validierung, `mit...`/`ohne...`, `preis()`, `kalorien()` |
| `model/BurgerBuilder.java` | Helfer fuer die GUI, `von(Burger)` und `build()` |
| `test/.../model/BurgerTest.java` | 16 Tests: Validierung, Unveraenderlichkeit, Preis, kcal |
| `test/.../model/BurgerBuilderTest.java` | 2 Tests: Standardwerte und `von(...)` |
| `PLAN.md` | T5 abgehakt |
| `docs/erklaerungen/T05-burger.md` | Diese Erklaerung |

(`model/...` steht kurz fuer `src/main/java/at/htl/burgerkonfigurator/model/...`.)

## 3. Code erklaert

### Der Record

```java
public record Burger(UUID id, UUID basisId, String name, Brot brot, Patty patty, Kaese kaese,
                     Sauce sauce, List<Gemuese> gemuese, List<Extra> extras, LocalDateTime erstelltAm) {
```
Ein `record` erzeugt automatisch: private final Felder, einen Konstruktor, Getter (`name()`, `brot()`, ...), `equals`, `hashCode` und `toString`. Es gibt **keine Setter** und die Klasse ist `final`. Darum ist ein Record automatisch unveraenderlich.

- `id`: eindeutige Nummer (`UUID` = zufaellige, weltweit eindeutige ID)
- `basisId`: id des Burgers, von dem dieser abgeleitet wurde, bei Originalen `null`

### Der kompakte Konstruktor

```java
public Burger {
    if (kaese == null) {
        kaese = Kaese.KEIN;
    }
    name = pruefeName(name);
    if (brot == null) {
        throw new ValidationException("Bitte ein Brot auswählen.");
    }
    ...
    gemuese = gemuese == null ? List.of() : List.copyOf(gemuese);
    pruefeListe(gemuese, "Gemüse");
}
```
Der kompakte Konstruktor (ohne Klammern nach `Burger`) laeuft bei JEDEM `new Burger(...)`, bevor die Felder gesetzt werden. Hier pruefen wir alles. Ist etwas falsch, wird eine `ValidationException` geworfen, und der Burger entsteht gar nicht.

`List.copyOf(...)` macht eine **unveraenderliche Kopie** der Liste. Das ist wichtig: Sonst koennte jemand die Liste von aussen aendern, die er dem Konstruktor gegeben hat, und damit den Burger aendern.

### Validierung mit Stream

```java
if (liste.stream().distinct().count() != liste.size()) {
    throw new ValidationException(art + " darf nicht doppelt vorkommen.");
}
```
`stream()` geht die Liste Element fuer Element durch. `distinct()` laesst doppelte weg, `count()` zaehlt. Ist die Anzahl kleiner als vorher, war etwas doppelt.

### "Aendern" = neuen Burger erstellen

```java
public Burger mitGemuese(Gemuese g) {
    List<Gemuese> neu = new ArrayList<>(gemuese);
    neu.add(g);
    return abgeleitet(name, neu, extras);
}

private Burger abgeleitet(String neuerName, List<Gemuese> neuesGemuese, List<Extra> neueExtras) {
    return new Burger(UUID.randomUUID(), id, neuerName, brot, patty, kaese, sauce,
            neuesGemuese, neueExtras, LocalDateTime.now());
}
```
Wir kopieren die Liste in eine neue `ArrayList`, fuegen das Gemuese hinzu und erstellen damit einen neuen Burger. Der neue Burger bekommt eine neue `id`, und `basisId` ist die `id` vom Original. Das Original wird nicht angefasst.

```java
public Burger ohneGemuese(Gemuese g) {
    return abgeleitet(name, gemuese.stream().filter(x -> x != g).toList(), extras);
}
```
`x -> x != g` ist ein **Lambda**: eine kleine Funktion ohne Namen. `filter` behaelt nur die Elemente, fuer die das Lambda `true` liefert, also alle ausser `g`. `toList()` macht daraus wieder eine Liste.

### Preis und Kalorien

```java
public BigDecimal preis() {
    return zutaten().stream()
            .map(Zutat::preis)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
}
```
`zutaten()` liefert alle Zutaten (Brot, Patty, Kaese, Sauce, Gemuese, Extras) als eine Liste. Das geht, weil alle Enums das Interface `Zutat` haben.
- `map(Zutat::preis)`: aus jeder Zutat wird ihr Preis. `Zutat::preis` ist eine **Methodenreferenz**, kurz fuer `z -> z.preis()`.
- `reduce(BigDecimal.ZERO, BigDecimal::add)`: faengt bei 0 an und zaehlt alle Preise zusammen.

```java
public int kalorien() {
    return zutaten().stream().mapToInt(Zutat::kalorien).sum();
}
```
`mapToInt` macht aus jeder Zutat eine Zahl, `sum()` zaehlt sie zusammen.

### Der BurgerBuilder

```java
Burger b = new BurgerBuilder()
        .name("Cheese Bacon")
        .brot(Brot.WEIZEN)
        .patty(Patty.RIND)
        .build();
```
Der Builder ist **veraenderbar**: Die GUI setzt Schritt fuer Schritt die Werte (z. B. wenn der Benutzer eine ComboBox aendert). Jede Methode gibt `this` zurueck, darum kann man die Aufrufe hintereinander schreiben. Erst `build()` erstellt den unveraenderlichen Burger.

```java
BurgerBuilder.von(original).name("Kopie").build();
```
`von(...)` fuellt den Builder mit den Werten eines bestehenden Burgers und merkt sich dessen `id` als `basisId`. So funktioniert spaeter "Als Vorlage bearbeiten": Der neue Burger ist abgeleitet, das Original bleibt.

## 4. Bezug zum Immutable Pattern

- Record = final Klasse, final Felder, keine Setter.
- `List.copyOf` im Konstruktor: Listen sind unveraenderliche Kopien. `b.gemuese().add(...)` wirft eine `UnsupportedOperationException`.
- Validierung im Konstruktor: Ein ungueltiger Burger kann nie existieren.
- `mit...`/`ohne...` liefern einen neuen Burger mit neuer `id` und `basisId` = `id` vom Original. Beispiel aus der Angabe: Burger A (Cheese + Bacon) -> `a.mitGemuese(Gemuese.ZWIEBEL)` -> Burger B (Cheese + Bacon + Zwiebel), Burger A bleibt gleich.
- Der `BurgerBuilder` ist absichtlich veraenderbar, aber er ist kein Burger. Er ist nur ein Werkzeug, um einen Burger zu bauen.

## 5. So testest du es

- `mvnw.cmd -q clean test` (Mac: `./mvnw -q clean test`) muss ohne Fehler laufen.
- In IntelliJ `BurgerTest` und `BurgerBuilderTest` ausfuehren: 18 Tests gruen.
- Wichtige Tests: `mitGemueseLaesstOriginalUnveraendert`, `zurueckgegebeneListenSindUnveraenderlich`, `aenderungDerUrsprungslisteAendertBurgerNicht`.

## 6. Moegliche Lehrerfragen

- **Warum ist ein Record unveraenderlich?** Die Klasse ist final, alle Felder sind private final, es gibt keine Setter.
- **Warum `List.copyOf` im Konstruktor?** Sonst koennte man die Liste, die man hineingegeben hat, spaeter aendern und damit den Burger aendern. `List.copyOf` macht eine unveraenderliche Kopie.
- **Wie "aendert" man einen Burger?** Gar nicht. `mitGemuese(...)` usw. erstellen einen neuen Burger mit neuer `id`. `basisId` zeigt auf das Original.
- **Warum ist der BurgerBuilder veraenderbar? Widerspricht das nicht dem Pattern?** Nein. Der Builder ist nur ein Hilfsmittel fuer die GUI. Das Ergebnis von `build()` ist ein unveraenderlicher Burger.
- **Warum `BigDecimal` statt `double` fuer den Preis?** `double` rechnet ungenau (z. B. `0.1 + 0.2 = 0.30000000000000004`). `BigDecimal` rechnet mit Geld exakt.
