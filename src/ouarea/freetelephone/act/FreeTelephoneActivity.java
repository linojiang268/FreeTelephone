package ouarea.freetelephone.act;

import ouarea.freetelephone.tool.BMapApiApp;
import ouarea.freetelephone.tool.PhoneTool;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;

public class FreeTelephoneActivity extends MapActivity {
	/** Called when the activity is first created. */
	private TextView mtv, mtv1;
	private Button mbtSearch;
	private LinearLayout mll;
	private EditText metAddress;

	BMapApiApp app = null;
	MapView mMapView = null;
	LocationListener mLocationListener = null;// onResume时注册此listener，onPause时需要Remove
	MyLocationOverlay mLocationOverlay = null; // 定位图层

	private Location nowLocation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mtv = (TextView) findViewById(R.id.tv);
		mtv1 = (TextView) findViewById(R.id.tv1);
		mtv.setOnClickListener(clickListener);
		mtv1.setOnClickListener(clickListener);
		mbtSearch = (Button) findViewById(R.id.btSearch);
		mll = (LinearLayout) findViewById(R.id.ll);
		metAddress = (EditText) findViewById(R.id.etAddress);
		mbtSearch.setOnClickListener(clickListener);
		mtv.setOnLongClickListener(longClickListener);
		mtv.setText(PhoneTool.getPhoneNumber(this));
		this.startMap();
		this.startMapView();
		this.startLocationListener();
		// initLocationTool();
		// mLocationTool.requestLocationUpdates(locationListener);
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btSearch:
				mll.setVisibility(View.GONE);
				break;
			case R.id.tv:
			case R.id.tv1:
				// 移动到定位点
				if (nowLocation != null) {
					GeoPoint pt = new GeoPoint((int) (nowLocation.getLatitude() * 1e6), (int) (nowLocation.getLongitude() * 1e6));
					mMapView.getController().animateTo(pt);
				}
				break;
			default:
				break;
			}
		}
	};

	private OnLongClickListener longClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			switch (v.getId()) {
			case R.id.tv:
				mll.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
			return false;
		}
	};

	private void startMap() {
		app = (BMapApiApp) this.getApplication();
		// if (app.mBMapMan == null) {
		// app.mBMapMan = new BMapManager(getApplication());
		// app.mBMapMan.init(app.mStrKey, new
		// BMapApiDemoApp.MyGeneralListener());
		// }
		app.mBMapMan.start();
	}

	private void startMapView() {
		// 如果使用地图SDK，请初始化地图Activity
		super.initMapActivity(app.mBMapMan);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.setBuiltInZoomControls(true);
		// 设置在缩放动画过程中也显示overlay,默认为不绘制
		mMapView.setDrawOverlayWhenZooming(true);

		// 添加定位图层
		mLocationOverlay = new MyLocationOverlay(this, mMapView);
		mMapView.getOverlays().add(mLocationOverlay);
	}

	private void startLocationListener() {
		// 注册定位事件
		mLocationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					nowLocation = location;
					String strLog = String.format("Baidu" + location.getProvider() + "您当前的位置:\r\n" + "纬度:%f\r\n" + "经度:%f", location.getLongitude(), location.getLatitude());
					mtv1.setText(strLog);
				}
			}
		};
	}

	@Override
	protected void onPause() {
		BMapApiApp app = (BMapApiApp) this.getApplication();
		// 移除listener
		app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		mLocationOverlay.disableMyLocation();
		mLocationOverlay.disableCompass(); // 关闭指南针
		app.mBMapMan.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		BMapApiApp app = (BMapApiApp) this.getApplication();
		// 注册Listener
		app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
		mLocationOverlay.enableMyLocation();
		mLocationOverlay.enableCompass(); // 打开指南针
		app.mBMapMan.start();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	// ====================================================
	//
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
	// // if (null != location) {
	// // mtv1.setText(location.getProvider() + "定位:" +
	// // location.getLongitude() + "," + location.getLatitude());
	// // }
	// if (location != null) {
	// nowLocation = location;
	// String strLog = String.format(location.getProvider() + "您当前的位置:\r\n" +
	// "纬度:%f\r\n" + "经度:%f", location.getLongitude(), location.getLatitude());
	// mtv1.setText(strLog);
	// }
	// }
	// };
	//
	// private final android.location.LocationListener locationListener = new
	// android.location.LocationListener() {
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
}