package nz.ac.vuw.ecs.swen225.gp21.persistency;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;

import nz.ac.vuw.ecs.swen225.gp21.app.AppWindow;
import nz.ac.vuw.ecs.swen225.gp21.app.GamePane;
import nz.ac.vuw.ecs.swen225.gp21.app.Main;
import nz.ac.vuw.ecs.swen225.gp21.domain.Direction;
import nz.ac.vuw.ecs.swen225.gp21.domain.Game;
import nz.ac.vuw.ecs.swen225.gp21.persistency.LoadGame;
import nz.ac.vuw.ecs.swen225.gp21.recorder.Recorder;

// TODO: Auto-generated Javadoc
/**
 * The Class LoadGameTests.
 */
public class LoadGameTests {
	
	/**
	 * Load game.
	 *
	 * @return the document
	 */
	public Document loadGame() {
		String fs = System.getProperty("file.separator");
		String filepath = "test" + fs + "nz" + fs + "ac" + fs + "vuw" + fs + "ecs" + fs + "swen225" + fs + "gp21" + fs + "persistency" + fs + "level1test.xml";
		Document loaded;
		try {
			loaded = LoadGame.loadFile(filepath);
		} catch (DocumentException e) {
			loaded = null;
		}
		return loaded;
	}
	
	
	/**
	 * test 1 tests that the document loaded is not null.
	 *
	 * @throws DocumentException the document exception
	 */
	@Test
	public void Test1() throws DocumentException {
		Document loaded = loadGame();
		assertFalse(loaded.equals(null));
	}
	
	/**
	 * test 2 tests that the level number is correct.
	 *
	 * @throws DocumentException the document exception
	 */
	@Test
	public void Test2() throws DocumentException {
		Document loaded = loadGame();
		int levelNumber = LoadGame.loadLevelNumber(loaded);
		assertTrue(levelNumber == 1);
	}
	
	/**
	 * test 3 tests that the time is correct.
	 *
	 * @throws DocumentException the document exception
	 */
	@Test
	public void Test3() throws DocumentException {
		Document loaded = loadGame();
		int time = LoadGame.loadTime(loaded);
		assertTrue(time == 30);
	}
	
	/**
	 * test 4 tests that the number of treasures is correct.
	 *
	 * @throws DocumentException the document exception
	 */
	@Test
	public void Test4() throws DocumentException {
		Document loaded = loadGame();
		int treasures = LoadGame.loadTreasures(loaded);
		assertTrue(treasures == 4);
	}
	
	/**
	 * test 5 tests that the board's size is correct.
	 *
	 * @throws DocumentException the document exception
	 */
	@Test
	public void Test5() throws DocumentException {
		Document loaded = loadGame();
		String[][] board = LoadGame.loadBoard(loaded);
		assertTrue(board.length * board[0].length == 225); 
	}
	
	/**
	 * test 6 tests that the inventory is correct.
	 *
	 * @throws DocumentException the document exception
	 */
	@Test
	public void Test6() throws DocumentException {
		Document loaded = loadGame();
		List<String> inventory = LoadGame.loadInventory(loaded);
		assertTrue(inventory.get(0) == "R"); 
	}
	
	/**
	 * test 7 tests that Chap's direction is correct.
	 *
	 * @throws DocumentException the document exception
	 */
	@Test
	public void Test7() throws DocumentException {
		Document loaded = loadGame();
		Direction direction = LoadGame.loadChapDirection(loaded);
		assertTrue(direction == Direction.RIGHT); 
	}
	
	/**
	 * test 8 tests the main LoadGame constructor.
	 *
	 * @throws DocumentException the document exception
	 */
	@Test
	public void Test8() throws DocumentException {
		Document loaded = loadGame();
		String fs = System.getProperty("file.separator");
		String filepath = "test" + fs + "nz" + fs + "ac" + fs + "vuw" + fs + "ecs" + fs + "swen225" + fs + "gp21" + fs + "persistency" + fs + "level1test.xml";
		Main.g = new Game();
		Game g = Main.g;
		AppWindow.gamePane = new GamePane();
		Main.r = new Recorder();
		LoadGame game = new LoadGame(filepath);
		 
	}
	

}
