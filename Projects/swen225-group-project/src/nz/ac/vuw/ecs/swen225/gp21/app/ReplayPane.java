package nz.ac.vuw.ecs.swen225.gp21.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

// TODO: Auto-generated Javadoc
/**
 * The Class ReplayPane.
 */
public class ReplayPane extends JPanel {
	
	/** The speeddown. */
	JButton auto, next, previous, pause, speedup, speeddown;
	
	/**
	 * Instantiates a new replay pane.
	 */
	public ReplayPane() {
		int width = AppWindow.appWidth;
		int height = AppWindow.appHeight;
		this.setBounds(0, height*4/5, width, height/4);
		//this.setLayout(null);
		previous = new JButton("Previous Move");
		previous.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	Main.isAuto = false;
            	if (Main.rep > 0) Main.rep--;
            	Main.runReplay();
            }
        });
		this.add(previous);
		next = new JButton("Next Move");
		next.setSize(previous.getWidth(), previous.getHeight());
		next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (Main.rep < Main.r.chapMoves.size()) Main.rep++;
            	Main.runReplay();
            }
        });
		this.add(next);
		auto = new JButton("Auto");
		auto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	Main.isAuto = true;
            }
        });
		this.add(auto);
		pause = new JButton("Pause");
		pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.isAuto = false;
            }
        });
		this.add(pause);
		speedup = new JButton("+ Speed");
		speedup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Main.interval > 400) Main.interval -= 50;
            }
        });
		this.add(speedup); 
		speeddown = new JButton("- Speed");
		speeddown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Main.interval < 2000) Main.interval += 50;
            }
        });
		this.add(speeddown); 
		this.setOpaque(false);
	}
	
	/**
	 * Re scale.
	 */
	public void reScale() {
		int width = AppWindow.appWidth;
		int height = AppWindow.appHeight;
		this.setBounds(0, height*4/5, width, height/4);
	}
}
