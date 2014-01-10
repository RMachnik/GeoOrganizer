package pl.rafik.geoorganizer.activities.main;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.*;
import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.activities.list.TaskArrayAdapter;
import pl.rafik.geoorganizer.activities.map.ShowListOnMap;
import pl.rafik.geoorganizer.model.dto.EmailBuilder;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.model.entity.TaskOpenHelper;
import pl.rafik.geoorganizer.services.IEmailService;
import pl.rafik.geoorganizer.services.IProximityAlertService;
import pl.rafik.geoorganizer.services.email.EmailService;
import pl.rafik.geoorganizer.services.proximity.ProximityAlertService;
import pl.rafik.geoorganizer.services.data.TaskService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Klasa obslugujaca listowanie zadan ktore nie zostaly jeszcze wykonane,
 * domyslnie lista powinna miec opcje ktore beda zmienialy jej wyswietlane
 * elementy tzn. zadania wszystkie, wykonane itp ;)
 *
 * @author Rafal
 */
public class TaskList extends ListActivity {

    private TaskService taskService;
    private TaskArrayAdapter adapter;
    private Vibrator vibrator;
    private IProximityAlertService proximityService;
    private String updateDate;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseServices();
        updateDate = "";
        setListAdapter(adapter);
        registerForContextMenu(this.getListView());
        if (adapter.isEmpty()) {
            Toast.makeText(this,
                    "Lista aktualnych zadan do wykonania jest pusta!",
                    Toast.LENGTH_LONG).show();
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initialiseServices() {
        taskService = new TaskService(TaskList.this);
        proximityService = new ProximityAlertService(this);
        try {
            adapter = new TaskArrayAdapter(TaskList.this,
                    taskService
                            .getActualTasks());
        } catch (DbxException e) {
            e.printStackTrace();
        }
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        fragmentManager = this.getFragmentManager();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        TaskDTO dto = null;
        try {
            dto = taskService.getTask(adapter.getItemMyId(info.position));
        } catch (DbxException e) {
            e.printStackTrace();
        }
        menu.setHeaderTitle("wazny do:" + dto.getDate()
                + System.getProperty("line.separator") + "priorytet: "
                + dto.getPriority() + System.getProperty("line.separator")
                + " adres:" + dto.getLocalisation().getLocalistationAddress()
                + System.getProperty("line.separator"));
        menu.add(0, R.id.mn_editTask, 0, "Edytuj");
        menu.add(0, R.id.mn_deleteTask, 1, "Usun");
        menu.add(0, R.id.mn_sendEmail, 1, "Wyslij");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        TaskDTO dto = null;
        try {
            dto = taskService.getTask(String.valueOf(adapter.getItemMyId(info.position)));
        } catch (DbxException e) {
            e.printStackTrace();
        }
        switch (item.getItemId()) {
            case R.id.mn_editTask: {

                Intent edit = new Intent(TaskList.this, EditTask.class);
                edit.putExtra("ID", dto.getId());
                TaskList.this.startActivityForResult(edit, 0);
                return true;
            }
            case R.id.mn_deleteTask: {
                Log.d("wszedlem do", "delete!!!!");
                assert dto != null;
                try {
                    if (taskService.deleteTask(dto.getId()) > 0) {
                        proximityService.removeAlert(dto);
                        vibrator.vibrate(200);
                        Toast.makeText(TaskList.this, "Usunieto.", Toast.LENGTH_SHORT)
                                .show();
                        adapter = new TaskArrayAdapter(TaskList.this,
                                taskService.getNotDoneTasks());
                        TaskList.this.setListAdapter(adapter);

                    } else {
                        Toast.makeText(TaskList.this, "Nie usunieto zdarzneia!",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (DbxException e) {
                    e.printStackTrace();
                }
                return true;
            }
            case R.id.mn_sendEmail: {
                Log.d("wszedlem do", "wysylania maila!!!");
                EmailBuilder emailBuilder = new EmailBuilder(this.getApplicationContext());

                IEmailService service = new EmailService();
                try {
                    service.sendEmail(emailBuilder.buildCurrentTask(dto.getId()), this);
                } catch (DbxException e) {
                    e.printStackTrace();
                }

            }

        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final Dialog dialog = new Dialog(TaskList.this);
        final TaskDTO dto;
        TaskDTO dto1 = null;
        try {
            dto1 = taskService.getTask(adapter.getItemMyId(position));
        } catch (DbxException e) {
            e.printStackTrace();
        }
        dto = dto1;
        dialog.setContentView(R.layout.custom_dialog);
        TextView tvDialog = (TextView) dialog.findViewById(R.id.dialog_tv);
        dialog.setTitle(dto.getNote());
        tvDialog.setText("wazny do:" + dto.getDate()
                + System.getProperty("line.separator") + "priorytet: "
                + dto.getPriority() + " adres:"
                + dto.getLocalisation().getLocalistationAddress());
        final ImageView image = (ImageView) dialog
                .findViewById(R.id.dialog_image);
        if (dto.getStatus().equals("NOT")) {
            image.setImageResource(R.drawable.ic_not);
        } else {
            image.setImageResource(R.drawable.ic_ok);
        }
        Button makeDone = (Button) dialog.findViewById(R.id.makeDoneBtn);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        Button showOnMap = (Button) dialog.findViewById(R.id.btn_showOnMap);

        showOnMap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String[] ids = {dto.getId()};
                Intent showOnMapIntent = new Intent(TaskList.this,
                        ShowListOnMap.class);
                showOnMapIntent.putExtra(TaskOpenHelper.ID, ids);
                TaskList.this.startActivity(showOnMapIntent);

            }

        });

        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                List<TaskDTO> lista = adapter.getCurrentTaskList();
                List<TaskDTO> refreshed = new ArrayList<TaskDTO>();
                if (!lista.isEmpty()) {

                    for (TaskDTO dto : lista) {
                        try {
                            refreshed.add(taskService.getTask(dto.getId()));
                        } catch (DbxException e) {
                            e.printStackTrace();
                        }
                    }
                }
                TaskList.this.setListAdapter(new TaskArrayAdapter(
                        TaskList.this, refreshed));
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
                            Toast.makeText(TaskList.this, "Zmieniono status!",
                                    Toast.LENGTH_SHORT).show();
                            image.setImageResource(R.drawable.ic_ok);
                        } else {
                            Toast.makeText(TaskList.this,
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
                                Toast.makeText(TaskList.this, "Zmieniono status!",
                                        Toast.LENGTH_SHORT).show();
                                image.setImageResource(R.drawable.ic_not);
                            } else {
                                Toast.makeText(TaskList.this,
                                        "Status nie zostal zmieniony!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (DbxException e) {
                            e.printStackTrace();
                        }
                        // kiedy data jest nieaktualna
                    } else {
                        Toast.makeText(TaskList.this,
                                "Zmien date na pozniejsza!", Toast.LENGTH_LONG)
                                .show();
                        showDatePickerDialog(arg0, dto, image);

                    }
                }
            }

        });

        dialog.show();

    }

    public void showDatePickerDialog(View v, TaskDTO dto, ImageView image) {
        DialogFragment newFragment = new DatePickerFragment(dto, image);
        newFragment.show(fragmentManager, "datePicker");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActiityResult", "wszedlem!!!!");

        if (resultCode == RESULT_OK) {

            try {
                adapter = new TaskArrayAdapter(TaskList.this,
                        taskService.getNotDoneTasks());
            } catch (DbxException e) {
                e.printStackTrace();
            }
            setListAdapter(adapter);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_actualTasks: {
                try {
                    adapter = new TaskArrayAdapter(TaskList.this,
                            taskService.getActualTasks());
                } catch (DbxException e) {
                    e.printStackTrace();
                }
                TaskList.this.setListAdapter(adapter);
                if (adapter.isEmpty())
                    Toast.makeText(TaskList.this,
                            "Lista nie aktualnych zadan jest pusta!",
                            Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.mn_doneSort: {
                try {
                    adapter = new TaskArrayAdapter(TaskList.this,
                            taskService.getDoneTasks());
                } catch (DbxException e) {
                    e.printStackTrace();
                }
                this.setListAdapter(adapter);
                if (adapter.isEmpty())
                    Toast.makeText(TaskList.this,
                            "Lista wykonanych zadan jest pusta!",
                            Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.mn_notActualTasks: {
                try {
                    adapter = new TaskArrayAdapter(TaskList.this,
                            taskService.getPastTasks());
                } catch (DbxException e) {
                    e.printStackTrace();
                }
                this.setListAdapter(adapter);
                if (adapter.isEmpty())
                    Toast.makeText(TaskList.this,
                            "Lista nieaktualnych zadan jest pusta!",
                            Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.mn_notDoneSort: {
                try {
                    adapter = new TaskArrayAdapter(TaskList.this,
                            taskService.getNotDoneTasks());
                } catch (DbxException e) {
                    e.printStackTrace();
                }
                this.setListAdapter(adapter);
                if (adapter.isEmpty())
                    Toast.makeText(TaskList.this,
                            "Lista nie wykonanych zadan jest pusta!",
                            Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.mn_showAllOnMap: {
                List<TaskDTO> dtoList = adapter.getCurrentTaskList();
                if (!dtoList.isEmpty()) {
                    String ids[] = new String[dtoList.size()];
                    for (int i = 0; i < dtoList.size(); i++) {
                        ids[i] = dtoList.get(i).getId();
                    }
                    Intent map = new Intent(TaskList.this, ShowListOnMap.class);
                    map.putExtra("IDS", ids);
                    TaskList.this.startActivity(map);
                } else {
                    Toast.makeText(TaskList.this,
                            "Nie ma zadnych zadan do pokazania!",
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            case R.id.mn_allTasks: {
                try {
                    adapter = new TaskArrayAdapter(TaskList.this,
                            taskService.getAllTasks());
                } catch (DbxException e) {
                    e.printStackTrace();
                }
                this.setListAdapter(adapter);
                if (adapter.isEmpty())
                    Toast.makeText(TaskList.this, "Lista zadan jest pusta!",
                            Toast.LENGTH_SHORT).show();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);

        }
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
            return new DatePickerDialog(TaskList.this, this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            updateDate = String.valueOf(dayOfMonth) + "-"
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
