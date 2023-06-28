package nz.ac.vuw.ecs.swen225.gp21.domain;

import nz.ac.vuw.ecs.swen225.gp21.app.AppWindow;

/**
 * The secondary actor used in level 2. When RegularChap shares a tile with a
 * variant, RegularChap dies and the level restarts.
 *
 * @author Victor Tam
 */
public class VariantChap extends AbstractChap {
	
	/**
	 * Make a new Secondary actor with a specific path.
	 *
	 * @param p starting position
	 */
	public VariantChap(Position p) {
		super(p);
	}

	/**
	 * Actor code.
	 *
	 * @return the string
	 */
	@Override
	public String actorCode() {
		char c = 0;
		if(dir==Direction.UP) c= '^';
		else if(dir==Direction.RIGHT) c= '>';
		else if(dir==Direction.DOWN) c= 'v';
		else if(dir==Direction.LEFT) c= '<';

		if (c==0) throw new IllegalArgumentException("Invalid code for Variant");
		return Character.toString(c);
	}

	/**
	 * Move.
	 *
	 * @param notUsedPos the not used pos
	 * @param notUsedDir the not used dir
	 * @param bm the bm
	 */
	@Override
	public void move(Position notUsedPos, Direction notUsedDir, Board bm) {
		Direction newDir = this.dir;
		AbstractTile newTile = bm.getNeighbouringTile(this.pos, newDir);
		Position newPos=null;
		if(newTile!=null) {
			newPos = newTile.getPosition();
		}
		while (!(newTile instanceof FreeTile)) {
			if (newDir.ordinal() == Direction.values().length-1) {
				newDir = Direction.values()[0];
			} else {
				newDir = Direction.values()[newDir.ordinal() + 1];
			}
			newTile = bm.getNeighbouringTile(this.pos, newDir);
			if(newTile!=null) {
				newPos = newTile.getPosition();
			}
		}

		//precondition check to see if board or game logic is faulty
//		if (newDir.equals(Direction.UP)) {
//			if (newPos.getX() != this.pos.getX())
//				throw new IllegalArgumentException("X should be constant when moving up");
//			if (newPos.getY() != this.pos.getY() - 1)
//				throw new IllegalArgumentException("Y should be -1 when moving up");
//		} else if (newDir.equals(Direction.DOWN)) {
//			if (newPos.getX() != this.pos.getX())
//				throw new IllegalArgumentException("X should be constant when moving down");
//			if (newPos.getY() != this.pos.getY() + 1)
//				throw new IllegalArgumentException("Y should be +1 when moving down");
//		} else if (newDir.equals(Direction.LEFT)) {
//			if (newPos.getX() != this.pos.getX() - 1)
//				throw new IllegalArgumentException("X should be -1 when moving left");
//			if (newPos.getY() != this.pos.getY())
//				throw new IllegalArgumentException("Y should be constant when moving left");
//		} else if (newDir.equals(Direction.RIGHT)) {
//			if (newPos.getX() != this.pos.getX() + 1)
//				throw new IllegalArgumentException("X should be +1 when moving right");
//			if (newPos.getY() != this.pos.getY())
//				throw new IllegalArgumentException("Y should be constant when moving right");
//		}

		// not supposed to throw an error
		if ( !(newPos.getX() < 0 || newPos.getX() > bm.getTextBoardToRestart()[0].length() 
				|| newPos.getY() < 0 || newPos.getY() > bm.getTextBoardToRestart().length)) {
			this.dir = newDir;
			this.pos = newPos;
		}

        if (AppWindow.gamePane != null) AppWindow.gamePane.repaint();
	}
}
