package nz.ac.vuw.ecs.swen225.gp21.domain;

import java.util.*;

import nz.ac.vuw.ecs.swen225.gp21.app.AppWindow;
import nz.ac.vuw.ecs.swen225.gp21.app.Countdown;
import nz.ac.vuw.ecs.swen225.gp21.app.Main;


/**
 * The Board class is responsible for representing and maintaining 
 * the state of the game, such as what type of objects are on the maze,
 * where they are, whether the level is finished and which actions are 
 * allowed to change the state of those objects, etc...
 * 
 * @author Victor Tam
 */
public class Board implements Cloneable {

	/** Current level the user is playing at, this must be set to activate the InfoTile Otherwise an IllegalStateException would be thrown  The currentlevel is set when Persistency calls loadFromXML() which  sets up all the game objects. */
	private int currentLevel =0;
	
	/**
	 * Gets the Current level the user is playing at.
	 *
	 * @return the current level
	 */
	public int getCurrentLevel() {
		return currentLevel;
	}
	
	/**
	 * Sets the Current level number the user is playing at
	 * This is not used anywhere except in DomainTest
	 * Actual level is set by Persistency.
	 *
	 * @param lvl the new current level
	 */
	public void setCurrentLevel(int lvl) {
//		if (lvl==1 || lvl==2) // spotbug says it's a useless condition as it was checked somewhere else already
		this.currentLevel = lvl; // not used
//		else throw new IllegalStateException("The current level must be either 1 or 2");
	}

	/**
	 * Stores tiles currently on board in a 2D array of AbstractTiles.
	 * Stores the most recent version of the board, ie Keys and Doors
	 * that were in original textOutput might have become FreeTiles.
	 * 
	 * Tiles only, Actors(AbstractChap/RegularChap/VariantChap's codes are not included)
	 */
	private AbstractTile[][] tileBoard;

	/**
	 * Getter for tileBoard which only contain AbstractTile objects.
	 *
	 * @return tileBoard
	 */
	public AbstractTile[][] getTileBoard() {
		return tileBoard;
	}

	/**
	 * Made out of single characters, the original input board but it is
	 * not updated, ie Keys being kicked up do not change textBoard's contents.
	 * 
	 * Used for resetting the game
	 */
	private String[] originalTextBoard;


	/** regularChap's current inventory: key(s) that unlock corresponding  coloured doors, a key leaves the inventory once it's been used. */
	private final List <KeyTile> inventoryKT = new ArrayList<>();
	
	/**
	 * Getter for inventory, setter is unnecessary as you simply add and remove from the list
	 * Used During the game.
	 *
	 * @return inventoryKT
	 */
	public List<KeyTile> getInventoryKT() {
		return Collections.unmodifiableList(inventoryKT);
	}

	/**
	 * Sets to the inventoryKT using a list of strings, ONLY used at the beginning of games.
	 *
	 * @param inventoryStr the new inventory
	 */
	public void setInventory(List <String> inventoryStr) {
		this.inventoryKT.addAll(ArrayConvertor.fromStrToKT(inventoryStr));
	}
	
	/**
	 * Converts list of keytiles into a list of strings of their respective codes.
	 *
	 * @return inventoryStr
	 */
	public List<String> getInventoryStr() {
		List <String> inventoryStr = new ArrayList<>();
		for(KeyTile kt: inventoryKT) {
			inventoryStr.add(kt.tileCode());
		}
		return inventoryStr;
	}

	/**
	 * Number of treasure tiles (aka Chips) left on current board, 
	 * ExitLock unlocks when it falls to 0. 
	 */
	private int numTreasureOnBoard;

	/** Controlled by user/player, the hero of the game and its Getter. */
	private RegularChap regularChap;
	
	/**
	 * Gets the regular chap.
	 *
	 * @return the regular chap
	 */
	public RegularChap getRegularChap() {
		return regularChap;
	}

	/** Stores secondary actors/Variants to move around in level 2. */
	private final ArrayList<VariantChap> variants = new ArrayList<>();
	
	/**
	 * Constructor using 1D array.
	 *
	 * @param input the input
	 */
	public Board(String[] input) {
		this.originalTextBoard=input; // save a copy in the field
		this.basicLoad(input);
	}
	
