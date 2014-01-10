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
import pl.rafik.geoorganizer.services.localisation.MyBestLocation;

import java.util.Calendar;

/**
 * rafik991@gmail.com
 * 1/10/14
 */
public class SchedulerFactory {

    private Context context;
    private AlarmManager alarmManager;
    private SharedPreferences sharedPreferences;
    private Intent broadcastIntent;
    private PendingIntent pendingIntent;
    private MyBestLocation myBestLocation;
    private DbxStart dbxStart;


    public SchedulerFactory(Context appContext) {
        context = appContext;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        dbxStart = new DbxStart();
    }

    public void setUpScheduledService(long updateTime) {
        if (dbxStart.getOpenedDatastore() == null) {
            Log.e("DROPBOX", "Dropbox account is not linked...");
            return;
        }
        broadcastIntent = new Intent(context, LocalisationUpdatesReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + updateTime, pendingIntent);
    }

}
