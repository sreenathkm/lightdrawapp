package com.sreenath.apps.lightdraw.tool.impl;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import com.sreenath.apps.lightdraw.R;
import com.sreenath.apps.lightdraw.tool.Tool;
import com.sreenath.apps.lightdraw.views.ToolBoxView;

/**
 * Created by sreenath on 6/10/17.
 */
public class AboutTool implements Tool {
    @Override
    public boolean click(ToolBoxView toolBoxView) {
        {
            final ScrollView linearLayout = (ScrollView)LayoutInflater.from(toolBoxView.getContext()).inflate(R.layout.about, null);
            WebView webView = (WebView)linearLayout.findViewById(R.id.aboutWeb);

            final PopupWindow popupWindow = new PopupWindow(linearLayout, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            popupWindow.showAtLocation(toolBoxView, Gravity.NO_GRAVITY, 0, 0);
            popupWindow.setTouchable(true);
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(false);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

            webView.loadUrl("file:///android_asset/about.html");

            Button ok = (Button) linearLayout.findViewById(R.id.aboutOk);
            ok.setOnClickListener(new View.OnClickListener() {

                public void onClick(View popupView) {
                    popupWindow.dismiss();
                }
            });
            return false;
        }
    }

    @Override
    public int getDrawableIcon() {
        return R.drawable.about;
    }
}
