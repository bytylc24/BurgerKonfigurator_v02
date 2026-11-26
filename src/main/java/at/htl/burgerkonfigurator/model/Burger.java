package at.htl.burgerkonfigurator.model;

import at.htl.burgerkonfigurator.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Ein fertiger Burger. Unveraenderlich (Immutable Pattern): Nach dem Erstellen
 * kann man ihn nie mehr aendern. Jede "Aenderung" liefert einen NEUEN Burger.
 *
 * @param id         eindeutige Nummer des Burgers
 * @param basisId    id des Burgers, von dem dieser abgeleitet wurde (null bei Originalen)
 * @param name       Name des Burgers (1 bis 40 Zeichen)
 * @param brot       Brot, darf nicht null sein
 * @param patty      Patty, darf nicht null sein
 * @param kaese      Kaese (null wird zu {@link Kaese#KEIN})
 * @param sauce      Sauce (null wird zu {@link Sauce#KEINE})
 * @param gemuese    hoechstens 5 Gemuese, keine doppelten
 * @param extras     hoechstens 5 Extras, keine doppelten
 * @param erstelltAm Zeitpunkt der Erstellung
 */
public record Burger(UUID id, UUID basisId, String name, Brot brot, Patty patty, Kaese kaese,
                     Sauce sauce, List<Gemuese> gemuese, List<Extra> extras, LocalDateTime erstelltAm) {

    /** Maximale Laenge des Namens. */
    public static final int MAX_NAME_LAENGE = 40;
    /** Maximale Anzahl an Gemuese und an Extras. */
    public static final int MAX_ANZAHL = 5;

    /**
     * Kompakter Konstruktor: prueft die Daten und kopiert die Listen.
     * Ein ungueltiger Burger kann dadurch nie entstehen.
     */
    public Burger {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (erstelltAm == null) {
            erstelltAm = LocalDateTime.now();
        }
        if (kaese == null) {
            kaese = Kaese.KEIN;
        }
        if (sauce == null) {
            sauce = Sauce.KEINE;
        }
        name = pruefeName(name);
        if (brot == null) {
            throw new ValidationException("Bitte ein Brot auswählen.");
        }
        if (patty == null) {
            throw new ValidationException("Bitte ein Patty auswählen.");
        }
        // Kopie: wer die alte Liste spaeter aendert, aendert den Burger nicht mit
        gemuese = gemuese == null ? List.of() : List.copyOf(gemuese);
        extras = extras == null ? List.of() : List.copyOf(extras);
        pruefeListe(gemuese, "Gemüse");
        pruefeListe(extras, "Extras");
    }

    /** Prueft den Namen und entfernt Leerzeichen am Anfang und Ende. */
    private static String pruefeName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Bitte einen Namen eingeben.");
        }
        String ohneLeerzeichen = name.strip();
        if (ohneLeerzeichen.length() > MAX_NAME_LAENGE) {
            throw new ValidationException("Der Name darf höchstens " + MAX_NAME_LAENGE + " Zeichen haben.");
        }
        return ohneLeerzeichen;
    }

    /** Prueft, dass die Liste nicht zu lang ist und nichts doppelt enthaelt. */
    private static void pruefeListe(List<?> liste, String art) {
        if (liste.size() > MAX_ANZAHL) {
            throw new ValidationException("Höchstens " + MAX_ANZAHL + " " + art + " erlaubt.");
        }
        if (liste.stream().distinct().count() != liste.size()) {
            throw new ValidationException(art + " darf nicht doppelt vorkommen.");
        }
    }

    /**
     * Liefert einen neuen Burger mit zusaetzlichem Gemuese. Dieser Burger bleibt gleich.
     *
     * @param g das neue Gemuese
     * @return neuer, abgeleiteter Burger
     */
    public Burger mitGemuese(Gemuese g) {
        List<Gemuese> neu = new ArrayList<>(gemuese);
        neu.add(g);
        return abgeleitet(name, neu, extras);
    }

    /**
     * Liefert einen neuen Burger ohne das angegebene Gemuese. Dieser Burger bleibt gleich.
     *
     * @param g das Gemuese, das weg soll
     * @return neuer, abgeleiteter Burger
     */
    public Burger ohneGemuese(Gemuese g) {
        return abgeleitet(name, gemuese.stream().filter(x -> x != g).toList(), extras);
    }

    /**
     * Liefert einen neuen Burger mit zusaetzlichem Extra. Dieser Burger bleibt gleich.
     *
     * @param e das neue Extra
     * @return neuer, abgeleiteter Burger
     */
    public Burger mitExtra(Extra e) {
        List<Extra> neu = new ArrayList<>(extras);
        neu.add(e);
        return abgeleitet(name, gemuese, neu);
    }

    /**
     * Liefert einen neuen Burger ohne das angegebene Extra. Dieser Burger bleibt gleich.
     *
     * @param e das Extra, das weg soll
     * @return neuer, abgeleiteter Burger
     */
    public Burger ohneExtra(Extra e) {
        return abgeleitet(name, gemuese, extras.stream().filter(x -> x != e).toList());
    }

    /**
     * Liefert einen neuen Burger mit anderem Namen. Dieser Burger bleibt gleich.
     *
     * @param n der neue Name
     * @return neuer, abgeleiteter Burger
     */
    public Burger mitName(String n) {
        return abgeleitet(n, gemuese, extras);
    }

    /** Erstellt den neuen Burger: neue id, basisId = id von diesem Burger. */
    private Burger abgeleitet(String neuerName, List<Gemuese> neuesGemuese, List<Extra> neueExtras) {
        return new Burger(UUID.randomUUID(), id, neuerName, brot, patty, kaese, sauce,
                neuesGemuese, neueExtras, LocalDateTime.now());
    }

    /**
     * Liefert alle Zutaten des Burgers in einer Liste.
     *
     * @return unveraenderliche Liste aller Zutaten
     */
    public List<Zutat> zutaten() {
        List<Zutat> alle = new ArrayList<>(List.of(brot, patty, kaese, sauce));
        alle.addAll(gemuese);
        alle.addAll(extras);
        return List.copyOf(alle);
    }

    /**
     * Berechnet den Preis aller Zutaten.
     *
     * @return Gesamtpreis in Euro
     */
    public BigDecimal preis() {
        return zutaten().stream()
                .map(Zutat::preis)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Berechnet die Kalorien aller Zutaten.
     *
     * @return Gesamtkalorien in kcal
     */
    public int kalorien() {
        return zutaten().stream()
                .mapToInt(Zutat::kalorien)
                .sum();
    }
}
