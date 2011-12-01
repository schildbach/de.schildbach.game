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

package de.schildbach.game.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import de.schildbach.game.Board;
import de.schildbach.game.Coordinate;
import de.schildbach.game.Piece;
import de.schildbach.game.chess.ChessBoardGeometry;
import de.schildbach.game.chess.ChessPosition;
import de.schildbach.game.chess.piece.King;
import de.schildbach.game.chess.piece.Queen;

/**
 * @author Andreas Schildbach
 */
public class OperationTest
{
	@Test
	public void swapOutPieceOperationShouldSetAndUnsetPiece()
	{
		ChessBoardGeometry geometry = ChessBoardGeometry.instance(8, 8);
		Piece queen = new Queen(0);
		Piece king = new King(0);
		Coordinate coordinate = geometry.locateCoordinate("a4");
		Board board = geometry.newBoard();
		board.setPiece(coordinate, queen);
		ChessPosition position = new ChessPosition(board);

		SwapOutPieceOperation op = new SwapOutPieceOperation(coordinate, king);

		assertSame(queen, board.getPiece(coordinate));
		op.doOperation(position);
		assertSame(king, board.getPiece(coordinate));
		op.undoOperation(position);
		assertSame(queen, board.getPiece(coordinate));
	}

	@Test
	public void capturePieceOperation()
	{
		ChessBoardGeometry geometry = ChessBoardGeometry.instance(8, 8);
		Piece queen = new Queen(0);
		Coordinate coordinate = geometry.locateCoordinate("a4");
		Board board = geometry.newBoard();
		board.setPiece(coordinate, queen);
		ChessPosition position = new ChessPosition(board);

		CapturePieceOperation op = new CapturePieceOperation(coordinate);

		assertSame(queen, board.getPiece(coordinate));
		assertEquals(0, position.getCapturedPieces(0).length);
		assertEquals(0, position.getCapturedPieces(1).length);
		op.doOperation(position);
		assertNull(board.getPiece(coordinate));
		assertEquals(1, position.getCapturedPieces(0).length);
		assertSame(queen, position.getCapturedPieces(0)[0]);
		assertEquals(0, position.getCapturedPieces(1).length);
		op.undoOperation(position);
		assertSame(queen, board.getPiece(coordinate));
		assertEquals(0, position.getCapturedPieces(0).length);
		assertEquals(0, position.getCapturedPieces(1).length);
	}
}
