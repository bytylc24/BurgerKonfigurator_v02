# PLAN.md - Burger-Konfigurator

Owners: **A** = Yll, **B** = partner. Both work on `main`: always pull before starting, commit and push when done.
A task may only start when its dependencies are ticked and pushed.
Every task ends with an explanation file in `docs/erklaerungen/` (see CLAUDE.md section 9).

Split: A owns the model and the Immutable pattern, B owns persistence and the main GUI. The tasks are arranged so that A and B never edit the same files at the same time.

## Order and parallel work

| Step | Person A | Person B |
|---|---|---|
| 1 | T1 Setup | install tools, wait for T1, clone |
| 2 | T2 Zutaten | T3 Exceptions, then T4 GUI layout |
| 3 | T5 Burger + Builder | T4 continued |
| 4 | T6 Bestellung, Daten, vordefinierte Burger | (wait for T6) |
| 5 | T8 BurgerService | T7 Persistence |
| 6 | T10 Meine Burger + Historie | T9 Konfigurator |
| 7 | T12a Docs | T11 Error handling + UX, then T12b Docs |
| 8 | T13 Final check together | T13 Final check together |

---

## Tasks

### [x] T1 - Project setup (A)
- Maven project with Maven Wrapper, Java 21, JavaFX 21, Jackson + jsr310, JUnit 5
- `module-info.java`, packages from CLAUDE.md
- `App.java`: window "Burger-Konfigurator" (1000x700) with a TabPane and the tabs "Konfigurator", "Meine Burger", "Bestellhistorie"
- `.gitignore` (target/, .idea/, *.iml, out/, .vscode/, .DS_Store), `README.md` stub
- Done when: tests run and the window opens

### [ ] T2 - Interface Zutat and ingredient enums (A) - depends on T1
- Interface `Zutat`: `bezeichnung()`, `preis()`, `kalorien()`
- Enums `Brot`, `Patty`, `Kaese` (with `KEIN`), `Sauce` (with `KEINE`), `Gemuese`, `Extra`, each 3 to 6 values with realistic price and kcal
- `toString()` returns the German name (so ComboBoxes show it nicely)
- Done when: test checks all values have price >= 0 and a name

### [ ] T3 - Exceptions (B) - depends on T1
- `BurgerException extends RuntimeException`, `ValidationException` and `PersistenceException extend BurgerException`
- Done when: compiles, small test

### [ ] T4 - GUI layout, no logic (B) - depends on T1
- `main.fxml` with TabPane, `konfigurator.fxml`, `meine-burger.fxml`, `historie.fxml` (via `fx:include`), empty controllers, `style.css`
- All controls from CLAUDE.md section 7, dummy values allowed
- Done when: app shows all three tabs with all controls

### [ ] T5 - Burger record and BurgerBuilder (A) - depends on T2, T3
- `Burger` as in CLAUDE.md section 5 (copied lists, validation, `mit...` / `ohne...`, `preis()`, `kalorien()`)
- `BurgerBuilder` with `von(Burger)` and `build()`
- Done when: tests for valid burger, every validation rule, unmodifiable lists, original unchanged, new id + basisId, price and kcal

### [ ] T6 - Bestellung, Daten, vordefinierte Burger (A) - depends on T5
- Records `Bestellung` (`id`, `zeitpunkt`, `burger`, `gesamtpreis()` method) and `Daten` (`burger`, `favoriten`, `bestellungen`)
- `VordefinierteBurger`: at least 4 (Cheeseburger, Bacon Burger, Veggie Burger, Chicken Burger) and a method `alle()`
- Done when: tests for immutability and total price

### [ ] T7 - Persistence (B) - depends on T6
- `BurgerRepository` interface (`Daten laden()`, `void speichern(Daten daten)`), `JsonBurgerRepository` as in CLAUDE.md section 6
- Done when: tests with `@TempDir`: save + load gives the same data, missing file, corrupt file

### [ ] T8 - BurgerService (A) - depends on T6 (uses the BurgerRepository interface from T7; if T7 is not pushed yet, create only the interface file exactly as described in T7)
- `alleBurger()`, `erstelle(BurgerBuilder)`, `leiteAb(Burger original, BurgerBuilder geaendert)`, `loesche(UUID)`, `toggleFavorit(UUID)`, `istFavorit(UUID)`, `bestelle(List<Burger>)`, `bestellungen()`
- Saves after every change
- Done when: tests with a small in-memory repository (lambda or anonymous class in the test)

### [ ] T9 - Konfigurator controller (B) - depends on T4, T7, T8
- Fill ComboBoxes and CheckBoxes from the enums, live price and kcal
- "Burger erstellen" calls the service, shows validation messages
- Edit mode: gets a burger from "Meine Burger", fills the form, saving creates a derived burger
- Done when: manual test: create, invalid input, edit as template

### [ ] T10 - Meine Burger and Bestellhistorie controllers (A) - depends on T4, T8
- TableView with columns from CLAUDE.md, filter "nur Favoriten", "abgeleitet von" shows the name of the original
- Buttons: Als Vorlage bearbeiten, Favorit, Loeschen (confirmation), Bestellen; Historie tab
- Done when: manual test, data still there after restart

### [ ] T11 - Error handling and UX (B) - depends on T9, T10
- One helper method for error dialogs, startup error (corrupt file) shown to the user
- Buttons disabled when no row is selected, Enter triggers "Burger erstellen"
- Done when: no exception reaches the console in normal use

### [ ] T12a - Documentation part A (A) - depends on T10
- `docs/dokumentation.md` (German): short app description, Immutable pattern description, why it fits, important classes (can reuse the explanation files)

### [ ] T12b - Documentation part B (B) - depends on T11
- UML class diagram `docs/klassendiagramm.puml` (PlantUML), description of data storage, README with start instructions

### [ ] T13 - Final check (A + B)
- Check against the assignment: OOP, persistence after restart, GUI/logic/data separation, pattern, validation, error handling, documentation
- All tests green, app starts from a fresh clone
