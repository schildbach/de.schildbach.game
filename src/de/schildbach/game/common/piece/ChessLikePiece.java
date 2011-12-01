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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import de.schildbach.game.Board;
import de.schildbach.game.Coordinate;
import de.schildbach.game.Piece;
import de.schildbach.game.common.ChessBoardLikeGeometry;

/**
 * @author Andreas Schildbach
 */
public abstract class ChessLikePiece extends Piece implements Serializable
{
	protected ChessLikePiece(int color)
	{
		super(color);
	}

	public abstract Set<Coordinate> getPotentialTargets(ChessBoardLikeGeometry geometry, Board board, Coordinate source);

	protected final Set<Coordinate> getPotentialJumpTargets(ChessBoardLikeGeometry geometry, Board board, Coordinate source, Set<int[]> jumpVectors,
			int factor, boolean friendlyCapture)
	{
		Set<Coordinate> targets = new HashSet<Coordinate>();

		for (int[] vector : jumpVectors)
		{
			Coordinate target = geometry.vectorAdd(source, vector, factor);
			if (target != null)
			{
				Piece piece = board.getPiece(target);
				if (piece == null)
				{
					targets.add(target);
				}
				else
				{
					if (friendlyCapture)
					{
						if (piece.getColor() == this.getColor())
						{
							targets.add(target);
						}
					}
					else
					{
						if (piece.getColor() != this.getColor())
						{
							targets.add(target);
						}
					}
				}
			}
		}

		return targets;
	}

	protected final Set<Coordinate> getPotentialPeacefulJumpTargets(ChessBoardLikeGeometry geometry, Board board, Coordinate source,
			Set<int[]> jumpVectors, int factor)
	{
		Set<Coordinate> targets = new HashSet<Coordinate>();

		for (int[] vector : jumpVectors)
		{
			Coordinate target = geometry.vectorAdd(source, vector, factor);
			if (target != null)
			{
				Piece piece = board.getPiece(target);
				if (piece == null)
				{
					targets.add(target);
				}
			}
		}

		return targets;
	}

	protected final Set<Coordinate> getPotentialCaptureTargets(ChessBoardLikeGeometry geometry, Board board, Coordinate source, Set<int[]> jumpVectors)
	{
		Set<Coordinate> targets = new HashSet<Coordinate>();

		for (int[] vector : jumpVectors)
		{
			Coordinate target = geometry.vectorAdd(source, vector, 1);
			if (target != null)
			{
				Piece piece = board.getPiece(target);
				if (piece != null && piece.getColor() != this.getColor())
				{
					targets.add(target);
				}
			}
		}

		return targets;
	}

	/**
	 * Convenience method.
	 */
	protected final Set<Coordinate> getPotentialMoveTargets(ChessBoardLikeGeometry geometry, Board board, Coordinate source,
			Set<int[]> movementVectors)
	{
		// "-1" is important because of presumed loop optimizer bug in 64bit JRE (unit tests will fail)
		return getPotentialMoveTargets(geometry, board, source, movementVectors, Integer.MAX_VALUE-1);
	}

	protected final Set<Coordinate> getPotentialMoveTargets(ChessBoardLikeGeometry geometry, Board board, Coordinate source,
			Set<int[]> movementVectors, int maxDistance)
	{
		Set<Coordinate> targets = new HashSet<Coordinate>();

		for (int[] movementVector : movementVectors)
		{
			for (int r = 1; r <= maxDistance; r++)
			{
				Coordinate target = geometry.vectorAdd(source, movementVector, r);
				if (target != null)
				{
					Piece piece = board.getPiece(target);
					if (piece == null)
					{
						targets.add(target);
					}
					else
					{
						if (piece.getColor() != this.getColor())
						{
							targets.add(target);
						}
						break;
					}
				}
				else
				{
					break;
				}
			}
		}

		return targets;
	}

	/**
	 * Determines if the piece on source coordinate is threatening the given (target) coordinate. It is irrelevant
	 * whether the target square is occupied or not. However - depending on the capabilities of the piece - it may be
	 * relevant if the squares inbetween are occupied (on the given board).
	 */
	public abstract boolean isThreateningSquare(ChessBoardLikeGeometry geometry, Board board, Coordinate source, Coordinate target);
}
