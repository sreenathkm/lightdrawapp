package com.sreenath.apps.lightdraw.render.impl;

import android.graphics.Paint;

/**
 * Created by sreenath on 7/9/17.
 */
public class FilledClosedPathShapeRenderer extends ClosedPathShapeRenderer {

    @Override
    public Paint getPaint() {
        Paint paint = super.getPaint();

        paint.setStyle(Paint.Style.FILL);

        return paint;
    }
}
