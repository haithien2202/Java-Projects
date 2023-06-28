package nz.ac.vuw.ecs.swen225.gp21.domain;


/**
 * If Chap steps onto this tile, the treasure (chip) is picked up
 * and added to the treasure chest. Then the tile turns into a free tile.
 * No need to be stored in inventory as the Treasure remaining field is 
 * used to keep track
 *
 * @author Victor Tam
 */
public class TreasureTile extends AbstractTile {

	/**
	 * Constructor for a TreasureTile.
	 *
	 * @param row y-coordinate of position
	 * @param col x-coordinate of position
	 */
	public TreasureTile(int row, int col) {
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
		return "$";
	}
}
