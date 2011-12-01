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
import de.schildbach.game.dragonchess.DragonchessBoardGeometry;

/**
 * @author Andreas Schildbach
 */
public class Dwarf extends DirectionalPiece
{
	public Dwarf(int color)
	{
		super(color);
	}

	@Override
	protected Set<int[]> moveVectors(ChessBoardLikeGeometry geometry, Coordinate source)
	{
		DragonchessBoardGeometry dragonchessGeometry = (DragonchessBoardGeometry) geometry;

		// all layers
		int[] forward = geometry.forwardDir(this.getColor());
		Set<int[]> dirs = geometry.orthogonalVectors2D();
		geometry.filterFacingDir(dirs, forward, true);

		// ground
		if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.GROUND))
			dirs.add(dragonchessGeometry.down());

		return dirs;
	}

	@Override
	protected Set<int[]> captureVectors(ChessBoardLikeGeometry geometry, Coordinate source)
	{
		DragonchessBoardGeometry dragonchessGeometry = (DragonchessBoardGeometry) geometry;

		// all layers
		int[] forward = geometry.forwardDir(this.getColor());
		Set<int[]> dirs = geometry.diagonalVectors2D();
		geometry.filterFacingDir(dirs, forward, false);

		// underworld
		if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.UNDERWORLD))
			dirs.add(dragonchessGeometry.up());

		return dirs;
	}
}
