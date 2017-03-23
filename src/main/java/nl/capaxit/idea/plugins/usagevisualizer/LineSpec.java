package nl.capaxit.idea.plugins.usagevisualizer;

import java.awt.*;

/**
 * Created by jamiecraane on 23/03/2017.
 */
public final class LineSpec {
    private final Point start, end;

    private LineSpec(final Point start, final Point end) {
        if (start == null) {
            throw new IllegalArgumentException("start is required");
        }
        if (end == null) {
            throw new IllegalArgumentException("end is required");
        }

        this.start = start;
        this.end = end;
    }

    public static LineSpec create(final Point start, final Point end) {
        return new LineSpec(start, end);
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}
