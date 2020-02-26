package org.cohoman.model.integration.persistence.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.integration.persistence.beans.MaintenanceBean;
import org.cohoman.model.integration.persistence.beans.MtaskBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.SortEnums;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MaintenanceDaoImpl implements MaintenanceDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void createMaintenanceItem(MaintenanceItemDTO dto) throws CohomanException {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			MaintenanceBean maintenanceBean = new MaintenanceBean();
			maintenanceBean.setFrequencyOfItem(dto.getFrequencyOfItem());
			maintenanceBean.setItemCreatedBy(dto.getItemCreatedBy());
			maintenanceBean.setItemCreatedDate(dto.getItemCreatedDate());
			maintenanceBean.setItemdescription(dto.getItemdescription());
			maintenanceBean.setItemname(dto.getItemname());
			maintenanceBean.setLastperformedDate(null);
			maintenanceBean.setPriority(dto.getPriority());
			maintenanceBean.setTargetTimeOfyear(dto.getTargetTimeOfyear());
			maintenanceBean.setTaskStatus(dto.getTaskStatus());
			maintenanceBean.setNextServiceDate(dto.getNextServiceDate());
			
			session.saveOrUpdate(maintenanceBean);

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

	public List<MaintenanceItemDTO> getMaintenanceItems(SortEnums sortEnum){
		Session session = HibernateUtil.getSessionFactory().openSession();

		String queryString = null;
		if (sortEnum.equals(SortEnums.ORDERBYNAME)) {
			queryString = "from MaintenanceBean order by itemname";			
		} else {
			queryString = "from MaintenanceBean order by nextServiceDate";
		}
		Query query = session.createQuery(queryString);

		List<MaintenanceBean> maintenanceBeans = query.list();
		
		// Copy data from a bean into a DTO (vs. passing a bean up to
		// higher levels.
		List<MaintenanceItemDTO> dtoList = new ArrayList<MaintenanceItemDTO>();

		for (MaintenanceBean onebean : maintenanceBeans) {
			MaintenanceItemDTO oneItem = new MaintenanceItemDTO();
			oneItem.setMaintenanceitemid(onebean.getMaintenanceitemid());
			oneItem.setFrequencyOfItem(onebean.getFrequencyOfItem());
			oneItem.setItemCreatedBy(onebean.getItemCreatedBy());
			oneItem.setItemCreatedDate(onebean.getItemCreatedDate());
			oneItem.setItemdescription(onebean.getItemdescription());
			oneItem.setItemname(onebean.getItemname());
			oneItem.setLastperformedDate(onebean.getLastperformedDate());
			oneItem.setPriority(onebean.getPriority());
			oneItem.setTargetTimeOfyear(onebean.getTargetTimeOfyear());
			oneItem.setTaskStatus(onebean.getTaskStatus());
			oneItem.setNextServiceDate(onebean.getNextServiceDate());
			
			dtoList.add(oneItem);	
		}

		session.flush();
		session.close();
		return dtoList;
	}

	public MaintenanceItemDTO getMaintenanceItem(Long eventId) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String queryString = "from MaintenanceBean " + "where maintenanceitemid = ?";
			Query query = session.createQuery(queryString).setLong(0, eventId);
			List<MaintenanceBean> maintenanceBeans = query.list();
			if (maintenanceBeans.size() != 1) {
				throw new RuntimeException("Expected just one bean but got "
						+ maintenanceBeans.size());
			}
			MaintenanceBean onebean = maintenanceBeans.iterator().next();
			MaintenanceItemDTO oneItem = new MaintenanceItemDTO();
			oneItem.setMaintenanceitemid(onebean.getMaintenanceitemid());
			oneItem.setFrequencyOfItem(onebean.getFrequencyOfItem());
			oneItem.setItemCreatedBy(onebean.getItemCreatedBy());
			oneItem.setItemCreatedDate(onebean.getItemCreatedDate());
			oneItem.setItemdescription(onebean.getItemdescription());
			oneItem.setItemname(onebean.getItemname());
			oneItem.setLastperformedDate(onebean.getLastperformedDate());
			oneItem.setPriority(onebean.getPriority());
			oneItem.setTargetTimeOfyear(onebean.getTargetTimeOfyear());
			oneItem.setTaskStatus(onebean.getTaskStatus());
			oneItem.setNextServiceDate(onebean.getNextServiceDate());
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

	public void updateMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		tx = session.beginTransaction();

		MaintenanceBean maintenanceBean = (MaintenanceBean) session.load(MaintenanceBean.class,
				maintenanceItemDTO.getMaintenanceitemid());
		maintenanceBean.setFrequencyOfItem(maintenanceItemDTO.getFrequencyOfItem());
		maintenanceBean.setItemCreatedBy(maintenanceItemDTO.getItemCreatedBy());
		maintenanceBean.setItemCreatedDate(maintenanceItemDTO.getItemCreatedDate());
		maintenanceBean.setItemdescription(maintenanceItemDTO.getItemdescription());
		maintenanceBean.setItemname(maintenanceItemDTO.getItemname());
		maintenanceBean.setLastperformedDate(maintenanceItemDTO.getLastperformedDate());
		maintenanceBean.setMaintenanceitemid(maintenanceItemDTO.getMaintenanceitemid());
		maintenanceBean.setPriority(maintenanceItemDTO.getPriority());
		maintenanceBean.setTargetTimeOfyear(maintenanceItemDTO.getTargetTimeOfyear());
		maintenanceBean.setTaskStatus(maintenanceItemDTO.getTaskStatus());
		maintenanceBean.setNextServiceDate(maintenanceItemDTO.getNextServiceDate());
		session.merge(maintenanceBean);

		tx.commit();
		session.flush();
		session.close();
	}

	public void deleteMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			// Delete the tasks for this maintenance item first.
			String queryString = "from MtaskBean where maintenanceitemid = ?";
			Query query = session.createQuery(queryString)
					.setLong(0, maintenanceItemDTO.getMaintenanceitemid());
			List<MtaskBean> mtaskBeans = query.list();

			// Loop through all beans deleting all entries .
			for (MtaskBean oneBean : mtaskBeans) {
				MtaskBean mtaskBean = (MtaskBean) session.load(
						MtaskBean.class, oneBean.getMtaskitemid());
				session.delete(mtaskBean);
			}

			// Now it should be safe to delete the parent maintenance item.
			MaintenanceBean maintenanceItemBean = (MaintenanceBean) session.load(
					MaintenanceBean.class, maintenanceItemDTO.getMaintenanceitemid());
			session.delete(maintenanceItemBean);

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
