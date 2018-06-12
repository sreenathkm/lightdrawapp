package com.sreenath.apps.lightdraw.tool.impl;

import android.view.View;

import com.sreenath.apps.lightdraw.R;
import com.sreenath.apps.lightdraw.tool.Tool;
import com.sreenath.apps.lightdraw.views.DrawableView;
import com.sreenath.apps.lightdraw.views.ToolBoxView;
import com.sreenath.apps.lightdraw.views.state.DrawableShape;

/**
 * Created by sreenath on 10/9/17.
 */
public class RotateTool implements Tool {

    private boolean counterClockwise = false;

    public RotateTool(boolean counterClockwise) {
        this.counterClockwise = counterClockwise;
    }

    @Override
    public boolean click(ToolBoxView toolBoxView) {

        DrawableView drawableView = (DrawableView)((View)(toolBoxView.getParent())).findViewById(R.id.drawableView);

        DrawableShape selectedShape = drawableView.getCurrentSelectedShape();

        if (selectedShape != null) {
            if (counterClockwise) {
                selectedShape.rotateCounterClockwise();
            } else {
                selectedShape.rotateClockwise();
            }
        }

        drawableView.invalidate();

        return true;
    }

    @Override
    public int getDrawableIcon() {
        return counterClockwise ? R.drawable.rotate_ccw : R.drawable.rotate_cw;
    }


}
