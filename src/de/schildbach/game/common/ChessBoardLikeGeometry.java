/*
 * Copyright 2001-2011 the original author or authors.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.schildbach.game.common;

import java.util.HashSet;
import java.util.Set;

import de.schildbach.game.Board;
import de.schildbach.game.Coordinate;

/**
 * @author Andreas Schildbach
 */
public abstract class ChessBoardLikeGeometry extends OrthogonalBoardGeometry
{
	protected ChessBoardLikeGeometry(int[] size, OrthogonalFieldNotations orthogonalFieldNotations)
	{
		super(size, orthogonalFieldNotations);
	}

	// vectors

	private static final int[] NORTH = { 0, 1 };

	private static final int[] SOUTH = { 0, -1 };

	private static final int[] EAST = { 1, 0 };

	private static final int[] WEST = { -1, 0 };

	private static final int[] NORTHEAST = { 1, 1 };

	private static final int[] SOUTHEAST = { 1, -1 };

	private static final int[] SOUTHWEST = { -1, -1 };

	private static final int[] NORTHWEST = { -1, 1 };

	public int[] east()
	{
		return EAST;
	}

	public int[] west()
	{
		return WEST;
	}

	public int[] north()
	{
		return NORTH;
	}

	public int[] south()
	{
		return SOUTH;
	}

	public int[] northeast()
	{
		return NORTHEAST;
	}

	public int[] southeast()
	{
		return SOUTHEAST;
	}

	public int[] southwest()
	{
		return SOUTHWEST;
	}

	public int[] northwest()
	{
		return NORTHWEST;
	}

	/**
	 * Determines the forward vector for directional pieces.
	 * 
	 * @param directional
	 *            piece
	 * @return forward vector
	 */
	public int[] forwardDir(int color)
	{
		if (color == 0)
			return north();
		else
			return south();
	}

	/**
	 * Determines the backward vector for directional pieces.
	 * 
	 * @param directional
	 *            piece
	 * @return backward vector
	 */
	public int[] backwardDir(int color)
	{
		if (color == 0)
			return south();
		else
			return north();
	}

	public int[] kingSideDir()
	{
		return east();
	}

	public int[] queenSideDir()
	{
		return west();
	}

	/**
	 * Returns the 4 orthogonal vectors in the 2d layer.
	 * 
	 * @return orthogonal vectors
	 */
	public Set<int[]> orthogonalVectors2D()
	{
		Set<int[]> vectors = new HashSet<int[]>();
		vectors.add(north());
		vectors.add(south());
		vectors.add(east());
		vectors.add(west());
		return vectors;
	}

	/**
	 * Determines direction that is indicated by two orthogonal coordinates. Two coordinates are orthogonal, if they are
	 * on the same orthogonal as defined by {@link #orthogonalVectors2D()}.
	 * 
	 * At the moment, 3D coordinates are primitively converted to 2D coordinates.
	 * 
	 * @param c1
	 *            first coordinate
	 * @param c2
	 *            second coordinate *
	 * @param accountableDimensions
	 *            number of dimensions to take into account
	 * @param maxDistance
	 *            maximum distance to take into account
	 * @return direction vector, or null if coordinates not diagonal
	 */
	public int[] orthogonalDir(Coordinate c1, Coordinate c2, int accountableDimensions, int maxDistance)
	{
		if (c1.equals(c2))
			return null;

		int dimension = getDimension();

		int[] diff = coordinateDiff(c2, c1);

		// check equality of coodinate components not taken into account later
		if (accountableDimensions < dimension)
			for (int i = accountableDimensions; i < dimension; i++)
				if (diff[i] != 0)
					return null;

		int axis = -1;
		for (int i = 0; i < accountableDimensions; i++)
		{
			int diffI = diff[i];

			// check max distance
			if (maxDistance > 0 && Math.abs(diffI) > maxDistance)
				return null;

			// check axis
			if (axis == -1)
			{
				if (diffI != 0)
					axis = i;
			}
			else
			{
				if (diffI != 0)
					return null;
			}
		}

		// normalize vector
		diff[axis] = diff[axis] / Math.abs(diff[axis]);

		return diff;
	}

	/**
	 * Convenience method.
	 */
	public int[] orthogonalDir2D(Coordinate c1, Coordinate c2, int maxDistance)
	{
		return orthogonalDir(c1, c2, 2, maxDistance);
	}

	/**
	 * Convenience method.
	 */
	public int[] orthogonalDir2D(Coordinate c1, Coordinate c2)
	{
		return orthogonalDir(c1, c2, 2, 0);
	}

	public int[] specificOrthogonalDir(Coordinate c1, Coordinate c2, int axis, int maxDistance)
	{
		if (c1.equals(c2))
			return null;

		int dimension = getDimension();

		int[] diff = coordinateDiff(c2, c1);

		// check other axis
		for (int i = 0; i < dimension; i++)
			if (i != axis && diff[i] != 0)
				return null;

		// check max distance
		if (maxDistance > 0 && Math.abs(diff[axis]) > maxDistance)
			return null;

		// normalize vector
		diff[axis] = diff[axis] / Math.abs(diff[axis]);

		return diff;
	}

	/**
	 * Returns the 4 diagonal vectors in the 2d layer.
	 * 
	 * @return diagonal vectors
	 */
	public Set<int[]> diagonalVectors2D()
	{
		Set<int[]> vectors = new HashSet<int[]>();
		vectors.add(northeast());
		vectors.add(southeast());
		vectors.add(southwest());
		vectors.add(northwest());
		return vectors;
	}

