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
import static org.junit.Assert.assertNotSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

import de.schildbach.game.GamePosition;
import de.schildbach.game.common.CapturingGamePosition;

/**
 * @author Andreas Schildbach
 */
public class CheckersPositionTest extends AbstractCheckersTest
{
	@Test
	public void toFromFEN()
	{
		String[] fens = { "mmmmm/mmmmm/mmmmm/mmmmm/5/5/MMMMM/MMMMM/MMMMM/MMMMM w 1", "5/5/5/1mkm1/1M3/2mm1/5/5/5/5 w 1" };

		for (int i = 0; i < fens.length; i++)
			assertEquals(fens[i], format(position(fens[i])));
	}

	@Test
	public void equals()
	{
		CapturingGamePosition position1 = (CapturingGamePosition) position("mmmmm/mmmmm/mmmmm/mmmmm/5/5/MMMMM/MMMMM/MMMMM/MMMMM w 1");
		CapturingGamePosition position2 = (CapturingGamePosition) position("mmmmm/mmmmm/mmmmm/mmmmm/5/5/MMMMM/MMMMM/MMMMM/MMMMM w 1");
		assertEquals(position1, position1);
		assertEquals(position1, position2);
		position2.setActivePlayerIndex(1);
		assertNotSame(position1, position2);
	}

	@Test
	public void testClone()
	{
		GamePosition position = defaultInitialPosition();
		GamePosition clone = (GamePosition) position.clone();
		assertEquals(position, clone);
		assertNotSame(position, clone);
		clone.setFullmoveNumber(10);
		assertEquals(defaultInitialPosition(), position);
	}

	@Test
	public void serializable() throws IOException, ClassNotFoundException
	{
		CapturingGamePosition position = (CapturingGamePosition) position("mmmmm/mmmmm/mmmmm/mmmmm/5/5/MMMMM/MMMMM/MMMMM/MMMMM w 1");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new ObjectOutputStream(bos).writeObject(position);

		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		CapturingGamePosition position2 = (CapturingGamePosition) new ObjectInputStream(bis).readObject();

		assertEquals(position, position2);
		assertEquals("mmmmm/mmmmm/mmmmm/mmmmm/5/5/MMMMM/MMMMM/MMMMM/MMMMM w 1", format(position2));
	}
}
