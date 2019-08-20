package org.cohoman.model.business.trash;

public enum TrashRolesEnums {
	
	CHOOSE("Choose the role for your Trash duties"), ORGANIZER("Organizer"), STRONGPERSON("StrongPerson"), TEAMMEMBER("TeamMember"), NOROLE("NoRole");
	
    private final String text;

    private TrashRolesEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
