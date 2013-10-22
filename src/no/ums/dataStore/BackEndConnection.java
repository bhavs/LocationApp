package no.ums.dataStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ums.mobileInfo.CDMACellInformation;
import no.ums.mobileInfo.GSMCellInformation;
import no.ums.mobileInfo.MobileInfo;
import no.ums.mobileInfo.PhoneType;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

public class BackEndConnection {

	private static final String URL = "https://ums.firebaseio.com/";
	Map<String, MobileInfo> map;
	Firebase fb;
	boolean flag = false;
	private List<String> cellIDInformation;

	public BackEndConnection() {
		fb = new Firebase(URL);
		map = new HashMap<String, MobileInfo>();
	}

	public void writeToFirebase(MobileInfo d) {
		// Log.d("UMS:BPH", "Writing to firebase value ");
		if (d.getPhoneNumber() != null) {
			if (!flag) {
				fb.child(d.getPhoneNumber()).setValue(d);
				addCellIDData(d);
				flag = true;
			} else {
				Firebase temp;
				temp = fb.child(d.getPhoneNumber() + "/imeiNum");
				temp.setValue(d.getImeiNum());
				temp = fb.child(d.getPhoneNumber() + "/latitude");
				temp.setValue(d.getLatitude());
				temp = fb.child(d.getPhoneNumber() + "/longitude");
				temp.setValue(d.getLongitude());
				temp = fb.child(d.getPhoneNumber() + "/phoneNumber");
				temp.setValue(d.getPhoneNumber());
				temp = fb.child(d.getPhoneNumber() + "/simCountryIso");
				temp.setValue(d.getSimCountryIso());
				temp = fb.child(d.getPhoneNumber() + "/simOperatorName");
				temp.setValue(d.getSimOperatorName());
				temp = fb.child(d.getPhoneNumber() + "/simSerialNum");
				temp.setValue(d.getSimSerialNum());
				temp = fb.child(d.getPhoneNumber() + "/time");
				temp.setValue(d.getTime());
				if (d.getCellInfo().getType() == PhoneType.GSM) {
					GSMCellInformation gsm = (GSMCellInformation) d
							.getCellInfo();
					temp = fb.child(d.getPhoneNumber() + "/cellInfo/cellID");
					temp.setValue(gsm.getCellID());
					temp = fb.child(d.getPhoneNumber() + "/cellInfo/lac");
					temp.setValue(gsm.getLac());
					temp = fb.child(d.getPhoneNumber() + "/cellInfo/type");
					temp.setValue(gsm.getType());
				} else if (d.getCellInfo().getType() == PhoneType.CDMA) {
					CDMACellInformation cdma = (CDMACellInformation) d
							.getCellInfo();
					temp = fb.child(d.getPhoneNumber()
							+ "/cellInfo/baseStationID");
					temp.setValue(cdma.getBaseStationID());
					temp = fb.child(d.getPhoneNumber()
							+ "/cellInfo/cdmaLatitude");
					temp.setValue(cdma.getCdmaLatitude());
					temp = fb.child(d.getPhoneNumber()
							+ "/cellInfo/cdmaLongitude");
					temp.setValue(cdma.getCdmaLongitude());
					temp = fb.child(d.getPhoneNumber() + "/cellInfo/networkID");
					temp.setValue(cdma.getNetworkID());
					temp = fb.child(d.getPhoneNumber() + "/cellInfo/type");
					temp.setValue(cdma.getType());

				}
			}
		}

	}

	private void addCellIDData(final MobileInfo d) {
		if (d.getCellInfo().getType() == PhoneType.GSM) {
			cellIDInformation = new ArrayList<String>();
			GSMCellInformation gsm = (GSMCellInformation) d.getCellInfo();
			final String oldCellID = Integer.toString(gsm.getCellID());
			final List<String> cellInfo = new ArrayList<String>();
			Log.d("BPH:UMS", "cell id retrieved from mobile data is "
					+ oldCellID);
			fb.child("CELLID").addChildEventListener(new ChildEventListener() {

				@Override
				public void onChildRemoved(DataSnapshot arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onChildMoved(DataSnapshot arg0, String arg1) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onChildChanged(DataSnapshot arg0, String arg1) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onChildAdded(DataSnapshot arg0, String arg1) {
					cellInfo.add(arg0.getName());
					Log.d("BPH:UMS",
							" Retrieved value from CellID " + arg0.getName());
					// cellIDInformation.add(arg0.getName());
					if (arg0.getName().equals(oldCellID)) {
						ArrayList<String> temp = (ArrayList<String>) arg0
								.getValue();
						Log.d("BPH:UMS",
								"Value fetched from datasnapshot is " + temp
										+ "  previous is " + arg1
										+ " mobile phone number is "
										+ d.getPhoneNumber());
						if (!temp.contains(d.getPhoneNumber())) {
							temp.add(d.getPhoneNumber());
						}
						// fb.child("CELLID").child(cellID).setValue(tempNumbers);
						Log.d("BPH:UMS", "value at cellID " + oldCellID
								+ " is value " + arg0.getValue() + " name  "
								+ arg0.getName());
						Firebase tempRef = arg0.getRef();
						Log.d("BPH:UMS", "FIREBASE reference is " + tempRef);
						for (String s : temp) {
							Log.d("BPH:UMS", "Phone numbers in array list " + s);
						}
						fb.child("CELLID").child(oldCellID).setValue(temp);
					}
				}

				@Override
				public void onCancelled() {
					// TODO Auto-generated method stub

				}
			});

			if (cellInfo.size()>0 && !cellInfo.contains(oldCellID)) {
				Log.d("BPH:UMS", "Is it coming here already ?");
				List<String> temp = new ArrayList<String>();
				temp.add(d.getPhoneNumber());
				fb.child("CELLID").child(oldCellID).setValue(temp);
			}

		}
	}
}
