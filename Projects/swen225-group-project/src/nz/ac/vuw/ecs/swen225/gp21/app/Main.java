package nz.ac.vuw.ecs.swen225.gp21.app;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.dom4j.DocumentException;

import nz.ac.vuw.ecs.swen225.gp21.domain.AbstractChap;
import nz.ac.vuw.ecs.swen225.gp21.domain.Board;
import nz.ac.vuw.ecs.swen225.gp21.domain.Game;
import nz.ac.vuw.ecs.swen225.gp21.domain.VariantChap;
import nz.ac.vuw.ecs.swen225.gp21.persistency.*;
import nz.ac.vuw.ecs.swen225.gp21.recorder.Recorder;
import nz.ac.vuw.ecs.swen225.gp21.recorder.Recorder.Tuple;

// TODO: Auto-generated Javadoc
/**
 * GUI/Main class for Chap's Challenge.
 * 
 * @author Thien
 */

public class Main {
    
    /**
     * Starts the game.
     *
     */
	
	/**
	 * Main game
	 */
	public static Game g;
	
	/** Recorder. */
	public static Recorder r;
	
	/** Separator for file path. */
	public static final String fs = System.getProperty("file.separator");
	
	/** Auto boolean for replay. */
	public static boolean isAuto = false;
	
	/** Int for replay array. */
	protected static int rep = 0;
	
	/** Boolean to check if it is in replay mode. */
	private static boolean isReplayMode = false;
	
	/** Interval for sleep. */
	static int interval = 1000;
	

