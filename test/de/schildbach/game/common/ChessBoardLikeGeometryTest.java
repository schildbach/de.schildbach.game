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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.schildbach.game.chess.ChessBoardGeometry;
import de.schildbach.game.common.ChessBoardLikeGeometry;

/**
 * @author Andreas Schildbach
 */
public class ChessBoardLikeGeometryTest
{
	private ChessBoardLikeGeometry geometry;

	@Before
	public void setup()
	{
		geometry = ChessBoardGeometry.instance();
	}

	@Test
	public void distance()
	{
		// knight-like
		assertEquals(2, geometry.distance(geometry.locateCoordinate("a1"), geometry.locateCoordinate("c2")));

		// orthogonal
		assertEquals(1, geometry.distance(geometry.locateCoordinate("a1"), geometry.locateCoordinate("b1")));
		assertEquals(1, geometry.distance(geometry.locateCoordinate("b1"), geometry.locateCoordinate("a1")));
		assertEquals(1, geometry.distance(geometry.locateCoordinate("a1"), geometry.locateCoordinate("a2")));
		assertEquals(1, geometry.distance(geometry.locateCoordinate("a2"), geometry.locateCoordinate("a1")));

		// diagonal
		assertEquals(1, geometry.distance(geometry.locateCoordinate("a1"), geometry.locateCoordinate("b2")));
		assertEquals(1, geometry.distance(geometry.locateCoordinate("b2"), geometry.locateCoordinate("a1")));

		// same square
		assertEquals(0, geometry.distance(geometry.locateCoordinate("e5"), geometry.locateCoordinate("e5")));
	}
}
