package nz.ac.vuw.ecs.swen225.gp21.domain;

/**
 * An Actor is a game character that moves around, like Chap, the hero of the game. 
 * Variants are added in level 2 that interacts with Chap (ie, by killing Chap).
 * Unlike Chap, Variants will move around on their own (in the same direction as long
 * as there's a freetile in fron of them, if not it would turn clockwise until it 
 * sees a freetile), and are not directed by user input. 
 * 
 * Must use IllegalArgumentException and IllegalStateException 
 * to enforce preconditions, and Java asserts for postconditions checks. 
 * 
 * @author Victor Tam
 */

public abstract class AbstractChap {
    /**
     * Position of the Actor.
     */
    protected Position pos;
   
    /**
     * Direction the Actor is facing.
     */
    protected Direction dir;
    
    /**
     * Constructor of a new Actor, who starts by facing down.
     *
     * @param p starting position
     */
    public AbstractChap(Position p) {
        pos = p;
        dir = Direction.DOWN;
    }
    /**
     * Changing Actor's position and direction. Using IllegalArgumentException
     * and IllegalStateException to enforce preconditions.
     * 
     * @param p new position
     * @param d new direction
     * @param bm current Board(Maze)
     */    
    public abstract void move(Position p, Direction d, Board bm) ;


    /**
     * Get a 1-character code that represents the Actor.
     *
     * @return code
     */
    public abstract String actorCode();

    /**
     * Getter method for the Actor's position on the board.
     *
     * @return actor's position on board
     */
    public Position getPosition() {
        return pos;
    }
    
    /**
     * Getter and Setter methods for the Actor's position on the board.
     *
     * @return the direction the actor is facing currently
     */    
    public Direction getDirection() {
        return dir;
    }
	
	/**
	 * Sets the direction.
	 *
	 * @param d the new direction
	 */
	public void setDirection(Direction d) {
		dir = d;
	}
	
	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(Position position) {
		pos = position;
	}
}
