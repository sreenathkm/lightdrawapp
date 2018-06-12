package com.sreenath.apps.lightdraw.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Random;

/**
 * Created by sreenath on 14/8/17.
 */
public class Utils {

    public enum ColorSuggestionStrategy {
        DARKER,
        LIGHTER,
        LIGHTER2,
        DARKER2
    }

    private static final ColorSuggestionStrategy COLOR_SUGGESTION_STRATEGY_ROTATION[] = ColorSuggestionStrategy.values();

    private static final ColorSuggestionStrategy DEFAULT_COLOR_SUGGESTION_STRATEGY = ColorSuggestionStrategy.DARKER;

    private static final String IMAGE_FOLDER = "LightDraw-Images";

    public static File saveImage(final Bitmap bitmap, final String fileName) {
        File file,f;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            file = new File(android.os.Environment.getExternalStorageDirectory(), IMAGE_FOLDER);
            if(!file.exists()) {
                file.mkdirs();
            }
            f = new File(file.getAbsolutePath()+ File.separator + fileName);

            try (FileOutputStream ostream = new FileOutputStream(f)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
                ostream.close();

                return f;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public static Point[] toPointsArray(List<Point> points) {
        Point[] pointsArray = new Point[points.size()];
        points.toArray(pointsArray);

        return pointsArray;
    }

    public static @ColorInt int getSimilarColor(@ColorInt int color) {
        return getSimilarColor(color, COLOR_SUGGESTION_STRATEGY_ROTATION[new Random().nextInt(COLOR_SUGGESTION_STRATEGY_ROTATION.length)]);
    }

    public static @ColorInt int getSimilarColor(@ColorInt int color, ColorSuggestionStrategy strategy) {
        @ColorInt int similarColor =
                Color.rgb(
                adjustColorComponent(Color.red(color), strategy),
                adjustColorComponent(Color.green(color), strategy),
                adjustColorComponent(Color.blue(color), strategy)
        );

        return similarColor;
    }

    private static int adjustColorComponent(int colorComponent, ColorSuggestionStrategy strategy) {
        switch (strategy) {
            case LIGHTER: return lighten(colorComponent, 25);
            case DARKER: return darken(colorComponent, 25);
            case LIGHTER2: return lighten(colorComponent, 50);// keeping the same as lighter for now
            case DARKER2: return darken(colorComponent, 50);// keeping the same as darker for now

            default: return colorComponent;
        }
    }

    private static int lighten(int colorComponent, int delta) {
        if (colorComponent + delta > 255) {
            return colorComponent - delta;
        }

        return colorComponent + delta;
    }

    private static int darken(int colorComponent, int delta) {
        if (colorComponent - delta < 0) {
            return colorComponent + delta;
        }

        return colorComponent - delta;
    }

    public static void openImageFile(Context context, String fileName) {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File file = new File(android.os.Environment.getExternalStorageDirectory(), IMAGE_FOLDER + File.separator + fileName);

            MimeTypeMap map = MimeTypeMap.getSingleton();
            String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
            String type = map.getMimeTypeFromExtension("png");

            if (type == null) {
                type = "*/*";
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.fromFile(file);

            intent.setDataAndType(data, type);

            context.startActivity(intent);
        }
    }
}
