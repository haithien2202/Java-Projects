package nz.ac.vuw.ecs.swen225.gp21.domain;

/**
 * Actors(Regular and Variant) can freely move onto these tiles and nothing would happen.
 *
 * @author Victor Tam
 */
public class FreeTile extends AbstractTile {

	/**
	 * Constructor for a FreeTile.
	 *
	 * @param row y-coordinate of position
	 * @param col x-coordinate of position
	 */
	public FreeTile(int row, int col) {
		position = new Position(col, row);
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
	public boolean canBecomeFreeTile() { // it is a FreeTile already
		return false;
	}

	/**
	 * Tile code.
	 *
	 * @return the string
	 */
	@Override
	public String tileCode() {
		return "_";
	}
}
