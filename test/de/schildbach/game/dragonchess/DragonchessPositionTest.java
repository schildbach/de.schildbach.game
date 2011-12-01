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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Andreas Schildbach
 */
public class DragonchessPositionTest extends AbstractDragonchessTest
{
	@Test
	public void toFromFEN()
	{
		String[] fens = { "2g3r3g1/s1s1s1s1s1s1/66/66/66/66/S1S1S1S1S1S1/2G3R3G1 " //
				+ "ouhtcmkpthuo/wwwwwwwwwwww/66/66/66/66/WWWWWWWWWWWW/OUHTCMKPTHUO " //
				+ "2b3e3b1/1d1d1d1d1d1d/66/66/66/66/1D1D1D1D1D1D/2B3E3B1 " //
				+ "g 1", //

				"66/2s1s1s1s3/66/9s2/6g5/5h6/66/66 " //
						+ "o3c1k2o2/ww1u1ww1h2w/2ww4w3/2W1w2w2w1/TW2W2W2W1/W2W1mW1W2W/7W4/4C7 " //
						+ "2b7b1/1d1d1d1d1d1d/66/4e7/66/4K4D2/1D1D1D1D2BD/2B3E5 " //
						+ "g 53" //
		};

		for (String fen : fens)
			assertEquals(fen, format(position(fen)));
	}
}
