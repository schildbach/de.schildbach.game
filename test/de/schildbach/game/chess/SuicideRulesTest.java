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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

import de.schildbach.game.AbstractGameRulesTest;
import de.schildbach.game.Board;
import de.schildbach.game.Game;
import de.schildbach.game.GameMove;
import de.schildbach.game.GamePosition;
import de.schildbach.game.GameRules;
import de.schildbach.game.chess.ChessRules.Variant;
import de.schildbach.game.exception.ParseException;

/**
 * @author Andreas Schildbach
 */
public class SuicideRulesTest extends AbstractGameRulesTest
{
	private static final ChessRules SUICIDE_CHESS_RULES = new ChessRules(Variant.SUICIDE);

	@Override
	protected GameRules gameRules()
	{
		return SUICIDE_CHESS_RULES;
	}

	@Test
	public void standardInitialPosition()
	{
		GamePosition position = defaultInitialPosition();
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w 0 1", rules.formatPosition(position));
		assertTrue(position instanceof ChessLikePosition);
		assertTrue(position.getBoard() instanceof Board);
	}

	@Test
	public void suicideChess()
	{
		String acn = "1. Na3 Nf6 2. Nc4 Nd5 3. Nd6 exd6 4. Nh3 Nb6 5. g4 Ke7 6. b4 Ke6 7. f3 Qe8 8. Kf2 Be7 9. Ke3 Bd8 10. Kd4 Qe7 "
				+ "11. Nf2 Re8 12. Nd3 Qf8 13. Bb2 Re7 14. Qb1 Qe8 15. Nc1 Qf8 16. g5 Na4 17. g6 Nxb2 18. gxh7 Nd1 19. Kd3 f6 20. Kd4 Qf7 "
				+ "21. h8=N Qg8 22. Ng6 Re8 23. Nh4 Be7 24. Ng2 Nf2 25. Rg1 Nh3 26. Rh1 Ng5 27. f4 Nf7 28. Ne3 Nh6 29. Nd1 Qf7 30. Nc3 Rh8 "
				+ "31. Nd3 Bf8 32. Ne1 Qe8 33. Nf3 Qd8 34. Qd1 Ke7 35. Ke3 Ke8 36. Kf2 Rg8 37. Ke1 Rh8 38. Nb1 Ng8 39. Ng1";
		Game game = game(null, acn);
		assertEquals(acn, rules.formatGame(game, Locale.ENGLISH));
		assertFalse(rules.isFinished(game));
	}

	@Test
	public void suicideChessWin()
	{
		String acn = "1. e3 d6 2. Qg4 Bxg4 3. Kd1 Bxd1 4. a3 Bxc2 5. Ra2 Bxb1 6. a4 Bxa2 7. b3 Bxb3 8. g3 Bxa4 9. Bb5 Bxb5 10. Ne2 Bxe2 "
				+ "11. Rf1 Bxf1 12. h3 Bxh3 13. g4 Bxg4 14. f3 Bxf3 15. e4 Bxe4 16. d3 Bxd3 17. Bh6 gxh6";
		Game game = game(null, acn);
		assertEquals(acn, rules.formatGame(game, Locale.ENGLISH));
		assertTrue(rules.isFinished(game));
		assertThat(rules.points(game), equalTo(new float[] { 1, 0 }));
	}

	@Test(expected = ParseException.class)
	public void castling()
	{
		String acn = "1. e4 a5 2. Bd3 b6 3. Nf3 c6";
		Game game = game(null, acn);
		rules.parseMove("O-O", Locale.ENGLISH, game);
	}

	@Test
	public void enPassant()
	{
		String acn = "1. e3 c5 2. Ba6 Nxa6 3. Qh5 Qb6 4. Qxc5 Nxc5 5. e4 Nxe4 6. b4 Nxf2 7. Kxf2 Qxb4 8. Bb2 Qxd2 9. Bxg7 Qxf2 10. Bxf8 Qxg2 "
				+ "11. Bxe7 Qxg1 12. Rxg1 Kxe7 13. Rxg8 Rxg8 14. Nd2 Rg3 15. hxg3 Ke6 16. Nb3 a6 17. Nc5 b5 18. Nxa6 Bxa6 19. c4 bxc4 20. a3 c3 "
				+ "21. Rf1 Bxf1 22. g4 Rxa3 23. g5 h5";
		Game game = game(null, acn);
		Set<GameMove> expectedMoves = new HashSet<GameMove>(Arrays.asList(rules.parseMove("g6", Locale.ENGLISH, game)));
		Set<GameMove> actualMoves = new HashSet<GameMove>(rules.allowedMoves(game));
		assertEquals(expectedMoves, actualMoves);
	}

	@Test
	public void game25475()
	{
		String acn = "1. b4 c5 2. bxc5 a5 3. c6 Nxc6 4. d4 Nxd4 5. Qxd4 g5 6. Bxg5 Qc7 7. Bxe7 Qxc2 8. Bxf8 Kxf8 9. Qxh8 Qxb1 10. Qxh7 Qxa2 11. Qxg8 Kxg8 12. Rxa2 Rb8 13. Rxa5 Ra8 14. Rxa8 Kf8 15. Rxc8 d6 16. Rxf8 d5 17. Rxf7 d4 18. Rxb7 d3 19. exd3";
		Game game = game(null, acn);

		String lastMove = "e2-d3";
		assertEquals(lastMove, rules.formatMove(game.getLastMove()));

		String expectedPosition = "8/1R6/8/8/8/3P4/5PPP/4KBNR b 0 19";
		assertEquals(expectedPosition, rules.formatPosition(game.getActualPosition()));
	}

	@Test
	public void game43912()
	{
		String acn = "1. e3 e6 2. Ba6 Nxa6 3. b4 Nxb4 4. Na3 Nxa2 5. Rxa2 Bxa3 6. Rxa3 Rb8 7. Rxa7 b6 8. Rxc7 Qxc7 9. Qf3 Qxh2 10. Qxf7 Kxf7 "
				+ "11. Rxh2 Nh6 12. Rxh6 gxh6 13. Ne2 Kg6 14. Nc3 d5 15. Nxd5 exd5 16. e4 dxe4 17. d3 exd3";
		Game game = game(null, acn);
		assertEquals(acn, rules.formatGame(game, Locale.ENGLISH));

		// must capture!
		assertEquals(2, rules.allowedMoves(game).size());
	}

	@Test
	public void game44297()
	{
		String acn = "1. e3 b5";
		Game game = game(null, acn);
		assertEquals(acn, rules.formatGame(game, Locale.ENGLISH));

		// must capture (Bxb5)!
		assertNotNull(rules.executeMoves(game, "a4", Locale.ENGLISH));
	}
}
