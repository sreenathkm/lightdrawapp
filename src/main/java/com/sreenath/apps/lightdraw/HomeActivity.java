package com.sreenath.apps.lightdraw;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.ads.MobileAds;

public class HomeActivity extends AppCompatActivity {

    private static final String DONT_SHOW_HELP_STARTUP_KEY = "DONT_SHOW_HELP_STARTUP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setUiVisibility();

        MobileAds.initialize(this, "ca-app-pub-3109420424273747~5982266992");

        startup();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            setUiVisibility();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private void setUiVisibility() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void startup() {
        final SharedPreferences settings = getSharedPreferences("LightDrawPrefs", 0);

        if (settings == null || !settings.getBoolean(DONT_SHOW_HELP_STARTUP_KEY, false)) {
            openStartupHelp(settings);
        }
    }

    private void openStartupHelp(final SharedPreferences settings) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (settings != null) {
                    final SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean(DONT_SHOW_HELP_STARTUP_KEY, true);
                    editor.commit();
                }
            }
        });

        alertBuilder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        alertBuilder.setTitle("Help")
                .setMessage(R.string.startup_help_message);

        alertBuilder.create().show();
    }
}
