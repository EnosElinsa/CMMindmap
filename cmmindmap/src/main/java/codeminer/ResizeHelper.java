package codeminer;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ResizeHelper extends Rectangle2D {

    private Stage stage;
    private double width;
    private double height;
    private double delta;

    public ResizeHelper(Stage stage, double width, double height, double delta) {
        super(stage.getX(), stage.getY(), width, height);
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.delta = delta;
    }
    
    public static void addResizeListener(Node node) {

        //创建一个ResizeHelper对象，并获取其属性值
        ResizeHelper resizeHelper = new ResizeHelper((Stage) node.getScene().getWindow(),
                node.getScene().getWindow().getWidth(),
                node.getScene().getWindow().getHeight(),
                6);
    
        //给节点添加鼠标移动事件处理器
        node.setOnMouseMoved((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
    
                //获取鼠标相对于窗口的位置和偏移量，并判断是否在边缘范围内
                if (event.getX() > (resizeHelper.getWidth() - resizeHelper.getDelta())
                        && event.getY() > (resizeHelper.getHeight() - resizeHelper.getDelta())) {
                    //如果在右下角范围内，则设置鼠标样式为南东方向箭头（↘）
                    node.setCursor(Cursor.SE_RESIZE);
                } else if (event.getX() < resizeHelper.getDelta()
                        && event.getY() < resizeHelper.getDelta()) {
                    //如果在左上角范围内，则设置鼠标样式为北西方向箭头（↖）
                    node.setCursor(Cursor.NW_RESIZE);
                } else if (event.getX() < resizeHelper.getDelta()
                        && event.getY() > (resizeHelper.getHeight() - resizeHelper.getDelta())) {
                    //如果在左下角范围内，则设置鼠标样式为南西方向箭头（↙）
                    node.setCursor(Cursor.SW_RESIZE);
                } else if (event.getX() > (resizeHelper.getWidth() - resizeHelper.getDelta())
                        && event.getY() < resizeHelper.getDelta()) {
                    //如果在右上角范围内，则设置鼠标样式为北东方向箭头（↗）
                    node.setCursor(Cursor.NE_RESIZE);
                } else if (event.getX() < resizeHelper.getDelta()) {
                    //如果在左边缘范围内，则设置鼠标样式为水平双向箭头（←→）
                    node.setCursor(Cursor.W_RESIZE);
                } else if (event.getX() > (resizeHelper.getWidth() - resizeHelper.getDelta())) {
                    //如果在右边缘范围内，则设置鼠标样式为水平双向箭头（←→）
                    node.setCursor(Cursor.E_RESIZE);
                } else if (event.getY() < resizeHelper.getDelta()) {
                    //如果在上边缘范围内，则设置鼠标样式为垂直双向箭头（↑↓）
                    node.setCursor(Cursor.N_RESIZE);
                } else if (event.getY() > (resizeHelper.getHeight()- resizeHelper.getDelta())) {
                    //如果在下边缘范围内，则设置鼠标样式为垂直双向箭头（↑↓）
                    node.setCursor(Cursor.S_RESIZE);
                }
            }
        });
        
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }
}


