module codeminer {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.fxml;
    opens codeminer to javafx.fxml;
    exports codeminer;
    exports codeminer.core;
}
