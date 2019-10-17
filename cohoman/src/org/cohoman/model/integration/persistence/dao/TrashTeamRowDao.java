package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.integration.persistence.beans.TrashTeamRowBean;
import org.cohoman.view.controller.CohomanException;

public interface TrashTeamRowDao {
	public void createTrashTeamRow(TrashTeamRowBean trashTeamRowBean) throws CohomanException;

	public List<TrashTeamRowBean> getAllTrashRows();

	//public void deleteMtask(Long mtaskitemid);

}