	/**
	 * Basic loading for tiles and actors on the board
	 * Without Countdown clock because that is connected to App
	 * Lets me test the functionality of module without using App.
	 *
	 * @param input the input
	 */
	public void basicLoad(String[] input) {
		regularChap = null;
		inventoryKT.clear();
		numTreasureOnBoard = 0;
		
		tileBoard = new AbstractTile[input.length][input[0].length()];
		boolean madeExitTile = false;
		for (int row = 0; row < input.length; row++) {
			for (int col = 0; col < input[row].length(); col++) {
				char c = input[row].charAt(col);
				if (c == '@') {
					if(this.regularChap!=null) throw new IllegalStateException("There can only be one regularChap in a level/game.");
					regularChap = new RegularChap(new Position(col, row));
					tileBoard[row][col] = new FreeTile(row, col);
				} else if (c == '^' || c == '>' || c == 'v' || c == '<') {
						VariantChap newVar = new VariantChap(new Position(col, row));
						switch(c) {
							case'^':newVar.setDirection(Direction.UP); break;
							case'>':newVar.setDirection(Direction.RIGHT);break;
							case'v':newVar.setDirection(Direction.DOWN);break;
							case'<':newVar.setDirection(Direction.LEFT);break;
							default: throw new IllegalArgumentException("Invalid code for variant: "+c);
						}
						variants.add(newVar);							
						tileBoard[row][col] = new FreeTile(row, col);
				} else {
					tileBoard[row][col] = makeTile(c, row, col);
					if (tileBoard[row][col] instanceof ExitTile) {
						madeExitTile = true;
					}
				}
			}
		}
		assert madeExitTile;
		assert regularChap != null;
	}

	/**
	 * Called to check if regularChap is standing on an InfoTile.
	 * If so, return the corresponding help text, else, return null.
	 *
	 * @return the corresponding help text or null
	 */
	public String checkIfOnInfo() {
		Position pos = regularChap.getPosition();
		AbstractTile t = tileBoard[pos.getY()][pos.getX()];
		if (t instanceof InfoTile) {
			if (currentLevel==1) return "You're at level 1, collect the treasures!";
			else if (currentLevel==2) return "You're at level 2, collect treasures and avoid Variant(s)!";
			else throw new IllegalStateException("checkIfOnInfo() failed: The current level must be either 1 or 2!");
//			return ((InfoTile) t).getHelp(); // text saved in infoTile instance
		}
		return null;
	}

	/**
	 * Make tile based on character representation.
	 *
	 * @param code the AbstractTile to make
	 * @param row y-coordinate of position
	 * @param column x-coordinate of position
	 * @return the AbstractTile made
	 */
	public AbstractTile makeTile(char code, int row, int column) {
		if (code == '_') return new FreeTile(row, column);
		if (code == 'X') return new ExitTile(row, column);
		if (code == '!') return new ExitLockTile(row, column);
		if (code == 'c' || code == 'r'|| code == 'g') return new LockedDoorTile(code, row, column);
		if (code == 'C' || code == 'R'|| code == 'G') return new KeyTile(code, row, column);
		if (code == '?') return new InfoTile(row, column);
		if (code == '#') return new WallTile(row, column);
		if (code == '$') {
			numTreasureOnBoard++;
			return new TreasureTile(row, column);
		}
		if (code == '*') return new ExtraTile(row, column);
		throw new IllegalArgumentException("Invalid Tile Code: "+code);
	}

