package codeminer;

import codeminer.core.MNode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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

    /**
     * 新建时加载文件
     */
    public static void newLoadOperatingFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(operatingFile))) {
            System.out.println("new successful");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开时加载文件
     */
    public static void openLoadOperatingFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("mp file", "*.mp"));
        operatingFile = fileChooser.showOpenDialog(new Stage());
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(operatingFile));
            MNode node = (MNode)ois.readObject();
            System.out.println(node);
            SecondaryController.setRootNode(node);
            MNode.setLeftSubtreeHeight(ois.readDouble());
            MNode.setLeftSubtreeWidth(ois.readDouble());
            MNode.setRightSubtreeHeight(ois.readDouble());
            MNode.setRightSubtreeWidth(ois.readDouble());
            MNode.setLeftSubtree((ArrayList<MNode>)ois.readObject());
            MNode.setRightSubtree((ArrayList<MNode>)ois.readObject());
            ois.close();
            System.out.println("open successful");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 退出前保存文件
     */
    public static void saveOperatingFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(operatingFile))) {
            oos.writeObject(SecondaryController.getRootNode());
            System.out.println("save successful");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出前另存为文件
     */
    public static void saveAsOperatingFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        operatingFile = fileChooser.showSaveDialog(new Stage());
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
                System.out.println("save as successful");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 打开程序时，加载最近文件队列
     */
    public static void loadFileQueue() {
        String fileName = "src/main/resources/recentFileQueue.txt";
        try (FileReader reader = new FileReader(fileName);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                recentFileQueue.add(new File(line));
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    /**
     * 关闭程序时，保存最近文件队列
     */
    public static void saveFileQueue() {
        String fileName = "src/main/resources/recentFileQueue.txt";
        if (recentFileQueue.contains(operatingFile))
            try (FileWriter writer = new FileWriter(fileName);
                 BufferedWriter bw = new BufferedWriter(writer)) {
                bw.write(operatingFile.toString() + "\n");
                while (!recentFileQueue.isEmpty()) {
                    if (recentFileQueue.peek() == operatingFile) recentFileQueue.remove();
                    else bw.write(recentFileQueue.remove().toString() + "\n");
                }
            } catch (IOException e) {
                System.err.format("IOException: %s%n", e);
            }
    }


}
