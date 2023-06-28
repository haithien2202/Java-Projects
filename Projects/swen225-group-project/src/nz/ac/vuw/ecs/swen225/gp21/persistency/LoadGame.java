package nz.ac.vuw.ecs.swen225.gp21.persistency;

import nz.ac.vuw.ecs.swen225.gp21.app.Main;
import nz.ac.vuw.ecs.swen225.gp21.domain.*;
import java.io.*;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

// TODO: Auto-generated Javadoc
/**
 * An object used to load a saved game.
 * @author markj
 *
 */
public class LoadGame {

	/**
	 * The filepath of the saved game to load.
	 */
	private String filepath;

	/**
	 * The document object created from the saved game file.
	 */
	private Document document;

	/**
	 * The time remaining in the game.
	 */
	private int time;

	/**
	 * The number of treasures remaining on the board.
	 */
	private int treasures;

	/**
	 * The player's current inventory.
	 */
	private List<String> inventory;

	/**
	 * An array of tiles represented by Strings.
	 */
	private String[][] tiles;

	/**
	 * The level number.
	 */
	private int levelNumber;
	
	
	/**
	 * The direction of Chap.
	 */
	private Direction direction;
	


	/**
	 * Constructor for LoadGame.
	 *
	 * @param filepath the filepath of the file to be loaded.
	 * @throws DocumentException if a Document object is not able to be created.
	 */
	public LoadGame(String filepath) throws DocumentException {
		this.filepath = filepath;
		this.document = loadFile(this.filepath);
		this.levelNumber = loadLevelNumber(this.document);
		this.time = loadTime(this.document);
		this.treasures = loadTreasures(this.document);
		this.tiles = loadBoard(this.document);
		this.inventory = loadInventory(this.document);
		this.direction = loadChapDirection(this.document);
		Game g = Main.g;
		String[] singleTiles = ArrayConvertor.from2dTo1d(tiles);
		Board b = new Board(singleTiles);
		g.setBoard(b);
		g.getBoard().loadFromXML(this.time, this.tiles, this.inventory, this.direction, this.treasures, this.levelNumber);
	}
	
	

	/**
	 * A document object representing the loaded saved game.
	 * @param filepath the filepath of the saved game to be loaded.
	 * @return a document object representing the saved game.
	 * @throws DocumentException if a Document object is not able to be created.
	 */
	protected static Document loadFile(String filepath) throws DocumentException {
		File inputFile = new File(filepath);
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(inputFile);
		return document;
	}
	
	/**
	 * Loads the current level number from the saved game file.
	 * @param document the document object representing the saved game.
	 * @return the current level number.
	 */
	protected static int loadLevelNumber(Document document) {
		Node node = document.selectSingleNode("/level");
		Element element = (Element) node;
	    String levelNumber = element.attributeValue("id");
		int levelNum = Integer.parseInt(levelNumber);
		return levelNum;
	}

	/**
	 * Loads the time remaining from the saved game file.
	 * @param document the document object representing the saved game.
	 * @return the time remaining.
	 */
	protected static int loadTime(Document document) {
		int time = 0;
		Node timeNode = document.selectSingleNode("/level/time");
		time = Integer.parseInt(timeNode.getText());
		return time;
	}

	/**
	 * Loads the number of remaining treasures on the board.
	 * @param document the document object representing the saved game.
	 * @return the number of remaining treasures.
	 */
	protected static int loadTreasures(Document document) {
		int treasures = 0;
		Node treasuresNode = document.selectSingleNode("/level/treasures");
		treasures = Integer.parseInt(treasuresNode.getText());
		return treasures;
	}
	
	/**
	 * Loads the direction Chap is facing.
	 * @param document the document object representing the saved game.
	 * @return the direction Chap is facing.
	 */
	protected static Direction loadChapDirection(Document document) {
		String dir = "";
		Direction direction = null;
		Node node = document.selectSingleNode("/level/board//row/chap"); 
		dir = node.getText();
		switch(dir) {
		case "up":
			direction = Direction.UP;
			break;
		case "down":
			direction = Direction.DOWN;
			break;
		case "left":
			direction = Direction.LEFT;
			break;
		case "right":
			direction = Direction.RIGHT;
			break;
		}

		return direction;
	}
	
	

