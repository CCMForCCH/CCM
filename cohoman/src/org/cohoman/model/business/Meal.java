package org.cohoman.model.business;

import java.util.List;

public class Meal {

		private String eventDate;
		private String menu;
		private List<String> cooks;
		private List<String> cleaners;
		
		public String getEventDate() {
			return eventDate;
		}
		public void setEventDate(String eventDate) {
			this.eventDate = eventDate;
		}
		public String getMenu() {
			return menu;
		}
		public void setMenu(String menu) {
			this.menu = menu;
		}
		public List<String> getCooks() {
			return cooks;
		}
		public void setCooks(List<String> cooks) {
			this.cooks = cooks;
		}
		public List<String> getCleaners() {
			return cleaners;
		}
		public void setCleaners(List<String> cleaners) {
			this.cleaners = cleaners;
		}
		
}
