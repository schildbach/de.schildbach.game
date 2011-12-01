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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Locale;

import org.junit.Test;

import de.schildbach.game.Board;
import de.schildbach.game.Game;
import de.schildbach.game.GameMove;
import de.schildbach.game.GamePosition;
import de.schildbach.game.chess.piece.Bishop;
import de.schildbach.game.chess.piece.King;
import de.schildbach.game.chess.piece.Knight;
import de.schildbach.game.chess.piece.Queen;
import de.schildbach.game.chess.piece.Rook;
import de.schildbach.game.common.piece.ChessLikePiece;
import de.schildbach.game.exception.ParseException;

/**
 * @author Andreas Schildbach
 */
public class ChessRulesTest extends AbstractChessTest
{
	@Test
	public void standardInitialPosition()
	{
		GamePosition position = defaultInitialPosition();
		assertTrue(position instanceof ChessPosition);
		assertTrue(position.getBoard() instanceof Board);
	}

	@Test
	public void getAllowedMoves()
	{
		// en passant
		GamePosition position = position("k6K/8/8/3Pp3/8/7B/8/8 w - e6 0 20");
		Collection<? extends GameMove> moves = allowedMoves(position, null);
		// pawn may capture en passant
		assertTrue(moves.contains(move("d5", "e6")));
		// bishop may not
		assertFalse(moves.contains(move("f3", "e6")));
	}

	@Test
	public void test()
	{
		GamePosition position = defaultInitialPosition();
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", rules.formatPosition(position));
		doMove(move("e2", "e3"), position, null);
		assertEquals("rnbqkbnr/pppppppp/8/8/8/4P3/PPPP1PPP/RNBQKBNR b KQkq - 0 1", rules.formatPosition(position));

		// capture
		position = position("rnbqkbnr/ppp1pppp/8/3p4/2P5/8/PP1PPPPP/RNBQKBNR w KQkq d6 0 2");
		doMove(move("c4", "d5"), position, null);
		assertEquals("rnbqkbnr/ppp1pppp/8/3P4/8/8/PP1PPPPP/RNBQKBNR b KQkq - 0 2", rules.formatPosition(position));

		// capture
		position = position("rnbqkbnr/ppp1pppp/8/3p4/2P5/1P6/P2PPPPP/RNBQKBNR b KQkq - 0 2");
		doMove(move("d5", "c4"), position, null);
		assertEquals("rnbqkbnr/ppp1pppp/8/8/2p5/1P6/P2PPPPP/RNBQKBNR w KQkq - 0 3", rules.formatPosition(position));
		doMove(move("b3", "c4"), position, null);
		assertEquals("rnbqkbnr/ppp1pppp/8/8/2P5/8/P2PPPPP/RNBQKBNR b KQkq - 0 3", rules.formatPosition(position));
	}

