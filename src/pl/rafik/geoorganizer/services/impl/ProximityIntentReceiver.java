package pl.rafik.geoorganizer.services.impl;

import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.activities.main.ShowDetails;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import pl.rafik.geoorganizer.services.ITaskService;

/**
 * Klasa przechwytujaca notifykacje z ProximityAlerta.
 * 
 * @author rafal.machnik
 * 
 */
public class ProximityIntentReceiver extends BroadcastReceiver {

	private static final int NOTIFICATION_ID = 1000;
	private ITaskService taskService;
	private Vibrator vibrator;

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		CharSequence contentText = "";
		CharSequence contentTitle = "";
		SharedPreferences SP = PreferenceManager
				.getDefaultSharedPreferences(context);
		String[] vmodel = SP.getString("vibration_value", "200,200").split(",");
		String repeat = SP.getString("vibration_repeat", "2");
		Boolean sound = SP.getBoolean("sound_pref_checkbox", false);
		String soundUri = SP.getString("chose_sound", "");
		String key = LocationManager.KEY_PROXIMITY_ENTERING;

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		taskService = new TaskService(context);
		Bundle bundle = intent.getExtras();
		TaskDTO dto = taskService.getTask(bundle.getLong("id"));
		Boolean entering = intent.getBooleanExtra(key, false);
		long pattern[] = new long[Integer.parseInt(repeat) * 2];
		for (int i = 0; i < pattern.length; i++) {
			pattern[i] = Long.parseLong(vmodel[i % 2]);
		}
		vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(pattern, -1);
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
		CharSequence tickerText = "";
		long when = System.currentTimeMillis();
		Intent notificationIntent = new Intent(context, ShowDetails.class);
		notificationIntent.putExtra("id", dto.getId());
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent cI = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		Notification n = new Notification(icon, tickerText, when);
		n.setLatestEventInfo(context, contentTitle, contentText, cI);
		if (sound) {
			if (soundUri.equals("")) {
				n.defaults |= Notification.DEFAULT_SOUND;
			} else {
				n.sound = Uri.parse(soundUri);
			}

		}
		n.defaults |= Notification.DEFAULT_LIGHTS;
		notificationManager.notify(NOTIFICATION_ID, n);
	}

}