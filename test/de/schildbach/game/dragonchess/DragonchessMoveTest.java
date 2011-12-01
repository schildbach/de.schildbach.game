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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

import de.schildbach.game.GameMove;
import de.schildbach.game.GamePosition;

/**
 * @author Andreas Schildbach
 */
public class DragonchessMoveTest extends AbstractDragonchessTest
{
	@Test
	public void equals()
	{
		GamePosition position = defaultInitialPosition();
		GameMove move1 = move("1c2-1d3", position, null);
		GameMove move2 = move("1c2-1d3", position, null);
		assertTrue(move1.equals(move2));
		move1 = move("2d2-2d3", position, null);
		assertFalse(move1.equals(move2));
		move2 = move("2d2-2d3", position, null);
		assertTrue(move1.equals(move2));
	}

	@Test
	public void testHashCode()
	{
		GamePosition position = defaultInitialPosition();
		GameMove move1 = move("1c2-1d3", position, null);
		GameMove move2 = move("1c2-1d3", position, null);
		assertEquals(move1.hashCode(), move2.hashCode());
		move1 = move("2d2-2d3", position, null);
		move2 = move("2d2-2d3", position, null);
		assertEquals(move1.hashCode(), move2.hashCode());
	}

	@Test
	public void serialization() throws IOException, ClassNotFoundException
	{
		GamePosition position = defaultInitialPosition();
		GameMove move = move("1c2-1d3", position, null);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new ObjectOutputStream(bos).writeObject(move);

		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		GameMove copy = (GameMove) new ObjectInputStream(bis).readObject();

		assertEquals(move, copy);
		assertEquals(move.hashCode(), copy.hashCode());
	}
}
