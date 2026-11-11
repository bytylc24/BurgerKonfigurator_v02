# CLAUDE.md - Burger-Konfigurator (HTL 3. Klasse, Projekt 11)

Claude Code reads this file automatically. It describes the project, the code style and the workflow. The task list is in `PLAN.md`.

Repository: https://github.com/bytylc24/BurgerKonfigurator_v02.git

## 1. Project

JavaFX desktop application in which users configure individual burgers.
Mandatory design pattern: **Immutable**. A finished burger can never be changed. Every "change" creates a NEW burger; the original stays exactly as it was.

Team: 2 students (Person A = Yll, Person B = partner). Claude Code writes the code. The students test, commit and push themselves.

### Must requirements (Muss)
- A burger can contain: Brot, Patty, Kaese, Sauce, Gemuese, Extras
- After creation the burger object cannot be modified
- Changing a burger creates a new burger (Burger A: Cheese + Bacon -> Burger B: Cheese + Bacon + Onion, Burger A unchanged)
- Persistent storage: data still exists after a restart
- Clear separation of GUI, logic and data storage
- Validation of user input, handling of error cases
- Clear, user-friendly GUI

### Optional requirements (Kann) - all planned
- Preis, Naehrwerte (kcal), Favoriten, Bestellhistorie, vordefinierte Burger

## 2. Code style: as simple as possible

The code must be easy to read and explain for a 3rd-year HTL student. Choose the simplest solution that fulfils the requirement.

Use:
- `record` for immutable data (`Burger`, `Bestellung`, `Daten`)
- `enum` for fixed choices (ingredients)
- Lambdas and method references where they make code shorter and clearer (e.g. `button.setOnAction(e -> erstelleBurger())`, `.map(Zutat::preis)`, `.reduce(BigDecimal.ZERO, BigDecimal::add)`)
- Simple streams (`filter`, `map`, `sum`, `toList()`)
- `List.of`, `List.copyOf`, `Set.copyOf`

Avoid:
- Unnecessary interfaces, abstract classes or inheritance (only `Zutat` and `BurgerRepository` are wanted)
- Extra design patterns, frameworks, reflection, threads, generics tricks
- DTO layers or mapper classes (Jackson can read and write records directly)
- Methods longer than about 30 lines, deeply nested code
- Clever one-liners that are hard to explain

Other conventions:
- Domain names German (as in the assignment). All texts in the GUI in German.
- Short German Javadoc on every class and every public method.
- Money as `BigDecimal`, displayed as `12,50 EUR`.
- No `System.out` debugging left in the code.

## 3. Tech stack

- Java 21, JavaFX 21 (`org.openjfx`), Maven with Maven Wrapper (`mvnw`)
- `javafx-maven-plugin` (start: `mvnw.cmd javafx:run` on Windows, `./mvnw javafx:run` on macOS/Linux)
- Jackson Databind + `jackson-datatype-jsr310` (for `LocalDateTime`)
- JUnit 5
- `module-info.java` (needed so JavaFX also starts directly from IntelliJ)
- Do NOT add other dependencies without asking.

## 4. Package structure

Base package: `at.htl.burgerkonfigurator`

```
at.htl.burgerkonfigurator
├── App.java                     starts JavaFX, creates repository + service
├── model                        no JavaFX imports
│   ├── Zutat.java               interface: bezeichnung(), preis(), kalorien()
│   ├── Brot, Patty, Kaese, Sauce, Gemuese, Extra    enums implementing Zutat
│   ├── Burger.java              record (immutable)
│   ├── BurgerBuilder.java       simple builder for the GUI
│   ├── Bestellung.java          record (immutable)
│   ├── Daten.java               record: burger, favoriten, bestellungen
│   └── VordefinierteBurger.java static factory methods
├── service
│   └── BurgerService.java       business logic, the only class controllers talk to
├── persistence
│   ├── BurgerRepository.java    interface: laden(), speichern(Daten)
│   └── JsonBurgerRepository.java
├── exception
│   ├── BurgerException.java     extends RuntimeException
│   ├── ValidationException.java
│   └── PersistenceException.java
└── controller                   one controller per FXML
resources/at/htl/burgerkonfigurator/view/*.fxml, style.css
```

Rules:
- Controllers only use `BurgerService`, never the repository.
- `model` does not depend on JavaFX, service or persistence.
- `BurgerService` gets a `BurgerRepository` in its constructor (created in `App`).
- `module-info.java` opens `controller` to `javafx.fxml` and `model` to `com.fasterxml.jackson.databind`.

## 5. Immutable pattern

