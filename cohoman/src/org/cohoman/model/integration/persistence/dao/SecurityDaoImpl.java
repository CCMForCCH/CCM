package org.cohoman.model.integration.persistence.dao;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.dto.SecurityStartingPointDTO;
import org.cohoman.model.integration.persistence.beans.SecurityStartingPointBean;
import org.cohoman.model.integration.persistence.beans.UnitBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SecurityDaoImpl implements SecurityDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	public void updateSecurityStart(SecurityStartingPointDTO securityStartingPointDTO) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			// Find unit number string from units table
			Query query = session.createQuery("from UnitBean where unitnumber = ?")
				.setString(0, securityStartingPointDTO.getUnitnumber());
			
			List<UnitBean> unitBeanList = query.list();

			UnitBean unitBean = null;
			for (UnitBean oneUnitBean : unitBeanList) {
				// Just one unit (???)
				unitBean = (UnitBean) session.load(
						UnitBean.class, oneUnitBean.getUnitid());
			}

			SecurityStartingPointBean newSecurityStartingPointBean = 
				(SecurityStartingPointBean) session.load(
						SecurityStartingPointBean.class, 
						securityStartingPointDTO.getSection());
			newSecurityStartingPointBean.setSection(securityStartingPointDTO.getSection());
			newSecurityStartingPointBean.setUnitid(unitBean.getUnitid().toString());
			newSecurityStartingPointBean.setStartdate(securityStartingPointDTO.getStartdate());

			session.merge(newSecurityStartingPointBean);
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


	public SecurityStartingPointDTO getSecurityStart(String section) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String queryString = 
		   "SELECT securitystartingpoint.section, securitystartingpoint.unitid, " + 
		   "units.unitnumber, securitystartingpoint.startdate " +
		   "FROM securitystartingpoint JOIN units " +
		   "WHERE securitystartingpoint.unitid = units.unitid " +
		   "AND securitystartingpoint.section = ?";
		Query query = session.createSQLQuery(queryString)
			.setString(0, section);

		List<Object[]> sspDTOObjects = query.list();
		
		SecurityStartingPointDTO securityStartingPointDTO = null;  //TODO: better way?
		for (Object[] anObjectArray : sspDTOObjects) {
			securityStartingPointDTO = new SecurityStartingPointDTO();
			securityStartingPointDTO.setSection(anObjectArray[0].toString());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = new Date(0);  //TODO: better value?
			try {
				startDate = new java.sql.Date((simpleDateFormat.parse(anObjectArray[3].toString()).getTime()));
			} catch (ParseException ex) {
				System.out.println("getSecurityStart: parse exception");
				ex.printStackTrace();
			}
			securityStartingPointDTO.setStartdate(startDate);
			securityStartingPointDTO.setUnitid(anObjectArray[1].toString());
			securityStartingPointDTO.setUnitnumber(anObjectArray[2].toString());
		}
		
		session.flush();
		session.close();
		
		return securityStartingPointDTO;
	}
			
}