	@Test
	public void enPassantMove1()
	{
		GamePosition position = position("rnbqkbnr/ppppp1pp/8/8/5p2/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		doMove(move("e2", "e4"), position, null);
		assertEquals("rnbqkbnr/ppppp1pp/8/8/4Pp2/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", rules.formatPosition(position));
		// expectTargets("f4", "8/8/8/8/8/4xx2/8/8");
		doMove(move("f4", "e3"), position, null);
		assertEquals("rnbqkbnr/ppppp1pp/8/8/8/4p3/PPPP1PPP/RNBQKBNR w KQkq - 0 2", rules.formatPosition(position));
	}

	@Test
	public void enPassantMove2()
	{
		GamePosition position = position("6r1/1R3ppp/2pPk2n/p3P3/4KP2/P7/P5PP/8 b - f3 0 26");
		doMove(move("f7", "f5"), position, null);
		assertEquals("6r1/1R4pp/2pPk2n/p3Pp2/4KP2/P7/P5PP/8 w - f6 0 27", rules.formatPosition(position));
		// expectSources("8/8/8/4x3/4x3/8/8/8");
		// expectTargets("e5", "8/8/5x2/8/8/8/8/8");
		doMove(move("e5", "f6"), position, null);
		assertEquals("6r1/1R4pp/2pPkP1n/p7/4KP2/P7/P5PP/8 b - - 0 27", rules.formatPosition(position));
	}

	@Test
	public void castlingMoves()
	{
		// white kingside castling
		GamePosition position = position("8/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
		doMove(move("e1", "h1"), position, null);
		assertEquals("8/8/8/8/8/8/8/R4RK1 b kq - 1 1", rules.formatPosition(position));

		// white queenside castling
		position = position("8/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
		doMove(move("e1", "a1"), position, null);
		assertEquals("8/8/8/8/8/8/8/2KR3R b kq - 1 1", rules.formatPosition(position));
	}

	@Test
	public void movingWhiteKingShouldClearCastlingAvailability()
	{
		// moving white king clears castling availability
		GamePosition position = position("8/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
		doMove(move("e1", "d1"), position, null);
		assertEquals("8/8/8/8/8/8/8/R2K3R b kq - 1 1", rules.formatPosition(position));
	}

	@Test
	public void movingWhiteA1RookShouldClearQueensideCastlingAvailability()
	{
		// moving white a1 rook clears queenside castling availability
		GamePosition position = position("8/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
		doMove(move("a1", "a8"), position, null);
		assertEquals("R7/8/8/8/8/8/8/4K2R b Kkq - 1 1", rules.formatPosition(position));
	}

	@Test
	public void movingBlackH1RookShouldClearKingsideCastlingAvailability()
	{
		// moving black h1 rook clears kingside castling availability
		GamePosition position = position("8/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
		doMove(move("h1", "h8"), position, null);
		assertEquals("7R/8/8/8/8/8/8/R3K3 b Qkq - 1 1", rules.formatPosition(position));

		// moving black h1 rook clears kingside castling availability
		position = position("k7/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
		doMove(move("h1", "h3"), position, null);
		assertEquals("k7/8/8/8/8/7R/8/R3K3 b Qkq - 1 1", rules.formatPosition(position));
	}

	@Test
	public void promotionMoves()
	{
		GamePosition position = position("4k3/1P6/8/8/8/8/8/4K3 w - - 0 1");
		// expectTargets("b7", "1x6/8/8/8/8/8/8/8");
		ChessMove move = move("b7", "b8", Queen.class);
		doMove(move, position, null);
		assertEquals("1Q2k3/8/8/8/8/8/8/4K3 b - - 0 1", rules.formatPosition(position));

		position = position("4k3/1P6/8/8/8/8/1p6/4K3 b - - 0 1");
		// expectTargets("b2", "8/8/8/8/8/8/8/1x6");
		move = move("b2", "b1", Knight.class);
		doMove(move, position, null);
		assertEquals("4k3/1P6/8/8/8/8/8/1n2K3 w - - 0 2", rules.formatPosition(position));
	}

	@Test
	public void canDrawBeClaimed()
	{
		Game game = game(null);
		assertFalse(rules.canDrawBeClaimed(game));
	}

	@Test
	public void isThreateningSquare()
	{
		Board board = geometry.newBoard();
		ChessLikePiece piece = (ChessLikePiece) rules.getPieceSet().getPiece(Bishop.class, 1);
		assertTrue(piece.isThreateningSquare((ChessBoardGeometry) geometry, board, coordinate("g4"), coordinate("d1")));
	}

	@Test
	public void allQueensideCastlingCombinations()
	{
		for (char rookFile = 'a'; rookFile <= 'f'; rookFile++)
		{
			for (char kingFile = (char) (rookFile + 1); kingFile <= 'g'; kingFile++)
			{
				GamePosition position = position("k7/8/8/8/8/8/8/7R w KQkq - 0 1");
				String rookCoordinate = rookFile + "1";
				Board board = position.getBoard();
				board.setPiece(coordinate(rookCoordinate), pieceSet.getPiece(Rook.class, 0));
				String kingCoordinate = kingFile + "1";
				board.setPiece(coordinate(kingCoordinate), pieceSet.getPiece(King.class, 0));
				ChessMove move = move(kingCoordinate, rookCoordinate);
				Collection<? extends GameMove> allowed = rules.allowedMoves(position, position.getBoard());
				if (!allowed.contains(move))
					fail(allowed.toString());
				doOperations(rules.disassembleMove(move, position, board), position);
				assertEquals(move.toString(), "k7/8/8/8/8/8/8/2KR3R b kq - 1 1", format(position));
			}
		}
	}

	@Test
	public void allKingsideCastlingCombinations()
	{
		for (char kingFile = 'b'; kingFile <= 'g'; kingFile++)
		{
			for (char rookFile = (char) (kingFile + 1); rookFile <= 'h'; rookFile++)
			{
				GamePosition position = position("k7/8/8/8/8/8/8/R7 w KQkq - 0 1");
				String kingCoordinate = kingFile + "1";
				position.getBoard().setPiece(coordinate(kingCoordinate), pieceSet.getPiece(King.class, 0));
				String rookCoordinate = rookFile + "1";
				position.getBoard().setPiece(coordinate(rookCoordinate), pieceSet.getPiece(Rook.class, 0));
				ChessMove move = move(kingCoordinate, rookCoordinate);
				Collection<? extends GameMove> allowed = rules.allowedMoves(position, position.getBoard());
				if (!allowed.contains(move))
					fail(allowed.toString());
				doOperations(rules.disassembleMove(move, position, position.getBoard()), position);
				assertEquals(move.toString(), "k7/8/8/8/8/8/8/R4RK1 b kq - 1 1", format(position));
			}
		}
	}

	private static final String BASE = "1. e4 e5 2. f4 exf4 3. Nf3 d6 4. Bc4 Bg4 5. O-O c6 6. d4 d5 7. exd5 cxd5 8. Bb5+ Bd7 9. Re1+ Ne7 "
			+ "10. Nc3 a6 11. Bxd7+ Qxd7 12. Ne5 Qf5 13. Nxd5 Nxd5 14. Ng6+ Be7";
	private static final String MOVE_BEFORE = "15. Nxh8"; // capture rook
	private static final String CASTLING = "O-O"; // this move is invalid, because rook is captured

	@Test
	public void parseExceptionBehaviour1()
	{
		String acn = BASE;
		Game game = game(null, acn);
		assertEquals(acn, rules.formatGame(game, Locale.ENGLISH));
		assertNull(rules.executeMoves(game, MOVE_BEFORE, Locale.ENGLISH));
		assertEquals(CASTLING, rules.executeMoves(game, CASTLING, Locale.ENGLISH));

		acn = BASE;
		game = game(null, acn);
		assertEquals(acn, rules.formatGame(game, Locale.ENGLISH));
		assertEquals(CASTLING, rules.executeMoves(game, MOVE_BEFORE + " " + CASTLING, Locale.ENGLISH));
	}

	@Test(expected = ParseException.class)
	public void parseExceptionBehaviour2()
	{
		String acn = BASE + " " + MOVE_BEFORE + " " + CASTLING;
		game(null, acn);
	}
}
