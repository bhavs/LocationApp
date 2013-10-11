package com.example.mobiledata;



public class MobileData {
	double latitude;
	double longitude;
	long time;
	String imeiNum;
	String simSerialNum;
	String simOperatorName;
	String simCountryIso;
	CellInformation cellInfo;
	int phoneNumber;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getImeiNum() {
		return imeiNum;
	}

	public void setImeiNum(String imeiNum) {
		this.imeiNum = imeiNum;
	}

	public String getSimSerialNum() {
		return simSerialNum;
	}

	public void setSimSerialNum(String simSerialNum) {
		this.simSerialNum = simSerialNum;
	}

	public String getSimOperatorName() {
		return simOperatorName;
	}

	public void setSimOperatorName(String simOperatorName) {
		this.simOperatorName = simOperatorName;
	}

	public String getSimCountryIso() {
		return simCountryIso;
	}

	public void setSimCountryIso(String simCountryIso) {
		this.simCountryIso = simCountryIso;
	}
	
	public CellInformation getCellInfo() {
		return cellInfo;
	}

	public void setCellInfo(CellInformation cellInfo) {
		this.cellInfo = cellInfo;
	}

	
	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("Latitude :" + this.getLatitude());
		str.append("Longitude: " + this.getLongitude() + " IMEINumber "
				+ getImeiNum() + " Time:" + getTime() + " SimSerialNum: "
				+ getSimSerialNum() + " SimOperatorName: "
				+ getSimOperatorName() + " CountryISO: " + getSimCountryIso());
		return str.toString();

	}

}
