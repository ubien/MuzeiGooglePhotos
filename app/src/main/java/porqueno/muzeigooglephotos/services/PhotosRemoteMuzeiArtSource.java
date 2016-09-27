package porqueno.muzeigooglephotos.services;

import android.content.Intent;
import android.net.Uri;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.android.gms.auth.GoogleAuthException;

import java.io.IOException;

import porqueno.muzeigooglephotos.activities.PhotosAuthActivity;
import porqueno.muzeigooglephotos.models.AppSharedPreferences;
import porqueno.muzeigooglephotos.models.PhotoModel;
import porqueno.muzeigooglephotos.models.PhotosModelDbHelper;
import porqueno.muzeigooglephotos.util.GoogleCredentialHelpers;
import porqueno.muzeigooglephotos.util.LocationHelpers;
import porqueno.muzeigooglephotos.util.TimeHelpers;

/**
 * Created by jacob on 6/16/16.
 */
public class PhotosRemoteMuzeiArtSource extends RemoteMuzeiArtSource {
	private static final String TAG = "PhotosRemoteMuzeiArtSource";
	private String token;

	public PhotosRemoteMuzeiArtSource() {
		super(TAG);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
	}

	@Override
	protected void onTryUpdate(int reason) throws RetryException {
		String accountName = AppSharedPreferences.getGoogleAccountName(getApplicationContext());

		if (accountName != null) {
			GoogleCredentialHelpers.get(getApplicationContext()).setSelectedAccountName(accountName);
		}

		if (!PhotosAuthActivity.isGooglePlayServicesAvailable(getApplicationContext()) ||
				GoogleCredentialHelpers.get(getApplicationContext()).getSelectedAccountName() == null ) {
			Intent i = new Intent(this, PhotosAuthActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		} else {
			fetchNewPhoto();
		}
	}

	private void fetchNewPhoto() {
		try {
			token = GoogleCredentialHelpers.get(getApplicationContext()).getToken();
		} catch (IOException | GoogleAuthException e) {
			e.printStackTrace();
		}

		// Fetch URL from google photos
		PhotosModelDbHelper pdb = PhotosModelDbHelper.getHelper(getApplicationContext());
		PhotoModel photo = pdb.getNextPhoto();

		publishArtwork(new Artwork.Builder()
				.title(
						TimeHelpers.getPrettyDateString(photo.getCreatedTime()) +
						" " +
						TimeHelpers.getPrettyTimeString(photo.getCreatedTime())
						)
				.byline(getByLine(photo))
				.imageUri(Uri.parse(photo.getUrl(token)))
				.token(photo.getId())
				.viewIntent(new Intent(Intent.ACTION_VIEW,
						Uri.parse(photo.getUrl(token))))
				.build());

		setNextRefresh();
	}

	private void setNextRefresh(){
		long refreshMs = AppSharedPreferences.getRefreshDurationMs(getApplicationContext());
		scheduleUpdate(System.currentTimeMillis() + refreshMs);
	}

	private String getByLine(PhotoModel photo){
		if (photo.getLatitude() != 0.0 && photo.getLongitude() != 0.0){
			return LocationHelpers.getLocationString(this, photo.getLatitude(), photo.getLongitude());
		} else{
			return "";
		}
	}
}
