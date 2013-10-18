package no.ums.mobileInfo;

public class GSMCellInformation extends CellInformation {
	int cellID;
	int lac;
	
	public int getCellID() {
		return cellID;
	}
	public void setCellID(int cellID) {
		this.cellID = cellID;
	}
	public int getLac() {
		return lac;
	}
	public void setLac(int lac) {
		this.lac = lac;
	}
	
}