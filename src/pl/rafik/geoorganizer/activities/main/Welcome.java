package pl.rafik.geoorganizer.activities.main;

import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.activities.preferences.RunPreferences;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Klasa aktywnosci poczatkowej dla aplikacji.
 * 
 * @author Rafal
 * 
 */
public class Welcome extends Activity {

	private Button btn;
	private Button btnTaskList;
	private Button help;
	private Button exit;
	private Button preferences;
	private Builder alert;
	private LocationManager lm;
	private int one = 0;
	public final static int NEWTASK = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btn = (Button) findViewById(R.id.btn_createNewTask);
		btnTaskList = (Button) findViewById(R.id.btn_showTasksList);
		help = (Button) findViewById(R.id.btn_main_help);
		exit = (Button) findViewById(R.id.btn_main_exit);
		preferences = (Button) findViewById(R.id.btn_main_preferences);
		lm = (LocationManager) Welcome.this
				.getSystemService(Context.LOCATION_SERVICE);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					alert = new AlertDialog.Builder(Welcome.this);
					alert.setTitle("Czy chcesz wlaczyc GPS?");

					alert.setNegativeButton("Nie",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent createTask = new Intent(
											Welcome.this, NewTask.class);
									startActivityForResult(createTask, 0);

								}
							});
					alert.setPositiveButton("Tak",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Welcome.this
											.startActivityForResult(
													new Intent(
															android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
													0);

								}
							});
					alert.show();
				} else {
					Intent createTask = new Intent(Welcome.this, NewTask.class);
					startActivityForResult(createTask, NEWTASK);
				}

			}
		});

		btnTaskList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent listIntent = new Intent(Welcome.this, TaskList.class);
				startActivityForResult(listIntent, -1);

			}
		});

		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}

		});

		help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent helpIntent = new Intent(Welcome.this, Help.class);
				startActivityForResult(helpIntent, -1);

			}

		});

		preferences.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent runPref = new Intent(Welcome.this, RunPreferences.class);
				startActivityForResult(runPref, -1);

			}

		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == NEWTASK) {
			Log.d("onActiityResult", "wszedlem!!!!");
			Intent listIntent = new Intent(Welcome.this, TaskList.class);
			startActivityForResult(listIntent, -1);
		} else {
			if (one == 0) {
				Intent createTask = new Intent(Welcome.this, NewTask.class);
				one = 1;
				startActivityForResult(createTask, 0);
			}

		}
	}
};
