package codeminer;

import codeminer.core.MNode;

import java.util.Optional;
import java.io.IOException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SecondaryController {

    /** 一个节点的预设高度 */
    public static final double PREF_HEIGHT    = 36.0;
    /** 一个节点的预设宽度 */
    public static final double PREF_WIDTH     = 81.0;
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
    private TreeView<String> treeView;

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private MenuButton menuButton;

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
    private Button leftLayoutButton;

    @FXML
    private Button rightLayoutButton;

    @FXML
    private Button autoLayoutButton;

    @FXML
    private Button hideDescendantButton;

    @FXML
    private Label leftStatusLabel;

    @FXML
    private MenuButton zoomMenu;

    /**
     * 菜单栏选项
     */
    @FXML
    private MenuItem newMenuItem;
    @FXML
    private MenuItem openMenuItem;
    @FXML
    private MenuItem undoMenuItem;
    @FXML
    private MenuItem redoMenuItem;
    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private MenuItem saveAsMenuItem;
    @FXML
    private MenuItem exportAsMenuItem;
    @FXML
    private MenuItem exitMenuItem;

    /**
     * 被选中的节点
     */
    private static MNode selectedNode;

    /**
     * 根节点
     */
    private static MNode rootNode;

    /**
     * 是否有节点被选择
     */
    private static BooleanProperty hasNodeBeenSelected = new SimpleBooleanProperty(true);

    private static boolean isModified = false;
    private static boolean isSaved = true;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    public void initialize() {
        PrimaryController.makeStageDraggable(vBox, null);
        initializeButtons(vBox);
        try {
            initializeMenuButton();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initializeRootNode();
    }

    public void initializeRootNode() {
        if (FileManager.getInstance().getOperatingFile() == null) {
            rootNode = new MNode("Topic", true);
            rootNode.setLayoutX(anchorPane.getPrefWidth() / 2 - MNode.PREF_WIDTH / 2);
            rootNode.setLayoutY(anchorPane.getPrefHeight() / 2 - MNode.PREF_HEIGHT / 2);
            MNode.setAnchorPane(anchorPane);
            anchorPane.getChildren().add(rootNode);
        } else {
            MNode.setRootNode(rootNode);
            MNode.setAnchorPane(anchorPane);
            rootNode.reload();
            isModified = false;
            isSaved = true;
        }

        if (treeView.getRoot() != null) {
            treeView.getRoot().getChildren().clear();
        }
        treeView.setRoot(rootNode.getTreeItem());
        treeView.getRoot().setExpanded(true);
        selectedNode = rootNode;
    }

    public void initializeButtons(Node root) {
        String previousStyle = "-fx-background-color: #222425";
        String mouseEnteredStyle = "-fx-background-color:#303132";

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

            MNode newNode = new MNode("Subtopic", false);
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
            leftStatusLabel.setText("Descendant node added");
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
            MNode newNode = new MNode("Subtopic", false);
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
            leftStatusLabel.setText("Sibling node added");
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
            leftStatusLabel.setText("Node removed");
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


        hideDescendantButton.setOnMouseEntered(event -> {
            hideDescendantButton.setStyle(mouseEnteredStyle);
        });
        hideDescendantButton.setOnMouseClicked(event -> {
            hideDescendantButton.setStyle(previousStyle);
            if (selectedNode==null) {
                leftStatusLabel.setText("The root  cannot be hidden.");
                return;
            }
            else if(selectedNode.getTreeItem().isExpanded()==true){
                selectedNode.getTreeItem().setExpanded(false);
                selectedNode.hideNode(selectedNode);
                selectedNode.isSelected().set(true);
                rootNode.updatehidenSize(rootNode);

            }else {
                selectedNode.getTreeItem().setExpanded(true);
                selectedNode.expandNode(selectedNode);
                selectedNode.isSelected().set(true);
                rootNode.updateexpandSize(rootNode);
            }

        });
        hideDescendantButton.setOnMouseExited(event -> {
            hideDescendantButton.setStyle(previousStyle);
        });



        button1.setOnMouseEntered(event -> {
            button1.setStyle(mouseEnteredStyle);
        });
        button1.setOnMouseClicked(event -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setIconified(true);
        });
        button1.setOnMouseExited(event -> {
            button1.setStyle(previousStyle);
        });


        button2.setOnMouseEntered(event -> {
            button2.setStyle(mouseEnteredStyle);
        });
        button2.setOnMouseClicked(event -> {
            Stage stage = (Stage) root.getScene().getWindow();
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
            if (isModified && !isSaved) {
                popSaveAlert(root);
            } else {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            }
        });
        button3.setOnMouseExited(event -> {
            button3.setStyle(previousStyle);
        });


        for (MenuItem item : zoomMenu.getItems()) {
            item.setOnAction(event -> {
                double scale = Double.parseDouble(item.getText().replace("%", "")) / 100;
                anchorPane.setScaleX(scale);
                anchorPane.setScaleY(scale);
                zoomMenu.setText(item.getText());
                scrollPane2.setHvalue(0.5);
                scrollPane2.setVvalue(0.5);
            });
        }

        anchorPane.setOnScroll((ScrollEvent event) -> {
            double zoomFactor = 1.1;
            if (event.isControlDown()) { // ctrl key is pressed
                if (event.getDeltaY() < 0) { // scroll down
                    zoomFactor = 0.95;
                }
                double scale = anchorPane.getScaleX() * zoomFactor;
                anchorPane.setScaleX(scale);
                anchorPane.setScaleY(scale);
                zoomMenu.setText(Integer.toString((int) (scale * 100)) + "%");
                event.consume();
            }
        });
    }


    /**
     * 初始化菜单栏
     */
    private void initializeMenuButton() throws IOException {
        newMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        newMenuItem.setOnAction(event -> {
            if (isModified && !isSaved) {
                FileManager.getInstance().saveOperatingFile(anchorPane.snapshot(new SnapshotParameters(), null));
                isSaved = true;
                isModified = false;
            }
            FileManager.getInstance().newLoadOperatingFile();
            MNode.getRightSubtree().clear();
            MNode.getLeftSubtree().clear();
            anchorPane.getChildren().clear();
            rootNode = new MNode("Topic", true);
            rootNode.setLayoutX(anchorPane.getPrefWidth() / 2 - MNode.PREF_WIDTH / 2);
            rootNode.setLayoutY(anchorPane.getPrefHeight() / 2 - MNode.PREF_HEIGHT / 2);
            anchorPane.getChildren().add(rootNode);
            treeView.setRoot(rootNode.getTreeItem());
            selectedNode = rootNode;
            System.out.println("newMenuItem clicked");
        });

        openMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openMenuItem.setOnAction(event -> {
            if (isModified && !isSaved) {
                FileManager.getInstance().saveOperatingFile(anchorPane.snapshot(new SnapshotParameters(), null));
                isSaved = true;
                isModified = false;
            }
            FileManager.getInstance().operatingFileChooser1();
            FileManager.getInstance().openLoadOperatingFile();
            rootNode.reload();
            treeView.getRoot().getChildren().clear();
            treeView.setRoot(rootNode.getTreeItem());
            treeView.getRoot().setExpanded(true);
            System.out.println("openMenuItem clicked");
        });

        undoMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        undoMenuItem.setOnAction(event -> {
            System.out.println("undoMenuItem clicked");
        });

        redoMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN));
        redoMenuItem.setOnAction(event -> {
            System.out.println("redoMenuItem clicked");
        });

        saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveMenuItem.setOnAction(event -> {
            System.out.println("saveMenuItem clicked");
            isSaved = true;
            FileManager.getInstance().saveOperatingFile(anchorPane.snapshot(new SnapshotParameters(), null));
        });

        saveAsMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN, KeyCombination.CONTROL_DOWN));
        saveAsMenuItem.setOnAction(event -> {
            System.out.println("saveAsMenuItem clicked");
            isSaved = true;
            FileManager.getInstance().saveAsOperatingFile(anchorPane.snapshot(new SnapshotParameters(), null));
        });

        exportAsMenuItem.setOnAction(event -> {
            WritableImage image = anchorPane.snapshot(new SnapshotParameters(), null);
            FileManager.getInstance().outPutFileChooser();
            FileManager.getInstance().saveOutputFile(image);
            System.out.println("exportAsMenuItem clicked");
        });

        exitMenuItem.setOnAction(event -> {
            if (isModified && !isSaved) {
                popSaveAlert(vBox);
            } else {
                Stage stage = (Stage) rootNode.getScene().getWindow();
                stage.close();
            }
            System.out.println("exitMenuItem clicked");
        });
    }

    private void popSaveAlert(Node root) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Inquiry");
        alert.setHeaderText("Do you want to save the changes you made in the mindmap?");
        alert.setContentText("Your changes will be lost if you don't save them.");

        ButtonType buttonType1 = new ButtonType("Save");
        ButtonType buttonType2 = new ButtonType("Do Not Save");
        ButtonType buttonType3 = new ButtonType("Cancel");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonType1, buttonType2, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonType1) {
            if (FileManager.getInstance().saveOperatingFile(anchorPane.snapshot(new SnapshotParameters(), null))) {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            }
        } else if (result.get() == buttonType2) {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        } else if (result.get() == buttonType3) {

        }
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

    public static boolean isModified() {
        return isModified;
    }

    public static void setModified(boolean isModified) {
        SecondaryController.isModified = isModified;
    }

    public static boolean isSaved() {
        return isSaved;
    }

    public static void setSaved(boolean isSaved) {
        SecondaryController.isSaved = isSaved;
    }

}