package ru.astar.locwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.widget.RemoteViews;

import java.util.Timer;
import java.util.TimerTask;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by molot on 29.05.2017.
 */

public class AppWidget extends AppWidgetProvider {
    private AppWidgetManager widgetManager = null;
    int[] widgetIds = null;

    // для местоположения
    private Timer timer = null;
    private MyLocation myLocation;
    private String locationString = null;

    private Context context = null;
    SharedPreferences options = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        this.context = context;
        options = context.getSharedPreferences(WidgetConfig.WIDGET_PREF, Context.MODE_PRIVATE);
        widgetManager = appWidgetManager;
        widgetIds = appWidgetIds.clone();

        // создаем объект класса для определения местоположения
        if (myLocation == null)
            myLocation = new MyLocation(context);

        if (timer == null) {
            timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    updateWidgets();
                }
            }, 0, 36000);

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateWidgets() {

        for (int i = 0; i < widgetIds.length; i++) {
            RemoteViews v = new RemoteViews(context.getPackageName(), R.layout.appwidget);

            locationString = myLocation.toString();

            if (locationString == null)
                locationString = context.getString(R.string.unavailable);


            v.setTextViewText(R.id.tvLocation, locationString);

            int color = options.getInt(String.valueOf(widgetIds[i]), -1);
            if (color != -1)
                v.setTextColor(R.id.tvLocation, color);

            widgetManager.updateAppWidget(widgetIds[i], v);

        }

    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        SmartLocation.with(context).location().stop();

        if (options == null)
            return;

        SharedPreferences.Editor editor = options.edit();
        for (int id : appWidgetIds)
            editor.remove(String.valueOf(id));
        editor.commit();
    }
}
