package ru.astar.locwidget;

import android.app.Activity;
import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by molot on 29.05.2017.
 */

public class WidgetConfig extends Activity implements OnClickListener {

    private int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Intent rIntent = null;
    public static final String WIDGET_PREF = "widget_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_config);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null)
            widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        rIntent = new Intent();
        rIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_CANCELED);
    }

    @Override
    public void onClick(View view) {
        if (view == null) {
            finish();
            return;
        }

        int color = Color.parseColor(view.getTag().toString());
        SharedPreferences options = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = options.edit();
        editor.putInt(String.valueOf(widgetId), color);
        editor.commit();

        setResult(RESULT_OK, rIntent);
        finish();
    }
}
