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
public final class Man extends CheckersPiece
{
	public Man(int color)
	{
		super(color);
	}

	public int direction()
	{
		return getColor() == 0 ? 1 : -1;
	}

	@Override
	public Set<Coordinate> getPotentialTargets(CheckersBoardGeometry geometry, Board board, Coordinate source, boolean capturingOnly)
	{
		Set<Coordinate> targets = new HashSet<Coordinate>();

		// plain moves
		if (!capturingOnly)
			addPlainMoveTargets(targets, geometry, board, source, geometry.forwardVectors(this.getColor()));

		// capturing moves
		addCapturingMoveTargets(targets, geometry, board, source, geometry.diagonalVectors2D());

		return targets;
	}

	private void addPlainMoveTargets(Set<Coordinate> targets, CheckersBoardGeometry geometry, Board board, Coordinate source, Set<int[]> vectors)
	{
		for (int[] v : vectors)
		{
			Coordinate target = geometry.vectorAdd(source, v, 1);
			if (target != null && board.getPiece(target) == null)
			{
				targets.add(target);
			}
		}
	}

	private void addCapturingMoveTargets(Set<Coordinate> targets, CheckersBoardGeometry geometry, Board board, Coordinate source, Set<int[]> vectors)
	{
		for (int[] v : vectors)
		{
			Coordinate target = geometry.vectorAdd(source, v, 2);
			if (target != null && board.getPiece(target) == null)
			{
				Coordinate jumpOver = geometry.vectorAdd(source, v, 1);
				if (jumpOver != null)
				{
					Piece j = board.getPiece(jumpOver);
					if (j != null && !j.sameColor(this))
					{
						targets.add(target);
					}
				}
			}
		}
	}
}
