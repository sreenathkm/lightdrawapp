package com.sreenath.apps.lightdraw.tool;

import com.sreenath.apps.lightdraw.views.ToolBoxView;

/**
 * Created by sreenath on 14/8/17.
 */
public interface Tool {
    public boolean click(ToolBoxView toolBoxView);

    public int getDrawableIcon();
}
