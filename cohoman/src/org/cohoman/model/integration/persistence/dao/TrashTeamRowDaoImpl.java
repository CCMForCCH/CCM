package org.cohoman.model.integration.persistence.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.integration.persistence.beans.TrashTeamRowBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TrashTeamRowDaoImpl implements TrashTeamRowDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void createTrashTeamRow(TrashTeamRowBean trashTeamRowBean) throws CohomanException {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			session.saveOrUpdate(trashTeamRowBean);

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


	public List<TrashTeamRowBean> getAllTrashRows() {
		Session session = HibernateUtil.getSessionFactory().openSession();

		String queryString = "from TrashTeamRowBean";
		Query query = session.createQuery(queryString);

		List<TrashTeamRowBean> trashTeamRowBeans = query.list();

		session.flush();
		session.close();
		return trashTeamRowBeans;
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
