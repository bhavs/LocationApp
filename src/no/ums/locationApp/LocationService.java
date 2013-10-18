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
	BackEndConnection fb;
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		fb = new BackEndConnection();
		Log.d("UMS:BPH", "starting service");
		getLocationInformation();
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private void getLocationInformation() {
		long minDistance = 0; // minimum time interval between
		// notifications
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
				Log.d("UMS:BPH", "getLocationInformation " + mobileInfo.toString());
				fb.writeToFirebase(mobileInfo);
			}

			// FIXME : this method need not be called so many times, the values
			// of the phone number IMSI, SIM country ISO and SIM operator NAME
			// will be pretty standard
			// FIXME : How to store GSM ad CDMA data without differentiating
			// between the way the data is being stored
			// FIXME hardcoded phone number
			private void getMobileInfo(MobileInfo d) {
				TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				String IMEINum = manager.getDeviceId();
				String phoneNumber = MainActivity.phoneNumber;
				int type = manager.getPhoneType();
				System.out.println(" Phone type is " + type);
				if (type == TelephonyManager.PHONE_TYPE_GSM) {
					GsmCellLocation gsmCell = (GsmCellLocation) manager
							.getCellLocation();
					Log.d("UMS:BPH",
							" GSM Cell Location  cell ID: " + gsmCell.getCid()
									+ " location access code "
									+ gsmCell.getLac());
					GSMCellInformation temp = new GSMCellInformation();
					temp.setType(PhoneType.GSM);
					temp.setCellID(gsmCell.getCid());
					temp.setLac(gsmCell.getLac());
					d.setCellInfo(temp);
				} else if (type == TelephonyManager.PHONE_TYPE_CDMA) {
					CdmaCellLocation cdmaLoc = (CdmaCellLocation) manager
							.getCellLocation();
					Log.d("UMS:BPH",
							"CDMA cell location " + cdmaLoc.getBaseStationId()
									+ " cdma lat "
									+ cdmaLoc.getBaseStationLatitude()
									+ " cdma  long "
									+ cdmaLoc.getBaseStationLongitude()
									+ " network ID" + cdmaLoc.getNetworkId());
					CDMACellInformation temp = new CDMACellInformation();
					temp.setType(PhoneType.CDMA);
					temp.setBaseStationID(cdmaLoc.getBaseStationId());
					temp.setCdmaLatitude(cdmaLoc.getBaseStationLatitude());
					temp.setCdmaLongitude(cdmaLoc.getBaseStationLongitude());
					temp.setNetworkID(cdmaLoc.getNetworkId());
					d.setCellInfo(temp);
				}
				Log.d("UMS:BPH", " SubscriberID : " + phoneNumber);
				Log.d("UMS:BPH",
						"Voice mail number " + manager.getVoiceMailNumber()
								+ " ");
				d.setPhoneNumber(phoneNumber);
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
				LocationManager.NETWORK_PROVIDER, 0, minDistance,
				locationListener);

	}

}