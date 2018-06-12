package com.sreenath.apps.lightdraw.render.impl;

import android.graphics.Path;
import android.graphics.Point;

/**
 * Created by sreenath on 7/9/17.
 */
public class ClosedPathShapeRenderer extends JoinPointsShapeRenderer {
    @Override
    public Path createPath(Point... points) {
        Path path = super.createPath(points);

        int length = points.length;

        if (length > 0) {
            path.moveTo(points[length - 1].x, points[length - 1].y);
            path.lineTo(points[0].x, points[0].y);
            path.close();
        }

        return path;
    }
}
