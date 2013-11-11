package pl.rafik.geoorganizer.activities.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.model.dto.GeoLocalisation;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.services.impl.TaskService;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * Klasa pozwalajaca na zobaczenie wszystkich nie wykonanych taskow na mapie.
 * 
 * @author rafal.machnik
 * 
 */
public class ShowListOnMap extends MapActivity {
	private Drawable icon;
	private CustomItemizedOverlayNT itemizedOverlay;
	private List<Overlay> listOverlays;
	private GeoPoint[] points;
	private OverlayItem items[];
	private MapView mv;
	private TaskService taskService;
	private List<TaskDTO> tasks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_list_items);
		taskService = new TaskService(this);
		tasks = new ArrayList<TaskDTO>();
		icon = this.getResources().getDrawable(R.drawable.ic_delete);
		itemizedOverlay = new CustomItemizedOverlayNT(icon, this);
		Bundle bundle = getIntent().getExtras();
		mv = (MapView) findViewById(R.id.mapView);
		long[] ids = bundle.getLongArray("IDS");
		points = new GeoPoint[ids.length];
		listOverlays = mv.getOverlays();
		for (int i = 0; i < ids.length; i++) {
			TaskDTO dto = taskService.getTask(ids[i]);
//			Log.d("Latitude",String.valueOf((Integer.parseInt(dto.getLocalisation()
//					.getLatitude()))));
//			Log.d("Longitude",String.valueOf((Integer.parseInt(dto.getLocalisation()
//					.getLongitude()))));
			points[i] = new GeoPoint(Integer.parseInt(dto.getLocalisation()
					.getLatitude()), Integer.parseInt(dto.getLocalisation()
					.getLongitude()));
			tasks.add(dto);
		}
		items = new OverlayItem[points.length];
		setMapCenter(mv, points);

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setMapCenter(MapView mv, GeoPoint[] points) {
		List<Integer> latitude = new ArrayList<Integer>();
		List<Integer> longitude = new ArrayList<Integer>();
		listOverlays.clear();
		itemizedOverlay.clear();
		for (int i = 0; i < points.length; i++) {
			GeoLocalisation geo = new GeoLocalisation();
			geo.setLatitude(String.valueOf(points[i].getLatitudeE6()));
			geo.setLongitude(String.valueOf(points[i].getLongitudeE6()));
			items[i] = new OverlayItem(points[i], "Nazwa", tasks.get(i)
					.getLocalisation().getLocalistationAddress());
			itemizedOverlay.addOverlay(items[i]);
			latitude.add(points[i].getLatitudeE6());
			longitude.add(points[i].getLongitudeE6());
		}

		listOverlays.add(itemizedOverlay);
		mv.getController().zoomToSpan(Collections.max(longitude),
				Collections.min(longitude));
		mv.invalidate();
	}
}
