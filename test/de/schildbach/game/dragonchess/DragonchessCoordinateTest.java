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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.schildbach.game.Coordinate;

/**
 * @author Andreas Schildbach
 */
public class DragonchessCoordinateTest
{
	private DragonchessBoardGeometry geometry;

	@Before
	public void setup()
	{
		geometry = DragonchessBoardGeometry.instance(12, 8, 3);
	}

	@Test
	public void equals()
	{
		Coordinate c1 = geometry.locateCoordinate("1c2");
		Coordinate c2 = geometry.locateCoordinate("1c2");
		assertTrue(c1.equals(c2));
		c1 = geometry.locateCoordinate("1d3");
		assertFalse(c1.equals(c2));
		c2 = geometry.locateCoordinate("1d3");
		assertTrue(c1.equals(c2));
	}

	@Test
	public void testHashCode()
	{
		Coordinate c1 = geometry.locateCoordinate("1c2");
		Coordinate c2 = geometry.locateCoordinate("1c2");
		assertEquals(c1.hashCode(), c2.hashCode());
		c1 = geometry.locateCoordinate("1d3");
		assertFalse(c1.hashCode() == c2.hashCode());
		c2 = geometry.locateCoordinate("1d3");
		assertEquals(c1.hashCode(), c2.hashCode());
	}
}
