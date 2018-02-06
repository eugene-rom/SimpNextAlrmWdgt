package info.eros2.simplenextalarmwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

public class TheWidget extends AppWidgetProvider
{
    private static final String TAP_INTENT = "info.eros2.simplenextalarmwidget.TAP_WIDGET";
    private final Intent tapIntent = new Intent( TAP_INTENT );

    @Override
    public void onUpdate( Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds )
    {
        PendingIntent pendingIntent = PendingIntent.getBroadcast( context, 0, tapIntent, PendingIntent.FLAG_UPDATE_CURRENT );
        String nextAlarm = getNextAlarmString( context );

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews( context.getPackageName(), R.layout.widget );
        views.setTextViewText( R.id.appwidget_text, nextAlarm );
        views.setOnClickPendingIntent( R.id.appwidget_text, pendingIntent );

        // There may be multiple widgets active, so update all of them
        for ( int appWidgetId : appWidgetIds ) {
            appWidgetManager.updateAppWidget( appWidgetId, views );
        }
    }

    @Override
    public void onReceive( Context context, Intent intent )
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance( context );
        ComponentName thisAppWidget = new ComponentName( context.getPackageName(), getClass().getName() );
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds( thisAppWidget );

        onUpdate( context, appWidgetManager, appWidgetIds );

        if ( TAP_INTENT.equals( intent.getAction() ) ) {
            Intent openClockIntent = new Intent( AlarmClock.ACTION_SHOW_ALARMS );
            openClockIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity( openClockIntent );
        }
    }

    @Override
    public void onEnabled( Context context ) {
    }

    @Override
    public void onDisabled( Context context ) {
    }

    private static String getNextAlarmString( Context context )
    {
        String res = null;
        AlarmManager am = (AlarmManager)context.getSystemService( Context.ALARM_SERVICE );

        if ( am == null ) {
            // Can't get AlarmManager, use deprecated method
            //noinspection deprecation
            res = Settings.System.getString( context.getContentResolver(), Settings.System.NEXT_ALARM_FORMATTED );
        }
        else
        {
            AlarmManager.AlarmClockInfo nextAlarmClock = am.getNextAlarmClock();
            if ( nextAlarmClock != null ) {
                // Format alarm time as e.g. "Di. 06:30"
                res = DateFormat.format("EEE HH:mm", nextAlarmClock.getTriggerTime() ).toString();
            }
        }

        if ( ( res == null ) || res.isEmpty() ) {
            res = context.getString( R.string.appwidget_empty_text );
        }

        return res;
    }
}

