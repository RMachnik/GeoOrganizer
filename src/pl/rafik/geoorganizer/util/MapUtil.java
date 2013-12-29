package pl.rafik.geoorganizer.util;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import pl.rafik.geoorganizer.model.dto.TaskDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * rafik991@gmail.com
 * 11/28/13
 */
public class MapUtil {

    public void setMapCenter(GoogleMap mv, LatLng point, String addr) {

        mv.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 5));

    }

    public void setMapCenter(GoogleMap mv, List<TaskDTO> points) {

        for (TaskDTO task : points) {
            LatLng point = new LatLng(Double.parseDouble(task.getLocalisation().getLatitude()), Double.parseDouble(task.getLocalisation().getLongitude()));
            mv.addMarker(new MarkerOptions()
                    .position(point)
                    .title(task.getLocalisation().getLocalistationAddress()));
            mv.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 5));
        }

    }
}
