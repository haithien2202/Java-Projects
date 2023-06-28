package nz.ac.vuw.ecs.swen225.gp21.recorder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import nz.ac.vuw.ecs.swen225.gp21.app.Main;
import nz.ac.vuw.ecs.swen225.gp21.domain.ArrayConvertor;
import nz.ac.vuw.ecs.swen225.gp21.domain.Board;
import nz.ac.vuw.ecs.swen225.gp21.domain.Direction;
import nz.ac.vuw.ecs.swen225.gp21.domain.Position;

// TODO: Auto-generated Javadoc
/**
 * The Class Recorder.
 *
 * @author Jamie
 */
public class Recorder {
	
	/** The chap moves. */
	public List<Tuple> chapMoves = new ArrayList<Tuple>();
	
	/** The level. */
	public int level = 0;
	
	/** The finished loading. */
	public boolean finishedLoading = false;
	
	/**
	 * Save record.
	 *
	 * @param check the check
	 */
	public void saveRecord(boolean check) {
		Document document = writeDocument();
		try {
			saveFile(document, check);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * A method to save information into list.
	 */
	public void saveMove() {
		Board newBoard = Main.g.getBoard().clone();
		List<String> l = Main.g.getBoard().getInventoryStr();
		Direction dir = Main.g.getBoard().getRegularChapDir();
		Position chapPos = Main.g.getBoard().getRegularChapPos();
		Tuple currentTuple = new Tuple(newBoard, l, dir, chapPos);
		chapMoves.add(currentTuple);
	}
	
	
	/**
	 * Save file into xml.
	 *
	 * @param saveGame the save game
	 * @param check the check
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void saveFile(Document saveGame, boolean check) throws IOException {
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
		String path = null;
		if (!check)
			path = "recorder" + Main.fs + "unfinishedRecord" + Main.fs;
		else
			path = "recorder" + Main.fs + "finishedRecord" + Main.fs;
		XMLWriter writer = new XMLWriter(new FileWriter(path + "savedgame " + dateTime.format(format) + ".xml"));
		writer.write(saveGame);
		writer.close();
	}
	
	/**
	 * Write all info into document.
	 *
	 * @return the document
	 */
	private Document writeDocument() {
		Document saveGame = DocumentHelper.createDocument();
		Element root = saveGame.addElement("level").addAttribute("id", Integer.toString(Main.g.getBoard().getCurrentLevel()));
		for (Tuple c : chapMoves) {
			    Element stepEle =  root.addElement("step");
				Element board = buildBoard(stepEle,c.getBoard());
				Element inventory = buildInventory(stepEle, c.getInventory());
				Element direction  = buildDirection(stepEle, c.getDirection());
				Element position  = buildPosition(stepEle, c.getPosition());
		}
		return saveGame; 
	}
	
	/**
	 * Build direction into text element.
	 *
	 * @param root the root
	 * @param dir the dir
	 * @return the element
	 */
	private Element buildDirection(Element root, Direction dir) {
		Element direction = root.addElement("direction");
		direction.addText(dir.toString());
		return direction; 
	}
	
	/**
	 * Generate position into text element.
	 *
	 * @param root the root
	 * @param pos the pos
	 * @return the element
	 */
	private Element buildPosition(Element root, Position pos) {
		Element position = root.addElement("position");
		Element x = position.addElement("x");
		x.addText(String.valueOf(pos.getX()));
		Element y = position.addElement("y");
		y.addText(String.valueOf(pos.getY()));
		return position; 
	}
	
	/**
	 * Build the board into xml element.
	 *
	 * @param root the root
	 * @param b the b
	 * @return the element
	 */
	private Element buildBoard(Element root, Board b) {
		Element board = root.addElement("board"); 
		String[][] bString = b.getCurrentBoard();
		for (int i = 0; i < bString.length; i++) {
			Element row = board.addElement("row");
			for (int j = 0; j < bString[i].length; j++) {
				@SuppressWarnings("unused")
				Element tile;
				switch (bString[i][j].toString()) {
				case "@":
					tile = row.addElement("chap");
					break;
				case "^":
					tile = row.addElement("up");
					break;
				case "v":
					tile = row.addElement("down");
					break;
				case "<":
					tile = row.addElement("left");
					break;
				case ">":
					tile = row.addElement("right");
					break;
				case "R":
					tile = row.addElement("redkey");
					break;
				case "C":
					tile = row.addElement("cyankey");
					break;
				case "G":
					tile = row.addElement("greenkey");
					break;
				case "r":
					tile = row.addElement("reddoor");
					break;
				case "c":
					tile = row.addElement("cyandoor");
					break;
				case "g":
					tile = row.addElement("greendoor");
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
	 * Build inventory into element.
	 *
	 * @param root the root
	 * @param inventoryList the inventory list
	 * @return the element
	 */
	private Element buildInventory(Element root, List<String> inventoryList) {
		Element inventory = root.addElement("inventory");
		for (int i = 0; i < inventoryList.size(); i++) {
			@SuppressWarnings("unused")
			Element item;
			switch (inventoryList.get(i)) {
			case "R":
				item = inventory.addElement("redkey");
				break;
			case "C":
				item = inventory.addElement("cyankey");
				break;
			case "G":
				item = inventory.addElement("greenkey");
				break;
			}
		}
		return inventory;
	}
	
	
	/**
	 * Load the record with @param file.
	 *
	 * @param savedFile the saved file
	 */
	public void loadRecord(File savedFile) {
		finishedLoading = false;
		if (savedFile != null) {
			chapMoves.clear();
			chapMoves = loadMove(loadFile(savedFile));
		}
		finishedLoading = true;
	}
	
	/**
	 * Load the record with @param file name string.
	 *
	 * @param name the name
	 */
	public void loadRecord(String name) {
		 String path = "recorder" + Main.fs + "unfinishedRecord" + Main.fs;
		 File savedFile = new File(path + name);
		 if (savedFile != null) {
				chapMoves.clear();
				chapMoves = loadMove(loadFile(savedFile));
		 }
	}
	
	/**
	 * Load the file into document.
	 *
	 * @param inputFile the input file
	 * @return the document
	 */
	protected static Document loadFile(File inputFile) {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(inputFile);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	
	/**
	 * Load the info into a list.
	 *
	 * @param d the d
	 * @return the list
	 */
	public List<Tuple> loadMove(Document d) {
		Element root = (Element) d.selectSingleNode("/level");
		level = loadLevelNumber(d);
		List<Tuple> loadedBoards = new ArrayList<Tuple>();
		for (Iterator<Element> i = root.elementIterator("step"); i.hasNext();) {
				Element step = i.next();
					Board c = loadBoard(step);
					List<String> inven = loadInventory(step);
					Direction dir = loadChapDirection(step);
					Position pos = loadChapPosition(step);
					loadedBoards.add(new Tuple(c,inven,dir, pos));	
		}
		return loadedBoards;
	}
	
	/**
	 * Get the level number.
	 *
	 * @param document the document
	 * @return the int
	 */
	private int loadLevelNumber(Document document) {
		Node node = document.selectSingleNode("/level");
		Element element = (Element) node;
	    String levelNumber = element.attributeValue("id");
		int levelNum = Integer.parseInt(levelNumber);
		return levelNum;
	}
	
	/**
	 * Get player direction .
	 *
	 * @param root the root
	 * @return the direction
	 */
	protected static Direction loadChapDirection(Element root) {
		String dir = "";
		Direction direction = null;
		Node dirNode = root.selectSingleNode("direction");
		dir = dirNode.getText();
		switch(dir) {
		case "UP":
			direction = Direction.UP;
			break;
		case "DOWN":
			direction = Direction.DOWN;
			break;
		case "LEFT":
			direction = Direction.LEFT;
			break;
		case "RIGHT":
			direction = Direction.RIGHT;
			break;
		}

		return direction;
	}
	
	/**
	 * Get player position.
	 *
	 * @param root the root
	 * @return the position
	 */
	protected static Position loadChapPosition(Element root) {
		Position position = null;
		Node posNode = root.selectSingleNode("position");
		Node x = posNode.selectSingleNode("x");
		Node y = posNode.selectSingleNode("y");
		position = new Position(Integer.parseInt(x.getText()), Integer.parseInt(y.getText()));
		return position;
	}
	
	/**
	 * Get player's inventory.
	 *
	 * @param root the root
	 * @return the list
	 */
	private static List<String> loadInventory(Element root)  {
		ArrayList<String> inventoryList = new ArrayList<String>();

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
			case "<redkey/>":
				inventory.add("R");
				break;
			case "<cyankey/>":
				inventory.add("C");
				break;
			case "<greenkey/>":
				inventory.add("G");
				break;
			}
		}
		return inventory;
	}
	
	/**
	 * Get board from file.
	 *
	 * @param root the root
	 * @return the board
	 */
	private Board loadBoard(Element root) {
		ArrayList<ArrayList<String>> tilesList = new ArrayList<ArrayList<String>>();

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
		for (ArrayList<String> list : tilesList) {
			int j = 0;
			for (String s : list) {
				switch (s) {
				case "<chap/>":
					tiles[i][j] = "@";
					break;
				case "<up/>":
					tiles[i][j] = "^";
					break;
				case "<down/>":
					tiles[i][j] = "v";
					break;
				case "<left/>":
					tiles[i][j] = "<";
					break;
				case "<right/>":
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
				case "<reddoor/>":
					tiles[i][j] = "r";
					break;
				case "<cyandoor/>":
					tiles[i][j] = "c";
					break;
				case "<greendoor/>":
					tiles[i][j] = "g";
					break;
				case "<redkey/>":
					tiles[i][j] = "R";
					break;
				case "<cyankey/>":
					tiles[i][j] = "C";
					break;
				case "<greenkey/>":
					tiles[i][j] = "G";
					break;
				}
				j++;
			}
			i++;
		}	
		String[] singleTiles = ArrayConvertor.from2dTo1d(tiles);
		Board b = new Board(singleTiles);
		return b;
	}
		
	/**
	 * The Class Tuple.
	 */
	public class Tuple{
		
		/** The current. */
		private Board current;
		
		/** The inventory list. */
		private List<String> inventoryList = new ArrayList<String>();
		
		/** The current chap dir. */
		private Direction currentChapDir;
		
		/** The chap pos. */
		private Position chapPos;
		
		/**
		 * Instantiates a new tuple.
		 *
		 * @param c the c
		 * @param l the l
		 * @param chapDir the chap dir
		 * @param p the p
		 */
		public Tuple(Board c, List<String> l, Direction chapDir, Position p) {
			this.current = c;
			this.inventoryList = l;
			this.currentChapDir = chapDir;
			this.chapPos = p;
		}
		
		/**
		 * Gets the board.
		 *
		 * @return the board
		 */
		public Board getBoard() {
			return this.current;
		}
		
		/**
		 * Gets the inventory.
		 *
		 * @return the inventory
		 */
		public List<String> getInventory() {
			return this.inventoryList;
		}
		
		/**
		 * Gets the direction.
		 *
		 * @return the direction
		 */
		public Direction getDirection() {
			return this.currentChapDir;
		}
		
		/**
		 * Gets the position.
		 *
		 * @return the position
		 */
		public Position getPosition() {
			return this.chapPos;
		}
	}
}