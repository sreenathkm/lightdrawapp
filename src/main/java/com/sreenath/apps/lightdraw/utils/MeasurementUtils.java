package com.sreenath.apps.lightdraw.utils;

import android.content.Context;
import android.graphics.Point;

/**
 * Created by sreenath on 4/6/17.
 */
public class MeasurementUtils {

    public static int dpiToPixels(Context context, final int dpi) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dpi * scale + 0.5f);

        return pixels;
    }

    public static Point calculateCenterOfGravity(Point[] points) {
        int xSum = 0, ySum = 0;
        for (Point point : points) {
            xSum += point.x;
            ySum += point.y;
        }

        return new Point((int)Math.round(1d * xSum / points.length), (int)Math.round(1d * ySum / points.length));
    }

    public static float distance(float x1, float y1, float x2, float y2) {
        return (float)Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static float distance(Point p1, Point p2) {
        return distance(p1.x, p1.y, p2.x, p2.y);
    }

    public static int angle(float x1, float y1, float x2, float y2) {
        return (int)(Math.atan((y2 - y1) / (x2 - x1)) * 180 / Math.PI);
    }
}