	/**
	 * Determines direction that is indicated by two diagonal coordinates. Two coordinates are diagonal, if they are on
	 * the same diagonal as defined by {@link #diagonalVectors2D()}.
	 * 
	 * At the moment, 3D coordinates are primitively converted to 2D coordinates.
	 * 
	 * @param c1
	 *            first coordinate
	 * @param c2
	 *            second coordinate
	 * @param accountableDimensions
	 *            number of dimensions to take into account
	 * @param maxDistance
	 *            maximum distance to take into account
	 * @return direction vector, or null if coordinates not diagonal
	 */
	public int[] diagonalDir(Coordinate c1, Coordinate c2, int accountableDimensions, int maxDistance)
	{
		if (c1.equals(c2))
			return null;

		int dimension = getDimension();

		int[] diff = coordinateDiff(c2, c1);

		// check equality of coodinate components not taken into account later
		if (accountableDimensions < dimension)
			for (int i = accountableDimensions; i < dimension; i++)
				if (diff[i] != 0)
					return null;

		int abs = -1;
		for (int i = 0; i < accountableDimensions; i++)
		{
			int absDiffI = Math.abs(diff[i]);

			// check max distance
			if (maxDistance > 0 && absDiffI > maxDistance)
				return null;

			// check equality of absolutes
			if (abs == -1)
				abs = absDiffI;
			else if (absDiffI != abs)
				return null;
		}

		// normalize vector
		for (int i = 0; i < dimension; i++)
			diff[i] /= abs;

		return diff;
	}

	/**
	 * Convenience method.
	 */
	public int[] diagonalDir2D(Coordinate c1, Coordinate c2, int maxDistance)
	{
		return diagonalDir(c1, c2, 2, maxDistance);
	}

	/**
	 * Convenience method.
	 */
	public int[] diagonalDir2D(Coordinate c1, Coordinate c2)
	{
		return diagonalDir(c1, c2, 2, 0);
	}

	public boolean knight2D(Coordinate c1, Coordinate c2)
	{
		int[] d = coordinateDiff(c1, c2);

		int dimension = getDimension();

		// check equality of coodinate components not taken into account later
		if (2 < dimension)
			for (int i = 2; i < dimension; i++)
				if (d[i] != 0)
					return false;

		d[0] = Math.abs(d[0]);
		d[1] = Math.abs(d[1]);
		if (d[0] < 1 || d[0] > 2 || d[1] < 1 || d[1] > 2 || d[0] + d[1] != 3)
			return false;
		return true;
	}

	public Set<int[]> knightVectors2D()
	{
		Set<int[]> vectors = new HashSet<int[]>();
		vectors.add(new int[] { 1, 2 });
		vectors.add(new int[] { 2, 1 });
		vectors.add(new int[] { 2, -1 });
		vectors.add(new int[] { 1, -2 });
		vectors.add(new int[] { -1, -2 });
		vectors.add(new int[] { -2, -1 });
		vectors.add(new int[] { -2, 1 });
		vectors.add(new int[] { -1, 2 });
		return vectors;
	}

	// geometric checks

	public boolean isNorthOf(Coordinate c1, Coordinate c2)
	{
		OrthogonalCoordinate oc1 = (OrthogonalCoordinate) c1;
		OrthogonalCoordinate oc2 = (OrthogonalCoordinate) c2;

		return coordinateDiff(OrthogonalBoardGeometry.AXIS_HEIGHT, oc1, oc2) > 0;
	}

	public boolean isEastOf(Coordinate c1, Coordinate c2)
	{
		OrthogonalCoordinate oc1 = (OrthogonalCoordinate) c1;
		OrthogonalCoordinate oc2 = (OrthogonalCoordinate) c2;

		return coordinateDiff(OrthogonalBoardGeometry.AXIS_WIDTH, oc1, oc2) > 0;
	}

	public boolean isMaxNorth(Coordinate c)
	{
		return isMaxOnAxis(OrthogonalBoardGeometry.AXIS_HEIGHT, c);
	}

	public boolean isMaxSouth(Coordinate c)
	{
		return isMinOnAxis(OrthogonalBoardGeometry.AXIS_HEIGHT, c);
	}

	// utility

	/**
	 * This is not the geometric distance but the number of moves a king would need to get from c1 to c2.
	 */
	public int distance(Coordinate c1, Coordinate c2)
	{
		int[] d = coordinateDiff(c1, c2);

		// determine maximum of absolutes
		int max = 0;
		for (int i = 0; i < d.length; i++)
		{
			int abs = Math.abs(d[i]);
			if (abs > max)
				max = abs;
		}
		return max;
	}

	/**
	 * Determines if the target square is reachable from the source quare by adding the vector one or more times. The
	 * sqares in between may not be occupied.
	 * 
	 * @todo change name to a more abstract one, as it is too much chess specific
	 */
	public boolean internalIsThreateningSquare(Board board, Coordinate source, Coordinate target, int[] vector)
	{
		while (true)
		{
			source = vectorAdd(source, vector, 1);
			if (source == null)
				return false;
			if (source.equals(target))
				return true;
			if (board.getPiece(source) != null)
				return false;
		}
	}

	/**
	 * Determines if the piece is in its target area.
	 */
	public boolean isInTargetArea(Coordinate coordinate, int color)
	{
		if (forwardDir(color) == north())
			return isMaxNorth(coordinate);
		else
			return isMaxSouth(coordinate);
	}
}
