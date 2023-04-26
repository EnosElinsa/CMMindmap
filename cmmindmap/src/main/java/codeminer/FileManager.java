package codeminer;

import codeminer.core.MNode;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FileManager {
    /**
     * 正在操作的文件
     */
    private File operatingFile;

    /**
     * 将要导出图片的文件
     */
    private File outputFile;

    /**
     * 最近文件队列
     */
    private Queue<File> recentFileQueue = new LinkedList<>();

    /**
     * 最近文件缩略图队列
     */
    private Queue<Image> recentFileImageQueue = new LinkedList<>();

    private static FileManager fileManager;

    private FileManager() {}

    public static FileManager getInstance() {
        if (fileManager == null) {
            fileManager = new FileManager();
        }
        return fileManager;
    }

    /**
     * 选择文件（打开）
     */
    public void operatingFileChooser1() {
        /*选择目标文件*/
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("mp file", "*.mp"));
        Stage fileChooserStage = new Stage();
        fileChooserStage.setAlwaysOnTop(true);
        fileChooserStage.initModality(Modality.APPLICATION_MODAL);
        operatingFile = fileChooser.showOpenDialog(fileChooserStage);
    }

    /**
     * 选择文件（保存）
     */
    public void operatingFileChooser2() {
        /*选择目标文件夹*/
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a directory to save the file");
        fileChooser.setInitialFileName(SecondaryController.getRootNode().getText());
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("mp file", "*.mp"));
        Stage fileChooserStage = new Stage();
        fileChooserStage.setAlwaysOnTop(true);
        fileChooserStage.initModality(Modality.APPLICATION_MODAL);
        operatingFile = fileChooser.showSaveDialog(fileChooserStage);
    }

    /**
     * 选择文件（导出）
     */
    public void outPutFileChooser() {
        /*选择目标文件夹*/
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Output a file");
        fileChooser.setInitialFileName(SecondaryController.getRootNode().getText());
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("jpg file", "*.jpg"), new FileChooser.ExtensionFilter("png file", "*.png"));
        Stage fileChooserStage = new Stage();
        fileChooserStage.setAlwaysOnTop(true);
        fileChooserStage.initModality(Modality.APPLICATION_MODAL);
        outputFile = fileChooser.showSaveDialog(fileChooserStage);
    }

    /**
     * 新建文件
     */
    public void newLoadOperatingFile() {
        operatingFile = null;
        loadRecentFileQueue();
    }

    /**
     * 打开文件
     */
    public void openLoadOperatingFile() {
        /*将文件中对象实例化和并读取MNode类信息*/
        if (operatingFile == null) return;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(operatingFile));
            MNode node = (MNode) ois.readObject();
            System.out.println(node);
            SecondaryController.setRootNode(node);
            MNode.setRootNode(node);
            MNode.setLeftSubtreeHeight(ois.readDouble());
            MNode.setLeftSubtreeWidth(ois.readDouble());
            MNode.setRightSubtreeHeight(ois.readDouble());
            MNode.setRightSubtreeWidth(ois.readDouble());
            MNode.setLeftSubtree((ArrayList<MNode>) ois.readObject());
            MNode.setRightSubtree((ArrayList<MNode>) ois.readObject());
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存文件
     */
    public boolean saveOperatingFile(WritableImage image) {
        /*将新建思维导图另存为*/
        if (operatingFile == null) return saveAsOperatingFile(image);
            /*将已有思维导图保存*/
        else {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(operatingFile));
                oos.writeObject(SecondaryController.getRootNode());
                oos.writeDouble(MNode.getLeftSubtreeHeight());
                oos.writeDouble(MNode.getLeftSubtreeWidth());
                oos.writeDouble(MNode.getRightSubtreeHeight());
                oos.writeDouble(MNode.getRightSubtreeWidth());
                oos.writeObject(MNode.getLeftSubtree());
                oos.writeObject(MNode.getRightSubtree());
                oos.flush();
                oos.close();
                saveRecentFileQueue(image);
                System.out.println("save successful");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 另存为文件
     */
    public boolean saveAsOperatingFile(WritableImage image) {
        /*选择目标文件*/
        operatingFileChooser2();
        /*将实例化对象和MNode类信息存入文件*/
        if (operatingFile != null) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(operatingFile));
                oos.writeObject(SecondaryController.getRootNode());
                oos.writeDouble(MNode.getLeftSubtreeHeight());
                oos.writeDouble(MNode.getLeftSubtreeWidth());
                oos.writeDouble(MNode.getRightSubtreeHeight());
                oos.writeDouble(MNode.getRightSubtreeWidth());
                oos.writeObject(MNode.getLeftSubtree());
                oos.writeObject(MNode.getRightSubtree());
                oos.flush();
                oos.close();
                saveRecentFileQueue(image);
                System.out.println("save as successful");
                return true;
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 打开程序时，加载最近文件队列
     *
     * @throws URISyntaxException
     */
    public void loadRecentFileQueue() {
        //System.out.println(Paths.get(App.class.getResource("recentFileQueue").toURI()).toString());
        try {
            FileReader reader = new FileReader(Paths.get(App.class.getResource("recentFileQueue").toURI()).toString());

            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                InputStream imageStream = App.class.getResourceAsStream("recentFileImage/" + new File(line).getName() + ".png");
                if (new File(line).exists() && imageStream != null) {
                    recentFileQueue.add(new File(line));
                    Image image = new Image(imageStream);
                    recentFileImageQueue.add(image);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 关闭程序时，保存最近文件队列
     *
     * @throws URISyntaxException
     */
    public void saveRecentFileQueue(WritableImage image) throws URISyntaxException {
        System.out.println(Paths.get(App.class.getResource("recentFileQueue").toURI()).toString());
        System.out.println(operatingFile);
        try {
            FileWriter writer = new FileWriter(Paths.get(App.class.getResource("recentFileQueue").toURI()).toString());
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(operatingFile + "\n");
            System.out.println(recentFileQueue);
            while (!recentFileQueue.isEmpty()) {
                if (recentFileQueue.peek() == operatingFile) recentFileQueue.remove();
                else bw.write(recentFileQueue.remove().toString() + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputFile = new File(Paths.get(App.class.getResource("recentFileImage").toURI()).toString() + "/" + operatingFile.getName() + ".png");
        saveOutputFile(image);
    }

    /**
     * 导出图片
     */
    public void saveOutputFile(WritableImage image) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        Thread thread = new Thread(() -> {
            try {
                ImageIO.scanForPlugins();
                ImageIO.write(bufferedImage, "png", outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public File getOperatingFile() {
        return operatingFile;
    }

    public void setOperatingFile(File operatingFile) {
        this.operatingFile = operatingFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public Queue<File> getRecentFileQueue() {
        return recentFileQueue;
    }

    public void setRecentFileQueue(Queue<File> recentFileQueue) {
        this.recentFileQueue = recentFileQueue;
    }

    public Queue<Image> getRecentFileImageQueue() {
        return recentFileImageQueue;
    }

    public void setRecentFileImageQueue(Queue<Image> recentFileImageQueue) {
        this.recentFileImageQueue = recentFileImageQueue;
    }
}
