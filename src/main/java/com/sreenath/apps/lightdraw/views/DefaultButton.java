package com.sreenath.apps.lightdraw.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by sreenath on 24/9/17.
 */
public class DefaultButton extends Button {
    public DefaultButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        setTransformationMethod(null);

        setBackgroundColor(0xff5555ff);
        setTextColor(Color.WHITE);
    }
}
