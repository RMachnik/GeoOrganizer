package pl.rafik.geoorganizer.activities.map;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.dropbox.sync.android.DbxException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.model.dto.GeoLocalisation;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.services.impl.TaskService;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa pozwalajaca na zobaczenie wszystkich nie wykonanych taskow na mapie.
 *
 * @author rafal.machnik
 */
public class ShowListOnMap extends FragmentActivity {
    private Drawable icon;
    private CustomItemizedOverlayNT itemizedOverlay;
    private List<Overlay> listOverlays;
    private GeoPoint[] points;
    private OverlayItem items[];
    private GoogleMap mv;
    private TaskService taskService;
    private List<TaskDTO> tasks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_list_items);
        taskService = new TaskService(getApplicationContext());
        tasks = new ArrayList<TaskDTO>();
        icon = this.getResources().getDrawable(R.drawable.ic_delete);
        itemizedOverlay = new CustomItemizedOverlayNT(icon, this);
        Bundle bundle = getIntent().getExtras();
        mv = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.mapView)).getMap();
        String[] ids = bundle.getStringArray("IDS");
        points = new GeoPoint[ids.length];
    /*	listOverlays = mv.getOverlays();*/
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
            points[i] = new GeoPoint(Integer.parseInt(dto.getLocalisation()
                    .getLatitude()), Integer.parseInt(dto.getLocalisation()
                    .getLongitude()));
            tasks.add(dto);
        }
        items = new OverlayItem[points.length];
        setMapCenter(mv, points);

    }

    public void setMapCenter(GoogleMap mv, GeoPoint[] points) {
        List<Integer> latitude = new ArrayList<Integer>();
        List<Integer> longitude = new ArrayList<Integer>();

        for (int i = 0; i < points.length; i++) {
            GeoLocalisation geo = new GeoLocalisation();
            geo.setLatitude(String.valueOf(points[i].getLatitudeE6()));
            geo.setLongitude(String.valueOf(points[i].getLongitudeE6()));
            mv.addMarker(new MarkerOptions()
                    .position(new LatLng(points[i].getLatitudeE6(), points[i].getLongitudeE6()))
                    .title(geo.getLocalistationAddress()));
            latitude.add(points[i].getLatitudeE6());
            longitude.add(points[i].getLongitudeE6());
            LatLng coordinate = new LatLng(points[i].getLatitudeE6(), points[i].getLongitudeE6());
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 5);
            mv.animateCamera(yourLocation);
        }

    }
}
