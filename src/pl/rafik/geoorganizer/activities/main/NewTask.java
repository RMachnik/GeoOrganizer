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
import pl.rafik.geoorganizer.services.IProximityAlertService;
import pl.rafik.geoorganizer.services.impl.LocalisationService;
import pl.rafik.geoorganizer.services.impl.MyBestLocation;
import pl.rafik.geoorganizer.services.impl.MyBestLocation.LocationResult;
import pl.rafik.geoorganizer.services.impl.ProximityAlertService;
import pl.rafik.geoorganizer.services.impl.TaskService;

import java.util.Calendar;

/**
 * Dodawanie nowego zadania.
 *
 * @author Rafal
 */
public class NewTask extends Activity {

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
    private Vibrator vibrator;
    private FragmentManager fragmentManager;
    private EditText edtTime;
    private IProximityAlertService proximityService;

    // private AutocompleteService autoService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtask);
        handler = new RefreshHandler();
        simpleHandler = new RefreshHandler1();
        radioGroup = (RadioGroup) findViewById(R.id.priority_group);
        findMap = (Button) findViewById(R.id.btn_find_on_map);
        service = new LocalisationService(handler, NewTask.this);
        service1 = new LocalisationService(simpleHandler, NewTask.this);
        taskService = new TaskService(this);
        placeName = (TextView) findViewById(R.id.postalAddress);
        // autoService = new AutocompleteService();

