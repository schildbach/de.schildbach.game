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
public class PaladinTest extends AbstractDragonchessPieceTest
{
	@Before
	public void setupPiece()
	{
		piece = new Paladin(0);
	}

	@Test
	public void sky()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "1b7", "1c7", "1d7", "1b6", "1d6", "1b5", "1c5", "1d5", //
				"2c8", "2a6", "2e6", "2c4", //
				"3c7", "3b6", "3d6", "3c5" };

		assertPotentialTargets(board, "1c6", expectedTargets);
		assertIsThreateningSquare(board, "1c6", expectedTargets);
	}

	@Test
	public void ground()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "2e5", "2g5", "2d4", "2e4", "2f4", "2g4", "2h4", "2e3", "2g3", "2d2", "2e2", "2f2", "2g2", "2h2",
				"2e1", "2g1", //
				"1f5", "1d3", "1h3", "1f1",//
				"3f5", "3d3", "3h3", "3f1" };

		assertPotentialTargets(board, "2f3", expectedTargets);
		assertIsThreateningSquare(board, "2f3", expectedTargets);
	}

	@Test
	public void underworld()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "3i7", "3j7", "3k7", "3i6", "3k6", "3i5", "3j5", "3k5", //
				"2j8", "2h6", "2l6", "2j4",//
				"1j7", "1i6", "1k6", "1j5" };

		assertPotentialTargets(board, "3j6", expectedTargets);
		assertIsThreateningSquare(board, "3j6", expectedTargets);
	}
}
