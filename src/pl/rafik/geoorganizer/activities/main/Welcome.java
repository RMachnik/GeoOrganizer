package pl.rafik.geoorganizer.activities.main;

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
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.activities.preferences.RunPreferences;
import pl.rafik.geoorganizer.dbx.DbxStart;

/**
 * Klasa aktywnosci poczatkowej dla aplikacji.
 *
 * @author Rafal
 */
public class Welcome extends Activity {

    public final static int NEWTASK = 2;
    private Button btn;
    private Button btnTaskList;
    private Button help;
    private Button exit;
    private Button preferences;
    private Builder alert;
    private LocationManager lm;
    private int one = 0;
    private DbxAccountManager dbxAccountManager;

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
        initialiseDbx();
        checkDbxLinkedAccount();

    }

    private void initialiseDbx() {
        DbxStart.APP_KEY = getString(R.string.DROPBOX_APP_KEY);
        DbxStart.APP_SECRET = getString(R.string.DROPBOX_SECRET_KEY);
        DbxStart.dbxAccountManager = DbxAccountManager.getInstance(getApplicationContext(), DbxStart.APP_KEY, DbxStart.APP_SECRET);
        try {
            DbxStart.dbxDatastore = DbxDatastore.openDefault(DbxStart.dbxAccountManager.getLinkedAccount());
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }

    private void checkDbxLinkedAccount() {
        if (DbxStart.dbxAccountManager.hasLinkedAccount()) {
            // already have an account linked

        } else {
            // Hide the add-task UI and show the link button
            Intent listIntent = new Intent(Welcome.this, DbxStart.class);
            startActivityForResult(listIntent, 3);
        }
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
