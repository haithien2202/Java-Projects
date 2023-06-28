package nz.ac.vuw.ecs.swen225.gp21.fuzz;


import org.junit.jupiter.api.Test;

import junit.framework.Assert;
import nz.ac.vuw.ecs.swen225.gp21.domain.*;
import nz.ac.vuw.ecs.swen225.gp21.recorder.Recorder;
import nz.ac.vuw.ecs.swen225.gp21.app.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;



// TODO: Auto-generated Javadoc
/**
 * To ensure game logic is sound.

 * @author Vaishnav
 */
public class FuzzTest {

	/**
	 * The class FuzzTest main purpose is to generate 
	 * random user inputs, and check whether they create
	 * an error within the game. Please run these tests individually. 
	 * 
	 *THE 2ND LEVEL TEST DOESNT RUN PROPERLY IF YOU RUN ALL THE TESTS 
	 *AT THE SAME TIME. 
	 */

	Game game = new Game();
	
	/** The recorder. */
	Recorder recorder = new Recorder();

	/**
	 * Testing level 1 of the game.
	 */
	@Test
	public void Level1() {
		//the static void main method is run here instead of the Main class in the application module.
		Main.g = game;
		Main.r = recorder;
		AppWindow window = new AppWindow();
		window.frame.setVisible(true);
		Thread domain = new Thread() {
			public void run() {
				Board bm = game.getBoard();
				while (true) {
					if (bm != null) {
						for (AbstractChap v : bm.getVariants()) {
							if (v instanceof VariantChap) {
								v.move(null, null, bm);
							} else {
								throw new IllegalArgumentException("Non-variant in a set of Variants");
							}
						}
					}
					try {
						sleep(2);
					} catch (InterruptedException e) {
						//EMPTY
					}
				}
			} 
		};

		Thread clock = new Thread() {
			public void run() {
				while (true) {
					Countdown.getCountDown(game);
					if (!game.isPaused() && game.isStarted()) {
						AppWindow.gamePane.updateTime(game);
						game.checkTime();
					}
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		clock.start();
		domain.start();

		//Fuzz testing begins
		//First thing the module does is make am input 
		//to load the first level of the game up

		AppWindow.frame.dispatchEvent(new KeyEvent(window.frame, KeyEvent.KEY_PRESSED, 
				System.currentTimeMillis(), KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_1));

		// a map that shows how many times the user has visited a  tile
		HashMap<AbstractTile, Integer> visitedTilesMap = 
				new HashMap<>(); 
		Board board = game.getBoard();

		AbstractTile[][] tileBoardReflected = board.getTileBoard();
		for (int row = 0; row < tileBoardReflected.length; row++) {
			for (int col = 0; col < tileBoardReflected.length; col++) {
				AbstractTile tile = tileBoardReflected[row][col];
				visitedTilesMap.put(tile, 0);

			}
		}

		Board currentBoard = null;
		Board newBoard = null;

		//Users current position.
		Position current = new Position(game.getBoard()
				.getRegularChapPos().getX(), game.getBoard().getRegularChapPos().getY()); 

		//The tile user is currently on.
		AbstractTile currentTile = game.getBoard().getTile(current);

		//Adding the first tile the user is on to the map
		visitedTilesMap.put(currentTile, visitedTilesMap.get(currentTile) + 1);


		boolean check1 = false;
		boolean check2 = false;

		try {
			//Simulating random user inputs to make the user move around the board.
			//The main idea of this code is to make the user move 
			// randomly around the board, but at the same time make the user move
			// intelligently. For example, the user shouldn't 
			// spend multiple times moving one tile forward then one tile back.

			//The code check which tiles the user can move to, and check how
			//many times the user can moved to that particular tile. It choose
			//the tile that the user had visited the least.
			//this way the code promotes the user to move all around the board.

			for (int i = 0; i < 6000; i++) {

				//Pausing the game.
				if (i == 30) {
					AppWindow.frame.dispatchEvent(new KeyEvent(window.frame, KeyEvent.KEY_PRESSED, 
							System.currentTimeMillis(), KeyEvent.KEY_PRESSED, KeyEvent.VK_SPACE));
					currentBoard = game.getBoard();
				}


				//Unpausing the game.
				if (i == 60) {
					newBoard =  game.getBoard();
					check2 = true;
					assert (newBoard.equals(currentBoard));

					AppWindow.frame.dispatchEvent(new KeyEvent(window.frame, KeyEvent.KEY_PRESSED, 
							System.currentTimeMillis(), KeyEvent.KEY_PRESSED, KeyEvent.VK_ESCAPE));
				}


				//A map that shows how many times the user
				//has visited a tile from it's current tile options.
				//The user only has up to 4 tile to move at it's current position.
				HashMap<AbstractTile, Integer> currentPossibleTilesMap = new HashMap<>(); 

				// Tiles that are top,bottom,left and right adjacent to the user, 
				ArrayList<AbstractTile> adjacentTiles = new ArrayList<>(); 

				current = new Position(game.getBoard().getRegularChapPos().getX(),
						game.getBoard().getRegularChapPos().getY());
				currentTile = game.getBoard().getTile(current);

				AbstractTile rightTile = board.getNeighbouringTile(current, Direction.RIGHT);
				AbstractTile bottomTile = board.getNeighbouringTile(current, Direction.DOWN);
				AbstractTile topTile = board.getNeighbouringTile(current, Direction.UP);
				AbstractTile leftTile = board.getNeighbouringTile(current, Direction.LEFT);
				adjacentTiles.add(rightTile);
				adjacentTiles.add(bottomTile);
				adjacentTiles.add(topTile);
				adjacentTiles.add(leftTile);

				// All tiles that the user might move to, that have been visited the least amount of times
				ArrayList<AbstractTile> chosenTiles = new ArrayList<>(); 


				//Finding the tiles that the user can move to, 
				//that have been visited the least amount of times.
				int lowestCount = 1000;
				for (AbstractTile tile : adjacentTiles) {
					if (!(tile == null) && tile.validTile(board)) {
						if (!visitedTilesMap.containsKey(tile)) {
							visitedTilesMap.put(tile, 0);
						}
						currentPossibleTilesMap.put(tile, visitedTilesMap.get(tile));
						if (currentPossibleTilesMap.get(tile) < lowestCount) {
							lowestCount = currentPossibleTilesMap.get(tile);
						}
					}
				}

				//Finding the tiles that have been visited the least.
				for (Entry<AbstractTile, Integer> entry : currentPossibleTilesMap.entrySet()) {
					if (entry.getValue().equals(lowestCount) && entry.getKey().validTile(game.getBoard())) {
						chosenTiles.add(entry.getKey());     
					}
				}

				//Moving the user to the least visited tiles.
				//Randomly picks one tile, if there are multiple
				//least visited tile.
				if (!chosenTiles.isEmpty()) {
					int rand = (int) (Math.random() * (chosenTiles.size())) + 0;
					AbstractTile theChosenTile = chosenTiles.get(rand);
					if (theChosenTile.equals(rightTile)) {
						window.frame.dispatchEvent(new KeyEvent(window.frame, KeyEvent.KEY_PRESSED, 
								System.currentTimeMillis(), KeyEvent.KEY_PRESSED, KeyEvent.VK_RIGHT));
						visitedTilesMap.put(theChosenTile, visitedTilesMap.get(theChosenTile) + 1);

					}
					else if (theChosenTile.equals(leftTile)) {  
						window.frame.dispatchEvent(new KeyEvent(window.frame, KeyEvent.KEY_PRESSED,
								System.currentTimeMillis(), KeyEvent.KEY_PRESSED, KeyEvent.VK_LEFT));
						visitedTilesMap.put(theChosenTile, visitedTilesMap.get(theChosenTile) + 1);
					}
					else if (theChosenTile.equals(bottomTile)) {
						if (theChosenTile instanceof ExitTile) {
							check1 = true;
							visitedTilesMap.put(theChosenTile, visitedTilesMap.get(theChosenTile) + 1);
							i = 6000;
						} else {
							window.frame.dispatchEvent(new KeyEvent(window.frame, KeyEvent.KEY_PRESSED,
									System.currentTimeMillis(), KeyEvent.KEY_PRESSED, KeyEvent.VK_DOWN));
							visitedTilesMap.put(theChosenTile, visitedTilesMap.get(theChosenTile) + 1);
						}
					}
					else if (theChosenTile.equals(topTile)) {
						window.frame.dispatchEvent(new KeyEvent(window.frame, KeyEvent.KEY_PRESSED,
								System.currentTimeMillis(), KeyEvent.KEY_PRESSED, KeyEvent.VK_UP));

						visitedTilesMap.put(theChosenTile, visitedTilesMap.get(theChosenTile) + 1);
					}                                   
				}

				//Printing the board
				String boardTextTemp = game.getBoard().toString();
				int m = 0;
				StringBuilder stringBoardTemp = new StringBuilder();
				for (int x = 0; x < boardTextTemp.length(); x++) {
					m++;
					stringBoardTemp.append(boardTextTemp.toCharArray()[x]);
					if (m == 15) {
						stringBoardTemp.append("\n");
						m = 0;
					}

				}
				System.out.println(stringBoardTemp);

			}
		}catch(IllegalArgumentException | IllegalStateException | NullPointerException e) {
			e.printStackTrace();
		}
		if (check1) {
			Main.g = null;
			Main.r = null;
			for (AbstractTile k : visitedTilesMap.keySet()) {
				if (k.validTile(game.getBoard())) {
					int valueCheck= visitedTilesMap.get(k);
					if (valueCheck == 0){
						System.out.println("Regular chap has not  visited every valid tile");       
					}       
				}
			}
			//checkcode.
		}
	}




	/**
	 * Testing level 2 of the game.
	 */
	@Test
	public void Level2() {
		//the static void main method is run here instead of the Main class in the application module.
		Main.g = game;
		Main.r = recorder;
		AppWindow window = new AppWindow();
		window.frame.setVisible(true);
		Thread domain = new Thread() {
			public void run() {
				Board bm = game.getBoard();
				while (true) {
					if (bm != null) {
						for (AbstractChap v : bm.getVariants()) {
							if (v instanceof VariantChap) {
								v.move(null, null, bm);
							} else {
								throw new IllegalArgumentException("Non-variant in a set of Variants");
							}
						}
					}
					try {
						sleep(2);
					} catch (InterruptedException e) {
						//EMPTY
					}
				}
			} 
		};

		Thread clock = new Thread() {
			public void run() {
				while (true) {
					Countdown.getCountDown(game);
					if (!game.isPaused() && game.isStarted()) {
						AppWindow.gamePane.updateTime(game);
						game.checkTime();
					}
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		clock.start();
		domain.start();

		//Fuzz testing begins
		//First thing the module does is make am input 
		//to load the first level of the game up

		AppWindow.frame.dispatchEvent(new KeyEvent(window.frame, KeyEvent.KEY_PRESSED, 
				System.currentTimeMillis(), KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_2));

		// a map that shows how many times the user has visited a  tile
		HashMap<AbstractTile, Integer> visitedTilesMap = 
				new HashMap<>(); 
		Board board = game.getBoard();

		AbstractTile[][] tileBoardReflected = board.getTileBoard();
		for (int row = 0; row < tileBoardReflected.length; row++) {
			for (int col = 0; col < tileBoardReflected.length; col++) {
				AbstractTile tile = tileBoardReflected[row][col];
				visitedTilesMap.put(tile, 0);

			}
		}

		Board currentBoard = game.getBoard();
		Board newBoard = null;

		//Users current position.
		Position current = new Position(game.getBoard()
				.getRegularChapPos().getX(), game.getBoard().getRegularChapPos().getY()); 

		//The tile user is currently on.
		AbstractTile currentTile = game.getBoard().getTile(current);

		//Adding the first tile the user is on to the map
		visitedTilesMap.put(currentTile, visitedTilesMap.get(currentTile) + 1);

		boolean check2 = false;
		boolean check1 = false;


		try {
			//Simulating random user inputs to make the user move around the board.
			//The main idea of this code is to make the user move 
			// randomly around the board, but at the same time make the user move
			// intelligently. For example, the user shouldn't 
			// spend multiple times moving one tile forward then one tile back.

			//The code check which tiles the user can move to, and check how
			//many times the user can moved to that particular tile. It choose
			//the tile that the user had visited the least.
			//this way the code promotes the user to move all around the board.

			for (int i = 0; i < 100; i++) {

				//Moving the variantChap around
				for (AbstractChap v : currentBoard.getVariants()) {
					if (v instanceof VariantChap) {
						v.move(null, null, currentBoard);
					} else {
						throw new IllegalArgumentException("Non-variant in a set of Variants");
					}
				}

				//Pause the game
				if (i == 30) {
					AppWindow.frame.dispatchEvent(new KeyEvent(window.frame, KeyEvent.KEY_PRESSED, 
							System.currentTimeMillis(), KeyEvent.KEY_PRESSED, KeyEvent.VK_SPACE));
					currentBoard = game.getBoard();

				}

				//Unpause the game
				if (i == 60) {
					newBoard =  game.getBoard();
					check2 = true;
					assert (newBoard.equals(currentBoard));
					AppWindow.frame.dispatchEvent(new KeyEvent(window.frame, KeyEvent.KEY_PRESSED, 
							System.currentTimeMillis(), KeyEvent.KEY_PRESSED, KeyEvent.VK_ESCAPE));

				}


				//A map that shows how many times the user
				//has visited a tile from it's current tile options.
				//The user only has up to 4 tile to move at it's current position.
				HashMap<AbstractTile, Integer> currentPossibleTilesMap = new HashMap<>(); 

				//Array of tiles that are top,bottom,left and right adjacent to the user, 
				ArrayList<AbstractTile> adjacentTiles = new ArrayList<>(); 

				current = new Position(game.getBoard().getRegularChapPos().getX(),
						game.getBoard().getRegularChapPos().getY());
				currentTile = game.getBoard().getTile(current);

				AbstractTile rightTile = board.getNeighbouringTile(current, Direction.RIGHT);
				AbstractTile bottomTile = board.getNeighbouringTile(current, Direction.DOWN);
				AbstractTile topTile = board.getNeighbouringTile(current, Direction.UP);
				AbstractTile leftTile = board.getNeighbouringTile(current, Direction.LEFT);
				adjacentTiles.add(rightTile);
				adjacentTiles.add(bottomTile);
				adjacentTiles.add(topTile);
				adjacentTiles.add(leftTile);


				//All chosen tiles that the user could move to, that have been visited the least amount of times
				ArrayList<AbstractTile> chosenTiles = new ArrayList<>();


				//Finding the tiles that the user can move to, 
				//that have been visited the least amount of times.
				int lowestCount = 1000;
				for (AbstractTile tile : adjacentTiles) {
					if (!(tile == null) && tile.validTile(board)) {
						if (!visitedTilesMap.containsKey(tile)) {
							visitedTilesMap.put(tile, 0);
						}
						currentPossibleTilesMap.put(tile, visitedTilesMap.get(tile));
						if (currentPossibleTilesMap.get(tile) < lowestCount) {
							lowestCount = currentPossibleTilesMap.get(tile);
						}
					}
				}

				//Finding the tiles that have been visited the least.
				for (Entry<AbstractTile, Integer> entry : currentPossibleTilesMap.entrySet()) {
					if (entry.getValue().equals(lowestCount)) {
						chosenTiles.add(entry.getKey());     
					}
				}

				//Moving the user to the least visited tiles.
				//Randomly picks one tile, if there are multiple
				//least visited tile.
				if (!chosenTiles.isEmpty()) {
					int rand = (int) (Math.random() * (chosenTiles.size())) + 0;
					AbstractTile theChosenTile = chosenTiles.get(rand);
					if (theChosenTile.equals(rightTile)) {
						if (theChosenTile instanceof ExitTile) {
							check1 = true;
							visitedTilesMap.put(theChosenTile, visitedTilesMap.get(theChosenTile) + 1);
							i = 6000;
						} else {
							window.frame.dispatchEvent(new KeyEvent(window.frame, KeyEvent.KEY_PRESSED, 
									System.currentTimeMillis(), KeyEvent.KEY_PRESSED, KeyEvent.VK_RIGHT));
							visitedTilesMap.put(theChosenTile, visitedTilesMap.get(theChosenTile) + 1);
						}
					}
					else if (theChosenTile.equals(leftTile)) {  
						window.frame.dispatchEvent(new KeyEvent(window.frame, KeyEvent.KEY_PRESSED, 
								System.currentTimeMillis(), KeyEvent.KEY_PRESSED, KeyEvent.VK_LEFT));

						visitedTilesMap.put(theChosenTile, visitedTilesMap.get(theChosenTile) + 1);
					}
					else if (theChosenTile.equals(bottomTile)) {
						if (theChosenTile instanceof ExitTile) {
							check1 = true;
							visitedTilesMap.put(theChosenTile, visitedTilesMap.get(theChosenTile) + 1);
							i = 6000;
						} else {
							window.frame.dispatchEvent(new KeyEvent(window.frame, KeyEvent.KEY_PRESSED, 
									System.currentTimeMillis(), KeyEvent.KEY_PRESSED, KeyEvent.VK_DOWN));
							visitedTilesMap.put(theChosenTile, visitedTilesMap.get(theChosenTile) + 1);
						}        }
					else if (theChosenTile.equals(topTile)) {
						window.frame.dispatchEvent(new KeyEvent(window.frame, KeyEvent.KEY_PRESSED,
								System.currentTimeMillis(), KeyEvent.KEY_PRESSED, KeyEvent.VK_UP));

						visitedTilesMap.put(theChosenTile, visitedTilesMap.get(theChosenTile) + 1);
					}
				}

				//Printing the board.
				String boardTextTemp = game.getBoard().toString();
				int m = 0;
				StringBuilder stringBoardTemp = new StringBuilder();
				for (int x = 0; x < boardTextTemp.length(); x++) {
					m++;
					stringBoardTemp.append(boardTextTemp.toCharArray()[x]);
					if (m == 15) {
						stringBoardTemp.append("\n");
						m = 0;
					}

				}
				System.out.println(stringBoardTemp);

			}
		} catch (IllegalArgumentException | IllegalStateException | NullPointerException e) {
			e.printStackTrace();
		}
		//Checkcode.
		if (check1) {
			Main.g = null;
			Main.r = null;
			for (AbstractTile k : visitedTilesMap.keySet()) {
				if (k.validTile(newBoard)) {
					int valueCheck= visitedTilesMap.get(k);
					if (valueCheck == 0){
						System.out.println("Regular chap has not  visited every valid tile");       
					}
				}

			}
		}

	}





}
