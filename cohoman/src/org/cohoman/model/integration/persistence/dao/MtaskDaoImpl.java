package org.cohoman.model.integration.persistence.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.dto.MtaskDTO;
import org.cohoman.model.integration.persistence.beans.CohoEvent;
import org.cohoman.model.integration.persistence.beans.CohoEventBean;
import org.cohoman.model.integration.persistence.beans.EventSpace;
import org.cohoman.model.integration.persistence.beans.MaintenanceBean;
import org.cohoman.model.integration.persistence.beans.MealEvent;
import org.cohoman.model.integration.persistence.beans.MealEventBean;
import org.cohoman.model.integration.persistence.beans.MtaskBean;
import org.cohoman.model.integration.persistence.beans.SpaceBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MtaskDaoImpl implements MtaskDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void createMtask(MtaskDTO dto) throws CohomanException {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			MtaskBean mtaskBean = new MtaskBean();
			mtaskBean.setItemCreatedBy(dto.getItemCreatedBy());
			mtaskBean.setMaintenanceitemid(dto.getMaintenanceitemid());
			mtaskBean.setNotes(dto.getNotes());
			mtaskBean.setTaskstartDate(dto.getTaskstartDate());
			mtaskBean.setTaskendDate(dto.getTaskendDate());
			mtaskBean.setVendorname(dto.getVendorname());

			session.saveOrUpdate(mtaskBean);

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

/*
	public List<MaintenanceItemDTO> getMaintenanceItems() {
		Session session = HibernateUtil.getSessionFactory().openSession();

		String queryString = "from MaintenanceBean";
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

			dtoList.add(oneItem);
		}

		session.flush();
		session.close();
		return dtoList;
	}
*/
	
	public MtaskDTO getMtask(Long mtaskItemId) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String queryString = "from MtaskBean " + "where mtaskitemid = ?";
			Query query = session.createQuery(queryString).setLong(0,
					mtaskItemId);
			List<MtaskBean> mtaskBeans = query.list();
			if (mtaskBeans.size() != 1) {
				throw new RuntimeException("Expected just one bean but got "
						+ mtaskBeans.size());
			}
			MtaskBean onebean = mtaskBeans.iterator().next();

			List<MtaskDTO> mtaskDTOList = new ArrayList<MtaskDTO>();

			MtaskDTO oneItem = new MtaskDTO();
			oneItem.setMaintenanceitemid(onebean.getMaintenanceitemid());
			oneItem.setItemCreatedBy(onebean.getItemCreatedBy());
			oneItem.setMtaskitemid(onebean.getMtaskitemid());
			oneItem.setNotes(onebean.getNotes());
			oneItem.setTaskendDate(onebean.getTaskendDate());
			oneItem.setTaskstartDate(onebean.getTaskstartDate());
			oneItem.setVendorname(onebean.getVendorname());

			return oneItem;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			return null;
		} finally {
			session.flush();
			session.close();
		}

	}

	public List<MtaskDTO> getMtasksForMaintenanceItem(Long maintenanceItemId) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String queryString = "from MtaskBean "
					+ "where maintenanceitemid = ? order by taskstartdate desc";
			Query query = session.createQuery(queryString).setLong(0,
					maintenanceItemId);
			List<MtaskBean> mtaskBeans = query.list();

			List<MtaskDTO> mtaskDTOList = new ArrayList<MtaskDTO>();
			for (MtaskBean onebean : mtaskBeans) {

				MtaskDTO oneItem = new MtaskDTO();
				oneItem.setMaintenanceitemid(onebean.getMaintenanceitemid());
				oneItem.setItemCreatedBy(onebean.getItemCreatedBy());
				oneItem.setMtaskitemid(onebean.getMtaskitemid());
				oneItem.setNotes(onebean.getNotes());
				oneItem.setTaskendDate(onebean.getTaskendDate());
				oneItem.setTaskstartDate(onebean.getTaskstartDate());
				oneItem.setVendorname(onebean.getVendorname());
				mtaskDTOList.add(oneItem);
			}
			return mtaskDTOList;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			return null;
		} finally {
			session.flush();
			session.close();
		}
	}

	public void updateMtask(MtaskDTO mtaskDTO) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		tx = session.beginTransaction();

		MtaskBean mtaskBean = (MtaskBean) session.load(MtaskBean.class,
				mtaskDTO.getMtaskitemid());
		mtaskBean.setItemCreatedBy(mtaskDTO.getItemCreatedBy());
		mtaskBean.setNotes(mtaskDTO.getNotes());
		mtaskBean.setTaskendDate(mtaskDTO.getTaskendDate());
		mtaskBean.setVendorname(mtaskDTO.getVendorname());
		session.merge(mtaskBean);

		tx.commit();
		session.flush();
		session.close();
	}

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

}
