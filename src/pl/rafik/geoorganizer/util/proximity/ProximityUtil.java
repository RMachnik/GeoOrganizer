package pl.rafik.geoorganizer.util.proximity;

import android.content.Context;
import android.location.Location;
import pl.rafik.geoorganizer.services.ITaskService;
import pl.rafik.geoorganizer.services.impl.TaskService;

/**
 * rafik991@gmai.com
 * 12/28/13
 */
public class ProximityUtil {
    private ITaskService taskService;
    private Location currentLocation;

    public ProximityUtil(Location currentLocation, Context context) {
        this.currentLocation = currentLocation;
        taskService = new TaskService(context);
    }



}
