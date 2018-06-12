package com.sreenath.apps.lightdraw.utils;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by sreenath on 28/12/16.
 */
public class PathUtils {

    public static Rect clipRectFrom(Point... points) {
        int topX = Integer.MAX_VALUE, topY = Integer.MAX_VALUE, rightX = 0, rightY = 0;

        for (Point point : points) {
            if (topX > point.x) {
                topX = point.x;
            } else if (rightX < point.x) {
                rightX = point.x;
            }

            if (topY > point.y) {
                topY = point.y;
            } else if (rightY < point.y) {
                rightY = point.y;
            }
        }

        return new Rect(topX, topY, rightX, rightY);

    }

    public static Rect roundToSquare(Rect rect) {
        int side =  Math.max(rect.width(), rect.height());

        return new Rect(rect.left, rect.top, rect.left + side, rect.top + side);
    }

    public static Rect clipSquareFrom(Point... points) {
        return roundToSquare(clipRectFrom(points));
    }

    public static Point[] scalePoints(float scale, Point... points) {
        Point[] scaledPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            Point scaledPoint = new Point((int)(scale * points[i].x), (int)(scale * points[i].y));

            scaledPoints[i] = scaledPoint;
        }
        return scaledPoints;
    }

    public static Point[] shiftToTopLeft(Rect squareContainer, Point... points) {

        Point[] shiftedPoints = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            Point shiftedPoint = new Point(points[i]);
            shiftedPoint.offset(-squareContainer.left, -squareContainer.top);

            shiftedPoints[i] = shiftedPoint;
        }
        return shiftedPoints;
    }

    public static Point[] passThroughCenter(double side, Point... points) {

        if (points.length != 2) {
            throw new IllegalArgumentException("Required exactly two points");
        }
        Point[] shiftedPoints = new Point[] {new Point(), new Point()};

        // Workaround to avoid divide-by-zero
        if (points[1].x == points[0].x) {
            points[1].x = points[1].x + 1;
        }

        final double slope = (double)(points[1].y - points[0].y) / (points[1].x - points[0].x);
        //final double slopeInDegrees = Math.abs(Math.toDegrees(slope));

        int shiftX0, shiftX1, shiftY0, shiftY1;

        if (Math.abs(slope) > 1) {
            shiftY0 = 0;
            shiftY1 = (int)side;

            shiftX0 = Math.round((float)(side / 2 - side / (2 * slope)));
            shiftX1 = Math.round((float)(side / 2 + slope / (side - side /2)));

        } else {
            shiftX0 = 0;
            shiftX1 = (int)side;

            shiftY0 = Math.round((float)(side / 2 - slope * side / 2));
            shiftY1 = Math.round((float)(side / 2 + slope * (side - side /2)));
        }

        if (points[0].x > points[1].x) {
            shiftedPoints[0].x = max(shiftX0, shiftX1);
            shiftedPoints[1].x = min(shiftX0, shiftX1);
        } else {
            shiftedPoints[1].x = max(shiftX0, shiftX1);
            shiftedPoints[0].x = min(shiftX0, shiftX1);
        }

        if (points[0].y > points[1].y) {
            shiftedPoints[0].y = max(shiftY0, shiftY1);
            shiftedPoints[1].y = min(shiftY0, shiftY1);
        } else {
            shiftedPoints[1].y = max(shiftY0, shiftY1);
            shiftedPoints[0].y = min(shiftY0, shiftY1);
        }

        return shiftedPoints;
    }

    private static int max(int a, int b) {
        return Math.max(a, b);
    }

    private static int min(int a, int b) {
        return Math.min(a, b);
    }

    public static Point[] cutDiameter(double side, Point... points) {
        if (points.length != 2) {
            throw new IllegalArgumentException("Required exactly two points");
        }
        Point[] shiftedPoints = new Point[] {new Point(), new Point()};

        // Workaround to avoid divide-by-zero
        if (points[1].x == points[0].x) {
            points[1].x = points[1].x + 1;
        }

        final double slope = (double)(points[1].y - points[0].y) / (points[1].x - points[0].x);

        double xsqrtBsquareMinus4ac = Math.sqrt(side * side / (1d + slope * slope ));
        double ysqrtBsquareMinus4ac = Math.sqrt(side * side / (1d + 1 / (slope * slope) ));

        if (points[0].x > points[1].x) {
            shiftedPoints[0].x = (int) Math.round((side + xsqrtBsquareMinus4ac) / 2);
            shiftedPoints[1].x = (int) Math.round((side - xsqrtBsquareMinus4ac) / 2);
        } else {
            shiftedPoints[1].x = (int) Math.round((side + xsqrtBsquareMinus4ac) / 2);
            shiftedPoints[0].x = (int) Math.round((side - xsqrtBsquareMinus4ac) / 2);
        }

        if (points[0].y > points[1].y) {
            shiftedPoints[0].y = (int) Math.round((side + ysqrtBsquareMinus4ac) / 2);
            shiftedPoints[1].y = (int) Math.round((side - ysqrtBsquareMinus4ac) / 2);
        } else {
            shiftedPoints[1].y = (int) Math.round((side + ysqrtBsquareMinus4ac) / 2);
            shiftedPoints[0].y = (int) Math.round((side - ysqrtBsquareMinus4ac) / 2);
        }

        return shiftedPoints;

    }

    public static Point[] moveToCenter(final int xCenter, final int yCenter, final Point[] points) {
        final Point centerOfGravity = MeasurementUtils.calculateCenterOfGravity(points);
        final Point[] movedPoints = new Point[points.length];

        int xDiff = xCenter- centerOfGravity.x;
        int yDiff = yCenter - centerOfGravity.y;

        for (int i = 0; i < points.length; i++) {
            movedPoints[i] = new Point(points[i].x + xDiff, points[i].y + yDiff);
        }

        return movedPoints;
    }
}
