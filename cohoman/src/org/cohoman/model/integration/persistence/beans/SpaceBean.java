package org.cohoman.model.integration.persistence.beans;


public class SpaceBean implements Comparable<SpaceBean> {

	private Long spaceId = null;
	private String spaceName;
	
	public SpaceBean() {
	}

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public int compareTo(SpaceBean spaceBean) {
		return spaceName.compareTo(spaceBean.getSpaceName());
	}
	
}
