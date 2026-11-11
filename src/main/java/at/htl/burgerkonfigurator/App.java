package at.htl.burgerkonfigurator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Startklasse der Anwendung. Oeffnet das Hauptfenster mit den drei Tabs.
 */
public class App extends Application {

    /** Titel des Hauptfensters. */
    public static final String TITEL = "Burger-Konfigurator";
    /** Breite des Hauptfensters in Pixel. */
    public static final int BREITE = 1000;
    /** Hoehe des Hauptfensters in Pixel. */
    public static final int HOEHE = 700;

    /**
     * Baut das Fenster auf und zeigt es an.
     *
     * @param stage das Fenster, das JavaFX bereitstellt
     */
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

    /** Erstellt einen leeren Tab, den man nicht schliessen kann. */
    private Tab erstelleTab(String titel) {
        Tab tab = new Tab(titel);
        tab.setClosable(false);
        return tab;
    }

    /**
     * Einstiegspunkt des Programms.
     *
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
