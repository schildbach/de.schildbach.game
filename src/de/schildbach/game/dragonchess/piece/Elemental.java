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
 * @author Andreas Schildbach
 */
public class Elemental extends ChessLikePiece
{
	public Elemental(int color)
	{
		super(color);
	}

	@Override
	public Set<Coordinate> getPotentialTargets(ChessBoardLikeGeometry geometry, Board board, Coordinate source)
	{
		DragonchessBoardGeometry dragonchessGeometry = (DragonchessBoardGeometry) geometry;

		Set<Coordinate> targets = new HashSet<Coordinate>();

		if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.GROUND))
		{
			Coordinate below = geometry.vectorAdd(source, dragonchessGeometry.down(), 1);
			targets.addAll(getPotentialJumpTargets(geometry, board, below, geometry.orthogonalVectors2D(), 1, false));
		}
		else if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.UNDERWORLD))
		{
			targets.addAll(getPotentialMoveTargets(geometry, board, source, geometry.orthogonalVectors2D(), 2));
			targets.addAll(getPotentialPeacefulJumpTargets(geometry, board, source, dragonchessGeometry.diagonalVectors2D(), 1));

			Coordinate above = geometry.vectorAdd(source, dragonchessGeometry.up(), 1);
			targets.addAll(getPotentialJumpTargets(geometry, board, above, geometry.orthogonalVectors2D(), 1, false));
		}

		return targets;
	}

	@Override
	public boolean isThreateningSquare(ChessBoardLikeGeometry geometry, Board board, Coordinate source, Coordinate target)
	{
		DragonchessBoardGeometry dragonchessGeometry = (DragonchessBoardGeometry) geometry;

		if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.GROUND))
		{
			Coordinate below = geometry.vectorAdd(source, dragonchessGeometry.down(), 1);
			if (geometry.orthogonalDir2D(below, target, 1) != null)
				return true;
		}
		else if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.UNDERWORLD))
		{
			Coordinate above = geometry.vectorAdd(source, dragonchessGeometry.up(), 1);
			if (geometry.orthogonalDir2D(above, target, 1) != null)
				return true;

			int[] vector = geometry.orthogonalDir2D(source, target, 2);

			if (vector == null)
				return false;

			return geometry.internalIsThreateningSquare(board, source, target, vector);
		}

		return false;
	}
}
