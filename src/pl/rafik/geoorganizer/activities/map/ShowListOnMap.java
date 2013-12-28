package pl.rafik.geoorganizer.activities.map;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.dropbox.sync.android.DbxException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.services.impl.TaskService;
import pl.rafik.geoorganizer.util.MapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa pozwalajaca na zobaczenie wszystkich nie wykonanych taskow na mapie.
 *
 * @author rafal.machnik
 */
public class ShowListOnMap extends FragmentActivity {
    private LatLng[] points;
    private GoogleMap mv;
    private TaskService taskService;
    private List<TaskDTO> tasks;
    private MapUtil mapUtil = new MapUtil();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_list_items);
        initialiseServices();
        initialiseContent();
        mapUtil.setMapCenter(mv, points);

    }

    private void initialiseContent() {

        Bundle bundle = getIntent().getExtras();
        mv = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.mapView)).getMap();
        String[] ids = bundle.getStringArray("IDS");
        points = new LatLng[ids.length];
        for (int i = 0; i < ids.length; i++) {
            TaskDTO dto = null;
            try {
                dto = taskService.getTask(ids[i]);
            } catch (DbxException e) {
                e.printStackTrace();
            }
//			Log.d("Latitude",String.valueOf((Integer.parseInt(dto.getLocalisation()
//					.getLatitude()))));
//			Log.d("Longitude",String.valueOf((Integer.parseInt(dto.getLocalisation()
//					.getLongitude()))));
            points[i] = new LatLng(Double.parseDouble(dto.getLocalisation()
                    .getLatitude()), Double.parseDouble(dto.getLocalisation()
                    .getLongitude()));
            tasks.add(dto);
        }

    }

    private void initialiseServices() {
        taskService = new TaskService(getApplicationContext());
        tasks = new ArrayList<TaskDTO>();
    }


}
