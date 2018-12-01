package org.cohoman.model.integration.persistence.dao;



import java.util.Date;

import org.cohoman.model.integration.persistence.beans.SubstitutesBean;
import org.cohoman.view.controller.CohomanException;

public interface SubstitutesDao {
	public void setSubstitute(Date startingDate, Long userid) throws CohomanException;
	public SubstitutesBean getSubstitute(String startingDate);
	public void deleteSubstitute(Long substitutesId);
	
}
