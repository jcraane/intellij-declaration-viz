package nl.capaxit.idea.plugins.usagevisualizer.visualizations;

import nl.capaxit.idea.plugins.usagevisualizer.configuration.UsageVisualizationConfig;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.QuadCurve2D;

/**
 * <p>
 * Created by jamiecraane on 06/04/2017.
 */
public class BezierCurve extends BaseVisualization {
    private static final int Y_OFFSET = 10;
    private static final int START_X = 100;
    private final Point start, end;

    private static final int CIRCLE_SIZE = 18;

    /**
     * @param start start of the line. The bezier curve must find the y pos based on this point.
     * @param end   end of the line. The bezier curve must find the y pos based on this point.
     */
    public BezierCurve(final Point start, final Point end, final UsageVisualizationConfig config) {
        super(config);
        this.start = start;
        this.end = end;
    }

    @Override
    public void draw(final Graphics2D graphics, final int index) {
        graphics.setColor(lineColor);
        graphics.setStroke(new BasicStroke(config.getLineWidth()));
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int startY = start.y;
        int endY = end.y;
        if (start.y < end.y) {
            startY += Y_OFFSET;
            endY += Y_OFFSET;
        } else {
            endY += Y_OFFSET;
        }

        final int ctrlY;
        if (startY < endY) {
            ctrlY = startY + Math.abs(endY - startY) / 2;
        } else {
            ctrlY = endY + Math.abs(endY - startY) / 2;
        }
        final int ctrlX = START_X;
        final QuadCurve2D.Float curve = new QuadCurve2D.Float(start.x, startY, ctrlX, ctrlY, end.x, endY);
        graphics.draw(curve);

//        subdivide the quadcurve to get the middle point which is used to determine the angle of the arrow-tip
        final QuadCurve2D.Float left = new QuadCurve2D.Float(start.x, startY, ctrlX, ctrlY, end.x, endY);
        final QuadCurve2D.Float right = new QuadCurve2D.Float(start.x, startY, ctrlX, ctrlY, end.x, endY);
        curve.subdivide(left, right);
        drawArrowTip(graphics, (int) left.getX2(), (int) left.getY2(), (int) right.getX2(), (int) right.getY2());

        if (config.isQuickJumpEnabled()) {
            drawIdentifier(graphics, left, index, lineColor);
        }
    }

    private void drawIdentifier(final Graphics2D graphics, final QuadCurve2D.Float first, final int index, final Color lineColor) {
        final QuadCurve2D.Float left = new QuadCurve2D.Float(0, 0, 0, 0, 0, 0);
        final QuadCurve2D.Float right = new QuadCurve2D.Float(0, 0, 0, 0, 0, 0);
        first.subdivide(left, right);

        final Ellipse2D.Double circle = new Ellipse2D.Double((int) left.x2 - (CIRCLE_SIZE / 2), (int) left.y2 - (CIRCLE_SIZE / 2), CIRCLE_SIZE, CIRCLE_SIZE);
        graphics.setColor(lineColor);
        graphics.fill(circle);
        graphics.setColor(new Color(0, 0, 141, 255));
        graphics.drawString(getIdentifier(index), left.x2 - (CIRCLE_SIZE / 3), (float) (left.y2 + (CIRCLE_SIZE / 3)));
    }

}