Implemented with Java records, because a record is automatically immutable: the class is final, all fields are private final, there are no setters.

`Burger` record components: `id (UUID)`, `basisId (UUID, null for original burgers)`, `name`, `brot`, `patty`, `kaese`, `sauce`, `gemuese (List<Gemuese>)`, `extras (List<Extra>)`, `erstelltAm (LocalDateTime)`

Rules:
1. The compact constructor copies lists with `List.copyOf(...)` (otherwise someone could change the list from outside) and validates the data (see below), so an invalid burger can never exist.
2. "Changes" are methods that return a NEW burger with a new `id` and `basisId` = id of the original: `mitGemuese(Gemuese g)`, `ohneGemuese(Gemuese g)`, `mitExtra(Extra e)`, `ohneExtra(Extra e)`, `mitName(String n)`. The original stays unchanged.
3. `preis()` and `kalorien()` are normal methods that add up all ingredients with a stream.
4. `BurgerBuilder` is a small mutable helper for the GUI; `build()` creates the record. `BurgerBuilder.von(Burger)` fills a builder from an existing burger ("Als Vorlage bearbeiten" creates a derived burger).
5. Favorites are NOT stored in the burger (that would require changing it), but as `Set<UUID>` in `Daten`.
6. `Bestellung` and `Daten` are records with copied collections too.
7. Tests prove: changing a returned list throws `UnsupportedOperationException`, `mit...` / `ohne...` leave the original unchanged, the new burger has a new id and the correct `basisId`.

Validation (throws `ValidationException` with a German message):
- Brot and Patty must not be null (Kaese has `KEIN`, Sauce has `KEINE`)
- Name: 1 to 40 characters, not blank
- At most 5 Gemuese and 5 Extras, no duplicates

## 6. Persistence

- File: `<user.home>/.burger-konfigurator/daten.json`, content is the `Daten` record (Jackson writes records directly).
- Write to a temp file first, then `Files.move(..., REPLACE_EXISTING)`, so the file is never half written.
- File missing: start with the predefined burgers.
- File corrupt: rename it to `daten.json.defekt`, start with the predefined burgers, the GUI shows an error message.
- IO errors are wrapped in `PersistenceException`.

## 7. GUI

`TabPane` with three tabs:
1. **Konfigurator**: ComboBoxes for Brot, Patty, Kaese, Sauce; CheckBoxes for Gemuese and Extras; name field; live price and kcal; button "Burger erstellen"; error messages in red under the form.
2. **Meine Burger**: `TableView` (Name, Zutaten, Preis, kcal, Favorit, abgeleitet von); buttons "Als Vorlage bearbeiten", "Favorit an/aus", "Loeschen" (with confirmation), "Bestellen"; CheckBox "nur Favoriten".
3. **Bestellhistorie**: orders with date, burgers and total price, newest first.

Errors are shown in `Alert` dialogs, never as stack traces.

## 8. Workflow for Claude Code

1. Read `PLAN.md`. Work only on the task the user names.
2. Run `git pull` first. You may use read-only git commands (`status`, `diff`, `log`, `pull`). **Never run `git add`, `git commit` or `git push`.** The students test and commit themselves.
3. Check that all dependencies of the task are ticked in `PLAN.md` and present in the code. If not, stop and say what is missing.
4. Briefly say what you will create, then implement it in the simple style of section 2.
5. Write unit tests (for model, service, persistence; not for controllers). Run `mvnw.cmd -q clean test` on Windows or `./mvnw -q clean test` otherwise, and fix all failures.
6. Never claim something was tested if it was not. You cannot see the GUI, so say what the student must check.
7. Write the explanation file `docs/erklaerungen/T<nr>-<short-name>.md` in German (see section 9).
8. Tick the task in `PLAN.md`.
9. End with:
   - short summary
   - **How to test**: test command, start command, what to click and what should happen
   - **Files to commit**
   - **Suggested commit message** (format `feat: ...`, `fix: ...`, `test: ...`, `docs: ...`)

## 9. Explanation file per task

Every task gets `docs/erklaerungen/T<nr>-<short-name>.md`, written in German for a 3rd-year HTL student, committed together with the code. Content:
1. **Was wurde gemacht** (2 to 4 sentences)
2. **Neue und geaenderte Dateien**: one line per file, what it is for
3. **Code erklaert**: the important parts with short code snippets and explanation in plain words; explain every lambda, method reference, record and stream used
4. **Bezug zum Immutable Pattern** (if relevant)
5. **So testest du es**
6. **Moegliche Lehrerfragen** with short answers (3 to 5)
