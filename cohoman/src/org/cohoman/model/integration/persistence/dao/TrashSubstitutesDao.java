package org.cohoman.model.integration.persistence.dao;



import java.util.Date;
import java.util.List;

import org.cohoman.model.integration.persistence.beans.TrashSubstitutesBean;
import org.cohoman.view.controller.CohomanException;

public interface TrashSubstitutesDao {
	public void setTrashSubstitute(Date startingDate, String origUsername, String substituteUsername) throws CohomanException;
	public List<TrashSubstitutesBean> getTrashSubstitutes();
	public void deleteTrashSubstitute(Long substitutesId);
	public TrashSubstitutesBean getTrashSubstitute(String startingDate, String origUsername);
	
}
