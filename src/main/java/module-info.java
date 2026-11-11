/**
 * Modul des Burger-Konfigurators.
 * Legt fest, welche Bibliotheken gebraucht werden und welche Pakete
 * JavaFX (FXML) und Jackson (JSON) von aussen lesen duerfen.
 */
module at.htl.burgerkonfigurator {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    // FXML erzeugt die Controller ueber Reflection
    opens at.htl.burgerkonfigurator.controller to javafx.fxml;
    // Jackson liest und schreibt die Records ueber Reflection
    opens at.htl.burgerkonfigurator.model to com.fasterxml.jackson.databind;

    exports at.htl.burgerkonfigurator;
}
