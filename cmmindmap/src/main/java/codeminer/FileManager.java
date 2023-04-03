package codeminer;

import codeminer.core.MNode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
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
        try (ObjectInputStream ois=new ObjectInputStream(new FileInputStream(operatingFile))) {
            SecondaryController.setRootNode((MNode)ois.readObject());
            System.out.println("open successful");
        }
        catch (IOException e){
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
            try  {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(operatingFile));
                oos.writeObject(SecondaryController.getRootNode());
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
