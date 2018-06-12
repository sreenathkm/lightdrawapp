package com.sreenath.apps.lightdraw.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sreenath.apps.lightdraw.R;
import com.sreenath.apps.lightdraw.enums.SuggestionPath;
import com.sreenath.apps.lightdraw.utils.MeasurementUtils;
import com.sreenath.apps.lightdraw.utils.PathUtils;
import com.sreenath.apps.lightdraw.utils.Utils;
import com.sreenath.apps.lightdraw.views.state.DrawableShape;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sreenath on 17/12/16.
 */
public class DrawableView extends View {

    private enum TouchEvent {
        DRAG,
        SELECT,
        ZOOM_OR_ROTATE,
        ZOOM_IN,
        ZOOM_OUT,
        ROTATE_CLOCKWISE,
        ROTATE_COUNTER_CLOCKWISE
    }

    private DrawableShape backgroundShape;

    private final int TOAST_MESSAGE_DURATIONS[] = {Toast.LENGTH_LONG, Toast.LENGTH_LONG, Toast.LENGTH_LONG, Toast.LENGTH_SHORT, Toast.LENGTH_SHORT, Toast.LENGTH_SHORT, Toast.LENGTH_SHORT, Toast.LENGTH_SHORT, Toast.LENGTH_SHORT, Toast.LENGTH_SHORT};

    private int toastMessageDurationIndex = 0;

    private int startAngle, endAngle;
    private float startDistanceZoom, endDistanceZoom;

    private TouchEvent touchEvent;

    private List<Point> points = new ArrayList<>();

    private List<DrawableShape> shapes = new ArrayList<>();

    private boolean moveMode = false;

    private boolean startedGesture = false;

    private GestureDetector gestureDetector;

    private DrawableShape currentSelectedShape;

    private float moveStartX, moveStartY;

    private EDGestureListener edGestureListener = new EDGestureListener();

    private String exportFileName;

    public synchronized DrawableShape getBackgroundShape() {
        if (backgroundShape == null) {
            int width = getWidth();
            int height = getHeight();

            backgroundShape = new DrawableShape(Arrays.asList(new Point[]{
                    new Point(0, 0),
                    new Point(width, 0),
                    new Point(width, height),
                    new Point(0, height)
            }), 0xffffff,
                    SuggestionPath.CLOSED_LOOP_FILLED
            );
        }

        return backgroundShape;
    }

    public DrawableView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        gestureDetector = new GestureDetector(context, edGestureListener);


        refreshListeners();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        actDrag(event);
        gestureDetector.onTouchEvent(event);// move comes here
        super.onTouchEvent(event);

