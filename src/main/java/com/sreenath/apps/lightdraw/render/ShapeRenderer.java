package com.sreenath.apps.lightdraw.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

/**
 * Created by sreenath on 7/9/17.
 */
public interface ShapeRenderer {

    Path createPath(Point... points);

    public Paint getPaint();

    void draw(Canvas canvas, int color, Point... points);


}
