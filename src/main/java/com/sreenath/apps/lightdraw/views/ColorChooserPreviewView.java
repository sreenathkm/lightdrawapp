package com.sreenath.apps.lightdraw.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import com.sreenath.apps.lightdraw.R;
import com.sreenath.apps.lightdraw.views.state.DrawableShape;

/**
 * Created by sreenath on 3/6/17.
 */
public class ColorChooserPreviewView extends View {

    @ColorInt
    private int color;

    public ColorChooserPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
        View rootView = ((Activity)getContext()).getWindow().getDecorView().findViewById(android.R.id.content);
        DrawableView drawableView = (DrawableView)rootView.findViewById(R.id.drawableView);

        DrawableShape drawableShape = drawableView.getCurrentSelectedShape();

        if (drawableShape == null) {
            drawableShape = drawableView.getBackgroundShape();
        }

        if (drawableShape != null) {
            drawableShape.drawPreview(canvas, getWidth() / 2, getHeight() / 2, color);
        }
    }

    public void setColor(@ColorInt final int color) {
        this.color = color;
    }


}
