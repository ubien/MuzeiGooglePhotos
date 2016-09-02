package porqueno.muzeigooglephotos;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import porqueno.muzeigooglephotos.databinding.SettingsActivityBinding;
import porqueno.muzeigooglephotos.models.AppSharedPreferences;
import porqueno.muzeigooglephotos.util.TimeHelpers;

/**
 * Created by jacob on 8/30/16.
 */
public class SettingsActivity extends Activity {
	private SettingsActivityBinding mBinding;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.settings_activity);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.hours_array, android.R.layout.simple_spinner_item);
		mBinding.setSpinnerAdapter(adapter);
		mBinding.setHandlers(this);
		long msRefresh = AppSharedPreferences.getRefreshDurationMs(getApplicationContext());
		setSpinnerValue(TimeHelpers.getHoursFromMs(msRefresh));
	}

	private void setSpinnerValue(int hours) {
		int hoursIndex = mBinding.getSpinnerAdapter().getPosition(
				Integer.toString(hours)
		);
		hoursIndex = hoursIndex == -1 ? 0 : hoursIndex;

		// Stupid hack to get this to work
		final int index = hoursIndex;
		mBinding.spinner.post(new Runnable() {
			public void run() {
				mBinding.spinner.setSelection(index);
			}
		});
	}

	public void onClickStartSync(View view) {
		Intent i = new Intent(this, GooglePhotosAuthActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}

	public void onClickUpdateSettings(View view){
		Integer hours = Integer.valueOf((String)mBinding.spinner.getSelectedItem());
		AppSharedPreferences.setRefreshDurationMs(getApplicationContext(), hours);
		finish();
	}
}
