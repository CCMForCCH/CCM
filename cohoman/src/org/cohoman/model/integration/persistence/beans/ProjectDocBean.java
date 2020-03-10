package org.cohoman.model.integration.persistence.beans;

import java.io.Serializable;
import java.util.Date;

public class ProjectDocBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 321321565461880117L;

	
	private Long docId;
	private String docName;
	private String docDesc;
	private Date docDate;
	private String docOwner;
	private String docLink;
	
	
	public Long getDocId() {
		return docId;
	}
	public void setDocId(Long docId) {
		this.docId = docId;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getDocDesc() {
		return docDesc;
	}
	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
	}
	public Date getDocDate() {
		return docDate;
	}
	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}
	public String getDocOwner() {
		return docOwner;
	}
	public void setDocOwner(String docOwner) {
		this.docOwner = docOwner;
	}
	public String getDocLink() {
		return docLink;
	}
	public void setDocLink(String docLink) {
		this.docLink = docLink;
	}
	
}
