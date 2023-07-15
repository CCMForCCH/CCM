package org.cohoman.view.controller.utils;

public enum VendorEnums {
	
	ACCUTEMPENGINEERING("AccuTemp Engineering"),
	ACPFIRE("ACP Fire and Security Systems"),
	ARBORWAYTREE("Arborway Tree Care"),
	ATLANTIC("Atlantic Restoration"),
	BARRETTTREESERVICE("Barrett Tree Service East"),
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
	FIREEXTINGUISHER("Fire Extinguisher Service Co. Inc."),
	GEMPLUMBING("GEM Plumbing and Heating Services"),
	GORHAM("Gorham Fire Appliance Co."),
	HIGHMARK("HighMark Land Design"),
	HRRAMIREZ("HR Ramirez Contractors Inc."),
	JOHNHENRYROOFING("John Henry Roofing, Inc."),
	JOHNSSEWER("John's Sewer & Pipe Cleaning, Inc."),
	JUNKLUGGERS("Junkluggers of Greater Boaton"),
	JWCPLUMBING("JWC Plumbing and Heating"),
	MAMMOTH("Mammoth Fire Alarm"),
	MIDDLEOAK("MiddleOak"),
	MIGHTYDUCT("Mighty Duct"),
	MINASIAN("Minasian Becker LLC"),
	NEWENGLANDSTAIRLIFTS("New England Stairlifts"),
	OTIS("Otis Elevator"),
	PAIVA("Paiva Masonry and Landscaping, Inc."),
	PEOPLESLOCK("Peoples Lock"),
	REALPRO("RealPro Mechanical"),
	ROSETTI("Rosetti Services Corp."),
	SAS("Specialty Answering Service, Inc."),
	SGH("Simpson Gumpertz & Heger"),
	SKILLINGS("Skillings & Sons LLC"),
	SOURCE("Source Equipment Company, Inc"),
	SPS("Schernecker Property Services"),
	SUNBUG("SunBug Solar"),
	METRO("The Metro Group, Inc"),
	TOBYS("Toby Stein's Piano Service"),
	TYLERLYNCH("Tyler Lynch, P. C.");
	
	
	
    private final String text;

    private VendorEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
