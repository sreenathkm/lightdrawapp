package com.sreenath.apps.lightdraw.render.impl;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

/**
 * Created by sreenath on 8/9/17.
 */
public class TwoPointFilledSemiCircleRenderer extends TwoPointArcRenderer {

    public TwoPointFilledSemiCircleRenderer(boolean counterClockwise) {
        super(counterClockwise);
    }

    @Override
    public Paint getPaint() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);

        return paint;
    }

    @Override
    public Path createPath(Point... points) {
        final Path path = super.createPath(points);
        path.close();

        return path;
    }
}
