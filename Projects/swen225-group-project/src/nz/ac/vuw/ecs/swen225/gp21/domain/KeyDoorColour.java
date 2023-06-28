package nz.ac.vuw.ecs.swen225.gp21.domain;

/**
 * Enumerates KeyTile and LockedDoorTile's colours.
 *
 * @author Victor Tam
 */
public enum KeyDoorColour {
	
	/** Cyan key's code is C, Cyan locked door's code is c Green key's code is G, Green locked door's code is g Red key's code is R, Red locked door's code is r. */
	CYAN, 
 /** The green. */
 GREEN, 
 /** The red. */
 RED;
	

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return name();
	}
}
