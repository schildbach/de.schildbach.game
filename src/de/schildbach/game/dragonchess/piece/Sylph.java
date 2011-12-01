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

import de.schildbach.game.Coordinate;
import de.schildbach.game.common.ChessBoardLikeGeometry;
import de.schildbach.game.common.piece.DirectionalPiece;
import de.schildbach.game.dragonchess.DragonchessBoardGeometry;

/**
 * The "jump home ability" is handled in DragonchessRules.
 *
 * @author Andreas Schildbach
 */
public class Sylph extends DirectionalPiece
{
	public Sylph(int color)
	{
		super(color);
	}

	@Override
	protected Set<int[]> moveVectors(ChessBoardLikeGeometry geometry, Coordinate source)
	{
		DragonchessBoardGeometry dragonchessGeometry = (DragonchessBoardGeometry) geometry;

		Set<int[]> dirs;

		if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.SKY))
		{
			int[] forward = geometry.forwardDir(this.getColor());
			dirs = geometry.diagonalVectors2D();
			geometry.filterFacingDir(dirs, forward, false);
		}
		else if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.GROUND))
		{
			dirs = new HashSet<int[]>();
			dirs.add(dragonchessGeometry.up());
		}
		else
		{
			throw new IllegalStateException();
		}

		return dirs;
	}

	@Override
	protected Set<int[]> captureVectors(ChessBoardLikeGeometry geometry, Coordinate source)
	{
		Set<int[]> dirs = new HashSet<int[]>();

		DragonchessBoardGeometry dragonchessGeometry = (DragonchessBoardGeometry) geometry;
		if (dragonchessGeometry.isLayer(source, DragonchessBoardGeometry.SKY))
		{
			dirs.add(geometry.forwardDir(this.getColor()));
			dirs.add(dragonchessGeometry.down());
		}

		return dirs;
	}
}
