package codeminer.core;

import java.io.Serializable;
import java.util.ArrayList;

import codeminer.SecondaryController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * 思维导图里的一个节点 (MNode / MindmapNode)
 * 
 * @author Enos
 */
public class MNode extends TextField implements Serializable {

    /** 常量的定义 */
    /** 一个节点的预设高度 */
    public static final double PREF_HEIGHT    = 36.0;
    /** 一个节点的预设宽度 */
    public static final double PREF_WIDTH     = 81.0;
    /** 相邻节点间的垂直间距 */
    public static final double VERTICAL_SPACING   = 24.0;
    /** 相邻节点间的水平间距 */
    public static final double HORIZONTAL_SPACING = 46.0;
    /** 文本域里一个字符占多个像素*/
    public static final double PIXEL_PER_CHAR = 13.0;
    /** 节点的朝向向左 */
    public static final boolean LEFT  = false;
    /** 节点的朝向向右 */
    public static final boolean RIGHT = true;

    public static final String DEFAULT_STYLE  = "-fx-control-inner-background:#DCDBF2";
    public static final String HOVERING_STYLE = "-fx-control-inner-background:#DCDBF2;"
                                               + "-fx-border-color: #96DEFF;"
                                               + "-fx-border-radius: 2px;"
                                               + "-fx-border-width: 3px";
    public static final String SELECTED_STYLE = "-fx-control-inner-background:#DCDBF2;"
                                               + "-fx-border-color: #2EBDFF;"   
                                               + "-fx-border-radius: 2px;"
                                               + "-fx-border-width: 3px";
    /** 根节点的属性（因为设计中只有一个根节点，所以把根节点的属性设置为类的属性，且一个根节点 有且只有 一个左子树和一个右子树） */
    /** 根节点的引用 */
    private static MNode rootNode;
    /** 根节点的整个左子树的高度 */
    private static double leftSubtreeHeight  = PREF_HEIGHT;
    /** 根节点的整个左子树的宽度 */
    private static double leftSubtreeWidth;
    /** 根节点的整个右子树的高度 */
    private static double rightSubtreeHeight = PREF_HEIGHT;
    /** 根节点的整个右子树的宽度 */
    private static double rightSubtreeWidth;
    /** 根节点的左子树节点列表 */
    private static ArrayList<MNode> leftSubtree  = new ArrayList<>();
    /** 根节点的右子树节点列表 */
    private static ArrayList<MNode> rightSubtree = new ArrayList<>();
    /** 根节点所在的面板 */
    private static AnchorPane anchorPane;
    
    /** 节点自身和其所有子树构成的树的高度 */
    private double treeHeight = PREF_HEIGHT;
    /** 节点自身和其所有子树构成的树的宽度 */
    private double treeWidth;
    /** 节点的文本域的宽度 */
    private double textFieldWidth = PREF_WIDTH;
    /** 一个节点在布局里的朝向 {@code LEFT} 和 {@code RIGHT} */
    private boolean orientation;
    /** 一个节点是否为根节点（一个思维导图里默认只有一个根节点） */
    private boolean isRootNode;
    /** 一个节点的文本 */
    private String nodeText;
    /** 子节点列表 */
    private ArrayList<MNode> childNodes = new ArrayList<>();
    /** 从父节点出发连接到该节点的边 */
    private MEdge edge = new MEdge();
    /** 父节点 */
    private MNode parentNode;
    /** 节点在大纲树视图里的视图 */
    private TreeItemString treeItem;
    /** 是否被选中 */
    private transient BooleanProperty isSelected = new SimpleBooleanProperty(false);
    
    public MNode(String nodeText, boolean isRootNode) {
        super(nodeText);
        super.setPrefHeight(PREF_HEIGHT);
        super.setPrefWidth(PREF_WIDTH);
        this.nodeText = nodeText;
        initializeNode();
        if (isRootNode) {
            setRootNode(true);
            rootNode = this;
            isSelected.set(true);
        }
    }

