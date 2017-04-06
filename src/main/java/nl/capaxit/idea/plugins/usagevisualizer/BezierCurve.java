package nl.capaxit.idea.plugins.usagevisualizer;

import java.awt.*;
import java.awt.geom.QuadCurve2D;

/**
 * todo provide a better endX position so that the curve points more towards the element instead of a fixed offset.
 *
 * Created by jamiecraane on 06/04/2017.
 */
public class BezierCurve implements UsageVisualization {
    private static final int Y_OFFSET = 10;
    private static final int START_X = 100;
    public static final int X_OFFSET = 30;
    private final Point start, end;

    /**
     * @param start start of the line. The bezier curve must find the y pos based on this point.
     * @param end end of the line. The bezier curve must find the y pos based on this point.
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
        } else {
            endY += Y_OFFSET;
        }

        int ctrlY;
        if (startY < endY) {
             ctrlY = startY + Math.abs(endY - startY) / 2;
        } else {
            ctrlY = endY + Math.abs(endY - startY) / 2;
        }
        int ctrlX = START_X - X_OFFSET;
        final QuadCurve2D.Float curve = new QuadCurve2D.Float(START_X, startY, ctrlX, ctrlY, START_X, endY);
        graphics.draw(curve);

        drawArrowTip(graphics, endY, ctrlY);
    }

    private void drawArrowTip(final Graphics2D graphics, final int endY, final int ctrlY) {
        final int dx = START_X - (START_X - X_OFFSET), dy = endY - ctrlY;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - 10, xn = xm, ym = 5, yn = -5, x;
        double sin = dy/D, cos = dx/D;

        x = xm*cos - ym*sin + START_X - X_OFFSET;
        ym = xm*sin + ym*cos + ctrlY;
        xm = x;

        x = xn*cos - yn*sin + START_X - X_OFFSET;
        yn = xn*sin + yn*cos + ctrlY;
        xn = x;

        final int[] xpoints = {START_X, (int) xm, (int) xn};
        final int[] ypoints = {endY, (int) ym, (int) yn};
        graphics.fillPolygon(xpoints, ypoints, 3);
    }
}
