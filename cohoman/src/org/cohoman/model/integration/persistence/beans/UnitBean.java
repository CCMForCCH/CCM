package org.cohoman.model.integration.persistence.beans;


public class UnitBean implements Comparable<UnitBean> {

	private Long unitid = null;
	private String section;
	private String unitnumber;
	
	public UnitBean() {
	}

	public Long getUnitid() {
		return unitid;
	}

	public void setUnitid(Long unitid) {
		this.unitid = unitid;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getUnitnumber() {
		return unitnumber;
	}

	public void setUnitnumber(String unitnumber) {
		this.unitnumber = unitnumber;
	}
	
	public int compareTo(UnitBean unitBean) {
		return unitnumber.compareTo(unitBean.getUnitnumber());
	}

}
