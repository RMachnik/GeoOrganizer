package pl.rafik.geoorganizer.services.proximity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import pl.rafik.geoorganizer.activities.dbx.DbxStart;
import pl.rafik.geoorganizer.receivers.LocalisationUpdatesReceiver;
import pl.rafik.geoorganizer.receivers.PassiveLocalisationUpdatesReceiver;

import java.util.Calendar;

/**
 * rafik991@gmail.com
 * 1/10/14
 */
public class ScheduledLocalisationExecutor {

    private Context context;
    private AlarmManager alarmManager;
    private Intent broadcastIntent;
    private PendingIntent pendingIntent;
    private DbxStart dbxStart;


    public ScheduledLocalisationExecutor(Context appContext) {
        context = appContext;
        dbxStart = new DbxStart();
    }

    public void setUpScheduledService(long updateTime) {
        if (dbxStart.getOpenedDatastore() == null) {
            Log.e("DROPBOX", "Dropbox account is not linked...");
            return;
        }
        Log.w("scheduled factory","updating Service!");
        broadcastIntent = new Intent(context, LocalisationUpdatesReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + updateTime, pendingIntent);
    }

}
