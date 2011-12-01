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

import de.schildbach.game.Coordinate;
import de.schildbach.game.common.ChessBoardLikeGeometry;
import de.schildbach.game.common.piece.DirectionalPiece;

/**
 * Note: The 'freezing' ability of the Basilisk is not handled by this class, it shall be handled upstream.
 * 
 * @author Andreas Schildbach
 */
public class Basilisk extends DirectionalPiece
{
	public Basilisk(int color)
	{
		super(color);
	}

	@Override
	protected Set<int[]> moveVectors(ChessBoardLikeGeometry geometry, Coordinate source)
	{
		Set<int[]> dirs = captureVectors(geometry, source);
		dirs.add(geometry.backwardDir(this.getColor()));
		return dirs;
	}

	@Override
	protected Set<int[]> captureVectors(ChessBoardLikeGeometry geometry, Coordinate source)
	{
		int[] forward = geometry.forwardDir(this.getColor());
		Set<int[]> dirs = geometry.diagonalVectors2D();
		geometry.filterFacingDir(dirs, forward, false);
		dirs.add(forward);
		return dirs;
	}
}
