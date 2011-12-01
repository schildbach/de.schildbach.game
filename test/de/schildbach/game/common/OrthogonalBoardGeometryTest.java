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

package de.schildbach.game.common;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * @author Andreas Schildbach
 */
public class OrthogonalBoardGeometryTest
{
	@Test
	public void dir()
	{
		OrthogonalBoardGeometry geometry = new OrthogonalBoardGeometry(new int[] { 8, 8 }, new CoordinateFieldNotations('a', '1'));

		// knight-like move
		assertArrayEquals(new int[] { 2, 1 }, geometry.dir(new int[] { 2, 1 }, geometry.locateCoordinate("a1"), geometry.locateCoordinate("c2")));
		assertNull(geometry.dir(new int[] { 1, 2 }, geometry.locateCoordinate("a1"), geometry.locateCoordinate("c2")));

		// orthogonal move
		assertArrayEquals(new int[] { 0, -1 }, geometry.dir(new int[] { 0, -1 }, geometry.locateCoordinate("d4"), geometry.locateCoordinate("d3")));
		assertArrayEquals(new int[] { 0, -1 }, geometry.dir(new int[] { 0, 1 }, geometry.locateCoordinate("d4"), geometry.locateCoordinate("d3")));
		assertNull(geometry.dir(new int[] { -1, -1 }, geometry.locateCoordinate("d4"), geometry.locateCoordinate("d3")));

		// diagonal move
		assertArrayEquals(new int[] { -1, 1 }, geometry.dir(new int[] { -1, 1 }, geometry.locateCoordinate("e4"), geometry.locateCoordinate("d5")));
		assertArrayEquals(new int[] { -1, 1 }, geometry.dir(new int[] { 1, -1 }, geometry.locateCoordinate("e4"), geometry.locateCoordinate("d5")));

		// same square
		assertArrayEquals(new int[] { 0, 0 }, geometry.dir(new int[] { 0, 0 }, geometry.locateCoordinate("e5"), geometry.locateCoordinate("e5")));
	}

	@Test
	public void computeCoordinateKey()
	{
		OrthogonalBoardGeometry geometry = new OrthogonalBoardGeometry(new int[] { 12, 8, 3 }, new BigCoordinateFieldNotations());

		assertEquals(0, geometry.computeCoordinateKey(new int[] { 0, 7, 0 }));
		assertEquals(1, geometry.computeCoordinateKey(new int[] { 1, 7, 0 }));
		assertEquals(287, geometry.computeCoordinateKey(new int[] { 11, 0, 2 }));
	}
}
