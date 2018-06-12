package com.sreenath.apps.lightdraw.tool.impl;

import android.view.View;

import com.sreenath.apps.lightdraw.R;
import com.sreenath.apps.lightdraw.tool.Tool;
import com.sreenath.apps.lightdraw.views.DrawableView;
import com.sreenath.apps.lightdraw.views.ToolBoxView;

/**
 * Created by sreenath on 24/9/17.
 */
public class NewDrawingTool implements Tool {
    @Override
    public boolean click(ToolBoxView toolBoxView) {
        final DrawableView drawableView = (DrawableView)((View)(toolBoxView.getParent())).findViewById(R.id.drawableView);
        drawableView.newCanvas();

        return false;
    }

    @Override
    public int getDrawableIcon() {
        return R.drawable.new_drawing;
    }
}
