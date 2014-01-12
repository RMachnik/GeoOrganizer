package pl.rafik.geoorganizer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;
import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.services.localisation.LastLocalisation;
import pl.rafik.geoorganizer.services.localisation.MyBestLocation;
import pl.rafik.geoorganizer.services.nofication.NotificationHelper;
import pl.rafik.geoorganizer.services.proximity.ProximityUtil;
import pl.rafik.geoorganizer.services.proximity.ScheduledLocalisationExecutor;

/**
 * rafik991@gmail.com
 * 1/10/14
 */
public class PassiveLocalisationUpdatesReceiver extends BroadcastReceiver {
    private ProximityUtil proximityUtil;
    private ScheduledLocalisationExecutor schedulerFactory;
    private SharedPreferences sharedPreferences;
    private LastLocalisation lastLocalisation;
    private NotificationHelper notificationHelper;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.w("PassiveLocalisationUpdatesReceiver!", "updating Service!");
        schedulerFactory = new ScheduledLocalisationExecutor(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        lastLocalisation = new LastLocalisation(context);
        notificationHelper = new NotificationHelper(context);
        try {
            final Location lastBestLocation = lastLocalisation.getLastBestLocation();
            if (lastBestLocation != null) {
                proximityUtil = new ProximityUtil(lastBestLocation, context);
                proximityUtil.updateLastClosestData();
                schedulerFactory.setUpScheduledService(proximityUtil.getCurrentUpdateTime());
                if (proximityUtil.shouldFireNotification())
                    notificationHelper.handleNotification(context, intent, proximityUtil);
            } else {
                throw new Exception("Localisation not found!");
            }

        } catch (DbxException e) {
            Log.w("DBXException in PassiveLocalisationUpdatesReceiver", "There was problem with Dbx service");
            e.printStackTrace();
        } catch (Exception e) {
            Log.w("lastLocalisation=null", "Last localisation was null!");
            e.printStackTrace();
        }

    }


}
