package nz.ac.vuw.ecs.swen225.gp21.persistency;

import nz.ac.vuw.ecs.swen225.gp21.app.Main;
import nz.ac.vuw.ecs.swen225.gp21.domain.*;
import java.io.*;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * An object used to save a game.
 * 
 * @author markj
 *
 */
public class SaveGame {

	/**
	 * The number of the level to be saved.
	 */
	protected int levelNumber;

	/**
	 * The time remaining.
	 */
	protected int time;

	/**
	 * The number of treasures remaining on the board.
	 */
	protected int treasures;

	/**
	 * A 2D array representing the board.
	 */
	protected String[][] tiles;

	/**
	 * An array representing the items in the player's inventory.
	 */
	protected List<String> inventory;

	/**
	 * The direction chap is facing.
	 */
	protected Direction direction;
	
	/**
	 * Saving file name.
	 */
	public String fileName;

	
	/**
	 * The document to be saved.
	 * 
	 */
	protected Document document;
	
	/**
	 * The current game that save game objects are derived from.
	 */
	Game g;
	
	/**
	 * The board of the current game.
	 */
	Board b;
	
	/**
	 * An object which collects all of the elements required to save a game together.
	 */
	BoardObjectsToSave toSave;


	/**
	 * Creates a new SaveGame object.
	 * Collects information required to save a game, then writes to XML.
	 * @throws IOException if unable to save to disk.
	 */
	public SaveGame() throws IOException {
		this.g = Main.g;
		this.b = g.getBoard();
		this.toSave = b.saveToXML();
		this.levelNumber = toSave.getCurrentLevel();
		this.time = toSave.getCountdown();
		this.treasures = toSave.getTreasureNum();
		this.tiles = toSave.getBoardTiles();
		this.inventory = toSave.getKeysPickedUp();
		this.direction = toSave.getChapDir();
		this.document = writeDocument();
	}

	/**
	 * Builds the document to be saved.
	 * 
	 * @return a Document object.
	 */
	protected Document writeDocument() {
		Document saveGame = DocumentHelper.createDocument();
		Element root = saveGame.addElement("level").addAttribute("id", Integer.toString(this.levelNumber));
		buildTime(root);
		buildTreasures(root);
		buildBoard(root);
		buildInventory(root);
		return saveGame; 
	}
	
	/**
	 * Builds an XML element representing the time remaining in the game, and adds it to the root element.
	 * @param root the root element.
	 * @return an XML element representing the time.
	 */
	protected Element buildTime(Element root) {
		Element time = root.addElement("time").addText(Integer.toString(this.time));
		return time;
	}
	
	/**
	 * Builds an XML element representing the number of treasures remaining in the game, and adds it to the root element.
	 * @param root the root element.
	 * @return an XML element representing the number of treasures.
	 */
	protected Element buildTreasures(Element root) {
		Element treasures = root.addElement("treasures").addText(Integer.toString(this.treasures));
		return treasures;
	}
	
	/**
	 * Builds an XML element representing the game board, and adds it to the root element.
	 * @param root the root element.
	 * @return an XML element representing the game board.
	 */
	protected Element buildBoard(Element root) {
		Element board = root.addElement("board"); 
		for (int i = 0; i < this.tiles.length; i++) {
			Element row = board.addElement("row");
			for (int j = 0; j < this.tiles[i].length; j++) {
				Element tile;
				switch (this.tiles[i][j].toString()) {
				case "@":
					tile = row.addElement("chap");
					switch (this.direction) {
					case UP:
						tile.addText("up");
						break;
					case DOWN:
						tile.addText("down");
						break;
					case LEFT:
						tile.addText("left");
						break;
					case RIGHT:
						tile.addText("right");
						break;
					}
					break;
				case "^":
					tile = row.addElement("variant");
					tile.addText("up");
					break;
				case "v":
					tile = row.addElement("variant");
					tile.addText("down");
					break;
				case "<":
					tile = row.addElement("variant");
					tile.addText("left");
					break;
				case ">":
					tile = row.addElement("variant");
					tile.addText("right");
					break;
				case "R":
					tile = row.addElement("key");
					tile.addText("red");
					break;
				case "C":
					tile = row.addElement("key");
					tile.addText("cyan");
					break;
				case "G":
					tile = row.addElement("key");
					tile.addText("green");
					break;
				case "r":
					tile = row.addElement("door");
					tile.addText("red");
					break;
				case "c":
					tile = row.addElement("door");
					tile.addText("cyan");
					break;
				case "g":
					tile = row.addElement("door");
					tile.addText("green");
					break; 
				case "_":
					tile = row.addElement("free");
					break;
				case "$":
					tile = row.addElement("treasure");
					break;
				case "#":
					tile = row.addElement("wall");
					break;
				case "!":
					tile = row.addElement("lock");
					break;
				case "X":
					tile = row.addElement("exit");
					break;
				case "?":
					tile = row.addElement("info");
					break;
				case "*":
					tile = row.addElement("extra");
					break;
				}
			}
		}
		return board;
	}
	
