package codeminer;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SecondaryController {

    @FXML
    private VBox vBox; // root

    @FXML
    private MenuBar menuBar;

    @FXML
    private Button button1;

    @FXML
    private Button button2; 
    
    @FXML
    private Button button3;


    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    public void initialize() {
        PrimaryController.makeStageDraggable(vBox, null);
        PrimaryController.makeStageDraggable(menuBar, null);
        initializeMenuButtons(vBox);
    }

    public void initializeMenuButtons(Node root) {
        String previousStyle = "-fx-background-color: #222425";

        button1.setOnMouseEntered(event -> {
            button1.setStyle("-fx-background-color: #303132");
        });
        button1.setOnMouseClicked(event -> {
            Stage stage = (Stage)root.getScene().getWindow();
            stage.setIconified(true);
        });
        button1.setOnMouseExited(event -> {
            button1.setStyle(previousStyle);
        });

        button2.setOnMouseEntered(event -> {
            button2.setStyle("-fx-background-color: #303132");
        });
        button2.setOnMouseClicked(event -> {
            Stage stage = (Stage)root.getScene().getWindow();
            if (stage.isMaximized()) { 
                stage.setMaximized(false); 
            } else { 
                stage.setMaximized(true);
            }
        });
        button2.setOnMouseExited(event -> {
            button2.setStyle(previousStyle);
        });

        button3.setOnMouseEntered(event -> {
            button3.setStyle("-fx-background-color: #E53935");
        });
        button3.setOnMouseClicked(event -> {
            Stage stage = (Stage)root.getScene().getWindow();
            stage.close();
        });
        button3.setOnMouseExited(event -> {
            button3.setStyle(previousStyle);
        });
    }
}