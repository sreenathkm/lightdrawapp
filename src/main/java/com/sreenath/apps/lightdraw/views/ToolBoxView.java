package com.sreenath.apps.lightdraw.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.sreenath.apps.lightdraw.enums.ToolWrapper;

/**
 * Created by sreenath on 23/12/16.
 */
public class ToolBoxView extends RelativeLayout {

    private int toolCount = 0;

    private int width, height;

    private PopupWindow popupWindow;
    public ToolBoxView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        initialize();

        super.onLayout(changed, l, t, r, b);
    }

    private void initialize() {

        width = getWidth();
        height = getHeight();

        addTools(getTools(false));

    }

    public void updateTools(final boolean selected) {
        reset();
        addTools(getTools(selected));
        invalidate();
    }

    private ToolWrapper[] getTools(final boolean selected) {
        return selected ?
            new ToolWrapper[]{ToolWrapper.COLOR_PICKER, ToolWrapper.IMAGE_SAVER, ToolWrapper.ROTETE_SHAPE_CW,
                    ToolWrapper.ROTATE_SHAPE_CCW, ToolWrapper.DELETE, ToolWrapper.ZOOM_IN, ToolWrapper.ZOOM_OUT, ToolWrapper.COPY} :
            new ToolWrapper[]{ToolWrapper.COLOR_PICKER, ToolWrapper.NEW_DRAWING, ToolWrapper.IMAGE_SAVER, ToolWrapper.UNDO, ToolWrapper.HELP, ToolWrapper.ABOUT};

    }

    public void reset() {
        removeAllViews();
    }

    private void addTools(final ToolWrapper[] tools) {
        if (tools != null) {
            for (int i = 0; i < tools.length; i++) {
                LayoutParams layoutParams = new LayoutParams(height, height);
                layoutParams.addRule(RelativeLayout.RIGHT_OF, i);

                addView(new ToolView(getContext(), i + 1, tools[i]), layoutParams);
            }
        }
    }

    public class ToolView extends AppCompatButton implements View.OnClickListener {

        private ToolWrapper toolWrapper;

        public ToolView(Context context, int id, ToolWrapper toolWrapper) {
            super(context);
            setId(id);

            setOnClickListener(this);
            this.toolWrapper = toolWrapper;

            setBackgroundColor(Color.TRANSPARENT);

            setCompoundDrawablesWithIntrinsicBounds(toolWrapper.getDrawableIcon(), 0, 0, 0);

        }

        @Override
        public void onClick(View v) {
            toolWrapper.click(ToolBoxView.this);
        }
    }
}

