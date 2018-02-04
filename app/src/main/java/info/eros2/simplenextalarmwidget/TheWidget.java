package info.eros2.simplenextalarmwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.RemoteViews;

public class TheWidget extends AppWidgetProvider
{

    static void updateAppWidget( Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {
        @SuppressWarnings("deprecation")
        String nextAlarm = Settings.System.getString( context.getContentResolver(),
                                                        Settings.System.NEXT_ALARM_FORMATTED );

        if ( ( nextAlarm == null ) || nextAlarm.isEmpty() ) {
            nextAlarm = "-- --:--";
        }

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews( context.getPackageName(), R.layout.widget );
        views.setTextViewText( R.id.appwidget_text, "\u23f0 \u00a0 " + nextAlarm );

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget( appWidgetId, views );
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //if ( intent.getAction().equals( "android.app.action.NEXT_ALARM_CLOCK_CHANGED" ) ) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

            onUpdate( context, appWidgetManager, appWidgetIds );
        //}
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

