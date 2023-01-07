package org.cohoman.view.controller.utils;

public enum VendorEnums {
	
	ACPFIRE("ACP Fire and Security Systems"),
	ARBORWAYTREE("Arborway Tree Care"),
	ATLANTIC("Atlantic Restoration"),
	BLUEHILLS("Blue Hills Maintenance"),
	BOSTONPIANO("Boston Piano Service"),
	BROWNANDBROWN("Brown & Brown of Massachusetts, LLC"),
	CENTRALARM("Centralarm Monitoring, Inc"), 
	CITYDPW("City of Cambridge Dept of Public Works"),
	CITYWATER("City of Cambridge Water Department"),
	CITYPUMP("City Pump & Motor Service Inc."),
	COLLINSDOOR("Collins Overhead Door"),
	COMMONWEALTHGLASS("Commonwealth Glass"),
	CRITTER("Critter Control"),
	DRAINDOCTOR("Drain Doctor"),
	DRANE("Drane Engineering"),
	ELGE("Elge Plumbing & Heating Co."),
	GOTHAM("Gotham Fire Appliance Co."),
	JOHNSSEWER("John's Sewer & Pipe Cleaning, Inc."),
	JWCPLUMBING("JWC Plumbing and Heating"),
	MAMMOTH("Mammoth Fire Alarm"),
	MIDDLEOAK("MiddleOak"),
	MIGHTYDUCT("Mighty Duct"),
	MINASIAN("Minasian Becker LLC"),
	OTIS("Otis Elevator"),
	PAIVA("Paiva Masonry and Landscaping, Inc."),
	PEOPLESLOCK("Peoples Lock"),
	REALPRO("RealPro Mechanical"),
	ROSETTI("Rosetti Services Corp."),
	SPS("Schernecker Property Services"),
	SEGALL("Segall Electric"),
	SKILLINGS("Skillings & Sons LLC"),
	SOURCE("Source Equipment Company, Inc"),
	SUNBUG("SunBug Solar"),
	METRO("The Metro Group, Inc"),
	TOBYS("Toby Stein's Piano Service");
	
	
	
    private final String text;

    private VendorEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
