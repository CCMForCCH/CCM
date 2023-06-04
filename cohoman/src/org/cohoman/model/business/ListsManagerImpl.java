package org.cohoman.model.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import org.cohoman.model.business.trash.TrashPerson;
import org.cohoman.model.business.trash.TrashRolesEnums;
import org.cohoman.model.business.trash.TrashRow;
import org.cohoman.model.business.trash.TrashSchedule;
import org.cohoman.model.business.trash.TrashTeam;
import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.dto.MtaskDTO;
import org.cohoman.model.dto.ProblemItemDTO;
import org.cohoman.model.dto.ProblemUpdateDTO;
import org.cohoman.model.dto.SecurityStartingPointDTO;
import org.cohoman.model.dto.UserDTO;
import org.cohoman.model.integration.SMS.SmsSender;
import org.cohoman.model.integration.email.SendEmail;
import org.cohoman.model.integration.persistence.beans.CchSectionTypeEnum;
import org.cohoman.model.integration.persistence.beans.SubstitutesBean;
import org.cohoman.model.integration.persistence.beans.TrashCycleBean;
import org.cohoman.model.integration.persistence.beans.TrashSubstitutesBean;
import org.cohoman.model.integration.persistence.beans.TrashTeamRowBean;
import org.cohoman.model.integration.persistence.beans.UnitBean;
import org.cohoman.model.integration.persistence.dao.MaintenanceDao;
import org.cohoman.model.integration.persistence.dao.MtaskDao;
import org.cohoman.model.integration.persistence.dao.ProblemUpdateDao;
import org.cohoman.model.integration.persistence.dao.ProblemsDao;
import org.cohoman.model.integration.persistence.dao.SecurityDao;
import org.cohoman.model.integration.persistence.dao.SubstitutesDao;
import org.cohoman.model.integration.persistence.dao.TrashCyclesDao;
import org.cohoman.model.integration.persistence.dao.TrashSubstitutesDao;
import org.cohoman.model.integration.persistence.dao.TrashTeamRowDao;
import org.cohoman.model.integration.persistence.dao.UnitsDao;
import org.cohoman.model.integration.persistence.dao.UserDao;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.cohoman.view.controller.utils.MaintenanceTypeEnums;
import org.cohoman.view.controller.utils.ProblemLocationEnums;
import org.cohoman.view.controller.utils.ProblemPriorityEnums;
import org.cohoman.view.controller.utils.ProblemStateEnums;
import org.cohoman.view.controller.utils.ProblemStatusEnums;
import org.cohoman.view.controller.utils.ProblemTypeEnums;
import org.cohoman.view.controller.utils.SortEnums;
import org.cohoman.view.controller.utils.TaskStatusEnums;
import org.cohoman.view.controller.utils.VendorEnums;

public class ListsManagerImpl implements ListsManager {

	public static final int NUMBER_OF_TRASH_CYCLES = 4;

	private UnitsDao unitsDao = null;
	private UserDao userDao = null;
	private SecurityDao securityDao = null;
	private SubstitutesDao substitutesDao = null;
	private TrashSubstitutesDao trashSubstitutesDao = null;
	private TrashCyclesDao trashCyclesDao = null;
	private TrashTeamRowDao trashTeamRowDao = null;
	private MaintenanceDao maintenanceDao = null;
	private MtaskDao mtaskDao = null;
	private ProblemsDao problemsDao = null;
	private ProblemUpdateDao problemUpdateDao = null;

	Logger logger = Logger.getLogger(this.getClass().getName());

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

	public TrashCyclesDao getTrashCyclesDao() {
		return trashCyclesDao;
	}

	public void setTrashCyclesDao(TrashCyclesDao trashCyclesDao) {
		this.trashCyclesDao = trashCyclesDao;
	}

	public TrashTeamRowDao getTrashTeamRowDao() {
		return trashTeamRowDao;
	}

