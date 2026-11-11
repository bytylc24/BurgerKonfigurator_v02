# T1 - Projekt-Setup

## 1. Was wurde gemacht

Wir haben ein leeres Maven-Projekt mit Java 21, JavaFX 21, Jackson und JUnit 5 angelegt. Der Maven Wrapper (`mvnw`) ist dabei, damit niemand Maven extra installieren muss. Die Paketstruktur aus `CLAUDE.md` gibt es schon, und `App.java` oeffnet ein Fenster "Burger-Konfigurator" (1000 x 700) mit drei leeren Tabs.

## 2. Neue und geaenderte Dateien

| Datei | Wofuer |
|---|---|
| `pom.xml` | Maven-Konfiguration: Java-Version, Bibliotheken, Plugins |
| `mvnw`, `mvnw.cmd`, `.mvn/wrapper/maven-wrapper.properties` | Maven Wrapper: laedt automatisch die richtige Maven-Version (3.9.9) herunter |
| `src/main/java/module-info.java` | Java-Modul: welche Bibliotheken wir brauchen, welche Pakete JavaFX und Jackson lesen duerfen |
| `src/main/java/at/htl/burgerkonfigurator/App.java` | Startklasse, baut das Fenster mit den drei Tabs |
| `src/main/java/at/htl/burgerkonfigurator/*/package-info.java` | Je eine Datei fuer `model`, `service`, `persistence`, `exception`, `controller`: beschreibt das Paket und sorgt dafuer, dass es existiert |
| `src/test/java/at/htl/burgerkonfigurator/AppTest.java` | Erster Test: zeigt, dass Bauen und Testen funktionieren |
| `.gitignore` | Dateien, die nicht ins Git sollen (`target/`, `.idea/`, ...) |
| `README.md` | Kurze Startanleitung |
| `docs/erklaerungen/T01-setup.md` | Diese Erklaerung |

## 3. Code erklaert

### pom.xml

```xml
<maven.compiler.release>21</maven.compiler.release>
```
Der Compiler erzeugt Code fuer Java 21. Das funktioniert auch, wenn auf dem Rechner ein neueres JDK (z. B. 24 oder 26) installiert ist.

Abhaengigkeiten:
- `javafx-controls`, `javafx-fxml`: Oberflaeche (Buttons, Tabs, ...) und FXML-Dateien
- `jackson-databind`: wandelt Java-Objekte in JSON um und zurueck (Speichern in T7)
- `jackson-datatype-jsr310`: damit Jackson auch `LocalDateTime` versteht
- `junit-jupiter` mit `<scope>test</scope>`: nur fuer die Tests, kommt nicht ins fertige Programm

Das `javafx-maven-plugin` erlaubt den Start mit `mvnw.cmd javafx:run`:
```xml
<mainClass>at.htl.burgerkonfigurator/at.htl.burgerkonfigurator.App</mainClass>
```
Vor dem `/` steht der Modulname, danach die Klasse mit `main`.

### module-info.java

```java
module at.htl.burgerkonfigurator {
    requires javafx.controls;
    ...
    opens at.htl.burgerkonfigurator.controller to javafx.fxml;
    opens at.htl.burgerkonfigurator.model to com.fasterxml.jackson.databind;
    exports at.htl.burgerkonfigurator;
}
```
- `requires`: diese Module (Bibliotheken) brauchen wir.
- `opens ... to javafx.fxml`: FXML erzeugt die Controller selbst und setzt die `@FXML`-Felder. Dafuer muss es in das Paket hineinschauen duerfen (Reflection).
- `opens ... to jackson.databind`: Jackson muss unsere Records lesen und neu erzeugen koennen.
- `exports`: JavaFX muss die Klasse `App` von aussen starten koennen.

Ohne `module-info.java` startet JavaFX aus IntelliJ oft nicht ("JavaFX runtime components are missing").

### package-info.java

```java
/**
 * Datenklassen (Zutaten, Burger, Bestellungen). Keine JavaFX-Abhaengigkeit.
 */
package at.htl.burgerkonfigurator.model;
```
Ein Paket, das in `module-info.java` mit `opens` steht, darf nicht leer sein, sonst gibt es einen Compilerfehler. Die `package-info.java` macht das Paket "vorhanden" und beschreibt gleichzeitig, wofuer es da ist.

