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
 * @author Andreas Schildbach
 */
public class BerolinaPawn extends DirectionalPiece
{
	public BerolinaPawn(int color)
	{
		super(color);
	}

	@Override
	protected Set<int[]> moveVectors(ChessBoardLikeGeometry geometry, Coordinate source)
	{
		Set<int[]> dirs = new HashSet<int[]>();
		int[] forward = geometry.forwardDir(this.getColor());
		dirs.add(geometry.vectorAdd(forward, geometry.kingSideDir()));
		dirs.add(geometry.vectorAdd(forward, geometry.queenSideDir()));
		return dirs;
	}

	@Override
	protected Set<int[]> captureVectors(ChessBoardLikeGeometry geometry, Coordinate source)
	{
		Set<int[]> dirs = new HashSet<int[]>();
		dirs.add(geometry.forwardDir(this.getColor()));
		return dirs;
	}
}
