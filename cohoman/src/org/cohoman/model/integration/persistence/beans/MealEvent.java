package org.cohoman.model.integration.persistence.beans;

import java.util.Date;

public class MealEvent extends EventBean {

	private Long mealEventId = null;
	private String menu = null;
	private Long cook1 = null;
	private Long cook2 = null;
	private Long cook3 = null;
	private Long cook4 = null;
	private Long cleaner1 = null;
	private Long cleaner2 = null;
	private Long cleaner3 = null;
	private String cook1String = null;
	private String cook2String = null;
	private String cook3String = null;
	private String cook4String = null;
	private String cleaner1String = null;
	private String cleaner2String = null;
	private String cleaner3String = null;
	private int maxattendees = 0;
	private boolean ismealclosed = false;

	public MealEvent(Date eventDate) {
		super(eventDate);
	}

	public Long getMealEventId() {
		return mealEventId;
	}

	public int getUsableEventid() {
		return super.getEventid().intValue();
	}

	public String getChoosableEventDate() {
		String mealChoice = super.getPrintableEventDate() + ",  Lead: " + cook1String;
		return mealChoice;
	}

	public void setMealEventId(Long mealEventId) {
		this.mealEventId = mealEventId;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public Long getCook1() {
		return cook1;
	}

	public void setCook1(Long cook1) {
		this.cook1 = cook1;
	}

	public Long getCook2() {
		return cook2;
	}

	public void setCook2(Long cook2) {
		this.cook2 = cook2;
	}

	public Long getCook3() {
		return cook3;
	}

	public void setCook3(Long cook3) {
		this.cook3 = cook3;
	}

	public Long getCook4() {
		return cook4;
	}

	public void setCook4(Long cook4) {
		this.cook4 = cook4;
	}

	public Long getCleaner1() {
		return cleaner1;
	}

	public void setCleaner1(Long cleaner1) {
		this.cleaner1 = cleaner1;
	}

	public Long getCleaner2() {
		return cleaner2;
	}

	public void setCleaner2(Long cleaner2) {
		this.cleaner2 = cleaner2;
	}

	public Long getCleaner3() {
		return cleaner3;
	}

	public void setCleaner3(Long cleaner3) {
		this.cleaner3 = cleaner3;
	}

	public String getCook1String() {
		return cook1String;
	}

	public void setCook1String(String cook1String) {
		this.cook1String = cook1String;
	}

	public String getCook2String() {
		return cook2String;
	}

	public void setCook2String(String cook2String) {
		this.cook2String = cook2String;
	}

	public String getCook3String() {
		return cook3String;
	}

	public void setCook3String(String cook3String) {
		this.cook3String = cook3String;
	}

	public String getCook4String() {
		return cook4String;
	}

	public void setCook4String(String cook4String) {
		this.cook4String = cook4String;
	}

	public String getCleaner1String() {
		return cleaner1String;
	}

	public void setCleaner1String(String cleaner1String) {
		this.cleaner1String = cleaner1String;
	}

	public String getCleaner2String() {
		return cleaner2String;
	}

	public void setCleaner2String(String cleaner2String) {
		this.cleaner2String = cleaner2String;
	}

	public String getCleaner3String() {
		return cleaner3String;
	}

	public void setCleaner3String(String cleaner3String) {
		this.cleaner3String = cleaner3String;
	}

	public int getMaxattendees() {
		return maxattendees;
	}

	public void setMaxattendees(int maxattendees) {
		this.maxattendees = maxattendees;
	}

	public boolean isIsmealclosed() {
		return ismealclosed;
	}

	public void setIsmealclosed(boolean ismealclosed) {
		this.ismealclosed = ismealclosed;
	}

	public String getPrintableMenu() {
		return "Menu: " + menu;
	}

	public String getPrintableCooks() {
		String cooks = "none";
		if (cook1String != null && !cook1String.isEmpty()) {
			cooks = cook1String + "(L)";
		}
		if (cook2String != null && !cook2String.isEmpty()) {
			cooks += ", " + cook2String;
		}
		if (cook3String != null && !cook3String.isEmpty()) {
			cooks += ", " + cook3String;
		}
		if (cook4String != null && !cook4String.isEmpty()) {
			cooks += ", " + cook4String;
		}
		return "Cooks: " + cooks;
	}

	public String getPrintableCleaners() {
		String cleaners = "none";
		if (cleaner1String != null && !cleaner1String.isEmpty()) {
			cleaners = cleaner1String + "(L)";
		}
		if (cleaner2String != null && !cleaner2String.isEmpty()) {
			cleaners += ", " + cleaner2String;
		}
		if (cleaner3String != null && !cleaner3String.isEmpty()) {
			cleaners += ", " + cleaner3String;
		}
		return "Cleaners: " + cleaners;
	}

}
