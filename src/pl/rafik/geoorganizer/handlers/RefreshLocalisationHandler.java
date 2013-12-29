package pl.rafik.geoorganizer.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.activities.main.AddEditTaskI;
import pl.rafik.geoorganizer.activities.map.ShowOnMap;

/**
 * Handler obslugujacy wspolbiezna obsluge wielowatkowa przy wcisnieciu
 * guzika.
 *
 * @author rafal.machnik
 */

public class RefreshLocalisationHandler extends Handler {

    private AddEditTaskI taskActivity;

    public RefreshLocalisationHandler(AddEditTaskI editTask) {
        this.taskActivity = editTask;
    }

    @Override
    public void handleMessage(Message msg) {
        if (taskActivity.getService().addressList.isEmpty() || taskActivity.getService().addressList == null) {
            Toast.makeText((Context) taskActivity,
                    ((Context) taskActivity).getString(R.string.error_noResultsFound),
                    Toast.LENGTH_LONG).show();
        } else {
            for (Address a : taskActivity.getService().addressList) {
                taskActivity.setAddress(a);
                for (int i = 0; i < a.getMaxAddressLineIndex() - 1; i++) {
                    taskActivity.setAddr(taskActivity.getAddr() + a.getAddressLine(i) + ", ");
                }
                taskActivity.setAddr(taskActivity.getAddr() + a.getAddressLine(a.getMaxAddressLineIndex() - 1));
                if (!taskActivity.getAddr().equals("")) {
                    Intent mapView = new Intent((Context) taskActivity,
                            ShowOnMap.class);
                    mapView.putExtra("Latitude", taskActivity.getAddress().getLatitude());
                    mapView.putExtra("Longitude", taskActivity.getAddress().getLongitude());
                    // aktywnosc uruchamiana w trybie request for result, w
                    // oczekiwaniu na potwierdzenie lolalizacji
                    ((Activity) taskActivity).startActivityForResult(mapView, 0);
                }

            }
        }
    }
}
