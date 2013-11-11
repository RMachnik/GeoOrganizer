package pl.rafik.geoorganizer.activities.map;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * Klasa rozszerzajaca overlaye ktore wczytywane sa na mape. Nie mozna dodawac nowych po dotknieciu!
 * 
 * @author rafal.machnik
 * 
 */
public class CustomItemizedOverlayNT extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();
	private Context context;

	public CustomItemizedOverlayNT(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public CustomItemizedOverlayNT(Drawable defaultMarker, Context c) {
		this(defaultMarker);
		this.context = c;

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

}
