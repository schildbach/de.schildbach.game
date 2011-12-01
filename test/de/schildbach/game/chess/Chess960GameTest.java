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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.schildbach.game.Board;
import de.schildbach.game.Game;
import de.schildbach.game.exception.ParseException;

/**
 * @author Andreas Schildbach
 */
public class Chess960GameTest extends AbstractChessTest
{
	@Test
	public void forfeitCastlingWithNonStandardInitialBoard()
	{
		Game game = game("qrkrnnbb/8/8/8/8/8/8/QRKRNNBB");
		Board initialBoard = game.getInitialPosition().getBoard();

		executeMove(game, move("b1-b2", game.getActualPosition(), initialBoard));
		assertFalse(((ChessPosition) game.getActualPosition()).getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertTrue(((ChessPosition) game.getActualPosition()).getCastlingAvailable(0, CastlingSide.KINGSIDE));

		executeMove(game, move("b8-b7", game.getActualPosition(), initialBoard));
		assertFalse(((ChessPosition) game.getActualPosition()).getCastlingAvailable(1, CastlingSide.QUEENSIDE));
		assertTrue(((ChessPosition) game.getActualPosition()).getCastlingAvailable(1, CastlingSide.KINGSIDE));

		executeMove(game, move("d1-d2", game.getActualPosition(), initialBoard));
		assertFalse(((ChessPosition) game.getActualPosition()).getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertFalse(((ChessPosition) game.getActualPosition()).getCastlingAvailable(0, CastlingSide.KINGSIDE));

		executeMove(game, move("d8-d7", game.getActualPosition(), initialBoard));
		assertFalse(((ChessPosition) game.getActualPosition()).getCastlingAvailable(1, CastlingSide.QUEENSIDE));
		assertFalse(((ChessPosition) game.getActualPosition()).getCastlingAvailable(1, CastlingSide.KINGSIDE));
	}

	@Test
	public void forfeitCastlingByCapturingWithNonStandardInitialBoard()
	{
		Game game = game("rqkrnnbb/8/8/8/8/8/8/RQKRNNBB");
		Board initialBoard = game.getInitialPosition().getBoard();

		executeMove(game, move("a1-a8", game.getActualPosition(), initialBoard));
		assertFalse(((ChessPosition) game.getActualPosition()).getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertFalse(((ChessPosition) game.getActualPosition()).getCastlingAvailable(1, CastlingSide.QUEENSIDE));
		assertTrue(((ChessPosition) game.getActualPosition()).getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertTrue(((ChessPosition) game.getActualPosition()).getCastlingAvailable(1, CastlingSide.KINGSIDE));

		executeMove(game, move("d8-d1", game.getActualPosition(), initialBoard));
		assertFalse(((ChessPosition) game.getActualPosition()).getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertFalse(((ChessPosition) game.getActualPosition()).getCastlingAvailable(1, CastlingSide.QUEENSIDE));
		assertFalse(((ChessPosition) game.getActualPosition()).getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(((ChessPosition) game.getActualPosition()).getCastlingAvailable(1, CastlingSide.KINGSIDE));
	}

	@Test(expected = ParseException.class)
	public void game60529()
	{
		Game game = game("rqkrbbnn/pppppppp/8/8/8/8/PPPPPPPP/RQKRBBNN", "1. c3 g6 2. Qc2 f5");
		Board initialBoard = game.getInitialPosition().getBoard();

		// throws ParseException, because castling is not possible
		move("c1-a1", game.getActualPosition(), initialBoard);
	}
}
