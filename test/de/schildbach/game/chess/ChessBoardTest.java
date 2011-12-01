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

package de.schildbach.game.chess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

import de.schildbach.game.Board;
import de.schildbach.game.chess.piece.King;
import de.schildbach.game.chess.piece.Pawn;
import de.schildbach.game.chess.piece.Rook;
import de.schildbach.game.common.FenFormat;
import de.schildbach.game.common.OrthogonalBoardGeometry;

/**
 * @author Andreas Schildbach
 */
public class ChessBoardTest extends AbstractChessTest
{
	@Test
	public void test()
	{
		// error reproduction
		Board board = geometry.newBoard();
		FenFormat.parse((OrthogonalBoardGeometry) geometry, pieceSet, "k7/8/8/8/8/8/8/7K", board);
		assertEquals("k7/8/8/8/8/8/8/7K", FenFormat.format((OrthogonalBoardGeometry) geometry, pieceSet, board));
		assertEquals(pieceSet.getPiece(King.class, 0), board.getPiece(coordinate("h1")));
		assertNull(board.getPiece(coordinate("h2")));
		assertNull(board.getPiece(coordinate("h3")));
	}

	@Test
	public void equals()
	{
		String fen = "4k3/8/8/8/8/8/4P3/4K3";
		Board board1 = geometry.newBoard();
		FenFormat.parse((OrthogonalBoardGeometry) geometry, pieceSet, fen, board1);
		board1.getPiece(coordinate("e5"));
		Board board2 = geometry.newBoard();
		FenFormat.parse((OrthogonalBoardGeometry) geometry, pieceSet, fen, board2);

		assertEquals(board1, board2);
		assertEquals(board1.hashCode(), board2.hashCode());

		board2.setPiece(coordinate("e5"), pieceSet.getPiece(Rook.class, 1));

		assertFalse(board1.equals(board2));
	}

	@Test
	public void equals2()
	{
		String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
		Board board1 = geometry.newBoard();
		FenFormat.parse((OrthogonalBoardGeometry) geometry, pieceSet, fen, board1);
		Board board2 = geometry.newBoard();
		FenFormat.parse((OrthogonalBoardGeometry) geometry, pieceSet, fen, board2);

		assertEquals(board1, board2);
		assertEquals(board1.hashCode(), board2.hashCode());

		board1.setPiece(coordinate("e5"), pieceSet.getPiece(Pawn.class, 0));

		assertFalse(board1.equals(board2));

		board2.setPiece(coordinate("e5"), pieceSet.getPiece(Pawn.class, 0));

		assertEquals(board1, board2);
		assertEquals(board1.hashCode(), board2.hashCode());

		board1.clearPiece(coordinate("a1"));

		assertFalse(board1.equals(board2));

		board2.clearPiece(coordinate("a1"));

		assertEquals(board1, board2);
		assertEquals(board1.hashCode(), board2.hashCode());
	}

	@Test
	public void testClone()
	{
		Board board = geometry.newBoard();
		FenFormat.parse((OrthogonalBoardGeometry) geometry, pieceSet, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", board);
		Board clone = (Board) board.clone();
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", FenFormat.format((OrthogonalBoardGeometry) geometry, pieceSet, clone));
		clone.clearPiece(coordinate("a1"));
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/1NBQKBNR", FenFormat.format((OrthogonalBoardGeometry) geometry, pieceSet, clone));
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", FenFormat.format((OrthogonalBoardGeometry) geometry, pieceSet, board));
	}

	@Test
	public void serialization() throws IOException, ClassNotFoundException
	{
		Board original = geometry.newBoard();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new ObjectOutputStream(bos).writeObject(original);

		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		Board copy = (Board) new ObjectInputStream(bis).readObject();

		assertEquals(original, copy);
		assertEquals(original.hashCode(), copy.hashCode());
	}
}
