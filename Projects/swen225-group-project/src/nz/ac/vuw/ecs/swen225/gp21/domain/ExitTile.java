package nz.ac.vuw.ecs.swen225.gp21.domain;

/**
 * Once (Regular)Chap reaches this tile, the current game level is won.
 *
 * @author Victor Tam
 */
public class ExitTile extends AbstractTile {

	/**
	 * Constructor for an ExitTile.
	 *
	 * @param row y-coordinate of position
	 * @param col x-coordinate of position
	 */
	public ExitTile(int row, int col) {
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
		return false;
	}

	/**
	 * Tile code.
	 *
	 * @return the string
	 */
	@Override
	public String tileCode() {
		return "X";
	}
}
