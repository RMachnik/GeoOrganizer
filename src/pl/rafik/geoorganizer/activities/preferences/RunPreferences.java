package pl.rafik.geoorganizer.activities.preferences;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.services.IProximityAlertService;
import pl.rafik.geoorganizer.services.ITaskService;
import pl.rafik.geoorganizer.services.proximity.ProximityAlertScheduledService;
import pl.rafik.geoorganizer.services.data.TaskService;

import java.util.List;

/**
 * Klasa uruchamiajaca ustawienia aplikacji, uruchamia fragment
 * {@link GeoOrganizerPreferences}
 *
 * @author rafal.machnik
 */
public class RunPreferences extends Activity {
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new GeoOrganizerPreferences())
                .commit();
        IProximityAlertService proxiService = new ProximityAlertScheduledService(this);
        ITaskService taskService = new TaskService(this);
        List<TaskDTO> list = null;
        try {
            list = taskService.getNotDoneTasks();
        } catch (DbxException e) {
            e.printStackTrace();
        }
        proxiService.actualizeProximities(list);
    }
}
