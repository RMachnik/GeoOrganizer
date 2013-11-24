package pl.rafik.geoorganizer.activities.map;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.services.impl.LocalisationService;

import java.util.List;

/**
 * Klasa przygotowana do pokazywania na mapie punkty ktory zostal wprowadzony
 * przy dodawaniu nowego zadania.
 *
 * @author Rafal
 */
public class ShowOnMap extends FragmentActivity {

    int latitude;
    int longitude;
    private GeoPoint point;
    private GoogleMap mv;
    private Button zatwierdz;
    private EditText searchEdt;
    private Button change;
    private Handler handler;
    private LocalisationService service;
    private Address address;
    private String addr = "";
    private Drawable icon;
    private CustomItemizedOverlay itemizedOverlay;
    private List<Overlay> listOverlays;
    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map);
        Bundle bundle = getIntent().getExtras();
        handler = new RefreshHandler();
        service = new LocalisationService(handler, this);
        initialiseMap(bundle);
        setMapCenter(mv, point);
        initialiseButtons();
    }

    private void initialiseButtons() {
        zatwierdz = (Button) findViewById(R.id.btn_confirmTaskLocation);
        searchEdt = (EditText) findViewById(R.id.et_search);
        change = (Button) findViewById(R.id.btn_searchOnMap);

        // wyszukiwanie nowej lokalizacji
        change.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (searchEdt.getText().toString().isEmpty()) {

                } else {
                    service.getAddresFromName(searchEdt.getText().toString());
                }

            }
        });

        zatwierdz.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // kiedy w polu wyszukiwania jest tekst ktory nie zostal jeszcze
                // odnaleziony, tzn funkcja wyszukiwania lokalizacji nie zostala
                // odpalona
                if (!addr.contains(searchEdt.getText().toString())) {
                    service.getAddresFromName(searchEdt.getText().toString());
                } else if (searchEdt.getText().toString().isEmpty()
                        && addr.isEmpty()) {
                    // gdy jednak nie wprowadzimy zadnej zmiany
                    setRequestedOrientation(RESULT_CANCELED);
                    Intent data = ShowOnMap.this.getIntent();
                    setResult(RESULT_CANCELED, data);
                    finish();
                } else if (searchEdt.getText().toString().isEmpty()
                        && !addr.isEmpty()) {
                    Intent data = ShowOnMap.this.getIntent();
                    data.putExtra("PlaceName", addr);
                    data.putExtra("Latitude", latitude);
                    data.putExtra("Longitude", longitude);
                    setResult(RESULT_OK, data);
                    finish();
                }
                Intent data = ShowOnMap.this.getIntent();
                data.putExtra("PlaceName", searchEdt.getText().toString());
                data.putExtra("Latitude", latitude);
                data.putExtra("Longitude", longitude);
                setResult(RESULT_OK, data);
                finish();
            }

        });
    }

    private void initialiseMap(Bundle bundle) {
        mv = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.map)).getMap();
        mv.setMyLocationEnabled(true);
        mv.getUiSettings().setMyLocationButtonEnabled(true);
        latitude = (int) (bundle.getDouble("Latitude") * 1000000);
        longitude = (int) (bundle.getDouble("Longitude") * 1000000);
        Log.d("Latitude", String.valueOf(latitude));
        Log.d("Longitude", String.valueOf(longitude));
        point = new GeoPoint(latitude, longitude);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    public void setMapCenter(GoogleMap mv, GeoPoint point) {
        Marker marker = mv.addMarker(new MarkerOptions()
                .position(new LatLng(point.getLatitudeE6(), point.getLongitudeE6()))
                .title(addr));
        LatLng coordinate = new LatLng(point.getLatitudeE6(), point.getLongitudeE6());
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 5);
        mv.animateCamera(yourLocation);

    }

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    /**
     * Klasa oboslugujaca serwis ktory po znalezieniu odpowiedniej lokalizacji
     * wysyla message.
     *
     * @author Rafal
     */
    private class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2: {
                    if (service.addressList.isEmpty()
                            || service.addressList == null) {
                        Toast.makeText(ShowOnMap.this,
                                "Nie znaleziono pasujacych rezultatow!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        for (Address a : service.addressList) {
                            address = a;
                            addr = "";
                            for (int i = 0; i < a.getMaxAddressLineIndex() - 1; i++) {
                                addr += a.getAddressLine(i) + ", ";
                            }
                            addr += a
                                    .getAddressLine(a.getMaxAddressLineIndex() - 1);
                            Log.d("fullAddress", addr);
                            latitude = (int) (address.getLatitude() * 1000000);
                            longitude = (int) (address.getLongitude() * 1000000);
                            GeoPoint nPoint = new GeoPoint(
                                    (int) (address.getLatitude() * 1000000),
                                    (int) (address.getLongitude() * 1000000));
                            setMapCenter(mv, nPoint);

                        }
                    }
                    return;
                }
                case 1: {
                    if (service.addressList.isEmpty()
                            || service.addressList == null) {
                        Toast.makeText(ShowOnMap.this,
                                "Nie znaleziono pasujacych rezultatow!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        for (Address a : service.addressList) {
                            address = a;
                            addr = "";
                            for (int i = 0; i < a.getMaxAddressLineIndex() - 1; i++) {
                                addr += a.getAddressLine(i) + ", ";
                            }
                            addr += a
                                    .getAddressLine(a.getMaxAddressLineIndex() - 1);
                            Log.d("fullAddress", addr);
                            latitude = (int) (address.getLatitude() * 1000000);
                            longitude = (int) (address.getLongitude() * 1000000);
                            GeoPoint nPoint = new GeoPoint(
                                    (int) (address.getLatitude() * 1000000),
                                    (int) (address.getLongitude() * 1000000));
                            searchEdt.setText(addr);
                            setMapCenter(mv, nPoint);
                        }
                        return;
                    }
                }
                case 3: {
                    Log.d("handler", "from map change desc");
                    searchEdt.setText(itemizedOverlay.getDescription());
                    return;
                }
            }
        }
    }

}
