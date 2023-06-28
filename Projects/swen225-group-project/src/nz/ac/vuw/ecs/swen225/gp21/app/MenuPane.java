package nz.ac.vuw.ecs.swen225.gp21.app;

import java.awt.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuPane.
 */
@SuppressWarnings("serial")
public class MenuPane extends JPanel {
	
	/** The src img. */
	private Image srcImg = null;
	
	/** The Background. */
	private JLabel Background = new JLabel("");
	
	/**
	 * Instantiates a new menu pane.
	 */
	public MenuPane() {
	int width = AppWindow.appWidth-16;
	int height =  AppWindow.appHeight-39;
	this.setBounds(0, 0, width, height);
	this.setLayout(null);
	
	
    try {
    	String fs = System.getProperty("file.separator");
		String path = "src" + fs + "nz" + fs + "ac" + fs + "vuw" + fs + "ecs" + fs + "swen225" + fs + "gp21" + fs + "app" + fs;
        srcImg = ImageIO.read(new File(path + "Chip's_Challenge.jpg"));
    } catch (IOException ex) {
    }
	Background.setBounds(0, 0, width, height);
	Background.setIcon(new ImageIcon(srcImg.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT)));
	this.add(Background);
	} 
	
	/**
	 * Scale.
	 */
	public void scale() {
		int width = AppWindow.appWidth-16;
		int height =  AppWindow.appHeight-60;
		this.setBounds(0, 0, width, height);
		Background.setBounds(0, 0, width, height);
		Background.setIcon(new ImageIcon(srcImg.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT)));
		this.repaint();
	}
}
