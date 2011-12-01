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

package de.schildbach.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.schildbach.game.chess.ChessBoardGeometry;
import de.schildbach.game.chess.piece.Pawn;

/**
 * @author Andreas Schildbach
 */
public class BoardTest
{
	private static final Pawn WHITE_PAWN = new Pawn(0);
	private static final Pawn BLACK_PAWN = new Pawn(1);

	private ChessBoardGeometry geometry;
	private Board board;

	@Before
	public void setup()
	{
		geometry = ChessBoardGeometry.instance(2, 2);
		board = geometry.newBoard();
		board.setPiece(geometry.locateCoordinate("a1"), WHITE_PAWN);
	}

	@Test
	public void locateEmptyFields()
	{
		Set<Coordinate> fields = new HashSet<Coordinate>(board.locateEmptyFields());

		assertTrue(fields.remove(geometry.locateCoordinate("a2")));
		assertTrue(fields.remove(geometry.locateCoordinate("b1")));
		assertTrue(fields.remove(geometry.locateCoordinate("b2")));
		assertTrue(fields.isEmpty());
	}

	@Test
	public void locateOccupiedFields()
	{
		Set<Coordinate> fields = new HashSet<Coordinate>(board.locateOccupiedFields());

		assertTrue(fields.remove(geometry.locateCoordinate("a1")));
		assertTrue(fields.isEmpty());
	}

	@Test
	public void locatePieces()
	{
		board.setPiece(geometry.locateCoordinate("a2"), WHITE_PAWN);
		board.setPiece(geometry.locateCoordinate("b1"), BLACK_PAWN);

		Set<Coordinate> fields = new HashSet<Coordinate>(board.locatePieces(WHITE_PAWN));

		assertTrue(fields.remove(geometry.locateCoordinate("a1")));
		assertTrue(fields.remove(geometry.locateCoordinate("a2")));
		assertTrue(fields.isEmpty());

		board.clearPiece(geometry.locateCoordinate("a2"));

		fields = new HashSet<Coordinate>(board.locatePieces(WHITE_PAWN));

		assertTrue(fields.remove(geometry.locateCoordinate("a1")));
		assertTrue(fields.isEmpty());
	}

	@Test
	public void cloneBoard()
	{
		Board clone = (Board) board.clone();

		assertEquals(board, clone);
		assertEquals(board.hashCode(), clone.hashCode());
		assertEquals(board.locateEmptyFields(), clone.locateEmptyFields());
	}
}
