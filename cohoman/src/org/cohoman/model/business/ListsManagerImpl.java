package org.cohoman.model.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.cohoman.model.business.trash.TrashCycle;
import org.cohoman.model.business.trash.TrashPerson;
import org.cohoman.model.business.trash.TrashRolesEnums;
import org.cohoman.model.business.trash.TrashRow;
import org.cohoman.model.business.trash.TrashSchedule;
import org.cohoman.model.business.trash.TrashTeam;
import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.dto.MtaskDTO;
import org.cohoman.model.dto.SecurityStartingPointDTO;
import org.cohoman.model.dto.UserDTO;
import org.cohoman.model.integration.persistence.beans.CchSectionTypeEnum;
import org.cohoman.model.integration.persistence.beans.SubstitutesBean;
import org.cohoman.model.integration.persistence.beans.TrashCycleBean;
import org.cohoman.model.integration.persistence.beans.TrashCycleRow;
import org.cohoman.model.integration.persistence.beans.TrashSubstitutesBean;
import org.cohoman.model.integration.persistence.beans.UnitBean;
import org.cohoman.model.integration.persistence.dao.MaintenanceDao;
import org.cohoman.model.integration.persistence.dao.MtaskDao;
import org.cohoman.model.integration.persistence.dao.SecurityDao;
import org.cohoman.model.integration.persistence.dao.SubstitutesDao;
import org.cohoman.model.integration.persistence.dao.TrashSubstitutesDao;
import org.cohoman.model.integration.persistence.dao.TrashSubstitutesDaoImpl;
import org.cohoman.model.integration.persistence.dao.UnitsDao;
import org.cohoman.model.integration.persistence.dao.UserDao;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.cohoman.view.controller.utils.TaskPriorityEnums;

public class ListsManagerImpl implements ListsManager {

	private UnitsDao unitsDao = null;
	private UserDao userDao = null;
	private SecurityDao securityDao = null;
	private SubstitutesDao substitutesDao = null;
	private TrashSubstitutesDao trashSubstitutesDao= null;
	private MaintenanceDao maintenanceDao = null;
	private MtaskDao mtaskDao = null;

	public UnitsDao getUnitsDao() {
		return unitsDao;
	}

