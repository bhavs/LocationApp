package no.ums.dataStore;

import java.util.HashMap;
import java.util.Map;

import no.ums.mobileInfo.CDMACellInformation;
import no.ums.mobileInfo.GSMCellInformation;
import no.ums.mobileInfo.MobileInfo;
import no.ums.mobileInfo.PhoneType;

import android.util.Log;
import com.firebase.client.Firebase;

public class BackEndConnection {

	private static final String URL = "https://ums.firebaseio.com/";
	private String pushRefName;
	private Firebase pushRef;
	Map<String, MobileInfo> map;
	Firebase fb;
	int value = 0;

	public BackEndConnection() {
		fb = new Firebase(URL);
		pushRef = fb.push();
		pushRefName = pushRef.getName();
		
		Log.d("UMS:BPH", "Firebase push refernce information is " + pushRefName);
		map = new HashMap<String, MobileInfo>();
	}

	public void writeToFirebase(MobileInfo d) {
		Log.d("UMS:BPH", "Writing to firebase value ");
		if (value == 0) {
			pushRef.setValue(d);
			value++;
		} else {
			Firebase temp;
			temp = fb.child(pushRefName + "/imeiNum");
			temp.setValue(d.getImeiNum());
			temp = fb.child(pushRefName + "/latitude");
			temp.setValue(d.getLatitude());
			temp = fb.child(pushRefName + "/longitude");
			temp.setValue(d.getLongitude());
			temp = fb.child(pushRefName + "/phoneNumber");
			temp.setValue(d.getPhoneNumber());
			temp = fb.child(pushRefName + "/simCountryIso");
			temp.setValue(d.getSimCountryIso());
			temp = fb.child(pushRefName + "/simOperatorName");
			temp.setValue(d.getSimOperatorName());
			temp = fb.child(pushRefName + "/simSerialNum");
			temp.setValue(d.getSimSerialNum());
			temp = fb.child(pushRefName + "/time");
			temp.setValue(d.getTime());
			if(d.getCellInfo().getType()== PhoneType.GSM){
				GSMCellInformation gsm = (GSMCellInformation) d.getCellInfo();
				temp = fb.child(pushRefName + "/cellInfo/cellID");
				temp.setValue(gsm.getCellID());
				temp = fb.child(pushRefName + "/cellInfo/lac");
				temp.setValue(gsm.getLac());
				temp = fb.child(pushRefName+"/cellInfo/type");
				temp.setValue(gsm.getType());
			} else if(d.getCellInfo().getType()==PhoneType.CDMA){
				CDMACellInformation cdma = (CDMACellInformation) d.getCellInfo();
				temp=fb.child(pushRefName + "/cellInfo/baseStationID");
				temp.setValue(cdma.getBaseStationID());
				temp = fb.child(pushRefName + "/cellInfo/cdmaLatitude");
				temp.setValue(cdma.getCdmaLatitude());
				temp = fb.child(pushRefName + "/cellInfo/cdmaLongitude");
				temp.setValue(cdma.getCdmaLongitude());
				temp = fb.child(pushRefName + "/cellInfo/networkID");
				temp.setValue(cdma.getNetworkID());
				temp = fb.child(pushRefName+"/cellInfo/type");
				temp.setValue(cdma.getType());
				
			}
		}
	}

}
