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
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

import de.schildbach.game.AbstractGameRulesTest;
import de.schildbach.game.Board;
import de.schildbach.game.Game;
import de.schildbach.game.GamePosition;
import de.schildbach.game.GameRules;
import de.schildbach.game.chess.ChessRules.Variant;

/**
 * @author Andreas Schildbach
 */
public class AntikingRulesTest extends AbstractGameRulesTest
{
	private static final ChessRules RULES = new ChessRules(Variant.ANTIKING);

	@Override
	protected GameRules gameRules()
	{
		return RULES;
	}

	@Test
	public void standardInitialPosition()
	{
		GamePosition position = defaultInitialPosition();
		assertEquals("2prbkqA/2p1nnbr/2pppppp/8/8/PPPPPP2/RBNN1P2/aQKBRP2 w KAka 0 1", rules.formatPosition(position));
		assertTrue(position instanceof AntiKingChessPosition);
		assertTrue(position.getBoard() instanceof Board);
	}

	@Test
	public void gameChessVariants1()
	{
		// peteraronson-AntoineFourriere-2003-327-293
		String acn = "1. Nd4 Ng5 2. dc4 ed5 3. Ne2 Qe6 4. Qd3 fe5 5. cb4 d4 6. Nf4 dxd3 7. Nxe6+ Nxe6 8. Ne4 Bc3 9. Ag7 Nd4 10. Af7 Bd7 "
				+ "11. Ae6 b5 12. Bxc3 a4 13. Bxd4 Bb5+ 14. Ad7 Bxc4 15. Rc2 Ab2 16. Rxc4 Ac2 17. Nxd6 b7 18. f4 Ra8+ 19. Ac6 b6 20. Re6 Nf5+ "
				+ "21. Ab5 a5+ 22. Ab6 c6+ 23. Aa7 Rd8 24. Ab7 Rh8+ 25. Ac8 Rxd6+ 26. Ad7 Rxe6+ 27. Ad6 Re1 28. Ac5 b5+ 29. Axb4 axa3 30. a4 axa4 "
				+ "31. Rc8+ Kf7 32. Rxh8 bc4+ 33. Ac3 cb3+ 34. Ad2 e2+ 35. Axd1+ Ac3 36. Bb6+ Ad4 37. Bd8#";
		Game game = game(null, acn);
		assertEquals(acn, rules.formatGame(game, Locale.ENGLISH));
		assertTrue(rules.isFinished(game));
	}

	@Test
	public void shouldCastle()
	{
		String acn = "1. Ke2 Kd7";
		Game game = game(null, acn);
		assertEquals(acn, rules.formatGame(game, Locale.ENGLISH));
		assertEquals("2prb1qA/2pknnbr/2pppppp/8/8/PPPPPP2/RBNNKP2/aQ1BRP2 w Aa 2 2", rules.formatPosition(game.getActualPosition()));
	}
}
