package ouarea.freetelephone.tool;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * 位置
 * 
 * @author ouArea
 * 
 */
public class LocationTool {
	private Context mContext;
	private LocationManager mLocationManager;
	private String mProvider;
	private Criteria mCriteria;
	private Location mLocation;
	private LocationListener mLocationListener;
	private long minTime = 5000;
	private float minDistance = 5;
	private Update mUpdate;

	public LocationTool(Context context, Update update) {
		super();
		this.mContext = context;
		this.mUpdate = update;
		String context_location = Context.LOCATION_SERVICE;
		mLocationManager = (LocationManager) mContext.getSystemService(context_location);
	}

	// public void testProviders() {
	// List<String> providers = mLocationManager.getProviders(true);
	// for (String provider : providers) {
	// mLocationManager.getLastKnownLocation(provider);
	// }
	// }

	/**
	 * 刷新位置服务
	 */
	public void refreshLocation() {
		mProvider = getBestProvider();
		mLocation = mLocationManager.getLastKnownLocation(mProvider);
		if (null != mUpdate) {
			mUpdate.updateWithNewLocation(mLocation);
		}
	}

	private void request() {
		mLocationManager.requestLocationUpdates(mProvider, minTime, minDistance, mLocationListener);
	}

	public void remove() {
		if (null != mLocationListener) {
			mLocationManager.removeUpdates(mLocationListener);
		}
	}

	/**
	 * 设置定位监听
	 * 
	 * @param locationListener
	 */
	public void requestLocationUpdates(LocationListener locationListener) {
		remove();
		this.mLocationListener = locationListener;
		mProvider = getBestProvider();
		request();
	}

	/**
	 * 设置定位监听
	 * 
	 * @param locationListener
	 */
	public void requestLocationUpdates(LocationListener locationListener, String provider) {
		remove();
		this.mLocationListener = locationListener;
		mProvider = provider;
		request();
	}

	/**
	 * 设置定位监听
	 * 
	 * @param locationListener
	 */
	public void requestLocationUpdates(LocationListener locationListener, String provider, long minTime, float minDistance) {
		remove();
		this.mLocationListener = locationListener;
		mProvider = provider;
		this.minTime = minTime;
		this.minDistance = minDistance;
		request();
	}

	/**
	 * 最佳定位工具
	 * 
	 * @return
	 */
	private String getBestProvider() {
		if (null == mCriteria) {
			mCriteria = new Criteria();
			mCriteria.setAccuracy(Criteria.ACCURACY_FINE);
			mCriteria.setAltitudeRequired(false);
			mCriteria.setBearingRequired(false);
			mCriteria.setCostAllowed(true);
			mCriteria.setPowerRequirement(Criteria.POWER_LOW);
		}
		return mLocationManager.getBestProvider(mCriteria, true);
	}

	/**
	 * 回调更新接口
	 * 
	 * @author ouArea
	 * 
	 */
	public interface Update {
		void updateWithNewLocation(Location location);
	}
}
// 本地设备定位监听参考
// initLocationTool();
// mLocationTool.requestLocationUpdates(locationListener);

// private LocationTool mLocationTool;
//
// private void initLocationTool() {
// mLocationTool = new LocationTool(this, update);
// mLocationTool.refreshLocation();
// }
//
// private LocationTool.Update update = new Update() {
//
// @Override
// public void updateWithNewLocation(Location location) {
// if (null != location) {
// mtv1.setText(location.getProvider() + "定位:" + location.getLongitude() +
// location.getLatitude());
// }
// }
// };
//
// private final LocationListener locationListener = new LocationListener() {
//
// @Override
// public void onStatusChanged(String provider, int status, Bundle extras) {
//
// }
//
// @Override
// public void onProviderEnabled(String provider) {
//
// }
//
// @Override
// public void onProviderDisabled(String provider) {
//
// }
//
// @Override
// public void onLocationChanged(Location location) {
// update.updateWithNewLocation(location);
// }
// };
//
// @Override
// protected void onDestroy() {
// if (null != mLocationTool) {
// mLocationTool.remove();
// }
// super.onDestroy();
// }