        return true;
    }

    private DrawableShape findAdjacentShape(int x, int y, int delta) {
        Point point = null;
        int[][] xyApproxs = new int[][] {
                {x, y}, {x, y + delta},
                {x + delta, y}, {x + delta, y + delta},
                {x, y  - delta},
                {x - delta, y}, {x - delta, y - delta},
        };

        // If there is atleast one shape to attach
        if (shapes.size() > 0) {
            for (DrawableShape shape : shapes) {
                for (int []xyApprox : xyApproxs) {
                    if (shape.containsPoint(xyApprox[0],xyApprox[1])) {
                        return shape;
                    }
                }
            }
        }

        return null;
    }

    public synchronized void saveCanvas() {
        currentSelectedShape = null;
        invalidate();
        setDrawingCacheEnabled(true);
        Bitmap bitmap = getDrawingCache();

        if (exportFileName == null) {
            exportFileName = "IMG_ECD_"+ System.currentTimeMillis() + ".png";
        }

        final File savedFile = Utils.saveImage(bitmap, exportFileName);

        setDrawingCacheEnabled(false);

        if (savedFile != null) {
            MediaScannerConnection.scanFile(getContext(), new String[]{savedFile.getPath()}, new String[]{"image/png"}, null);

            showSaveFileDialog();
        }
    }

    private void showSaveFileDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.message_save_notification)
                .setTitle(R.string.title_save_notification);

        builder.setPositiveButton("Open File", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Utils.openImageFile(getContext(), exportFileName);

            }
        });

        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();
    }

    private void showNewFileDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.message_save_before_new_canvas)
                .setTitle(R.string.title_save_before_new_canvas);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                saveCanvas();
                initNewCanvas();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                initNewCanvas();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();
    }

    public synchronized void newCanvas() {
        if (shapes.size() != 0) {
            showNewFileDialog();
        } else {
            initNewCanvas();
        }
    }

    private void initNewCanvas() {
        exportFileName = null;
        shapes.clear();
        points.clear();
        invalidate();
    }

    public String getExportFileName() {
        return exportFileName;
    }

    public void setExportFileName(String exportFileName) {
        this.exportFileName = exportFileName;
    }

    private void addPoint(MotionEvent event) {
        Point point = new Point((int)event.getX(), (int)event.getY());

        if (!points.contains(point)) {
            points.add(point);
        }

        generateNewSuggestions();

    }

    private void generateNewSuggestions() {
        Point pointsArray[] = new Point[points.size()];

        points.toArray(pointsArray);

        SuggestionsView suggestionsView = (SuggestionsView)((View)(this.getParent())).findViewById(R.id.suggestionsView);

        suggestionsView.suggest(pointsArray);

        invalidate();
    }

    private boolean actTouch(MotionEvent event) {
        if (!moveMode) {
            addPoint(event);
            updateCurrentSelectedShapeAndInvalidate(null);
        } else {
            /*final DrawableShape selectedShape = findSelectedShape((int)event.getX(), (int)event.getY());
            updateCurrentSelectedShapeAndInvalidate(selectedShape);*/
        }

        return true;
    }

    public void deleteSelectedShape() {
        if (currentSelectedShape != null) {
            shapes.remove(currentSelectedShape);
            updateCurrentSelectedShapeAndInvalidate(null);
        }
    }

    private void updateCurrentSelectedShapeAndInvalidate(DrawableShape shape) {
        this.currentSelectedShape = shape;
        final boolean selected = currentSelectedShape != null && touchEvent == TouchEvent.SELECT;
        getToolBoxView().updateTools(selected);
        invalidate();

        SuggestionsView suggestionsView = (SuggestionsView)((View)(this.getParent())).findViewById(R.id.suggestionsView);

        if (selected) {
            suggestionsView.suggest(Utils.toPointsArray(shape.getPoints()));
        } else {
            suggestionsView.suggest(Utils.toPointsArray(points));
        }
    }

    private ToolBoxView getToolBoxView() {
        return (ToolBoxView)((LinearLayout)getParent()).findViewById(R.id.toolBoxView);
    }

    private boolean actDrag(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                if (startedGesture) {
                    switch (touchEvent) {
                        case DRAG:
                            keepMoving(event);
                            break;
                        case ZOOM_OR_ROTATE:
                        case ZOOM_IN:
                        case ZOOM_OUT:
                        case ROTATE_CLOCKWISE:
                        case ROTATE_COUNTER_CLOCKWISE:
                            zoomOrRotate(event);
                            break;
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                stopMoving(event);
                break;

            case MotionEvent.ACTION_DOWN:
                startMoving(event);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                startZoomOrRotate(event);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                stopZoomOrRotate(event);
                break;
        }
        return true;
    }

    private void startZoomOrRotate(MotionEvent event) {
        touchEvent = TouchEvent.ZOOM_OR_ROTATE;
        startedGesture = true;

        startAngle = MeasurementUtils.angle(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
        startDistanceZoom = MeasurementUtils.distance(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
    }

    private void stopZoomOrRotate(MotionEvent event) {
        touchEvent = null;
        startedGesture = false;
    }

    private TouchEvent guessZoomOrRotateEvent(MotionEvent event) {
        final float endAngle = MeasurementUtils.angle(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
        final float endDistanceZoom = MeasurementUtils.distance(event.getX(0), event.getY(0), event.getX(1), event.getY(1));

        TouchEvent touchEvent = TouchEvent.ZOOM_OR_ROTATE;

        if (currentSelectedShape != null) {
            if (Math.abs(startAngle - endAngle) < 5 && Math.abs(startDistanceZoom - endDistanceZoom) > 20) {
                if (startDistanceZoom < endDistanceZoom) {
                    touchEvent = TouchEvent.ZOOM_IN;
                } else {
                    touchEvent = TouchEvent.ZOOM_OUT;
                }
            } else if (Math.abs(startAngle - endAngle) > 15) {
                if (startAngle > endAngle) {
                    touchEvent = TouchEvent.ROTATE_COUNTER_CLOCKWISE;
                } else if (startAngle < endAngle) {
                    touchEvent = TouchEvent.ROTATE_CLOCKWISE;
                }
            }
        }

        return touchEvent;
    }

    private void zoomOrRotate(MotionEvent event) {

        if (touchEvent == TouchEvent.ZOOM_OR_ROTATE) {
            touchEvent = guessZoomOrRotateEvent(event);
        }

        if (currentSelectedShape != null) {
            switch (touchEvent) {
                case ZOOM_IN:
                    currentSelectedShape.zoomIn();
                    break;
                case ZOOM_OUT:
                    currentSelectedShape.zoomOut();
                    break;
                case ROTATE_CLOCKWISE:
                    currentSelectedShape.rotateClockwise();
                    break;
                case ROTATE_COUNTER_CLOCKWISE:
                    currentSelectedShape.rotateCounterClockwise();
                    break;
            }
        }
        invalidate();
    }

    private void startMoving(MotionEvent event) {
        /*if (!moveMode) {
            return;
        }*/
        startedGesture = true;
        touchEvent = TouchEvent.DRAG;
        moveStartPoint(event);
        updateCurrentSelectedShapeAndInvalidate(findSelectedShape((int)moveStartX, (int)moveStartY));
    }

    private void moveStartPoint(MotionEvent event) {
        moveStartX = event.getX();
        moveStartY = event.getY();
    }

    private void stopMoving(MotionEvent event) {
        startedGesture = false;
        touchEvent = null;
    }

    public void undoTap() {
        if (!points.isEmpty()) {
            points.remove(points.size() - 1);
            generateNewSuggestions();
        }
    }

    private void keepMoving(MotionEvent event) {
        if (currentSelectedShape != null) {
            float newX = event.getX();
            float newY = event.getY();

            Rect rect = new Rect((int)(moveStartX - newX), (int)(moveStartY - newY), getWidth(), getHeight());

            Point[] pointsArray = new Point[currentSelectedShape.getPoints().size()];

            currentSelectedShape.getPoints().toArray(pointsArray);

            Point[] newPoints = PathUtils.shiftToTopLeft(rect, pointsArray);

            ArrayList<Point> newList = new ArrayList<>();

            for (Point point : newPoints) {
                newList.add(point);
            }

            currentSelectedShape.setPoints(newList);

            invalidate();

            moveStartPoint(event);
        }
    }

    private DrawableShape findSelectedShape(int posX, int posY) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            final DrawableShape drawableShape = shapes.get(i);
            if (drawableShape.containsPoint(posX, posY)) {
                return drawableShape;
            }
        }

        return findAdjacentShape(posX, posY, 50);
    }

    public void addShape(SuggestionPath suggestionPath) {
        @ColorInt int newColor = suggestColorForNewShape();
        DrawableShape shape = new DrawableShape(points, newColor, suggestionPath);
        shapes.add(shape);

        points = new ArrayList<>();
    }

    public void addShape(final DrawableShape shape) {
        shapes.add(shape);
    }

    private @ColorInt int suggestColorForNewShape() {
        @ColorInt  int color = Color.GRAY;

        DrawableShape adjacentShape = null;
        for (Point point : points) {
            adjacentShape = findAdjacentShape(point.x, point.y, 20);

            if (adjacentShape == null) {
                adjacentShape = findAdjacentShape(point.x, point.y, 200);
            }

            if (adjacentShape != null) {
                break;
            }
        }

        if (adjacentShape != null) {
            color = Utils.getSimilarColor(
                    adjacentShape.getColor());
        }

        return color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Point pointsArray[] = new Point[points.size()];

        points.toArray(pointsArray);

        //Rect clipRect = PathUtils.clipRectFrom(pointsArray);

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF000000);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        if (backgroundShape != null) {
            backgroundShape.draw(canvas, false);
        }

        for (DrawableShape shape : shapes) {
            final boolean drawSelection = touchEvent == TouchEvent.SELECT && currentSelectedShape == shape;
            shape.draw(canvas, drawSelection);
        }

        for (Point point : points) {
            canvas.drawCircle(point.x, point.y, 10, mPaint);
        }
    }

    public DrawableShape getCurrentSelectedShape() {
        return currentSelectedShape;
    }


    private void toggleMoveMode() {
        /*moveMode = !moveMode;

        showModeChangedMessage();

        points.clear();
        refreshListeners();
        invalidate();*/
    }

    private void showModeChangedMessage() {
        final String message = moveMode ? getResources().getString(R.string.message_switched_to_select) :
                getResources().getString(R.string.message_switched_to_draw);

        Toast.makeText(getContext(), message, TOAST_MESSAGE_DURATIONS[toastMessageDurationIndex]).show();

        toastMessageDurationIndex = (toastMessageDurationIndex + 1) % TOAST_MESSAGE_DURATIONS.length;
    }

    @Override
    public boolean onDragEvent(DragEvent dragEvent) {
        return false;
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return true;
    }

    private void refreshListeners() {
        boolean isLongPressEnabled = !moveMode;

        gestureDetector.setIsLongpressEnabled(isLongPressEnabled);
    }

    private class EDGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            touchEvent = TouchEvent.SELECT;
            updateCurrentSelectedShapeAndInvalidate(findSelectedShape((int)event.getX(), (int)event.getY()));
            /*if (moveMode) {
                toggleMoveMode();
            }*/

            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            return actTouch(event);
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            //toggleMoveMode();
        }

    }
}
