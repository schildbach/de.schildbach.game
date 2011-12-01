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

package de.schildbach.game.chess.piece;

import org.junit.Before;
import org.junit.Test;

import de.schildbach.game.Board;

/**
 * @author Andreas Schildbach
 */
public class PawnTest extends AbstractChessPieceTest
{
	@Before
	public void setupPiece()
	{
		piece = new Pawn(0);
	}

	@Test
	public void ground()
	{
		assertAll("f4", new String[] { "e5", "f5", "g5" });

		assertIsThreateningSquare(geometry.newBoard(), "f4", new String[] { "e5", "g5" });
	}

	private void assertAll(String source, String[] expectedTargets)
	{
		Board board = board(geometry, new String[] { "e5", "g5" }, new Pawn(1));

		assertPotentialTargets(board, source, expectedTargets);
	}
}
