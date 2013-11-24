package pl.rafik.geoorganizer.activities.main;

import pl.rafik.geoorganizer.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Pomoc aplikacji.
 * @author rafal.machnik
 *
 */
public class Help extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
        initialiseContent();
	}

    private void initialiseContent() {
        TextView email = (TextView) findViewById(R.id.tv_email);
        email.setTextColor(Color.BLUE);
        email.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL,
                        new String[] { "rafik991@gmail.com" });
                i.putExtra(Intent.EXTRA_SUBJECT,
                        "Aplikacja Geoorganizer Uwagi: ");
                i.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Help.this,
                            "There are no email clients installed.",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
