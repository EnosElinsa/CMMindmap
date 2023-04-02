package codeminer;

import codeminer.core.MNode;
import javafx.scene.Node;

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
    public static Queue<File> recentFileQueue = new LinkedList<>();;

    /**
     * 新建时加载文件
     */
    public static void newLoadOperatingFile() {
        operatingFile=new File("src/main/resources/tempNewFile.xmind");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(operatingFile))) {
            SecondaryController.setRootNode((MNode)ois.readObject());
            System.out.println("新建成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 打开时加载文件
     */
    public void openLoadOperatingFile() {
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(operatingFile))){
            SecondaryController.setRootNode((MNode)ois.readObject());
            System.out.println("打开成功");
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
        operatingFile=new File("src/main/resources/tempNewFile.xmind");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(operatingFile))) {
            oos.writeObject(SecondaryController.getRootNode());
            System.out.println("保存成功");
        } catch (IOException e) {
            e.printStackTrace();
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
