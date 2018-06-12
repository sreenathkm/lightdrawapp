package com.sreenath.apps.lightdraw.enums;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;

import com.sreenath.apps.lightdraw.render.ShapeRenderer;
import com.sreenath.apps.lightdraw.render.impl.CircleRenderer;
import com.sreenath.apps.lightdraw.render.impl.ClosedPathShapeRenderer;
import com.sreenath.apps.lightdraw.render.impl.FilledCircleRenderer;
import com.sreenath.apps.lightdraw.render.impl.FilledClosedPathShapeRenderer;
import com.sreenath.apps.lightdraw.render.impl.JoinPointsShapeRenderer;
import com.sreenath.apps.lightdraw.render.impl.TwoPointArcRenderer;
import com.sreenath.apps.lightdraw.render.impl.TwoPointFilledSemiCircleRenderer;

/**
 * Created by sreenath on 28/12/16.
 */
public enum SuggestionPath {

    FOLLOW_POINTS (new JoinPointsShapeRenderer()),
    CLOSED_LOOP (new ClosedPathShapeRenderer()),
    CLOSED_LOOP_FILLED (new FilledClosedPathShapeRenderer()),
    CIRCLE (new CircleRenderer()),
    CIRCLE_FILLED (new FilledCircleRenderer()),
    TWO_POINT_ARC_CW (new TwoPointArcRenderer(false)),
    TWO_POINT_ARC_CCW (new TwoPointArcRenderer(true)),
    TWO_POINT_SEMICIRCLE_FILLED_CW(new TwoPointFilledSemiCircleRenderer(false)),
    TWO_POINT_SEMICIRCLE_FILLED_CCW(new TwoPointFilledSemiCircleRenderer(true))
    ;

    private ShapeRenderer shapeRenderer;

    SuggestionPath(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    public void draw(Canvas canvas, int color, Point... points) {
        shapeRenderer.draw(canvas, color, points);
    }

    public Path createPath(Point ...points) {
        return shapeRenderer.createPath(points);
    }


}