	/**
	 * Loads the board of the saved game.
	 * @param document the document object representing the saved game.
	 * @return a 2D array representing the board.
	 * @throws DocumentException if a Document object is not able to be created.
	 */
	protected static String[][] loadBoard(Document document) throws DocumentException {
		ArrayList<ArrayList<String>> tilesList = new ArrayList<ArrayList<String>>();
		Element root = document.getRootElement();

		for (Iterator<Element> i = root.elementIterator("board"); i.hasNext();) {
			Element board = (Element) i.next();
			for (Iterator<Element> j = board.elementIterator("row"); j.hasNext();) {
				Element row = (Element) j.next();
				ArrayList<String> rowTiles = new ArrayList<String>();
				for (Iterator<Element> k = row.elementIterator(); k.hasNext();) {
					Element tile = (Element) k.next();
					rowTiles.add(tile.asXML().toString());
				}
				tilesList.add(rowTiles);
			}
		}

		int tilesSize = tilesList.size();
		String[][] tiles = new String[tilesSize][tilesSize];

		int i = 0;
		int j = 0;
		for (ArrayList<String> list : tilesList) {
			j = 0;
			for (String s : list) {
				switch (s) {
				case "<chap>up</chap>":
					tiles[i][j] = "@";
					break;
				case "<chap>down</chap>":
					tiles[i][j] = "@";
					break;
				case "<chap>left</chap>":
					tiles[i][j] = "@";
					break;
				case "<chap>right</chap>":
					tiles[i][j] = "@";
					break;
				case "<variant>up</variant>":
					tiles[i][j] = "^";
					break;
				case "<variant>down</variant>":
					tiles[i][j] = "v";
					break;
				case "<variant>left</variant>":
					tiles[i][j] = "<";
					break;
				case "<variant>right</variant>":
					tiles[i][j] = ">";
					break;
				case "<treasure/>":
					tiles[i][j] = "$";
					break;
				case "<free/>":
					tiles[i][j] = "_";
					break;
				case "<wall/>":
					tiles[i][j] = "#";
					break;
				case "<lock/>":
					tiles[i][j] = "!";
					break;
				case "<exit/>":
					tiles[i][j] = "X";
					break;
				case "<info/>":
					tiles[i][j] = "?";
					break;
				case "<extra/>":
					tiles[i][j] = "*";
					break;
				case "<door>red</door>":
					tiles[i][j] = "r";
					break;
				case "<door>cyan</door>":
					tiles[i][j] = "c";
					break;
				case "<door>green</door>":
					tiles[i][j] = "g";
					break;
				case "<key>red</key>":
					tiles[i][j] = "R";
					break;
				case "<key>cyan</key>":
					tiles[i][j] = "C";
					break;
				case "<key>green</key>":
					tiles[i][j] = "G";
					break;
				}
				j++;
			}
			i++;
		}
//		System.out.println("Loaded tiles:"); // added by Victor for checking
//		System.out.println(ArrayConvertor.from2dTo0d(tiles));
		return tiles;

	}

	/**
	 * Loads the player's current inventory from the saved game file.
	 * @param document the document object representing the saved game.
	 * @return a String array representing the inventory.
	 * @throws DocumentException if a Document object is not able to be created.
	 */
	protected static List<String> loadInventory(Document document) throws DocumentException {
		ArrayList<String> inventoryList = new ArrayList<String>();
		Element root = document.getRootElement();

		for (Iterator<Element> i = root.elementIterator("inventory"); i.hasNext();) {
			Element inventory = (Element) i.next();
			for (Iterator<Element> j = inventory.elementIterator(); j.hasNext();) {
				Element item = (Element) j.next();
				inventoryList.add(item.asXML().toString());
			}
		}

		List<String> inventory = new ArrayList<String>();
		for (String s : inventoryList) {
			switch (s) {
			case "<key>red</key>":
				inventory.add("R");
				break;
			case "<key>cyan</key>":
				inventory.add("C");
				break;
			case "<key>green</key>":
				inventory.add("G");
				break;
			}
		}
		return inventory;
	}


	/**
	 * Gets the time remaining in the game.
	 * @return the time remaining.
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Gets the number of treasures remaining on the board.
	 * @return the number of treasures remaining on the board.
	 */
	public int getTreasures() {
		return treasures;
	}

	/**
	 * Gets the player's current inventory.
	 * @return a String array representing the inventory.
	 */
	public List<String> getInventory() {
		return inventory;
	}

	/**
	 * Gets the current board.
	 * @return a 2D array representing the board.
	 */
	public String[][] getTiles() {
		return tiles;
	}

	/**
	 * Gets the level number from the saved game file.
	 * @return the level number from the saved game file.
	 */
	public int getLevelNumber() {
		return levelNumber;
	}
	
	/**
	 * Gets the filepath of the XML file to load.
	 * @return the filepath.
	 */
	public String getFilepath() {
		return filepath;
	}


	/**
	 * Gets the Document object created from the loaded file.
	 * @return the Document object.
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * Gets the direction of Chap.
	 * @return the direction of Chap.
	 */
	public Direction getDirection() {
		return direction;
	}


}
