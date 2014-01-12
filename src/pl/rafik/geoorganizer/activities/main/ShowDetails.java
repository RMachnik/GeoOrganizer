package pl.rafik.geoorganizer.activities.main;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.*;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.activities.dbx.DbxStart;
import pl.rafik.geoorganizer.activities.map.ShowListOnMap;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.model.entity.TaskOpenHelper;
import pl.rafik.geoorganizer.services.ITaskService;
import pl.rafik.geoorganizer.services.data.TaskService;

import java.util.Calendar;

public class ShowDetails extends Activity {
    private ITaskService taskService;
    private FragmentManager fragmentManager;
    private DbxStart dbxStart;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        initialiseDbx();
        initialiseServices();
        initialiseDetails();

    }

    private void initialiseDbx() {
        dbxStart = new DbxStart();
        dbxStart.initialiseDbx(this.getApplicationContext());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initialiseServices() {
        taskService = new TaskService(this);
        fragmentManager = this.getFragmentManager();
    }

    private void initialiseDetails() {
        final TaskDTO dto;
        TaskDTO dto1 = null;
        try {
            dto1 = taskService.getTask(this.getIntent().getExtras()
                    .getString(TaskOpenHelper.ID));
        } catch (DbxException e) {
            e.printStackTrace();
        }
        dto = dto1;
        TextView tvDialog = (TextView) findViewById(R.id.dialog_tv);
        setTitle(dto1.getNote());
        tvDialog.setText("wazny do:" + dto1.getDate()
                + System.getProperty("line.separator") + "priorytet: "
                + dto.getPriority() + " adres:"
                + dto.getLocalisation().getLocalistationAddress());
        final ImageView image = (ImageView) findViewById(R.id.dialog_image);
        if (dto1.getStatus().equals("NOT")) {
            image.setImageResource(R.drawable.ic_not);
        } else {
            image.setImageResource(R.drawable.ic_ok);
        }
        Button makeDone = (Button) findViewById(R.id.makeDoneBtn);
        Button dialogButton = (Button) findViewById(R.id.dialogButtonOK);
        Button showOnMap = (Button) findViewById(R.id.btn_showOnMap);

        showOnMap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String[] ids = {dto.getId()};
                Intent showOnMapIntent = new Intent(ShowDetails.this,
                        ShowListOnMap.class);
                showOnMapIntent.putExtra("IDS", ids);
                ShowDetails.this.startActivity(showOnMapIntent);

            }

        });
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        makeDone.setOnClickListener(new OnClickListener() {
            // zmiana logiki bo pasuje zeby pytalo czy zmienic date jesli
            // zmieniamy status z wykonanego na niewykonane
            @Override
            public void onClick(View arg0) {
                if (dto.getStatus().equals("NOT")) {
                    try {
                        if (taskService.makeDone(dto.getId()) > 0) {

                            Toast.makeText(ShowDetails.this, "Zmieniono status!",
                                    Toast.LENGTH_SHORT).show();
                            image.setImageResource(R.drawable.ic_ok);
                        } else {
                            Toast.makeText(ShowDetails.this,
                                    "Status nie zostal zmieniony!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (DbxException e) {
                        e.printStackTrace();
                    }

                } else {
                    Calendar c = Calendar.getInstance();

                    Calendar tmp = Calendar.getInstance();
                    String dtime[] = dto.getDate().split(" ");
                    if (dtime.length > 1) {
                        String data[] = dtime[0].split("-");
                        tmp.set(Calendar.DAY_OF_MONTH,
                                Integer.parseInt(data[0]));
                        tmp.set(Calendar.MONTH, Integer.parseInt(data[1]));
                        tmp.set(Calendar.YEAR, Integer.parseInt(data[2]));
                        String[] time = dtime[1].split(":");
                        tmp.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                        tmp.set(Calendar.MINUTE, Integer.parseInt(time[1]));
                    } else {
                        Log.d("DAO", "blad parsowania daty");
                    }
                    if (c.before(tmp)) {
                        try {
                            if (taskService.makeNotDone(dto.getId()) > 0) {
                                Toast.makeText(ShowDetails.this,
                                        "Zmieniono status!", Toast.LENGTH_SHORT)
                                        .show();
                                image.setImageResource(R.drawable.ic_not);
                            } else {
                                Toast.makeText(ShowDetails.this,
                                        "Status nie zostal zmieniony!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (DbxException e) {
                            e.printStackTrace();
                        }
                        // kiedy data jest nieaktualna
                    } else {
                        Toast.makeText(ShowDetails.this,
                                "Zmien date na pozniejsza!", Toast.LENGTH_LONG)
                                .show();
                        showDatePickerDialog(arg0, dto, image);

                    }
                }
            }

        });
    }

    public void showDatePickerDialog(View v, TaskDTO dto, ImageView image) {
        DialogFragment newFragment = new DatePickerFragment(dto, image);
        newFragment.show(fragmentManager, "datePicker");
    }

    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        TaskDTO dto;
        ImageView image;

        public DatePickerFragment(TaskDTO d, ImageView im) {
            dto = d;
            image = im;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(ShowDetails.this, this, year, month,
                    day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String updateDate = String.valueOf(dayOfMonth) + "-"
                    + String.valueOf(monthOfYear) + "-" + String.valueOf(year);
            dto.setDate(updateDate);
            try {
                taskService.updateTask(dto);
                taskService.makeNotDone(dto.getId());
            } catch (DbxException e) {
                e.printStackTrace();
            }
            image.setImageResource(R.drawable.ic_not);

        }

    }

    ;
}
