package nl.capaxit.idea.plugins.usagevisualizer.visualizations;

import java.awt.*;
import java.awt.geom.QuadCurve2D;

/**
 * <p>
 * Created by jamiecraane on 06/04/2017.
 */
public class BezierCurve implements UsageVisualization {
    private static final int Y_OFFSET = 10;
    private static final int START_X = 100;
    private final Point start, end;

    /**
     * @param start start of the line. The bezier curve must find the y pos based on this point.
     * @param end   end of the line. The bezier curve must find the y pos based on this point.
     */
    public BezierCurve(final Point start, final Point end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void draw(final Graphics2D graphics) {
        graphics.setColor(new Color(131, 142, 255, 128));
        graphics.setStroke(new BasicStroke(2));
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int startY = start.y;
        int endY = end.y;
        if (start.y < end.y) {
            startY += Y_OFFSET;
            endY += Y_OFFSET;
        } else {
            endY += Y_OFFSET;
        }

        int ctrlY;
        if (startY < endY) {
            ctrlY = startY + Math.abs(endY - startY) / 2;
        } else {
            ctrlY = endY + Math.abs(endY - startY) / 2;
        }
        int ctrlX = START_X;
        final QuadCurve2D.Float curve = new QuadCurve2D.Float(start.x, startY, ctrlX, ctrlY, end.x, endY);
        graphics.draw(curve);

//        subdivide the quadtree to get the middle point which is used to determine the angle of the arrow-tip
        final QuadCurve2D.Float left = new QuadCurve2D.Float(start.x, startY, ctrlX, ctrlY, end.x, endY);
        final QuadCurve2D.Float right = new QuadCurve2D.Float(start.x, startY, ctrlX, ctrlY, end.x, endY);
        curve.subdivide(left, right);
        drawArrowTip(graphics, (int) left.getX2(), (int) left.getY2(), (int) right.getX2(), (int) right.getY2());
    }

    private void drawArrowTip(final Graphics2D graphics, final int startX, final int startY, final int endX, final int endY) {
        final int dx = endX - startX, dy = endY - startY;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - 10, xn = xm, ym = 5, yn = -5, x;
        double sin = dy/D, cos = dx/D;

        x = xm*cos - ym*sin + startX;
        ym = xm*sin + ym*cos + startY;
        xm = x;

        x = xn*cos - yn*sin + startX;
        yn = xn*sin + yn*cos + startY;
        xn = x;

        final int[] xpoints = {endX, (int) xm, (int) xn};
        final int[] ypoints = {endY, (int) ym, (int) yn};
        graphics.fillPolygon(xpoints, ypoints, 3);
    }
}
