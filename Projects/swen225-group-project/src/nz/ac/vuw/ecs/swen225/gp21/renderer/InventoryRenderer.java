package nz.ac.vuw.ecs.swen225.gp21.renderer;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nz.ac.vuw.ecs.swen225.gp21.domain.*;

// TODO: Auto-generated Javadoc
/**
 * InventoryRenderer JPanel to display the keys currently held by the player.
 *
 * @author Alex Eastlake, 300544079
 */
@SuppressWarnings("serial")
public class InventoryRenderer extends JPanel {
	
	/**
	 * Field for a list of keys held by the player.
	 */
	private List<KeyTile> keys;
	
	/**
	 * Labels for displaying icons.
	 */
	private JLabel[] labels;
	
	/**
	 * Creates an InventoryRenderer JPanel.
	 */
	public InventoryRenderer() {
		labels = new JLabel[8];
		
		for (int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel(new ImageIcon(RendererImages.freeTileIcon));
			this.add(labels[i]);
		}
		
		this.setLayout(new GridLayout(2, 4));
		this.setVisible(true);
	}
	
	/**
	 * Updates the inventory JPanel according to the given list of keys.
	 *
	 * @param keys Keys the player currently has
	 */
	public void update(List<KeyTile> keys) {
		this.keys = keys;
		this.draw();
		this.repaint();
	}
	
	/**
	 * Draws the current inventory of keys held by the player onto a 4x2 grid of squares.
	 */
	public void draw() {
		if (keys == null) {
			return;
		}
		
		for (int i = 0; i < labels.length; i++) {
			if (i < keys.size()) {
				KeyDoorColour col = keys.get(i).getColour();
				
				switch (col) {
				case CYAN:
					labels[i].setIcon(new ImageIcon(RendererImages.keyTileCyanIcon));
					break;
				case GREEN:
					labels[i].setIcon(new ImageIcon(RendererImages.keyTileGreenIcon));
					break;
				case RED:
					labels[i].setIcon(new ImageIcon(RendererImages.keyTileRedIcon));
					break;
				}
			} else {
				labels[i].setIcon(new ImageIcon(RendererImages.freeTileIcon));
			}
		}
	}
}