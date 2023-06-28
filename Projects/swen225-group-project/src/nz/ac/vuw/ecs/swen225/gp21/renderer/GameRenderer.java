package nz.ac.vuw.ecs.swen225.gp21.renderer;

import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nz.ac.vuw.ecs.swen225.gp21.domain.*;

// TODO: Auto-generated Javadoc
/**
 * GameRenderer JPanel to display a 2D representation of the maze.
 *
 * @author Alex Eastlake, 300544079
 */
@SuppressWarnings("serial")
public class GameRenderer extends JPanel {
	
	/**
	 * Board to display.
	 */
	private Board board;
	
	/**
	 * Labels for displaying icons.
	 */
	private JLabel[] labels;
	
	/**
	 * RendererSounds instance for playing sound effects.
	 */
	private RendererSounds sounds;
	
	/**
	 * Stores previous Chap position to check if Chap moved on update.
	 */
	private Position prevChapPos;
	
	/**
	 * Stores previous number of treasures to check if treasure was collected on update.
	 */
	private int prevNumTreasures;
	
	/**
	 * Stores previous number of keys to check if key was collected/used on update.
	 */
	private int prevNumKeys;
	
	/**
	 * Creates a GameRenderer JPanel.
	 *
	 * @param board Board to display
	 */
	public GameRenderer(Board board) {
		this.board = board;
		this.labels = new JLabel[81];
		this.sounds = new RendererSounds();
		this.prevNumTreasures = -1;
		this.prevNumKeys = -1;
		
		for (int i = 0; i < labels.length; i++) {
			this.labels[i] = new JLabel();
			this.add(labels[i]);			
		}
		
		this.setLayout(new GridLayout(9, 9));
		this.draw();
		this.setVisible(true);
	}
	
	/**
	 * Updates the JPanel completely according to the given board.
	 *
	 * @param board Board to update to
	 */
	public void update(Board board) {
		this.board = board;
		this.draw();
		
		// Checks if Chap moved for sound effect
		if (this.prevChapPos == null) {
			this.prevChapPos = board.getRegularChapPos();
		} else if (!this.prevChapPos.equals(this.board.getRegularChapPos())) {
			this.sounds.playFootstep();
			this.prevChapPos = board.getRegularChapPos();
		}
		
		// Checks if Chap collected treasure or collected/used a key for sound effect
		if (this.prevNumTreasures == -1 || this.prevNumKeys == -1) {
			this.prevNumTreasures = board.getNumTreasures();
			this.prevNumKeys = board.getInventoryKT().size();
		} else if (this.prevNumTreasures != board.getNumTreasures() || this.prevNumKeys != board.getInventoryKT().size()) {
			this.sounds.playInteract();
			this.prevNumTreasures = board.getNumTreasures();
			this.prevNumKeys = board.getInventoryKT().size();
		}
		
		this.repaint();
	}
	
	/**
	 * Draws the current board onto this GameRenderer JPanel in a 9x9 focus area around the player.
	 */
	public void draw() {
		if (this.board == null) {
			return;
		}
		
		int currentViewY = board.getRegularChapPos().getY() - 4;
		
		for (int i = 0; i < 9; i++) {
			int currentViewX = board.getRegularChapPos().getX() - 4;
			
			for (int j = 0; j < 9; j++) {
				AbstractTile currentTile;
				Position currentPos;;
				
				if (currentViewX >= 0 && currentViewX < board.getCurrentBoard().length && currentViewY >= 0 && currentViewY < board.getCurrentBoard().length) {
					currentPos = new Position(currentViewX, currentViewY);
					currentTile = board.getTile(currentPos);
					boolean isVariant = false;
					
					for (VariantChap variant : board.getVariants()) {
						if (currentPos.equals(variant.getPosition())) {
							isVariant = true;
							Direction dir = variant.getDirection(); 
							
							switch (dir) {
							case RIGHT:
								labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.variantChapIconR));
								break;
							case LEFT:
								labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.variantChapIconL));
								break;
							case UP:
								labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.variantChapIconU));
								break;
							case DOWN:
								labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.variantChapIconD));
								break;
							}
							
							break;
						}
					}
					
					if (isVariant) {
						currentViewX++;
						continue;
					}
					
					// Checks if currentPos is where the player is
					if (currentPos.equals(board.getRegularChapPos())) {
						Direction dir = board.getRegularChap().getDirection();
						
						switch (dir) {
						case RIGHT:
							labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.regularChapIconR));
							break;
						case LEFT:
							labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.regularChapIconL));
							break;
						case UP:
							labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.regularChapIconU));
							break;
						case DOWN:
							labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.regularChapIconD));
							break;
						}
						
						currentViewX++;
						continue;
					}
				} else {
					currentTile = new WallTile(0, 0);
				}
				
				currentViewX ++;
				
				// Checks the type of currentTile
				if (currentTile instanceof FreeTile) {
					labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.freeTileIcon));
				} else if (currentTile instanceof WallTile) {
					labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.wallTileIcon));
				} else if (currentTile instanceof InfoTile) {
					labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.infoTileIcon));
				} else if (currentTile instanceof ExitTile) {
					labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.exitTileIcon));
				} else if (currentTile instanceof TreasureTile) {
					labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.treasureTileIcon));
				} else if (currentTile instanceof ExtraTile) {
					labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.extraTileIcon));
				} else if (currentTile instanceof ExitLockTile) {
					labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.exitLockTileIcon));
				} else if (currentTile instanceof LockedDoorTile) {
					if (((LockedDoorTile) currentTile).getColour() == KeyDoorColour.CYAN) {
						labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.lockedDoorTileCyanIcon));
					} else if (((LockedDoorTile) currentTile).getColour() == KeyDoorColour.GREEN) {
						labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.lockedDoorTileGreenIcon));
					} else if (((LockedDoorTile) currentTile).getColour() == KeyDoorColour.RED) {
						labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.lockedDoorTileRedIcon));
					}
				} else if (currentTile instanceof KeyTile) {
					if (((KeyTile) currentTile).getColour() == KeyDoorColour.CYAN) {
						labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.keyTileCyanIcon));
					} else if (((KeyTile) currentTile).getColour() == KeyDoorColour.GREEN) {
						labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.keyTileGreenIcon));
					} else if (((KeyTile) currentTile).getColour() == KeyDoorColour.RED) {
						labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.keyTileRedIcon));
					}
				} else {
					// If no known tiles match
					labels[i * 9 + j].setIcon(new ImageIcon(RendererImages.unknownTileIcon));
				}
			}
			currentViewY++;
		}
	}
}