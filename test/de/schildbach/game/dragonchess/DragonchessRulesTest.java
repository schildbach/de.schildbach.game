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

package de.schildbach.game.dragonchess;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import de.schildbach.game.GameMove;
import de.schildbach.game.GamePosition;
import de.schildbach.game.common.CapturingGamePosition;

/**
 * @author Andreas Schildbach
 */
public class DragonchessRulesTest extends AbstractDragonchessTest
{
	@Test
	public void getNumberOfPlayers()
	{
		assertEquals(2, rules.getNumberOfPlayers());
	}

	@Test
	public void coordinatesShouldExist()
	{
		assertNotNull(coordinate("1a1"));
		assertNotNull(coordinate("1l1"));
		assertNotNull(coordinate("1a8"));
		assertNotNull(coordinate("1l8"));
		assertNotNull(coordinate("3a1"));
		assertNotNull(coordinate("3l1"));
		assertNotNull(coordinate("3a8"));
		assertNotNull(coordinate("3l8"));
	}

	@Test
	public void coordinatesShouldNotExist()
	{
		assertNull(coordinate("4a1"));
		assertNull(coordinate("1a9"));
		assertNull(coordinate("1m1"));
	}

	@Test
	public void getAllowedMovesEmptyBoard()
	{
		// empty board
		GamePosition position = position("66/66/66/66/66/66/66/66 66/66/66/66/66/66/66/66 66/66/66/66/66/66/66/66 g 1");
		assertTrue(allowedMoves(position, null).isEmpty());
	}

