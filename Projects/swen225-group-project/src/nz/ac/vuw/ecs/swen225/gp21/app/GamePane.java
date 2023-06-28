package nz.ac.vuw.ecs.swen225.gp21.app;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import nz.ac.vuw.ecs.swen225.gp21.domain.Board;
import nz.ac.vuw.ecs.swen225.gp21.domain.Game;
import nz.ac.vuw.ecs.swen225.gp21.renderer.*;

// TODO: Auto-generated Javadoc
/**
 * The Class GamePane.
 */
public class GamePane extends JPanel{
	
	/** The Pause. */
	private JLabel Pause = new JLabel("");
	
	/** The time. */
	private JLabel time = new JLabel();
	
	/** The level. */
	private JLabel level = new JLabel();
	
	/** The chips. */
	private JLabel chips = new JLabel();
	
	/** The new game. */
	private GameRenderer newGame = null;
	
	/** The inventory. */
	private InventoryRenderer inventory = null;
	
	/** The src img. */
	private Image srcImg = null;
	
	/** The pause img. */
	private Image pauseImg = null;
	
	/** The Background. */
	private JLabel Background = new JLabel("");
	
	/** The rep pane. */
	public ReplayPane repPane = null;
	
	/**
	 * Instantiates a new game pane.
	 */
	public GamePane() {
		int width = AppWindow.appWidth-16;
		int height =  AppWindow.appHeight-39;
		this.setBounds(0, 0, width, height);
		this.setLayout(null);
		
		
	    try {
	    	String fs = System.getProperty("file.separator");
			String path = "src" + fs + "nz" + fs + "ac" + fs + "vuw" + fs + "ecs" + fs + "swen225" + fs + "gp21" + fs + "app" + fs;
	        srcImg = ImageIO.read(new File(path + "Background.png"));
	        pauseImg = ImageIO.read(new File(path + "pause.png"));
	    } catch (IOException ex) {
	    }
	    
		repPane = new ReplayPane();
		repPane.setVisible(false);
		this.add(repPane);
	    
		Background.setBounds(0, 0, width, height);
		Background.setIcon(new ImageIcon(srcImg.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT)));
			
		Pause.setBounds(width/2 - pauseImg.getWidth(this)/2, height/2 -  pauseImg.getHeight(this)/2, pauseImg.getWidth(this), pauseImg.getHeight(this));
		Pause.setIcon(new ImageIcon(pauseImg));
		Pause.setVisible(false);
		this.add(Pause);
		
		time.setBounds(-width*1/7, height*3/13, width, height);
		time.setFont(new Font("Dialog", Font.PLAIN, 30));
		time.setHorizontalAlignment(SwingConstants.RIGHT); 
		time.setForeground(Color.WHITE);
		this.add(time);
		
		chips.setBounds(-width*1/7, height*3/13, width, height);
		chips.setFont(new Font("Dialog", Font.PLAIN, 30));
		chips.setHorizontalAlignment(SwingConstants.RIGHT); 
		chips.setForeground(Color.WHITE);
		this.add(chips);
		
		level.setBounds(-width*1/7, height*1/13, width, height);
		level.setFont(new Font("Dialog", Font.PLAIN, 30));
		level.setHorizontalAlignment(SwingConstants.RIGHT); 
		level.setForeground(Color.WHITE);
		this.add(level);
		
		inventory = new InventoryRenderer();
		inventory.setBounds(width*14/16, height*17/22, (int)(width*5/16), (int) (height*4/20));
		this.add(inventory);
		
		newGame = new GameRenderer(null);
		newGame.setBounds(width/16, height/11, (int)(width*9.05/16), (int) (height*4.95/6));
		this.add(newGame);		
		this.add(Background);
		}
	
	/**
	 * Update the clock on to game panel.
	 *
	 * @param g the g
	 */
	public void updateTime(Game g) {
		if (g == null) time.setText("--");
		else time.setText(String.valueOf(g.getTimeLeft()));
		this.time.repaint();
	}
	

	/**
	 * Update move, redraw board, inventory and save the move into recorder.
	 *
	 * @param g the g
	 */
	public void updateMove(Game g) {
		this.newGame.update(g.getBoard());	
		this.inventory.update(g.getBoard().getInventoryKT());
		this.chips.setText(String.valueOf(g.getBoard().getNumTreasures()));
		Main.r.saveMove();
	}
	
	/**
	 * Similar with updateMove method, but without saving the move. This method is for replay.
	 *
	 * @param b the b
	 */
	public void updateReplay(Board b) {	
		this.newGame.update(b);
		this.inventory.update(b.getInventoryKT());
		this.chips.setText(String.valueOf(b.getNumTreasures()));
		repPane.repaint();
	}
	
	/**
	 * Update text level label.
	 *
	 * @param n the n
	 */
	public void updateLevel(int n) {
		this.level.setText(String.valueOf(n));
	}
	
	/**
	 * Set pause image.
	 *
	 * @param s the s
	 */
	public void pause(boolean s) {
		Pause.setVisible(s);
	}
	
	
	/**
	 * Scale all images when the window is resized.
	 */
	public void scale() {
		int width = AppWindow.appWidth-16;
		int height =  AppWindow.appHeight-60;
		this.setBounds(0, 0, width, height);
		Background.setBounds(0, 0, width, height);	
		repPane.reScale();
		Background.setIcon(new ImageIcon(srcImg.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT)));
		Pause.setBounds(width/2 - width/2/2, height/2 -  height/4/2, width/2, height/4);
		Pause.setIcon(new ImageIcon(pauseImg.getScaledInstance(width/2, height/4, java.awt.Image.SCALE_DEFAULT)));
		level.setBounds(-width*1/7, (int) (height*-4.9/17), width, height);
		level.setFont(new Font("Dialog", Font.PLAIN, height/11));
		chips.setBounds(-width*1/7, (int) (height*2.55/17), width, height);
		chips.setFont(new Font("Dialog", Font.PLAIN, height/11));
		time.setBounds(-width*1/7, -height*1/9, width, height);
		time.setFont(new Font("Dialog", Font.PLAIN, height/11));
		RendererImages.scale(newGame.getWidth() / 9, newGame.getHeight() / 9);
		inventory.setBounds((int) (width*11.07/16), (int) (height*15.7/22), (int)(width*4/16), (int) (height*3.55/20));
		inventory.draw();
		newGame.setBounds(width/16, height/11, (int)(width*9.05/16), (int) (height*4.95/6));
		newGame.draw();
		this.repaint();
	}

}
