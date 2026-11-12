# Git-Anleitung

Der tägliche Ablauf (wer auf welchem Branch arbeitet und welche Befehle man braucht) steht kurz in **`PLAN.md`** unter *Git-Anleitung (kurz)*. Hier stehen die Einrichtung, die Knöpfe in IntelliJ und Hilfe bei typischen Fehlern.

## Grundidee

```
main    ──●────●─────────●────●──────●──     Yll arbeitet hier
            \             \   ↑        ↑
fabian       ●──●──●───────●──●───●────●──   Fabian arbeitet hier
                     ↑ Pull Request    ↑ Pull Request
```

- `main` = Ylls Branch und die offizielle Version
- `fabian` = Fabians Branch, er bleibt immer bestehen
- Fabian holt sich Ylls Stand mit `git pull origin main`
- Fabian bringt seine Arbeit über einen **Pull Request** auf `main`

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
git remote -v      # hier muss der GitHub-Link stehen
```

Login bei GitHub: Beim ersten `git push` öffnet sich unter Windows ein Browser-Fenster. Am Mac braucht man statt dem Passwort einen **Personal Access Token** (GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic) → Haken bei `repo`).

---

## Das Gleiche in IntelliJ

| Was | Wo |
|---|---|
| Branch wechseln | unten rechts (oder oben links) auf den Branch-Namen klicken → Branch wählen → **Checkout** |
| Pull | **Git → Pull...** (Strg+T), für Fabian dort `origin/main` auswählen |
| Commit | **Git → Commit...** (Strg+K), Dateien anhaken, Nachricht eingeben |
| Push | **Git → Push...** (Strg+Shift+K) |
| GitHub-Login | **Settings → Version Control → GitHub → Add account** |

Den Pull Request macht man auf der GitHub-Webseite.

---

## Häufige Probleme

**`not a git repository`**
Man ist im falschen Ordner. Mit `cd BurgerKonfigurator_v02` in den Projektordner wechseln.

**`git pull` sagt `Already up to date`, aber Fabians Änderungen fehlen**
Sein Pull Request ist noch nicht gemerged. Auf GitHub unter **Pull requests** nachschauen.

**Push wird abgelehnt: `Permission denied` oder `403`**
Die Einladung wurde nicht angenommen, oder man ist mit einem falschen Konto angemeldet. Auf GitHub unter **Settings → Collaborators** prüfen.

**Push wird abgelehnt: `rejected ... fetch first`**
Auf GitHub gibt es neuere Commits. Lösung:
```bash
git pull
git push
```

**Fabian hat aus Versehen auf `main` gearbeitet (noch nicht gepusht)**
```bash
git stash              # Änderungen kurz beiseite legen
git checkout fabian
git stash pop          # Änderungen wieder herholen
```
Danach wie gewohnt committen und pushen.

**Konflikt beim Pull oder Merge**
Beide haben dieselbe Stelle in derselben Datei geändert. IntelliJ zeigt die Datei rot an: Datei öffnen → **Resolve** → die richtige Version wählen → `git add .`, `git commit`, `git push`.
