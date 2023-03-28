package codeminer;

import java.io.IOException;

import codeminer.core.MNode;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SecondaryController {

    @FXML
    private VBox vBox; // root

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private HBox menuHBox;

    @FXML
    private ScrollPane scrollPane1;

    @FXML
    private ScrollPane scrollPane2;

    @FXML
    private ScrollPane scrollPane3;

    @FXML
    private TreeView<String> treeView;

    @FXML
    private Button button1;

    @FXML
    private Button button2; 
    
    @FXML
    private Button button3;

    @FXML
    private Button menuButton;

    @FXML
    private Button addDescendantButton;

    @FXML
    private Label addDescendantLabel;

    @FXML
    private Button addSiblingButton;

    @FXML
    private Label addSiblingLabel;

    @FXML
    private Button removeButton;

    @FXML
    private Label removeLabel;

    @FXML
    private Button leftLayoutButton;;

    @FXML
    private Button rightLayoutButton;
    
    @FXML
    private Button autoLayoutButton;

    @FXML
    private Label leftStatusLabel;

    @FXML
    private MenuButton zoomMenu;

    /** 被选中的节点 */
    private static MNode selectedNode;
    /** 根节点 */
    private static MNode rootNode;
    /** 是否有节点被选择 */
    private static BooleanProperty hasNodeBeenSelected = new SimpleBooleanProperty(true);

    private static double currentScale = 1.0;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    public void initialize() {
        PrimaryController.makeStageDraggable(vBox, null);
        initializeButtons(vBox);
        initializeNodes();
    }

    private void initializeNodes() {
        rootNode = new MNode("Topic");
        rootNode.setRootNode(true);
        rootNode.isSelected().set(true);
        rootNode.setLayoutX(anchorPane.getPrefWidth() / 2 - MNode.PREF_WIDTH / 2);
        rootNode.setLayoutY(anchorPane.getPrefHeight() / 2 - MNode.PREF_HEIGHT / 2);
        selectedNode = rootNode;
        MNode.setAnchorPane(anchorPane);
        MNode.setRootNode(rootNode);
        anchorPane.getChildren().add(rootNode);
        treeView.setRoot(rootNode.getTreeItem());
        
        scrollPane2.setContent(anchorPane);
        for (MenuItem item : zoomMenu.getItems()) {
            item.setOnAction(event -> {
                // double scale = Double.parseDouble(item.getText().replace("%", "")) / 100;
                
            });
        }
    }

    public void initializeButtons(Node root) {
        String previousStyle = "-fx-background-color: #222425";
        String mouseEnteredStyle = "-fx-background-color: #303132";

        menuButton.setOnMouseEntered(event -> {
            menuButton.setStyle(mouseEnteredStyle);
        });
        menuButton.setOnMouseClicked(event -> {

            
        });
        menuButton.setOnMouseExited(event -> {
            menuButton.setStyle(previousStyle);
        });

        anchorPane.setOnMouseClicked(event -> {
            if (selectedNode != null) {
                selectedNode.setStyle(MNode.DEFAULT_STYLE);
                selectedNode.isSelected().set(false);
                hasNodeBeenSelected.set(false);
                selectedNode = null;
            }
        });

        hasNodeBeenSelected.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    addDescendantButton.setDisable(false);
                    addSiblingButton.setDisable(false);
                    removeButton.setDisable(false);
                    addDescendantLabel.setDisable(false);
                    addSiblingLabel.setDisable(false);
                    removeLabel.setDisable(false);
                } else {
                    addDescendantButton.setDisable(true);
                    addSiblingButton.setDisable(true);
                    removeButton.setDisable(true);
                    addDescendantLabel.setDisable(true);
                    addSiblingLabel.setDisable(true);
                    removeLabel.setDisable(true);
                }
            }
        }); 

        addDescendantButton.setOnMouseEntered(event -> {
            addDescendantButton.setStyle(mouseEnteredStyle);
        });
        addDescendantButton.setOnMouseClicked(event -> {
            addDescendantButton.setStyle(previousStyle);
            
            MNode newNode = new MNode("Subtopic");
            newNode.setParentNode(selectedNode);
            newNode.isSelected().set(true);
            selectedNode.isSelected().set(false);

            anchorPane.getChildren().add(newNode);
            anchorPane.getChildren().add(newNode.getEdge());
            newNode.getParentNode().getTreeItem().getChildren().add(newNode.getTreeItem());
            if (selectedNode.isRootNode()) {
                if (MNode.getLeftSubtree().size() < MNode.getRightSubtree().size()) {
                    MNode.getLeftSubtree().add(newNode);
                    newNode.setOrientation(MNode.LEFT);
                } else {
                    MNode.getRightSubtree().add(newNode);
                    newNode.setOrientation(MNode.RIGHT);
                }
            } else {
                selectedNode.getChildNodes().add(newNode);
                newNode.setOrientation(selectedNode.getOrientation());
            }
            selectedNode = newNode;
            rootNode.update(rootNode);
        });
        addDescendantButton.setOnMouseExited(event -> {
            addDescendantButton.setStyle(previousStyle);
        });


        addSiblingButton.setOnMouseEntered(event -> {
            addSiblingButton.setStyle(mouseEnteredStyle);
        });
        addSiblingButton.setOnMouseClicked(event -> {
            addSiblingButton.setStyle(previousStyle);

            if (selectedNode == rootNode) {
                leftStatusLabel.setText("Root node cannot add a sibling node");
                return;
            }
            MNode newNode = new MNode("Subtopic");
            newNode.setParentNode(selectedNode.getParentNode());
            anchorPane.getChildren().add(newNode);
            anchorPane.getChildren().add(newNode.getEdge());
            newNode.getParentNode().getTreeItem().getChildren().add(newNode.getTreeItem());
            if (selectedNode.getParentNode().isRootNode()) {
                if (selectedNode.getOrientation() == MNode.LEFT) {
                    MNode.getLeftSubtree().add(newNode);
                    newNode.setOrientation(MNode.LEFT);
                } else {
                    MNode.getRightSubtree().add(newNode);
                    newNode.setOrientation(MNode.RIGHT);
                }
            } else {
                selectedNode.getParentNode().getChildNodes().add(newNode);
                newNode.setOrientation(selectedNode.getOrientation());
            }
            rootNode.update(rootNode);
        });
        addSiblingButton.setOnMouseExited(event -> {
            addSiblingButton.setStyle(previousStyle);
        });


        removeButton.setOnMouseEntered(event -> {
            removeButton.setStyle(mouseEnteredStyle);
        });
        removeButton.setOnMouseClicked(event -> {
            removeButton.setStyle(previousStyle);

            if (selectedNode == rootNode) {
                leftStatusLabel.setText("The root node cannot be removed");
                return;
            }
            selectedNode.deleteNode(selectedNode);
            if (selectedNode.getParentNode().isRootNode()) {
                if (selectedNode.getOrientation() == MNode.LEFT && MNode.getLeftSubtree().size() > 0) {
                    selectedNode = MNode.getLeftSubtree().get(MNode.getLeftSubtree().size() - 1);
                } else if (selectedNode.getOrientation() == MNode.RIGHT && MNode.getRightSubtree().size() > 0) {
                    selectedNode = MNode.getRightSubtree().get(MNode.getRightSubtree().size() - 1);
                } else {
                    selectedNode = rootNode;
                }
            } else {
                if (selectedNode.getParentNode().getChildNodes().size() > 0) {
                    selectedNode = selectedNode.getParentNode().getChildNodes()
                        .get(selectedNode.getParentNode().getChildNodes().size() - 1);
                } else {
                    selectedNode = selectedNode.getParentNode();
                }
            }
            selectedNode.isSelected().set(true);
            rootNode.update(rootNode);
        });
        removeButton.setOnMouseExited(event -> {
            removeButton.setStyle(previousStyle);
        });


        leftLayoutButton.setOnMouseEntered(event -> {
            leftLayoutButton.setStyle(mouseEnteredStyle);
        });
        leftLayoutButton.setOnMouseClicked(event -> {
            leftLayoutButton.setStyle(previousStyle);

            for (MNode childNode : MNode.getRightSubtree()) {
                MNode.getLeftSubtree().add(childNode);
            }
            MNode.getRightSubtree().clear();
            rootNode.update(rootNode);
        });
        leftLayoutButton.setOnMouseExited(event -> {
            leftLayoutButton.setStyle(previousStyle);
        });


        rightLayoutButton.setOnMouseEntered(event -> {
            rightLayoutButton.setStyle(mouseEnteredStyle);
        });
        rightLayoutButton.setOnMouseClicked(event -> {
            rightLayoutButton.setStyle(previousStyle);

            for (MNode childNode : MNode.getLeftSubtree()) {
                MNode.getRightSubtree().add(childNode);
            }
            MNode.getLeftSubtree().clear();
            rootNode.update(rootNode);
        });
        rightLayoutButton.setOnMouseExited(event -> {
            rightLayoutButton.setStyle(previousStyle);
        });


        autoLayoutButton.setOnMouseEntered(event -> {
            autoLayoutButton.setStyle(mouseEnteredStyle);
        });
        autoLayoutButton.setOnMouseClicked(event -> {
            autoLayoutButton.setStyle(previousStyle);

            while (MNode.getLeftSubtree().size() > MNode.getRightSubtree().size()) {
                MNode.getRightSubtree().add(MNode.getLeftSubtree().get(MNode.getLeftSubtree().size() - 1));
                MNode.getLeftSubtree().remove(MNode.getLeftSubtree().size() - 1);
            }
            while (MNode.getLeftSubtree().size() < MNode.getRightSubtree().size()) {
                MNode.getLeftSubtree().add(MNode.getRightSubtree().get(MNode.getRightSubtree().size() - 1));
                MNode.getRightSubtree().remove(MNode.getRightSubtree().size() - 1);
            }
            rootNode.update(rootNode);
        });
        autoLayoutButton.setOnMouseExited(event -> {
            autoLayoutButton.setStyle(previousStyle);
        });

        button1.setOnMouseEntered(event -> {
            button1.setStyle(mouseEnteredStyle);
        });
        button1.setOnMouseClicked(event -> {
            Stage stage = (Stage)root.getScene().getWindow();
            stage.setIconified(true);
        });
        button1.setOnMouseExited(event -> {
            button1.setStyle(previousStyle);
        });


        button2.setOnMouseEntered(event -> {
            button2.setStyle(mouseEnteredStyle);
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

    public static MNode getSelectedNode() {
        return selectedNode;
    }

    public static void setSelectedNode(MNode selectedNode) {
        SecondaryController.selectedNode = selectedNode;
    }

    public static MNode getRootNode() {
        return rootNode;
    }

    public static void setRootNode(MNode rootNode) {
        SecondaryController.rootNode = rootNode;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    public static BooleanProperty hasNodeBeenSelected() {
        return hasNodeBeenSelected;
    }

    public static double getCurrentScale() {
        return currentScale;
    }

    public static void setCurrentScale(double currentScale) {
        SecondaryController.currentScale = currentScale;
    }

    
}