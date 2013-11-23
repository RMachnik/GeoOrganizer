package pl.rafik.geoorganizer.dbx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dropbox.sync.android.DbxAccountManager;
import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.activities.main.Welcome;

/**
 * User: rafik991@gmail.com
 * Date: 11/23/13
 */
public class DbxStart extends Activity {
    public static final int REQUEST_LINK_TO_DBX = 3;
    private DbxAccountManager dbxAccountManager;
    private String APP_KEY;
    private String APP_SECRET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dbx_link);
        APP_KEY = getString(R.string.DROPBOX_APP_KEY);
        APP_SECRET = getString(R.string.DROPBOX_SECRET_KEY);
        dbxAccountManager = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);
        setContentView(R.layout.dbx_link);

        Button linkButton = (Button) findViewById(R.id.link_button);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbxAccountManager.startLink(DbxStart.this, REQUEST_LINK_TO_DBX);
            }
        });


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


