package com.sreenath.apps.lightdraw.enums;

import com.sreenath.apps.lightdraw.tool.Tool;
import com.sreenath.apps.lightdraw.tool.impl.AboutTool;
import com.sreenath.apps.lightdraw.tool.impl.ColorChooserTool;
import com.sreenath.apps.lightdraw.tool.impl.CopyShapeTool;
import com.sreenath.apps.lightdraw.tool.impl.DeleteTool;
import com.sreenath.apps.lightdraw.tool.impl.HelpTool;
import com.sreenath.apps.lightdraw.tool.impl.ImageSaverTool;
import com.sreenath.apps.lightdraw.tool.impl.NewDrawingTool;
import com.sreenath.apps.lightdraw.tool.impl.RotateTool;
import com.sreenath.apps.lightdraw.tool.impl.UndoTool;
import com.sreenath.apps.lightdraw.tool.impl.ZoomTool;
import com.sreenath.apps.lightdraw.views.ToolBoxView;

/**
 * Created by sreenath on 18/5/17.
 */
public enum ToolWrapper {

    COLOR_PICKER(new ColorChooserTool()),
    IMAGE_SAVER(new ImageSaverTool()),
    ROTETE_SHAPE_CW(new RotateTool(false)),
    ROTATE_SHAPE_CCW(new RotateTool(true)),
    DELETE(new DeleteTool()),
    ZOOM_IN(new ZoomTool(false)),
    ZOOM_OUT(new ZoomTool(true)),
    HELP(new HelpTool()),
    NEW_DRAWING(new NewDrawingTool()),
    UNDO(new UndoTool()),
    ABOUT(new AboutTool()),
    COPY(new CopyShapeTool());

    private Tool tool;

    private ToolWrapper(Tool tool) {
        this.tool = tool;
    }

    public boolean click(ToolBoxView toolBoxView) {
        return this.tool.click(toolBoxView);
    }

    public int getDrawableIcon() {
        return tool.getDrawableIcon();
    }


}
