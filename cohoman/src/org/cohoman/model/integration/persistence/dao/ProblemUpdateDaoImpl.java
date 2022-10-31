package org.cohoman.model.integration.persistence.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.dto.MtaskDTO;
import org.cohoman.model.dto.ProblemUpdateDTO;
import org.cohoman.model.integration.persistence.beans.MtaskBean;
import org.cohoman.model.integration.persistence.beans.ProblemUpdateBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sun.enterprise.security.cli.UpdatePasswordAlias;

public class ProblemUpdateDaoImpl implements ProblemUpdateDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void createProblemUpdate(ProblemUpdateDTO dto) throws CohomanException {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			ProblemUpdateBean problemUpdateBean = new ProblemUpdateBean();
			problemUpdateBean.setProblemitemid(dto.getProblemitemid());
			problemUpdateBean.setUpdateDate(dto.getUpdateDate());
			problemUpdateBean.setItemCreatedBy(dto.getItemCreatedBy());
			problemUpdateBean.setNotes(dto.getNotes());

			session.saveOrUpdate(problemUpdateBean);

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

	
	public ProblemUpdateDTO getProblemUpdate(Long problemUpdateId) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String queryString = "from ProblemUpdateBean " + "where problemupdateid = ?";
			Query query = session.createQuery(queryString).setLong(0,
					problemUpdateId);
			List<ProblemUpdateBean> problemUpdateBeans = query.list();
			if (problemUpdateBeans.size() != 1) {
				throw new RuntimeException("Expected just one bean but got "
						+ problemUpdateBeans.size());
			}
			ProblemUpdateBean onebean = problemUpdateBeans.iterator().next();

			List<ProblemUpdateDTO> problemUpdateDTOList = new ArrayList<ProblemUpdateDTO>();

			ProblemUpdateDTO oneItem = new ProblemUpdateDTO();
			oneItem.setProblemupdateid(onebean.getProblemupdateid());
			oneItem.setProblemitemid(onebean.getProblemitemid());
			oneItem.setUpdateDate(onebean.getUpdateDate());
			oneItem.setItemCreatedBy(onebean.getItemCreatedBy());
			oneItem.setNotes(onebean.getNotes());

			return oneItem;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			return null;
		} finally {
			session.flush();
			session.close();
		}

	}

	public List<ProblemUpdateDTO> getProblemUpdatesForProblemItem(Long problemItemId) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String queryString = "from ProblemUpdateBean "
					+ "where problemitemid = ? order by updateDate desc";
			Query query = session.createQuery(queryString).setLong(0,
					problemItemId);
			List<ProblemUpdateBean> problemUpdateBeans = query.list();

			List<ProblemUpdateDTO> problemUpdateDTOList = new ArrayList<ProblemUpdateDTO>();
			for (ProblemUpdateBean onebean : problemUpdateBeans) {

				ProblemUpdateDTO oneItem = new ProblemUpdateDTO();
				oneItem.setProblemitemid(onebean.getProblemitemid());
				oneItem.setItemCreatedBy(onebean.getItemCreatedBy());
				oneItem.setProblemupdateid(onebean.getProblemupdateid());
				oneItem.setNotes(onebean.getNotes());
				oneItem.setUpdateDate(onebean.getUpdateDate());
				problemUpdateDTOList.add(oneItem);
			}
			return problemUpdateDTOList;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			return null;
		} finally {
			session.flush();
			session.close();
		}
	}

	public void updateProblemUpdate(ProblemUpdateDTO problemUpdateDTO) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		tx = session.beginTransaction();

		ProblemUpdateBean problemUpdateBean = (ProblemUpdateBean) session.load(ProblemUpdateBean.class,
				//problemUpdateDTO.getProblemitemid());
				problemUpdateDTO.getProblemupdateid());
		problemUpdateBean.setItemCreatedBy(problemUpdateDTO.getItemCreatedBy());
		problemUpdateBean.setNotes(problemUpdateDTO.getNotes());
		problemUpdateBean.setUpdateDate(problemUpdateDTO.getUpdateDate());
		session.merge(problemUpdateBean);

		tx.commit();
		session.flush();
		session.close();
	}

	public void deleteProblemUpdate(Long problemUpdateitemid) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			ProblemUpdateBean problemUpdateBean = (ProblemUpdateBean) session.load(ProblemUpdateBean.class,
					problemUpdateitemid);
			session.delete(problemUpdateBean);

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

}
