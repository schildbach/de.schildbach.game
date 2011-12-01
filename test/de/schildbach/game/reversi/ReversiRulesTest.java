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

package de.schildbach.game.reversi;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.schildbach.game.AbstractGameRulesTest;
import de.schildbach.game.Game;
import de.schildbach.game.GameMove;
import de.schildbach.game.GameRules;
import de.schildbach.game.common.SingleCoordinateMove;

/**
 * @author Andreas Schildbach
 */
public class ReversiRulesTest extends AbstractGameRulesTest
{
	private static final ReversiRules REVERSI_RULES = new ReversiRules();

	@Override
	protected GameRules gameRules()
	{
		return REVERSI_RULES;
	}

	@Test
	public void newGame()
	{
		Game game = game(null);
		assertEquals("8/8/8/3wb3/3bw3/8/8/8", rules.formatBoard(game.getActualPosition().getBoard()));
	}

	@Test
	public void getAllowedMoves()
	{
		Game game = game(null);
		assertEquals(0, game.getActualPosition().getActivePlayerIndex());
		assertEquals(moves("d3", "c4", "f5", "e6"), allowedMoves(game.getActualPosition(), null));
		rules.executeMove(game, move("d3"));
		assertEquals(1, game.getActualPosition().getActivePlayerIndex());
		assertEquals(moves("c3", "e3", "c5"), allowedMoves(game.getActualPosition(), null));
		rules.executeMove(game, move("e3"));
		assertEquals(0, game.getActualPosition().getActivePlayerIndex());
	}

	private Set<GameMove> moves(String... coordinates)
	{
		Set<GameMove> moves = new HashSet<GameMove>();
		for (String c : coordinates)
			moves.add(move(c));
		return moves;
	}

	private SingleCoordinateMove move(String coordinate)
	{
		return new SingleCoordinateMove(geometry.locateCoordinate(coordinate));
	}
}
