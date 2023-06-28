package nz.ac.vuw.ecs.swen225.gp21.domain;

/**
 * Part of a wall, actors can never move onto these tiles.
 *
 * @author Victor Tam
 */
public class WallTile extends AbstractTile {
	
	/**
	 * Constructor for a WallTile.
	 *
	 * @param row y-coordinate of position
	 * @param col x-coordinate of position
	 */
	public WallTile(int row, int col) {
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
		return false;
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
		return "#";
	}
}