        // placeName.setAdapter(new PlacesAutocompleteAdapter(this,
        // R.layout.list_autocomplete_item, autoService));
        // placeName.setOnItemClickListener(new OnItemClickListener() {
        //
        // @Override
        // public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        // long arg3) {
        // String str = (String) arg0.getItemAtPosition(arg2);
        // Toast.makeText(NewTask.this, str, Toast.LENGTH_SHORT).show();
        // }
        // });
        taskName = (TextView) findViewById(R.id.task_note);
        dedline = (TextView) findViewById(R.id.dedline);
        edtTime = (EditText) findViewById(R.id.nt_edt_time);
        fragmentManager = this.getFragmentManager();
        proximityService = new ProximityAlertService(this);
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
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        findMap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (placeName.getText().toString().isEmpty()) {
                    Location location = null;
                    LocationResult locationResult = new LocationResult() {

                        // metoda dziala po otrzymaniu lokacji z MyBestLocation
                        @Override
                        public void gotLocation(Location location) {
                            if (location != null) {
                                Intent mapIntent = new Intent(NewTask.this,
                                        ShowOnMap.class);
                                mapIntent.putExtra("Latitude",
                                        location.getLatitude());
                                mapIntent.putExtra("Longitude",
                                        location.getLongitude());
                                mapIntent.putExtra("Desc", addr);
                                mapIntent.putExtra("Titile", taskName.getText());
                                NewTask.this.startActivityForResult(mapIntent,
                                        0);
                            } else {
                                Toast.makeText(NewTask.this,
                                        "Czekam na lokalizacje...",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    MyBestLocation bestLocation = new MyBestLocation();
                    if (bestLocation.getLocation(NewTask.this, locationResult)) {
                        locationResult.gotLocation(location);
                    }

                } else {
                    service.getAddresFromName(placeName.getText().toString());

                }
            }
        });

        save = (Button) findViewById(R.id.btn_addTask);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    addNewTask();
                } catch (DbxException e) {
                    e.printStackTrace();
                }
            }

        });

        placeName.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1 == false && placeName.getText().length() > 2) {

                    service1.getAddresFromName(placeName.getText().toString());
                }
            }

        });

    }

    // !!!!!!!!!!!!!!!! poprawic problem z zapisywaniem danych do lokalizatora.

    /*
     * Metoda dodajaca nowe zadanie do bazy, zdefiniowane w formularzu
     * aktywnosci.
     */
    public void addNewTask() throws DbxException {
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
        }// do sprawdzenia!!!!!!!!!!!!!
        // else if (checkActualDate()) {
        // dedline.setError("!");
        // return;
        // }
        if (!(service1.addressList.isEmpty() && addr.isEmpty())) {
            taskDTO.setNote(taskName.getText().toString());
            if (edtTime.getText().toString().isEmpty())
                taskDTO.setDate(prepareDedline(dedline.getText().toString(),
                        "00:00"));
            else
                taskDTO.setDate(prepareDedline(dedline.getText().toString(),
                        edtTime.getText().toString()));
            taskDTO.setStatus("NOT");
            service1.getAddresFromName(placeName.getText().toString());
            if (!addr.equals("")) {
                GeoLocalisation geo = new GeoLocalisation();
                geo.setLatitude(String.valueOf((int) (address.getLatitude() * 1000000)));
                geo.setLongitude(String.valueOf((int) (address.getLongitude() * 1000000)));

                geo.setLocalistationAddress(addr);
                taskDTO.setLocalisation(geo);
            } else {
                placeName.setError("Prosze wprowadzic poprawna lokalizacje!");
            }
            int selected = radioGroup.getCheckedRadioButtonId();
            RadioButton button = (RadioButton) findViewById(selected);
            taskDTO.setPriority(button.getText().toString());
            taskDTO.setId(taskService.addNewTask(taskDTO));
            if (taskDTO.getId() >= 0) {
                // wystartowanie nowej aktywnosci
                if (proximityService.addProximityAlert(taskDTO)) {
                    Toast.makeText(NewTask.this, "Dodano zadanie!",
                            Toast.LENGTH_SHORT).show();
                    vibrator.vibrate(200);
                    Intent ret = this.getIntent();
                    setResult(Welcome.NEWTASK, ret);
                    finish();
                }
            } else {
                Toast.makeText(this, "Blad zapisu do bazy!", Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            placeName.setError("Niewlasciwa lokalizacja wprowadz inna!");
            return;
        }

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

    // **********Metody dodatkowe obsluga daty i czasu**********

    // gdy data jest nieaktualna zwraca true
    public boolean checkActualDate() {
        Calendar c = Calendar.getInstance();

        Calendar tmp = Calendar.getInstance();
        String dtime[] = dedline.getText().toString().split(" ");
        if (dtime.length > 1) {
            String data[] = dtime[0].split("-");
            tmp.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data[0]));
            tmp.set(Calendar.MONTH, Integer.parseInt(data[1]));
            tmp.set(Calendar.YEAR, Integer.parseInt(data[2]));
            String[] time = dtime[1].split(":");
            tmp.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
            tmp.set(Calendar.MINUTE, Integer.parseInt(time[1]));
            return c.after(tmp);
        }
        return true;
    }

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
        // SimpleDateFormat sdf = new SimpleDateFormat()
        return date + " " + time;
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
                Toast.makeText(NewTask.this,
                        "Nie znaleziono pasujacych rezultatow!",
                        Toast.LENGTH_LONG).show();
            } else {
                for (Address a : service.addressList) {
                    address = a;
                    addr = "";
                    for (int i = 0; i < a.getMaxAddressLineIndex() - 1; i++) {
                        addr += a.getAddressLine(i) + ", ";
                    }
                    addr += a.getAddressLine(a.getMaxAddressLineIndex() - 1);
                    if (!addr.equals("")) {
                        Intent mapView = new Intent(NewTask.this,
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
            if (service1.addressList == null || service1.addressList.isEmpty()) {
                Toast.makeText(NewTask.this,
                        "Nie znaleziono pasujacych rezultatow!",
                        Toast.LENGTH_LONG).show();
            } else {
                for (Address a : service1.addressList) {
                    address = a;
                    addr = "";
                    for (int i = 0; i < a.getMaxAddressLineIndex(); i++) {
                        addr += a.getAddressLine(i) + ", ";
                    }
                }
            }
        }
    }

    ;

    @SuppressLint("ValidFragment")
    private class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(NewTask.this, this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            updateDate(year, monthOfYear, dayOfMonth);

        }

    }

    ;

    @SuppressLint("ValidFragment")
    private class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(NewTask.this, this, hour, minute,
                    DateFormat.is24HourFormat(NewTask.this));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            updateTime(hourOfDay, minute);

        }

    }

    ;

}
