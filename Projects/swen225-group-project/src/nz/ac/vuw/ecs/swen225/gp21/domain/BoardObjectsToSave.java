package nz.ac.vuw.ecs.swen225.gp21.domain;

//import java.util.ArrayList;
import java.util.List;

/**
 * For PERSISTENCY to use when saving game
 * To avoid calling multiple methods in Board from the Persistency module.
 *
 * @author Victor Tam
 */
public class BoardObjectsToSave {
	
	/** The countdown. */
	private int countdown;
	
	/** The treasure num. */
	private int treasureNum;
	
	/** The board tiles. */
	private String[][] boardTiles;
	
	/** The inventory. */
	private List<String> inventory;
	
	/** The regular chap dir. */
	private Direction regularChapDir;
	
	/** The current level. */
	private int currentLevel;


	/**
	 * First three arguments are crucial, last three are expendable and can be replaced with null.
	 *
	 * @param countdown the countdown
	 * @param boardObjects the board objects
	 * @param keysPickedUp the keys picked up
	 * @param chapDir the chap dir
	 * @param treasureNum the treasure num
	 * @param lvl current Level
	 */

	public BoardObjectsToSave(int countdown, String[][] boardObjects, List<String> keysPickedUp,
			Direction chapDir, int treasureNum, int lvl) {
		super();
//		if(countdown <= 0) { // commented out because of App
//			throw new IllegalArgumentException("Countdown clock must have at least 1 second remaining.");
//		}
		if(boardObjects==null) {
			throw new IllegalArgumentException("BoardTiles cannot be null, the board must have at least 4 tiles");
		}
		if(keysPickedUp==null) {
			throw new IllegalArgumentException("Inventory cannot be null, if theres nothing in it, pass an empty list.");
		}
		this.countdown = countdown;
		this.treasureNum = treasureNum;
		this.boardTiles = boardObjects;
		this.inventory = keysPickedUp;
		this.regularChapDir = chapDir;

		if(lvl<1) throw new IllegalArgumentException("lvl/level cannot be lower than 1");
		this.currentLevel=lvl;
	}
	
	/**
	 * Getter methods.
	 *
	 * @return the countdown
	 */
	public int getCountdown() {
		return countdown;
	}
	
	/**
	 * Gets the treasure num.
	 *
	 * @return the treasure num
	 */
	public int getTreasureNum() {
		return treasureNum;
	}
	
	/**
	 * Gets the board tiles.
	 *
	 * @return the board tiles
	 */
	public String[][] getBoardTiles() {
		return boardTiles;
	}
	
	/**
	 * Gets the keys picked up.
	 *
	 * @return the keys picked up
	 */
	public List<String> getKeysPickedUp() {
		return inventory;
	}
	
	/**
	 * Gets the chap dir.
	 *
	 * @return the chap dir
	 */
	public Direction getChapDir() {
		return regularChapDir;
	}
	
	/**
	 * Gets the current level.
	 *
	 * @return the current level
	 */
	public int getCurrentLevel() {
		return currentLevel;
	}
}
