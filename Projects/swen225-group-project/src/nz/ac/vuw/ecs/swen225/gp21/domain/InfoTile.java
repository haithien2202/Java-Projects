package nz.ac.vuw.ecs.swen225.gp21.domain;

/**
 * Like a free tile, but when Chap steps on this field, a help text will be displayed.
 * There are two ways to get the text string, it's up to App which one to use
 *
 * @author Victor Tam
 */
public class InfoTile extends AbstractTile {

	/**
	 * Constructor for an InfoTile.
	 *
	 * @param row y-coordinate of position
	 * @param col x-coordinate of position
	 */
	public InfoTile(int row, int col) {
		position = new Position(col, row);
	}


	/** Help text to display, alternative way to get text but is NOT USED eventually Only used in DomainTest, this is not dependant on which level the board is on unlike the alternative way to show text. */
	private final String helpText = "Use arrows or wasd to move Chap, avoid Variants!";
	
	/**
	 * Getter for helpText.
	 *
	 * @return help text for display
	 */
	public String getHelp() {
		return this.helpText;
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
		return "?";
	}
}
