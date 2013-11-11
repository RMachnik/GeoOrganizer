package pl.rafik.geoorganizer.services.impl;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Klasa implementujaca interfejs {@android.location.LocationListener} zgodnie z
 * jego przeznaczniem.
 * 
 * @author Rafal
 * 
 */
public class LocListener implements LocationListener {

//	private Context context;

	// private TaskService service;

	public LocListener(Context c) {
//		this.context = c;
		// this.service = new TaskService(c);
	}

	// jesli jest lokalizacja zapisz ta w ktorej jestes
	@Override
	public void onLocationChanged(Location location) {
		Log.d("Zmiana lokacji", String.valueOf(location.getLatitude()) + " "
				+ String.valueOf(location.getLongitude()));
	}

	@Override
	public void onProviderDisabled(String provider) {
		// Log.d("LocListener", "provider Disabled" + provider);
		// Intent intent = new Intent(
		// android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		// context.startActivity(intent);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d("LocListener", "enabled " + provider);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d("LocListener", "single");

	}

}