    /**
     * 给节点初始化事件处理和样式
     */
    private void initializeNode() {
        super.setPrefHeight(PREF_HEIGHT);
        super.setPrefWidth(textFieldWidth);
        super.setAlignment(Pos.CENTER);
        super.setText(nodeText);
        super.setEditable(false);
        super.setStyle(DEFAULT_STYLE);

        super.setOnMouseClicked(event -> {
            if (SecondaryController.getSelectedNode() != null) {
                SecondaryController.getSelectedNode().isSelected.set(false);
                SecondaryController.getSelectedNode().setEditable(false);
            }
            SecondaryController.hasNodeBeenSelected().set(true);
            SecondaryController.setSelectedNode(this);
            SecondaryController.getSelectedNode().isSelected.set(true);
            if (event.getClickCount() == 2) {
                super.setCursor(Cursor.TEXT);
                super.setEditable(true);
            }
        });

        super.setOnMouseEntered(event -> {
            super.setCursor(Cursor.HAND);
            if (this.isSelected.get() == false) {
                super.setStyle(HOVERING_STYLE);
            }
        });

        super.setOnMouseExited(event-> {
            if (SecondaryController.getSelectedNode() != this) {
                this.setStyle(DEFAULT_STYLE);
            }
            else {
                this.setStyle(SELECTED_STYLE);
            }
        });

        super.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                nodeText = MNode.super.getText();
                treeItem.setValue(nodeText);
                MNode.super.setPrefWidth(Math.max(MNode.super.getText().length() * PIXEL_PER_CHAR, PREF_HEIGHT));
                textFieldWidth = MNode.super.getPrefWidth();
                update(rootNode);
            }
        });

        isSelected.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (SecondaryController.getSelectedNode() == null) return;
                if (newValue) {
                    SecondaryController.getSelectedNode().setStyle(SELECTED_STYLE);
                } else {
                    SecondaryController.getSelectedNode().setStyle(DEFAULT_STYLE);
                }
            }
        });

        treeItem = new TreeItemString(nodeText);
        treeItem.setExpanded(true);
    }

    /**
     * 用于更新整个树结构中的树的宽度和高度属性。
     * @param node 要调整的节点
     */
    private void updateSize(MNode node) {
        double leftSubtreeHeight  = 0;
        double rightSubtreeHeight = 0;
        double treeHeight = 0;

        if (node.isRootNode()) { // 根节点的更新按照左子树和右子树分别进行
            // 更新左子树
            MNode.leftSubtreeWidth = 0; // 重置为0来进行重新计算
            for (MNode childNode : MNode.leftSubtree) {
                childNode.setOrientation(LEFT);
                updateSize(childNode); // 递归更新左子树

                
                MNode.leftSubtreeWidth = // 找出根节点的左子树的宽度：当前的宽度 和 子节点的宽度+间距 两者之中最大的一个
                    Math.max(MNode.leftSubtreeWidth, childNode.getTreeWidth() + HORIZONTAL_SPACING + childNode.getTextFieldWidth());
                leftSubtreeHeight += childNode.getTreeHeight(); // 根节点的左子树的高度包含每一个子树的高度
            }
            leftSubtreeHeight += (MNode.leftSubtree.size() - 1) * VERTICAL_SPACING; // 根节点的左子树的高度包含每一个节点间的垂直间距
            MNode.leftSubtreeHeight = Math.max(leftSubtreeHeight, PREF_HEIGHT); // 根节点没有左子树时，左子树的高度设置为一个节点的预设高度

            // 更新右子树
            MNode.rightSubtreeWidth = 0;
            for (MNode childNode : MNode.rightSubtree) {
                childNode.setOrientation(RIGHT);
                update(childNode);

                MNode.rightSubtreeWidth = 
                    Math.max(MNode.rightSubtreeWidth, childNode.getTreeWidth() + HORIZONTAL_SPACING + childNode.getTextFieldWidth());
                rightSubtreeHeight += childNode.getTreeHeight();
            }
            rightSubtreeHeight += (MNode.rightSubtree.size() - 1) * VERTICAL_SPACING;
            MNode.rightSubtreeHeight = Math.max(rightSubtreeHeight, PREF_HEIGHT);
        } else {
            // 更新子节点
            node.setTreeWidth(0); // 重置为0来进行重新计算
            for (MNode childNode : node.getChildNodes()) {
                childNode.setOrientation(node.getOrientation());
                updateSize(childNode);

                node.setTreeWidth(Math.max(node.getTreeWidth(), childNode.getTreeWidth() + HORIZONTAL_SPACING + childNode.getTextFieldWidth()));
                treeHeight += childNode.getTreeHeight();
            }
            treeHeight += (node.getChildNodes().size() - 1) * VERTICAL_SPACING;
            node.setTreeHeight(Math.max(treeHeight, PREF_HEIGHT));
        }
    }

    /**
     * 根据父节点的位置和上边界来确定子节点在布局的位置
     * @param parentNode 父节点
     * @param childNode 子节点
     * @param upperBoundOfTheChildNode 子节点的上边界
     * @return 下一个子节点的上边界
     */
    private double updatePositionUtil(MNode parentNode, MNode childNode, double upperBoundOfTheChildNode) {
        double layoutYOfTheChildNode = upperBoundOfTheChildNode + childNode.getTreeHeight() / 2 - PREF_HEIGHT / 2;
        childNode.setLayoutY(layoutYOfTheChildNode);
        if (childNode.getOrientation() == LEFT) {
            childNode.setLayoutX(parentNode.getLayoutX() - HORIZONTAL_SPACING - childNode.getTextFieldWidth());
            childNode.getEdge().drawEdge(parentNode.getLayoutX(), parentNode.getLayoutY() + PREF_HEIGHT / 2,
                childNode.getLayoutX() + childNode.getTextFieldWidth(), childNode.getLayoutY() + PREF_HEIGHT / 2);
        } else {
            childNode.setLayoutX(parentNode.getLayoutX() + HORIZONTAL_SPACING + parentNode.getTextFieldWidth());
            childNode.getEdge().drawEdge(parentNode.getLayoutX() + parentNode.getTextFieldWidth(), parentNode.getLayoutY() + PREF_HEIGHT / 2,
                childNode.getLayoutX(), childNode.getLayoutY() + PREF_HEIGHT / 2);
        }
        double upperBoundOfTheNextchildNode = layoutYOfTheChildNode + childNode.getTreeHeight() / 2 + VERTICAL_SPACING + PREF_HEIGHT / 2;
        return upperBoundOfTheNextchildNode;
    }

    /**
     * 把除了根节点的各个节点根据其子树的高度和父节点的纵坐标来设置其在布局里的纵坐标，
     * 根据父节点的横坐标来确定其在布局里的横坐标。同时将连接节点间的边进行设置。
     * @param node 要调整的节点（直接调用的时候传入的只能是根节点）
     * @param orientation 要调整的根节点的子树的朝向（在递归调用时，该参数无实际意义）
     */
    private void updateChildNodesPosition(MNode node, boolean orientation) {
        if (!node.isRootNode() && node.getChildNodes().isEmpty()) return; // 叶子节点无需进行调整
        if (node.isRootNode()) { // 根节点需要特地设置树的高度
            if (orientation == LEFT) { // 当调整的是根节点的左子树时，需要设置它的树的高度为左子树的高度
                node.setTreeHeight(leftSubtreeHeight);
            } else if (orientation == RIGHT) { // 当调整的是根节点的右子树时，需要设置它的树的高度为右子树的高度
                node.setTreeHeight(rightSubtreeHeight);
            }
        }

        double upperBoundOfTheChildNode = node.getLayoutY() + PREF_HEIGHT / 2 - node.getTreeHeight() / 2; // 子节点的上边界（子节点需要布局在上下边界的中间）
        if (node.isRootNode()) { // 根节点按照左右子树分别进行调整
            if (orientation == LEFT) {
                for (MNode childNode : leftSubtree) { // 
                    upperBoundOfTheChildNode = updatePositionUtil(node, childNode, upperBoundOfTheChildNode); // 迭代更新，得到下一个子节点的上边界
                }
                for (MNode childNode : leftSubtree) { // 递归调整子节点
                    updateChildNodesPosition(childNode, LEFT);
                }
            } else if (orientation == RIGHT) {
                for (MNode childNode : rightSubtree) {
                    upperBoundOfTheChildNode = updatePositionUtil(node, childNode, upperBoundOfTheChildNode);
                }
                for (MNode childNode : rightSubtree) {
                    updateChildNodesPosition(childNode, RIGHT);
                }
            }
        } else {
            for (MNode childNode : node.getChildNodes()) {
                upperBoundOfTheChildNode = updatePositionUtil(node, childNode, upperBoundOfTheChildNode);
            }
            for (MNode childNode : node.getChildNodes()) {
                updateChildNodesPosition(childNode, node.getOrientation());
            }
        }
    }

    private void updatePaneSizeAndRootNodePosition() {
        double topMargin = Math.min(rootNode.getLayoutY() + PREF_HEIGHT / 2 - leftSubtreeHeight / 2,
        rootNode.getLayoutY() + PREF_HEIGHT / 2 - rightSubtreeHeight/ 2);
        if (topMargin < 0) {
            anchorPane.setPrefHeight(anchorPane.getPrefHeight() + Math.abs(topMargin) + PREF_HEIGHT);
        }
        double leftMargin = rootNode.getLayoutX() - leftSubtreeWidth;
        if (leftMargin < 0) {
            anchorPane.setPrefWidth(anchorPane.getPrefWidth() + Math.abs(leftMargin) + PREF_WIDTH);
        }
        rootNode.setLayoutX(anchorPane.getPrefWidth() / 2 - PREF_WIDTH / 2);
        rootNode.setLayoutY(anchorPane.getPrefHeight() / 2 - PREF_HEIGHT / 2);
    }

    /**
     * 当树结构发生更改时，根据调整更新整个树的布局
     * @param root 根节点
     */
    public void update(MNode root) {
        updateSize(root);
        updatePaneSizeAndRootNodePosition();
        updateChildNodesPosition(root, LEFT);
        updateChildNodesPosition(root, RIGHT);
    }

    /**
     * 从anchorPane上删除节点以及节点的子节点以及之间的边
     * @param node 要删除的节点
     */
    private void deleteNodeFromPane(MNode node) {
        if (node.isRootNode()) { // 不能删除根节点
            return;
        }
        for (int index = 0; index < node.getChildNodes().size(); index++) {
            deleteNodeFromPane(node.getChildNodes().get(index));
        }
        anchorPane.getChildren().remove(node);
        anchorPane.getChildren().remove(node.getEdge());
    }

    /**
     * 从childNodes的列表上移除节点
     * @param node 要删除的节点
     */
    private void deleteNodeFromList(MNode node) {
        for (int index = 0; index < node.getChildNodes().size(); index++) {
            deleteNodeFromList(node.getChildNodes().get(index));
        }
        if (node.getParentNode().isRootNode()) {
            if (node.getOrientation() == LEFT) {
                leftSubtree.remove(node);
            } else {
                rightSubtree.remove(node);
            }
            rootNode.getTreeItem().getChildren().remove(node.getTreeItem());
        } else {
            node.getParentNode().getChildNodes().remove(node);
            node.getParentNode().getTreeItem().getChildren().remove(node.getTreeItem());
        }
    }

    /**
     * 删除一个节点
     * @param node 要删除的节点
     */
    public void deleteNode(MNode node) {
        deleteNodeFromPane(node);
        deleteNodeFromList(node);
    }

    private static void reloadUtil(MNode node, AnchorPane anchorPane) {
        node.isSelected = new SimpleBooleanProperty(false);
        node.initializeNode();
        node.getParentNode().getTreeItem().getChildren().add(node.getTreeItem());
        anchorPane.getChildren().add(node);
        anchorPane.getChildren().add(node.getEdge());
        for (MNode childNode : node.getChildNodes()) {
            reloadUtil(childNode, anchorPane);
        }
    }

    /**
     * 当保存后再被打开的树结构需要重新把节点和边添加到画布中
     * @param treeView
     */
    public void reload() {
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(rootNode);
        rootNode.isSelected = new SimpleBooleanProperty(true);
        rootNode.initializeNode();
        for (MNode childNode : rightSubtree) {
            reloadUtil(childNode, anchorPane);
        }
        for (MNode childNode : leftSubtree) {
            reloadUtil(childNode, anchorPane);
        }
        update(rootNode);
    }

    public ArrayList<MNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(ArrayList<MNode> childNodes) {
        this.childNodes = childNodes;
    }

    public MNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(MNode parentNode) {
        this.parentNode = parentNode;
    }

    public static double getLeftSubtreeHeight() {
        return leftSubtreeHeight;
    }

    public static void setLeftSubtreeHeight(double leftSubtreeHeight) {
        MNode.leftSubtreeHeight = leftSubtreeHeight;
    }

    public static double getRightSubtreeHeight() {
        return rightSubtreeHeight;
    }

    public static void setRightSubtreeHeight(double rightSubtreeHeight) {
        MNode.rightSubtreeHeight = rightSubtreeHeight;
    }

    public static double getLeftSubtreeWidth() {
        return leftSubtreeWidth;
    }

    public static void setLeftSubtreeWidth(double leftSubtreeWidth) {
        MNode.leftSubtreeWidth = leftSubtreeWidth;
    }

    public static double getRightSubtreeWidth() {
        return rightSubtreeWidth;
    }

    public static void setRightSubtreeWidth(double rightSubtreeWidth) {
        MNode.rightSubtreeWidth = rightSubtreeWidth;
    }

    public static ArrayList<MNode> getLeftSubtree() {
        return leftSubtree;
    }

    public static void setLeftSubtree(ArrayList<MNode> leftSubtree) {
        MNode.leftSubtree = leftSubtree;
    }

    public static ArrayList<MNode> getRightSubtree() {
        return rightSubtree;
    }

    public static void setRightSubtree(ArrayList<MNode> rightSubtree) {
        MNode.rightSubtree = rightSubtree;
    }

    public double getTreeHeight() {
        return treeHeight;
    }

    public void setTreeHeight(double treeHeight) {
        this.treeHeight = treeHeight;
    }

    public double getTreeWidth() {
        return treeWidth;
    }

    public void setTreeWidth(double treeWidth) {
        this.treeWidth = treeWidth;
    }

    public double getTextFieldWidth() {
        return textFieldWidth;
    }

    public void setTextFieldWidth(double textFieldWidth) {
        this.textFieldWidth = textFieldWidth;
    }

    public boolean getOrientation() {
        return orientation;
    }

    public void setOrientation(boolean orientation) {
        this.orientation = orientation;
    }

    public boolean isRootNode() {
        return isRootNode;
    }

    public void setRootNode(boolean isRootNode) {
        this.isRootNode = isRootNode;
    }

    public BooleanProperty isSelected() {
        return isSelected;
    }

    public String getNodeText() {
        return nodeText;
    }

    public void setNodeText(String nodeText) {
        this.nodeText = nodeText;
    }

    public TreeItemString getTreeItem() {
        return treeItem;
    }

    public void setTreeItem(TreeItemString treeItem) {
        this.treeItem = treeItem;
    }

    public static MNode getRootNode() {
        return rootNode;
    }

    public static void setRootNode(MNode rootNode) {
        MNode.rootNode = rootNode;
    }

    public static AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public static void setAnchorPane(AnchorPane anchorPane) {
        MNode.anchorPane = anchorPane;
    }

    public MEdge getEdge() {
        return edge;
    }

    public void setEdge(MEdge edge) {
        this.edge = edge;
    }


}
