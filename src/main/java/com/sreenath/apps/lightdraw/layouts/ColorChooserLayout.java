package com.sreenath.apps.lightdraw.layouts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.sreenath.apps.lightdraw.R;
import com.sreenath.apps.lightdraw.views.ColorChooserPreviewView;
import com.sreenath.apps.lightdraw.views.MixerComponent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by sreenath on 2/6/17.
 */
public class ColorChooserLayout extends LinearLayout implements View.OnTouchListener {


    private MixerComponent redComponent, greenComponent, blueComponent, opacityComponent;
    public ColorChooserLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(LinearLayout.VERTICAL);

        redComponent = new MixerComponent(context, 16, this);
        greenComponent = new MixerComponent(context, 8, this);
        blueComponent = new MixerComponent(context, 0, this);
        opacityComponent = new MixerComponent.OpacityComponent(context, this);

        addView(redComponent);
        addView(greenComponent);
        addView(blueComponent);
        addView(opacityComponent);

    }



    public int getColor() {
        return redComponent.getColor() | greenComponent.getColor() | blueComponent.getColor() | opacityComponent.getColor();
    }

    public void update(@ColorInt int color) {
        redComponent.set(Color.red(color));
        greenComponent.set(Color.green(color));
        blueComponent.set(Color.blue(color));
        opacityComponent.set(Color.alpha(color));

        updatePreview();

        invalidate();
    }

    private void updatePreview() {
        ColorChooserPreviewView previewView = (ColorChooserPreviewView)((View)(getParent())).findViewById(R.id.previewView);
        previewView.setColor(getColor());
        previewView.invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        MixerComponent mixerComponent = (MixerComponent)view;
        mixerComponent.set((int)(event.getX() * 255f / getWidth()));
        mixerComponent.invalidate();

        updatePreview();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        redComponent.invalidate();
        greenComponent.invalidate();
        blueComponent.invalidate();
        opacityComponent.invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        AdView adView = (AdView) ((View)(getParent())).findViewById(R.id.bannerAd);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("********************")
                .build();

        adView.loadAd(adRequest);
    }
}
