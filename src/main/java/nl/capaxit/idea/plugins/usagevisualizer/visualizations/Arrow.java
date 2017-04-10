package nl.capaxit.idea.plugins.usagevisualizer.visualizations;

import java.awt.*;

/**
 * Created by jamiecraane on 23/03/2017.
 */
public final class Arrow implements UsageVisualization {
    private static final int Y_OFFSET = 17;
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
        graphics.drawLine(start.x, startY, end.x - 2, endY - 2);

        final int dx = end.x - start.x, dy = endY - startY;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - 10, xn = xm, ym = 5, yn = -5, x;
        double sin = dy/D, cos = dx/D;

        x = xm*cos - ym*sin + start.x;
        ym = xm*sin + ym*cos + startY;
        xm = x;

        x = xn*cos - yn*sin + start.x;
        yn = xn*sin + yn*cos + startY;
        xn = x;

        final int[] xpoints = {end.x, (int) xm, (int) xn};
        final int[] ypoints = {endY, (int) ym, (int) yn};
        graphics.fillPolygon(xpoints, ypoints, 3);
    }
}
