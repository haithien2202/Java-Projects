package nz.ac.vuw.ecs.swen225.gp21.persistency;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.jupiter.api.Test;

import nz.ac.vuw.ecs.swen225.gp21.app.AppWindow;
import nz.ac.vuw.ecs.swen225.gp21.app.GamePane;
import nz.ac.vuw.ecs.swen225.gp21.app.Main;
import nz.ac.vuw.ecs.swen225.gp21.domain.ArrayConvertor;
import nz.ac.vuw.ecs.swen225.gp21.domain.Board;
import nz.ac.vuw.ecs.swen225.gp21.domain.BoardObjectsToSave;
import nz.ac.vuw.ecs.swen225.gp21.domain.Direction;
import nz.ac.vuw.ecs.swen225.gp21.domain.Game;
import nz.ac.vuw.ecs.swen225.gp21.recorder.Recorder;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveGameTests.
 */
public class SaveGameTests {

	/**
	 * Load game.
	 *
	 * @return the document
	 * @throws DocumentException the document exception
	 */
	public Document loadGame() throws DocumentException {
		String fs = System.getProperty("file.separator");
		String filepath = "test" + fs + "nz" + fs + "ac" + fs + "vuw" + fs + "ecs" + fs + "swen225" + fs + "gp21" + fs + "persistency" + fs + "level1test.xml";
		Document loaded;
		try {
			loaded = LoadGame.loadFile(filepath);
		} catch (DocumentException e) {
			loaded = null;
			e.printStackTrace();
		}
		int levelNumber = LoadGame.loadLevelNumber(loaded);
		int time = LoadGame.loadTime(loaded);
		List<String> inventory = LoadGame.loadInventory(loaded);
		Direction direction = LoadGame.loadChapDirection(loaded);
		int treasures = LoadGame.loadTreasures(loaded);
		Main.g = new Game();
		Game g = Main.g;
		AppWindow.gamePane = new GamePane();
		Main.r = new Recorder();
		String[][] board = LoadGame.loadBoard(loaded);
		String[] singleTiles = ArrayConvertor.from2dTo1d(board);
		Board b = new Board(singleTiles);
		b.setCurrentLevel(levelNumber);
		g.setBoard(b);
		g.getBoard().loadFromXML(time, board, inventory, direction, treasures, levelNumber);
		return loaded;
	}
	
	
	/**
	 * test 1 that the saved document is not null.
	 *
	 * @throws DocumentException the document exception
	 */
	@Test
	public void Test1() throws DocumentException {
		
		Document loaded = loadGame();
		assertFalse(loaded.equals(null));
	}
	
	/**
	 * test 2 that the level number to be saved is correct.
	 *
	 * @throws DocumentException the document exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void Test2() throws DocumentException, IOException {
		
		Document loaded = loadGame();
		int levelNumber = LoadGame.loadLevelNumber(loaded);
		SaveGame saveGame = new SaveGame();
		int saveLevelNumber = saveGame.getLevelNumber();
		assertEquals(levelNumber, saveLevelNumber);
	}
	
	/**
	 * test 3 that the number of treasures to be saved is correct.
	 *
	 * @throws DocumentException the document exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void Test3() throws DocumentException, IOException {
		
		Document loaded = loadGame();
		int treasureNumber = LoadGame.loadTreasures(loaded);
		SaveGame saveGame = new SaveGame();
		int saveTreasureNumber = saveGame.getTreasures();
		assertEquals(treasureNumber, saveTreasureNumber);
	}
	
	/**
	 * test 4 that the time to be saved is correct.
	 *
	 * @throws DocumentException the document exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void Test4() throws DocumentException, IOException {
		
		Document loaded = loadGame();
		int time = LoadGame.loadTime(loaded);
		SaveGame saveGame = new SaveGame();
		int saveTime = saveGame.getTime();
		assertEquals(time, saveTime);
	}
	
	/**
	 * test 5 that the board to be saved is correct.
	 *
	 * @throws DocumentException the document exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void Test5() throws DocumentException, IOException {
		
		Document loaded = loadGame();
		String[][] board = LoadGame.loadBoard(loaded);
		SaveGame saveGame = new SaveGame();
		String[][] saveBoard = saveGame.getTiles();
		assertArrayEquals(board, saveBoard);
	}
	
	/**
	 * test 6 that the inventory to be saved is correct.
	 *
	 * @throws DocumentException the document exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void Test6() throws DocumentException, IOException {
		
		Document loaded = loadGame();
		List<String> inventory = LoadGame.loadInventory(loaded);
		SaveGame saveGame = new SaveGame();
		List<String>  saveInventory = saveGame.getInventory();
		assertEquals(inventory, saveInventory);
	}
	
	/**
	 * test 7 that the time to be saved is correct.
	 *
	 * @throws DocumentException the document exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void Test7() throws DocumentException, IOException {
		
		Document loaded = loadGame();
		Direction direction = LoadGame.loadChapDirection(loaded);
		SaveGame saveGame = new SaveGame();
		Direction saveDirection = saveGame.getDirection();
		assertEquals(direction, saveDirection);
	}
	
	/**
	 * test 8 that the output of the buildTime() method is correct.
	 *
	 * @throws DocumentException the document exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void Test8() throws DocumentException, IOException {
		
		Document loaded = loadGame();
		SaveGame saveGame = new SaveGame();
		int saveLevelNumber = saveGame.getLevelNumber();
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("level").addAttribute("id", Integer.toString(saveLevelNumber));
		String buildTime = saveGame.buildTime(root).asXML().toString();
		assertEquals(buildTime, "<time>30</time>");
	}
	
	/**
	 * test 9 that the output of the buildTreasures() method is correct.
	 *
	 * @throws DocumentException the document exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void Test9() throws DocumentException, IOException {
		
		Document loaded = loadGame();
		SaveGame saveGame = new SaveGame();
		int saveLevelNumber = saveGame.getLevelNumber();
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("level").addAttribute("id", Integer.toString(saveLevelNumber));
		String buildTreasures = saveGame.buildTreasures(root).asXML().toString();
		assertEquals(buildTreasures, "<treasures>4</treasures>");
	}
	
	/**
	 * test 10 that the output of the buildInventory() method is correct.
	 *
	 * @throws DocumentException the document exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void Test10() throws DocumentException, IOException {
		
		Document loaded = loadGame();
		SaveGame saveGame = new SaveGame();
		int saveLevelNumber = saveGame.getLevelNumber();
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("level").addAttribute("id", Integer.toString(saveLevelNumber));
		String buildInventory = saveGame.buildInventory(root).asXML().toString();
		assertEquals(buildInventory, "<inventory><key>red</key><key>cyan</key><key>green</key></inventory>");
	}
	
	/**
	 * test 10 that the refresh method is correct.
	 *
	 * @throws DocumentException the document exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void Test12() throws DocumentException, IOException {
		
		Document loaded = loadGame();
		SaveGame saveGame = new SaveGame();
		saveGame.refresh();
		
	}
	
	/**
	 * test 10 that the save and saveFile methods are correct.
	 *
	 * @throws DocumentException the document exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void Test11() throws DocumentException, IOException {
		
		Document loaded = loadGame();
		SaveGame saveGame = new SaveGame();
		saveGame.save();
		
	}

}
