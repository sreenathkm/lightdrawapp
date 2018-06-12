package com.sreenath.apps.lightdraw.tool.impl;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.sreenath.apps.lightdraw.R;
import com.sreenath.apps.lightdraw.layouts.ColorChooserLayout;
import com.sreenath.apps.lightdraw.tool.Tool;
import com.sreenath.apps.lightdraw.views.DrawableView;
import com.sreenath.apps.lightdraw.views.ToolBoxView;
import com.sreenath.apps.lightdraw.views.state.DrawableShape;

/**
 * Created by sreenath on 14/8/17.
 */
public class ColorChooserTool implements Tool {
    @Override
    public boolean click(final ToolBoxView toolBoxView) {
        final View view = LayoutInflater.from(toolBoxView.getContext()).inflate(R.layout.color_picker, null);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        popupWindow.showAtLocation(toolBoxView, Gravity.NO_GRAVITY, 0, 0);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);

        Button close = (Button) view.findViewById(R.id.cancelButton);
        close.setOnClickListener(new View.OnClickListener() {

            public void onClick(View popupView) {
                popupWindow.dismiss();
            }
        });

        Button ok = (Button) view.findViewById(R.id.okButton);
        ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View popupView) {
                ColorChooserLayout colorChooser = (ColorChooserLayout)view.findViewById(R.id.progressBar);
                DrawableView drawableView = (DrawableView)((View)(toolBoxView.getParent())).findViewById(R.id.drawableView);

                final int selectedColor = colorChooser.getColor();
                DrawableShape drawableShape = drawableView.getCurrentSelectedShape();

                if (drawableShape == null) {
                    drawableShape = drawableView.getBackgroundShape();
                }

                drawableShape.setColor(selectedColor);

                drawableView.invalidate();
                popupWindow.dismiss();
            }
        });

        updateColorChooser(view, toolBoxView);

        return true;
    }

    @Override
    public int getDrawableIcon() {
        return R.drawable.color_chooser;
    }

    private void updateColorChooser(final View popupView, final ToolBoxView toolBoxView) {
        ColorChooserLayout colorChooser = (ColorChooserLayout)popupView.findViewById(R.id.progressBar);
        DrawableView drawableView = (DrawableView)((View)(toolBoxView.getParent())).findViewById(R.id.drawableView);

        DrawableShape shape = drawableView.getCurrentSelectedShape();

        if (shape != null) {
            colorChooser.update(shape.getColor());
        }
    }
}
