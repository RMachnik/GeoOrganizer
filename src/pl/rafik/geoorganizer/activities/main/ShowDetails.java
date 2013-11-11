package pl.rafik.geoorganizer.activities.main;

import java.util.Calendar;

import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.activities.map.ShowListOnMap;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.services.ITaskService;
import pl.rafik.geoorganizer.services.impl.TaskService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowDetails extends Activity {
	private ITaskService taskService;
	private FragmentManager fragmentManager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_dialog);
		taskService = new TaskService(this);
		fragmentManager = this.getFragmentManager();
		final TaskDTO dto = taskService.getTask(this.getIntent().getExtras()
				.getLong("id"));
		TextView tvDialog = (TextView) findViewById(R.id.dialog_tv);
		setTitle(dto.getNote());
		tvDialog.setText("wazny do:" + dto.getDate()
				+ System.getProperty("line.separator") + "priorytet: "
				+ dto.getPriority() + " adres:"
				+ dto.getLocalisation().getLocalistationAddress());
		final ImageView image = (ImageView) findViewById(R.id.dialog_image);
		if (dto.getStatus().equals("NOT")) {
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
				long[] ids = { dto.getId() };
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
					if (taskService.makeDone(dto.getId()) > 0) {
						Toast.makeText(ShowDetails.this, "Zmieniono status!",
								Toast.LENGTH_SHORT).show();
						image.setImageResource(R.drawable.ic_ok);
					} else {
						Toast.makeText(ShowDetails.this,
								"Status nie zostal zmieniony!",
								Toast.LENGTH_SHORT).show();
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
			taskService.updateTask(dto);
			taskService.makeNotDone(dto.getId());
			image.setImageResource(R.drawable.ic_not);

		}

	};
}
