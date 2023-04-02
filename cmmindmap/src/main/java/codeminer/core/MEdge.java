package codeminer.core;

import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

import java.io.Serializable;

/**
 * 从当前节点到下一个节点的边
 * 
 * @author Enos
 */
public class MEdge extends CubicCurve implements Serializable {
    public MEdge() {}
    public void drawEdge(double startX, double startY, double endX, double endY) {
        super.setStartX(startX); super.setStartY(startY);
        super.setEndX(endX); super.setEndY(endY);

        super.setControlX1((startX + endX) / 2); super.setControlY1(startY);
        super.setControlX2((startX + endX) / 2); super.setControlY2(endY);
        
        super.setStrokeWidth(1.5);
        super.setFill(Color.TRANSPARENT);
        super.setStroke(Color.BLACK);
    }
}
