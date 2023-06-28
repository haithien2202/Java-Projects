package nz.ac.vuw.ecs.swen225.gp21.domain;

/**
 * Invented tile that gives (Regular)Chap extra 10 seconds on the
 * countdown clock if he reaches it. Becomes FreeTile afterwards.
 *
 * @author Victor Tam
 */
public class ExtraTile extends AbstractTile{

	/**
	 * Constructor for an ExtraLifeTile, invented tile that 
	 * gives regularChap either extra life or extra time.
	 *
	 * @param row y-coordinate of position
	 * @param col x-coordinate of position
	 */
	public ExtraTile(int row, int col) {
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
		return "*";
	}

}
