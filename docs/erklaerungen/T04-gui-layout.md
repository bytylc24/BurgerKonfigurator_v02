# T4 - GUI-Layout (ohne Logik)

## 1. Was wurde gemacht

Die Oberflaeche ist jetzt in FXML-Dateien beschrieben: `main.fxml` mit dem TabPane und je eine Datei pro Tab. `App.java` laedt `main.fxml`, die drei Tabs werden mit `fx:include` eingebunden. Jeder Tab hat einen eigenen, noch leeren Controller. Alle wichtigen Controls haben eine `fx:id`, damit die Controller sie in T9 und T10 ansprechen koennen.

## 2. Neue und geaenderte Dateien

| Datei | Wofuer |
|---|---|
| `resources/.../view/main.fxml` | Hauptfenster: TabPane mit 3 Tabs per `fx:include` |
| `resources/.../view/konfigurator.fxml` | Formular: Name, 4 ComboBoxen, CheckBoxen, Preis, kcal, Button, Fehler-Label |
| `resources/.../view/meine-burger.fxml` | TableView mit 6 Spalten, 4 Buttons, CheckBox "nur Favoriten" |
| `resources/.../view/historie.fxml` | TableView: Datum, Burger, Gesamtpreis |
| `resources/.../view/style.css` | Farben und Schriften, `.error-label` fuer rote Fehler |
| `controller/MainController.java` | leer, gehoert zu main.fxml |
| `controller/KonfiguratorController.java` | leer, Logik in T9 |
| `controller/MeineBurgerController.java` | leer, Logik in T10 |
| `controller/HistorieController.java` | leer, Logik in T10 |
| `App.java` | laedt jetzt `view/main.fxml` statt die Tabs im Code zu bauen |
| `test/.../AppTest.java` | prueft zusaetzlich, dass alle FXML-Dateien gefunden werden |
| `PLAN.md` | T4 abgehakt |

(`resources/.../view/` steht fuer `src/main/resources/at/htl/burgerkonfigurator/view/`.)

Die FXML-Dateien lagen vorher in `src/main/java/.../recources/`. Dort kopiert Maven aber nur `.java`-Dateien, darum wurden sie nach `src/main/resources/` verschoben.

## 3. Code erklaert

### FXML laden

```java
Parent root = FXMLLoader.load(App.class.getResource(MAIN_FXML));
stage.setScene(new Scene(root, BREITE, HOEHE));
```
`getResource("view/main.fxml")` sucht die Datei relativ zum Paket von `App`, also in `at/htl/burgerkonfigurator/view/main.fxml`. `FXMLLoader.load` liest die XML-Datei und baut daraus die JavaFX-Objekte (TabPane, Buttons, ...). `Parent` ist der oberste Knoten.

### Tabs mit fx:include

```xml
<Tab text="Konfigurator">
    <content>
        <fx:include source="konfigurator.fxml"/>
    </content>
</Tab>
```
`fx:include` fuegt eine andere FXML-Datei ein. So bleibt jede Datei klein, und jeder Tab hat seinen eigenen Controller.

### Controller und fx:id

```xml
<VBox fx:controller="at.htl.burgerkonfigurator.controller.MeineBurgerController">
    <TableView fx:id="burgerTabelle">
```
`fx:controller` sagt, welche Java-Klasse zu dieser Datei gehoert. `fx:id` gibt einem Control einen Namen. Spaeter schreibt man im Controller `@FXML private TableView<Burger> burgerTabelle;` und JavaFX setzt das Feld automatisch. Dafuer steht in `module-info.java` `opens ...controller to javafx.fxml`.

### Fehler-Label

```xml
<Label fx:id="fehlerLabel" styleClass="error-label" wrapText="true"/>
```
```css
.error-label { -fx-text-fill: #c0392b; }
```
Am Anfang leer. In T9 schreibt der Controller die Meldung der `ValidationException` hinein, sie erscheint rot unter dem Formular.

### Platzhalter

Die CheckBoxen fuer Gemuese und Extras stehen jetzt fest in der FXML. In T9 erzeugt der Controller sie aus den Enums (`Gemuese.values()`), damit GUI und Enums immer zusammenpassen.

## 4. Bezug zum Immutable Pattern

Kein direkter Bezug. Die GUI ist aber klar vom Model getrennt: FXML beschreibt nur das Aussehen, die Controller reden spaeter nur mit dem `BurgerService`.

## 5. So testest du es

- `mvnw.cmd -q clean test`: muss ohne Fehler laufen (AppTest prueft, dass alle FXML-Dateien gefunden werden).
- `mvnw.cmd javafx:run` starten und pruefen:
  - Fenster "Burger-Konfigurator", 3 Tabs: Konfigurator, Meine Burger, Bestellhistorie, nicht schliessbar
  - Konfigurator: Namensfeld, 4 ComboBoxen (noch leer), CheckBoxen Gemuese und Extras, "0,00 EUR", "0 kcal", Button "Burger erstellen"
  - Meine Burger: "nur Favoriten", Tabelle mit 6 Spalten, 4 Buttons
  - Bestellhistorie: Tabelle mit 3 Spalten
  - Die Buttons machen noch nichts, das ist richtig so.

## 6. Moegliche Lehrerfragen

- **Warum FXML statt alles im Java-Code?** Aussehen (FXML) und Verhalten (Controller) sind getrennt. Die Dateien sind kuerzer und leichter zu lesen.
- **Was macht `fx:include`?** Bindet eine andere FXML-Datei ein, so bekommt jeder Tab eine eigene Datei und einen eigenen Controller.
- **Wofuer ist `fx:id`?** Damit der Controller ein Control ueber ein `@FXML`-Feld mit demselben Namen ansprechen kann.
- **Warum liegen die FXML-Dateien in `src/main/resources`?** Maven kopiert nur aus diesem Ordner Nicht-Java-Dateien in das fertige Programm.
- **Warum `opens ...controller to javafx.fxml`?** Der FXMLLoader erzeugt die Controller und setzt die `@FXML`-Felder ueber Reflection. Das erlaubt das Modulsystem nur mit `opens`.
