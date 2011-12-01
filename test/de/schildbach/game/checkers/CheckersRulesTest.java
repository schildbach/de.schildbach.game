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

package de.schildbach.game.checkers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

import de.schildbach.game.Board;
import de.schildbach.game.Game;
import de.schildbach.game.GamePosition;
import de.schildbach.game.common.CapturingGamePosition;

/**
 * @author Andreas Schildbach
 */
public class CheckersRulesTest extends AbstractCheckersTest
{
	@Test
	public void standardInitialPosition()
	{
		GamePosition position = defaultInitialPosition();
		assertTrue(position instanceof CapturingGamePosition);
		assertTrue(position.getBoard() instanceof Board);
	}

	@Test
	public void coordinatesShouldExist()
	{
		assertNotNull(coordinate("1"));
		assertNotNull(coordinate("6"));
		assertNotNull(coordinate("18"));
		assertNotNull(coordinate("50"));
	}

	@Test
	public void coordinatesShouldNotExist()
	{
		assertNull(coordinate("0"));
		assertNull(coordinate("51"));
	}

	@Test
	public void getAllowedMoves_initialPosition()
	{
		// initial position, just plain moves
		GamePosition position = defaultInitialPosition();
		Set<CheckersMove> expectedMoves = new HashSet<CheckersMove>(Arrays.asList(new CheckersMove[] { move("31", "26"), move("31", "27"),
				move("32", "27"), move("32", "28"), move("33", "28"), move("33", "29"), move("34", "29"), move("34", "30"), move("35", "30") }));
		assertEquals(expectedMoves, allowedMoves(position, null));
	}

	@Test
	public void getAllowedMoves_whiteForcedToCapture()
	{
		// white is forced to capture (quantity rule)
		CapturingGamePosition position = (CapturingGamePosition) position("mmmmm/mmmmm/mmmmm/mmm1m/2m2/2M2/MM1MM/MMMMM/MMMMM/MMMMM w 2");
		Set<CheckersMove> expectedMoves = new HashSet<CheckersMove>(Arrays.asList(new CheckersMove[] { capture("28", "19", "23") }));
		assertEquals(expectedMoves, allowedMoves(position, null));
	}

	@Test
	public void getAllowedMoves_blackForcedToDoubleCapture()
	{
		// black is forced to double capture (quantity rule)
		CapturingGamePosition position = (CapturingGamePosition) position("mmmmm/mmmmm/mmmmm/mm1mm/2m2/1MM2/2MMM/MMMMM/MMMMM/MMMMM b 2");
		Set<CheckersMove> expectedMoves = new HashSet<CheckersMove>(Arrays.asList(new CheckersMove[] { capture("23", "32", "28", "21", "27") }));
		assertEquals(expectedMoves, allowedMoves(position, null));
	}

	@Test
	public void getAllowedMoves_blackMayChoose1()
	{
		// black may choose between three quantity 1's
		CapturingGamePosition position = (CapturingGamePosition) position("mmmmm/mmmmm/mmmmm/1m1mm/m1m2/1MMM1/2M1M/MMMMM/MMMMM/MMMMM b 3");
		Set<CheckersMove> expectedMoves = new HashSet<CheckersMove>(Arrays.asList(new CheckersMove[] { capture("21", "32", "27"),
				capture("23", "32", "28"), capture("23", "34", "29"), }));
		assertEquals(expectedMoves, allowedMoves(position, null));
	}

	@Test
	public void getAllowedMoves_blackMayChoose2()
	{
		// black may choose between two quantity 2's
		CapturingGamePosition position = (CapturingGamePosition) position("mmmmm/mmmmm/mmmmm/1m1mm/2m2/mMMMM/2M2/MMMMM/MMMMM/MMMMM b 4");
		Set<CheckersMove> expectedMoves = new HashSet<CheckersMove>(Arrays.asList(new CheckersMove[] { capture("23", "32", "28", "21", "27"),
				capture("23", "34", "29", "25", "30") }));
		assertEquals(expectedMoves, allowedMoves(position, null));
	}

	@Test
	public void circleCaptureWithMan()
	{
		String fen = "5/5/5/1mkm1/1M3/2mm1/5/5/5/5 w 1";
		CapturingGamePosition position = (CapturingGamePosition) position(fen);
		Set<CheckersMove> expectedMoves = new HashSet<CheckersMove>();
		expectedMoves.add(move("22", "13", "24", "33", "22", "11"));
		assertEquals(expectedMoves, allowedMoves(position, null));
		assertEquals(fen, format(position));
	}

