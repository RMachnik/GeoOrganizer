package pl.rafik.geoorganizer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.model.entity.TaskOpenHelper;
import pl.rafik.geoorganizer.services.localisation.MyBestLocation;
import pl.rafik.geoorganizer.services.nofication.NotificationHelper;
import pl.rafik.geoorganizer.services.proximity.ProximityUtil;
import pl.rafik.geoorganizer.services.proximity.ScheduledLocalisationExecutor;

/**
 * rafik991@gmail.com
 * 1/11/14
 */
public class LocalisationUpdatesReceiver extends BroadcastReceiver {
    private ProximityUtil proximityUtil;
    private ScheduledLocalisationExecutor schedulerFactory;
    private MyBestLocation myBestLocation = new MyBestLocation();
    private SharedPreferences sharedPreferences;
    private NotificationHelper notificationHelper;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.w("PassiveLocalisationUpdatesReceiver!", "updating Service!");
        schedulerFactory = new ScheduledLocalisationExecutor(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        notificationHelper = new NotificationHelper(context);
        MyBestLocation.LocationResult locationResult = new MyBestLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                try {
                    if (location != null) {
                        proximityUtil = new ProximityUtil(location, context);
                        proximityUtil.updateLastClosestData();
                        schedulerFactory.setUpScheduledService(proximityUtil.getCurrentUpdateTime());
                        if (proximityUtil.shouldFireNotification())
                            notificationHelper.handleNotification(context, intent,proximityUtil);
                    } else
                        throw new Exception("Sth is wrong there was no localisation");
                } catch (DbxException e) {
                    Log.e("LOCALISATION_UPDATES_RECEIVER", "Dbx have fault! Broadcast receiver is shutdown!");
                    e.printStackTrace();
                    return;
                } catch (Exception e) {
                    Log.e("LOCALISATION_UPDATES_RECEIVER", "Localisation wasn't found! Broadcast receiver is shutdown!");
                    e.printStackTrace();
                    return;
                }
            }
        };

        myBestLocation.getLocation(context, locationResult);

    }


}
