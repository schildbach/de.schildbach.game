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

package de.schildbach.game.checkers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Andreas Schildbach
 */
public class CheckersMoveTest extends AbstractCheckersTest
{
	@Test
	public void testClone()
	{
		CheckersMove move = move("1", "12", "7");
		CheckersMove clone = new CheckersMove(move);
		assertTrue(move.equals(clone));
		assertTrue(clone.equals(move));
		assertEquals(move.hashCode(), clone.hashCode());

		move = move("28", "19", "23");
		clone = new CheckersMove(move);
		assertTrue(move.equals(clone));
		assertTrue(clone.equals(move));
		assertEquals(move.hashCode(), clone.hashCode());
	}

	@Test
	public void equals()
	{
		CheckersMove move = capture("1", "12", "7");

		CheckersMove fake = capture("2", "12", "7");
		assertFalse(move.equals(fake));
		assertFalse(fake.equals(move));

		fake = capture("1", "11", "7");
		assertFalse(move.equals(fake));
		assertFalse(fake.equals(move));

		fake = capture("1", "12", "7");
		assertTrue(move.equals(fake));
		assertTrue(fake.equals(move));

		fake = move("1", "12", "7", "50");
		assertFalse(move.equals(fake));
		assertFalse(fake.equals(move));

		fake = capture("1", "12", "6");
		assertTrue(move.equals(fake));
		assertTrue(fake.equals(move));

		fake = move("1", "12");
		assertTrue(move.equals(fake));
		assertTrue(fake.equals(move));
	}
}
