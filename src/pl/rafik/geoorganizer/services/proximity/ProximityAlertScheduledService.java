package pl.rafik.geoorganizer.services.proximity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.services.IProximityAlertService;

import java.util.ArrayList;
import java.util.List;

/**
 * rafik991@gmail.com
 * 1/10/14
 */
public class ProximityAlertScheduledService implements IProximityAlertService {
    private ScheduledLocalisationExecutor schedulerFactory;
    private Context context;
    private SharedPreferences sharedPreferences;

    public ProximityAlertScheduledService(Context c) {
        context = c;
        schedulerFactory = new ScheduledLocalisationExecutor(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
    }

    @Override
    public boolean addProximityAlert(TaskDTO dto) {
        schedulerFactory.setUpScheduledService(sharedPreferences.getLong(ProximityUtil.LAST_UPDATE_TIME, ProximityUtil.DEFAULT_UPDATE_TIME));
        return true;
    }

    @Override
    public boolean addProximityAlerts(Context caller, ArrayList<TaskDTO> reminders) {
        schedulerFactory.setUpScheduledService(sharedPreferences.getLong(ProximityUtil.LAST_UPDATE_TIME, ProximityUtil.DEFAULT_UPDATE_TIME));
        return true;
    }

    @Override
    public void removeAlert(TaskDTO reminder) {

    }

    @Override
    public long computeExpirationTime(TaskDTO dto) {
        return 0;
    }

    @Override
    public void actualizeProximities(List<TaskDTO> list) {

    }
}
