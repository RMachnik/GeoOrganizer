package pl.rafik.geoorganizer.activities.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.*;
import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.activities.main.pickers.DatePickerFragment;
import pl.rafik.geoorganizer.activities.main.pickers.TimePickerFragment;
import pl.rafik.geoorganizer.handlers.RefreshAddressHandler;
import pl.rafik.geoorganizer.handlers.RefreshLocalisationHandler;
import pl.rafik.geoorganizer.activities.map.ShowOnMap;
import pl.rafik.geoorganizer.model.dto.GeoLocalisation;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.model.entity.TaskOpenHelper;
import pl.rafik.geoorganizer.services.localisation.LocalisationService;
import pl.rafik.geoorganizer.services.localisation.MyBestLocation;
import pl.rafik.geoorganizer.services.IProximityAlertService;
import pl.rafik.geoorganizer.services.proximity.ProximityAlertScheduledService;
import pl.rafik.geoorganizer.services.data.*;
import pl.rafik.geoorganizer.services.localisation.MyBestLocation.LocationResult;

import java.util.Calendar;

/**
 * Dodawanie nowego zadania.
 *
 * @author Rafal
 */
public class NewTask extends Activity implements AddEditTaskI {

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
    private MyBestLocation bestLocation;

    // private AutocompleteService autoService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtask);
        initialiseHandlers();
        initialiseTaskForm();

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initialiseTaskForm() {
        radioGroup = (RadioGroup) findViewById(R.id.priority_group);
        findMap = (Button) findViewById(R.id.btn_find_on_map);
        setService(new LocalisationService(handler, NewTask.this));
        setService1(new LocalisationService(simpleHandler, NewTask.this));
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
        proximityService = new ProximityAlertScheduledService(this);
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
                                mapIntent.putExtra("Desc", getAddr());
                                mapIntent.putExtra("Titile", taskName.getText());
                                NewTask.this.startActivityForResult(mapIntent,
                                        0);
                            } else {
                                Toast.makeText(NewTask.this,
                                        getApplicationContext().getString(R.string.toast_waiting),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    bestLocation = new MyBestLocation();
                    if (bestLocation.getLocation(NewTask.this, locationResult)) {
                        locationResult.gotLocation(location);
                    }

                } else {
                    getService().getAddresFromName(placeName.getText().toString());

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

                    getService1().getAddresFromName(placeName.getText().toString());
                }
            }

        });
    }

    private void initialiseHandlers() {
        handler = new RefreshLocalisationHandler(this);
        simpleHandler = new RefreshAddressHandler(this);
    }

    // !!!!!!!!!!!!!!!! poprawic problem z zapisywaniem danych do lokalizatora.

    /*
     * Metoda dodajaca nowe zadanie do bazy, zdefiniowane w formularzu
     * aktywnosci.
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void addNewTask() throws DbxException {
        TaskDTO taskDTO = new TaskDTO();
        if (taskName.getText().toString().isEmpty()) {
            taskName.setError(getApplicationContext().getString(R.string.error_fieldRequired));
            return;
        } else if (placeName.getText().toString().isEmpty()) {
            placeName.setError(getApplicationContext().getString(R.string.error_fieldRequired));
            return;
        } else if (dedline.getText().toString().isEmpty()) {
            dedline.setError(getApplicationContext().getString(R.string.error_fieldRequired));
            return;
        }// do sprawdzenia!!!!!!!!!!!!!
        // else if (checkActualDate()) {
        // dedline.setError("!");
        // return;
        // }
        if (!(getService1().addressList.isEmpty() && getAddr().isEmpty())) {
            taskDTO.setNote(taskName.getText().toString());
            if (edtTime.getText().toString().isEmpty())
                taskDTO.setDate(prepareDedline(dedline.getText().toString(),
                        "00:00"));
            else
                taskDTO.setDate(prepareDedline(dedline.getText().toString(),
                        edtTime.getText().toString()));
            taskDTO.setStatus(TaskOpenHelper.NOT_DONE);
            getService1().getAddresFromName(placeName.getText().toString());
            if (!getAddr().equals("")) {
                GeoLocalisation geo = new GeoLocalisation();
                geo.setLatitude(String.valueOf(getAddress().getLatitude()));
                geo.setLongitude(String.valueOf(getAddress().getLongitude()));

                geo.setLocalistationAddress(getAddr());
                taskDTO.setLocalisation(geo);
            } else {
                placeName.setError(getApplicationContext().getString(R.string.error_localisationName));
            }
            int selected = radioGroup.getCheckedRadioButtonId();
            RadioButton button = (RadioButton) findViewById(selected);
            taskDTO.setPriority(button.getText().toString());
            taskDTO.setId(String.valueOf(taskService.addNewTask(taskDTO)));
            if (!taskDTO.getId().equals("0")) {
                // wystartowanie nowej aktywnosci
                if (proximityService.addProximityAlert(taskDTO)) {
                    Toast.makeText(NewTask.this, getApplicationContext().getString(R.string.toast_taskAdded),
                            Toast.LENGTH_SHORT).show();
                    vibrator.vibrate(200);
                    Intent ret = this.getIntent();
                    setResult(Welcome.NEWTASK, ret);
                    finish();
                }
            } else {
                Toast.makeText(this, getApplicationContext().getString(R.string.toast_dbSaveFail), Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            placeName.setError(getApplicationContext().getString(R.string.error_placeName));
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
    @Override
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

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(this);
        newFragment.show(fragmentManager, "datePicker");
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(this);
        newFragment.show(fragmentManager, "timePicker");
    }

    @Override
    public void updateDate(int year, int month, int day) {
        dedline.setText(String.valueOf(day) + "-" + String.valueOf(month) + "-"
                + String.valueOf(year));
    }

    @Override
    public void updateTime(int hour, int minute) {
        edtTime.setText(String.valueOf(hour) + ":" + String.valueOf(minute));
    }

    @Override
    public String prepareDedline(String date, String time) {
        // SimpleDateFormat sdf = new SimpleDateFormat()
        return date + " " + time;
    }

    @Override
    public LocalisationService getService() {
        return service;
    }

    public void setService(LocalisationService service) {
        this.service = service;
    }

    @Override
    public LocalisationService getService1() {
        return service1;
    }

    public void setService1(LocalisationService service1) {
        this.service1 = service1;
    }

    @Override
    public TaskService getTaskService() {
        return taskService;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String getAddr() {
        return addr;
    }

    @Override
    public void setAddr(String addr) {
        this.addr = addr;
    }


}
