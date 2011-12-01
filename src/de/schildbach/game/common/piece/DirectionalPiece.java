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

package de.schildbach.game.common.piece;

import java.util.HashSet;
import java.util.Set;

import de.schildbach.game.Board;
import de.schildbach.game.Coordinate;
import de.schildbach.game.Piece;
import de.schildbach.game.common.ChessBoardLikeGeometry;

/**
 * @author Andreas Schildbach
 */
public abstract class DirectionalPiece extends ChessLikePiece
{
	protected DirectionalPiece(int color)
	{
		super(color);
	}

	@Override
	public final Set<Coordinate> getPotentialTargets(ChessBoardLikeGeometry geometry, Board board, Coordinate source)
	{
		Set<int[]> moveVectors = moveVectors(geometry, source);

		Set<int[]> captureVectors = captureVectors(geometry, source);

		Set<Coordinate> targets = new HashSet<Coordinate>();

		for (int[] v : moveVectors)
		{
			Coordinate target = geometry.vectorAdd(source, v, 1);
			if (target != null && board.getPiece(target) == null)
			{
				targets.add(target);
			}
		}

		for (int[] v : captureVectors)
		{
			Coordinate target = geometry.vectorAdd(source, v, 1);
			if (target != null)
			{
				Piece piece = board.getPiece(target);
				if (piece != null && !sameColor(piece))
					targets.add(target);
			}
		}

		return targets;
	}

	@Override
	public final boolean isThreateningSquare(ChessBoardLikeGeometry geometry, Board board, Coordinate source, Coordinate target)
	{
		Set<int[]> captureVectors = captureVectors(geometry, source);

		for (int[] v : captureVectors)
		{
			Coordinate captureTarget = geometry.vectorAdd(source, v, 1);
			if (captureTarget != null && target.equals(captureTarget))
				return true;
		}

		return false;
	}

	protected abstract Set<int[]> captureVectors(ChessBoardLikeGeometry geometry, Coordinate source);

	protected abstract Set<int[]> moveVectors(ChessBoardLikeGeometry geometry, Coordinate source);
}
