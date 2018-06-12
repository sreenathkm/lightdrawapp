package com.sreenath.apps.lightdraw.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.sreenath.apps.lightdraw.R;
import com.sreenath.apps.lightdraw.enums.SuggestionPath;
import com.sreenath.apps.lightdraw.utils.PathUtils;
import com.sreenath.apps.lightdraw.views.state.DrawableShape;

/**
 * Created by sreenath on 23/12/16.
 */
public class SuggestionsView extends RelativeLayout {

    private int colors[] = {0xFFDED9E9, 0xFFDED2EA};

    int width, height, totalSuggstions, totalPossibleSuggestions;

    private Point[] scaledPoints;

    private boolean suggestionsEnabled = true;

    public synchronized void suggest(Point... points) {
        suggestionsEnabled = true;
        Rect square = PathUtils.clipSquareFrom(points);
        float scale = Math.abs((float)height / square.width());

        Point[] shiftedPoints = PathUtils.shiftToTopLeft(square, points);
        scaledPoints = PathUtils.scalePoints(scale, shiftedPoints);

        if (points.length == 2) {
            scaledPoints = PathUtils.cutDiameter(height,
                    PathUtils.passThroughCenter(height, scaledPoints)
            )
            ;
        }

        refresh(points.length);

        invalidate();
    }

    public void enableSuggestions() {
        suggestionsEnabled = true;
    }

    public void disableSuggestions() {
        suggestionsEnabled = false;
    }

    public boolean canShowSuggestions() {
        return suggestionsEnabled;
    }

    public boolean canShowHelpText() {
        return !suggestionsEnabled;
    }

    public SuggestionsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        initialize();

        super.onLayout(changed, l, t, r, b);
    }

    public void reset() {
        removeAllViews();
    }

    public void refresh(final int pointsCount) {
        reset();

        final SuggestionPath[] suggestions = getSuggestionsForCount(pointsCount);

        for (SuggestionPath suggestionPath : suggestions) {
            addSuggestion(suggestionPath);
        }
    }

    private SuggestionPath[] getSuggestionsForCount(final int count) {
        switch (count) {
            case 1: return new SuggestionPath[] {};

            case 2: return new SuggestionPath[] {SuggestionPath.CIRCLE, SuggestionPath.FOLLOW_POINTS,
                    SuggestionPath.CIRCLE_FILLED, SuggestionPath.TWO_POINT_ARC_CW,
                    SuggestionPath.TWO_POINT_ARC_CCW,
                    SuggestionPath.TWO_POINT_SEMICIRCLE_FILLED_CW,
                    SuggestionPath.TWO_POINT_SEMICIRCLE_FILLED_CCW
            };

            default:
                return new SuggestionPath[] {SuggestionPath.FOLLOW_POINTS, SuggestionPath.CLOSED_LOOP, SuggestionPath.CLOSED_LOOP_FILLED};

        }
    }

    public void onDraw(Canvas canvas) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).invalidate();
        }
    }

    private void initialize() {
        width = getWidth();
        height = getHeight();

        totalPossibleSuggestions = width / height;
    }

    private synchronized void addSuggestion(final SuggestionPath suggestionPath) {
        LayoutParams layoutParams = new LayoutParams(height, height);

        totalSuggstions++;

        if (totalSuggstions > 0) {
            layoutParams.addRule(RelativeLayout.RIGHT_OF, totalSuggstions - 1);
        } else {
            layoutParams.addRule(RelativeLayout.RIGHT_OF);
        }
        Suggestion suggestion = createSuggestion(totalSuggstions);
        suggestion.setSuggestionPath(suggestionPath);

        addView(suggestion, layoutParams);
    }

    private Suggestion createSuggestion(int count) {
        Suggestion suggestion = new Suggestion(getContext(), count);
        return suggestion;
    }

    public class Suggestion extends View implements View.OnTouchListener {

        private SuggestionPath suggestionPath;

        public Suggestion(Context context, int id) {
            super(context);
            setId(id);
            setOnTouchListener(this);
        }

        public void setSuggestionPath(SuggestionPath suggestionPath) {
            this.suggestionPath = suggestionPath;
        }

        public void onDraw(Canvas canvas) {
            if (SuggestionsView.this.canShowSuggestions()) {
                drawSuggestions(canvas);
            } else {
                showHelpText(canvas);
            }
        }

        private void showHelpText(Canvas canvas) {
            // not implemented
            setOnTouchListener(null);
        }

        private void drawSuggestions(final Canvas canvas) {
            if (scaledPoints != null) {
                suggestionPath.draw(canvas, 0xff888888, scaledPoints);

            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            DrawableView drawableView = (DrawableView)((View)(SuggestionsView.this.getParent())).findViewById(R.id.drawableView);
            final DrawableShape currentShape = drawableView.getCurrentSelectedShape();

            if (currentShape != null) {
                currentShape.setSuggestionPath(suggestionPath);
            } else {
                drawableView.addShape(suggestionPath);
            }

            SuggestionsView.this.disableSuggestions();
            SuggestionsView.this.invalidate();
            drawableView.invalidate();
            return false;
        }

    }
}
