package com.sreenath.apps.lightdraw.render.impl;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;

import com.sreenath.apps.lightdraw.utils.MeasurementUtils;

/**
 * Created by sreenath on 7/9/17.
 */
public class TwoPointArcRenderer extends AbstractShapeRenderer {

    protected final boolean counterClockwise;

    public TwoPointArcRenderer(boolean counterClockwise) {
        this.counterClockwise = counterClockwise;
    }

    @Override
    public Path createPath(Point... points) {
        final Path arc = new Path();

        float startAngle, sweepAngle;

        float angle = MeasurementUtils.angle(points[0].x, points[0].y, points[1].x, points[1].y);
        float radius = MeasurementUtils.distance(points[0], points[1]) / 2;

        if (counterClockwise) {
            startAngle = angle;
            sweepAngle = 180;
        } else {
            startAngle = angle + 180;
            sweepAngle = 180;
        }

        Point center = MeasurementUtils.calculateCenterOfGravity(points);
        arc.arcTo(new RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius), startAngle, sweepAngle);

        return arc;
    }

    @Override
    public Paint getPaint() {
        Paint paint = super.getPaint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);

        return paint;
    }
}
