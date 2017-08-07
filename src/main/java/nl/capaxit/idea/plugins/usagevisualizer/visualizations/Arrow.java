package nl.capaxit.idea.plugins.usagevisualizer.visualizations;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by jamiecraane on 23/03/2017.
 */
public final class Arrow extends BaseVisualization {
    private static final int Y_OFFSET = 17;
    private static final int CIRCLE_SIZE = 18;
    public static final int BASE_DISTANCE = 40;
    public static final int DISTANCE_NEW_INDEX_MULTIPLIER = 8;
    private final Point start, end;

    private Arrow(final Point start, final Point end) {
        if (start == null) {
            throw new IllegalArgumentException("start is required");
        }
        if (end == null) {
            throw new IllegalArgumentException("end is required");
        }

        this.start = start;
        this.end = end;
    }

    public static Arrow create(final Point start, final Point end) {
        return new Arrow(start, end);
    }

    @Override
    public void draw(final Graphics2D graphics, final int index) {
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

        graphics.drawLine(start.x, startY, end.x - 2, endY - 2);
        drawArrowTip(graphics, start.x, startY, end.x, endY);
        drawCircle(graphics, startY, endY, index);
        drawIdentifier(graphics, startY, endY, index);
    }

//    todo dynamische offset werkt nog niet
    private void drawCircle(final Graphics2D graphics, final int startY, final int endY, final int index) {
        final double distance = Math.sqrt(Math.pow((double) end.x - 2 - (double) start.x, 2) + Math.pow((double) endY - 2 - (double) startY, 2));
        final double rtCircle =  (BASE_DISTANCE + ((index + 1) * DISTANCE_NEW_INDEX_MULTIPLIER)) / distance;
//        final double rtCircle =  (BASE_DISTANCE + index) / distance;
        final double circleX, circleY;
        circleX = (1 - rtCircle) * start.x + rtCircle * end.x;
        circleY = (1 - rtCircle) * startY + rtCircle * endY;
        final Ellipse2D.Double circle = new Ellipse2D.Double(circleX, circleY, CIRCLE_SIZE, CIRCLE_SIZE);
        graphics.setColor(new Color(27, 198, 141, 255));
        graphics.fill(circle);
    }

    private void drawIdentifier(final Graphics2D graphics, final int startY, final int endY, final int index) {
        final double distance = Math.sqrt(Math.pow((double) end.x - 2 - (double) start.x, 2) + Math.pow((double) endY - 2 - (double) startY, 2));
        final double rtCircle = (BASE_DISTANCE + ((index + 1) * DISTANCE_NEW_INDEX_MULTIPLIER)) / distance;
//        final double rtCircle = (BASE_DISTANCE) / distance;
        final double idX, idY;
//        3 and 1.5 should be calculated from the size of the text.
        idX = (1 - rtCircle) * start.x + rtCircle * end.x + (CIRCLE_SIZE / 3);
        idY = (1 - rtCircle) * startY + rtCircle * endY + (CIRCLE_SIZE / 1.5);
        graphics.setColor(new Color(0, 0, 141, 255));
        graphics.drawString(String.valueOf(IDENTIFIERS[index]), (float) idX, (float) idY);
    }
}
