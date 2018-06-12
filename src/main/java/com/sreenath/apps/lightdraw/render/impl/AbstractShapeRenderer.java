package com.sreenath.apps.lightdraw.render.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.sreenath.apps.lightdraw.render.ShapeRenderer;

/**
 * Created by sreenath on 7/9/17.
 */
public abstract class AbstractShapeRenderer implements ShapeRenderer {

    private Paint mPaint;
    public AbstractShapeRenderer() {
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(4);
    }

    @Override
    public Paint getPaint() {
        return mPaint;
    }

    @Override
    public void draw(Canvas canvas, int color, Point... points) {
        final Paint paint = getPaint();
        paint.setColor(color);

        canvas.drawPath(createPath(points), paint);
    }
}