	@Test
	public void getAllowedMovesInitialPosition()
	{
		// initial position
		GamePosition position = defaultInitialPosition();
		Collection<? extends GameMove> m = allowedMoves(position, null);
		// sylphs
		assertTrue(m.remove(move("1a2-1b3", position, null)));
		assertTrue(m.remove(move("1c2-1b3", position, null)));
		assertTrue(m.remove(move("1c2-1d3", position, null)));
		assertTrue(m.remove(move("1e2-1d3", position, null)));
		assertTrue(m.remove(move("1e2-1f3", position, null)));
		assertTrue(m.remove(move("1g2-1f3", position, null)));
		assertTrue(m.remove(move("1g2-1h3", position, null)));
		assertTrue(m.remove(move("1i2-1h3", position, null)));
		assertTrue(m.remove(move("1i2-1j3", position, null)));
		assertTrue(m.remove(move("1k2-1j3", position, null)));
		assertTrue(m.remove(move("1k2-1l3", position, null)));
		// griffins
		assertTrue(m.remove(move("1c1-1a4", position, null)));
		assertTrue(m.remove(move("1c1-1e4", position, null)));
		assertTrue(m.remove(move("1c1-1f3", position, null)));
		assertTrue(m.remove(move("1k1-1i4", position, null)));
		assertTrue(m.remove(move("1k1-1h3", position, null)));
		// dragon
		assertTrue(m.remove(move("1g1-1a7", position, null)));
		assertTrue(m.remove(move("1g1-1b6", position, null)));
		assertTrue(m.remove(move("1g1-1c5", position, null)));
		assertTrue(m.remove(move("1g1-1d4", position, null)));
		assertTrue(m.remove(move("1g1-1e3", position, null)));
		assertTrue(m.remove(move("1g1-1f2", position, null)));
		assertTrue(m.remove(move("1g1-1f1", position, null)));
		assertTrue(m.remove(move("1g1-1h1", position, null)));
		assertTrue(m.remove(move("1g1-1h2", position, null)));
		assertTrue(m.remove(move("1g1-1i3", position, null)));
		assertTrue(m.remove(move("1g1-1j4", position, null)));
		assertTrue(m.remove(move("1g1-1k5", position, null)));
		assertTrue(m.remove(move("1g1-1l6", position, null)));
		// unicorns
		assertTrue(m.remove(move("2b1-2a3", position, null)));
		assertTrue(m.remove(move("2b1-2c3", position, null)));
		assertTrue(m.remove(move("2k1-2j3", position, null)));
		assertTrue(m.remove(move("2k1-2l3", position, null)));
		// heroes
		assertTrue(m.remove(move("2c1-2a3", position, null)));
		assertTrue(m.remove(move("2c1-2e3", position, null)));
		assertTrue(m.remove(move("2c1-1b2", position, null)));
		assertTrue(m.remove(move("2c1-1d2", position, null)));
		assertTrue(m.remove(move("2j1-2h3", position, null)));
		assertTrue(m.remove(move("2j1-2l3", position, null)));
		assertTrue(m.remove(move("2j1-3i2", position, null)));
		assertTrue(m.remove(move("2j1-3k2", position, null)));
		// cleric
		assertTrue(m.remove(move("2e1-1e1", position, null)));
		assertTrue(m.remove(move("2e1-3e1", position, null)));
		// mage
		assertTrue(m.remove(move("2f1-1f1", position, null)));
		assertTrue(m.remove(move("2f1-3f1", position, null)));
		// paladin
		assertTrue(m.remove(move("2h1-2g3", position, null)));
		assertTrue(m.remove(move("2h1-2i3", position, null)));
		assertTrue(m.remove(move("2h1-1f1", position, null)));
		assertTrue(m.remove(move("2h1-1j1", position, null)));
		assertTrue(m.remove(move("2h1-1h3", position, null)));
		assertTrue(m.remove(move("2h1-3f1", position, null)));
		assertTrue(m.remove(move("2h1-3j1", position, null)));
		assertTrue(m.remove(move("2h1-3h3", position, null)));
		// warriors
		assertTrue(m.remove(move("2a2-2a3", position, null)));
		assertTrue(m.remove(move("2b2-2b3", position, null)));
		assertTrue(m.remove(move("2c2-2c3", position, null)));
		assertTrue(m.remove(move("2d2-2d3", position, null)));
		assertTrue(m.remove(move("2e2-2e3", position, null)));
		assertTrue(m.remove(move("2f2-2f3", position, null)));
		assertTrue(m.remove(move("2g2-2g3", position, null)));
		assertTrue(m.remove(move("2h2-2h3", position, null)));
		assertTrue(m.remove(move("2i2-2i3", position, null)));
		assertTrue(m.remove(move("2j2-2j3", position, null)));
		assertTrue(m.remove(move("2k2-2k3", position, null)));
		assertTrue(m.remove(move("2l2-2l3", position, null)));
		// basilisks
		assertTrue(m.remove(move("3c1-3c2", position, null)));
		assertTrue(m.remove(move("3k1-3k2", position, null)));
		// elemental
		assertTrue(m.remove(move("3g1-3f1", position, null)));
		assertTrue(m.remove(move("3g1-3e1", position, null)));
		assertTrue(m.remove(move("3g1-3h1", position, null)));
		assertTrue(m.remove(move("3g1-3i1", position, null)));
		assertTrue(m.remove(move("3g1-3g2", position, null)));
		assertTrue(m.remove(move("3g1-3g3", position, null)));
		// dwarfs
		assertTrue(m.remove(move("3b2-3a2", position, null)));
		assertTrue(m.remove(move("3b2-3b3", position, null)));
		assertTrue(m.remove(move("3b2-3c2", position, null)));
		assertTrue(m.remove(move("3d2-3c2", position, null)));
		assertTrue(m.remove(move("3d2-3d3", position, null)));
		assertTrue(m.remove(move("3d2-3e2", position, null)));
		assertTrue(m.remove(move("3f2-3e2", position, null)));
		assertTrue(m.remove(move("3f2-3f3", position, null)));
		assertTrue(m.remove(move("3f2-3g2", position, null)));
		assertTrue(m.remove(move("3h2-3g2", position, null)));
		assertTrue(m.remove(move("3h2-3h3", position, null)));
		assertTrue(m.remove(move("3h2-3i2", position, null)));
		assertTrue(m.remove(move("3j2-3i2", position, null)));
		assertTrue(m.remove(move("3j2-3j3", position, null)));
		assertTrue(m.remove(move("3j2-3k2", position, null)));
		assertTrue(m.remove(move("3l2-3k2", position, null)));
		assertTrue(m.remove(move("3l2-3l3", position, null)));

		assertTrue(m.isEmpty());
	}

