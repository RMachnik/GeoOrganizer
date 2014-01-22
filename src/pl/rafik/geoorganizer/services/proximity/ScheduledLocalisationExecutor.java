package pl.rafik.geoorganizer.services.proximity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import pl.rafik.geoorganizer.activities.dbx.DbxStart;
import pl.rafik.geoorganizer.receivers.LocalisationUpdatesReceiver;

import java.util.Calendar;

/**
 * rafik991@gmail.com
 * 1/10/14
 */
public class ScheduledLocalisationExecutor {

    public static String STOP_SCHEDULED_UPDATES = "stop_updates";
    private Context context;
    private AlarmManager alarmManager;
    private Intent broadcastIntent;
    private PendingIntent pendingIntent;
    private DbxStart dbxStart;
    private SharedPreferences sharedPreferences;


    public ScheduledLocalisationExecutor(Context appContext) {
        context = appContext;
        dbxStart = new DbxStart();
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
    }

    public void setUpScheduledService(long updateTime) {
        if (dbxStart.getOpenedDataStore() == null) {
            Log.e("DROPBOX", "Dropbox account is not linked...");
            return;
        }
        if (sharedPreferences.getBoolean(STOP_SCHEDULED_UPDATES, false))
            updateTime = ProximityUtil.DEFAULT_UPDATE_TIME * 2;
        Log.w("scheduled factory", "updating Service!");
        broadcastIntent = new Intent(context, LocalisationUpdatesReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + updateTime, pendingIntent);
    }
}
