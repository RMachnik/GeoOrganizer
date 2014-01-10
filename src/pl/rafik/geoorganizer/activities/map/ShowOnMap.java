package pl.rafik.geoorganizer.activities.map;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.handlers.RefreshHandlerMapSearch;
import pl.rafik.geoorganizer.services.localisation.LocalisationService;
import pl.rafik.geoorganizer.util.MapUtil;

/**
 * Klasa przygotowana do pokazywania na mapie punkty ktory zostal wprowadzony
 * przy dodawaniu nowego zadania.
 *
 * @author Rafal
 */
public class ShowOnMap extends FragmentActivity {

    private double latitude;
    private double longitude;
    private LatLng point;
    private GoogleMap mv;
    private Button zatwierdz;
    private EditText searchEdt;
    private Button change;
    private Handler handler;
    private LocalisationService service;
    private Address address;
    private String addr = "";
    private GoogleMap googleMap;
    private MapUtil mapUtil = new MapUtil();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map);
        Bundle bundle = getIntent().getExtras();
        handler = new RefreshHandlerMapSearch(this);
        setService(new LocalisationService(handler, this));
        initialiseButtons();
        initialiseMap(bundle);
        mapUtil.setMapCenter(getMv(), point, getAddr());

    }

    private void initialiseButtons() {
        zatwierdz = (Button) findViewById(R.id.btn_confirmTaskLocation);
        setSearchEdt((EditText) findViewById(R.id.et_search));
        change = (Button) findViewById(R.id.btn_searchOnMap);

        // wyszukiwanie nowej lokalizacji
        change.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (getSearchEdt().getText().toString().isEmpty()) {

                } else {
                    getService().getAddresFromName(getSearchEdt().getText().toString());
                }

            }
        });

        zatwierdz.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // kiedy w polu wyszukiwania jest tekst ktory nie zostal jeszcze
                // odnaleziony, tzn funkcja wyszukiwania lokalizacji nie zostala
                // odpalona
                if (!getAddr().contains(getSearchEdt().getText().toString())) {
                    getService().getAddresFromName(getSearchEdt().getText().toString());
                } else if (getSearchEdt().getText().toString().isEmpty()
                        && getAddr().isEmpty()) {
                    // gdy jednak nie wprowadzimy zadnej zmiany
                    setRequestedOrientation(RESULT_CANCELED);
                    Intent data = ShowOnMap.this.getIntent();
                    setResult(RESULT_CANCELED, data);
                    finish();
                } else if (getSearchEdt().getText().toString().isEmpty()
                        && !getAddr().isEmpty()) {
                    Intent data = ShowOnMap.this.getIntent();
                    data.putExtra("PlaceName", getAddr());
                    data.putExtra("Latitude", getLatitude());
                    data.putExtra("Longitude", getLongitude());
                    setResult(RESULT_OK, data);
                    finish();
                }
                Intent data = ShowOnMap.this.getIntent();
                data.putExtra("PlaceName", getSearchEdt().getText().toString());
                data.putExtra("Latitude", getLatitude());
                data.putExtra("Longitude", getLongitude());
                setResult(RESULT_OK, data);
                finish();
            }

        });
    }

    private void initialiseMap(Bundle bundle) {
        setMv(((MapFragment) getFragmentManager().findFragmentById(
                R.id.map)).getMap());
        getMv().setMyLocationEnabled(true);
        getMv().getUiSettings().setMyLocationButtonEnabled(true);
        setLatitude((bundle.getDouble("Latitude")));
        setLongitude((bundle.getDouble("Longitude")));
        Log.d("Latitude", String.valueOf(getLatitude()));
        Log.d("Longitude", String.valueOf(getLongitude()));
        point = new LatLng(getLatitude(), getLongitude());
        getSearchEdt().setText(getAddr());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    private void initialiseMap() {
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
        initialiseMap();
    }

    public GoogleMap getMv() {
        return mv;
    }

    public void setMv(GoogleMap mv) {
        this.mv = mv;
    }

    public EditText getSearchEdt() {
        return searchEdt;
    }

    public void setSearchEdt(EditText searchEdt) {
        this.searchEdt = searchEdt;
    }

    public LocalisationService getService() {
        return service;
    }

    public void setService(LocalisationService service) {
        this.service = service;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
