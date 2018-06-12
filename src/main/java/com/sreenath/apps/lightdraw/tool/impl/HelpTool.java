package com.sreenath.apps.lightdraw.tool.impl;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.sreenath.apps.lightdraw.R;
import com.sreenath.apps.lightdraw.tool.Tool;
import com.sreenath.apps.lightdraw.views.ToolBoxView;

/**
 * Created by sreenath on 21/9/17.
 */
public class HelpTool implements Tool {
    @Override
    public boolean click(ToolBoxView toolBoxView) {
        final View view = LayoutInflater.from(toolBoxView.getContext()).inflate(R.layout.help, null);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        popupWindow.showAtLocation(toolBoxView, Gravity.NO_GRAVITY, 0, 0);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);

        Button ok = (Button) view.findViewById(R.id.okHelp);
        ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View popupView) {
                popupWindow.dismiss();
            }
        });
        return false;
    }

    @Override
    public int getDrawableIcon() {
        return R.drawable.help;
    }
}
