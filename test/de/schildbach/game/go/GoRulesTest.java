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

package de.schildbach.game.go;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.schildbach.game.AbstractGameRulesTest;
import de.schildbach.game.Board;
import de.schildbach.game.Game;
import de.schildbach.game.GameRules;
import de.schildbach.game.Piece;
import de.schildbach.game.common.SingleCoordinateMove;

/**
 * @author Andreas Schildbach
 */
public class GoRulesTest extends AbstractGameRulesTest
{
	private static final GoRules GO_RULES = new GoRules(null);

	private Piece white;
	private Piece black;

	@Override
	protected GameRules gameRules()
	{
		return GO_RULES;
	}

	@Before
	public void setup()
	{
		white = rules.getPieceSet().getPiece("w");
		black = rules.getPieceSet().getPiece("b");
	}

	@Test
	public void newGame()
	{
		Game game = game(null);
		assertEquals("9/9/9/9/9/9/9/9/9", rules.formatBoard(game.getActualPosition().getBoard()));
	}

	@Test
	public void getAllowedMoves()
	{
		Game game = game(null);
		assertEquals(9 * 9 + 1, allowedMoves(game.getActualPosition(), null).size());
		rules.executeMove(game, new SingleCoordinateMove(geometry.locateCoordinate("4/5")));
		assertEquals(9 * 9, allowedMoves(game.getActualPosition(), null).size());
		rules.executeMove(game, new SingleCoordinateMove(geometry.locateCoordinate("5/5")));
		assertEquals(9 * 9 - 1, allowedMoves(game.getActualPosition(), null).size());
	}

	@Test
	public void capture()
	{
		Game game = game(null);
		Board board = game.getActualPosition().getBoard();
		board.setPiece(geometry.locateCoordinate("5/5"), white);
		board.setPiece(geometry.locateCoordinate("4/5"), black);
		board.setPiece(geometry.locateCoordinate("6/5"), black);
		board.setPiece(geometry.locateCoordinate("5/4"), black);
		assertEquals("9/9/9/9/3bwb3/4b4/9/9/9", rules.formatBoard(game.getActualPosition().getBoard()));
		rules.executeMove(game, new SingleCoordinateMove(geometry.locateCoordinate("5/6")));
		assertEquals("9/9/9/4b4/3b1b3/4b4/9/9/9", rules.formatBoard(game.getActualPosition().getBoard()));
		rules.undoLastMove(game);
		assertEquals("9/9/9/9/3bwb3/4b4/9/9/9", rules.formatBoard(game.getActualPosition().getBoard()));
	}

	@Test
	public void nonCapture()
	{
		Game game = game(null);
		Board board = game.getActualPosition().getBoard();
		board.setPiece(geometry.locateCoordinate("9/9"), black);
		rules.executeMove(game, new SingleCoordinateMove(geometry.locateCoordinate("9/8")));
	}

	@Test
	public void stackOverflowBug()
	{
		game(null, "1. 9/9 4/7 2. 9/8 9/7");
	}

	@Test
	public void captureInCorner()
	{
		Game game = game(null, "1. 1/1 1/2 2. 9/9 2/1");
		assertEquals("8b/9/9/9/9/9/9/w8/1w7", rules.formatBoard(game.getActualPosition().getBoard()));
	}

	@Test
	public void captureAtBorder()
	{
		Game game = game(null, "1. 5/9 5/1 2. 4/9 4/1 3. 5/2 5/8 4. 4/2 4/8 5. 3/1 3/9 6. 6/1");
		assertEquals("2wbb4/3ww4/9/9/9/9/9/3bb4/2b2b3", rules.formatBoard(game.getActualPosition().getBoard()));
	}

	@Test
	public void captureInMiddle()
	{
		Game game = game(null, "1. 4/5 4/6 2. 5/5 3/5 3. 9/9 4/4 4. 5/6 5/7 5. 8/9 6/6 6. 7/9 6/5 7. 6/9 5/4");
		assertEquals("5bbbb/9/4w4/3w1w3/2w2w3/3ww4/9/9/9", rules.formatBoard(game.getActualPosition().getBoard()));
	}

	@Test
	public void suicideInCorner()
	{
		Game game = game(null, "1. 2/9 9/9 2. 1/8 1/9");
		assertEquals("1b6w/b8/9/9/9/9/9/9/9", rules.formatBoard(game.getActualPosition().getBoard()));
		rules.undoLastMove(game);
		assertEquals("1b6w/b8/9/9/9/9/9/9/9", rules.formatBoard(game.getActualPosition().getBoard()));
	}

	@Test
	public void suicideInMiddle()
	{
		Game game = game(null, "1. 4/5 9/9 2. 6/5 8/9 3. 5/6 7/9 4. 5/4 5/5");
		assertEquals("6www/9/9/4b4/3b1b3/4b4/9/9/9", rules.formatBoard(game.getActualPosition().getBoard()));
	}

	@Test
	public void suicideWithTwoEyes()
	{
		Game game = game(null, "1. 4/7 5/7 2. 5/6 6/6 3. 5/5 1/9 4. 5/8 2/9 5. 6/8 3/9 6. 7/7 4/9 7. 7/6 5/9 8. 7/5 6/9 9. 6/4 6/5 10. 9/9 6/7");
		assertEquals("wwwwww2b/4bb3/3b2b2/4b1b2/4b1b2/5b3/9/9/9", rules.formatBoard(game.getActualPosition().getBoard()));
	}
}