	@Test
	public void circleCaptureWithKing()
	{
		String fen = "5/5/k4/2km1/1K3/2mm1/5/5/5/5 w 1";
		CapturingGamePosition position = (CapturingGamePosition) position(fen);
		Set<CheckersMove> expectedMoves = new HashSet<CheckersMove>();
		expectedMoves.add(move("22", "13", "24", "33", "22", "6"));
		expectedMoves.add(move("22", "13", "24", "33", "17", "6"));
		assertEquals(expectedMoves, allowedMoves(position, null));
		assertEquals(fen, format(position));
	}

	@Test
	public void canDrawBeClaimed()
	{
		Game game = game(null);
		assertFalse(rules.canDrawBeClaimed(game));
		rules
				.executeMoves(
						game,
						"1. 32-27 17-21 2. 31-26 21-32 3. 38-27 11-17 4. 37-32 17-21 5. 26-17 12-21 6. 42-37 18-22 7. 27-18 13-22 8. 37-31 21-27 9. 32-21 16-27 10. 31-26 19-23 11. 34-29 23-34 12. 39-30 20-24 13. 30-19 14-23 14. 41-37 7-11 15. 35-30 15-20 16. 30-25 8-12 17. 25-14 9-20 18. 43-38 2-8 19. 37-32 11-16 20. 32-21 16-27 21. 46-41 12-18 22. 41-37 20-24 23. 37-32 27-31 24. 26-37 6-11 25. 40-35 8-12 26. 37-31 11-17 27. 32-27 10-14 28. 44-39 14-20 29. 38-32 1-7 30. 45-40 20-25 31. 48-43 7-11 32. 39-34 11-16 33. 31-26 22-31 34. 26-37 17-21 35. 37-31 5-10 36. 31-26 3-8 37. 26-17 12-21 38. 34-29 23-34-45 39. 36-31 21-26 40. 31-27 8-12 41. 33-28 12-17 42. 43-38 10-14 43. 27-21 16-27 44. 32-21-12-23 25-30 45. 23-18 30-34 46. 18-12 34-39 47. 38-32 24-29 48. 12-7 29-34 49. 7-2 39-43 50. 49-38 34-39 51. 32-27 39-44 52. 50-39 45-50 53. 39-33 50-45 54. 35-30 45-18 55. 28-22 18-9 56. 33-29 26-31 57. 27-36 9-27-43-25 58. 29-24 25-39 59. 2-8 39-33 60. 36-31 33-15 61. 31-27 4-9 62. 8-35 15-33 63. 27-21 14-20 64. 47-41 33-28 65. 41-36 28-22 66. 21-16 20-25 67. 35-19 22-6 68. 36-31 6-22 69. 31-26 22-17 70. 19-24 9-14 71. 24-35 14-20 72. 35-13 17-28 73. 13-35 28-33 74. 35-13 33-39 75. 13-35 39-33 76. 35-40 25-30 77. 40-45 33-50 78. 45-1 50-6 79. 26-21 30-35 80. 1-45 6-50 81. 45-1 50-6 82. 1-45 6-50 83. 45-29 20-25 84. 29-45 25-30 85. 45-1 50-6 86. 1-45 6-50 87. 45-1 50-6 88. 1-45 6-50 89. 45-1 50-6 90. 1-45 6-50 91. 45-23 50-33 92. 23-1 33-6 93. 1-45 6-50 94. 45-23 50-33 95. 23-12 33-28 96. 12-1 28-6",
						Locale.ENGLISH);
		assertTrue(rules.canDrawBeClaimed(game));
	}

	@Test
	public void winGame()
	{
		String notation = "1. 32-28 20-25 2. 34-29 16-21 3. 37-32 11-16 4. 31-27 6-11 5. 36-31 15-20 "
				+ "6. 42-37 21-26 7. 41-36 17-21 8. 28-22 19-24 9. 47-42 14-19 10. 46-41 25-30 "
				+ "11. 40-34 9-14 12. 34-25 10-15 13. 45-40 19-23 14. 50-45 23-34 15. 39-30-19-10 5-14 "
				+ "16. 43-39 20-24 17. 40-34 18-23 18. 33-29 24-33 19. 38-29-18-9-20 15-24 20. 34-30 24-29 "
				+ "21. 39-34 29-40 22. 45-34 4-9 23. 30-24 9-14 24. 24-20 3-9 25. 34-29 8-13 "
				+ "26. 42-38 12-18 27. 38-33 7-12 28. 44-39 2-8 29. 29-24 18-23 30. 33-28 23-29 "
				+ "31. 24-33 1-7 32. 48-43 12-18 33. 49-44 13-19 34. 22-13-2 9-13 35. 20-9-18 19-24 "
				+ "36. 2-30 11-17 37. 44-40 17-22 38. 28-17 21-12-23 39. 30-34 7-12 40. 34-18-7 16-21 " + "41. 27-16";
		Game game = game(null, notation);
		assertTrue(rules.isFinished(game));
		assertThat(rules.points(game), equalTo(new float[] { 1, 0 }));
	}
}
