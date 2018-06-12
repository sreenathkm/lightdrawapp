package com.sreenath.apps.lightdraw.tool.impl;

import android.view.View;

import com.sreenath.apps.lightdraw.R;
import com.sreenath.apps.lightdraw.tool.Tool;
import com.sreenath.apps.lightdraw.views.DrawableView;
import com.sreenath.apps.lightdraw.views.ToolBoxView;

/**
 * Created by sreenath on 14/8/17.
 */
public class UndoTool implements Tool {
    @Override
    public boolean click(ToolBoxView toolBoxView) {
        DrawableView drawableView = (DrawableView)((View)(toolBoxView.getParent())).findViewById(R.id.drawableView);

        drawableView.undoTap();
        return false;
    }

    @Override
    public int getDrawableIcon() {
        return R.drawable.undo;
    }
}
