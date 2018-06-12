package com.sreenath.apps.lightdraw.tool.impl;

import android.graphics.Rect;
import android.view.View;

import com.sreenath.apps.lightdraw.R;
import com.sreenath.apps.lightdraw.tool.Tool;
import com.sreenath.apps.lightdraw.utils.PathUtils;
import com.sreenath.apps.lightdraw.utils.Utils;
import com.sreenath.apps.lightdraw.views.DrawableView;
import com.sreenath.apps.lightdraw.views.ToolBoxView;
import com.sreenath.apps.lightdraw.views.state.DrawableShape;

import java.util.Arrays;

/**
 * Created by sreenath on 18/10/17.
 */
public class CopyShapeTool implements Tool {
    @Override
    public boolean click(ToolBoxView toolBoxView) {
        DrawableView drawableView = (DrawableView)((View)(toolBoxView.getParent())).findViewById(R.id.drawableView);

        final DrawableShape currentShape = drawableView.getCurrentSelectedShape();

        drawableView.addShape(copyShape(currentShape));
        drawableView.invalidate();
        return false;
    }

    private DrawableShape copyShape(final DrawableShape shape) {
        final DrawableShape newShape = new DrawableShape(
                Arrays.asList(
                        PathUtils.shiftToTopLeft(
                                new Rect(10, 10, 10, 10), Utils.toPointsArray(shape.getPoints()))),
                Utils.getSimilarColor(shape.getColor(), Utils.ColorSuggestionStrategy.DARKER), shape.getSuggestionPath());

        return newShape;
    }

    @Override
    public int getDrawableIcon() {
        return R.drawable.copy;
    }
}
