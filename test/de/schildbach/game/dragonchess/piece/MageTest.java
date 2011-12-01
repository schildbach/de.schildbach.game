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
public class MageTest extends AbstractDragonchessPieceTest
{
	@Before
	public void setupPiece()
	{
		piece = new Mage(0);
	}

	@Test
	public void sky()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "1b8", "1a7", "1c7", "1b6", "2b7", "3b7" };

		assertPotentialTargets(board, "1b7", expectedTargets);
		assertIsThreateningSquare(board, "1b7", expectedTargets);
	}

	@Test
	public void ground()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "2a5", "2b5", "2c5", "2d5", "2e5", "2g5", "2h5", "2i5", "2j5", "2k5", "2l5", //
				"2f8", "2f7", "2f6", "2f4", "2f3", "2f2", "2f1", //
				"2c8", "2d7", "2e6", "2f6", "2g6", "2h7", "2i8", //
				"2b1", "2c2", "2d3", "2e4", "2f4", "2g4", "2h3", "2i2", "2j1", //
				"1f5", "3f5" };

		assertPotentialTargets(board, "2f5", expectedTargets);
		assertIsThreateningSquare(board, "2f5", expectedTargets);
	}

	@Test
	public void underworld()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "3k3", "3j2", "3l2", "3k1", "2k2", "1k2" };

		assertPotentialTargets(board, "3k2", expectedTargets);
		assertIsThreateningSquare(board, "3k2", expectedTargets);
	}
}
