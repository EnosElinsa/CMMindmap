package codeminer;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PrimaryController {
    @FXML
    private Button exitButton;
    
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private FlowPane flowPane;
    
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    public void initialize() {
        initializeExitButton(anchorPane, exitButton);
        makeStageDraggable(anchorPane, null);
        initializeNodesInFlowPane(flowPane);
    }

    public void initializeExitButton(Node root, Button exitButton) {
        exitButton.setOnMouseEntered(event -> {
            exitButton.setStyle("-fx-background-color: #E53935");
        });
        exitButton.setOnMouseClicked(event -> {
            Stage stage = (Stage)root.getScene().getWindow();
            stage.close();
        });
        exitButton.setOnMouseExited(event -> {
            exitButton.setStyle("-fx-background-color: #1D1F20");
        });
    }
    
    public void makeStageDraggable(Node root, Alert alert) {
        double[] xOffset = {0}, yOffset = {0};
        root.setOnMousePressed(event -> {
            Stage stage = (Stage)root.getScene().getWindow();
            if (stage != null && alert == null){
                xOffset[0] = stage.getX() - event.getScreenX();
                yOffset[0] = stage.getY() - event.getScreenY();
            } else if(stage == null && alert != null){
                xOffset[0] = alert.getX() - event.getScreenX();
                yOffset[0] = alert.getY() - event.getScreenY();
            }
        });
    
        root.setOnMouseDragged(event -> {
            Stage stage = (Stage)root.getScene().getWindow();
            if (stage != null && alert == null){
                stage.setX(event.getScreenX() + xOffset[0]);
                stage.setY(event.getScreenY() + yOffset[0]);
            } else if(stage == null && alert != null){
                alert.setX(event.getScreenX() + xOffset[0]);
                alert.setY(event.getScreenY() + yOffset[0]);
            }
        });
    }

    public void initializeNodesInFlowPane(FlowPane flowPane) {
        for (Node node : flowPane.getChildren()) {
            Node nestedNode = ((VBox)node).getChildren().get(0);
            String previousStyle = nestedNode.getStyle();
            nestedNode.setOnMouseEntered(event -> {
                nestedNode.setStyle("-fx-border-width: 10px");
                nestedNode.setStyle("-fx-border-color: #3e4040");
                nestedNode.setStyle("-fx-border-style: solid");
                nestedNode.setStyle("-fx-background-color: #343638");
            });
            nestedNode.setOnMouseExited(event -> {
                nestedNode.setStyle(previousStyle);
            });
        }
    }
}