	public void setUnitsDao(UnitsDao unitsDao) {
		this.unitsDao = unitsDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public SecurityDao getSecurityDao() {
		return securityDao;
	}

	public void setSecurityDao(SecurityDao securityDao) {
		this.securityDao = securityDao;
	}

	public SubstitutesDao getSubstitutesDao() {
		return substitutesDao;
	}

	public void setSubstitutesDao(SubstitutesDao substitutesDao) {
		this.substitutesDao = substitutesDao;
	}

	public TrashSubstitutesDao getTrashSubstitutesDao() {
		return trashSubstitutesDao;
	}

	public void setTrashSubstitutesDao(TrashSubstitutesDao trashSubstitutesDao) {
		this.trashSubstitutesDao = trashSubstitutesDao;
	}

	public MaintenanceDao getMaintenanceDao() {
		return maintenanceDao;
	}

	public void setMaintenanceDao(MaintenanceDao maintenanceDao) {
		this.maintenanceDao = maintenanceDao;
	}

	public MtaskDao getMtaskDao() {
		return mtaskDao;
	}

	public void setMtaskDao(MtaskDao mtaskDao) {
		this.mtaskDao = mtaskDao;
	}

	public List<UnitBean> getAllUnits() {
		return unitsDao.getAllUnits();
	}

	public List<SecurityRow> getSecurityList(CchSectionTypeEnum sectionEnum) {

		int column = 0;
		int row = 0;
		int numberOfUnits = 0;

		// Get starting date and starting unit
		SecurityStartingPointDTO securityStartingPointDTO = securityDao
				.getSecurityStart(sectionEnum.toString());
		String startingUnitnumber = securityStartingPointDTO.getUnitnumber();
		Calendar startingDate = Calendar.getInstance();
		startingDate.setTime(securityStartingPointDTO.getStartdate());
		startingDate.set(Calendar.HOUR_OF_DAY, 0);
		startingDate.set(Calendar.MINUTE, 0);
		startingDate.set(Calendar.SECOND, 0);
		startingDate.set(Calendar.MILLISECOND, 0);

		Calendar thisWeekDate = Calendar.getInstance();
		thisWeekDate.set(Calendar.HOUR_OF_DAY, 0);
		thisWeekDate.set(Calendar.MINUTE, 0);
		thisWeekDate.set(Calendar.SECOND, 0);
		thisWeekDate.set(Calendar.MILLISECOND, 0);

		if (sectionEnum == CchSectionTypeEnum.COMMONHOUSE) {
			CalendarUtils.adjustToStartingSunday(startingDate);
			// startingDate.add(Calendar.DAY_OF_YEAR, -14); // test
			CalendarUtils.adjustToStartingSunday(thisWeekDate);
		}

		if (sectionEnum == CchSectionTypeEnum.WESTEND) {
			CalendarUtils.adjustToStartingSaturday(startingDate);
			CalendarUtils.adjustToStartingSaturday(thisWeekDate);
		}

		// adjust to current week
		List<SecurityRow> chList = new ArrayList<SecurityRow>();
		List<UnitBean> unitBeanList = null;
		if (sectionEnum == CchSectionTypeEnum.COMMONHOUSE) {
			unitBeanList = unitsDao.getCommonhouseUnits();
		}
		
		if (sectionEnum == CchSectionTypeEnum.WESTEND) {
			unitBeanList = unitsDao.getWestendUnits();

			// Exception code to remove units 109 and 208 from the list.
			List<UnitBean> modifiedUnitBeanList = new ArrayList<UnitBean>();
			for (UnitBean oneUnitBean : unitBeanList) {
				if (!(oneUnitBean.getUnitnumber().equals("208") || oneUnitBean
						.getUnitnumber().equals("108"))) {
					modifiedUnitBeanList.add(oneUnitBean);
				}
			}
			unitBeanList = modifiedUnitBeanList;
		}
		if (unitBeanList == null) {
			throw new RuntimeException("Unknown CH section");
		}
		Collections.sort(unitBeanList);

		if (sectionEnum == CchSectionTypeEnum.WESTEND) {
			numberOfUnits = unitBeanList.size();

			CircularUnitList circularChList = new CircularUnitList(unitBeanList);

			circularChList.setUnitStart(startingUnitnumber);
			String displayStartingUnit = circularChList.getNextUnitLoop();
			while (startingDate.before(thisWeekDate)) {
				startingDate.add(Calendar.DAY_OF_YEAR, 7);
				displayStartingUnit = circularChList.getNextUnitLoop();
				// System.out.println(displayStartingUnit);
			}

			// TODO: error handling
			if (!startingDate.equals(thisWeekDate)) {
				System.out.println("Date problem!");
			}

			circularChList.setUnitStart(displayStartingUnit);
			while ((displayStartingUnit = circularChList.getNextUnit()) != null) {
				SecurityRow onerow = new SecurityRow();
				column = 0;

				// displayStartingUnit = circularChList.getNextUnit();
				// 02/28/16 !!!!!!!!!!!!!!!!!!!!
				List<String> unitLastNames = userDao
						.getUserLastNamesAtUnit(displayStartingUnit);
				// onerow.setHousehold(displayStartingUnit);
				String household = "";
				if (unitLastNames.size() > 0) {
					for (String lastname : unitLastNames) {
						if (!household.contains(lastname)) {
							if (household.length() == 0) {
								household = lastname;
							} else {
								household += "-" + lastname;
							}
						}
					}
				}
				onerow.setHousehold(displayStartingUnit + " " + household);

				onerow.setDate1(getNextSecurityDate(thisWeekDate.getTime(),
						row, column++, numberOfUnits));
				onerow.setDate2(getNextSecurityDate(thisWeekDate.getTime(),
						row, column++, numberOfUnits));
				onerow.setDate3(getNextSecurityDate(thisWeekDate.getTime(),
						row, column++, numberOfUnits));
				onerow.setDate4(getNextSecurityDate(thisWeekDate.getTime(),
						row++, column, numberOfUnits));
				chList.add(onerow);
			}
			return chList;
		} else {
			// create ArrayList of strings based on unitBeanList with unit +
			// person
			List<String> unitPlusPersonList = new ArrayList<String>();
			for (UnitBean unitBean : unitBeanList) {
				List<String> unitFirstNames = userDao
						.getUserFirstNamesAtUnit(unitBean.getUnitnumber());
				for (String oneFirstName : unitFirstNames) {
					unitPlusPersonList.add(unitBean.getUnitnumber() + " "
							+ oneFirstName);
				}
			}

			/*
			 * Calendar nextDate = startingDate; SimpleDateFormat formatter =
			 * new SimpleDateFormat("MMM d, yyyy");
			 * 
			 * for (String oneUnitPlusPerson : unitPlusPersonList) { SecurityRow
			 * onerow = new SecurityRow();
			 * onerow.setHousehold(oneUnitPlusPerson);
			 * onerow.setDate1(formatter.format(nextDate.getTime()));
			 * chList.add(onerow); nextDate.add(Calendar.DAY_OF_YEAR, 7); }
			 */

			int numberOfPersonUnits = unitPlusPersonList.size();

			CircularUnitPersonList circularChList = new CircularUnitPersonList(
					unitPlusPersonList);

			circularChList.setUnitStart(startingUnitnumber);
			String displayStartingUnit = circularChList.getNextUnitPersonLoop();
			while (startingDate.before(thisWeekDate)) {
				startingDate.add(Calendar.DAY_OF_YEAR, 7);
				displayStartingUnit = circularChList.getNextUnitPersonLoop();
				// System.out.println(displayStartingUnit);
			}

			circularChList.setUnitStart(displayStartingUnit);
			while ((displayStartingUnit = circularChList.getNextUnitPerson()) != null) {
				SecurityRow onerow = new SecurityRow();
				column = 0;

				onerow.setHousehold(displayStartingUnit);

				onerow.setDate1(getNextSecurityDate(thisWeekDate.getTime(),
						row++, 0, numberOfUnits));

				/*
				 * onerow.setDate2(getNextSecurityDate(thisWeekDate.getTime(),
				 * row, column++, numberOfUnits));
				 * onerow.setDate3(getNextSecurityDate(thisWeekDate.getTime(),
				 * row, column++, numberOfUnits));
				 * onerow.setDate4(getNextSecurityDate(thisWeekDate.getTime(),
				 * row++, column, numberOfUnits));
				 */

				chList.add(onerow);
			}

			return chList;
		}
	}

	// For CH security
	public void setSubstitute(Date startingDate, Long userid)
			throws CohomanException {

		SimpleDateFormat formatterForSQL = new SimpleDateFormat("yyyy-MM-dd");
		SubstitutesBean substitutesBean = substitutesDao
				.getSubstitute(formatterForSQL.format(startingDate.getTime()));
		if (substitutesBean != null) {

			// Found an entry: delete it first before creating a new one.
			substitutesDao.deleteSubstitute(substitutesBean.getSubstitutesid());
		}
		
		// Now, add in the substitute
		substitutesDao.setSubstitute(startingDate, userid);
	}

	
	public void setTrashSubstitute(Date startingDate, String originalUsername, String substituteUsername) 
			throws CohomanException {

		trashSubstitutesDao.setTrashSubstitute(startingDate, originalUsername, substituteUsername);
	}

	public List<SecurityDataForRow> getSecurityListWithSubs(
			CchSectionTypeEnum sectionEnum) {

		// Get starting date and starting unit
		SecurityStartingPointDTO securityStartingPointDTO = securityDao
				.getSecurityStart(sectionEnum.toString());
		String startingUnitnumber = securityStartingPointDTO.getUnitnumber();
		Calendar startingDate = Calendar.getInstance();
		startingDate.setTime(securityStartingPointDTO.getStartdate());
		startingDate.set(Calendar.HOUR_OF_DAY, 0);
		startingDate.set(Calendar.MINUTE, 0);
		startingDate.set(Calendar.SECOND, 0);
		startingDate.set(Calendar.MILLISECOND, 0);

		Calendar thisWeekDate = Calendar.getInstance();
		thisWeekDate.set(Calendar.HOUR_OF_DAY, 0);
		thisWeekDate.set(Calendar.MINUTE, 0);
		thisWeekDate.set(Calendar.SECOND, 0);
		thisWeekDate.set(Calendar.MILLISECOND, 0);

		if (sectionEnum == CchSectionTypeEnum.COMMONHOUSE) {
			CalendarUtils.adjustToStartingSunday(startingDate);
			// startingDate.add(Calendar.DAY_OF_YEAR, -14); // test
			CalendarUtils.adjustToStartingSunday(thisWeekDate);
		}

		if (sectionEnum == CchSectionTypeEnum.WESTEND) {
			CalendarUtils.adjustToStartingSaturday(startingDate);
			CalendarUtils.adjustToStartingSaturday(thisWeekDate);
		}

		// adjust to current week
		List<SecurityDataForRow> chListWithSubs = new ArrayList<SecurityDataForRow>();
		List<UnitBean> unitBeanList = null;
		if (sectionEnum == CchSectionTypeEnum.COMMONHOUSE) {
			unitBeanList = unitsDao.getCommonhouseUnits();
			
			// Exception code to remove units 312 and 414 from the list.
			List<UnitBean> modifiedUnitBeanList = new ArrayList<UnitBean>();
			for (UnitBean oneUnitBean : unitBeanList) {
				if (!(oneUnitBean.getUnitnumber().equals("312") || oneUnitBean
						.getUnitnumber().equals("414"))) {
					modifiedUnitBeanList.add(oneUnitBean);
				}
			}
			unitBeanList = modifiedUnitBeanList;

		}

		// create ArrayList of strings based on unitBeanList with unit + person
		List<String> unitPlusPersonList = new ArrayList<String>();
		for (UnitBean unitBean : unitBeanList) {
			List<String> unitFirstNames = userDao
					.getUserFirstNamesAtUnit(unitBean.getUnitnumber());
			for (String oneFirstName : unitFirstNames) {
				unitPlusPersonList.add(unitBean.getUnitnumber() + " "
						+ oneFirstName);
			}
		}

		CircularUnitPersonList circularChList = new CircularUnitPersonList(
				unitPlusPersonList);

		circularChList.setUnitStart(startingUnitnumber);
		String displayStartingUnit = circularChList.getNextUnitPersonLoop();
		while (startingDate.before(thisWeekDate)) {
			startingDate.add(Calendar.DAY_OF_YEAR, 7);
			displayStartingUnit = circularChList.getNextUnitPersonLoop();
		}

		circularChList.setUnitStart(displayStartingUnit);
		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
		SimpleDateFormat formatterForSQL = new SimpleDateFormat("yyyy-MM-dd");
		Calendar workingDate = Calendar.getInstance();
		
		// hack to correct off0by-one error 9/30/2017
		thisWeekDate.add(Calendar.DAY_OF_YEAR, -7);
		
		workingDate.setTime(thisWeekDate.getTime());

		// Walk through entire list of unit-people creating a SecurityDataForRow
		// object for each one.
		while ((displayStartingUnit = circularChList.getNextUnitPerson()) != null) {

			SecurityDataForRow onerow = new SecurityDataForRow();
			// Just have strings like "214 Norma"
			String[] unitPersonStrings = displayStartingUnit.split(" ");

			workingDate.add(Calendar.DAY_OF_YEAR, 7);

			onerow.setStartingDate(workingDate.getTime());
			onerow.setStartingDateString(formatter.format(workingDate.getTime()));
			onerow.setUnitnumber(unitPersonStrings[0]);
			onerow.setFirstname(unitPersonStrings[1]);

			// Check the substitues table to see if there is a substitute
			// for the associated unit-person. If found, add it to
			// the SecurityDataForRow entry.
			SubstitutesBean substitutesBean = substitutesDao
					.getSubstitute(formatterForSQL.format(workingDate.getTime()));
			if (substitutesBean != null) {

				Long userid = substitutesBean.getUserid();
				onerow.setSubstitute((userDao.getUser(userid).getFirstname()
						+ " " + userDao.getUser(userid).getLastname()));
			}
			chListWithSubs.add(onerow);
		}

		return chListWithSubs;

	}

	private String getNextSecurityDate(Date baseDate, int row, int column,
			int numberOfChUnits) {

		Calendar workingDate = Calendar.getInstance();
		int daysToAdd;

		daysToAdd = (row * 7) + (column * 7 * numberOfChUnits);
		workingDate.setTime(baseDate);
		workingDate.add(Calendar.DAY_OF_YEAR, daysToAdd);
		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
		return formatter.format(workingDate.getTime());
	}

	public SubstitutesBean getSubstitute(String startingDate) {
		
		return substitutesDao.getSubstitute(startingDate);
		
	}

	public void deleteSubstitute(Long substitutesId) {
		
		substitutesDao.deleteSubstitute(substitutesId);
		
	}
	
	public class SecurityDataForRow {

		private Date startingDate;
		private String unitnumber;
		private String firstname;
		private Long userid;
		private String startingDateString;
		private String substitute;

		public Date getStartingDate() {
			return startingDate;
		}

		public void setStartingDate(Date startingDate) {
			this.startingDate = startingDate;
		}

		public String getUnitnumber() {
			return unitnumber;
		}

		public void setUnitnumber(String unitnumber) {
			this.unitnumber = unitnumber;
		}

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

		public Long getUserid() {
			return userid;
		}

		public void setUserid(Long userid) {
			this.userid = userid;
		}

		public String getStartingDateString() {
			return startingDateString;
		}

		public void setStartingDateString(String startingDateString) {
			this.startingDateString = startingDateString;
		}

		public String getSubstitute() {
			return substitute;
		}

		public void setSubstitute(String substitute) {
			this.substitute = substitute;
		}

	}

	public class SecurityRow {
		private String household;
		private String date1;
		private String date2;
		private String date3;
		private String date4;

		public String getHousehold() {
			return household;
		}

		public void setHousehold(String household) {
			this.household = household;
		}

		public String getDate1() {
			return date1;
		}

		public void setDate1(String date1) {
			this.date1 = date1;
		}

		public String getDate2() {
			return date2;
		}

		public void setDate2(String date2) {
			this.date2 = date2;
		}

		public String getDate3() {
			return date3;
		}

		public void setDate3(String date3) {
			this.date3 = date3;
		}

		public String getDate4() {
			return date4;
		}

		public void setDate4(String date4) {
			this.date4 = date4;
		}

	}

	public class CircularUnitList {

		String[] circularUnitListArray;
		int listStartingIndex;
		int currentIndex;
		boolean justStarting = true;

		public CircularUnitList(List<UnitBean> inputList) {

			String[] theUnitArray = new String[inputList.size()];
			int insertIdx = 0;
			for (UnitBean unitBean : inputList) {
				theUnitArray[insertIdx++] = unitBean.getUnitnumber();
			}
			circularUnitListArray = theUnitArray;
		}

		public void setUnitStart(String startingUnit) {

			for (int idx = 0; idx < circularUnitListArray.length; idx++) {
				if (startingUnit.equals(circularUnitListArray[idx])) {
					listStartingIndex = idx;
					currentIndex = idx;
					justStarting = true;
					return;
				}
			}
			// TODO: check for unit not found!!
			System.out.println("Error: unit " + startingUnit
					+ " not found in list!");
		}

		public String getNextUnit() {
			// If we cycled through back to the start, return null
			if (currentIndex == listStartingIndex && !justStarting) {
				return null;
			}

			String unit = circularUnitListArray[currentIndex];

			// Advance pointer to next entry.
			if (currentIndex == circularUnitListArray.length - 1) {
				currentIndex = 0;
			} else {
				currentIndex++;
			}

			justStarting = false;
			return unit;
		}

		public String getNextUnitLoop() {

			String unit = circularUnitListArray[currentIndex];

			// Advance pointer to next entry.
			if (currentIndex == circularUnitListArray.length - 1) {
				currentIndex = 0;
			} else {
				currentIndex++;
			}

			return unit;
		}

	}

	public class CircularUnitPersonList {

		String[] circularUnitListArray;
		int listStartingIndex;
		int currentIndex;
		boolean justStarting = true;

		public CircularUnitPersonList(List<String> inputList) {

			String[] theUnitPersonArray = new String[inputList.size()];
			int insertIdx = 0;
			for (String oneUnitPerson : inputList) {
				theUnitPersonArray[insertIdx++] = oneUnitPerson;
			}
			circularUnitListArray = theUnitPersonArray;
		}

		public void setUnitStart(String startingUnit) {

			for (int idx = 0; idx < circularUnitListArray.length; idx++) {
				// if (startingUnit.equals(circularUnitListArray[idx])) {
				if (circularUnitListArray[idx].startsWith(startingUnit)) {
					listStartingIndex = idx;
					currentIndex = idx;
					justStarting = true;
					return;
				}
			}
			// TODO: check for unit not found!!
			System.out.println("Error: unit " + startingUnit
					+ " not found in list!");
		}

		public String getNextUnitPerson() {
			// If we cycled through back to the start, return null
			if (currentIndex == listStartingIndex && !justStarting) {
				return null;
			}

			String unit = circularUnitListArray[currentIndex];

			// Advance pointer to next entry
			if (currentIndex == circularUnitListArray.length - 1) {
				currentIndex = 0;
			} else {
				currentIndex++;
			}


			justStarting = false;
			return unit;
		}

		public String getNextUnitPersonLoop() {

			String unit = circularUnitListArray[currentIndex];

			// Advance pointer to next entry.
			if (currentIndex == circularUnitListArray.length - 1) {
				currentIndex = 0;
			} else {
				currentIndex++;
			}

			return unit;
		}

	}
	
	// Maintenance item operations
	public void createMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO) throws CohomanException {
		maintenanceDao.createMaintenanceItem(maintenanceItemDTO);
	}
	