	/**
	 * Builds an XML element representing the items in Chap's inventory, and adds it to the root element.
	 * @param root the root element.
	 * @return an XML element representing the the items in Chap's inventory.
	 */
	protected Element buildInventory(Element root) {
		Element inventory = root.addElement("inventory");
		for (int i = 0; i < this.inventory.size(); i++) {
			Element item;
			switch (this.inventory.get(i).toString()) {
			case "R":
				item = inventory.addElement("key");
				item.addText("red");
				break;
			case "C":
				item = inventory.addElement("key");
				item.addText("cyan");
				break;
			case "G":
				item = inventory.addElement("key");
				item.addText("green");
				break;
			}
		}
		return inventory;
	}
	

	/**
	 * Saves the Document to disk.
	 * 
	 * @param saveGame the document to be saved.
	 * @throws IOException thrown if the file is not able to be saved for any
	 *                     reason.
	 */
	private void saveFile(Document saveGame) throws IOException {
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
		String path = "levels" + Main.fs + "SavedGame" + Main.fs;
		fileName =   "savedgame " + dateTime.format(format) + ".xml";
		XMLWriter writer = new XMLWriter(new FileWriter(path + fileName));
		System.out.println("writing to file..."); 
		writer.write(saveGame);
		writer.close();
	}
	
	/**
	 * Public method that calls the saveFile method.
	 * @throws IOException thrown if the file is not able to be saved for any reason.
	 */
	public void save() throws IOException {
		saveFile(this.document);
	}
	
	/**
	 *  Public method that refreshes the data after the SaveGame object has already been created, for subsequent saves.
	 */
	public void refresh() {
		this.g = Main.g;
		this.b = g.getBoard();
		this.toSave = b.saveToXML();
		this.levelNumber = toSave.getCurrentLevel();
		this.time = toSave.getCountdown();
		this.treasures = toSave.getTreasureNum();
		this.tiles = toSave.getBoardTiles();
		this.inventory = toSave.getKeysPickedUp();
		this.direction = toSave.getChapDir();
		this.document = writeDocument();
	}
	
	/**
	 * Gets the level number to be saved. Used for testing only.
	 * @return the level number to be saved.
	 */
	protected int getLevelNumber() {
		return levelNumber;
	}

	/**
	 * Gets the time remaining to be saved. Used for testing only.
	 * @return the time remaining to be saved
	 */
	protected int getTime() {
		return time;
	}

	/**
	 * Gets the number of treasures to be saved. Used for testing only.
	 * @return the number of treasures to be saved.
	 */
	protected int getTreasures() {
		return treasures;
	}

	/**
	 * Gets the tiles representing the board to be saved. Used for testing only.
	 * @return the tiles representing the board to be saved.
	 */
	protected String[][] getTiles() {
		return tiles;
	}

	/**
	 * Gets the player's inventory to be saved. Used for testing only.
	 * @return the player's inventory to be saved.
	 */
	protected List<String> getInventory() {
		return inventory;
	}

	/**
	 * Gets the direction of Chap to be saved. Used for testing only.
	 * @return the direction of Chap to be saved.
	 */
	protected Direction getDirection() {
		return direction;
	}

	/**
	 * Gets the final document to be saved. Used for testing only.
	 * @return the final document to be saved.
	 */
	protected Document getDocument() {
		return document;
	}

}