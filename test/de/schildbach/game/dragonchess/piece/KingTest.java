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
public class KingTest extends AbstractDragonchessPieceTest
{
	@Before
	public void setupPiece()
	{
		piece = new King(0);
	}

	@Test
	public void sky()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "2b7" };

		assertPotentialTargets(board, "1b7", expectedTargets);
		assertIsThreateningSquare(board, "1b7", expectedTargets);
	}

	@Test
	public void ground()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "2e6", "2f6", "2g6", "2e5", "2g5", "2e4", "2f4", "2g4", //
				"1f5", "3f5" };

		assertPotentialTargets(board, "2f5", expectedTargets);
		assertIsThreateningSquare(board, "2f5", expectedTargets);
	}

	@Test
	public void underworld()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "2j5" };

		assertPotentialTargets(board, "3j5", expectedTargets);
		assertIsThreateningSquare(board, "3j5", expectedTargets);
	}
}