	@Test
	public void allowedMovesWhenFrozen()
	{
		CapturingGamePosition position = (CapturingGamePosition) position("kK46/66/66/66/66/66/66/66 66/66/66/66/66/66/66/65W 66/66/66/66/66/66/66/65b g 1");
		Collection<? extends GameMove> m = allowedMoves(position, null);
		// king
		assertTrue(m.remove(move("1b8-2b8", position, null)));

		assertTrue(m.isEmpty());
	}

	@Test
	public void allowedMovesSylphReturnHome()
	{
		// sylph
		CapturingGamePosition position = (CapturingGamePosition) position("kK46/66/66/66/66/66/66/66 66/66/66/66/S56/66/66/66 66/66/66/66/66/66/66/66 g 1");
		Collection<? extends GameMove> m = allowedMoves(position, null);
		// king
		assertTrue(m.remove(move("1b8-2b8", position, null)));
		// sylph
		assertTrue(m.remove(move("2a4-1a2", position, null)));
		assertTrue(m.remove(move("2a4-1c2", position, null)));
		assertTrue(m.remove(move("2a4-1e2", position, null)));
		assertTrue(m.remove(move("2a4-1g2", position, null)));
		assertTrue(m.remove(move("2a4-1i2", position, null)));
		assertTrue(m.remove(move("2a4-1k2", position, null)));
		assertTrue(m.remove(move("2a4-1a4", position, null)));

		assertTrue(m.isEmpty());
	}

	@Test
	public void captureFromAfar()
	{
		CapturingGamePosition position = (CapturingGamePosition) position("kK55/66/66/66/56R/66/66/66 66/66/66/66/65w/66/66/66 66/66/66/66/66/66/66/66 g 1");
		doMove(move("1l4-2l4", position, null), position, null);
		assertEquals("kK55/66/66/66/56R/66/66/66 66/66/66/66/66/66/66/66 66/66/66/66/66/66/66/66 s 1", format(position));
	}

	@Test
	public void warriorPromotion()
	{
		CapturingGamePosition position = (CapturingGamePosition) position("kK55/66/66/66/66/66/66/66 66/W56/66/66/66/66/66/66 66/66/66/66/66/66/66/66 g 1");
		doMove(move("2a7-2a8", position, null), position, null);
		assertEquals("kK55/66/66/66/66/66/66/66 H56/66/66/66/66/66/66/66 66/66/66/66/66/66/66/66 s 1", format(position));
	}

	@Test
	public void isFinished()
	{
		GamePosition position = defaultInitialPosition();
		assertFalse(rules.isFinished(position, defaultInitialBoard()));

		// checkmate position (1. RxS RxS 2. RxO RxO 3. U2c3 R1d5 4. W2f3 RxS#)
		position = (CapturingGamePosition) position("2g7g1/R1s1s1s1s1s1/66/66/66/66/2S1S1r1S1S1/2G7G1 "
				+ "1uhtcmkpthuo/wwwwwwwwwwww/66/66/66/2U2W6/WWWWW1WWWWWW/2HTCMKPTHUO 2b3e3b1/1d1d1d1d1d1d/66/66/66/66/1D1D1D1D1D1D/2B3E3B1 g 5");
		assertTrue(rules.isFinished(position, defaultInitialBoard()));
	}

	@Test
	public void getPoints()
	{
		GamePosition position = defaultInitialPosition();
		assertThat(rules.points(position, defaultInitialBoard()), equalTo(new float[] { 0f, 0f }));

		// checkmate position (1. RxS RxS 2. RxO RxO 3. U2c3 R1d5 4. W2f3 RxS#)
		position = position("2g7g1/R1s1s1s1s1s1/66/66/66/66/2S1S1r1S1S1/2G7G1 "
				+ "1uhtcmkpthuo/wwwwwwwwwwww/66/66/66/2U2W6/WWWWW1WWWWWW/2HTCMKPTHUO 2b3e3b1/1d1d1d1d1d1d/66/66/66/66/1D1D1D1D1D1D/2B3E3B1 g 5");
		assertThat(rules.points(position, defaultInitialBoard()), equalTo(new float[] { 0f, 1f }));
	}
}
