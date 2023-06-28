package nz.ac.vuw.ecs.swen225.gp21.app;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * Panel for the closing screen.
 */
public class ClosingOptionPane extends WindowAdapter{
	
	/** The frame. */
	JFrame f;
	
	/**
	 * Instantiates a new closing option pane.
	 *
	 * @param frame the frame
	 */
	ClosingOptionPane(JFrame frame){
		this.f = frame;
	}

	/**
	 * Closes the window.
	 *
	 * @param e The event to check whether player wants to close.
	 */
	public void windowClosing(WindowEvent e) {
		int a = JOptionPane.showConfirmDialog(f,"Are you sure you want to exit?");
		if(a == JOptionPane.YES_OPTION) {
			try {
				SaveState.saveSaveState(0,null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(0);
		}
	}
}