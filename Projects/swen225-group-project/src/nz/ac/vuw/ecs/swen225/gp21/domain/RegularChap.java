package nz.ac.vuw.ecs.swen225.gp21.domain;

import nz.ac.vuw.ecs.swen225.gp21.app.AppWindow;

/**
 *  Chap can be moved by key strokes (up-right-down-left), his movement is 
 *  restricted by the nature of the tiles (for instance, cannot move into walls). 
 *  Note that the icon may depend on the current direction of movement.
 *
 * @author Victor Tam
 */
public class RegularChap extends AbstractChap {

    /**
     * Create a new Chap, default position is down with his face being in front and visible.
     *
     * @param p starting position
     */
    public RegularChap(Position p) {
        super(p);
    }


    /**
     * Actor code.
     *
     * @return the string
     */
    @Override
    public String actorCode() {
        return "@";
    }

	/**
	 * Move.
	 *
	 * @param p the p
	 * @param d the d
	 * @param bm the bm
	 */
	@Override
	public void move(Position p, Direction d, Board bm) {
		 
		 if (d.equals(Direction.UP)) {
        	 if (p.getX() != pos.getX())throw new IllegalArgumentException("X should be constant when moving up");
        	 if (p.getY() != pos.getY()-1) throw new IllegalArgumentException("Y should be -1 when moving up");
        } else if (d.equals(Direction.DOWN)) {
       	     if (p.getX() != pos.getX()) throw new IllegalArgumentException("X should be constant when moving down");
        	 if (p.getY() != pos.getY()+1) throw new IllegalArgumentException("Y should be +1 when moving down");
        } else if (d.equals(Direction.LEFT)) {
        	 if (p.getX() != pos.getX()-1) throw new IllegalArgumentException("X should be -1 when moving left");
        	 if (p.getY() != pos.getY()) throw new IllegalArgumentException("Y should be constant when moving left");      	
        } else if (d.equals(Direction.RIGHT)) {
        	 if (p.getX() != pos.getX()+1) throw new IllegalArgumentException("X should be +1 when moving right");
        	 if (p.getY() != pos.getY()) throw new IllegalArgumentException("Y should be constant when moving right");
        }     
		 
		// not supposed to throw an error
		if ( !(p.getX() < 0 || p.getX() > bm.getTextBoardToRestart().length || p.getY() < 0
				|| p.getY() > bm.getTextBoardToRestart()[pos.getX()].length()))
			pos = p;
        dir = d;
        if (AppWindow.gamePane != null) AppWindow.gamePane.repaint();
        
        // Check if regularChap has finished the level
        if(bm.checkIfOnExit()) {
        	System.out.println("This level is finished");
//        	Game.updatePause(true);
        	// Stop the level immediately
//        	JFrame jf = new JFrame();
//        	int ans = JOptionPane.showConfirmDialog(jf,"Do you want to move to the next level?");
//    		if(ans == JOptionPane.YES_OPTION) bm.basicLoad(bm.getTextBoard()); //Persistency?
//    		if(ans == JOptionPane.NO_OPTION) System.exit(0);
        }
        
        // Check if information should be printed
//        System.out.println(bm.checkIfOnInfo());

	}

}
