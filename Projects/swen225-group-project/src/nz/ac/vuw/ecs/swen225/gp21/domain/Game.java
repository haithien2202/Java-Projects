package nz.ac.vuw.ecs.swen225.gp21.domain;

import javax.swing.JOptionPane;

import org.dom4j.DocumentException;

import nz.ac.vuw.ecs.swen225.gp21.app.AppWindow;
import nz.ac.vuw.ecs.swen225.gp21.app.Countdown;
import nz.ac.vuw.ecs.swen225.gp21.app.Main;
import nz.ac.vuw.ecs.swen225.gp21.persistency.LoadGame;

/**
 * The Game class handles the clock and interacts with the App module.
 * It is static and is initialised in the field of Main to get the
 * game started.
 * 
 * @author Victor Tam
 *
 */
public class Game {
		
		/** The board maze. */
		private Board boardMaze;
		
		/** The is paused. */
		private boolean isPaused = false;
		
		/** The is started. */
		private boolean isStarted = false;
		
		/** The time left. */
		private int timeLeft = 10000;
		
		/** The is won. */
		private boolean isWon = false;

		/**
		 * Instantiates a new game.
		 */
		public Game() {
		}
		
		/**
		 * Creates a copy of the board, and then updates the gamePane.
		 *
		 * @param b the new initial board
		 */
		public void setInitialBoard(Board b) {
			  boardMaze = b;
//			  initialBoard = b.clone();
			  AppWindow.gamePane.updateMove(Main.g);
		}
		
		/**
		 * Updates the game in App module.
		 *
		 * @param b the new board
		 */
		public void setBoard(Board b) {
			boardMaze = b;
			AppWindow.gamePane.updateMove(Main.g);
		}
		
		/**
		 * Gets the board.
		 *
		 * @return the board
		 */
		public Board getBoard() {
			return boardMaze;
		}
		
		/**
		 * Update pause.
		 *
		 * @param p the p
		 */
		public void updatePause(boolean p) {
			this.isPaused = p;
		}
		
		/**
		 * Checks if is paused.
		 *
		 * @return true, if is paused
		 */
		public boolean isPaused() {
			return isPaused;
		}
		
		/**
		 * Sets the started.
		 *
		 * @param s the new started
		 */
		public void setStarted(boolean s) {
			isStarted = s;
		}
		
		/**
		 * Checks if is started.
		 *
		 * @return true, if is started
		 */
		public boolean isStarted() {
			return isStarted;
		}
		
		/**
		 * Resets the level if the user runs out of time.
		 */
		public void checkTime() {
			if (timeLeft > 0) return;
			Main.g.setStarted(false);
			
			String[] buttons = {"Menu", "Reset Level", "Quit"};

		    int rc = JOptionPane.showOptionDialog(AppWindow.frame, "Time out!", "Confirmation",
		        JOptionPane.INFORMATION_MESSAGE, 0, null, buttons, buttons[2]);
		    if (rc == -1) {
		    	
		    }else if (rc == 0) {
		    	   AppWindow.gamePane.setVisible(false);
		    	   AppWindow.menu.setVisible(true);
		    	   Main.g.setStarted(false);
		    	   Main.g.setWin(false);
		    }else if (rc == 1) {
		    	  resetLevel();
		    }else if (rc == 2) {
		    	int a = JOptionPane.showConfirmDialog(AppWindow.frame, "Are you sure you want to exit?");
				if(a == JOptionPane.YES_OPTION) System.exit(0);
		    }
		}
		
		/**
		 * Reset level.
		 */
		public void resetLevel() {
			Main.g.setStarted(true);
			Main.resumeNPauseGame(false);
			Main.r.chapMoves.clear();
			try {
				String fs = System.getProperty("file.separator");
				String path = "levels" + fs;
				
				if (Main.g.getBoard().getCurrentLevel() == 1) new LoadGame(path + "level1.xml");
	    			else if (Main.g.getBoard().getCurrentLevel() == 2) new LoadGame(path + "level2.xml");
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			Countdown.resetCountDown();
			AppWindow.gamePane.updateMove(Main.g);
		}
		
		/**
		 * Sets the win.
		 *
		 * @param w the new win
		 */
		public void setWin(boolean w) {
			this.isWon = w;
		}
		
		/**
		 * Checks if is won.
		 *
		 * @return true, if is won
		 */
		public boolean isWon() {
			return this.isWon;
		}
		
		/**
		 * For PERSISTENCY when saving game.
		 * 
		 * Getter for timeLeft
		 *
		 * @return the time left
		 */
		public int getTimeLeft() {
			return timeLeft;
		}
		
		/**
		 * For PERSISTENCY when loading game.
		 * 
		 * Getter for timeLeft
		 *
		 * @param tl the new time left
		 */
		public void setTimeLeft(int tl) {
//			if(tl<1) throw new IllegalArgumentException("Has to have at least 1 second on the clock");// reomved bc of App
			this.timeLeft = tl;
		}
}
