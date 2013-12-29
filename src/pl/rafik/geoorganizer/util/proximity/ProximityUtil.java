package pl.rafik.geoorganizer.util.proximity;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.activities.preferences.GeoOrganizerPreferences;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.services.ITaskService;
import pl.rafik.geoorganizer.services.impl.TaskService;

import java.util.List;

/**
 * rafik991@gmail.com
 * 12/28/13
 */
public class ProximityUtil {
    private final long UPDATES_TIME = 20000;
    private ITaskService taskService;
    private Location currentLocation;
    private long radius;


    public ProximityUtil(Location currentLocation, Context context) {
        this.currentLocation = currentLocation;
        taskService = new TaskService(context);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        radius = Long.parseLong(SP.getString(GeoOrganizerPreferences.PROXY_RADIUS, "100"));
    }

    public long getUpdateTime(ITaskService taskService, Location currentLocation) throws DbxException {
        List<TaskDTO> taskList = taskService.getActualTasks();
        for (TaskDTO task : taskList) {
            Location taskLocation = new Location(currentLocation.getProvider());
            taskLocation.setLatitude(Double.parseDouble(task.getLocalisation().getLatitude()));
            taskLocation.setLongitude(Double.parseDouble(task.getLocalisation().getLongitude()));
            if (currentLocation.distanceTo(taskLocation) < 2 * radius) {
                return UPDATES_TIME / 2;
            }
        }
        return UPDATES_TIME;
    }

}
