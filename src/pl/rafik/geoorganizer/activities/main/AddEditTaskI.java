package pl.rafik.geoorganizer.activities.main;

import android.annotation.TargetApi;
import android.location.Address;
import android.os.Build;
import android.view.View;
import pl.rafik.geoorganizer.services.localisation.LocalisationService;
import pl.rafik.geoorganizer.services.data.TaskService;

/**
 * rafik991@gmail.com
 * 12/29/13
 */
public interface AddEditTaskI {
    // gdy data jest nieaktualna zwraca true
    boolean checkActualDate();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    void showDatePickerDialog(View v);

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    void showTimePickerDialog(View v);

    void updateDate(int year, int month, int day);

    void updateTime(int hour, int minute);

    String prepareDedline(String date, String time);

    public LocalisationService getService();

    public LocalisationService getService1();

    public TaskService getTaskService();

    public Address getAddress();

    public void setAddress(Address address);

    public String getAddr();

    public void setAddr(String addr);
}
