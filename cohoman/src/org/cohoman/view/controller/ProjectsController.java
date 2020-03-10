package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.cohoman.model.integration.persistence.beans.ProjectDocBean;

@ManagedBean
@SessionScoped
public class ProjectsController implements Serializable {

	private static final long serialVersionUID = 4678206276499587830L;


	public List<ProjectDocBean> getProjectDocListView() {
		
		List<ProjectDocBean> projectList = new ArrayList<ProjectDocBean>();
		
		ProjectDocBean projectDocBean = new ProjectDocBean();
		projectDocBean.setDocId(1L);
		projectDocBean.setDocName("CCH Project Overview Template"); 
		projectDocBean.setDocDesc("Template for use in starting a new project document");
		projectDocBean.setDocDate(Calendar.getInstance().getTime());
		projectDocBean.setDocOwner("bill");
		projectDocBean.setDocLink("https://docs.google.com/document/d/15doch9TXW2YI4pzaH6o7F6EkMe0iUOfA/view?usp=sharing");
		projectList.add(projectDocBean);

		ProjectDocBean projectDocBean2 = new ProjectDocBean();
		projectDocBean2.setDocId(2L);
		projectDocBean2.setDocName("Drainage and WE Patio Project"); 
		projectDocBean2.setDocDesc("Project document for replacement of WE patio and associated drainage work");
		projectDocBean2.setDocDate(Calendar.getInstance().getTime());
		projectDocBean2.setDocOwner("bill");
		projectDocBean2.setDocLink("https://docs.google.com/document/d/1UyfSbzei11g-AdMOwtlPXj5yer2nxMzg9L7QA_slH8M/view?usp=sharing");
		projectList.add(projectDocBean2);
		
		return projectList;

	}
	
	public List<ProjectDocBean> getProjectDocListEdit() {
		
		List<ProjectDocBean> projectList = new ArrayList<ProjectDocBean>();
		
		ProjectDocBean projectDocBean = new ProjectDocBean();
		projectDocBean.setDocId(1L);
		projectDocBean.setDocName("CCH Project Template"); 
		projectDocBean.setDocDesc("Template for use in starting a new project document");
		projectDocBean.setDocDate(Calendar.getInstance().getTime());
		projectDocBean.setDocOwner("bill");
		projectDocBean.setDocLink("https://docs.google.com/document/d/15doch9TXW2YI4pzaH6o7F6EkMe0iUOfA/edit?usp=sharing");
		projectList.add(projectDocBean);

		ProjectDocBean projectDocBean2 = new ProjectDocBean();
		projectDocBean2.setDocId(2L);
		projectDocBean2.setDocName("Drainage and WE Patio Project"); 
		projectDocBean2.setDocDesc("Project document for replacement of WE patio and associated drainage work");
		projectDocBean2.setDocDate(Calendar.getInstance().getTime());
		projectDocBean2.setDocOwner("bill");
		projectDocBean2.setDocLink("https://docs.google.com/document/d/1UyfSbzei11g-AdMOwtlPXj5yer2nxMzg9L7QA_slH8M/edit?usp=sharing");
		projectList.add(projectDocBean2);
		
		return projectList;

	}
	
}
