package at.htl.burgerkonfigurator.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Kleiner, veraenderbarer Helfer fuer die GUI. Man setzt die Zutaten nach und nach,
 * am Ende erstellt {@link #build()} den unveraenderlichen {@link Burger}.
 */
public class BurgerBuilder {

    private UUID basisId;
    private String name = "";
    private Brot brot;
    private Patty patty;
    private Kaese kaese = Kaese.KEIN;
    private Sauce sauce = Sauce.KEINE;
    private List<Gemuese> gemuese = new ArrayList<>();
    private List<Extra> extras = new ArrayList<>();

    /**
     * Erstellt einen Builder, der mit den Werten eines bestehenden Burgers gefuellt ist.
     * Der Burger aus {@link #build()} ist dann von diesem Burger abgeleitet.
     *
     * @param vorlage der bestehende Burger
     * @return gefuellter Builder
     */
    public static BurgerBuilder von(Burger vorlage) {
        BurgerBuilder builder = new BurgerBuilder()
                .name(vorlage.name())
                .brot(vorlage.brot())
                .patty(vorlage.patty())
                .kaese(vorlage.kaese())
                .sauce(vorlage.sauce())
                .gemuese(vorlage.gemuese())
                .extras(vorlage.extras());
        builder.basisId = vorlage.id();
        return builder;
    }

    /**
     * Setzt den Namen.
     *
     * @param name der Name
     * @return dieser Builder
     */
    public BurgerBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Setzt das Brot.
     *
     * @param brot das Brot
     * @return dieser Builder
     */
    public BurgerBuilder brot(Brot brot) {
        this.brot = brot;
        return this;
    }

    /**
     * Setzt das Patty.
     *
     * @param patty das Patty
     * @return dieser Builder
     */
    public BurgerBuilder patty(Patty patty) {
        this.patty = patty;
        return this;
    }

    /**
     * Setzt den Kaese.
     *
     * @param kaese der Kaese
     * @return dieser Builder
     */
    public BurgerBuilder kaese(Kaese kaese) {
        this.kaese = kaese;
        return this;
    }

    /**
     * Setzt die Sauce.
     *
     * @param sauce die Sauce
     * @return dieser Builder
     */
    public BurgerBuilder sauce(Sauce sauce) {
        this.sauce = sauce;
        return this;
    }

    /**
     * Setzt das Gemuese (ersetzt die bisherige Auswahl).
     *
     * @param gemuese das ausgewaehlte Gemuese
     * @return dieser Builder
     */
    public BurgerBuilder gemuese(List<Gemuese> gemuese) {
        this.gemuese = new ArrayList<>(gemuese);
        return this;
    }

    /**
     * Setzt die Extras (ersetzt die bisherige Auswahl).
     *
     * @param extras die ausgewaehlten Extras
     * @return dieser Builder
     */
    public BurgerBuilder extras(List<Extra> extras) {
        this.extras = new ArrayList<>(extras);
        return this;
    }

    /**
     * Erstellt den unveraenderlichen Burger mit neuer id.
     *
     * @return der fertige Burger
     * @throws at.htl.burgerkonfigurator.exception.ValidationException wenn Daten ungueltig sind
     */
    public Burger build() {
        return new Burger(UUID.randomUUID(), basisId, name, brot, patty, kaese, sauce,
                gemuese, extras, LocalDateTime.now());
    }
}
