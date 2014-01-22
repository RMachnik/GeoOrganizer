package pl.rafik.geoorganizer.activities.dbx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.activities.main.Welcome;

/**
 * User: rafik991@gmail.com
 * Date: 11/23/13
 */
public class DbxStart extends Activity {
    public static final int REQUEST_LINK_TO_DBX = 3;
    public static DbxAccountManager dbxAccountManager;
    public static DbxDatastore dbxDatastore;
    public static String APP_KEY;
    public static String APP_SECRET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dbx_link);
        initialiseDbx(getApplicationContext());

        Button linkButton = (Button) findViewById(R.id.link_button);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbxAccountManager.startLink(DbxStart.this, REQUEST_LINK_TO_DBX);
            }
        });
    }

    public DbxDatastore getOpenedDataStore() {
        if (dbxDatastore == null) {
            try {
                dbxDatastore = DbxDatastore.openDefault(DbxStart.dbxAccountManager.getLinkedAccount());
            } catch (DbxException e) {
                e.printStackTrace();
            }
        }
        if (!dbxDatastore.isOpen()) {
            try {
                dbxDatastore = DbxDatastore.openDefault(DbxStart.dbxAccountManager.getLinkedAccount());
            } catch (DbxException e) {
                e.printStackTrace();
            }
        }
        return dbxDatastore;
    }

    public void initialiseDbx(Context context) {
        APP_KEY = context.getString(R.string.DROPBOX_APP_KEY);
        APP_SECRET = context.getString(R.string.DROPBOX_SECRET_KEY);
        dbxAccountManager = DbxAccountManager.getInstance(context, APP_KEY, APP_SECRET);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LINK_TO_DBX) {
            if (resultCode == RESULT_OK) {
                Intent welcome = new Intent(DbxStart.this, Welcome.class);
                startActivity(welcome);
            } else {
                // ... Link failed or was cancelled by the user.
                Toast.makeText(this, "Link to Dropbox failed.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}