### App.java

```java
public class App extends Application {
    public static final String TITEL = "Burger-Konfigurator";
    public static final int BREITE = 1000;
    public static final int HOEHE = 700;
```
`App` erbt von `Application` (JavaFX). Titel und Groesse sind Konstanten (`static final`), damit der Test sie pruefen kann.

```java
@Override
public void start(Stage stage) {
    TabPane tabPane = new TabPane(
            erstelleTab("Konfigurator"),
            erstelleTab("Meine Burger"),
            erstelleTab("Bestellhistorie"));
    stage.setTitle(TITEL);
    stage.setScene(new Scene(tabPane, BREITE, HOEHE));
    stage.show();
}
```
JavaFX ruft `start` automatisch auf. Die `Stage` ist das Fenster, die `Scene` der Inhalt. Die `TabPane` bekommt die drei Tabs direkt im Konstruktor.

```java
private Tab erstelleTab(String titel) {
    Tab tab = new Tab(titel);
    tab.setClosable(false);
    return tab;
}
```
Hilfsmethode, damit wir den gleichen Code nicht dreimal schreiben. `setClosable(false)` entfernt das "x" am Tab, damit der Benutzer keinen Tab schliessen kann.

```java
public static void main(String[] args) {
    launch(args);
}
```
`launch` startet JavaFX, das dann `start` aufruft.

In T4 wird der Inhalt der Tabs ueber FXML-Dateien geladen.

### AppTest.java

```java
@Test
void fensterHatRichtigenTitelUndGroesse() {
    assertEquals("Burger-Konfigurator", App.TITEL);
    ...
}
```
`@Test` markiert eine Testmethode fuer JUnit. `assertEquals(erwartet, tatsaechlich)` laesst den Test fehlschlagen, wenn die Werte verschieden sind. Der Test oeffnet kein Fenster, er zeigt nur, dass Bauen und Testen funktionieren.

In T1 gibt es noch keine Lambdas, Methodenreferenzen, Records oder Streams. Die kommen ab T2/T5.

## 4. Bezug zum Immutable Pattern

Noch keiner direkt. Vorbereitet ist aber: `opens ... model to com.fasterxml.jackson.databind` erlaubt Jackson spaeter, unsere unveraenderlichen Records (`Burger`, `Daten`) zu speichern und zu laden.

## 5. So testest du es

1. Tests: im Projektordner `mvnw.cmd clean test` (macOS/Linux: `./mvnw clean test`). Am Ende muss `BUILD SUCCESS` stehen, mit `Tests run: 1, Failures: 0`.
2. Starten: `mvnw.cmd javafx:run`. Es oeffnet sich ein Fenster "Burger-Konfigurator" mit den Tabs "Konfigurator", "Meine Burger" und "Bestellhistorie". Man kann zwischen den Tabs wechseln, sie haben kein "x" zum Schliessen und sind noch leer.
3. IntelliJ: `App.java` oeffnen und auf den gruenen Pfeil neben `main` klicken. Es soll dasselbe Fenster kommen.

## 6. Moegliche Lehrerfragen

**Wozu braucht man den Maven Wrapper?**
Damit jeder im Team (und der Lehrer) das Projekt mit derselben Maven-Version bauen kann, ohne Maven installieren zu muessen. `mvnw` laedt Maven beim ersten Mal selbst herunter.

**Was macht `module-info.java`?**
Es beschreibt unser Programm als Java-Modul: welche Module wir brauchen (`requires`) und welche Pakete andere Module sehen (`exports`) oder per Reflection lesen duerfen (`opens`).

**Warum `opens` und nicht `exports` beim Controller-Paket?**
`exports` erlaubt nur den Zugriff auf `public`-Sachen. FXML muss aber auch private `@FXML`-Felder setzen. Das geht nur mit Reflection, und dafuer braucht man `opens`.

**Warum hat JUnit `<scope>test</scope>`?**
JUnit wird nur zum Testen gebraucht. Mit `test` ist es beim Kompilieren der Tests da, aber nicht im fertigen Programm.

**Wer ruft die Methode `start` auf?**
JavaFX selbst. `main` ruft `launch()` auf, JavaFX baut alles auf und ruft dann `start(Stage)` mit dem Hauptfenster auf.
