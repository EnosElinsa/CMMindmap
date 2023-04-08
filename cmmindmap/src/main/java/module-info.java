module codeminer {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;
    opens codeminer to javafx.fxml;
    exports codeminer;
    exports codeminer.core;
}
