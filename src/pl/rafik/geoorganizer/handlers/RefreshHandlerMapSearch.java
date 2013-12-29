package pl.rafik.geoorganizer.handlers;

import android.location.Address;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.activities.map.ShowOnMap;
import pl.rafik.geoorganizer.util.MapUtil;

/**
 * Klasa oboslugujaca serwis ktory po znalezieniu odpowiedniej lokalizacji
 * wysyla message.
 *
 * @author Rafal
 */
public class RefreshHandlerMapSearch extends Handler {
    MapUtil mapUtil = new MapUtil();
    private ShowOnMap showOnMap;

    public RefreshHandlerMapSearch(ShowOnMap showOnMap) {
        this.showOnMap = showOnMap;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 2: {
                if (showOnMap.getService().addressList.isEmpty()
                        || showOnMap.getService().addressList == null) {
                    Toast.makeText(showOnMap,
                            showOnMap.getString(R.string.error_noResultsFound),
                            Toast.LENGTH_LONG).show();
                } else {
                    for (Address a : showOnMap.getService().addressList) {
                        showOnMap.setAddress(a);
                        showOnMap.setAddr("");
                        for (int i = 0; i < a.getMaxAddressLineIndex() - 1; i++) {
                            showOnMap.setAddr(showOnMap.getAddr() + a.getAddressLine(i) + ", ");
                        }
                        showOnMap.setAddr(showOnMap.getAddr() + a
                                .getAddressLine(a.getMaxAddressLineIndex() - 1));
                        Log.d("fullAddress", showOnMap.getAddr());
                        showOnMap.setLatitude((showOnMap.getAddress().getLatitude()));
                        showOnMap.setLongitude((showOnMap.getAddress().getLongitude()));
                        LatLng nPoint = new LatLng(
                                (showOnMap.getAddress().getLatitude()),
                                (showOnMap.getAddress().getLongitude()));
                        mapUtil.setMapCenter(showOnMap.getMv(), nPoint, showOnMap.getAddr());

                    }
                }
                return;
            }
            case 1: {
                if (showOnMap.getService().addressList.isEmpty()
                        || showOnMap.getService().addressList == null) {
                    Toast.makeText(showOnMap,
                            showOnMap.getString(R.string.error_noResultsFound),
                            Toast.LENGTH_LONG).show();
                } else {
                    for (Address a : showOnMap.getService().addressList) {
                        showOnMap.setAddress(a);
                        showOnMap.setAddr("");
                        for (int i = 0; i < a.getMaxAddressLineIndex() - 1; i++) {
                            showOnMap.setAddr(showOnMap.getAddr() + a.getAddressLine(i) + ", ");
                        }
                        showOnMap.setAddr(showOnMap.getAddr() + a
                                .getAddressLine(a.getMaxAddressLineIndex() - 1));
                        Log.d("fullAddress", showOnMap.getAddr());
                        showOnMap.setLatitude((showOnMap.getAddress().getLatitude()));
                        showOnMap.setLongitude((showOnMap.getAddress().getLongitude()));
                        LatLng nPoint = new LatLng(
                                (showOnMap.getAddress().getLatitude()),
                                (showOnMap.getAddress().getLongitude()));
                        showOnMap.getSearchEdt().setText(showOnMap.getAddr());
                        mapUtil.setMapCenter(showOnMap.getMv(), nPoint, showOnMap.getAddr());
                    }
                    return;
                }
            }
            case 3: {
                Log.d("handler", "from map change desc");
                return;
            }
        }
    }
}
