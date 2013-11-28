package pl.rafik.geoorganizer.util;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import pl.rafik.geoorganizer.model.dto.GeoLocalisation;

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

    public void setMapCenter(GoogleMap mv, LatLng[] points) {
        List<Integer> latitude = new ArrayList<Integer>();
        List<Integer> longitude = new ArrayList<Integer>();

        for (int i = 0; i < points.length; i++) {
            GeoLocalisation geo = new GeoLocalisation();

            mv.addMarker(new MarkerOptions()
                    .position(points[i])
                    .title(geo.getLocalistationAddress()));


            mv.animateCamera(CameraUpdateFactory.newLatLngZoom(points[i], 5));
        }

    }
}
