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
public class ClericTest extends AbstractDragonchessPieceTest
{
	@Before
	public void setupPiece()
	{
		piece = new Cleric(0);
	}

	@Test
	public void sky()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "1a8", "1b8", "1c8", "1a7", "1c7", "1a6", "1b6", "1c6", "2b7" };

		assertPotentialTargets(board, "1b7", expectedTargets);
		assertIsThreateningSquare(board, "1b7", expectedTargets);
	}

	@Test
	public void ground()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "2d3", "2e3", "2f3", "2d2", "2f2", "2d1", "2e1", "2f1", "1e2", "3e2" };

		assertPotentialTargets(board, "2e2", expectedTargets);
		assertIsThreateningSquare(board, "2e2", expectedTargets);
	}

	@Test
	public void underworld()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "3j3", "3k3", "3l3", "3j2", "3l2", "3j1", "3k1", "3l1", "2k2" };

		assertPotentialTargets(board, "3k2", expectedTargets);
		assertIsThreateningSquare(board, "3k2", expectedTargets);
	}
}
