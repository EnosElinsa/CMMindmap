module codeminer {
    requires javafx.controls;
    requires javafx.fxml;

    opens codeminer to javafx.fxml;
    exports codeminer;
}
