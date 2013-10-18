package no.ums.mobileInfo;

public class CDMACellInformation extends CellInformation {
    int baseStationID;
    int cdmaLatitude;
    int cdmaLongitude;
    int networkID;
	public int getBaseStationID() {
		return baseStationID;
	}
	public void setBaseStationID(int baseStationID) {
		this.baseStationID = baseStationID;
	}
	public int getCdmaLatitude() {
		return cdmaLatitude;
	}
	public void setCdmaLatitude(int cdmaLatitude) {
		this.cdmaLatitude = cdmaLatitude;
	}
	public int getCdmaLongitude() {
		return cdmaLongitude;
	}
	public void setCdmaLongitude(int cdmaLongitude) {
		this.cdmaLongitude = cdmaLongitude;
	}
	public int getNetworkID() {
		return networkID;
	}
	public void setNetworkID(int networkID) {
		this.networkID = networkID;
	}
    
}