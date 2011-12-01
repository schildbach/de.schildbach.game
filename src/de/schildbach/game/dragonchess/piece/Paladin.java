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

import java.util.Set;

import de.schildbach.game.Board;
import de.schildbach.game.Coordinate;
import de.schildbach.game.common.ChessBoardLikeGeometry;
import de.schildbach.game.common.OrthogonalBoardGeometry;
import de.schildbach.game.common.piece.ChessLikePiece;
import de.schildbach.game.dragonchess.DragonchessBoardGeometry;

/**
 * @author Andreas Schildbach
 */
public class Paladin extends ChessLikePiece
{
	public Paladin(int color)
	{
		super(color);
	}

	@Override
	public Set<Coordinate> getPotentialTargets(ChessBoardLikeGeometry geometry, Board board, Coordinate source)
	{
		DragonchessBoardGeometry dragonchessGeometry = (DragonchessBoardGeometry) geometry;

		// movement on all layers
		Set<Coordinate> targets = getPotentialJumpTargets(geometry, board, source, dragonchessGeometry.knightVectors3D(), 1, false);
		Set<int[]> vectors = geometry.orthogonalVectors2D();
		vectors.addAll(geometry.diagonalVectors2D());
		targets.addAll(getPotentialJumpTargets(geometry, board, source, vectors, 1, false));

		// ground movement
		if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.GROUND))
			targets.addAll(getPotentialJumpTargets(geometry, board, source, geometry.knightVectors2D(), 1, false));

		return targets;
	}

	@Override
	public boolean isThreateningSquare(ChessBoardLikeGeometry geometry, Board board, Coordinate source, Coordinate target)
	{
		DragonchessBoardGeometry dragonchessGeometry = (DragonchessBoardGeometry) geometry;

		// movement on all layers
		if (dragonchessGeometry.knight3D(source, target))
			return true;

		if (geometry.coordinateDiff(source, target)[OrthogonalBoardGeometry.AXIS_LAYER] == 0 && geometry.distance(source, target) == 1)
			return true;

		// ground movement
		if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.GROUND))
		{
			if (geometry.knight2D(source, target))
				return true;
		}

		return false;
	}
}