	/**
	 * For testing in the domain, chap will draw over variants
	 * DOES NOT CONTAIN next line (\n) characters.
	 *
	 * @return a single line line String
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tileBoard.length; i++) {
			NextSquare:
			for (int j = 0; j < tileBoard[i].length; j++) {
				Position p = new Position(j, i);
				if(!variants.isEmpty()) {
					for(VariantChap v: variants) {
						if (v.getPosition().equals(p)) {
							String str = null;
							switch(v.getDirection()) {
							case UP:str="^";break;
							case RIGHT:str=">";break;
							case DOWN:str="v";break;
							case LEFT: str="<";break;
							}
							if (str==null) throw new IllegalArgumentException("Invalid code stored for variants");
							else sb.append(str);
							continue NextSquare; // one variant/actor per square
						}
					}
				}
				if (regularChap.getPosition().equals(p)) sb.append('@');
				else sb.append(tileBoard[i][j].tileCode());
			}
		}
		return sb.toString();
	}
	
	/**
	 * For testing in the domain, chap will draw over variants
	 * CONTAINS next line (\n) characters .
	 *
	 * @return a 2D string you can visualise in the console
	 */
	public String toVisualString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tileBoard.length; i++) {
			NextSquare:
			for (int j = 0; j < tileBoard[i].length; j++) {
				Position p = new Position(j, i);
				if(!variants.isEmpty()) {
					for(VariantChap v: variants) {
						if (v.getPosition().equals(p)) {
							String str = null;
							switch(v.getDirection()) {
							case UP:str="^";break;
							case RIGHT:str=">";break;
							case DOWN:str="v";break;
							case LEFT: str="<";break;
							}
							if (str==null) throw new IllegalArgumentException("Invalid code stored for variants");
							else sb.append(str);
							continue NextSquare; // one variant/actor per square
						}
					}
				}
				if (regularChap.getPosition().equals(p)) sb.append('@');
				else sb.append(tileBoard[i][j].tileCode());
			}
			sb.append('\n');
		}
		return sb.toString();
	}
	/**
	 * Getter for tile in a given Direction from a given Position.
	 *
	 * @param p position of tile the Actor is on
	 * @param d direction to move to
	 * @return position of new tile
	 */
	public AbstractTile getNeighbouringTile(Position p, Direction d) {
		Position newPos = d.movePosition(p);
		// make it more flexible by changing to board lengths
		
		return getTile(newPos);
	}
	
	/**
	 * Getter for tile in a given Position.
	 *
	 * @param p position of tile you want to get
	 * @return the tile wanted
	 */
	public AbstractTile getTile(Position p) {

		if(p.getY()<0 || p.getY()>=tileBoard.length || p.getX()<0 ||p.getX()>=tileBoard[0].length) {
			return null;
		}
		
		return tileBoard[p.getY()][p.getX()];
	}

	/**
	 * Put FreeTile at a given position, ie when the tile if the old tile
	 * has been picked up or unlocked by regularChap.
	 *
	 * @param p the position of tile to be replaced with freetile
	 */
	public void setFreeTile(Position p) {
		if(getTile(p)==null) {
			throw new IllegalArgumentException("Tile does not exist");
		}
		tileBoard[p.getY()][p.getX()] = new FreeTile(p.getY(), p.getX());
	}

	/**
	 * Check if the inventory contains a key of a given colour.
	 *
	 * @param c KeyDoorColour
	 * @return true if there is a key of given colour
	 */
	public boolean containsKey(KeyDoorColour c) {
		for(KeyTile kt: inventoryKT) {
			if(kt.getColour().equals(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove a key of the given colour from inventory, this is called
	 * once a key of this colour has  been used to unlock a door tile.
	 *
	 * @param c KeyDoorColour
	 */
	public void removeKey(KeyDoorColour c) {
		int removalIndex=-1;
		for(int i=0; i<inventoryKT.size(); i++) {
			if(inventoryKT.get(i).getColour().equals(c)) {
				removalIndex=i;
			}
		}
		if (removalIndex>-1) {
			inventoryKT.remove(removalIndex);
		} else {
			throw new IllegalArgumentException("Attempt to remove a "+c.toString()+
				" key was unsuccesful as the inventory does not contain it");
		}
	}

	/**
	 * Unlock ExitLock(s) on current Board, only called when no treasures/chips left on board.
	 */
	public void unlockExit() {
		if(numTreasureOnBoard != 0) {
			throw new IllegalArgumentException("There are still "+numTreasureOnBoard+" treasures left uncollected");
		}
		for (AbstractTile[] row : tileBoard) {
			for (AbstractTile t : row) {
				if (t instanceof ExitLockTile) {
					((ExitLockTile) t).unlock();
				}
			}
		}
	}

	/**
	 * Move regularChap in a given direction, renderer add sound here?.
	 *
	 * @param d given direction
	 */
	public void moveRegularChap(Direction d) {
		try {
			AbstractTile t = getNeighbouringTile(regularChap.getPosition(), d);
			if (t == null) {
				regularChap.setDirection(d);
			} else if (t.validTile(this)) {
				regularChap.move(t.getPosition(), d, clone());
				if (t.canBecomeFreeTile()) {
					if (t instanceof KeyTile) {
						inventoryKT.add((KeyTile) t);
					} else if (t instanceof TreasureTile) {
						numTreasureOnBoard--;
						assert numTreasureOnBoard > -1; // Remaining Treasure cannot be lower than 0
					} else if (t instanceof LockedDoorTile) {
						KeyDoorColour c = ((LockedDoorTile) t).getColour();
						removeKey(c);
					} else if (t instanceof ExtraTile) {
						if(AppWindow.gamePane == null) {
							throw new IllegalStateException("ExtraTile only works if the App thus "
									+ "GamPane amd clock are set up");
						}
						Countdown.setExtraTime(10); //connected to app, cannot be tested domaintest
						AppWindow.gamePane.updateTime(Main.g);
					}
					setFreeTile(regularChap.getPosition());
				}
				if (numTreasureOnBoard == 0) {
					unlockExit();
					// unlock sound
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check if the player has landed on the ExitTile, if so, the level is finished.
	 *
	 * @return true if the player is on exit thus the level is finished
	 */
	public boolean checkIfOnExit() {
		for (AbstractTile[] currentRow : tileBoard) {
			for (AbstractTile currentTile : currentRow) {
				if (currentTile instanceof ExitTile) {
					ExitTile e = (ExitTile) currentTile;
					if (e.getPosition().equals(regularChap.getPosition())) return true;
				}
			}
		}
		return false;
	}

	/**
	 * Add a new Variant/Secondary Actor to the board.
	 *
	 * @param aa the aa
	 */
	public void addVariant(AbstractChap aa) {
		if(aa instanceof RegularChap) {
			throw new IllegalArgumentException("Variant cannot be regularChap");
		}
		variants.add((VariantChap)aa);
	} 

	/**
	 * regularChap is dead if he overlaps/shares a position with any Variant.
	 *
	 * @return true if chap is alive
	 */
	public boolean chapIsAlive() {		
		for (AbstractChap v: variants) {
			if (v.getPosition().equals(regularChap.getPosition())) {
				// check if regularChap has an extraLife/immunity to variants
//				if (extraLife) {
//					extraLife = false;
//					return true;
//				} else 
					return false;
			}
		}
		return true;
	}

	
	/**
	 * Returns the number of items that match the given class
	 * Useless as I had decided to just make it store KeyTiles.
	 *
	 * @return count of items belonging to class passed in the param
	 */
//	public int countTypes(Class<? extends AbstractTile> type) {
//		int total = 0;
//		for (AbstractTile t : keysPickedUp) {
//			if (t.getClass().equals(type)) total++;
//		}
//		return total;
//	}
		
	/**
	 * Below methods have nothing to do with game logic,
	 * They are simply getter methods and conversion methods to make
	 * interactions between modules easier.
	 */
	
	/**
	 * For PERSISTENCY to be saved in a XML file
	 * 
	 * Getter for the amount of treasures left to obtain on board.
	 *
	 * @return the 2D array of tiles converted into 1D array of String
	 */
	public String[] getTextBoardToRestart() {
		return originalTextBoard;
	}
	
	/**
	 * For PERSISTENCY and APP to be saved in a XML file
	 * 
	 * Getter for the amount of treasures left to obtain on board.
	 * Kind of useless as you can simply iterate through tileBoard
	 * to count the treasure tile, but it is convenient, and can be
	 * used to double check the game logic
	 *
	 * @return number treasures left
	 */
	public int getNumTreasures() {
		return numTreasureOnBoard;
	}
	
	/**
	 * Getter for regularChap's Position.
	 *
	 * @return position
	 */
	public Position getRegularChapPos() {
		return regularChap.getPosition();
	}

	/**
	 *  
	 * Getter for regularChap's Direction.
	 *
	 * @return direction
	 */
	public Direction getRegularChapDir() {
		return regularChap.getDirection();
	}

	/** 
	 * @return list of Variants
	 */
	public List<VariantChap> getVariants() {
		return variants;
	}
		
	/**
	 * Called by PERSISTENCY module to load a level from a XML file, 
	 * which is the result of user clicking "load game" in APP.
	 *
	 * @param clock time remaining
	 * @param boardObjests in String 2D array form
	 * @param savedKeys inventory in String List form
	 * @param chapDir direction chap is facing
	 * @param treasureNum to confirm treasure is counted correctly in basicLoad
	 * @param lvl current level number
	 */
	public void loadFromXML(int clock, String[][] boardObjests, List<String> savedKeys,
			Direction chapDir, int treasureNum, int lvl) {
		if(Main.g!=null)// added condition to make testing easier
		Main.g.setTimeLeft(clock);
		
		this.numTreasureOnBoard=0; // reset to 0
		basicLoad(ArrayConvertor.from2dTo1d(boardObjests)); // this will add treasure count in field
		// -1 means the number of treasure is not saved on the XML file
		if (treasureNum>-1) assert this.numTreasureOnBoard==treasureNum;
		
		if(chapDir!=null)
		regularChap.setDirection(chapDir);
		
		inventoryKT.clear();
		setInventory(savedKeys);
		
		assert lvl>0;
		this.currentLevel=lvl;

	}
	
	/**
	 * Called by PERSISTENCY when user saves the game, a separate class is created to
	 * reduce coupling between modules, so only one method is needed.
	 * The first 3 arguments are important, last 3 are not and are expendable, the 
	 * game would run the same if they were to be left null
	 * 
	 * @return BoardObjectsToSave instance
	 */
	public BoardObjectsToSave saveToXML() {	
		return new BoardObjectsToSave( Main.g.getTimeLeft(), getCurrentBoard(),
				getInventoryStr(), getRegularChapDir(), numTreasureOnBoard,currentLevel);
	}
	
	/**
	 * Stores all codes of the current game objects currently on board in
	 * a 2D String array. Includes Actors and their clouds, my game logic dictates
	 * that all AbstractChaps stand on FreeTiles thus saving the information regarding
	 * what tile the respective AbstractChaps stand on can be skipped.
	 * 
	 * @return String 2D array that reflects the current positions of all tiles and actors.
	 */
	public String[][] getCurrentBoard() {
		int iLength = tileBoard.length;
		int jLength = tileBoard[0].length;
		String[][] strTileBoard = new String[iLength][jLength];
		for(int i =0; i<iLength; i++) {
			for(int j =0; j<jLength; j++) {
				strTileBoard[i][j]=tileBoard[i][j].tileCode();
			}
		}
		
		// ADD RegularChap's position to board here
		int chapY = getRegularChapPos().getY();		
		int chapX = getRegularChapPos().getX();
//		if (!(strTileBoard[chapY][chapX].equals("_") ||strTileBoard[chapY][chapX].equals("X"))) {
//			throw new IllegalStateException("regularChap's code must be replacing a FreeTile or ExitTile!");
//		}
		strTileBoard[chapY][chapX] = "@";

		// ADD Variant(s)'s position to board here
		for (AbstractChap v: variants) {
			if (v instanceof RegularChap) throw new IllegalStateException("regularChap cannot be a variant!");
			int varY = v.getPosition().getY();
			int varX = v.getPosition().getX();
			Direction varD = v.getDirection();
			String str = null;
			switch(varD) {
			case UP:str="^";break;
			case RIGHT:str=">";break;
			case DOWN:str="v";break;
			case LEFT: str="<";break;
			}
			if (str==null) throw new IllegalArgumentException("Invalid code stored for variants");
//			if (!strTileBoard[varY][varX].equals("_")) {
//				throw new IllegalStateException("variant's code must be replacing a FreeTile!");
//			}
			strTileBoard[varY][varX] = str;
		}
		return strTileBoard;
	}

	/**
	 * Boolean to indicate whether player has immunity against variants,
	 * commented out because I decided to add time instead
	 * 
	 * Useless as I had decided to make ExtraTile give chap more time on 
	 * the clock instead.
	 *
	 * @return the board
	 */
//	private boolean extraLife = false;
//	
//	public boolean hasExtraLife() {
//		return extraLife;
//	}
	
	@Override
	/**
	 * Returns a clone of the ORIGINAL board
	 */
	public Board clone() {
		Board c = new Board(ArrayConvertor.from2dTo1d(getCurrentBoard()));
		return c;
	}
}
