package nz.ac.vuw.ecs.swen225.gp21.domain;

/**
 * Enumerates which directions we can travel from current position.
 *
 * @author Victor Tam
 */
public enum Direction {

	/** The up. */
	UP, /** The right. */
 RIGHT, /** The down. */
 DOWN, /** The left. */
 LEFT;

	/**
	 * Get new position from given position and direction.
	 *
	 * @param p original position
	 * @return new position
	 */
	public Position movePosition(Position p) {
		if (this == UP) return new Position(p.getX(), p.getY()-1);
		if (this == RIGHT) return new Position(p.getX()+1, p.getY());
		if (this == DOWN) return new Position(p.getX(), p.getY()+1);
		if (this == LEFT) return new Position(p.getX()-1, p.getY());
		throw new IllegalArgumentException("Dead code: Invalid Direction");
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return name();
	}
}