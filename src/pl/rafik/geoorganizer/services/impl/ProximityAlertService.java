package pl.rafik.geoorganizer.services.impl;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.services.IProximityAlertService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Implementacja interfejsu {@link pl.rafik.geoorganizer.services.IProximityAlertService} zgodnie z jego
 * opisem.
 *
 * @author rafal.machnik
 */
public class ProximityAlertService implements IProximityAlertService {
    private static final String PROX_ALERT_INTENT = "pl.rafik.geoorganizer.PROXIMITY_ALERT";
    private LocationManager locationManager;
    private Context context;
    private float radius;

    public ProximityAlertService(Context c) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(c);
        this.locationManager = (LocationManager) c
                .getSystemService(Context.LOCATION_SERVICE);
        this.context = c;
        this.radius = Float.parseFloat(SP.getString("proxradius", "100"));

    }

    @Override
    public boolean addProximityAlert(TaskDTO dto) {
        Intent intent = new Intent(PROX_ALERT_INTENT);
        intent.putExtra("id", dto.getId());
        PendingIntent proximityIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.d("computation", String.valueOf(computeExpirationTime(dto)));
        locationManager.addProximityAlert((double) (Double.parseDouble((dto
                .getLocalisation().getLatitude()))),
                (double) (Double.parseDouble((dto.getLocalisation()
                        .getLongitude()))), radius,
                computeExpirationTime(dto), proximityIntent);
        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_COARSE);
        locationManager.requestLocationUpdates((long) 10000, radius, crit,
                proximityIntent);
        return true;

    }

    @Override
    public boolean addProximityAlerts(Context caller, ArrayList<TaskDTO> list) {
        for (TaskDTO dto : list) {
            Intent intent = new Intent(PROX_ALERT_INTENT);
            intent.putExtra("id", dto.getId());
            PendingIntent proximityIntent = PendingIntent.getBroadcast(context,
                    0, intent, 0);
            Log.d("data", String.valueOf(computeExpirationTime(dto)));
            locationManager.addProximityAlert(
                    Double.parseDouble(dto.getLocalisation().getLatitude()),
                    Double.parseDouble(dto.getLocalisation().getLongitude()),
                    radius, computeExpirationTime(dto), proximityIntent);
            Criteria crit = new Criteria();
            crit.setAccuracy(Criteria.ACCURACY_COARSE);
            locationManager.requestLocationUpdates((long) 10000, radius, crit,
                    proximityIntent);
        }
        return true;
    }

    @Override
    public void removeAlert(TaskDTO dto) {

        Intent intent = new Intent(PROX_ALERT_INTENT);
        intent.putExtra("id", dto.getId());
        PendingIntent proximityIntent = PendingIntent.getBroadcast(context, 0,
                intent, 0);
        locationManager.removeProximityAlert(proximityIntent);
        locationManager.removeUpdates(proximityIntent);

    }

    @Override
    public void actualizeProximities(List<TaskDTO> list) {
        for (TaskDTO dto : list) {
            this.removeAlert(dto);
            this.addProximityAlert(dto);
        }

    }

    @Override
    public long computeExpirationTime(TaskDTO dto) {
        Calendar c = Calendar.getInstance();
        Calendar tmp = Calendar.getInstance();
        String dtime[] = dto.getDate().split(" ");
        if (dtime.length > 1) {
            String data[] = dtime[0].split("-");
            tmp.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data[0]));
            tmp.set(Calendar.MONTH, Integer.parseInt(data[1]));
            tmp.set(Calendar.YEAR, Integer.parseInt(data[2]));
            String[] time = dtime[1].split(":");
            tmp.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
            tmp.set(Calendar.MINUTE, Integer.parseInt(time[1]));

        }
        return tmp.getTimeInMillis() - c.getTimeInMillis();

    }
}
