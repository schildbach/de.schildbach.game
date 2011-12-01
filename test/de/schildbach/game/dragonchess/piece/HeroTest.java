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
public class HeroTest extends AbstractDragonchessPieceTest
{
	@Before
	public void setupPiece()
	{
		piece = new Hero(0);
	}

	@Test
	public void sky()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "2j8", "2l8", "2j6", "2l6" };

		assertPotentialTargets(board, "1k7", expectedTargets);
		assertIsThreateningSquare(board, "1k7", expectedTargets);
	}

	@Test
	public void ground()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "1b7", "1d7", "1b5", "1d5", //
				"2a8", "2b7", "2d7", "2e8", "2a4", "2b5", "2d5", "2e4", //
				"3b7", "3d7", "3b5", "3d5" };

		assertPotentialTargets(board, "2c6", expectedTargets);
		assertIsThreateningSquare(board, "2c6", expectedTargets);
	}

	@Test
	public void underworld()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "2j3", "2l3", "2j1", "2l1" };

		assertPotentialTargets(board, "3k2", expectedTargets);
		assertIsThreateningSquare(board, "3k2", expectedTargets);
	}
}
