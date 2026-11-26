package at.htl.burgerkonfigurator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Startklasse der Anwendung. Laedt main.fxml und oeffnet das Hauptfenster mit den drei Tabs.
 */
public class App extends Application {

    /** Titel des Hauptfensters. */
    public static final String TITEL = "Burger-Konfigurator";
    /** Breite des Hauptfensters in Pixel. */
    public static final int BREITE = 1000;
    /** Hoehe des Hauptfensters in Pixel. */
    public static final int HOEHE = 700;
    /** Pfad zur Haupt-FXML, relativ zum Paket dieser Klasse. */
    public static final String MAIN_FXML = "view/main.fxml";

    /**
     * Baut das Fenster aus main.fxml auf und zeigt es an.
     *
     * @param stage das Fenster, das JavaFX bereitstellt
     * @throws IOException wenn main.fxml nicht geladen werden kann
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(App.class.getResource(MAIN_FXML));

        stage.setTitle(TITEL);
        stage.setScene(new Scene(root, BREITE, HOEHE));
        stage.show();
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
