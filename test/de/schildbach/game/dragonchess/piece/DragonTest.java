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
public class DragonTest extends AbstractDragonchessPieceTest
{
	@Before
	public void setupPiece()
	{
		piece = new Dragon(0);
	}

	@Test
	public void sky()
	{
		Board board = board(geometry, new String[] { "2f6", "2e5", "2f5", "2g5", "2f4" }, new Dragon(1));

		String[] expectedTargets = new String[] { "1c8", "1d7", "1e6", "1f6", "1g6", "1h7", "1i8", //
				"1e5", "1g5", //
				"1b1", "1c2", "1d3", "1e4", "1f4", "1g4", "1h3", "1i2", "1j1", //
				"2f6", "2e5", "2f5", "2g5", "2f4" };
		assertPotentialTargets(board, "1f5", expectedTargets);
		assertIsThreateningSquare(board, "1f5", expectedTargets);
	}
}
