package nz.ac.vuw.ecs.swen225.gp21.domain;

/**
 * Actors can move onto those tiles. If Chap moves onto
 * such a tile, he picks up the key with this colour, 
 * once this is done, the tile turns into a free tile.
 *
 * @author Victor Tam
 */
public class KeyTile extends AbstractTile {
	
	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colour == null) ? 0 : colour.hashCode());
		return result;
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyTile other = (KeyTile) obj;
		if (colour != other.colour)
			return false;
		return true;
	}


	/** Stores the colour of the key. */
	private final KeyDoorColour colour;

	/**
	 * Constructor for a KeyTile with a colour represented by a char.
	 *
	 * @param colo character representation of key's colour
	 * @param row y-coordinate of position
	 * @param column x-coordinate of position
	 */
	public KeyTile(char colo, int row, int column) {
		int i = 0;
		switch (colo) {
		case 'C': i = 0; break;
		case 'G': i = 1; break;
		case 'R': i = 2; break;
		}
		colour = KeyDoorColour.values()[i];
		position = new Position(column, row);
	}

	/**
	 * Valid tile.
	 *
	 * @param m the m
	 * @return true, if successful
	 */
	@Override
	public boolean validTile(Board m) {
		return true;
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
	 * Retrieve colour of this key.
	 *
	 * @return this key's colour
	 */
	public KeyDoorColour getColour() {
		return colour;
	}


	/**
	 * Tile code.
	 *
	 * @return the string
	 */
	@Override
	public String tileCode() {
		char c = 0;
		if(colour==KeyDoorColour.CYAN) c= 'C';
		else if(colour==KeyDoorColour.RED) c= 'R';
		else if(colour==KeyDoorColour.GREEN) c= 'G';
		
		if (c==0) throw new IllegalArgumentException("Invalid code for key");
		return Character.toString(c);
	}
}
