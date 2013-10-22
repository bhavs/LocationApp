package no.ums.locationApp;

import no.ums.dataStore.BackEndConnection;
import no.ums.mobileInfo.CDMACellInformation;
import no.ums.mobileInfo.GSMCellInformation;
import no.ums.mobileInfo.MobileInfo;
import no.ums.mobileInfo.PhoneType;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class LocationService extends Service {
	private BackEndConnection fb;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("UMS:BPH"," calling onCreate() command");
		fb = new BackEndConnection();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("UMS:BPH", "starting service");
		getLocationInformation();
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private void getLocationInformation() {
		long minDistance = 5; // minimum time interval between
		// notifications
		long minTime = 1000;
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider.
				MobileInfo mobileInfo = new MobileInfo();
				getMobileInfo(mobileInfo);
				mobileInfo.setLatitude(location.getLatitude());
				mobileInfo.setLongitude(location.getLongitude());
				mobileInfo.setTime(location.getTime());
				fb.writeToFirebase(mobileInfo);
			}

			private void getMobileInfo(MobileInfo d) {
				TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				String IMEINum = manager.getDeviceId();
				int type = manager.getPhoneType();
				if (type == TelephonyManager.PHONE_TYPE_GSM) {
					GsmCellLocation gsmCell = (GsmCellLocation) manager
							.getCellLocation();
					GSMCellInformation temp = new GSMCellInformation();
					temp.setType(PhoneType.GSM);
					temp.setCellID(gsmCell.getCid());
					temp.setLac(gsmCell.getLac());
					d.setCellInfo(temp);
				} else if (type == TelephonyManager.PHONE_TYPE_CDMA) {
					CdmaCellLocation cdmaLoc = (CdmaCellLocation) manager
							.getCellLocation();
					CDMACellInformation temp = new CDMACellInformation();
					temp.setType(PhoneType.CDMA);
					temp.setBaseStationID(cdmaLoc.getBaseStationId());
					temp.setCdmaLatitude(cdmaLoc.getBaseStationLatitude());
					temp.setCdmaLongitude(cdmaLoc.getBaseStationLongitude());
					temp.setNetworkID(cdmaLoc.getNetworkId());
					d.setCellInfo(temp);
				}
				d.setPhoneNumber(MainActivity.getPhoneNumber());
				d.setImeiNum(IMEINum);
				d.setSimSerialNum(manager.getSimSerialNumber());
				d.setSimCountryIso(manager.getSimCountryIso());
				d.setSimOperatorName(manager.getSimOperatorName());
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, minTime, minDistance,
				locationListener);

	}

}