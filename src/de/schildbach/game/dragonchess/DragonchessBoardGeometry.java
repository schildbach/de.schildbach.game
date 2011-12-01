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

package de.schildbach.game.dragonchess;

import java.util.HashSet;
import java.util.Set;

import de.schildbach.game.Coordinate;
import de.schildbach.game.common.ChessBoardLikeGeometry;
import de.schildbach.game.common.CoordinateFieldNotations;
import de.schildbach.game.common.OrthogonalBoardGeometry;
import de.schildbach.game.common.OrthogonalFieldNotations;

/**
 * @author Andreas Schildbach
 */
public class DragonchessBoardGeometry extends ChessBoardLikeGeometry
{
	public static final int SKY = 0;
	public static final int GROUND = 1;
	public static final int UNDERWORLD = 2;

	private static final int[] DOWN = { 0, 0, 1 };
	private static final int[] UP = { 0, 0, -1 };

	private static final OrthogonalFieldNotations FIELD_NOTATIONS = new CoordinateFieldNotations(new int[] { OrthogonalBoardGeometry.AXIS_LAYER,
			OrthogonalBoardGeometry.AXIS_WIDTH, OrthogonalBoardGeometry.AXIS_HEIGHT }, '1', 'a', '1');

	private DragonchessBoardGeometry(int width, int height, int layers)
	{
		super(new int[] { width, height, layers }, FIELD_NOTATIONS);
	}

	public static DragonchessBoardGeometry instance(int width, int height, int layers)
	{
		return new DragonchessBoardGeometry(width, height, layers);
	}

	public static DragonchessBoardGeometry instance()
	{
		return instance(12, 8, 3);
	}

	public Set<int[]> upDownVector()
	{
		Set<int[]> vectors = new HashSet<int[]>();
		vectors.add(UP);
		vectors.add(DOWN);
		return vectors;
	}

	public int[] up()
	{
		return UP;
	}

	public int[] down()
	{
		return DOWN;
	}

	public Set<int[]> diagonalVectors3D(boolean up, boolean down)
	{
		Set<int[]> vectors = new HashSet<int[]>();
		if (down)
		{
			vectors.add(new int[] { 1, 1, 1 });
			vectors.add(new int[] { 1, -1, 1 });
			vectors.add(new int[] { -1, -1, 1 });
			vectors.add(new int[] { -1, 1, 1 });
		}
		if (up)
		{
			vectors.add(new int[] { 1, 1, -1 });
			vectors.add(new int[] { 1, -1, -1 });
			vectors.add(new int[] { -1, -1, -1 });
			vectors.add(new int[] { -1, 1, -1 });
		}
		return vectors;
	}

	/**
	 * Convenience method.
	 */
	public int[] diagonalDir3D(Coordinate c1, Coordinate c2, int maxDistance)
	{
		return diagonalDir(c1, c2, 3, maxDistance);
	}

	public Set<int[]> knightVectors3D()
	{
		Set<int[]> vectors = new HashSet<int[]>();
		vectors.add(new int[] { 0, 1, 2 });
		vectors.add(new int[] { 0, 2, 1 });
		vectors.add(new int[] { 0, 2, -1 });
		vectors.add(new int[] { 0, 1, -2 });
		vectors.add(new int[] { 0, -1, -2 });
		vectors.add(new int[] { 0, -2, -1 });
		vectors.add(new int[] { 0, -2, 1 });
		vectors.add(new int[] { 0, -1, 2 });
		vectors.add(new int[] { 1, 0, 2 });
		vectors.add(new int[] { 2, 0, 1 });
		vectors.add(new int[] { 2, 0, -1 });
		vectors.add(new int[] { 1, 0, -2 });
		vectors.add(new int[] { -1, 0, -2 });
		vectors.add(new int[] { -2, 0, -1 });
		vectors.add(new int[] { -2, 0, 1 });
		vectors.add(new int[] { -1, 0, 2 });
		return vectors;
	}

	public boolean knight3D(Coordinate source, Coordinate target)
	{
		int[] d = coordinateDiff(source, target);

		d[0] = Math.abs(d[0]);
		d[1] = Math.abs(d[1]);
		d[2] = Math.abs(d[2]);

		if (d[0] == 0)
		{
			if (d[1] < 1 || d[1] > 2 || d[2] < 1 || d[2] > 2 || d[1] + d[2] != 3)
				return false;
		}
		else if (d[1] == 0)
		{
			if (d[0] < 1 || d[0] > 2 || d[2] < 1 || d[2] > 2 || d[0] + d[2] != 3)
				return false;
		}
		else
		{
			return false;
		}

		return true;
	}

	public Set<int[]> griffinVectors()
	{
		Set<int[]> vectors = new HashSet<int[]>();
		vectors.add(new int[] { 2, 3 });
		vectors.add(new int[] { 3, 2 });
		vectors.add(new int[] { 3, -2 });
		vectors.add(new int[] { 2, -3 });
		vectors.add(new int[] { -2, -3 });
		vectors.add(new int[] { -3, -2 });
		vectors.add(new int[] { -3, 2 });
		vectors.add(new int[] { -2, 3 });
		return vectors;
	}

	public boolean griffin(Coordinate c1, Coordinate c2)
	{
		int[] d = coordinateDiff(c1, c2);
		if (d[2] != 0)
			return false;
		d[0] = Math.abs(d[0]);
		d[1] = Math.abs(d[1]);
		if (d[0] < 2 || d[0] > 3 || d[1] < 2 || d[1] > 3 || d[0] + d[1] != 5)
			return false;
		return true;
	}

	public boolean isLayer(Coordinate target, int layer)
	{
		return getComponent(AXIS_LAYER, target) == layer;
	}

	public boolean isPointingUp(int[] vector)
	{
		return vector[AXIS_LAYER] < 0;
	}
}
