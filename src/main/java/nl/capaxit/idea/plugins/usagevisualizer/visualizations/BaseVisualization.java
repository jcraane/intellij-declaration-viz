package nl.capaxit.idea.plugins.usagevisualizer.visualizations;

import java.awt.*;

/**
 * Created by jamiecraane on 12/04/2017.
 */
public abstract class BaseVisualization implements UsageVisualization {
    protected final void drawArrowTip(final Graphics2D graphics, final int startX, final int startY, final int endX, final int endY) {
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
