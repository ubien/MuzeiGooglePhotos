package porqueno.muzeigooglephotos.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by jacob on 9/21/16.
 */

public class LocationHelpers {

	public static String getLocationString(Context ctx, double lat, double lng){
		Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
		try{
			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
			if (addresses.size() > 0){
				Address address = addresses.get(0);
				return address.getAdminArea() + ',' + address.getCountryName();
			}
		} catch (IllegalArgumentException | IOException exception) {
			Log.e(TAG, exception.toString());
		}
		return "";
	}
}
