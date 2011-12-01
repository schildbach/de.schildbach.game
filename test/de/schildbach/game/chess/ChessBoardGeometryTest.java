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

package de.schildbach.game.chess;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.Test;

import de.schildbach.game.Coordinate;
import de.schildbach.game.common.OrthogonalBoardGeometry;

/**
 * @author Andreas Schildbach
 */
public class ChessBoardGeometryTest
{
	private ChessBoardGeometry geometry;
	private Coordinate d1;
	private Coordinate g1;
	private Coordinate g4;
	private Coordinate d4;
	private Coordinate e8;

	@Before
	public void setup()
	{
		geometry = ChessBoardGeometry.instance();
		d1 = geometry.locateCoordinate("d1");
		g1 = geometry.locateCoordinate("g1");
		g4 = geometry.locateCoordinate("g4");
		d4 = geometry.locateCoordinate("d4");
		e8 = geometry.locateCoordinate("e8");
	}

	@Test
	public void geometricChecks()
	{
		assertTrue(geometry.isEastOf(g4, d1));
		assertTrue(geometry.isNorthOf(g4, d1));
	}

	@Test
	public void isAboveOrBelow()
	{
		assertTrue(geometry.isOnSameOrthogonal(OrthogonalBoardGeometry.AXIS_WIDTH, d1, d4));
		assertFalse(geometry.isOnSameOrthogonal(OrthogonalBoardGeometry.AXIS_WIDTH, g4, d1));
		assertFalse(geometry.isOnSameOrthogonal(OrthogonalBoardGeometry.AXIS_WIDTH, d4, g4));
	}

	@Test
	public void isAside()
	{
		assertFalse(geometry.isOnSameOrthogonal(OrthogonalBoardGeometry.AXIS_HEIGHT, d1, d4));
		assertTrue(geometry.isOnSameOrthogonal(OrthogonalBoardGeometry.AXIS_HEIGHT, d4, g4));
		assertFalse(geometry.isOnSameOrthogonal(OrthogonalBoardGeometry.AXIS_HEIGHT, g4, d1));
	}

	@Test
	public void diagonalDir()
	{
		assertArrayEquals(geometry.northeast(), geometry.diagonalDir2D(d1, g4));
		assertArrayEquals(geometry.southwest(), geometry.diagonalDir2D(g4, d1));
		assertArrayEquals(geometry.northwest(), geometry.diagonalDir2D(g1, d4));
		assertArrayEquals(geometry.southeast(), geometry.diagonalDir2D(d4, g1));

		assertNull(geometry.diagonalDir2D(d1, g1));
		assertNull(geometry.diagonalDir2D(g1, d1));
		assertNull(geometry.diagonalDir2D(d1, d4));
		assertNull(geometry.diagonalDir2D(d4, d1));

		assertNull(geometry.diagonalDir2D(d4, d4));
	}

	@Test
	public void orthogonalDir()
	{
		assertArrayEquals(geometry.east(), geometry.orthogonalDir2D(d1, g1));
		assertArrayEquals(geometry.west(), geometry.orthogonalDir2D(g1, d1));
		assertArrayEquals(geometry.north(), geometry.orthogonalDir2D(d1, d4));
		assertArrayEquals(geometry.south(), geometry.orthogonalDir2D(d4, d1));

		assertNull(geometry.orthogonalDir2D(d1, g4));
		assertNull(geometry.orthogonalDir2D(g4, d1));
		assertNull(geometry.orthogonalDir2D(g1, d4));
		assertNull(geometry.orthogonalDir2D(d4, g1));

		assertNull(geometry.orthogonalDir2D(d4, d4));
	}

	@Test
	public void maxNorthSouth()
	{
		assertTrue(geometry.isMaxNorth(e8));
		assertFalse(geometry.isMaxNorth(g4));
		assertFalse(geometry.isMaxNorth(d1));

		assertFalse(geometry.isMaxSouth(e8));
		assertFalse(geometry.isMaxSouth(g4));
		assertTrue(geometry.isMaxSouth(d1));
	}

	@Test
	public void serialization() throws IOException, ClassNotFoundException
	{
		Coordinate original = geometry.locateCoordinate("a4");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new ObjectOutputStream(bos).writeObject(original);

		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		Coordinate copy = (Coordinate) new ObjectInputStream(bis).readObject();

		assertEquals(original, copy);
	}
}
