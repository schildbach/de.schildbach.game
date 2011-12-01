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

package de.schildbach.game.checkers;

import java.util.HashSet;
import java.util.Set;

import de.schildbach.game.Coordinate;
import de.schildbach.game.common.ChessBoardLikeGeometry;
import de.schildbach.game.common.OrthogonalFieldNotations;

/**
 * @author Andreas Schildbach
 */
public class CheckersBoardGeometry extends ChessBoardLikeGeometry
{
	private static class CheckersFieldNotations implements OrthogonalFieldNotations
	{
		private final int size;

		public CheckersFieldNotations(int size)
		{
			this.size = size;
		}

		private boolean usedSquare(int x, int y)
		{
			return (x + y) % 2 == 0;
		}

		private String coordinateToSquareNumber(int x, int y)
		{
			if (!usedSquare(x, y))
				return null;
			int advance = size / 2;
			int squareNumber = ((size - 1 - y) * advance + x / 2) + 1;
			return Integer.toString(squareNumber);
		}

		public String notation(int... components)
		{
			return coordinateToSquareNumber(components[0], components[1]);
		}
	}

	private CheckersBoardGeometry(int size)
	{
		super(new int[] { size, size }, new CheckersFieldNotations(size));

		if (size % 2 == 1)
			throw new IllegalArgumentException("odd sizes not supported");
	}

	public static CheckersBoardGeometry instance(int size)
	{
		return new CheckersBoardGeometry(size);
	}

	public static CheckersBoardGeometry instance()
	{
		return instance(10);
	}

	protected static int maxSquareNumber(int boardDimension)
	{
		return boardDimension * boardDimension / 2;
	}

	// vectors

	public Set<int[]> forwardVectors(int color)
	{
		Set<int[]> vectors = new HashSet<int[]>();
		int[] forward = forwardDir(color);
		vectors.add(vectorAdd(forward, east()));
		vectors.add(vectorAdd(forward, west()));
		return vectors;
	}

	// geometric checks

	public boolean isTargetRank(Coordinate coordinate, int activePlayerIndex)
	{
		if (activePlayerIndex == 0)
			return isMaxNorth(coordinate);
		else
			return isMaxSouth(coordinate);
	}
}
