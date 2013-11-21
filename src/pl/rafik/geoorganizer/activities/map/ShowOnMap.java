package pl.rafik.geoorganizer.activities.map;

import java.util.List;

import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.services.impl.LocalisationService;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * Klasa przygotowana do pokazywania na mapie punkty ktory zostal wprowadzony
 * przy dodawaniu nowego zadania.
 * 
 * @author Rafal
 * 
 */
public class ShowOnMap extends FragmentActivity {

	private GeoPoint point;
	private GoogleMap mv;
	private Button zatwierdz;
	private EditText searchEdt;
	private Button change;
	private Handler handler;
	private LocalisationService service;
	private Address address;
	private String addr = "";
	int latitude;
	int longitude;
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

		mv =((MapFragment) getFragmentManager().findFragmentById(
                R.id.mapView)).getMap();
		latitude = (int) (bundle.getDouble("Latitude") * 1000000);
		longitude = (int) (bundle.getDouble("Longitude") * 1000000);
		Log.d("Latitude", String.valueOf(latitude));
		Log.d("Longitude", String.valueOf(longitude));
		point = new GeoPoint(latitude, longitude);
		//listOverlays = mv.getOverlays();
		icon = this.getResources().getDrawable(R.drawable.ic_delete);
		itemizedOverlay = new CustomItemizedOverlay(icon, this, handler);
		service.getAdress(bundle.getDouble("Latitude"),
				bundle.getDouble("Longitude"));

		setMapCenter(mv, point);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}


	/**
	 * Klasa oboslugujaca serwis ktory po znalezienio odpowiedniej lokalizacji
	 * wysyla message.
	 * 
	 * @author Rafal
	 * 
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
	};

	public void setMapCenter(GoogleMap mv, GeoPoint point) {

		OverlayItem item = new OverlayItem(point, "Nazwa", addr);

		listOverlays.clear();
	//	mv.invalidate();
		itemizedOverlay.clear();
		itemizedOverlay.addOverlay(item);
		listOverlays.add(itemizedOverlay);
	//	mc.animateTo(point);
	//	mc.setZoom(mv.getMaxZoomLevel() - 5);
	//	mv.invalidate();
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

}