	public List<MaintenanceItemDTO> getMaintenanceItems() {
		List<MaintenanceItemDTO> dtoListOut = new ArrayList<MaintenanceItemDTO>();
		List<MaintenanceItemDTO> dtoListIn = maintenanceDao.getMaintenanceItems();
		for(MaintenanceItemDTO oneDTO : dtoListIn) {
			
			// Convert userid to username and save in DTO
			Long userid = oneDTO.getItemCreatedBy();
			UserDTO theUser = userDao.getUser(userid);
			oneDTO.setUsername(theUser.getUsername());
			
			// Compute the printable Created On and last performed dates
			oneDTO.setPrintableCreatedDate(getPrintableDate(oneDTO.getItemCreatedDate()));
			oneDTO.setPrintableLastperformedDate(getPrintableDate(oneDTO.getLastperformedDate()));
			
			// Decided not to use the last performed field, but will instead
			// compute it based on the list of associated Mtask items which
			// is NOT sorted as I want. So, will have to find the latest date.
			Date lastPerformedDate = null;
			List<MtaskDTO>	mtaskDTOList = mtaskDao.getMtasksForMaintenanceItem(oneDTO.getMaintenanceitemid());
			if (mtaskDTOList != null) {
				for (MtaskDTO mtaskDTO : mtaskDTOList) {
					if (lastPerformedDate == null) {
						lastPerformedDate = mtaskDTO.getTaskendDate();
					} else {
						if (mtaskDTO.getTaskendDate() != null) {
							if (lastPerformedDate.compareTo(mtaskDTO.getTaskendDate()) < 0) {
								lastPerformedDate = mtaskDTO.getTaskendDate();						
							}
						}
					}
				}
			}
			oneDTO.setLastperformedDate(lastPerformedDate);
			
			dtoListOut.add(oneDTO);
		}
		
		// Hacky sorting by priority. Make 3 passes, one for High, Medium, and Low
		List<MaintenanceItemDTO> dtoListOutFinal = new ArrayList<MaintenanceItemDTO>();
		
		for(MaintenanceItemDTO oneDTO : dtoListOut) {
			if (oneDTO.getPriority().equalsIgnoreCase(TaskPriorityEnums.HIGH.name())) {
				dtoListOutFinal.add(oneDTO);
			}
		}

		for(MaintenanceItemDTO oneDTO : dtoListOut) {
			if (oneDTO.getPriority().equalsIgnoreCase(TaskPriorityEnums.MEDIUM.name())) {
				dtoListOutFinal.add(oneDTO);
			}
		}

		for(MaintenanceItemDTO oneDTO : dtoListOut) {
			if (oneDTO.getPriority().equalsIgnoreCase(TaskPriorityEnums.LOW.name())) {
				dtoListOutFinal.add(oneDTO);
			}
		}

		return dtoListOutFinal;
	}

