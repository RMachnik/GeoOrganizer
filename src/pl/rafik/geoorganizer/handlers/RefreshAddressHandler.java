package pl.rafik.geoorganizer.handlers;

import android.content.Context;
import android.location.Address;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.activities.main.AddEditTaskI;

/**
 * rafik991@gmail.com
 * 12/29/13
 */
public class RefreshAddressHandler extends Handler {
    private AddEditTaskI taskActivity;

    public RefreshAddressHandler(AddEditTaskI editTask) {
        this.taskActivity = editTask;
    }

    @Override
    public void handleMessage(Message msg) {
        if (taskActivity.getService1().addressList.isEmpty() || taskActivity.getService1().addressList == null) {
            Toast.makeText((Context) taskActivity,
                    ((Context) taskActivity).getString(R.string.error_noResultsFound),
                    Toast.LENGTH_LONG).show();
        } else {
            for (Address a : taskActivity.getService1().addressList) {
                taskActivity.setAddress(a);
                for (int i = 0; i < a.getMaxAddressLineIndex(); i++) {
                    taskActivity.setAddr(taskActivity.getAddr() + a.getAddressLine(i) + ", ");
                }
            }
        }
    }
}
