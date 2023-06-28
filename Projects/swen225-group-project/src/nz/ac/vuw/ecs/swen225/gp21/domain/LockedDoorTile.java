package nz.ac.vuw.ecs.swen225.gp21.domain;

/**
 * A Locked Door Tile:
 * Chap can only move onto these tiles if they have the
 * key with the matching colour -- this unlocks the tile.
 * After unlocking, the locked tile turns into a free tile.
 *
 * @author Victor Tam
 */
public class LockedDoorTile extends AbstractTile {
		
	/** Stores the kind/colour of key that will unlock the door. */
	private final KeyDoorColour colour;

	/**
	 * Constructor of a LockedTile with a colour represented by a character.
	 *
	 * @param colo character representation of the Locked door's colour
	 * @param row y-coordinate of position
	 * @param column x-coordinate of position
	 */
	public LockedDoorTile(char colo, int row, int column) {
		int i = 0;
		switch (colo) {
		case 'c': i = 0; break;
		case 'g': i = 1; break;
		case 'r': i = 2; break;
		}
		colour = KeyDoorColour.values()[i];
		position = new Position(column, row);
	}

	/**
	 * Getter for door's colour.
	 *
	 * @return door's colour
	 */
	public KeyDoorColour getColour() {
		return colour;
	}

	/**
	 * Movable only if player possesses key with matching colour.
	 *
	 * @param m the m
	 * @return true if player has matching key
	 */
	@Override
	public boolean validTile(Board m) {
		return m.containsKey(colour);
	}

	/**
	 * Can become free tile.
	 *
	 * @return true, if successful
	 */
	@Override
	public boolean canBecomeFreeTile() {
		return true;
	}


	/**
	 * Tile code.
	 *
	 * @return the string
	 */
	@Override
	public String tileCode() {
		char c = 0;
		if(colour==KeyDoorColour.CYAN) c= 'c';
		else if(colour==KeyDoorColour.RED) c= 'r';
		else if(colour==KeyDoorColour.GREEN) c= 'g';
		
		if (c==0) throw new IllegalArgumentException("Invalid code for door");
		return Character.toString(c);
	}
}
