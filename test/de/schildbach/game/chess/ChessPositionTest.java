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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

/**
 * @author Andreas Schildbach
 */
public class ChessPositionTest extends AbstractChessTest
{
	@Test
	public void toFromFEN()
	{
		String[] fens = { "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1",
				"rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2", "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2",
				"4k3/8/8/8/8/8/4P3/4K3 w - - 5 39" };

		for (int i = 0; i < fens.length; i++)
			assertEquals(fens[i], format(position(fens[i])));
	}

	@Test
	public void equals()
	{
		ChessPosition position1 = (ChessPosition) position("4k3/8/8/8/8/8/4P3/4K3 w - - 5 39");
		ChessPosition position2 = (ChessPosition) position("4k3/8/8/8/8/8/4P3/4K3 w - - 5 39");
		assertEquals(position1, position1);
		assertEquals(position1, position2);
		position2.setActivePlayerIndex(1);
		assertNotSame(position1, position2);
	}

	@Test
	public void testClone()
	{
		ChessPosition position = (ChessPosition) position("4k3/8/8/8/8/8/4P3/4K3 w - - 5 39");
		ChessPosition clone = (ChessPosition) position.clone();
		assertNotSame(position, clone);
		assertEquals(position, clone);
		clone.setFullmoveNumber(10);
		assertNotSame(position, clone);
		// assertNotEquals(position, clone);
	}

	@Test
	public void serialization() throws IOException, ClassNotFoundException
	{
		ChessPosition position = (ChessPosition) position("4k3/8/8/8/8/8/4P3/4K3 w - - 5 39");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new ObjectOutputStream(bos).writeObject(position);

		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ChessPosition position2 = (ChessPosition) new ObjectInputStream(bis).readObject();

		assertEquals(position, position2);
		assertEquals("4k3/8/8/8/8/8/4P3/4K3 w - - 5 39", format(position2));
	}
}
