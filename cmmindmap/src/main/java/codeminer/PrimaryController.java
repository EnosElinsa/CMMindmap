package codeminer;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
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
    private Button recentFileButton1;
    @FXML
    private Button recentFileButton2;
    @FXML
    private Button recentFileButton3;
    @FXML
    private Button recentFileButton4;
    @FXML
    private Button recentFileButton5;
    @FXML
    private Button recentFileButton6;
    @FXML
    private Button recentFileButton7;
    @FXML
    private Button recentFileButton8;
    @FXML
    private Button recentFileButton9;
    @FXML
    private Button recentFileButton10;
    @FXML
    private Button recentFileButton11;

    @FXML
    private Label recentFileName1;
    @FXML
    private Label recentFileName2;
    @FXML
    private Label recentFileName3;
    @FXML
    private Label recentFileName4;
    @FXML
    private Label recentFileName5;
    @FXML
    private Label recentFileName6;
    @FXML
    private Label recentFileName7;
    @FXML
    private Label recentFileName8;
    @FXML
    private Label recentFileName9;
    @FXML
    private Label recentFileName10;
    @FXML
    private Label recentFileName11;

    @FXML
    private ImageView recentFileButtonImage1;
    @FXML
    private ImageView recentFileButtonImage2;
    @FXML
    private ImageView recentFileButtonImage3;
    @FXML
    private ImageView recentFileButtonImage4;
    @FXML
    private ImageView recentFileButtonImage5;
    @FXML
    private ImageView recentFileButtonImage6;
    @FXML
    private ImageView recentFileButtonImage7;
    @FXML
    private ImageView recentFileButtonImage8;
    @FXML
    private ImageView recentFileButtonImage9;
    @FXML
    private ImageView recentFileButtonImage10;
    @FXML
    private ImageView recentFileButtonImage11;

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
                FileManager.operatingFileChooser1();
                FileManager.openLoadOperatingFile();
                switchToSecondary();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("未选择");
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
        /*以下三个数组分别存放11个最近文件按钮、名称、按钮缩略图*/
        Button[] recentFileButton = new Button[]{
                recentFileButton1,
                recentFileButton2,
                recentFileButton3,
                recentFileButton4,
                recentFileButton5,
                recentFileButton6,
                recentFileButton7,
                recentFileButton8,
                recentFileButton9,
                recentFileButton10,
                recentFileButton11};
        Label[] recentFileName = new Label[]{
                recentFileName1,
                recentFileName2,
                recentFileName3,
                recentFileName4,
                recentFileName5,
                recentFileName6,
                recentFileName7,
                recentFileName8,
                recentFileName9,
                recentFileName10,
                recentFileName11};
        ImageView[] recentFileButtonImage = new ImageView[]{
                recentFileButtonImage1,
                recentFileButtonImage2,
                recentFileButtonImage3,
                recentFileButtonImage4,
                recentFileButtonImage5,
                recentFileButtonImage6,
                recentFileButtonImage7,
                recentFileButtonImage8,
                recentFileButtonImage9,
                recentFileButtonImage10,
                recentFileButtonImage11
        };

        /*从本地加载最近文件队列*/
        FileManager.loadRecentFileQueue();
        int recentFileQueueLength = FileManager.recentFileQueue.size();
        File[] loadFileArray = FileManager.recentFileQueue.toArray(new File[recentFileQueueLength]);
        Image[] loadFileImageArray = FileManager.recentFileImageQueue.toArray(new Image[recentFileQueueLength]);

        /*将最近文件按钮绑定文件并设置点击事件*/
        for (int i = 0; i < recentFileQueueLength && i < 11; i++) {
            recentFileButtonImage[i].setImage(loadFileImageArray[i]);
            recentFileButton[i].setOnMouseEntered(event -> {
            });
            int finalI = i;
            recentFileButton[i].setOnMouseClicked(event -> {
                try {
                    FileManager.operatingFile = loadFileArray[finalI];
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

        /*将未使用的组件设置为不可用不可见*/
        for (int i = recentFileQueueLength; i < 11; i++) {
            recentFileButton[i].setVisible(false);
            recentFileButton[i].setDisable(true);
            recentFileName[i].setVisible(false);
            recentFileName[i].setDisable(false);
            recentFileButtonImage[i].setVisible(false);
            recentFileButtonImage[i].setDisable(false);
        }
    }
}
