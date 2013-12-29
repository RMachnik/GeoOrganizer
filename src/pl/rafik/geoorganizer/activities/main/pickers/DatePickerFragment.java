package pl.rafik.geoorganizer.activities.main.pickers;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;
import pl.rafik.geoorganizer.activities.main.AddEditTaskI;

import java.util.Calendar;

/**
* rafik991@gmail.com
* 12/29/13
*/
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {
    private AddEditTaskI newTask;

    public DatePickerFragment(AddEditTaskI newTask) {
        this.newTask = newTask;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog((Context) newTask, this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        newTask.updateDate(year, monthOfYear, dayOfMonth);

    }

}
