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
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import de.schildbach.game.Board;
import de.schildbach.game.GameMove;
import de.schildbach.game.GamePosition;
import de.schildbach.game.MicroOperation;

/**
 * @author Andreas Schildbach
 */
public class ChessMoveTest extends AbstractChessTest
{
	@Test
	public void execute_casteling()
	{
		ChessPosition position = (ChessPosition) position("k7/8/8/8/8/8/8/4K2R w K - 1 1");

		ChessMove move = (ChessMove) move("e1-h1", position, null);
		assertTrue(((ChessRules) rules).looksLikeCastlingMove(move, position.getBoard()));
		doMove(move, position, null);
		assertEquals("k7/8/8/8/8/8/8/5RK1 b - - 2 1", rules.formatPosition(position));
	}

	@Test
	public void execute_enPassant()
	{
		ChessPosition position = (ChessPosition) position("2kr2nr/pppq1p2/7p/4P1p1/1bPQ2B1/6PP/PP3P2/RNB2K1R b - - 0 13");

		ChessMove enPassantMove = (ChessMove) move("f7-f5", position, null);
		assertFalse(((ChessRules) rules).looksLikeEnPassantCapture(enPassantMove, position.getBoard()));
		assertTrue(((ChessRules) rules).looksLikeEnPassantMove(enPassantMove, position.getBoard()));
		doMove(enPassantMove, position, null);
		assertEquals("2kr2nr/pppq4/7p/4Ppp1/1bPQ2B1/6PP/PP3P2/RNB2K1R w - f6 0 14", rules.formatPosition(position));

		ChessMove enPassantCapture = (ChessMove) move("e5-f6", position, null);
		assertTrue(((ChessRules) rules).looksLikeEnPassantCapture(enPassantCapture, position.getBoard()));
		assertFalse(((ChessRules) rules).looksLikeEnPassantMove(enPassantCapture, position.getBoard()));
		doMove(enPassantCapture, position, null);
		assertEquals("2kr2nr/pppq4/5P1p/6p1/1bPQ2B1/6PP/PP3P2/RNB2K1R b - - 0 14", rules.formatPosition(position));
	}

	@Test
	public void execute_german()
	{
		GamePosition position = defaultInitialPosition();
		Board initialBoard = defaultInitialBoard();

		doMove(((ChessMove) rules.parseMove("a4", Locale.GERMAN, position, initialBoard)), position, null);
		doMove(((ChessMove) rules.parseMove("e5", Locale.GERMAN, position, initialBoard)), position, null);
		doMove(((ChessMove) rules.parseMove("e4", Locale.GERMAN, position, initialBoard)), position, null);
		doMove(((ChessMove) rules.parseMove("h5", Locale.GERMAN, position, initialBoard)), position, null);
		doMove(((ChessMove) rules.parseMove("h4", Locale.GERMAN, position, initialBoard)), position, null);
		doMove(((ChessMove) rules.parseMove("Sc6", Locale.GERMAN, position, initialBoard)), position, null);
		doMove(((ChessMove) rules.parseMove("Sc3", Locale.GERMAN, position, initialBoard)), position, null);

		assertEquals("r1bqkbnr/pppp1pp1/2n5/4p2p/P3P2P/2N5/1PPP1PP1/R1BQKBNR b KQkq - 2 4", rules.formatPosition(position));
	}

