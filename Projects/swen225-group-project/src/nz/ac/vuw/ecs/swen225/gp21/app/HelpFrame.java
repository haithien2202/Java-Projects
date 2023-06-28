package nz.ac.vuw.ecs.swen225.gp21.app;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.*;

// TODO: Auto-generated Javadoc
/**
 * The frame for the help dropdown menu.
 */
@SuppressWarnings("serial")
public class HelpFrame extends JFrame{
	
	/** The scroll. */
	JScrollPane scroll;
	
	/** The area. */
	JTextArea area;
	
	/** The pane. */
	JTextPane pane;
	
	/**
	 * Instantiates a new help frame.
	 */
	public HelpFrame() {
        super("Help");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	Main.resumeNPauseGame(false);
            }
        });
	}

	/**
	 * Displays the full details of the rules.
	 */
	public void display() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("<html>Structure\r<br>"
				+ "\r<br>"
				+ "How to play:\r<br>"
				+ "	- Collect coloured keys to open doors which match with the key colour.\r<br>"
				+ "	- Collect all chips to unlock the exit door.\r<br>"
				+ "	- Avoid the variants in red shirt.\r<br>"
				+ "	- Extra of 10 seconds is added when collecting clock tile.\r<br>"
				+ "Short key:\r<br>"
				+ "	- Ctrl X: exit the game, the current game state will be lost, the next time the game is started, it will resume from the last unfinished level.\r<br>"
				+ "	- Ctrl S: exit the game, saves the game state, game will resume next time the application will be started.\r<br>"
				+ "	- Ctrl R: resume a saved game -- this will pop up a file selector to select a saved game to be loaded.\r<br>"
				+ "	- Ctrl 1: start a new game at level 1.\r<br>"
				+ "	- Ctrl 2: start a new game at level 2.\r<br>"
				+ "	- SPACE:  pause the game.\r<br>"
				+ "	- ESC: resume the game.\r<br>"
				+ "	- UP, DOWN, LEFT, RIGHT ARROWS/ A,S,D,W: move Chap.\r<br>"
				+ "</html>");
		
		JLabel label = new JLabel(sb.toString());
		JScrollPane scroll = new JScrollPane(label);
		label.setSize(AppWindow.appWidth-30, AppWindow.appHeight - 100);
		this.add(scroll);
		this.setSize(AppWindow.appWidth-30, AppWindow.appHeight - 100);
		this.setVisible(true);
		this.setResizable(false);
	}
}
