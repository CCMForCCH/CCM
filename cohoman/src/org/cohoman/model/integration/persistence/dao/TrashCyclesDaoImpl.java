package org.cohoman.model.integration.persistence.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.integration.persistence.beans.TrashCycleBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TrashCyclesDaoImpl implements TrashCyclesDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void createTrashCycle(TrashCycleBean trashCycleBean) throws CohomanException {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			session.saveOrUpdate(trashCycleBean);

			tx.commit();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
			throw new CohomanException(LoggingUtils.INTERNAL_ERROR);
		} finally {
			session.flush();
			session.close();
		}
	}


	public List<TrashCycleBean> getAllTrashCycles() {
		Session session = HibernateUtil.getSessionFactory().openSession();

		String queryString = "from TrashCycleBean";
		Query query = session.createQuery(queryString);

		List<TrashCycleBean> trashCycleBeans = query.list();

		session.flush();
		session.close();
		return trashCycleBeans;
	}


/*
	public void deleteMtask(Long mtaskitemid) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			MtaskBean mtaskBean = (MtaskBean) session.load(MtaskBean.class,
					mtaskitemid);
			session.delete(mtaskBean);

			tx.commit();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.flush();
			session.close();
		}

	}
*/
	
}
