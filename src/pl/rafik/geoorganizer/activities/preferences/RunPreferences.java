package pl.rafik.geoorganizer.activities.preferences;

import java.util.List;

import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.services.IProximityAlertService;
import pl.rafik.geoorganizer.services.ITaskService;
import pl.rafik.geoorganizer.services.impl.ProximityAlertService;
import pl.rafik.geoorganizer.services.impl.TaskService;
import android.app.Activity;
import android.os.Bundle;

/**
 * Klasa uruchamiajaca ustawienia aplikacji, uruchamia fragment
 * {@link PreferencesFragment}
 * 
 * @author rafal.machnik
 * 
 */
public class RunPreferences extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PreferencesFragment())
				.commit();
		IProximityAlertService proxiService = new ProximityAlertService(this);
		ITaskService taskService = new TaskService(this);
		List<TaskDTO> list = taskService.getNotDoneTasks();
		proxiService.actualizeProximities(list);
	}
}
