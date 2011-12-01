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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Locale;

import org.junit.Test;

import de.schildbach.game.Game;

/**
 * @author Andreas Schildbach
 */
public class CheckersGameTest extends AbstractCheckersTest
{
	@Test
	public void game1()
	{
		String notation = "1. 33-29 19-23 2. 35-30 14-19 3. 40-35 17-22 4. 45-40 10-14 5. 30-25 20-24 "
				+ "6. 29x20 15x24 7. 50-45 5-10 8. 32-28 23x32 9. 37x17 12x21 10. 38-33 7-12 "
				+ "11. 31-26 1-7 12. 26x17 12x21 13. 34-29 10-15 14. 29x20 15x24 15. 40-34 18-23 "
				+ "16. 34-29 23x34 17. 39x30 13-18 18. 44-40 18-23 19. 41-37 8-13 20. 37-32 21-26 "
				+ "21. 40-34 4-10 22. 43-38 16-21 23. 49-43 11-16 24. 34-29 23x34 25. 30x39 7-12 "
				+ "26. 33-28 21-27 27. 32x21 26x17 28. 46-41 13-18 29. 41-37 3-8 30. 38-32 18-22 "
				+ "31. 39-33 16-21 32. 42-38 6-11 33. 33-29 22x31 34. 36x18 24x33 35. 18-12 33-38 " + "36. 12x3 38x49 37. 3x26 49x27 38. 26-12";
		Game game = game(null, notation);
		assertFalse(rules.isFinished(game));
	}

	@Test
	public void game2()
	{
		String notation = "1. 32-28 18-22 2. 37-32 12-18 3. 41-37 7-12 4. 46-41 1-7 5. 34-29 19-23 "
				+ "6. 28x19 14x34 7. 40x29 10-14 8. 35-30 20-25 9. 30-24 14-20 10. 32-28 16-21 "
				+ "11. 31-26 11-16 12. 37-32 21-27 13. 32x21 16x27 14. 44-40 5-10 15. 39-34 10-14 "
				+ "16. 43-39 14-19 17. 41-37 19x30 18. 38-32 27x38 19. 26-21 17x26 20. 28x17 12x21 "
				+ "21. 29-24 30x19 22. 36-31 38x29 23. 34x1 9-14 24. 42-38 2-7 25. 1x9 4x13 "
				+ "26. 38-32 6-11 27. 31-27 11-16 28. 39-33 19-23 29. 33-28 14-19 30. 50-44 8-12 "
				+ "31. 48-43 12-17 32. 44-39 13-18 33. 39-33 25-30 34. 43-39 19-24 35. 28x19 24x13 "
				+ "36. 33-28 17-22 37. 28x17 21x12 38. 32-28 30-35 39. 40-34 12-17 40. 39-33 35-40 "
				+ "41. 33-29 20-24 42. 29x20 15x24 43. 34-30 24x35 44. 45x34 3-9 45. 27-22 18x27 "
				+ "46. 28-23 9-14 47. 49-44 27-31 48. 47-42 31-36 49. 44-39 26-31 50. 37x26 36-41 " + "51. 42-37 41x32 52. 39-33 32-37 53. 34-29"; // 0-1
		Game game = game(null, notation);
		assertFalse(rules.isFinished(game));
	}

	@Test
	public void pretendedHistoryBugGame15210()
	{
		String notation = "1. 33-28 19-23 2. 28-19 13-24 3. 34-30 20-25 4. 30-19 14-23 5. 35-30 25-34 "
				+ "6. 40-29 23-34 7. 39-30 15-20 8. 38-33 10-15 9. 42-38 17-22 10. 47-42 9-13 "
				+ "11. 30-25 20-24 12. 45-40 11-17 13. 25-20 24-30 14. 40-35 15-24 15. 33-28 22-33 "
				+ "16. 38-29-20 30-34 17. 20-15 13-19 18. 44-39 8-13 19. 39-30 2-8 20. 42-38 19-24 "
				+ "21. 30-19 13-24 22. 43-39 18-23 23. 38-33 8-13 24. 32-28 23-32 25. 37-28 12-18 "
				+ "26. 39-34 3-8 27. 34-29 13-19 28. 29-20 4-9 29. 20-14 19-10 30. 15-4-13-22-11-2-13 6-11 "
				+ "31. 13-9 16-21 32. 31-26 21-27 33. 9-4 11-17 34. 4-31 5-10 35. 31-9 1-7 " + "36. 49-43 10-15 37. 35-30 15-20 38. 9-25";
		Game game = game(null, notation);
		assertEquals(notation, rules.formatGame(game, Locale.ENGLISH));
	}

