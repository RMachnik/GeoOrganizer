package pl.rafik.geoorganizer.services.impl;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.activities.main.ShowDetails;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.services.ITaskService;

/**
 * Klasa przechwytujaca notifykacje z ProximityAlerta.
 *
 * @author rafal.machnik
 */
public class ProximityIntentReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1000;
    private Boolean sound;
    private String soundUri;
    private String key = LocationManager.KEY_PROXIMITY_ENTERING;
    private ITaskService taskService;
    private Vibrator vibrator;
    private String[] vmodel;
    private String repeat;
    private SharedPreferences sharedPreferences;
    private CharSequence contentText;
    private CharSequence contentTitle;
    private NotificationManager notificationManager;
    private Bundle bundle;
    private TaskDTO dto;
    private Boolean entering;
    private CharSequence tickerText;
    private Notification notification;

    @SuppressWarnings("deprecation")
    @Override
    public void onReceive(Context context, Intent intent) {

        initialiseVibration(context);
        notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        loadTask(context, intent);
        entering = intent.getBooleanExtra(key, false);
        vibrationRepeat(context);
        handleNotification(context);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void handleNotification(Context context) {
        contentText = "";
        contentTitle = "";
        if (entering) {
            contentTitle = "Zblizasz sie do: ";
            Log.d(getClass().getSimpleName(), "entering");
            contentText = dto.getNote();

        } else {
            contentTitle = "Oddalasz sie od: ";
            contentText = dto.getNote();
            Log.d(getClass().getSimpleName(), "exiting");
        }
        int icon = R.drawable.ic_notification;
        tickerText = "";
        long when = System.currentTimeMillis();
        Intent notificationIntent = new Intent(context, ShowDetails.class);
        notificationIntent.putExtra("id", dto.getId());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent cI = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
         //old way of notification creations
        // Notification n = new Notification(icon, tickerText, when);
        //notification.setLatestEventInfo(context, contentTitle, contentText, cI);
        notification = new Notification.Builder(context).setSmallIcon(icon).setTicker(tickerText).setWhen(when).setContentTitle
                (contentTitle).setContentText(contentText).setContentIntent(cI).build();

        if (sound) {
            if (soundUri.equals("")) {
                notification.defaults |= Notification.DEFAULT_SOUND;
            } else {
                notification.sound = Uri.parse(soundUri);
            }

        }
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void vibrationRepeat(Context context) {
        long pattern[] = new long[Integer.parseInt(repeat) * 2];
        for (int i = 0; i < pattern.length; i++) {
            pattern[i] = Long.parseLong(vmodel[i % 2]);
        }
        vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, -1);
    }

    private void loadTask(Context context, Intent intent) {
        taskService = new TaskService(context);
        bundle = intent.getExtras();
        try {
            dto = taskService.getTask(bundle.getString("id"));
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }

    private void initialiseVibration(Context context) {
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        vmodel = sharedPreferences.getString("vibration_value", "200,200").split(",");
        repeat = sharedPreferences.getString("vibration_repeat", "2");
        sound = sharedPreferences.getBoolean("sound_pref_checkbox", false);
        soundUri = sharedPreferences.getString("chose_sound", "");
    }

}