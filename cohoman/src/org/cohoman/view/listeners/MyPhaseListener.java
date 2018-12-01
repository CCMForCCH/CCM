package org.cohoman.view.listeners;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class MyPhaseListener implements PhaseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1715521876865868021L;

	public void beforePhase(PhaseEvent pe) {
		//System.out.println("before - " + pe.getPhaseId().toString());
	}
	
	public void afterPhase(PhaseEvent pe) {
		//System.out.println("after - " + pe.getPhaseId().toString());
	}
	
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
}
