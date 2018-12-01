package org.cohoman.view.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

//@Named
@ManagedBean
@SessionScoped
public class TableData implements Serializable {

		private static final Name[] names = new Name[] {
			new Name("1", "2"),
			new Name("3", "4"),
			new Name("5", "6")
		};
		
		private static final Name[] times = new Name[] {
			new Name("7:30", "9:30"),
			new Name("10:30", "11:30")
		};
		
	public Name[] getNames() {
		return names;
	}
	
	public Name[] getTimes() {
		return times;
	}
}
