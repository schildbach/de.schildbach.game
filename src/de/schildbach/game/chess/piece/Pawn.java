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

package de.schildbach.game.chess.piece;

import java.util.HashSet;
import java.util.Set;

import de.schildbach.game.Coordinate;
import de.schildbach.game.common.ChessBoardLikeGeometry;
import de.schildbach.game.common.piece.DirectionalPiece;

/**
 * Note: 'En passant' captures, pawn promotions and two-square advancement are not handled by this
 * class, they have to be handled "upstream"
 * 
 * @author Andreas Schildbach
 */
public class Pawn extends DirectionalPiece
{
	public Pawn(int color)
	{
		super(color);
	}

	@Override
	protected Set<int[]> moveVectors(ChessBoardLikeGeometry geometry, Coordinate source)
	{
		Set<int[]> vectors = new HashSet<int[]>();
		vectors.add(geometry.forwardDir(this.getColor()));
		return vectors;
	}

	@Override
	protected Set<int[]> captureVectors(ChessBoardLikeGeometry geometry, Coordinate source)
	{
		Set<int[]> vectors = new HashSet<int[]>();
		int[] forward = geometry.forwardDir(this.getColor());
		vectors.add(geometry.vectorAdd(forward, geometry.kingSideDir()));
		vectors.add(geometry.vectorAdd(forward, geometry.queenSideDir()));
		return vectors;
	}
}
