package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.integration.persistence.beans.TrashCycleBean;
import org.cohoman.view.controller.CohomanException;

public interface TrashCyclesDao {
	public void createTrashCycle(TrashCycleBean trashCycleBean) throws CohomanException;

	public List<TrashCycleBean> getAllTrashCycles();

	public void deleteCycle(Long trashCycleId);

}
