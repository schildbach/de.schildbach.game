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
public class GriffinTest extends AbstractDragonchessPieceTest
{
	@Before
	public void setupPiece()
	{
		piece = new Griffin(0);
	}

	@Test
	public void sky()
	{
		assertAll("1d5", new String[] { "1a7", "1b8", "1f8", "1g7", "1a3", "1b2", "1f2", "1g3", "2c6", "2e6", "2c4", "2e4" });
	}

	@Test
	public void ground()
	{
		assertAll("2k2", new String[] { "2j3", "2l3", "2j1", "2l1", "1j3", "1l3", "1j1", "1l1" });
	}

	private void assertAll(String source, String[] expectedTargets)
	{
		Board board = geometry.newBoard();

		assertPotentialTargets(board, source, expectedTargets);
		assertIsThreateningSquare(board, source, expectedTargets);
	}
}
