package nl.capaxit.idea.plugins.usagevisualizer.visualizations;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by jamiecraane on 23/03/2017.
 */
public final class Arrow extends BaseVisualization {
    private static final int Y_OFFSET = 17;
    private static final int CIRCLE_SIZE = 18;
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
    public void draw(final Graphics2D graphics, final int count) {
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

        // todo uitlijning klopt nog niet helemaal
        drawCircle(graphics, startY, endY);
        drawIdentifier(graphics, startY, endY, count);

//        graphics.setColor(new Color(0, 0, 141, 255));
//        graphics.drawString("1", (float) circleX, (float) circleY);
//        todo draw number in circle, then continue from a to z.
    }

    private void drawCircle(final Graphics2D graphics, final int startY, final int endY) {
        final double distance = Math.sqrt(Math.pow((double) end.x - 2 - (double) start.x, 2) + Math.pow((double) endY - 2 - (double) startY, 2));
        final double rtCircle = 50 / distance;
        final double circleX, circleY;
        circleX = (1 - rtCircle) * start.x + rtCircle * end.x;
        circleY = (1 - rtCircle) * startY + rtCircle * endY;
        final Ellipse2D.Double circle = new Ellipse2D.Double(circleX, circleY, CIRCLE_SIZE, CIRCLE_SIZE);
        graphics.setColor(new Color(27, 198, 141, 255));
        graphics.fill(circle);
    }

    private void drawIdentifier(final Graphics2D graphics, final int startY, final int endY, final int count) {
        final double distance = Math.sqrt(Math.pow((double) end.x - 2 - (double) start.x, 2) + Math.pow((double) endY - 2 - (double) startY, 2));
        final double rtCircle = 65 / distance;
        final double idX, idY;
        idX = (1 - rtCircle) * start.x + rtCircle * end.x;
        idY = (1 - rtCircle) * startY + rtCircle * endY;
        graphics.setColor(new Color(0, 0, 141, 255));
        graphics.drawString(String.valueOf(count), (float) idX, (float) idY);
    }
}
