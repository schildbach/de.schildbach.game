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
public class ElementalTest extends AbstractDragonchessPieceTest
{
	@Before
	public void setupPiece()
	{
		piece = new Elemental(0);
	}

	@Test
	public void ground()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "3i4", "3j5", "3k4", "3j3" };

		assertPotentialTargets(board, "2j4", expectedTargets);
		assertIsThreateningSquare(board, "2j4", expectedTargets);
	}

	@Test
	public void underworld()
	{
		Board board = geometry.newBoard();

		String[] expectedTargets = new String[] { "3c6", "3c5", "3c3", "3c2", //
				"3a4", "3b4", "3d4", "3e4", //
				"3b5", "3d5", "3d3", "3b3", // move only
				"2c3", "2d4", "2c5", "2b4" };

		assertPotentialTargets(board, "3c4", expectedTargets);

		expectedTargets = new String[] { "3c6", "3c5", "3c3", "3c2", //
				"3a4", "3b4", "3d4", "3e4", //
				"2c3", "2d4", "2c5", "2b4" };

		assertIsThreateningSquare(board, "3c4", expectedTargets);
	}
}
