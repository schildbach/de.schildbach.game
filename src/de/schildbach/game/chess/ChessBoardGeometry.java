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

package de.schildbach.game.chess;

import de.schildbach.game.Coordinate;
import de.schildbach.game.common.ChessBoardLikeGeometry;
import de.schildbach.game.common.CoordinateFieldNotations;
import de.schildbach.game.common.OrthogonalBoardGeometry;
import de.schildbach.game.common.OrthogonalFieldNotations;

/**
 * @author Andreas Schildbach
 */
public class ChessBoardGeometry extends ChessBoardLikeGeometry
{
	private static final OrthogonalFieldNotations FIELD_NOTATIONS = new CoordinateFieldNotations('a', '1');

	private ChessBoardGeometry(int width, int height)
	{
		super(new int[] { width, height }, FIELD_NOTATIONS);
	}

	public static ChessBoardGeometry instance(int width, int height)
	{
		return new ChessBoardGeometry(width, height);
	}

	public static ChessBoardGeometry instance()
	{
		return instance(8, 8);
	}

	// geometric checks

	public boolean isInEastHalf(Coordinate c)
	{
		return getComponent(AXIS_WIDTH, c) > getSize(AXIS_WIDTH) / 2;
	}

	// geometric checks

	public boolean sameRank(Coordinate c1, Coordinate c2)
	{
		return isOnSameOrthogonal(OrthogonalBoardGeometry.AXIS_HEIGHT, c1, c2);
	}

	public int[] sameRankDir(Coordinate c1, Coordinate c2)
	{
		if (!sameRank(c1, c2))
			return null;
		else
			return isEastOf(c2, c1) ? east() : west();
	}

	public boolean sameFile(Coordinate c1, Coordinate c2)
	{
		return isOnSameOrthogonal(OrthogonalBoardGeometry.AXIS_WIDTH, c1, c2);
	}

	public int[] sameFileDir(Coordinate c1, Coordinate c2)
	{
		if (!sameFile(c1, c2))
			return null;
		else
			return isNorthOf(c2, c1) ? north() : south();
	}

	/** determines if c2 is kingside of c1 */
	public boolean isKingSide(Coordinate c1, Coordinate c2)
	{
		return isEastOf(c2, c1);
	}

	/** determines if c is on the kingside half of the board */
	public boolean isKingSide(Coordinate c)
	{
		return isInEastHalf(c);
	}

	/**
	 * determines if c2 is forward of c1, depending on forward definition of piece
	 */
	public boolean isForward(Coordinate c1, int color, Coordinate c2)
	{
		if (forwardDir(color) == north())
			return isNorthOf(c2, c1);
		else
			return isNorthOf(c1, c2);
	}
}
