# Git-Anleitung: Arbeiten mit Branches und Pull Requests

Diese Anleitung beschreibt, wie wir beide gleichzeitig am Burger-Konfigurator arbeiten, ohne uns gegenseitig etwas kaputt zu machen.

## Grundidee

```
main  ──●───────────────●────────────●──   (immer funktionierend)
         \             /  \          /
t3-exceptions ●──●──●─┘    \        /
                            t5-burger ●──●─┘
```

- `main` ist die offizielle Version. Sie muss **immer** kompilieren und alle Tests bestehen.
- Jede Aufgabe aus `PLAN.md` bekommt einen **eigenen Branch**, z. B. `t3-exceptions` oder `t5-burger`.
- Auf `main` kommt Code nur über einen **Pull Request**, den der andere anschaut und merged.
- **Niemand pusht direkt auf `main`.**

Branch-Namen: `t<nr>-<kurzname>`, klein geschrieben, ohne Leerzeichen und Umlaute.
Beispiele: `t3-exceptions`, `t4-gui-layout`, `t5-burger`, `t7-persistenz`.

---

## Einmalig einrichten (jeder auf seinem Computer)

```bash
git config --global user.name "DeinGitHubName"
git config --global user.email "deine-email@beispiel.at"
```

Projekt holen (nur beim ersten Mal):
```bash
git clone https://github.com/bytylc24/BurgerKonfigurator_v02.git
cd BurgerKonfigurator_v02
```

Prüfen, ob man im richtigen Ordner ist:
```bash
git remote -v
```
Hier muss der GitHub-Link stehen. Wenn `not a git repository` kommt, ist man im falschen Ordner (`cd BurgerKonfigurator_v02`).

Login bei GitHub: Beim ersten `git push` öffnet sich unter Windows ein Browser-Fenster zum Anmelden. Am Mac braucht man statt dem Passwort einen **Personal Access Token** (GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic) → Haken bei `repo`).

---

## Ablauf für jede Aufgabe

### 1. Aktuellen Stand holen und Branch erstellen
```bash
git checkout main
git pull
git checkout -b t3-exceptions
```

### 2. Arbeiten und testen
```bash
mvnw.cmd -q clean test      # Windows
./mvnw -q clean test        # Mac / Linux
```
Erst weitermachen, wenn keine Fehler kommen.

### 3. Committen (so oft man will)
```bash
git status
git add .
git commit -m "feat: T3 Exceptions"
```
Commit-Nachrichten: `feat: ...` (neue Funktion), `fix: ...` (Fehler behoben), `test: ...` (Tests), `docs: ...` (Dokumentation).

### 4. Branch auf GitHub hochladen
```bash
git push -u origin t3-exceptions
```
`-u` braucht man nur beim ersten Push dieses Branches, danach reicht `git push`.

### 5. Pull Request erstellen
1. https://github.com/bytylc24/BurgerKonfigurator_v02 öffnen.
2. Oben erscheint eine gelbe Leiste: **Compare & pull request** anklicken.
3. Prüfen: *base:* `main` ← *compare:* `t3-exceptions`.
4. Titel z. B. `T3 Exceptions`, kurz beschreiben, was gemacht wurde.
5. **Create pull request**.

### 6. Der andere prüft und merged
1. Pull Request öffnen → Reiter **Files changed** anschauen.
2. Am besten den Branch auch lokal testen:
   ```bash
   git fetch
   git checkout t3-exceptions
   mvnw.cmd -q clean test
   ```
3. Passt alles: **Merge pull request** → **Confirm merge** → **Delete branch**.
4. Passt etwas nicht: im Pull Request einen Kommentar schreiben. Der Autor ändert es auf demselben Branch und macht `git push`. Der Pull Request aktualisiert sich automatisch.

### 7. Beide holen sich den neuen `main`
```bash
git checkout main
git pull
```
Den alten lokalen Branch kann man löschen:
```bash
git branch -d t3-exceptions
```
Dann geht es bei Schritt 1 mit der nächsten Aufgabe weiter.

---

## Das Gleiche in IntelliJ

| Was | Wo |
|---|---|
| Neuer Branch | unten rechts (oder oben links) auf den Branch-Namen klicken → **New Branch...** |
| Branch wechseln | auf den Branch-Namen klicken → Branch auswählen → **Checkout** |
| Pull | **Git → Pull...** (Strg+T) |
| Commit | **Git → Commit...** (Strg+K), Dateien anhaken, Nachricht eingeben |
| Push | **Git → Push...** (Strg+Shift+K) |
| GitHub-Login | **Settings → Version Control → GitHub → Add account** |

Den Pull Request erstellt und merged man am einfachsten auf der GitHub-Webseite.

---

## Häufige Probleme

**`not a git repository`**
Man ist im falschen Ordner. Mit `cd BurgerKonfigurator_v02` in den Projektordner wechseln.

**`git pull` sagt `Already up to date`, aber die Änderungen des anderen fehlen**
Er hat noch nicht gepusht, oder sein Pull Request ist noch nicht gemerged. Auf GitHub unter **Pull requests** nachschauen.

**Push wird abgelehnt: `Permission denied` oder `403`**
Die Einladung ins Repository wurde nicht angenommen, oder man ist mit einem falschen Konto angemeldet. Auf GitHub unter **Settings → Collaborators** prüfen, ob beide eingetragen sind (nicht mehr *Pending*).

**Push wird abgelehnt: `rejected ... fetch first`**
Auf dem Branch auf GitHub gibt es neuere Commits. Lösung:
```bash
git pull
git push
```

**Aus Versehen auf `main` gearbeitet (noch nicht gepusht)**
Einfach jetzt einen Branch erstellen, die Commits und Änderungen kommen mit:
```bash
git checkout -b t3-exceptions
git push -u origin t3-exceptions
```
Danach den lokalen `main` wieder auf den Stand von GitHub setzen:
```bash
git checkout main
git reset --hard origin/main
```
(Achtung: `reset --hard` nur machen, wenn die Arbeit vorher auf dem Branch gepusht wurde.)

**Konflikt beim Pull oder Merge**
Beide haben dieselbe Stelle in derselben Datei geändert. IntelliJ zeigt die Datei rot an: Datei öffnen → **Resolve** → die richtige Version auswählen → danach `git add .`, `git commit` und `git push`. Durch die Aufteilung in `PLAN.md` sollte das selten passieren.
