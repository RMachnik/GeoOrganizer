package pl.rafik.geoorganizer.activities.main.pickers;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import pl.rafik.geoorganizer.activities.main.AddEditTaskI;

import java.util.Calendar;

/**
 * rafik991@gmail.com
 * 12/29/13
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener {
    private AddEditTaskI editTask;

    public TimePickerFragment(AddEditTaskI editTask) {
        this.editTask = editTask;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog((Context) editTask, this, hour, minute,
                DateFormat.is24HourFormat((Context) editTask));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        editTask.updateTime(hourOfDay, minute);

    }

}