	/**
	 * Launch the application.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Set<AbstractChap> p = new HashSet<AbstractChap>();
		g = new Game();
		r = new Recorder();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppWindow window = new AppWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}			
			}
		});
		
		Thread domain = new Thread() {
		    public void run() { 	
		    	Board bm = null;
//				System.out.println("run happened");
		    	while(true) {
		    		if (!g.isPaused()) {
		    		bm = g.getBoard();
		    		if (bm != null) {
		    			for(AbstractChap v: bm.getVariants()) {
		    				if (v instanceof VariantChap) {
		    					v.move(null, null, bm);
		    					AppWindow.gamePane.updateMove(g);
		    					if(!g.getBoard().chapIsAlive()) {
		    						String[] buttons = {"Menu", "Reset Level", "Quit"};

		    					    int rc = JOptionPane.showOptionDialog(AppWindow.frame, "Died!", "Confirmation",
		    					        JOptionPane.INFORMATION_MESSAGE, JOptionPane.WARNING_MESSAGE, null, buttons, buttons[2]);
		    					    if (rc == -1) {
		    					    	
		    					    }else if (rc == 0) {
		    					    	   AppWindow.gamePane.setVisible(false);
		    					    	   AppWindow.menu.setVisible(true);
		    					    	   Main.g.setStarted(false);
		    					    	   Main.g.setWin(false);
		    					    }else if (rc == 1) {
		    					    	  g.resetLevel();
		    					    }else if (rc == 2) {
		    					    	int a = JOptionPane.showConfirmDialog(AppWindow.frame, "Are you sure you want to exit?");
		    							if(a == JOptionPane.YES_OPTION) System.exit(0);
		    					    }
		    					}
		    				} else throw new IllegalArgumentException("Non-variant in a set of Variants");
		    			}
		    		}
		    		}
		    		try {
						sleep(1111);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		    }  
		};

		Thread clock = new Thread() {
		    public void run() {    	
		    	while(true) {
		    		Countdown.getCountDown(g);
		    		if (!g.isPaused() && g.isStarted() && !g.isWon() && !isReplayMode) {
		    			AppWindow.gamePane.updateTime(g);
		    			g.checkTime();
		    		}
		    		try {
						sleep(interval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		if (isReplayMode && r.finishedLoading && isAuto) {
		    			runReplay();
		    			if (! (rep >= r.chapMoves.size()))  rep++;
		    		}
		    	}
		    	  
		    }  	  
		};

		
		clock.start();
		domain.start();
		
		askResume();
	}
	
	/**
	 * Ask resume.
	 */
	private static void askResume() {
		 String[] buttons = {"Yes", "No"};
		 
		 int rc = -1;
		 if (SaveState.loadSaveState() == 0) {
			 return;
		 }else if (SaveState.loadSaveState() == 1) {
			 rc = JOptionPane.showOptionDialog(AppWindow.frame, "Do you want to resume last level?", "Confirmation",
				 JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
		 }else if (SaveState.loadSaveState() == 2) {
			 rc = JOptionPane.showOptionDialog(AppWindow.frame, "Do you want to resume last saved game?", "Confirmation",
					 JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
		 }
		 if (rc == 0) {
		     if (SaveState.loadSaveState() == 1) {
		    	 startGame(SaveState.level);
		     }else if (SaveState.loadSaveState() == 2) {
		    	 loadGame(SaveState.fileName);
		     }
		}
	}
	
	/**
	 * A method to pause the replay.
	 */
	private static void pauseReplay() {
		isAuto = false;
		 String[] buttons = {"Menu", "Play again"};

		 int rc = JOptionPane.showOptionDialog(AppWindow.frame, "Replay over.", "Confirmation",
			 JOptionPane.YES_NO_OPTION, 0, null, buttons, buttons[1]);
		 if (rc == -1) {
			 AppWindow.menu.setVisible(true);
			 AppWindow.gamePane.setVisible(false);
			 AppWindow.gamePane.repPane.setVisible(false);
			 isReplayMode = false;
		}else if (rc == 0) {
			 AppWindow.menu.setVisible(true);
			 AppWindow.gamePane.setVisible(false);
			 AppWindow.gamePane.repPane.setVisible(false);
			 isReplayMode = false;
		}else if (rc == 1) {
   		  rep = 0;
   		  isAuto = true;
		}
	}
    
    /**
     * Start the game, load initial level.
     *
     * @param level the level
     */
    public static void startGame(int level) {
    	reset();
    	LoadGame l = null;
    	try {
    		String path = "levels" + fs;
    		if (level == 1) l = new LoadGame(path + "level1.xml");
    			else if (level == 2)  l = new LoadGame(path + "level2.xml");
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	// add null pointer check here
    	Countdown.setTimer(l.getTime());
    	g.setWin(false);
    	g.setStarted(true);
    	sleep(1000);
    	AppWindow.gamePane.updateLevel(g.getBoard().getCurrentLevel());
    	AppWindow.menu.setVisible(false);
    	AppWindow.gamePane.setVisible(true);	
	}  
    
    /**
     * A method to pause and resume the game.
     *
     * @param n the n
     */
    public static void resumeNPauseGame(boolean n) {
    	 if (!g.isStarted()) return;
    	 if (n) {
    		 g.updatePause(true);
    		 Countdown.setInitPauseTime();
    	 }
    	 else {
    		 g.updatePause(false);
    		 Countdown.updateTotalPauseTime();
    	 }
    	 AppWindow.gamePane.pause(n);
	} 
     
	/**
	 * Exit no save.
	 */
	public static void exitNoSave() {
		  try {
			SaveState.saveSaveState(1,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  System.exit(0);
	}
	
	
	/**
	 * Start the game, load choosen file.
	 */
	public static void loadGame() {
		reset() ;
		JFileChooser fileChooser = new JFileChooser();
		String path = "levels" + fs + "SavedGame";
		fileChooser.setCurrentDirectory(new File(path));
		int result = fileChooser.showOpenDialog(AppWindow.frame);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    r.loadRecord(selectedFile.getName());
		    LoadGame l = null;
		    try {
				 l = new LoadGame(selectedFile.getPath());
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    Countdown.setTimer(l.getTime());
	    	g.setWin(false);
	    	g.setStarted(true);
	    	sleep(1000);
	    	AppWindow.gamePane.updateLevel(g.getBoard().getCurrentLevel());
	    	AppWindow.menu.setVisible(false);
	    	AppWindow.gamePane.setVisible(true);	
		} else {
			if (g.isStarted()) resumeNPauseGame(false);
		}
	}
	
	/**
	 * Load game.
	 *
	 * @param filename the filename
	 */
	public static void loadGame(String filename) {
			reset() ;
			String path = "levels" + fs + "SavedGame" + fs + filename;
		    r.loadRecord(filename);
		    LoadGame l = null;
		    try {
				 l = new LoadGame(path);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    Countdown.setTimer(l.getTime());
	    	g.setWin(false);
	    	g.setStarted(true);
	    	sleep(1000);
	    	AppWindow.gamePane.updateLevel(g.getBoard().getCurrentLevel());
	    	AppWindow.menu.setVisible(false);
	    	AppWindow.gamePane.setVisible(true);	
		
	}
	
	
	
	/**
	 * Reset all variable in the game.
	 */
	public static void reset() {
		isReplayMode = false;
		r.chapMoves.clear();
    	AppWindow.gamePane.repPane.setVisible(false);
    	AppWindow.gamePane.pause(false);
    	g.updatePause(false);
    	
    	interval = 1000;
    	Countdown.resetCountDown();
    	Countdown.setStartTime();  
	}
	
	
	
	/**
	 * Call replay.
	 */
	public static void replay() {
			AppWindow.menu.setVisible(false);
			AppWindow.gamePane.setVisible(true);	
			isReplayMode = true;
	}
	
	
	
	/**
	 * Repaint all the board with given index in the recorder array.
	 */
	public static void runReplay() {
		    if (rep >= r.chapMoves.size())pauseReplay();
		    else {
			Tuple t = r.chapMoves.get(rep);
			Board newBoard = t.getBoard().clone();
			newBoard.getRegularChap().setDirection(t.getDirection());
			newBoard.getRegularChap().setPosition(t.getPosition());
			newBoard.setInventory(t.getInventory());
			AppWindow.gamePane.updateLevel(r.level);
			AppWindow.gamePane.updateReplay(newBoard);
		    }
	}
	
	/**
	 * Exit and save the game.
	 */
	public static void exitAndSave() {
		  try {
			  // get current level from board rather than game?
			SaveGame newSave = new SaveGame();
			newSave.save();
			SaveState.saveSaveState(2, newSave.fileName);
			r.saveRecord(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  System.exit(0);
	}
	
	/**
	 * Sleep.
	 *
	 * @param s the s
	 */
	private static void sleep(int s) {
		try {
			Thread.sleep(s);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
