package codeminer;

import codeminer.core.MNode;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FileManager {
    /**
     * 正在操作的文件
     */
    public static File operatingFile;

    /**
     * 最近文件队列
     */
    public static Queue<File> recentFileQueue = new LinkedList<>();

    /** 将要导出图片的文件 */
    public static File outputFile;


    /** 选择文件（打开） */
    public static void operatingFileChooser1() {
        /*选择目标文件*/
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("mp file", "*.mp"));
        Stage fileChooserStage = new Stage();
        fileChooserStage.setAlwaysOnTop(true);
        fileChooserStage.initModality(Modality.APPLICATION_MODAL);
        operatingFile = fileChooser.showOpenDialog(fileChooserStage);
    }

    /** 选择文件（保存） */
    public static void operatingFileChooser2(){
        /*选择目标文件夹*/
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save a file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("mp file", "*.mp"));
        Stage fileChooserStage = new Stage();
        fileChooserStage.setAlwaysOnTop(true);
        fileChooserStage.initModality(Modality.APPLICATION_MODAL);
        operatingFile = fileChooser.showSaveDialog(fileChooserStage);
    }

    /** 选择文件（导出） */
    public static void outPutFileChooser(){
        /*选择目标文件夹*/
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Output a file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("jpg file", "*.jpg"),new FileChooser.ExtensionFilter("png file", "*.png"));
        Stage fileChooserStage = new Stage();
        fileChooserStage.setAlwaysOnTop(true);
        fileChooserStage.initModality(Modality.APPLICATION_MODAL);
        outputFile=fileChooser.showSaveDialog(fileChooserStage);
    }

    /**
     * 新建时加载文件
     */
    public static void newLoadOperatingFile() {
        FileManager.operatingFile = null;
    }

    /**
     * 打开时加载文件
     */
    public static void openLoadOperatingFile() {
        /*将文件中对象实例化和并读取MNode类信息*/
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(operatingFile));
            MNode node = (MNode) ois.readObject();
            System.out.println(node);
            SecondaryController.setRootNode(node);
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
        saveFileQueue();
    }

    /**
     * 退出前保存文件
     */
    public static boolean saveOperatingFile() {
        /*将新建思维导图另存为*/
        if (operatingFile == null) return saveAsOperatingFile();
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
                saveFileQueue();
                System.out.println("save successful");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 退出前另存为文件
     */
    public static boolean saveAsOperatingFile() {
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
                saveFileQueue();
                System.out.println("save as successful");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 打开程序时，加载最近文件队列
     */
    public static void loadFileQueue() {
        String fileName = "cmmindmap/src/main/resources/recentFileQueue";
        try (FileReader reader = new FileReader(fileName);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (new File(line).exists())
                    recentFileQueue.add(new File(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭程序时，保存最近文件队列
     */
    public static void saveFileQueue() {
        String fileName = "cmmindmap/src/main/resources/recentFileQueue";
        if (!recentFileQueue.contains(operatingFile))
            try (FileWriter writer = new FileWriter(fileName);
                 BufferedWriter bw = new BufferedWriter(writer)) {
                bw.write(operatingFile.toString() + "\n");
                while (!recentFileQueue.isEmpty()) {
                    if (recentFileQueue.peek() == operatingFile) recentFileQueue.remove();
                    else bw.write(recentFileQueue.remove().toString() + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /** 导出图片 */
    public static void saveOutputFile(WritableImage image) {
        java.awt.image.BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        outPutFileChooser();
        System.out.println(outputFile);
        File file = new File(outputFile.getPath());
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