	// Newer, nicer versions of printable dates (3/5/17)
	private String getPrintableDate(Date justADate) {
		
		if (justADate == null) {
			return "";
		}
		SimpleDateFormat formatter;
		Calendar cal = new GregorianCalendar();
		cal.setTime(justADate);
		//if (cal.get(Calendar.HOUR_OF_DAY) == 0) {
			//formatter = new SimpleDateFormat("MMM d, yyyy");
		//} else {
			formatter = new SimpleDateFormat("MMM d, yyyy");
		//}
		return formatter.format(justADate.getTime());
	}

	public void updateMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO) {
		maintenanceDao.updateMaintenanceItem(maintenanceItemDTO);
	}

	public void deleteMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO) {
		maintenanceDao.deleteMaintenanceItem(maintenanceItemDTO);
	}

	public MaintenanceItemDTO getMaintenanceItem(Long maintenanceItemId) {
		return maintenanceDao.getMaintenanceItem(maintenanceItemId);
	}

	public void createMtask(MtaskDTO dto) throws CohomanException {
		mtaskDao.createMtask(dto);
	}

	public List<MtaskDTO> getMtasksForMaintenanceItem(Long maintenanceItemId) {
		List<MtaskDTO> dtoListOut = new ArrayList<MtaskDTO>();
		List<MtaskDTO> dtoListIn = mtaskDao.getMtasksForMaintenanceItem(maintenanceItemId);
		for(MtaskDTO oneDTO : dtoListIn) {
			
			// Convert userid to username and save in DTO
			Long userid = oneDTO.getItemCreatedBy();
			UserDTO theUser = userDao.getUser(userid);
			oneDTO.setUsername(theUser.getUsername());
			
			// Compute the printable Created On and ending date
			oneDTO.setPrintableStartdate(getPrintableDate(oneDTO.getTaskstartDate()));
			oneDTO.setPrintableEnddate(getPrintableDate(oneDTO.getTaskendDate()));
			dtoListOut.add(oneDTO);
		}
		return dtoListOut;

	}

	public MtaskDTO getMtask(Long mtaskItemId) {		
		return mtaskDao.getMtask(mtaskItemId);
	}

	public void updateMtask(MtaskDTO mtaskDTO) {
		mtaskDao.updateMtask(mtaskDTO);
	}

	public void deleteMtask(Long mtaskitemid) {
		mtaskDao.deleteMtask(mtaskitemid);
	}

	/*
	 * Build Trash Schedule
	 */
	private TrashSchedule getTrashScheduleObject() {
		
		// Build TrashPerson list for all CH and WE residents
		
		// Create brand new list of TrashPeople
		List<TrashPerson> trashPersonList = new ArrayList<TrashPerson>();
		
		// Start with units in Common House and then add in West End and sort.
		List<UnitBean> chweUnits = unitsDao.getCommonhouseUnits();
		chweUnits.addAll(unitsDao.getWestendUnits());
		Collections.sort(chweUnits);
			
		// For every unit, create TrashPerson(s)
		for (UnitBean thisUnitBean : chweUnits) {
			// replace this call with new method: getUsersAtUnit() and then extract whatever name
			// we want along with the trash role in the for loop below
			List<String> usernamesInUnit = userDao.getUserUsernamesAtUnit(thisUnitBean.getUnitnumber());

			for (String thisUserUsername : usernamesInUnit) {
				
				// Temporarily use method to map FirstName to get Role
				String trashRole;
				if (thisUserUsername.equals("bonnie") || thisUserUsername.equals("carol") ||
						thisUserUsername.equals("marianne") || thisUserUsername.equals("molly") ||
						thisUserUsername.equals("elaine") || thisUserUsername.equals("francie")) {
						trashRole = TrashRolesEnums.ORGANIZER.name();
				} else {
					if (thisUserUsername.equals("anne") || thisUserUsername.equals("joan") ||
							thisUserUsername.equals("maddy") || thisUserUsername.equals("lindsey") ||
						    thisUserUsername.equals("annie") || thisUserUsername.equals("johnm") ||
						    thisUserUsername.equals("johnn")){
						trashRole = TrashRolesEnums.TEAMMEMBER.name();
					} else {
						trashRole = TrashRolesEnums.STRONGPERSON.name();
					}
				}
				if (thisUserUsername.equals("peg") ||  thisUserUsername.equals("diane") ||
					thisUserUsername.equals("brian") || thisUserUsername.equals("peter") ||
					thisUserUsername.equals("kerry") || thisUserUsername.equals("neal") ||
					thisUserUsername.equals("sara") || thisUserUsername.equals("felix"))
				{
					trashRole = TrashRolesEnums.NOROLE.name();
				}
				if (thisUserUsername.equals("diane")) {
					trashRole = TrashRolesEnums.NOROLE.name();
				}
				
				// Skip over people who have no role
				if (trashRole.equals(TrashRolesEnums.NOROLE.name())) {
					continue;
				}
				
				// Now for all people in this unit, create a TrashPerson object
				// and add it to the list of TrashPeople
				TrashPerson trashPerson = new TrashPerson();
				trashPerson.setUnitnumber(thisUnitBean.getUnitnumber());
				trashPerson.setUsername(thisUserUsername);
				trashPerson.setTrashRole(trashRole);
				trashPersonList.add(trashPerson);		
			}
		}
				
		// Pass cycle to new TrashSchedule to
		// compute the printable rows.
		// temp until the date for the 
		// temporarily create a bean and fill it in. Later on will read this
		// from the database. And when done, will update all 4 rows
		List<TrashCycleRow> trashCycleRows = new ArrayList<TrashCycleRow>();
		TrashCycleRow trashCycleRow = new TrashCycleRow();
		trashCycleRow.setNextuseridtoskip(trashPersonList.get(0).getUsername());
		Calendar calToIncrement = Calendar.getInstance();
		calToIncrement.set(2019, Calendar.OCTOBER, 2);
	/* temporarily commented out so I see every day; will need to put back
		int todaysDayOfWeek = calToIncrement.get(Calendar.DAY_OF_WEEK);
		calToIncrement = CalendarUtils.adjustToStartingSunday(calToIncrement);
		if (todaysDayOfWeek != Calendar.SUNDAY) {
			// Add a week so it's next week's team, unless it's Sunday
			calToIncrement.add(Calendar.DAY_OF_YEAR, 7);  
		}
	*/

		//rightnow.add(Calendar.DAY_OF_YEAR, 10);  // temp to have a date within the cycle
		trashCycleRow.setTrashcyclestartdate(calToIncrement.getTime());
		
		trashCycleRows.add(trashCycleRow);
		
		//<<<<<<<<
		int teamsInOneCycle = trashPersonList.size();
		teamsInOneCycle = teamsInOneCycle / 4;
		
		int leftoverPeople = trashPersonList.size() % 4;

		TrashCycleRow trashCycleRow2 = new TrashCycleRow();
		trashCycleRow2.setNextuseridtoskip(trashPersonList.get(leftoverPeople).getUsername());		// Compute number of teams in one cycle
		calToIncrement.add(Calendar.DAY_OF_YEAR, teamsInOneCycle);  
		trashCycleRow2.setTrashcyclestartdate(calToIncrement.getTime());
		trashCycleRows.add(trashCycleRow2);
		
		TrashCycleRow trashCycleRow3 = new TrashCycleRow();
		trashCycleRow3.setNextuseridtoskip(trashPersonList.get(leftoverPeople * 2).getUsername());		// Compute number of teams in one cycle
		calToIncrement.add(Calendar.DAY_OF_YEAR, teamsInOneCycle);  
		trashCycleRow3.setTrashcyclestartdate(calToIncrement.getTime());
		trashCycleRows.add(trashCycleRow3);
	
		// >>>>>>>>>>>
		
		// Get Substitutes for Trash teams
		List<TrashSubstitutesBean> trashSubstituteBeans = trashSubstitutesDao.getTrashSubstitutes();
		
		TrashSchedule trashSchedule = new TrashSchedule(trashPersonList, trashCycleRows, trashSubstituteBeans);
		//return trashSchedule.getTrashRows();
		return trashSchedule;
					
	}

	public List<TrashRow> getTrashSchedule(int numberOfCycles) {
		TrashSchedule trashSchedule = getTrashScheduleObject();
		return trashSchedule.getTrashRows(numberOfCycles);	
	}
	
	public List<TrashTeam> getTrashTeams(int numberOfCycles) {
		TrashSchedule trashSchedule = getTrashScheduleObject();
		return trashSchedule.getTrashTeams(numberOfCycles);
	}

	public List<TrashPerson> getTrashPersonListOrig() {
		TrashSchedule trashSchedule = getTrashScheduleObject();
		return trashSchedule.getTrashPersonListOrig();
	}
	
	public void deleteTrashSubstitute(Long substitutesId) {
		trashSubstitutesDao.deleteTrashSubstitute(substitutesId);
	}

	public List<TrashSubstitutesBean> getTrashSubstitutes() {
		return trashSubstitutesDao.getTrashSubstitutes();
	}

	public TrashSubstitutesBean getTrashSubstitute(String startingDate, String origUsername) {
		return trashSubstitutesDao.getTrashSubstitute(startingDate, origUsername);
	}

}