	@Test
	public void executeAndUndoMove()
	{
		Game game = game(null);
		rules.executeMove(game, rules.parseMove("35-30", Locale.ENGLISH, game));
		assertEquals("mmmmm/mmmmm/mmmmm/mmmmm/5/4M/MMMM1/MMMMM/MMMMM/MMMMM b 1", rules.formatPosition(game.getActualPosition()));
		rules.undoLastMove(game);
		assertEquals("mmmmm/mmmmm/mmmmm/mmmmm/5/5/MMMMM/MMMMM/MMMMM/MMMMM w 1", rules.formatPosition(game.getActualPosition()));
	}

	@Test
	public void executeAndUndoPromotionMove()
	{
		String acn = "1. 32-28 20-25 2. 34-29 16-21 3. 37-32 11-16 4. 31-27 6-11 5. 36-31 15-20 6. 42-37 21-26 7. 41-36 17-21 8. 28-22 19-24 9. 47-42 14-19 10. 46-41 25-30 11. 40-34 9-14 12. 34-25 10-15 13. 45-40 19-23 14. 50-45 23-34 15. 39-30-19-10 5-14 16. 43-39 20-24 17. 40-34 18-23 18. 33-29 24-33 19. 38-29-18-9-20 15-24 20. 34-30 24-29 21. 39-34 29-40 22. 45-34 4-9 23. 30-24 9-14 24. 24-20 3-9 25. 34-29 8-13 26. 42-38 12-18 27. 38-33 7-12 28. 44-39 2-8 29. 29-24 18-23 30. 33-28 23-29 31. 24-33 1-7 32. 48-43 12-18 33. 49-44 13-19";
		Game game = game(null, acn);
		rules.executeMove(game, rules.parseMove("22-13-2", Locale.ENGLISH, game));
		assertEquals("1K3/1m1m1/m2m1/m2mM/m3M/mMM2/MMM1M/MM1M1/M1MM1/5 b 34", rules.formatPosition(game.getActualPosition()));
		rules.undoLastMove(game);
		assertEquals("5/1mmm1/m2m1/m1mmM/mM2M/mMM2/MMM1M/MM1M1/M1MM1/5 w 34", rules.formatPosition(game.getActualPosition()));
	}

	@Test
	public void parseMoveBug()
	{
		String acn = "1. 33-29 18-23 2. 29-18 12-23 3. 38-33 13-18 4. 32-28 23-32 5. 37-28 17-22 6. 28-17 11-22 7. 41-37 8-12 8. 37-32 19-23 9. 46-41 14-19 10. 43-38 10-14 11. 34-30 20-24 12. 48-43 12-17 13. 30-25 9-13 14. 39-34 24-30 15. 35-24";
		Game game = game(null, acn);
		rules.executeMove(game, rules.parseMove("19-30-39-48-37-28-39", Locale.ENGLISH, game));
		assertEquals(
				"1. 33-29 18-23 2. 29-18 12-23 3. 38-33 13-18 4. 32-28 23-32 5. 37-28 17-22 6. 28-17 11-22 7. 41-37 8-12 8. 37-32 19-23 9. 46-41 14-19 10. 43-38 10-14 11. 34-30 20-24 12. 48-43 12-17 13. 30-25 9-13 14. 39-34 24-30 15. 35-24 19-30-39-48-37-28-39",
				rules.formatGame(game, Locale.ENGLISH));
	}
}
