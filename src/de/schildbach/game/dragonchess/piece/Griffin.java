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
public class Griffin extends ChessLikePiece
{
	public Griffin(int color)
	{
		super(color);
	}

	@Override
	public Set<Coordinate> getPotentialTargets(ChessBoardLikeGeometry geometry, Board board, Coordinate source)
	{
		DragonchessBoardGeometry dragonchessGeometry = (DragonchessBoardGeometry) geometry;

		Set<Coordinate> targets = new HashSet<Coordinate>();

		if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.SKY))
		{
			targets.addAll(getPotentialJumpTargets(geometry, board, source, dragonchessGeometry.griffinVectors(), 1, false));
			targets.addAll(getPotentialJumpTargets(geometry, board, source, dragonchessGeometry.diagonalVectors3D(false, true), 1, false));
		}
		else if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.GROUND))
		{
			targets.addAll(getPotentialJumpTargets(geometry, board, source, dragonchessGeometry.diagonalVectors2D(), 1, false));
			targets.addAll(getPotentialJumpTargets(geometry, board, source, dragonchessGeometry.diagonalVectors3D(true, false), 1, false));
		}
		else
		{
			throw new IllegalStateException();
		}

		return targets;
	}

	@Override
	public boolean isThreateningSquare(ChessBoardLikeGeometry geometry, Board board, Coordinate source, Coordinate target)
	{
		DragonchessBoardGeometry dragonchessGeometry = (DragonchessBoardGeometry) geometry;

		if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.SKY))
		{
			if (dragonchessGeometry.griffin(source, target))
				return true;
			int[] dir = dragonchessGeometry.diagonalDir3D(source, target, 1);
			if (dir != null && !dragonchessGeometry.isPointingUp(dir))
				return true;
		}
		else if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.GROUND))
		{
			if (geometry.diagonalDir2D(source, target, 1) != null)
				return true;
			int[] dir = dragonchessGeometry.diagonalDir3D(source, target, 1);
			if (dir != null && dragonchessGeometry.isPointingUp(dir))
				return true;
		}
		else
		{
			throw new IllegalStateException();
		}

		return false;
	}
}
