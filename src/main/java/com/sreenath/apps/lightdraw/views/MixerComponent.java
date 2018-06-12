package com.sreenath.apps.lightdraw.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sreenath.apps.lightdraw.utils.MeasurementUtils;

/**
 * Created by sreenath on 31/5/17.
 */
public class MixerComponent extends View {

    @ColorInt private int base = 0x00;

    private int value;

    private final int shiftPositions;

    public MixerComponent(final Context context, final int shiftPositions, final OnTouchListener onTouchListener) {
        super(context);
        this.shiftPositions = shiftPositions;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
        layoutParams.topMargin = MeasurementUtils.dpiToPixels(getContext(), 16);
        layoutParams.bottomMargin = MeasurementUtils.dpiToPixels(getContext(), 16);
        layoutParams.weight = 1;
        setLayoutParams(layoutParams);

        setOnTouchListener(onTouchListener);
        set(124);

        setBackgroundColor(Color.WHITE);
    }

    public void set(final int newColor) {
        this.value = newColor;
    }

    public int getColor() {
        return value << shiftPositions;
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor((value << shiftPositions) | 0xFF000000);

        canvas.drawRect(0, 0, getWidthProportionalToColor(), getHeight(), paint);
    }

    private int getWidthProportionalToColor() {
        return (int)((float)value / 255f * getWidth());
    }

    public static class OpacityComponent extends MixerComponent {

        public OpacityComponent(Context context, OnTouchListener onTouchListener) {
            super(context, 24, onTouchListener);

            set(255);
            setBackgroundColor(Color.GRAY);
        }
    }

}
