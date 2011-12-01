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

package de.schildbach.game.checkers.piece;

import java.util.HashSet;
import java.util.Set;

import de.schildbach.game.Board;
import de.schildbach.game.Coordinate;
import de.schildbach.game.Piece;
import de.schildbach.game.checkers.CheckersBoardGeometry;

/**
 * @author Andreas Schildbach
 */
public final class King extends CheckersPiece
{
	public King(int color)
	{
		super(color);
	}

	@Override
	public Set<Coordinate> getPotentialTargets(CheckersBoardGeometry geometry, Board board, Coordinate source, boolean capturingOnly)
	{
		Set<Coordinate> targets = new HashSet<Coordinate>();

		for (int[] dir : geometry.diagonalVectors2D())
			addMoveTargets(targets, geometry, board, source, capturingOnly, dir);

		return targets;
	}

	private void addMoveTargets(Set<Coordinate> targets, CheckersBoardGeometry geometry, Board board, Coordinate source, boolean capturingOnly,
			int[] vector)
	{
		Coordinate c = geometry.vectorAdd(source, vector, 1);
		boolean capturing = false;
		while (c != null)
		{
			Piece piece = board.getPiece(c);
			if (!capturing)
			{
				if (piece == null && !capturingOnly)
				{
					targets.add(c);
				}
				else if (piece != null)
				{
					if (piece.sameColor(this))
						break;
					else
						capturing = true;
				}
			}
			else
			{
				if (piece == null)
					targets.add(c);
				else
					break;
			}

			c = geometry.vectorAdd(c, vector, 1);
		}
	}
}
