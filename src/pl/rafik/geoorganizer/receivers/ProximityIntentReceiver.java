package pl.rafik.geoorganizer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import pl.rafik.geoorganizer.services.nofication.NotificationHelper;

/**
 * Klasa przechwytujaca notifykacje z ProximityAlerta.
 *
 * @author rafal.machnik
 */
public class ProximityIntentReceiver extends BroadcastReceiver {


    private NotificationHelper notificationHandler;

    @SuppressWarnings("deprecation")
    @Override
    public void onReceive(Context context, Intent intent) {
        notificationHandler = new NotificationHelper(context);
        notificationHandler.runNotification(context, intent);
    }


}