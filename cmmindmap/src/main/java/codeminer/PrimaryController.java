package codeminer;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class PrimaryController {
    @FXML
    private Button exitButton;

    @FXML
    private Button browseButton;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private FlowPane flowPane;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    public void initialize() {
        initializeBrowseButton(anchorPane, browseButton);
        initializeExitButton(anchorPane, exitButton);
        makeStageDraggable(anchorPane, null);
        initializeNodesInFlowPane(flowPane);
    }

    public static void initializeBrowseButton(Node root, Button browseButton) {
        String previousStyle = browseButton.getStyle();
        browseButton.setOnMouseEntered(event -> {
            browseButton.setStyle("-fx-background-color: #00CDCD");
        });
        //保存文件
        browseButton.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open a file");
            //Stage stage = (Stage) root.getScene().getWindow();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("dt file","*.xmind"));
            File openFile = fileChooser.showOpenDialog(new Stage());
        });
        browseButton.setOnMouseExited(event -> {
            browseButton.setStyle(previousStyle);
        });
    }

    public static void initializeExitButton(Node root, Button exitButton) {
        String previousStyle = exitButton.getStyle();
        exitButton.setOnMouseEntered(event -> {
            exitButton.setStyle("-fx-background-color: #E53935");
        });
        exitButton.setOnMouseClicked(event -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        });
        exitButton.setOnMouseExited(event -> {
            exitButton.setStyle(previousStyle);
        });
    }

    /** 窗口拖动 */
    public static void makeStageDraggable(Node root, Alert alert) {
        double[] xOffset = {0}, yOffset = {0};
        root.setOnMousePressed(event -> {
            Stage stage = (Stage) root.getScene().getWindow();
            if (stage != null && alert == null) {
                xOffset[0] = stage.getX() - event.getScreenX();
                yOffset[0] = stage.getY() - event.getScreenY();
            } else if (stage == null && alert != null) {
                xOffset[0] = alert.getX() - event.getScreenX();
                yOffset[0] = alert.getY() - event.getScreenY();
            }
        });

        root.setOnMouseDragged(event -> {
            Stage stage = (Stage) root.getScene().getWindow();
            if (stage != null && alert == null) {
                stage.setX(event.getScreenX() + xOffset[0]);
                stage.setY(event.getScreenY() + yOffset[0]);
            } else if (stage == null && alert != null) {
                alert.setX(event.getScreenX() + xOffset[0]);
                alert.setY(event.getScreenY() + yOffset[0]);
            }
        });
    }

    public void initializeNodesInFlowPane(FlowPane flowPane) {
        for (Node node : flowPane.getChildren()) {
            Node nestedNode1 = ((VBox) node).getChildren().get(0);
            Node nestedNode2 = ((VBox) node).getChildren().get(1);
            String previousStyle1 = nestedNode1.getStyle();
            String previousStyle2 = nestedNode2.getStyle();
            nestedNode1.setOnMouseEntered(event -> {
                nestedNode1.setStyle("-fx-border-width: 6px");
                nestedNode1.setStyle("-fx-border-color: #3e4040");
                nestedNode2.setStyle("-fx-background-color: #3e4040");
            });
            nestedNode1.setOnMouseClicked(event -> {
                nestedNode1.setStyle("-fx-border-color: #215b90");
                nestedNode2.setStyle("-fx-background-color: #215b90");

                // Switch to the main menu
                try {
                    switchToSecondary();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            nestedNode1.setOnMouseExited(event -> {
                nestedNode1.setStyle(previousStyle1);
                nestedNode2.setStyle(previousStyle2);
            });
        }
    }
}
