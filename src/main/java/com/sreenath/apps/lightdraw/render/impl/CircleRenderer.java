package com.sreenath.apps.lightdraw.render.impl;

import android.graphics.Path;
import android.graphics.Point;

/**
 * Created by sreenath on 7/9/17.
 */
public class CircleRenderer extends AbstractShapeRenderer {

    @Override
    public Path createPath(Point... points) {
        final Path circle = new Path();

        if (points.length == 2) {

            circle.addCircle((float)(points[0].x + points[1].x) / 2, (float)(points[0].y + points[1].y) / 2, (float)Math.sqrt(Math.pow(points[0].x - points[1].x, 2) + Math.pow(points[0].y - points[1].y, 2)) / 2.0f, Path.Direction.CW);
        }

        return circle;

    }
}
