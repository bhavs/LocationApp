package com.example.locationapp;

import com.example.mobiledata.CDMACellInformation;
import com.example.mobiledata.GSMCellInformation;
import com.example.mobiledata.MobileData;
import com.example.mobiledata.PhoneType;
import com.firebase.client.Firebase;

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


public class MyService extends Service {

	private Firebase ref ;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		ref = new Firebase("https://ums.firebaseio.com/");
		getLocationInformation();
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}

	private void getLocationInformation() {
		long minTimeInterval = 0; // minimum time interval between
		// notifications
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider.
				MobileData d = new MobileData();
				getMobileInfo(d);
				d.setLatitude(location.getLatitude());
				d.setLongitude(location.getLongitude());
				d.setTime(location.getTime());
				System.out.println(d.toString());
				Firebase temp = getRef().push();
				temp.setValue(d);
				
			}

			// FIXME : this method need not be called so many times, the values
			// of the phone number IMSI, SIM country ISO and SIM operator NAME
			// will be pretty standard
			// FIXME : How to store GSM ad CDMA data without differentiating
			// between the way the data is being stored
			//FIXME hardcoded phone number 
			private void getMobileInfo(MobileData d) {
				TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				String IMEINum = manager.getDeviceId();
				int phoneNumber=1234567890;
				int type = manager.getPhoneType();
				System.out.println(" Phone type is "+type);
				if (type == TelephonyManager.PHONE_TYPE_GSM) {
					GsmCellLocation gsmCell = (GsmCellLocation) manager
							.getCellLocation();
					System.out.println(" GSM Cell Location  cell ID: "
							+ gsmCell.getCid() + " location access code "
							+ gsmCell.getLac());
					GSMCellInformation temp = new GSMCellInformation();
					temp.setType(PhoneType.GSM);
					temp.setCellID(gsmCell.getCid());
				    temp.setLac(gsmCell.getLac());
				    d.setCellInfo(temp);
				} else if (type == TelephonyManager.PHONE_TYPE_CDMA) {
					CdmaCellLocation cdmaLoc = (CdmaCellLocation) manager
							.getCellLocation();
					System.out.println("CDMA cell location "
							+ cdmaLoc.getBaseStationId() + " cdma lat "
							+ cdmaLoc.getBaseStationLatitude() + " cdma  long "
							+ cdmaLoc.getBaseStationLongitude() + " network ID"
							+ cdmaLoc.getNetworkId());
					CDMACellInformation temp = new CDMACellInformation();
					temp.setType(PhoneType.CDMA);
					temp.setBaseStationID(cdmaLoc.getBaseStationId());
					temp.setCdmaLatitude(cdmaLoc.getBaseStationLatitude());
					temp.setCdmaLongitude(cdmaLoc.getBaseStationLongitude());
					temp.setNetworkID(cdmaLoc.getNetworkId());
					d.setCellInfo(temp);
				}
				System.out.println(" SubscriberID : " + phoneNumber);
				System.out.println("Voice mail number "
						+ manager.getVoiceMailNumber() + " ");
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
				LocationManager.NETWORK_PROVIDER, minTimeInterval, 0,
				locationListener);

	}

	public Firebase getRef() {
		return ref;
	}

	public void setRef(Firebase ref) {
		this.ref = ref;
	}

}