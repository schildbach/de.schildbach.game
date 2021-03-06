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
import de.schildbach.game.common.piece.ChessLikePiece;
import de.schildbach.game.dragonchess.DragonchessBoardGeometry;

/**
 * @author Andreas Schildbach
 */
public class Hero extends ChessLikePiece
{
	public Hero(int color)
	{
		super(color);
	}

	@Override
	public Set<Coordinate> getPotentialTargets(ChessBoardLikeGeometry geometry, Board board, Coordinate source)
	{
		DragonchessBoardGeometry dragonchessGeometry = (DragonchessBoardGeometry) geometry;

		// movement on all layers
		Set<Coordinate> moves = getPotentialJumpTargets(geometry, board, source, dragonchessGeometry.diagonalVectors3D(true, true), 1, false);

		// additional movement on ground
		if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.GROUND))
		{
			Set<int[]> diagonalVectors = geometry.diagonalVectors2D();
			moves.addAll(getPotentialJumpTargets(geometry, board, source, diagonalVectors, 1, false));
			moves.addAll(getPotentialJumpTargets(geometry, board, source, diagonalVectors, 2, false));
		}

		return moves;
	}

	@Override
	public boolean isThreateningSquare(ChessBoardLikeGeometry geometry, Board board, Coordinate source, Coordinate target)
	{
		DragonchessBoardGeometry dragonchessGeometry = (DragonchessBoardGeometry) geometry;

		// movement on all layers
		if (dragonchessGeometry.diagonalDir3D(source, target, 1) != null)
			return true;

		// additional movement on ground
		if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.GROUND))
			return dragonchessGeometry.diagonalDir2D(source, target, 2) != null;

		return false;
	}
}
