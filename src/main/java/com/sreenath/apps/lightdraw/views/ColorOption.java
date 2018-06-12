package com.sreenath.apps.lightdraw.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.sreenath.apps.lightdraw.R;
import com.sreenath.apps.lightdraw.layouts.ColorChooserLayout;

/**
 * Created by sreenath on 31/5/17.
 */
public class ColorOption extends View implements View.OnTouchListener {

    public ColorOption(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ColorDrawable background = (ColorDrawable)this.getBackground();

        ColorChooserLayout colorChooser = (ColorChooserLayout)((LinearLayout)(getParent().getParent())).findViewById(R.id.progressBar);
        colorChooser.update(background.getColor());
        return false;
    }
}
