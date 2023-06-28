package nz.ac.vuw.ecs.swen225.gp21.domain;

/**
 * Superclass of Tiles that make up the maze.
 *
 * @author Victor Tam
 */
public abstract class  AbstractTile {

	/** The position. */
	protected Position position; // cannot be made final because the constructor is not in this class

	/**
	 * Check if tile can be moved onto in the current BoardMaze.
	 *
	 * @param m current BoardMaze instance
	 * @return true if actor can move onto tile
	 */
	public abstract boolean validTile(Board m);

	/**
	 * Check if the tile can be a FreeTile later in the game.
	 *
	 * @return true if player/regularChap's action can clear it
	 */
	public abstract boolean canBecomeFreeTile();

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Position getPosition(){
		return position;
	}

	/**
	 * Getter for the tile's code.
	 *
	 * @return tile's code
	 */
	public abstract String tileCode();
}
