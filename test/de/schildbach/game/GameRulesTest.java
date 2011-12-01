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

import java.util.Locale;

import org.junit.Test;

import de.schildbach.game.chess.ChessRules;

/**
 * @author Andreas Schildbach
 */
public class GameRulesTest extends AbstractGameRulesTest
{
	private static final ChessRules CHESS_RULES = new ChessRules(null);

	@Override
	protected GameRules gameRules()
	{
		return CHESS_RULES;
	}

	@Test
	public void countContainsBoard()
	{
		String acn = "1. e4 e5";
		Game game = game(null, acn);
		Board board = (Board) game.getActualPosition().getBoard().clone();
		assertEquals(1, rules.countContainsBoard(game, board));
		rules.executeMoves(game, "2. Qg4 Qg5", Locale.ENGLISH);
		assertEquals(1, rules.countContainsBoard(game, board));
		rules.executeMoves(game, "3. Qd1 Qd8", Locale.ENGLISH);
		assertEquals(2, rules.countContainsBoard(game, board));
		rules.executeMoves(game, "4. Qg4 Qg5", Locale.ENGLISH);
		assertEquals(2, rules.countContainsBoard(game, board));
		rules.executeMoves(game, "5. Qd1 Qd8", Locale.ENGLISH);
		assertEquals(3, rules.countContainsBoard(game, board));
		rules.executeMoves(game, "6. a3 a6", Locale.ENGLISH);
		assertEquals(3, rules.countContainsBoard(game, board));
		rules.executeMoves(game, "7. Qg4 Qg5", Locale.ENGLISH);
		assertEquals(3, rules.countContainsBoard(game, board));
		rules.executeMoves(game, "8. Qd1 Qd8", Locale.ENGLISH);
		assertEquals(3, rules.countContainsBoard(game, board));
	}
}
