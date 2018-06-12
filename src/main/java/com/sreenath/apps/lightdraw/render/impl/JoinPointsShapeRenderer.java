package com.sreenath.apps.lightdraw.render.impl;

import android.graphics.Path;
import android.graphics.Point;

/**
 * Created by sreenath on 7/9/17.
 */
public class JoinPointsShapeRenderer extends AbstractShapeRenderer {
    @Override
    public Path createPath(Point... points) {
        Path path = new Path();

        for (int i = 0; i < points.length - 1; i++) {
            if (i == 0) {
                path.moveTo(points[i].x, points[i].y);
            }
            path.lineTo(points[i + 1].x, points[i + 1].y);
        }

        return path;
    }
}
