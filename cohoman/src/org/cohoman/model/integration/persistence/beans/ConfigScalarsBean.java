package org.cohoman.model.integration.persistence.beans;


public class ConfigScalarsBean {

	private Long configscalarid = null;
	private String scalarName;
	private String scalarValue;
	
	public ConfigScalarsBean() {
	}

	public Long getConfigscalarid() {
		return configscalarid;
	}

	public void setConfigscalarid(Long configscalarid) {
		this.configscalarid = configscalarid;
	}

	public String getScalarName() {
		return scalarName;
	}

	public void setScalarName(String scalarName) {
		this.scalarName = scalarName;
	}

	public String getScalarValue() {
		return scalarValue;
	}

	public void setScalarValue(String scalarValue) {
		this.scalarValue = scalarValue;
	}
	
}
