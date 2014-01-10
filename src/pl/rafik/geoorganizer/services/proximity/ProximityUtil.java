package pl.rafik.geoorganizer.services.proximity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.preference.PreferenceManager;
import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.activities.preferences.GeoOrganizerPreferences;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.services.ITaskService;
import pl.rafik.geoorganizer.services.data.TaskService;

import java.util.List;

/**
 * rafik991@gmail.com
 * 12/28/13
 */
public class ProximityUtil {
    public static final String LAST_UPDATE_TIME = "LAST_UPDATE_TIME";
    public final String LAST_MIN_DIST_TASK_ID = "LAST_MIN_DIST_TASK_ID";
    private final String DEFAULT_RADIUS = "100";
    private final String LAST_MIN_DIST = "LAST_MIN_DIST";
    //one minute
    public static final long DEFAULT_UPDATE_TIME = 60000;
    private SharedPreferences sharedPreferences;
    private ITaskService taskService;
    private Location currentLocation;
    private long radius;
    private ClosestData currentClosestData;
    private long currentUpdateTime;


    public ProximityUtil(Location currentLocation, Context context) throws DbxException {
        this.currentLocation = currentLocation;
        taskService = new TaskService(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        radius = Long.parseLong(sharedPreferences.getString(GeoOrganizerPreferences.PROXY_RADIUS, DEFAULT_RADIUS));
        currentClosestData = getCurrentClosestData();
        currentUpdateTime = updateTime();
    }

    public boolean shouldFireNotification() throws DbxException {
        if (currentClosestData.getMinDist() < radius)
            return true;
        return false;
    }

    public boolean isEntering() throws DbxException {
        if (shouldFireNotification()) {
            ClosestData lastClosestData = getLastClosestData();
            if (lastClosestData.getMinDist() > currentClosestData.getMinDist())
                return true;
        }
        return false;
    }

    private long updateTime() throws DbxException {
        float closestDist = currentClosestData.getMinDist();
        long newTime = DEFAULT_UPDATE_TIME;
        if (closestDist > radius) {
            int times = (int) (closestDist / radius);
            if (times < 10) {
                newTime = DEFAULT_UPDATE_TIME - (DEFAULT_UPDATE_TIME * (1 / times));
            }
        } else {
            newTime = DEFAULT_UPDATE_TIME / 4;
        }

        return newTime;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private ClosestData getLastClosestData() throws DbxException {
        ClosestData closestData = new ClosestData();
        closestData.setTask(null);
        closestData.setMinDist(-1);
        String lastTaskId = sharedPreferences.getString(LAST_MIN_DIST_TASK_ID, "");
        if (!lastTaskId.isEmpty()) {
            TaskDTO lastTask = taskService.getTask(lastTaskId);
            if (lastTask != null) {
                closestData.setTask(lastTask);
            }
            closestData.setMinDist(sharedPreferences.getFloat(LAST_MIN_DIST, -1));
        }
        return closestData;
    }

    public void updateLastClosestData() {
        float minDist = currentClosestData.getMinDist();
        TaskDTO task = currentClosestData.getTask();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(LAST_MIN_DIST, minDist);
        editor.putString(LAST_MIN_DIST_TASK_ID, task.getId());
        editor.putLong(LAST_UPDATE_TIME, currentUpdateTime);
        editor.commit();
    }

    private ClosestData getCurrentClosestData() throws DbxException {
        List<TaskDTO> taskList = taskService.getActualTasks();
        float minDist = Float.MIN_VALUE;
        TaskDTO closestTask = null;
        ClosestData closestData = new ClosestData();
        for (TaskDTO task : taskList) {
            Location taskLocation = new Location(currentLocation.getProvider());
            taskLocation.setLatitude(Double.parseDouble(task.getLocalisation().getLatitude()));
            taskLocation.setLongitude(Double.parseDouble(task.getLocalisation().getLongitude()));
            float current = currentLocation.distanceTo(taskLocation);
            if (current < minDist) {
                minDist = current;
                closestTask = task;
            }
        }
        closestData.setMinDist(minDist);
        closestData.setTask(closestTask);
        return closestData;
    }

    public long getCurrentUpdateTime() {
        return currentUpdateTime;
    }

    private class ClosestData {
        private TaskDTO task;
        private float minDist;

        public TaskDTO getTask() {
            return task;
        }

        public void setTask(TaskDTO task) {
            this.task = task;
        }

        public float getMinDist() {
            return minDist;
        }

        public void setMinDist(float minDist) {
            this.minDist = minDist;
        }
    }


}
