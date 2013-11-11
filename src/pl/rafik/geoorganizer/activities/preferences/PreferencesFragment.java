package pl.rafik.geoorganizer.activities.preferences;

import pl.rafik.geoorganizer.R;
import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class PreferencesFragment extends PreferenceFragment {
	private Preference soundChose;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences);
		soundChose = (Preference) findPreference("chose_sound");
		soundChose.setDependency("sound_pref_checkbox");
		soundChose
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						Intent intent = new Intent(
								RingtoneManager.ACTION_RINGTONE_PICKER);
						intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
								"Select ringtone for notifications:");
						intent.putExtra(
								RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT,
								false);
						intent.putExtra(
								RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT,
								true);
						intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
								RingtoneManager.TYPE_NOTIFICATION);
						PreferencesFragment.this.startActivityForResult(intent,
								1);
						return true;
					}
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Uri uri = data
					.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			if (uri != null) {
				String ringTonePath = uri.toString();
				soundChose.setDefaultValue(ringTonePath);

			}
		}
	}

}
