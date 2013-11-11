package pl.rafik.geoorganizer.activities.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * Klasa rozszerzajaca overlaye ktora pozwala na dodanie go po dotknieciu dodac
 * nowego.
 * 
 * @author rafal.machnik
 * 
 */
public class CustomItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();
	private Context context;
	private Handler handler;
	private String add;
	private GeoPoint p;

	public CustomItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public CustomItemizedOverlay(Drawable defaultMarker, Context c, Handler h) {
		this(defaultMarker);
		this.context = c;
		this.handler = h;
		this.add = "";
	}

	@Override
	protected OverlayItem createItem(int index) {
		return mapOverlays.get(index);
	}

	public void clear() {
		mapOverlays.clear();
	}

	@Override
	public int size() {
		return mapOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mapOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}

	public void addOverlay(OverlayItem overlay) {
		mapOverlays.add(overlay);
		this.populate();
	}

	public String getDescription() {
		return add;
	}

	public GeoPoint getPoint() {
		return p;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		if (event.getAction() == 1) {
			p = mapView.getProjection().fromPixels((int) event.getX(),
					(int) event.getY());
			Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
			setMapCenter(mapView, p, "");
			try {
				List<Address> addresses = geoCoder.getFromLocation(
						p.getLatitudeE6() / 1E6, p.getLongitudeE6() / 1E6, 1);

				add = "";
				if (addresses.size() > 0) {
					for (int i = 0; i < addresses.get(0)
							.getMaxAddressLineIndex(); i++)
						add += addresses.get(0).getAddressLine(i) + "\n";
				}
				setMapCenter(mapView, p, add);
				handler.sendEmptyMessage(3);
				// Toast.makeText(context, add, Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;

	}

	public void setMapCenter(MapView mv, GeoPoint point, String addr) {
		MapController mc = mv.getController();
		OverlayItem item = new OverlayItem(point, "Nazwa", addr);
		List<Overlay> overlays = mv.getOverlays();
		this.clear();
		mv.invalidate();

		this.addOverlay(item);
		overlays.add(this);
		mc.animateTo(point);
		mc.setZoom(mv.getMaxZoomLevel() - 5);
		mv.invalidate();
	}

}