	@Test
	public void executeAndUndo_Marathon()
	{
		GamePosition position = defaultInitialPosition();

		// opening moves (1. d4 h5 2. d5 e5)
		doMove(((ChessMove) move("d4", position, null)), position, null);
		doMove(((ChessMove) move("h5", position, null)), position, null);
		doMove(((ChessMove) move("d5", position, null)), position, null);
		doMove(((ChessMove) move("e5", position, null)), position, null);

		// en passant capture
		ChessMove enPassantMove = (ChessMove) move("d5-e6", position, null);
		assertEquals("rnbqkbnr/pppp1pp1/8/3Pp2p/8/8/PPP1PPPP/RNBQKBNR w KQkq e6 0 3", rules.formatPosition(position));
		List<MicroOperation> ops = disassemble(enPassantMove, position, null);
		doOperations(ops, position);
		assertEquals("rnbqkbnr/pppp1pp1/4P3/7p/8/8/PPP1PPPP/RNBQKBNR b KQkq - 0 3", rules.formatPosition(position));
		undoOperations(ops, position);
		assertEquals("rnbqkbnr/pppp1pp1/8/3Pp2p/8/8/PPP1PPPP/RNBQKBNR w KQkq e6 0 3", rules.formatPosition(position));
		doMove(enPassantMove, position, null);
		assertEquals("rnbqkbnr/pppp1pp1/4P3/7p/8/8/PPP1PPPP/RNBQKBNR b KQkq - 0 3", rules.formatPosition(position));

		// moves inbetween (3... d5 4. h4 Bc5 5. Bf4 Nf6 6. Qd4 a5 7. Nc3 Qd6 8.
		// e4 Na6 9. a4 Bd7 10. e5)
		doMove(((ChessMove) move("d5", position, null)), position, null);
		doMove(((ChessMove) move("h4", position, null)), position, null);
		doMove(((ChessMove) move("Bc5", position, null)), position, null);
		doMove(((ChessMove) move("Bf4", position, null)), position, null);
		doMove(((ChessMove) move("Nf6", position, null)), position, null);
		doMove(((ChessMove) move("Qd4", position, null)), position, null);
		doMove(((ChessMove) move("a5", position, null)), position, null);
		doMove(((ChessMove) move("Nc3", position, null)), position, null);
		doMove(((ChessMove) move("Qd6", position, null)), position, null);
		doMove(((ChessMove) move("e4", position, null)), position, null);
		doMove(((ChessMove) move("Na6", position, null)), position, null);
		doMove(((ChessMove) move("a4", position, null)), position, null);
		doMove(((ChessMove) move("Bd7", position, null)), position, null);
		doMove(((ChessMove) move("e5", position, null)), position, null);

		// forfeit kingside casteling
		ChessMove forfeitKingsideCastelingMove = (ChessMove) move("h8-h7", position, null);
		assertEquals("r3k2r/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR b KQkq - 0 10", rules.formatPosition(position));
		ops = disassemble(forfeitKingsideCastelingMove, position, null);
		doOperations(ops, position);
		assertEquals("r3k3/1ppb1ppr/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR w KQq - 1 11", rules.formatPosition(position));
		undoOperations(ops, position);
		assertEquals("r3k2r/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR b KQkq - 0 10", rules.formatPosition(position));
		ChessMove kingsideCastelingMove = (ChessMove) move("O-O", position, null);
		ops = disassemble(kingsideCastelingMove, position, null);
		doOperations(ops, position);
		assertEquals("r4rk1/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR w KQ - 1 11", rules.formatPosition(position));
		undoOperations(ops, position);
		assertEquals("r3k2r/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR b KQkq - 0 10", rules.formatPosition(position));

		// forfeit queenside casteling
		ChessMove forfeitQueensideCastelingMove = (ChessMove) move("a8-a7", position, null);
		ops = disassemble(forfeitQueensideCastelingMove, position, null);
		doOperations(ops, position);
		assertEquals("4k2r/rppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR w KQk - 1 11", rules.formatPosition(position));
		undoOperations(ops, position);
		assertEquals("r3k2r/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR b KQkq - 0 10", rules.formatPosition(position));
		ChessMove queensideCastelingMove = (ChessMove) move("O-O-O", position, null);
		ops = disassemble(queensideCastelingMove, position, null);
		doOperations(ops, position);
		assertEquals("2kr3r/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR w KQ - 1 11", rules.formatPosition(position));
		undoOperations(ops, position);
		assertEquals("r3k2r/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR b KQkq - 0 10", rules.formatPosition(position));

		// forfeit casteling
		GameMove forfeitCastelingMove = move("e8-e7", position, null);
		ops = disassemble(forfeitCastelingMove, position, null);
		doOperations(ops, position);
		assertEquals("r6r/1ppbkpp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR w KQ - 1 11", rules.formatPosition(position));
		undoOperations(ops, position);
		assertEquals("r3k2r/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR b KQkq - 0 10", rules.formatPosition(position));
		ops = disassemble(kingsideCastelingMove, position, null);
		doOperations(ops, position);
		assertEquals("r4rk1/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR w KQ - 1 11", rules.formatPosition(position));
		undoOperations(ops, position);
		assertEquals("r3k2r/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR b KQkq - 0 10", rules.formatPosition(position));
		ops = disassemble(queensideCastelingMove, position, null);
		doOperations(ops, position);
		assertEquals("2kr3r/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR w KQ - 1 11", rules.formatPosition(position));
		undoOperations(ops, position);
		assertEquals("r3k2r/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR b KQkq - 0 10", rules.formatPosition(position));

		// casteling
		ops = disassemble(kingsideCastelingMove, position, null);
		doOperations(ops, position);
		assertEquals("r4rk1/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR w KQ - 1 11", rules.formatPosition(position));
		undoOperations(ops, position);
		assertEquals("r3k2r/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR b KQkq - 0 10", rules.formatPosition(position));
		ops = disassemble(queensideCastelingMove, position, null);
		doOperations(ops, position);
		assertEquals("2kr3r/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR w KQ - 1 11", rules.formatPosition(position));
		undoOperations(ops, position);
		assertEquals("r3k2r/1ppb1pp1/n2qPn2/p1bpP2p/P2Q1B1P/2N5/1PP2PP1/R3KBNR b KQkq - 0 10", rules.formatPosition(position));
		doMove(kingsideCastelingMove, position, null);

		// moves inbetween (11. e7 Bxd4)
		doMove(((ChessMove) move("e7", position, null)), position, null);
		doMove(((ChessMove) move("Bxd4", position, null)), position, null);

		// promotion
		ChessMove promotionMove = (ChessMove) move("e7-e8=R", position, null);
		ops = disassemble(promotionMove, position, null);
		doOperations(ops, position);
		assertEquals("r3Rrk1/1ppb1pp1/n2q1n2/p2pP2p/P2b1B1P/2N5/1PP2PP1/R3KBNR b KQ - 0 12", rules.formatPosition(position));
		undoOperations(ops, position);
		assertEquals("r4rk1/1ppbPpp1/n2q1n2/p2pP2p/P2b1B1P/2N5/1PP2PP1/R3KBNR w KQ - 0 12", rules.formatPosition(position));
		doMove(promotionMove, position, null);

		// last position
		assertEquals("r3Rrk1/1ppb1pp1/n2q1n2/p2pP2p/P2b1B1P/2N5/1PP2PP1/R3KBNR b KQ - 0 12", rules.formatPosition(position));
	}

	@Test
	public void execute_bug()
	{
		String fen = "r3k2r/pb1nqp1p/2pp1npQ/1p2p3/3PP3/P1N2P2/1PP1N1PP/2KR1B1R w kq - 2 12";
		ChessPosition position = (ChessPosition) position(fen);

		ChessMove move = (ChessMove) move("c1-d2", position, null);
		List<MicroOperation> ops = disassemble(move, position, null);
		doOperations(ops, position);
		assertEquals("r3k2r/pb1nqp1p/2pp1npQ/1p2p3/3PP3/P1N2P2/1PPKN1PP/3R1B1R b kq - 3 12", rules.formatPosition(position));
		undoOperations(ops, position);
		assertEquals(fen, rules.formatPosition(position));
	}

	@Test
	public void serialization() throws IOException, ClassNotFoundException
	{
		ChessMove original = move("a1", "h4");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new ObjectOutputStream(bos).writeObject(original);

		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ChessMove copy = (ChessMove) new ObjectInputStream(bis).readObject();

		assertEquals(original, copy);
	}
}
