package com.sreenath.apps.lightdraw.views.state;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.ColorInt;

import com.sreenath.apps.lightdraw.enums.SuggestionPath;
import com.sreenath.apps.lightdraw.utils.MeasurementUtils;
import com.sreenath.apps.lightdraw.utils.PathUtils;
import com.sreenath.apps.lightdraw.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sreenath on 24/1/17.
 */
public class DrawableShape {
    private List<Point> points;
    private SuggestionPath suggestionPath;

    private static final float DEFAULT_ZOOM_PERCENTAGE = 1f;

    private static final int DEFAULT_ROTATION_ANGLE = 1;

    @ColorInt
    private int color;

    public DrawableShape(List<Point> points, @ColorInt int color, SuggestionPath suggestionPath) {
        this.suggestionPath = suggestionPath;
        this.color = color;

        setClonedPoints(points);
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public SuggestionPath getSuggestionPath() {
        return suggestionPath;
    }

    public void setSuggestionPath(SuggestionPath suggestionPath) {
        this.suggestionPath = suggestionPath;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
    }

    public @ColorInt int getColor() {
        return color;
    }

    public void drawPreview(Canvas canvas, int xCenter, int yCenter, @ColorInt int previewColor) {
        Point[] pointsArray = new Point[points.size()];
        points.toArray(pointsArray);

        Point[] movedPoints = PathUtils.moveToCenter(xCenter, yCenter, pointsArray);
        suggestionPath.draw(canvas, previewColor, movedPoints);
    }

    public void draw(Canvas canvas, boolean select) {
        Point[] pointsArray = new Point[points.size()];
        points.toArray(pointsArray);
        suggestionPath.draw(canvas, color, pointsArray);

        if (select) {
            Paint mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(0xFF000000);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            for (Point point : pointsArray) {
                canvas.drawCircle(point.x, point.y, 10, mPaint);
            }
        }
    }

    public void zoomIn() {
        zoomIn(DEFAULT_ZOOM_PERCENTAGE);
    }

    public void zoomIn(float percentage) {
        final Point centerOfGravity = MeasurementUtils.calculateCenterOfGravity(Utils.toPointsArray(this.points));

        for (Point point : this.points) {
            point.x = centerOfGravity.x + (int)((1f + percentage / 100) * (point.x - centerOfGravity.x));
            point.y = centerOfGravity.y + (int)((1f + percentage / 100) * (point.y - centerOfGravity.y));
        }
    }

    public void zoomOut() {
        zoomOut(DEFAULT_ZOOM_PERCENTAGE);
    }

    public void zoomOut(float percentage) {
        final Point centerOfGravity = MeasurementUtils.calculateCenterOfGravity(Utils.toPointsArray(this.points));

        for (Point point : this.points) {
            point.x = centerOfGravity.x + (int)((1f - percentage / 100) * (point.x - centerOfGravity.x));
            point.y = centerOfGravity.y + (int)((1f - percentage / 100) * (point.y - centerOfGravity.y));
        }
    }

    public void rotateClockwise() {
        rotate(DEFAULT_ROTATION_ANGLE);
    }

    public void rotateCounterClockwise() {
        rotate(-DEFAULT_ROTATION_ANGLE);
    }

    public void rotate(float angleInDegree) {
        final Point centerOfGravity = MeasurementUtils.calculateCenterOfGravity(Utils.toPointsArray(this.points));

        for (Point point : this.points) {
            final float newAngleInRads = (float)((angleInDegree) * Math.PI / 180);

            double sin = /*Math.abs*/(Math.sin(newAngleInRads));
            double cos = /*Math.abs*/(Math.cos(newAngleInRads));

            final int newX = (int)(cos * (point.x - centerOfGravity.x) - sin * (point.y - centerOfGravity.y)) + centerOfGravity.x;
            final int newY = (int)(sin * (point.x - centerOfGravity.x) + cos * (point.y - centerOfGravity.y)) + centerOfGravity.y;

            point.x = newX;
            point.y = newY;

        }

    }


    private void setClonedPoints(List<Point> points) {
        this.points = new ArrayList<>();

        for (Point point : points) {
            Point clonedPoint = new Point(point.x, point.y);
            this.points.add(clonedPoint);
        }
    }

    private Point[] creatingEnclosingPointsForLine() {
        Point point0 = points.get(0);
        Point point1 = points.get(1);
        return new Point[] {
                new Point(point0.x - 10, point0.y),
                new Point(point0.x + 10, point0.y),
                new Point(point1.x + 10, point1.y),
                new Point(point1.x - 10, point1.y)
        };
    }

    public boolean containsPoint(int x, int y) {
        Point[] pointsArray;
        if (points.size() == 2 && suggestionPath == SuggestionPath.FOLLOW_POINTS) {
            pointsArray = creatingEnclosingPointsForLine();
        } else {
            pointsArray = new Point[points.size()];
            points.toArray(pointsArray);
        }

        Path path = suggestionPath.createPath(pointsArray);

        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        Region region = new Region();
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));

        return region.contains(x, y);
    }
}
