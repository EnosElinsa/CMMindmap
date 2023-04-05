package codeminer;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class PrimaryController {
    @FXML
    private Button exitButton;

    @FXML
    private Button browseButton;

    @FXML
    private Button newButton;

    @FXML
    private Button recentFileButtonEight;

    @FXML
    private Button recentFileButtonEleven;

    @FXML
    private Button recentFileButtonFive;

    @FXML
    private Button recentFileButtonFour;

    @FXML
    private Button recentFileButtonNine;

    @FXML
    private Button recentFileButtonOne;

    @FXML
    private Button recentFileButtonSeven;

    @FXML
    private Button recentFileButtonSix;

    @FXML
    private Button recentFileButtonTen;

    @FXML
    private Button recentFileButtonThree;

    @FXML
    private Button recentFileButtonTwo;

    @FXML
    private Label recentFileNameEight;

    @FXML
    private Label recentFileNameTwo;

    @FXML
    private Label recentFileNameEleven;

    @FXML
    private Label recentFileNameFive;

    @FXML
    private Label recentFileNameFour;

    @FXML
    private Label recentFileNameNine;

    @FXML
    private Label recentFileNameTen;

    @FXML
    private Label recentFileNameOne;

    @FXML
    private Label recentFileNameSeven;

    @FXML
    private Label recentFileNameSix;

    @FXML
    private Label recentFileNameThree;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private FlowPane flowPane;

    @FXML
    private static void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    /**
     * 初始化Primary窗口
     */
    public void initialize() {
        initializeBrowseButton(anchorPane, browseButton);
        initializeExitButton(anchorPane, exitButton);
        makeStageDraggable(anchorPane, null);
        initializeNewButton(flowPane);
        initializeRecentFile();
    }

    /**
     * 初始化加载文件按钮
     */
    public static void initializeBrowseButton(Node root, Button browseButton) {
        String previousStyle = browseButton.getStyle();
        browseButton.setOnMouseEntered(event -> {
            browseButton.setStyle("-fx-background-color: #00CDCD");
        });
        browseButton.setOnMouseClicked(event -> {
            try {
                FileManager.operatingFileChooser();
                FileManager.openLoadOperatingFile();
                switchToSecondary();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        browseButton.setOnMouseExited(event -> {
            browseButton.setStyle(previousStyle);
        });
    }

    /**
     * 初始化退出按钮
     */
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

    /**
     * 窗口拖动
     */
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

    /**
     * 初始化新建文件按钮
     */
    public void initializeNewButton(FlowPane flowPane) {
        newButton.setOnMouseEntered(event -> {
        });
        newButton.setOnMouseClicked(event -> {
            try {
                FileManager.newLoadOperatingFile();
                switchToSecondary();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        newButton.setOnMouseExited(event -> {
        });
    }

    /**
     * 初始化11个最近文件按钮
     */
    private void initializeRecentFile() {
        /*两个数组分别存放11个最近文件按钮和名称*/
        Button[] recentFileButton = new Button[]{recentFileButtonOne, recentFileButtonTwo, recentFileButtonThree, recentFileButtonFour, recentFileButtonFive, recentFileButtonSix, recentFileButtonSeven, recentFileButtonEight, recentFileButtonNine, recentFileButtonTen, recentFileButtonEleven};
        Label[] recentFileName = new Label[]{recentFileNameOne, recentFileNameTwo, recentFileNameThree, recentFileNameFour, recentFileNameFive, recentFileNameSix, recentFileNameSeven, recentFileNameEight, recentFileNameNine, recentFileNameTen, recentFileNameEleven};
        /*从本地加载最近文件队列*/
        FileManager.loadFileQueue();
        int recentFileQueueLength = FileManager.recentFileQueue.size();
        File[] loadFileArray = FileManager.recentFileQueue.toArray(new File[recentFileQueueLength]);
        /*将最近文件按钮绑定文件并设置点击事件*/
        for (int i = 0; i < recentFileQueueLength&&i<11; i++) {
            recentFileButton[i].setOnMouseEntered(event -> {
            });
            int finalI = i;
            recentFileButton[i].setOnMouseClicked(event -> {




                try {
                    FileManager.operatingFile=loadFileArray[finalI];
                    FileManager.openLoadOperatingFile();
                    switchToSecondary();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            recentFileButton[i].setOnMouseExited(event -> {
            });
            recentFileName[i].setText(loadFileArray[finalI].getName());
        }
        for (int i = recentFileQueueLength; i < 11; i++) {
            recentFileButton[i].setVisible(false);
            recentFileButton[i].setDisable(true);
            recentFileName[i].setVisible(false);
            recentFileName[i].setDisable(false);
        }
    }
}
