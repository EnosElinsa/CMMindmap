package codeminer.core;

import java.io.Serializable;

import javafx.scene.control.TreeItem;

/**
 * 让TreeItem可以被序列化
 */
public class TreeItemString extends TreeItem<String> implements Serializable {
    public TreeItemString(String text) {
        super(text);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
