package porqueno.muzeigooglephotos.models;

import com.google.api.client.util.DateTime;

/**
 * Created by jacob on 7/10/16.
 */
@SuppressWarnings({"SameParameterValue", "CanBeFinal"})
public class PhotoModel {
	private static final String GOOGLE_PHOTO_URL = "https://www.googleapis.com/drive/v3/files/";
	private String id;
	private boolean viewed;
	private long createdTime;
	private double latitude;
	private double longitude;

	public PhotoModel(String id, DateTime createdTime, double lat, double lng, boolean viewed) {
		this.id = id;
		this.createdTime = createdTime.getValue();
		this.viewed = viewed;
		this.latitude = lat;
		this.longitude = lng;
	}

	public PhotoModel(String id, long createdTime, double lat, double lng, boolean viewed) {
		this.id = id;
		this.createdTime = createdTime;
		this.latitude = lat;
		this.longitude = lng;
		this.viewed = viewed;
	}

	public String getId() { return id; }
	public boolean getViewed() { return viewed; }
	public String getUrl(String token) {
		return GOOGLE_PHOTO_URL + this.id + "?alt=media&access_token=" + token;
	}
	public DateTime getCreatedTime() {
		return new DateTime(this.createdTime);
	}

	public double getLatitude() { return latitude; }
	public double getLongitude() { return longitude; }
}
