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
public class DwarfTest extends AbstractDragonchessPieceTest
{
	@Before
	public void setupPiece()
	{
		piece = new Dwarf(0);
	}

	@Test
	public void ground()
	{
		assertAll("2j3", new String[] { "2i3", "2i4", "2j4", "2k4", "2k3", //
				"3j3" });

		assertIsThreateningSquare(geometry.newBoard(), "2j3", new String[] { "2i4", "2k4" });
	}

	@Test
	public void underworld()
	{
		assertAll("3c3", new String[] { "3b3", "3b4", "3c4", "3d4", "3d3", //
				"2c3" });

		assertIsThreateningSquare(geometry.newBoard(), "3c3", new String[] { "3b4", "3d4", "2c3" });
	}

	private void assertAll(String source, String[] expectedTargets)
	{
		Board board = board(geometry, new String[] { "2i4", "2k4", "2c3", "3b4", "3d4" }, new Dwarf(1));

		assertPotentialTargets(board, source, expectedTargets);
	}
}
