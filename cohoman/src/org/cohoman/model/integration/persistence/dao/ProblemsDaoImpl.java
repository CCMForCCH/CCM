package org.cohoman.model.integration.persistence.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.dto.ProblemItemDTO;
import org.cohoman.model.integration.persistence.beans.ProblemUpdateBean;
import org.cohoman.model.integration.persistence.beans.ProblemsBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.ProblemStateEnums;
import org.cohoman.view.controller.utils.ProblemTypeEnums;
import org.cohoman.view.controller.utils.SortEnums;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ProblemsDaoImpl implements ProblemsDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void createProblemItem(ProblemItemDTO dto) throws CohomanException {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			ProblemsBean problemsBean = new ProblemsBean();
			problemsBean.setItemCreatedBy(dto.getItemCreatedBy());
			problemsBean.setItemCreatedDate(dto.getItemCreatedDate());
			problemsBean.setItemdescription(dto.getItemdescription());
			problemsBean.setItemname(dto.getItemname());
			problemsBean.setPriority(dto.getPriority());
			problemsBean.setProblemStatus(dto.getProblemStatus());
			problemsBean.setProblemType(dto.getProblemType());
			problemsBean.setVendor(dto.getVendor());
			problemsBean.setDuplicateproblemitemname(dto.getDuplicateproblemitemname());
			problemsBean.setLocation(dto.getLocation());
			problemsBean.setAssignedTo(dto.getAssignedTo());
			problemsBean.setCost(dto.getCost());
			problemsBean.setInvoiceLink(dto.getInvoiceLink());
			problemsBean.setInvoiceNumber(dto.getInvoiceNumber());
			problemsBean.setItemCompletedDate(dto.getItemCompletedDate());
			
			session.saveOrUpdate(problemsBean);

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

	public List<ProblemItemDTO> getProblemItems(
			ProblemStateEnums problemStateEnum){
		Session session = HibernateUtil.getSessionFactory().openSession();

		// Build query string depending on Status of the problem. 
		String queryString;
		
		if (problemStateEnum.name().equals(ProblemStateEnums.PROBLEMISACTIVE.name()))
			queryString = "FROM ProblemsBean WHERE problemstatus = 'NEW' OR problemstatus = 'INPROGRESS' ORDER BY priority";
		else if (problemStateEnum.name().equals(ProblemStateEnums.PROBLEMISINACTIVE.name()))
			queryString = "FROM ProblemsBean WHERE problemstatus = 'COMPLETED' OR problemstatus = 'CLOSED' ORDER BY priority";
		else
			queryString = "FROM ProblemsBean ORDER BY priority";
			
		
		// Do maintenance type check unless it is ALL. If it's ALL, there's
		// nothing to do by way of filtering so nothing added to query string
/*
		if (problemTypeEnum.name().equals(ProblemsTypeEnums.)) {
			// Hofeller
			queryString += "WHERE maintenanceType='HOFELLER'";
		} else if (maintenanceTypeEnum.name().equals(MaintenanceTypeEnums.OWNER.name())) {
			// Owner
			queryString += "WHERE maintenanceType='OWNER'";			
		}
	
		// Now add the ORDER BY clause to sort appropriately
		if (sortEnum.equals(SortEnums.ORDERBYNAME)) {
			queryString += " ORDER BY itemname";			
		} else {
			queryString += " ORDER BY nextServiceDate";
		}
*/		
		// Now do the actual query
		Query query = session.createQuery(queryString);
		List<ProblemsBean> problemsBeans = query.list();
		
		// Copy data from a bean into a DTO (vs. passing a bean up to
		// higher levels.
		List<ProblemItemDTO> dtoList = new ArrayList<ProblemItemDTO>();

		for (ProblemsBean onebean : problemsBeans) {
			
			ProblemItemDTO oneItem = new ProblemItemDTO();
			oneItem.setProblemitemid(onebean.getProblemitemid());
			oneItem.setItemCreatedBy(onebean.getItemCreatedBy());
			oneItem.setItemCreatedDate(onebean.getItemCreatedDate());
			oneItem.setItemdescription(onebean.getItemdescription());
			oneItem.setItemname(onebean.getItemname());
			oneItem.setPriority(onebean.getPriority());
			oneItem.setProblemStatus(onebean.getProblemStatus());
			oneItem.setProblemType(onebean.getProblemType());
			oneItem.setLocation(onebean.getLocation());
			oneItem.setAssignedTo(onebean.getAssignedTo());
			oneItem.setInvoiceNumber(onebean.getInvoiceNumber());
			oneItem.setInvoiceLink(onebean.getInvoiceLink());
			oneItem.setCost(onebean.getCost());
			oneItem.setVendor(onebean.getVendor());
			oneItem.setDuplicateproblemitemname(onebean.getDuplicateproblemitemname());
			oneItem.setItemCompletedDate(onebean.getItemCompletedDate());
			
			dtoList.add(oneItem);	
		}

		session.flush();
		session.close();
		return dtoList;
	}

	public ProblemItemDTO getProblemItem(Long eventId) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String queryString = "from ProblemsBean " + "where problemitemid = ?";
			Query query = session.createQuery(queryString).setLong(0, eventId);
			List<ProblemsBean> problemsBeans = query.list();
			if (problemsBeans.size() != 1) {
				throw new RuntimeException("Expected just one bean but got "
						+ problemsBeans.size());
			}
			
			ProblemsBean onebean = problemsBeans.iterator().next();
			ProblemItemDTO oneItem = new ProblemItemDTO();
			oneItem.setProblemitemid(onebean.getProblemitemid());
			oneItem.setItemCreatedBy(onebean.getItemCreatedBy());
			oneItem.setItemCreatedDate(onebean.getItemCreatedDate());
			oneItem.setItemdescription(onebean.getItemdescription());
			oneItem.setItemname(onebean.getItemname());
			oneItem.setPriority(onebean.getPriority());
			oneItem.setProblemStatus(onebean.getProblemStatus());
			oneItem.setProblemType(onebean.getProblemType());
			oneItem.setLocation(onebean.getLocation());
			oneItem.setAssignedTo(onebean.getAssignedTo());
			oneItem.setInvoiceNumber(onebean.getInvoiceNumber());
			oneItem.setInvoiceLink(onebean.getInvoiceLink());
			oneItem.setCost(onebean.getCost());
			oneItem.setVendor(onebean.getVendor());
			oneItem.setDuplicateproblemitemname(onebean.getDuplicateproblemitemname());
			oneItem.setItemCompletedDate(onebean.getItemCompletedDate());

			tx.commit();
			return oneItem;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.flush();
			session.close();
		}
	}

	public void updateProblemItem(ProblemItemDTO problemItemDTO) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		tx = session.beginTransaction();

		ProblemsBean problemsBean = (ProblemsBean) session.load(ProblemsBean.class,
				problemItemDTO.getProblemitemid());
		problemsBean.setItemCreatedBy(problemItemDTO.getItemCreatedBy());
		problemsBean.setItemCreatedDate(problemItemDTO.getItemCreatedDate());
		problemsBean.setItemdescription(problemItemDTO.getItemdescription());
		problemsBean.setItemname(problemItemDTO.getItemname());
		problemsBean.setProblemitemid(problemItemDTO.getProblemitemid());
		problemsBean.setPriority(problemItemDTO.getPriority());
		problemsBean.setProblemStatus(problemItemDTO.getProblemStatus());
		problemsBean.setProblemType(problemItemDTO.getProblemType());
		problemsBean.setLocation(problemItemDTO.getLocation());
		problemsBean.setAssignedTo(problemItemDTO.getAssignedTo());
		problemsBean.setInvoiceNumber(problemItemDTO.getInvoiceNumber());
		problemsBean.setInvoiceLink(problemItemDTO.getInvoiceLink());
		problemsBean.setCost(problemItemDTO.getCost());
		problemsBean.setVendor(problemItemDTO.getVendor());
		problemsBean.setDuplicateproblemitemname(problemItemDTO.getDuplicateproblemitemname());
		problemsBean.setItemCompletedDate(problemItemDTO.getItemCompletedDate());
		session.merge(problemsBean);

		tx.commit();
		session.flush();
		session.close();
	}

	public void deleteProblemItem(ProblemItemDTO problemItemDTO) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();

/*
			// Delete the tasks for this maintenance item first.
			String queryString = "from ProblemsBean where problemitemid = ?";
			Query query = session.createQuery(queryString)
					.setLong(0, problemItemDTO.getProblemitemid());
			List<ProblemUpdateBean> problemUpdateBeans = query.list();

			// Loop through all beans deleting all entries .
			for (ProblemUpdateBean oneBean : problemUpdateBeans) {
				ProblemUpdateBean problemUpdateBean = (ProblemUpdateBean) session.load(
						ProblemUpdateBean.class, oneBean.getProblemupdateid());
				session.delete(problemUpdateBean);
			}
*/
			
			// Now it should be safe to delete the parent problems item.
			ProblemsBean problemsBean = (ProblemsBean) session.load(
					ProblemsBean.class, problemItemDTO.getProblemitemid());
			session.delete(problemsBean);

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
