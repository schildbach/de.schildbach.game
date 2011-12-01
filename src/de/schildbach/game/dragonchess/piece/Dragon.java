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

package de.schildbach.game.dragonchess.piece;

import java.util.HashSet;
import java.util.Set;

import de.schildbach.game.Board;
import de.schildbach.game.Coordinate;
import de.schildbach.game.common.ChessBoardLikeGeometry;
import de.schildbach.game.common.piece.ChessLikePiece;
import de.schildbach.game.dragonchess.DragonchessBoardGeometry;

/**
 * "Capturing from afar" is handled like a normal move
 * 
 * @author Andreas Schildbach
 */
public class Dragon extends ChessLikePiece
{
	public Dragon(int color)
	{
		super(color);
	}

	@Override
	public Set<Coordinate> getPotentialTargets(ChessBoardLikeGeometry geometry, Board board, Coordinate source)
	{
		DragonchessBoardGeometry dragonchessGeometry = (DragonchessBoardGeometry) geometry;

		Set<Coordinate> targets = getPotentialMoveTargets(geometry, board, source, geometry.diagonalVectors2D());
		targets.addAll(getPotentialJumpTargets(geometry, board, source, geometry.orthogonalVectors2D(), 1, false));
		Coordinate down = geometry.vectorAdd(source, dragonchessGeometry.down(), 1);
		HashSet<int[]> hashSet = new HashSet<int[]>();
		hashSet.add(new int[] {});
		targets.addAll(getPotentialCaptureTargets(geometry, board, down, hashSet));
		targets.addAll(getPotentialCaptureTargets(geometry, board, down, geometry.orthogonalVectors2D()));
		return targets;
	}

	@Override
	public boolean isThreateningSquare(ChessBoardLikeGeometry geometry, Board board, Coordinate source, Coordinate target)
	{
		DragonchessBoardGeometry dragonchessGeometry = (DragonchessBoardGeometry) geometry;

		if (dragonchessGeometry.isLayer(target, DragonchessBoardGeometry.SKY))
		{
			if (geometry.distance(source, target) == 1)
				return true;

			int[] vector = geometry.diagonalDir2D(source, target);
			if (vector == null)
				return false;

			return geometry.internalIsThreateningSquare(board, source, target, vector);
		}
		else if (dragonchessGeometry.isLayer(target, DragonchessBoardGeometry.GROUND))
		{
			Coordinate targetAbove = geometry.vectorAdd(target, dragonchessGeometry.up(), 1);
			if (source.equals(targetAbove))
				return true;
			return geometry.orthogonalDir2D(source, targetAbove, 1) != null;
		}
		else
		{
			return false;
		}
	}
}
