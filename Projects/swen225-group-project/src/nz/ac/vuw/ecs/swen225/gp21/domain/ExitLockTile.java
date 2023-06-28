package nz.ac.vuw.ecs.swen225.gp21.domain;

/**
 * The Exit Lock tile blocks the sole entrance into the ExitTile
 * at the beginning of each level. It behaves like a wall for Chap
 * as long as there are still uncollected treasures. Once the treasure 
 * chest is full (all treasures have been collected), Chap can pass through.
 *
 * @author Victor Tam
 */
public class ExitLockTile extends AbstractTile {
	
	/** Stores whether or not we can go through, locked by default. */
	private boolean isLocked = true;

	/**
	 * Constructor for an ExitLockTile.
	 *
	 * @param row y-coordinate of position
	 * @param col x-coordinate of position
	 */
	public ExitLockTile(int row, int col) {
		position = new Position(col, row);
	}

	/**
	 * Unlock exit tile (called if all treasures are picked up). It cannot be locked back up afterwards.
	 */
	public void unlock() {
		isLocked = false;
	}

	/**
	 * It is a valid tile if unlocked and vice versa.
	 *
	 * @param m the m
	 * @return true, if successful
	 */
	@Override
	public boolean validTile(Board m) {
		return !isLocked;
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
		return "!";
	}
}
