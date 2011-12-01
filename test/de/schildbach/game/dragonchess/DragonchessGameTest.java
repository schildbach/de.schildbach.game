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
import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;

import de.schildbach.game.Game;

/**
 * @author Andreas Schildbach
 */
public class DragonchessGameTest extends AbstractDragonchessTest
{
	@Test
	public void game1() // http://www.chessvariants.org/3d.dir/dragonchess.html
	{
		String notation = "1. R1d4 P1h6 2. G1e4 R1j5 3. S/1g2-1h3 P1g6 4. H2h3 S/1e7-1f6 5. R1d5 R1i5 "
				+ "6. P2i3 W2e6 7. PxR GxP 8. R1e6 G1a5 9. R1e7 P1f7 10. R1d8 C1e8 "
				+ "11. Rx1c7 G2b6 12. R1c6 C1d7 13. R1d5 C1e6 14. R1c4 G1a5 15. GxS M2d6 "
				+ "16. H2a3 Gx1k2 17. H2c5 M2e5 18. W2j3 H2h6 19. G1j5 U2c6 20. G2j2 W2b6 "
				+ "21. G2i3 WxH 22. G1h4 GxG 23. SxG O2b8 24. G1h8 C1e7 25. R1d5 M2f6 "
				+ "26. T2j2 H2g5 27. E3g3 W2h6 28. E3g4 M2g6 29. H2i4 M2f5 30. S1g5 H2f4 "
				+ "31. W2e3 HxS 32. R1e5 GxS 33. RxM GxR 34. TxT UxT 35. M2c4 W2d6 "
				+ "36. H2h5 S/1i7-1j6 37. E3f5 E3g7 38. H2i6+ WxH 39. TxW+ T2h7 40. M2g4 W2g6 "
				+ "41. S1f3 P1g8 42. S1g4 O2b4 43. TxT+ DxT 44. W2e4 H2f6 45. W2a3 O2b6 "
				+ "46. GxW UxG 47. E3h5 E3g5 48. ExE HxE 49. W2a4 O2j8 50. D3f3 G1g2 "
				+ "51. O2a3 U2h5 52. S1h5 O2j4 53. M2f3 GxM 54. DxG U2f6 55. U2l3 O2j5 "
				+ "56. W2g3 P1g7 57. W2a5 O2a6 58. O2a4 P2g5 59. D2f4 P2f3+ 60. K2f1 H2d7 "
				+ "61. U2c3 H2c6 62. C2e2 P3h3 63. O2a3 UxW 64. D3g2 PxD 65. C2f3 UxU " //
				+ "66. W/2b2x2c3" + /* W/2b2xU would be nicer */" W2c4 67. D2e4 O/2j5x2a5"
				+ /* should be made nicer */" 68. OxO OxO 69. K2g2 P3g3 70. U2j4 P1g4+ "
				+ "71. K2h3 HxD 72. C2e3 P2g2+ 73. K2i4 O2i5+ 74. K3i4 D3j6 75. W2l3 D3j5#";
		Game game = game(null, notation);
		assertEquals(notation, rules.formatGame(game, Locale.ENGLISH));
	}

	@Test
	public void game2() // http://www.chessvariants.org/3d.dir/dragonchess.html
	{
		String notation = "1. R1d4 G1i5 2. G1e4 Gx1g2 3. Rx1g7+ RxR 4. GxR P1h6 5. GxM GxP "
				+ "6. GxS G1g2 7. M3f1 P2h4 8. D/3f2-3e2 PxD 9. W2k3 E3g6 10. M3f2 E3g4 "
				+ "11. C3e1 E3g3 12. C3f1 ExE 13. CxE P3i1+ 14. K1g1 G1a5 15. C3h1 G1d3#";
		Game game = game(null, notation);
		assertEquals(notation, rules.formatGame(game, Locale.ENGLISH));
	}

	@Test
	public void game10154()
	{
		String notation = "1. W2g3 W2e6 2. H2h3 W2h6 3. P2i3 TxP 4. W/2j2x2i3 RxS 5. W2f3 RxO 6. D/3f2-3e2 R1b1 7. G1f3 RxH 8. U2c3 RxS 9. RxS RxU 10. R1b8 R1e4 11. G1i5 GxG 12. RxG R1b7 13. RxR HxR 14. T2l4 Gx1k2 15. G1h3 GxO 16. U2j3 W2j6 17. C1e1 G1k2 18. H2j5 T2k6 19. H2i6+ PxH 20. U2h4 PxU 21. W/2i3x2h4 TxM 22. KxT W2i6 23. W2c3 W2h5 24. W2k3 W2k6 25. W2a3 W2k5 26. TxW WxT 27. T2a4 U2j6 28. G1k5 H2i7 29. GxU HxG 30. S/1i2-1h3 O2j8 31. C2e1 G1i5 32. W2b3 GxS+ 33. K2g1 M2c5+ 34. W2e3 GxW 35. S1f3 G1g4 36. S1i4 S/1k7-1j6 37. S1j5 SxS 38. D3j3 W2c6 39. W2b4 M2e7 40. B3k2 W2d6 41. W2d3 U2d7 42. W2i3 H2c8 43. W2l3 W2e5 44. W2k4 H2i7 45. K2f2 M2f6+ 46. K2e2 H2e6 47. W2e4 H2g4+ 48. K2e3 HxS 49. W2c4 M2f3+ 50. K3e3 E3g6 51. D3f2 E3e6 52. W2c5 E3e5#";
		Game game = game(null, notation);
		assertEquals(notation, rules.formatGame(game, Locale.ENGLISH));
	}

	@Test
	public void game55526()
	{
		String notation = "1. S/1a2-1b3 S/1a7-1b6 2. S/1e2-1d3 S1a5 3. G1a4 S/1c7-1d6 4. R1e3 P2i6 5. H2l3 D3b6 6. W2j3 P2i5 7. W2i3 W2d6 8. U2i2 W2e6 9. T2j2 D3b5 10. B3c2 D3b4 11. D3b3 B3b7 12. D3f3 B3c6 13. D3h3 D3f6 14. W2g3 H2h6 15. W2j4 TxW 16. TxT D3f5 17. WxP H2j4 18. TxH W2g6 19. W2e3 MxW 20. TxM RxS 21. GxS RxS 22. G1a8 Rx2c2 23. W2d3 RxS 24. U2h4 W2d5 25. Tx2k7 SxT 26. RxS R1b1 27. G1c5 RxU 28. C1e1 D3g5 29. RxT";
		Game game = game(null, notation);
		assertEquals(notation, rules.formatGame(game, Locale.ENGLISH));

		// cannot return sylph to non-empty cell
		assertNotNull(rules.executeMoves(game, "SxR", Locale.ENGLISH));
	}

	@Test
	public void executeAndUndoLastMove()
	{
		Game game = game(null);
		rules.executeMoves(game, "R1d4", Locale.ENGLISH);
		rules.undoLastMove(game);
		assertEquals("", rules.formatGame(game, Locale.ENGLISH));
		assertEquals(defaultInitialPosition(), game.getActualPosition());
	}
}
