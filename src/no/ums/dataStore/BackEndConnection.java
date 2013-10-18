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
		Log.d("UMS:BPH", "Writing to firebase value ");
		if (d.getPhoneNumber() != null) {
			if (!flag) {
				fb.child(d.getPhoneNumber()).setValue(d);
				addCellIDData(d);
				flag=true;
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

	private void addCellIDData(MobileInfo d) {
		if(d.getCellInfo().getType()==PhoneType.GSM){
			cellIDInformation = new ArrayList<String>();
			GSMCellInformation gsm = (GSMCellInformation) d.getCellInfo();
			String cellID = Integer.toString(gsm.getCellID());
			Log.d("BPH:UMS", "cell id retrieved from mobile data is "+cellID);
			fb.child("CELLID").addChildEventListener( new ChildEventListener() {
				
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
					Log.d("BPH:UMS", " Retrieved value from CellID "+arg0.getName());
					cellIDInformation.add(arg0.getName());
				}
				
				@Override
				public void onCancelled() {
					// TODO Auto-generated method stub
					
				}
			});
			if(cellIDInformation.contains(cellID)){
			} else {
				fb.child("CELLID").child(cellID).setValue(d.getPhoneNumber());
			}

		}
	}

}
