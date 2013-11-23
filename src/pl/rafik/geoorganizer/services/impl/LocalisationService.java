package pl.rafik.geoorganizer.services.impl;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.util.Log;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.services.ILocalisationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalisationService implements ILocalisationService {
    private static final int MAX_SEARCH = 1;
    public List<Address> addressList;
    private Handler uiCallback;
    private Geocoder geoCoder;
    private int timeout;

    public LocalisationService(Handler callBack, Context c) {
        this.addressList = new ArrayList<Address>();
        this.uiCallback = callBack;
        this.geoCoder = new Geocoder(c);
        timeout = Integer.parseInt(c.getString(R.string.timeout));
    }

    @Override
    public void getAdress(final double latitude, final double longitude) {
        Thread thrd = new Thread() {
            @Override
            public void run() {
                try {
                    addressList.clear();
                    addressList = geoCoder.getFromLocation(latitude, longitude,
                            MAX_SEARCH);
                    sleep(timeout);
                    uiCallback.sendEmptyMessage(1);
                } catch (IOException e) {
                    Log.d("Fail Geokodowania gedAdress", e.getMessage());
                    uiCallback.sendEmptyMessage(-1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thrd.start();

    }

    @Override
    public void getAddresFromName(final String name) {
        Thread thrd = new Thread() {

            @Override
            public void run() {
                try {
                    addressList.clear();
                    addressList = geoCoder
                            .getFromLocationName(name, MAX_SEARCH);
                    sleep(timeout);
                    uiCallback.sendEmptyMessage(2);
                } catch (IOException e) {
                    Log.d("Fail Geokodowania getAddressFromName",
                            e.getMessage());
                    uiCallback.sendEmptyMessage(-1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        thrd.start();
    }
}