	public void setTrashTeamRowDao(TrashTeamRowDao trashTeamRowDao) {
		this.trashTeamRowDao = trashTeamRowDao;
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

	public ProblemsDao getProblemsDao() {
		return problemsDao;
	}

	public void setProblemsDao(ProblemsDao problemsDao) {
		this.problemsDao = problemsDao;
	}

	public ProblemUpdateDao getProblemUpdateDao() {
		return problemUpdateDao;
	}

	public void setProblemUpdateDao(ProblemUpdateDao problemUpdateDao) {
		this.problemUpdateDao = problemUpdateDao;
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

	public void setTrashSubstitute(Date startingDate, String originalUsername,
			String substituteUsername) throws CohomanException {

		trashSubstitutesDao.setTrashSubstitute(startingDate, originalUsername,
				substituteUsername);
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

			// Exception code to remove unit 211 from the list.
			List<UnitBean> modifiedUnitBeanList = new ArrayList<UnitBean>();
			for (UnitBean oneUnitBean : unitBeanList) {
				if (!(oneUnitBean.getUnitnumber().equals("211")|| 
						oneUnitBean.getUnitnumber().equals("410") )) {
					modifiedUnitBeanList.add(oneUnitBean);
				}
			}
			unitBeanList = modifiedUnitBeanList;

		}

		// create ArrayList of strings based on unitBeanList with unit + person
		List<String> unitPlusPersonList = new ArrayList<String>();
		for (UnitBean unitBean : unitBeanList) {
			List<String> unitUserNames = userDao
					.getUserUsernamesAtUnit(unitBean.getUnitnumber());
			for (String oneUserName : unitUserNames) {
				unitPlusPersonList.add(unitBean.getUnitnumber() + " "
						+ oneUserName);
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
			// Just have strings like "214 norma"
			String[] unitPersonStrings = displayStartingUnit.split(" ");

			workingDate.add(Calendar.DAY_OF_YEAR, 7);

			onerow.setStartingDate(workingDate.getTime());
			onerow.setStartingDateString(formatter.format(workingDate.getTime()));
			onerow.setUnitnumber(unitPersonStrings[0]);
			onerow.setUsername(unitPersonStrings[1]);

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
		private String username;
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

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
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
	public void createMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO)
			throws CohomanException {
		maintenanceDao.createMaintenanceItem(maintenanceItemDTO);
	}

	public List<MaintenanceItemDTO> getMaintenanceItems(
			SortEnums sortEnum, MaintenanceTypeEnums maintenanceTypeEnum) {
		List<MaintenanceItemDTO> dtoListOut = new ArrayList<MaintenanceItemDTO>();
		List<MaintenanceItemDTO> dtoListIn = maintenanceDao
				.getMaintenanceItems(sortEnum, maintenanceTypeEnum);
		for (MaintenanceItemDTO oneDTO : dtoListIn) {


			// If NextServiceDate is > current date, set task
			// status to OVERDUE
			oneDTO = ageCurrentTaskStatus(oneDTO);
			
			// Convert userid to username and save in DTO
			Long userid = oneDTO.getItemCreatedBy();
			UserDTO theUser = userDao.getUser(userid);
			oneDTO.setUsername(theUser.getUsername());

			// Now do the same for assigned user, if there is an assigned user
			if (oneDTO.getAssignedTo() != null && oneDTO.getAssignedTo() != 0) {
				userid = oneDTO.getAssignedTo();
				theUser = userDao.getUser(userid);
				oneDTO.setAssignedToString(theUser.getUsername());
			}

			// Convert from Enum string to printable string
			TaskStatusEnums taskStatusEnum = TaskStatusEnums.valueOf(oneDTO.getTaskStatus());
			oneDTO.setTaskStatus(taskStatusEnum.toString());

			
			// Compute the printable Created On and last performed dates
			oneDTO.setPrintableCreatedDate(getPrintableDate(oneDTO
					.getItemCreatedDate()));
			oneDTO.setPrintableLastperformedDate(getPrintableDate(oneDTO
					.getLastperformedDate()));
			oneDTO.setPrintableNextServiceDate(getPrintableDate(oneDTO
					.getNextServiceDate()));

			// Decided not to use the last performed field, but will instead
			// compute it based on the list of associated Mtask items which
			// is NOT sorted as I want. So, will have to find the latest date.
			Date lastPerformedDate = null;
			List<MtaskDTO> mtaskDTOList = mtaskDao
					.getMtasksForMaintenanceItem(oneDTO.getMaintenanceitemid());
			if (mtaskDTOList != null) {
				for (MtaskDTO mtaskDTO : mtaskDTOList) {
					if (lastPerformedDate == null) {
						lastPerformedDate = mtaskDTO.getTaskendDate();
					} else {
						if (mtaskDTO.getTaskendDate() != null) {
							if (lastPerformedDate.compareTo(mtaskDTO
									.getTaskendDate()) < 0) {
								lastPerformedDate = mtaskDTO.getTaskendDate();
							}
						}
					}
				}
			}
			oneDTO.setLastperformedDate(lastPerformedDate);

			dtoListOut.add(oneDTO);
		}

		return dtoListOut;
	}

	// Newer, nicer versions of printable dates (3/5/17)
	private String getPrintableDate(Date justADate) {

		if (justADate == null) {
			return "";
		}
		SimpleDateFormat formatter;
		Calendar cal = new GregorianCalendar();
		cal.setTime(justADate);
		// if (cal.get(Calendar.HOUR_OF_DAY) == 0) {
		// formatter = new SimpleDateFormat("MMM d, yyyy");
		// } else {
		formatter = new SimpleDateFormat("MMM d, yyyy");
		// }
		return formatter.format(justADate.getTime());
	}

	private String getPrintableDateWithTime(Date justADate) {

		if (justADate == null) {
			return "";
		}
		SimpleDateFormat formatter;
		Calendar cal = new GregorianCalendar();
		cal.setTime(justADate);

		//if (cal.get(Calendar.HOUR_OF_DAY) == 0) {
			//formatter = new SimpleDateFormat("EEE, MMM d, yyyy");
		//} else {
			formatter = new SimpleDateFormat("MMM dd, yyyy hh:mm aa");
		//}

		return formatter.format(justADate.getTime());
	}

	private MaintenanceItemDTO ageCurrentTaskStatus(MaintenanceItemDTO itemDTO) {
		
		Calendar calNow = Calendar.getInstance();
		calNow.set(Calendar.HOUR_OF_DAY, 0);
		calNow.set(Calendar.MINUTE, 0);
		calNow.set(Calendar.SECOND, 0);
		calNow.set(Calendar.MILLISECOND, 0);

		Calendar calNextServiceDate = Calendar.getInstance();
		calNextServiceDate.setTime(itemDTO.getNextServiceDate());
		calNextServiceDate.set(Calendar.HOUR_OF_DAY, 0);
		calNextServiceDate.set(Calendar.MINUTE, 0);
		calNextServiceDate.set(Calendar.SECOND, 0);
		calNextServiceDate.set(Calendar.MILLISECOND, 0);
		
		if (calNextServiceDate.before(calNow)) {
			// Task is overdue.
			// Change task status to OVERDUE and write to DB as well
			// But, only due so if Task Status is UPTODATE
			// (i.e. don't overwrite INPROGRESS)
			if (itemDTO.getTaskStatus().equalsIgnoreCase(
					TaskStatusEnums.UPTODATE.name())) {
				itemDTO.setTaskStatus(TaskStatusEnums.OVERDUE.name());
				updateMaintenanceItem(itemDTO);
			}
		}
		
		return itemDTO;
	}
	
	public void updateMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO) {
		maintenanceItemDTO = ageCurrentTaskStatus(maintenanceItemDTO);
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
		List<MtaskDTO> dtoListIn = mtaskDao
				.getMtasksForMaintenanceItem(maintenanceItemId);
		for (MtaskDTO oneDTO : dtoListIn) {

			// Convert userid to username and save in DTO
			Long userid = oneDTO.getItemCreatedBy();
			UserDTO theUser = userDao.getUser(userid);
			oneDTO.setUsername(theUser.getUsername());

			// Compute the printable Created On and ending date
			oneDTO.setPrintableStartdate(getPrintableDate(oneDTO
					.getTaskstartDate()));
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

	/********************************************************************/
	// Problem item operations
	public void createProblemItem(ProblemItemDTO problemItemDTO)
			throws CohomanException {
		problemsDao.createProblemItem(problemItemDTO);
	}

	public List<ProblemItemDTO> getProblemItems(ProblemStateEnums problemStateEnum, SortEnums sortEnum) {
		List<ProblemItemDTO> dtoListOut = new ArrayList<ProblemItemDTO>();
		List<ProblemItemDTO> dtoListIn = problemsDao
				.getProblemItems(problemStateEnum, sortEnum);
		for (ProblemItemDTO oneDTO : dtoListIn) {
			
			// Convert userid to username and save in DTO
			Long userid = oneDTO.getItemCreatedBy();
			UserDTO theUser = userDao.getUser(userid);
			oneDTO.setUsername(theUser.getUsername());
			
			// Now do the same for assigned user, if there is an assigned user
			if (oneDTO.getAssignedTo() != null && oneDTO.getAssignedTo() != 0) {
				userid = oneDTO.getAssignedTo();
				theUser = userDao.getUser(userid);
				oneDTO.setAssignedToString(theUser.getUsername());
			}

			// Compute the printable Created On and Completed dates
			oneDTO.setPrintableCreatedDate(getPrintableDateWithTime(oneDTO
					.getItemCreatedDate()));
			oneDTO.setPrintableCompletedDate(getPrintableDateWithTime(oneDTO
					.getItemCompletedDate()));
			
			// Convert Enum names to strings
			ProblemLocationEnums locationEnum = ProblemLocationEnums.valueOf(oneDTO.getLocation());
			oneDTO.setLocation(locationEnum.toString());
			ProblemStatusEnums statusEnum = ProblemStatusEnums.valueOf(oneDTO.getProblemStatus());
			oneDTO.setProblemStatus(statusEnum.toString());
			ProblemTypeEnums typeEnum = ProblemTypeEnums.valueOf(oneDTO.getProblemType());
			oneDTO.setProblemType(typeEnum.toString());
			ProblemPriorityEnums priorityEnum = ProblemPriorityEnums.valueOf(oneDTO.getPriority());
			oneDTO.setPriority(priorityEnum.toString());
			if (oneDTO.getVendor() != null && !oneDTO.getVendor().equals("0")) {
				VendorEnums vendorEnum = VendorEnums.valueOf(oneDTO.getVendor());
				oneDTO.setVendor(vendorEnum.toString());
			}
			
			dtoListOut.add(oneDTO);
		}

		return dtoListOut;
	}
	
	public void updateProblemItem(ProblemItemDTO problemItemDTO) {
		
		// Automatically change the status to ASSIGNED if a new name assigned just occurred
		//if (problemItemDTO.getProblemStatus().equals(ProblemStatusEnums.NEW.name()) &&
				//problemItemDTO.getAssignedToString() != null) {
			//problemItemDTO.setProblemStatus(ProblemStatusEnums.ASSIGNED.name());
		//}
			
		problemsDao.updateProblemItem(problemItemDTO);
	}

	public void deleteProblemItem(ProblemItemDTO problemItemDTO) {
		problemsDao.deleteProblemItem(problemItemDTO);
	}

	public ProblemItemDTO getProblemItem(Long problemItemId) {
		
		ProblemItemDTO oneDTO = problemsDao.getProblemItem(problemItemId);
		
		// Convert userid to username and save in DTO
		Long userid = oneDTO.getItemCreatedBy();
		UserDTO theUser = userDao.getUser(userid);
		oneDTO.setUsername(theUser.getUsername());

		// Now do the same for assigned user, if there is one
		if (oneDTO.getAssignedTo() != null && oneDTO.getAssignedTo() != 0) {
			userid = oneDTO.getAssignedTo();
			theUser = userDao.getUser(userid);
			oneDTO.setAssignedToString(theUser.getUsername());
		}

		// Compute the printable Created On and Completed On dates
		oneDTO.setPrintableCreatedDate(getPrintableDateWithTime(oneDTO
				.getItemCreatedDate()));
		oneDTO.setPrintableCompletedDate(getPrintableDateWithTime(oneDTO
				.getItemCompletedDate()));

		return oneDTO;
	}
	/********************************************************************/

	/*
	 * Problem Update operations
	 */
	public void createProblemUpdate(ProblemUpdateDTO dto) throws CohomanException {
		problemUpdateDao.createProblemUpdate(dto);
	}

	public List<ProblemUpdateDTO> getProblemUpdatesForProblemItem(
			Long problemItemId) {

		List<ProblemUpdateDTO> dtoListOut = new ArrayList<ProblemUpdateDTO>();
		List<ProblemUpdateDTO> dtoListIn = problemUpdateDao
				.getProblemUpdatesForProblemItem(problemItemId);
		for (ProblemUpdateDTO oneDTO : dtoListIn) {

			// Convert userid to username and save in DTO
			Long userid = oneDTO.getItemCreatedBy();
			UserDTO theUser = userDao.getUser(userid);
			oneDTO.setUsername(theUser.getUsername());

			// Compute the printable Created BY date
			oneDTO.setPrintableUpdateDate(getPrintableDateWithTime(oneDTO
					.getUpdateDate()));
			dtoListOut.add(oneDTO);
		}
		return dtoListOut;

	}

	public ProblemUpdateDTO getProblemUpdate(Long problemUpdateId) {
		return problemUpdateDao.getProblemUpdate(problemUpdateId);
	}

	public void updateProblemUpdate(ProblemUpdateDTO problemUpdateDTO) {
		problemUpdateDao.updateProblemUpdate(problemUpdateDTO);
	}

	public void deleteProblemUpdate(Long problemUpdateId) {
		problemUpdateDao.deleteProblemUpdate(problemUpdateId);
	}

	/********************************************************************/

	/*
	 * Utilties for problem reporting
	 */
	
	public String getUsernameForProblemType(ProblemTypeEnums problemTypeEnum) {
		
		if (problemTypeEnum.equals(ProblemTypeEnums.ADMINISTRATIVE)) {
			return ("bill");
		} else if (problemTypeEnum.equals(ProblemTypeEnums.BUILDINGEX)){
			return ("annie");
		} else if (problemTypeEnum.equals(ProblemTypeEnums.BUILDINGIN)) {
			return ("anne");
		} else if (problemTypeEnum.equals(ProblemTypeEnums.ELECTRICAL)) {
			return ("katie");
		} else if (problemTypeEnum.equals(ProblemTypeEnums.ELEVATOR)) {
			return "bill";
		} else if (problemTypeEnum.equals(ProblemTypeEnums.FINANCIAL)) {
			return "bill";
		} else if (problemTypeEnum.equals(ProblemTypeEnums.FIREALARM)) {
			return "bill";
		} else if (problemTypeEnum.equals(ProblemTypeEnums.HEATINGCOOLING)) {
			return "bobm";
		} else if (problemTypeEnum.equals(ProblemTypeEnums.HOTWATER)) {
			return "bobm";
		} else if (problemTypeEnum.equals(ProblemTypeEnums.LANDSCAPING)) {
			return "polly";
		} else if (problemTypeEnum.equals(ProblemTypeEnums.LAUNDRYMACHINES)) {
			return "priscilla";
		} else if (problemTypeEnum.equals(ProblemTypeEnums.MECHANICAL)) {
			return "bill";
		} else if (problemTypeEnum.equals(ProblemTypeEnums.NONEOFTHEABOVE)) {
			return "bill";
		} else if (problemTypeEnum.equals(ProblemTypeEnums.PERIODICSERVICES)) {
			return "bill";
		} else if (problemTypeEnum.equals(ProblemTypeEnums.PESTCONTROL)) {
			return "joan";
		} else if (problemTypeEnum.equals(ProblemTypeEnums.PLUMBING)) {
			return "steve";
		} else if (problemTypeEnum.equals(ProblemTypeEnums.SNOWREMOVAL)) {
			return "polly";
		} else if (problemTypeEnum.equals(ProblemTypeEnums.TRASH)) {
			return "dick";
		}
		// return null;
		// Enum passed in does match the list we have in this method
		throw new RuntimeException("Invalid problem type specified: "
				+ problemTypeEnum.name());
	}


	/********************************************************************/

	/*
	 * Build Trash Schedule
	 */
	public List<TrashRow> getTrashSchedule() throws CohomanException {

		List<TrashRow> trashRowsToPrintTotal = new ArrayList<TrashRow>();

		// Delete stale entries from all trash DB tables.
		deleteStaleCyclesFromTrashDBs();

		// Preload rows already in the DB (Could be 1 to 4)
		List<TrashTeamRowBean> trashTeamRowBeans = trashTeamRowDao
				.getAllTrashRows();
		if (!trashTeamRowBeans.isEmpty()) {
			for (TrashTeamRowBean oneRowBean : trashTeamRowBeans) {
				TrashRow oneTrashRow = new TrashRow();
				oneTrashRow.setSundayDate(oneRowBean.getTrashteamrowdate());
				oneTrashRow.setOrganizer(oneRowBean.getTrashteamroworganizer());
				oneTrashRow.setStrongPerson(oneRowBean.getTrashteamrowstrong());
				oneTrashRow.setTeamMember1(oneRowBean.getTrashteamrowmember1());
				oneTrashRow.setTeamMember2(oneRowBean.getTrashteamrowmember2());
				trashRowsToPrintTotal.add(oneTrashRow);
			}
		}

		// Create TrashPerson List (For TrashSchedule)
		List<TrashPerson> trashPersonList = buildTrashPersonList();

		// Get Substitute beans (For TrashSchedule)
		List<TrashSubstitutesBean> trashSubstituteBeans = trashSubstitutesDao
				.getTrashSubstitutes();

		// Now, get all Trash Cycle beans in preparation for adding new trash
		// cycles if there are not enough (e.g. 4)
		List<TrashCycleBean> trashCycleBeanList = trashCyclesDao
				.getAllTrashCycles();

		// Sanity check to make sure the trashcycles table are in sync
		// with trashrows table.
		if (trashTeamRowBeans.isEmpty() && trashCycleBeanList.size() > 0) {
			logger.severe("TrashCycle table has cycles but TrashTeamRows table is empty, i.e. out of sync.");
			throw new RuntimeException(
					"TrashCycle table has cycles but TrashTeamRows table is empty, i.e. out of sync.");
		}

		// Now let's see if there are cycles to create. If there are, drop
		// into this code. Otherwise, ignore the loop below.
		if (trashCycleBeanList.isEmpty()
				|| trashCycleBeanList.size() < NUMBER_OF_TRASH_CYCLES) {

			// OK, we definitely need to create at least one cycle. Loop on
			// the following code until we have the desired amount of cycles.
			while (trashCycleBeanList.isEmpty()
					|| trashCycleBeanList.size() < NUMBER_OF_TRASH_CYCLES) {

				logger.info(">>>> Creating new Trash Cycle: trashPerson list size = "
						+ trashPersonList.size());

				// Create a TrashSchedule object that will hold 3 important
				// lists needed to make the schedule: TrashPersons,
				// TrashCycle Beans, and TrashSubstitute beans.
				TrashSchedule trashSchedule = new TrashSchedule(
						trashPersonList, trashCycleBeanList,
						trashSubstituteBeans);

				// Are there any Trash Cycles right now in the DB?
				if (trashCycleBeanList.isEmpty()) {

					// No, no trash cycles in the DB. Make the first cycle
					// using default values.
					TrashCycleBean trashCycleBean = new TrashCycleBean();
					trashCycleBean.setNextusertoskip(trashPersonList.get(0)
							.getUsername());

					// Date for brand new first cycle!!!!
					Calendar calToIncrement = Calendar.getInstance();

					// <<<<<<<<<<<<<<<<<<<<<<<< Starting Date !!!!!!!
					// >>>>>>>>>>>>>>>>>>>
					calToIncrement.set(2019, Calendar.DECEMBER, 15);

					trashCycleBean.setTrashcyclestartdate(calToIncrement
							.getTime());

					int teamsInOneCycle = trashPersonList.size();
					teamsInOneCycle = teamsInOneCycle / 4;
					int daysInCycle = teamsInOneCycle * 7;
					calToIncrement.add(Calendar.DAY_OF_YEAR, daysInCycle - 1);
					trashCycleBean.setTrashcycleenddate(calToIncrement
							.getTime());
					logger.info("Creating first Trash Cycle); date is "
							+ trashCycleBean.getTrashcyclestartdate());
					try {
						trashCyclesDao.createTrashCycle(trashCycleBean);
					} catch (Exception ex) {
						logger.severe("problem persisting TrashCycle for row0: "
								+ ex.toString());
						throw new RuntimeException(
								"problem persisting TrashCycle for row0: "
										+ ex.toString());
					}

				} else {

					// Already have some cycles. Do we need to create more?
					if (trashCycleBeanList.size() < NUMBER_OF_TRASH_CYCLES) {

						// Yes, need to create another cycle row. Use the last
						// bean in the table as a basis for creating a new
						// Trash Cycle Bean (which will be used to generate a
						// new cycle).
						// Then persist that new trash cycle bean.
						TrashCycleBean lastTrashCycleBean = trashCycleBeanList
								.get(trashCycleBeanList.size() - 1);
						TrashCycleBean newTrashCycleBean = trashSchedule
								.getNextTrashCycleDBRow(lastTrashCycleBean);
						logger.info("Creating new Trash Cycle beginning on "
								+ newTrashCycleBean.getTrashcyclestartdate()
								+ " and last team date is "
								+ newTrashCycleBean.getTrashcycleenddate());
						try {
							trashCyclesDao.createTrashCycle(newTrashCycleBean);
						} catch (Exception ex) {
							logger.severe("problem persisting TrashCycle for row1: "
									+ ex.toString());
							throw new RuntimeException(
									"problem persisting TrashCycle for row1");
						}
					}
				}

				// Build a new trash cycle based on the last bean in the list.
				trashCycleBeanList = trashCyclesDao.getAllTrashCycles();
				TrashCycleBean lastTrashCycleBean = trashCycleBeanList
						.get(trashCycleBeanList.size() - 1);
				List<TrashRow> trashRowsToPrint = trashSchedule
						.getTrashRowsNew(lastTrashCycleBean);
				trashRowsToPrintTotal.addAll(trashRowsToPrint);

				// Persist the newly created cycle in the DB
				for (TrashRow oneRow : trashRowsToPrint) {
					TrashTeamRowBean trashTeamRowBean = new TrashTeamRowBean();
					trashTeamRowBean
							.setTrashteamrowdate(oneRow.getSundayDate());
					trashTeamRowBean.setTrashteamroworganizer(oneRow
							.getOrganizer());
					trashTeamRowBean.setTrashteamrowstrong(oneRow
							.getStrongPerson());
					trashTeamRowBean.setTrashteamrowmember1(oneRow
							.getTeamMember1());
					trashTeamRowBean.setTrashteamrowmember2(oneRow
							.getTeamMember2());
					try {
						trashTeamRowDao.createTrashTeamRow(trashTeamRowBean);
					} catch (Exception ex) {
						logger.severe("problem persisting TrashTeamRow row1");
						throw new RuntimeException(
								"problem persisting TrashTeamRow row1");
					}

				}
			}
			// End of Loop to add new cycles.

		}

		// Rows need some final adjustments. First capitalize "my" entries.
		List<TrashRow> rowsToReturn = modifyRowsToShowMe(trashRowsToPrintTotal);

		// Next, modify the rows to include substitutions.
		rowsToReturn = addSubstitutesIntoRows(rowsToReturn);

		// Lastly, only print out 26 teams

		List<TrashRow> rowsToReturnFinally = new ArrayList<TrashRow>();
		Calendar calNow = Calendar.getInstance();
		for (TrashRow oneRow : rowsToReturn) {
			if (rowsToReturnFinally.size() >= 26) {
				break;
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"MMM d, yyyy");
			Date printableDateAsDate = null;
			try {
				printableDateAsDate = simpleDateFormat.parse(oneRow
						.getSundayDate());
			} catch (ParseException pe) {
				logger.severe("problem parsing date string "
						+ oneRow.getSundayDate());
				throw new RuntimeException("problem parsing date string "
						+ oneRow.getSundayDate());
			}

			if (CalendarUtils.dayEarlier(printableDateAsDate, calNow.getTime())) {
				// date has passed for this team; skip over it
				continue;
			}
			rowsToReturnFinally.add(oneRow);
		}

		return rowsToReturnFinally;

	}

	private void deleteStaleCyclesFromTrashDBs() {

		List<TrashCycleBean> trashCycleBeanList = trashCyclesDao
				.getAllTrashCycles();
		List<TrashTeamRowBean> trashTeamRowBeans = trashTeamRowDao
				.getAllTrashRows();
		Calendar calNow = Calendar.getInstance();

		// Determine whether there are any stale cycles by looking at the
		// TrashCycle entries. Stale => end date of Cycle < today
		for (TrashCycleBean oneCycleBean : trashCycleBeanList) {
			if (CalendarUtils.dayEarlier(oneCycleBean.getTrashcycleenddate(),
					calNow.getTime())) {
				// Delete Cycle
				logger.info("<<<<<<< Deleting Trash Cycle with starting date of "
						+ oneCycleBean.getTrashcyclestartdate());
				try {
					trashCyclesDao.deleteCycle(oneCycleBean.getTrashcycleid());
				} catch (Exception ex) {
					String errorMsg = "problem deleting TrashCycle row from database for id "
							+ oneCycleBean.getTrashcycleid();
					logger.severe(errorMsg);
					throw new RuntimeException(errorMsg);
				}

				// Next remove associated entries from the TeamRows table.
				for (TrashTeamRowBean oneRowBean : trashTeamRowBeans) {

					// Parse the row date into a Date object.
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"MMM d, yyyy");
					Date rowDate = null;
					try {
						rowDate = simpleDateFormat.parse(oneRowBean
								.getTrashteamrowdate());
					} catch (ParseException pe) {
						logger.severe("problem parsing date string "
								+ oneRowBean.getTrashteamrowdate());
						throw new RuntimeException(
								"problem parsing date string "
										+ oneRowBean.getTrashteamrowdate());
					}

					// Remove the row entry if the row date is "bounded" by the
					// start
					// and end date of the obsolete cycle being deleted.
					if (CalendarUtils.sameDay(rowDate,
							oneCycleBean.getTrashcyclestartdate())
							|| CalendarUtils.sameDay(rowDate,
									oneCycleBean.getTrashcycleenddate())
							|| (CalendarUtils.dayEarlier(
									oneCycleBean.getTrashcyclestartdate(),
									rowDate) && CalendarUtils.dayEarlier(
									rowDate,
									oneCycleBean.getTrashcycleenddate()))) {
						try {
							trashTeamRowDao.deleteTrashRow(oneRowBean
									.getTrashteamrowid());
						} catch (Exception ex) {
							String errorMsg = "problem deleting TrashRow from database for date "
									+ oneRowBean.getTrashteamrowdate();
							logger.severe(errorMsg);
							throw new RuntimeException(errorMsg);
						}
					}
				}

				// Lastly remove associated entries from the TrashSubstitutes
				// table.
				List<TrashSubstitutesBean> trashSubstitutesBeans = getTrashSubstitutes();
				if (trashSubstitutesBeans == null
						|| trashSubstitutesBeans.isEmpty()) {
					return;
				}
				for (TrashSubstitutesBean oneSubstituteBean : trashSubstitutesBeans) {

					// Remove the substitute entry if the starting date is
					// "bounded" by the start
					// and end date of the obsolete cycle being deleted.
					Date substituteDate = oneSubstituteBean.getStartingdate();
					if (CalendarUtils.sameDay(substituteDate,
							oneCycleBean.getTrashcyclestartdate())
							|| CalendarUtils.sameDay(substituteDate,
									oneCycleBean.getTrashcycleenddate())
							|| (CalendarUtils.dayEarlier(
									oneCycleBean.getTrashcyclestartdate(),
									substituteDate) && CalendarUtils
									.dayEarlier(substituteDate,
											oneCycleBean.getTrashcycleenddate()))) {
						try {
							trashSubstitutesDao
									.deleteTrashSubstitute(oneSubstituteBean
											.getSubstitutesid());
						} catch (Exception ex) {
							String errorMsg = "problem deleting substitute TrashRow "
									+ oneSubstituteBean.getStartingdate();
							logger.severe(errorMsg);
							throw new RuntimeException(errorMsg);
						}
					}
				}

			}
		}
	}

	private List<TrashRow> addSubstitutesIntoRows(List<TrashRow> trashRows) {

		List<TrashSubstitutesBean> trashSubstituteBeans = trashSubstitutesDao
				.getTrashSubstitutes();
		boolean doShowMe = true;
		// If there's no "current" user because we're not in a session (i.e.
		// we're
		// running a separate task), give up on the "ShowMe" idea and
		// just return the rows as-is.
		if (LoggingUtils.getCurrentUsername() == null) {
			doShowMe = false;
		}

		for (TrashRow oneRow : trashRows) {
			if (trashSubstituteBeans != null) {
				for (TrashSubstitutesBean oneSubBean : trashSubstituteBeans) {

					SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
					if (oneRow.getSundayDate().equals(
							sdf.format(oneSubBean.getStartingdate()))) {

						// Have a substitute for this row. Find the role on the
						// team who's
						// name matches the original name in the substitute
						// entry.
						String origTrashUser = oneSubBean.getOrigusername();
						String subTrashUser = oneSubBean
								.getSubstituteusername();

						// If organizer matches the substitute, replace on the
						// row line
						// including uppercasing the substitute if need be.
						if (oneRow.getOrganizer().equalsIgnoreCase(
								origTrashUser)) {
							if (doShowMe
									&& subTrashUser
											.equalsIgnoreCase(LoggingUtils
													.getCurrentUsername())) {
								subTrashUser = subTrashUser.toUpperCase();
							}
							oneRow.setOrganizer(subTrashUser + " for "
									+ oneRow.getOrganizer());
						}

						// If strong person matches the substitute, replace on
						// the row line
						// including uppercasing the substitute if need be.
						if (oneRow.getStrongPerson().equalsIgnoreCase(
								origTrashUser)) {
							if (doShowMe
									&& subTrashUser
											.equalsIgnoreCase(LoggingUtils
													.getCurrentUsername())) {
								subTrashUser = subTrashUser.toUpperCase();
							}
							oneRow.setStrongPerson(subTrashUser + " for "
									+ oneRow.getStrongPerson());
						}

						// If team member 1 matches the substitute, replace on
						// the row line
						// including uppercasing the substitute if need be.
						if (oneRow.getTeamMember1().equalsIgnoreCase(
								origTrashUser)) {
							if (doShowMe
									&& subTrashUser
											.equalsIgnoreCase(LoggingUtils
													.getCurrentUsername())) {
								subTrashUser = subTrashUser.toUpperCase();
							}
							oneRow.setTeamMember1(subTrashUser + " for "
									+ oneRow.getTeamMember1());
						}

						// If team member 2 matches the substitute, replace on
						// the row line
						// including uppercasing the substitute if need be.
						if (oneRow.getTeamMember2().equalsIgnoreCase(
								origTrashUser)) {
							if (doShowMe
									&& subTrashUser
											.equalsIgnoreCase(LoggingUtils
													.getCurrentUsername())) {
								subTrashUser = subTrashUser.toUpperCase();
							}
							oneRow.setTeamMember2(subTrashUser + " for "
									+ oneRow.getTeamMember2());
						}
					}
				}
			}

			// One last thing. Hack to change "jean" to "brian for jean"
			if (oneRow.getStrongPerson().equalsIgnoreCase("jean")) {
				oneRow.setStrongPerson("brian for jean");
			}
			if (oneRow.getTeamMember1().equalsIgnoreCase("jean")) {
				oneRow.setTeamMember1("brian for jean");
			}
			if (oneRow.getTeamMember2().equalsIgnoreCase("jean")) {
				oneRow.setTeamMember2("brian for jean");
			}

		}

		return trashRows;

	}

	private List<TrashRow> modifyRowsToShowMe(List<TrashRow> trashRows) {

		// If there's no "current" user because we're not in a session (i.e.
		// we're
		// running a separate task), give up on the "ShowMe" idea and
		// just return the rows as-is.
		if (LoggingUtils.getCurrentUsername() == null) {
			return trashRows;
		}

		for (TrashRow oneRow : trashRows) {

			// Capitalize entry if it's me.
			String organizerUsername = oneRow.getOrganizer();
			if (organizerUsername.equalsIgnoreCase(LoggingUtils
					.getCurrentUsername())) {
				organizerUsername = organizerUsername.toUpperCase();
				oneRow.setOrganizer(organizerUsername);
			}

			// Capitalize entry if it's me.
			String strongUsername = oneRow.getStrongPerson();
			if (strongUsername.equalsIgnoreCase(LoggingUtils
					.getCurrentUsername())) {
				strongUsername = strongUsername.toUpperCase();
				oneRow.setStrongPerson(strongUsername);
			}

			// Capitalize entry if it's me.
			String member1Username = oneRow.getTeamMember1();
			if (member1Username.equalsIgnoreCase(LoggingUtils
					.getCurrentUsername())) {
				member1Username = member1Username.toUpperCase();
				oneRow.setTeamMember1(member1Username);
			}

			// Capitalize entry if it's me.
			String member2Username = oneRow.getTeamMember2();
			if (member2Username.equalsIgnoreCase(LoggingUtils
					.getCurrentUsername())) {
				member2Username = member2Username.toUpperCase();
				oneRow.setTeamMember2(member2Username);
			}
		}
		return trashRows;

	}

	private List<TrashPerson> buildTrashPersonList() {

		// Build TrashPerson list for all CH and WE residents

		// Create brand new list of TrashPeople
		List<TrashPerson> trashPersonList = new ArrayList<TrashPerson>();

		// Start with units in Common House and then add in West End and sort.
		List<UnitBean> chweUnits = unitsDao.getCommonhouseUnits();
		chweUnits.addAll(unitsDao.getWestendUnits());
		Collections.sort(chweUnits);

		// For every unit, create TrashPerson(s)
		for (UnitBean thisUnitBean : chweUnits) {
			// replace this call with new method: getUsersAtUnit() and then
			// extract whatever name
			// we want along with the trash role in the for loop below
			List<String> usernamesInUnit = userDao
					.getUserUsernamesAtUnit(thisUnitBean.getUnitnumber());

			for (String thisUserUsername : usernamesInUnit) {
				/*
				 * // Temporarily use method to map FirstName to get Role String
				 * trashRole; if (thisUserUsername.equals("bonnie") ||
				 * thisUserUsername.equals("carol") ||
				 * thisUserUsername.equals("marianne") ||
				 * thisUserUsername.equals("molly") ||
				 * thisUserUsername.equals("elaine") ||
				 * thisUserUsername.equals("francie") ||
				 * thisUserUsername.equals("diane") ) { trashRole =
				 * TrashRolesEnums.ORGANIZER.name(); } else { if
				 * (thisUserUsername.equals("anne") ||
				 * thisUserUsername.equals("joan") ||
				 * thisUserUsername.equals("maddy") ||
				 * thisUserUsername.equals("annie") ||
				 * thisUserUsername.equals("johnm") ||
				 * thisUserUsername.equals("johnn") ||
				 * thisUserUsername.equals("jean") ||
				 * thisUserUsername.equals("meredith") ||
				 * thisUserUsername.equals("lindsey") ||
				 * thisUserUsername.equals("nasim") ||
				 * thisUserUsername.equals("katier")||
				 * thisUserUsername.equals("nick")){ trashRole =
				 * TrashRolesEnums.TEAMMEMBER.name(); } else { trashRole =
				 * TrashRolesEnums.STRONGPERSON.name(); } } if
				 * (thisUserUsername.equals("peg") ||
				 * thisUserUsername.equals("brian") ||
				 * thisUserUsername.equals("peter") ||
				 * thisUserUsername.equals("kerry") ||
				 * thisUserUsername.equals("neal") ||
				 * thisUserUsername.equals("sara") ||
				 * thisUserUsername.equals("felix") ||
				 * thisUserUsername.equals("carolyn")) { trashRole =
				 * TrashRolesEnums.NOROLE.name(); }
				 */
				// Get the Trash Role from the user structure from the user
				// database table
				String trashRole;
				UserDTO userDTO = userDao.getUserByUsername(thisUserUsername);
				trashRole = userDTO.getTrashrole();

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
		return trashPersonList;
	}

	public List<TrashTeam> getTrashTeams(int numberOfCycles)
			throws CohomanException {

		List<TrashCycleBean> trashCycleBeanList = trashCyclesDao
				.getAllTrashCycles();
		List<TrashSubstitutesBean> trashSubstituteBeans = trashSubstitutesDao
				.getTrashSubstitutes();
		TrashSchedule trashSchedule = new TrashSchedule(buildTrashPersonList(),
				trashCycleBeanList, trashSubstituteBeans);
		return trashSchedule.getTrashTeams(numberOfCycles);
	}

	public List<TrashRow> getTrashTeamsFromDB() {

		List<TrashRow> allTrashRows = new ArrayList<TrashRow>();

		// Preload rows already in the DB (Could be 1 to 4)
		List<TrashTeamRowBean> trashTeamRowBeans = trashTeamRowDao
				.getAllTrashRows();
		if (!trashTeamRowBeans.isEmpty()) {
			for (TrashTeamRowBean oneRowBean : trashTeamRowBeans) {
				TrashRow oneTrashRow = new TrashRow();
				oneTrashRow.setSundayDate(oneRowBean.getTrashteamrowdate());
				oneTrashRow.setOrganizer(oneRowBean.getTrashteamroworganizer());
				oneTrashRow.setStrongPerson(oneRowBean.getTrashteamrowstrong());
				oneTrashRow.setTeamMember1(oneRowBean.getTrashteamrowmember1());
				oneTrashRow.setTeamMember2(oneRowBean.getTrashteamrowmember2());
				allTrashRows.add(oneTrashRow);
			}
		}
		return allTrashRows;
	}

	public List<TrashPerson> getTrashPersonListOrig() {
		return buildTrashPersonList();
	}

	public void deleteTrashSubstitute(Long substitutesId) {
		trashSubstitutesDao.deleteTrashSubstitute(substitutesId);
	}

	public List<TrashSubstitutesBean> getTrashSubstitutes() {
		return trashSubstitutesDao.getTrashSubstitutes();
	}

	public TrashSubstitutesBean getTrashSubstitute(String startingDate,
			String origUsername) {
		return trashSubstitutesDao.getTrashSubstitute(startingDate,
				origUsername);
	}

	public void sendEmailToAddress(String emailAddress, String subject, String body) {
		SendEmail.sendEmailToAddress(emailAddress, subject, body);
	}

	public void sendTextMessageToPerson(String cellphoneNumber, String textMessage) {
		SmsSender.sendtextMessage(cellphoneNumber, textMessage);
	}

}
