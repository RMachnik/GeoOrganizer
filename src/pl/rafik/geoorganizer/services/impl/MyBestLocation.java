package pl.rafik.geoorganizer.services.impl;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Klasa pozwalajaca na otrzymanie lokalizacji w ktorej jestesmy za pomoca
 * najlepszego providera. Jesli nie jest dostepny zaden nastepuje proba pobrania
 * ostatniej znanej lokalizacji.
 * 
 * @author rafal.machnik
 * 
 */

public class MyBestLocation {
	Timer timer1;
	LocationManager lm;
	LocationResult locationResult;
	boolean gps_enabled = false;
	boolean network_enabled = false;

	public boolean getLocation(Context context, LocationResult result) {
		// przypisanie referencji
		locationResult = result;
		if (lm == null)
			lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);

		// sprawdzam dostepnosc providerow
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// jesli zaden nie jest dostepny wylaczam sie
		if (!gps_enabled && !network_enabled)
			return false;

		if (gps_enabled)
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					locationListenerGps);
		if (network_enabled)
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
					locationListenerNetwork);
		timer1 = new Timer();
		// opozniam uruchomienie watku w celu odszukania lokacji jesli ktorys z
		// providerow zdola to zrobic
		timer1.schedule(new GetLastLocation(), 20000);
		return true;
	}

	LocationListener locationListenerGps = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			timer1.cancel();
			locationResult.gotLocation(location);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerNetwork);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			timer1.cancel();
			locationResult.gotLocation(location);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerGps);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	// Timer task sluzy do odszukania najmlodszej lokalizacji
	/**
	 * Klasa pomocnicza odpalana przez TimerTask.
	 * 
	 * @author rafal.machnik
	 * 
	 */
	class GetLastLocation extends TimerTask {
		@Override
		public void run() {
			lm.removeUpdates(locationListenerGps);
			lm.removeUpdates(locationListenerNetwork);

			Location net_loc = null, gps_loc = null;
			if (gps_enabled)
				gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (network_enabled)
				net_loc = lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			// if there are both values use the latest one
			if (gps_loc != null && net_loc != null) {
				if (gps_loc.getTime() > net_loc.getTime())
					locationResult.gotLocation(gps_loc);
				else
					locationResult.gotLocation(net_loc);
				return;
			}

			if (gps_loc != null) {
				locationResult.gotLocation(gps_loc);
				return;
			}
			if (net_loc != null) {
				locationResult.gotLocation(net_loc);
				return;
			}
//			locationResult.gotLocation(null);
		}
	}

	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);
	}

}
