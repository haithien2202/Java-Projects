package nz.ac.vuw.ecs.swen225.gp21.app;

import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import nz.ac.vuw.ecs.swen225.gp21.domain.Direction;
import nz.ac.vuw.ecs.swen225.gp21.persistency.SaveGame;

/**
 * The Class AppWindow.
 */
public class AppWindow {

	/** Height of the frame. */
	public static int appHeight = 450;

	/** Width of the frame. */
	public static int appWidth = 570;

	/** Main frame. */
	public static JFrame frame = new JFrame();
	
	/** Menu panel. */
	public static MenuPane menu;
	
	/** Game panel. */
	public static GamePane gamePane;
	
	/** Menu bar. */
	private static MenuBar menubar;
	
	/**
	 * Help Panel. Display instruction.
	 */
	public static HelpFrame helpFrame = new HelpFrame();
	
	/** The help. */
	private static JMenuItem help = new JMenuItem(new AbstractAction("Help") {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			helpFrame.display();
		}
	});

	/**
	 * Create the application.
	 */
	public AppWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame.setBounds(100, 100, appWidth, appHeight + 20);
		frame.addWindowListener(new ClosingOptionPane(frame));
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.requestFocus();
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent comp) {
				appHeight = frame.getHeight();
				appWidth = frame.getWidth();
				frame.getContentPane().setBounds(0, 0, appWidth, appHeight + 100);
				gamePane.scale();
				menu.scale();
			}
		});

		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_X && e.isControlDown()) {
					Main.resumeNPauseGame(true);
					String[] buttons = { "Save game", "Yes", "No" };

					int rc = JOptionPane.showOptionDialog(AppWindow.frame, "Your current game will be lost! Quit?",
							"Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, buttons,
							buttons[2]);
					if (rc == -1)
						Main.resumeNPauseGame(false);
					else if (rc == 0)
						Main.exitAndSave();
					else if (rc == 1)
						Main.exitNoSave();
					else if (rc == 2)
						Main.resumeNPauseGame(false);
				} else if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) {
					if (Main.g.isStarted() == true && !Main.g.isWon()) {
						Main.resumeNPauseGame(true);
						String[] buttons = { "Yes", "No" };

						int rc = JOptionPane.showOptionDialog(AppWindow.frame, "Save and quit?", "Confirmation",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[1]);
						if (rc == -1)
							Main.resumeNPauseGame(false);
						else if (rc == 0)
							Main.exitAndSave();
						else if (rc == 1)
							Main.resumeNPauseGame(false);
					}
				} else if (e.getKeyCode() == KeyEvent.VK_R && e.isControlDown()) {
					if (!Main.g.isStarted()) {
						Main.loadGame();
					} else {
						Main.resumeNPauseGame(true);
						String[] buttons = { "Yes", "No", "Cancel" };

						int rc = JOptionPane.showOptionDialog(AppWindow.frame, "Do you want to save current game?",
								"Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons,
								buttons[2]);
						if (rc == -1)
							Main.resumeNPauseGame(false);
						else if (rc == 0) {
							try {
								SaveGame s = new SaveGame();
								s.save();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							Main.loadGame();
						} else if (rc == 1)
							Main.loadGame();
						else if (rc == 2)
							Main.resumeNPauseGame(false);
					}
				} else if (e.getKeyCode() == KeyEvent.VK_1 && e.isControlDown()) {
					Main.startGame(1);
				} else if (e.getKeyCode() == KeyEvent.VK_2 && e.isControlDown()) {
					Main.startGame(2);
				} else if (e.getKeyCode() == KeyEvent.VK_SPACE && Main.g.isStarted()) {
					Main.resumeNPauseGame(true);
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && Main.g.isStarted()) {
					Main.resumeNPauseGame(false);
				} else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
					if (!Main.g.isStarted() || Main.g.isPaused() || Main.g.isWon() || !Main.g.getBoard().chapIsAlive())
						return;
					Main.g.getBoard().moveRegularChap(Direction.LEFT);
					gamePane.updateMove(Main.g);
					check();
				} else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (!Main.g.isStarted() || Main.g.isPaused() || Main.g.isWon() || !Main.g.getBoard().chapIsAlive())
						return;
					Main.g.getBoard().moveRegularChap(Direction.DOWN);
					gamePane.updateMove(Main.g);
					check();
				} else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if (!Main.g.isStarted() || Main.g.isPaused() || Main.g.isWon() || !Main.g.getBoard().chapIsAlive())
						return;
					Main.g.getBoard().moveRegularChap(Direction.RIGHT);
					gamePane.updateMove(Main.g);
					check();
				} else if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
					if (!Main.g.isStarted() || Main.g.isPaused() || Main.g.isWon() || !Main.g.getBoard().chapIsAlive())
						return;
					Main.g.getBoard().moveRegularChap(Direction.UP);
					gamePane.updateMove(Main.g);
					check();
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		menu = new MenuPane();
		gamePane = new GamePane();
		frame.getContentPane().add(menu);
		gamePane.setVisible(false);
		frame.getContentPane().add(gamePane);
		menubar = new MenuBar();
		frame.setJMenuBar(menubar);
		frame.add(help);
	}

	/**
	 * Check if the is win.
	 */
	private void check() {
		if (Main.g.getBoard().checkIfOnExit()) {
			Main.g.setWin(true);
			String[] buttons = { "Menu", "Next level", "Save replay", "Quit" };

			int rc = JOptionPane.showOptionDialog(frame, "You Win!", "Confirmation", JOptionPane.INFORMATION_MESSAGE,
					JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[2]);
			if (rc == -1) {

			} else if (rc == 0) {
				gamePane.setVisible(false);
				menu.setVisible(true);
				Main.g.setStarted(false);
				Main.g.setWin(false);
			} else if (rc == 1) {
				if (Main.g.getBoard().getCurrentLevel() < 2)
					Main.startGame(Main.g.getBoard().getCurrentLevel() + 1);
				else
					Main.startGame(Main.g.getBoard().getCurrentLevel());
				Main.g.setWin(false);
			} else if (rc == 2) {
				Main.r.saveRecord(true);
				askPlayerForOption();

			} else if (rc == 3) {
				int a = JOptionPane.showConfirmDialog(AppWindow.frame, "Are you sure you want to exit?");
				if (a == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		}
		if (Main.g.getBoard().checkIfOnInfo() != null) {
			helpFrame.display();
			if (Main.g.isStarted())
				Main.resumeNPauseGame(true);
		}
	}

	/**
	 * Confirm options for player.
	 */
	private void askPlayerForOption() {
		Main.g.setWin(true);
		String[] buttons = { "Menu", "Next level", "Quit" };

		int rc = JOptionPane.showOptionDialog(frame, "You Win!", "Confirmation", JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[2]);
		if (rc == -1) {

		} else if (rc == 0) {
			gamePane.setVisible(false);
			menu.setVisible(true);
			Main.g.setStarted(false);
			Main.g.setWin(false);
		} else if (rc == 1) {
			if (Main.g.getBoard().getCurrentLevel() < 2)
				Main.startGame(Main.g.getBoard().getCurrentLevel() + 1);
			else
				Main.startGame(Main.g.getBoard().getCurrentLevel());
			Main.g.setWin(false);
		} else if (rc == 2) {
			int a = JOptionPane.showConfirmDialog(AppWindow.frame, "Are you sure you want to exit?");
			if (a == JOptionPane.YES_OPTION)
				System.exit(0);
		}
	}

}
