package nl.capaxit.idea.plugins.usagevisualizer;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by jamiecraane on 23/03/2017.
 */
public final class Arrow implements UsageVisualization {
    private final Point start, end;
    private final Polygon arrowHead = new Polygon();

    private Arrow(final Point start, final Point end) {
        if (start == null) {
            throw new IllegalArgumentException("start is required");
        }
        if (end == null) {
            throw new IllegalArgumentException("end is required");
        }

        this.start = start;
        this.end = end;

        arrowHead.addPoint( 0,5);
        arrowHead.addPoint( -5, -5);
        arrowHead.addPoint( 5,-5);
    }

    public static Arrow create(final Point start, final Point end) {
        return new Arrow(start, end);
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    @Override
    public void draw(final Graphics2D graphics) {
        AffineTransform tx = new AffineTransform();

        graphics.setColor(new Color(131, 142, 255, 128));
        graphics.setStroke(new BasicStroke(2));
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawLine(start.x, start.y, end.x, end.y);

        tx.setToIdentity();
        double angle = Math.atan2(end.y-start.y, end.x-start.x);
        tx.translate(end.x, end.y);
        tx.rotate((angle-Math.PI/2d));

        graphics.setTransform(tx);
        graphics.fill(arrowHead);
    }
}
