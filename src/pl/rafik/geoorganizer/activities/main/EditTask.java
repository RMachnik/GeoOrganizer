package pl.rafik.geoorganizer.activities.main;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.*;
import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.activities.map.ShowOnMap;
import pl.rafik.geoorganizer.model.dto.GeoLocalisation;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.services.impl.LocalisationService;
import pl.rafik.geoorganizer.services.impl.MyBestLocation;
import pl.rafik.geoorganizer.services.impl.MyBestLocation.LocationResult;
import pl.rafik.geoorganizer.services.impl.TaskService;

import java.util.Calendar;

import static pl.rafik.geoorganizer.model.entity.TaskOpenHelper.NOT_DONE;

/**
 * Edycja tasku.
 *
 * @author rafal.machnik
 */
public class EditTask extends Activity {

    private Button findMap;
    private Button save;
    private LocalisationService service = null;
    private LocalisationService service1 = null;
    private TaskService taskService = null;
    private Handler handler;
    private Handler simpleHandler;
    private TextView placeName;
    private TextView taskName;
    private TextView dedline;
    private RadioGroup radioGroup;
    private Address address;
    private String addr = "";
    private TaskDTO dto;
    private Vibrator vibrator;
    private FragmentManager fragmentManager;
    private TextView edtTime;
    private MyBestLocation bestLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);
        // inicjacja serwisu
        taskService = new TaskService(getApplicationContext());

        loadTask();

        initialiseHandlers();
        initialiseTaskEditForm();

    }

    private void loadTask() {
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("ID");
        try {
            dto = taskService.getTask(id);
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }

    private void initialiseTaskEditForm() {
        fragmentManager = this.getFragmentManager();
        radioGroup = (RadioGroup) findViewById(R.id.edt_priority_group);
        findMap = (Button) findViewById(R.id.edt_btn_find_on_map);
        service = new LocalisationService(handler, EditTask.this);
        service1 = new LocalisationService(simpleHandler, EditTask.this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        placeName = (TextView) findViewById(R.id.edt_postalAddress);
        placeName.setText(dto.getLocalisation().getLocalistationAddress());
        edtTime = (TextView) findViewById(R.id.edit_edt_time);
        dedline = (TextView) findViewById(R.id.edt_dedline);

        String dtime[] = dto.getDate().split(" ");
        if (dtime.length > 1) {
            edtTime.setText(dtime[1]);
            dedline.setText(dtime[0]);
        } else {
            dedline.setText(dto.getDate());
        }

        dedline.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1 == true)
                    showDatePickerDialog(arg0);
            }

        });
        edtTime.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1 == true)
                    showTimePickerDialog(arg0);
            }

        });
        taskName = (TextView) findViewById(R.id.edt_task_note);
        taskName.setText(dto.getNote());

        addr = dto.getLocalisation().getLocalistationAddress();

        // ustawianie priorytetow
        if (dto.getPriority().equals("wysoki")) {
            RadioButton btn = (RadioButton) findViewById(R.id.edt_priority_high);
            btn.setSelected(true);
        } else if (dto.getPriority().equals("normalny")) {
            RadioButton btn = (RadioButton) findViewById(R.id.edt_priority_normal);
            btn.setSelected(true);
        } else if (dto.getPriority().equals("niski")) {
            RadioButton btn = (RadioButton) findViewById(R.id.edt_priority_low);
            btn.setSelected(true);
        }

        findMap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (placeName.getText().toString().isEmpty()) {
                    Location location = null;
                    LocationResult locationResult = new LocationResult() {

                        // metoda dziala po otrzymaniu lokacji z MyBestLocation
                        @Override
                        public void gotLocation(Location location) {
                            Intent mapIntent = new Intent(EditTask.this,
                                    ShowOnMap.class);
                            mapIntent.putExtra("Latitude",
                                    location.getLatitude());
                            mapIntent.putExtra("Longitude",
                                    location.getLongitude());
                            EditTask.this.startActivity(mapIntent);
                        }
                    };
                    bestLocation = new MyBestLocation();
                    if (bestLocation.getLocation(EditTask.this, locationResult)) {
                        locationResult.gotLocation(location);
                    }

                } else {
                    service.getAddresFromName(placeName.getText().toString());

                }
            }
        });

        save = (Button) findViewById(R.id.edt_btn_addTask);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                editTask();
            }

        });

        placeName.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1 == false
                        && !placeName
                        .getText()
                        .toString()
                        .equals(dto.getLocalisation()
                                .getLocalistationAddress())
                        && placeName.getText().length() > 2)
                    service1.getAddresFromName(placeName.getText().toString());
            }

        });
    }

    private void initialiseHandlers() {
        handler = new RefreshHandler();
        simpleHandler = new RefreshHandler1();
    }

    /*
     * Metoda dodajaca nowe zadanie do bazy, zdefiniowane w formularzu
     * aktywnosci.
     */
    public void editTask() {
        TaskDTO taskDTO = new TaskDTO();
        if (taskName.getText().toString().isEmpty()) {
            taskName.setError("Pole wymagane!");
            return;
        } else if (placeName.getText().toString().isEmpty()) {
            placeName.setError("Pole wymagane!");
            return;
        } else if (dedline.getText().toString().isEmpty()) {
            dedline.setError("Pole wymagane!");
            return;
        }
        if (!service1.addressList.isEmpty()
                || !placeName.getText().toString().isEmpty()) {
            taskDTO.setNote(taskName.getText().toString());
            taskDTO.setDate(dedline.getText().toString());
            taskDTO.setStatus(NOT_DONE);
            if (!addr.equals("")) {
                GeoLocalisation geo = new GeoLocalisation();
                if (address != null) {
                    geo.setLatitude(String.valueOf(address.getLatitude()));
                    geo.setLongitude(String.valueOf(address.getLongitude()));

                    geo.setLocalistationAddress(addr);
                } else {
                    geo.setLatitude(dto.getLocalisation().getLatitude());
                    geo.setLongitude(dto.getLocalisation().getLongitude());
                    geo.setLocalistationAddress(dto.getLocalisation()
                            .getLocalistationAddress());
                }

                taskDTO.setLocalisation(geo);
            } else {
                placeName.setError("Prosze wprowadzic poprawna lokalizacje");
            }
            int selected = radioGroup.getCheckedRadioButtonId();
            RadioButton button = (RadioButton) findViewById(selected);
            taskDTO.setPriority(button.getText().toString());
            taskDTO.setId(dto.getId());
            try {
                if (taskService.updateTask(taskDTO) > 0) {
                    Intent editIntent = this.getIntent();
                    setResult(RESULT_OK, editIntent);
                    vibrator.vibrate(200);
                    Toast.makeText(EditTask.this, "Edycja udana.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditTask.this, "Edycja nieudana!",
                            Toast.LENGTH_LONG).show();

                }
            } catch (DbxException e) {
                e.printStackTrace();
            }
        } else {
            placeName.setError("Niewlasciwa lokalizacja wprowadz inna!");
            return;
        }

    }

    // **********Metody dodatkowe obsluga daty i czasu**********

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(fragmentManager, "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(fragmentManager, "timePicker");
    }

    public void updateDate(int year, int month, int day) {
        dedline.setText(String.valueOf(day) + "-" + String.valueOf(month) + "-"
                + String.valueOf(year));
    }

    public void updateTime(int hour, int minute) {
        edtTime.setText(String.valueOf(hour) + ":" + String.valueOf(minute));
    }

    public String prepareDedline(String date, String time) {
        return date + " " + time;
    }

    /**
     * Metoda obslugujaca powrot z aktywnosci pokazujacej lokalizacje na mapie.
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActiityResult", "wszedlem!!!!");

        if (resultCode == RESULT_OK) {

            Log.d("PlaceName", data.getStringExtra("PlaceName"));
            placeName.setText(data.getStringExtra("PlaceName"));

        }

    }

    /**
     * Handler obslugujacy wspolbiezna obsluge wielowatkowa przy wcisnieciu
     * guzika.
     *
     * @author rafal.machnik
     */

    private class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (service.addressList.isEmpty() || service.addressList == null) {
                Toast.makeText(EditTask.this,
                        "Nie znaleziono pasujacych rezultatow!",
                        Toast.LENGTH_LONG).show();
            } else {
                for (Address a : service.addressList) {
                    address = a;
                    for (int i = 0; i < a.getMaxAddressLineIndex() - 1; i++) {
                        addr += a.getAddressLine(i) + ", ";
                    }
                    addr += a.getAddressLine(a.getMaxAddressLineIndex() - 1);
                    if (!addr.equals("")) {
                        Intent mapView = new Intent(EditTask.this,
                                ShowOnMap.class);
                        mapView.putExtra("Latitude", address.getLatitude());
                        mapView.putExtra("Longitude", address.getLongitude());
                        // aktywnosc uruchamiana w trybie request for result, w
                        // oczekiwaniu na potwierdzenie lolalizacji
                        startActivityForResult(mapView, 0);
                    }

                }
            }
        }
    }

    ;

    private class RefreshHandler1 extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (service1.addressList.isEmpty() || service1.addressList == null) {
                Toast.makeText(EditTask.this,
                        "Nie znaleziono pasujacych rezultatow!",
                        Toast.LENGTH_LONG).show();
            } else {
                for (Address a : service1.addressList) {
                    address = a;
                    for (int i = 0; i < a.getMaxAddressLineIndex(); i++) {
                        addr += a.getAddressLine(i) + ", ";
                    }
                }
            }
        }
    }

    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(EditTask.this, this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            updateDate(year, monthOfYear, dayOfMonth);

        }

    }

    ;

    @SuppressLint("ValidFragment")
    public class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(EditTask.this, this, hour, minute,
                    DateFormat.is24HourFormat(EditTask.this));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            updateTime(hourOfDay, minute);

        }

    }

    ;

}
