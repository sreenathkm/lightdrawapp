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
public class ZoomTool implements Tool {

    private boolean zoomOut = false;

    public ZoomTool(boolean zoomOut) {
        this.zoomOut = zoomOut;
    }

    @Override
    public boolean click(ToolBoxView toolBoxView) {
        DrawableView drawableView = (DrawableView)((View)(toolBoxView.getParent())).findViewById(R.id.drawableView);

        DrawableShape selectedShape = drawableView.getCurrentSelectedShape();

        if (selectedShape != null) {
            if (zoomOut) {
                selectedShape.zoomOut();
            } else {
                selectedShape.zoomIn();
            }

            drawableView.invalidate();
        }
        return false;
    }

    @Override
    public int getDrawableIcon() {
        return zoomOut ? R.drawable.zoom_out : R.drawable.zoom_in;
    }
}
