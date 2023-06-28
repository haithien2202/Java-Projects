package nz.ac.vuw.ecs.swen225.gp21.domain;

import java.util.Objects;

/**
 * Position of Actor or Tile on board.
 *
 * @author Victor Tam
 */
public final class Position {
	
	/** Column aka x-coordinate. */
	private final int x;

	/** Row aka y-coordinate. */
	private final int y;

	/**
	 * Construct a new position.
	 *
	 * @param x column/x-coordinate
	 * @param y row/y-coordinate
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter for column.
	 *
	 * @return x-coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter for row.
	 *
	 * @return y-coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Equals.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Position position = (Position) o;
		return x == position.x && y == position.y;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "(x: " + x + " , y:" + y + " )";
	}
}
