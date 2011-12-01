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

import org.junit.Before;
import org.junit.Test;

import de.schildbach.game.Board;

/**
 * @author Andreas Schildbach
 */
public class SylphTest extends AbstractDragonchessPieceTest
{
	@Before
	public void setupPiece()
	{
		piece = new Sylph(0);
	}

	@Test
	public void sky()
	{
		assertAll("1d5", new String[] { "1c6", "1d6", "1e6", "2d5" });

		assertIsThreateningSquare(geometry.newBoard(), "1d5", new String[] { "1d6", "2d5" });
	}

	@Test
	public void ground()
	{
		assertAll("2j4", new String[] { "1j4" });

		assertIsThreateningSquare(geometry.newBoard(), "2j4", new String[0]);
	}

	private void assertAll(String source, String[] expectedTargets)
	{
		Board board = board(geometry, new String[] { "1d6", "2d5" }, new Sylph(1));

		assertPotentialTargets(board, source, expectedTargets);
	}
}
