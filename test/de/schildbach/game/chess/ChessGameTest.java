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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

import org.junit.Test;

import de.schildbach.game.Game;
import de.schildbach.game.GameMove;
import de.schildbach.game.PieceCapturing;
import de.schildbach.game.common.ChessLikeRules;
import de.schildbach.game.common.FenFormat;
import de.schildbach.game.common.ChessLikeRules.CheckState;
import de.schildbach.game.exception.ParseException;

/**
 * @author Andreas Schildbach
 */
public class ChessGameTest extends AbstractChessTest
{
	@Test
	public void executeMovesAndUndo_191MovesGame()
	{
		String expectedAcn;

		// game from book_1.00.pgn ("Thessaloniki olm")
		expectedAcn = "1. d4 d5 2. c4 e6 3. Nf3 c5 4. cxd5 exd5 5. Bg5 Be7 6. Bxe7 Qxe7 7. dxc5 Qxc5 8. "
				+ "Nbd2 Nc6 9. Nb3 Qd6 10. g3 Nf6 11. Bg2 O-O 12. O-O Be6 13. Rc1 Rac8 14. Nbd4 "
				+ "Nxd4 15. Nxd4 Qb4 16. Rxc8 Rxc8 17. b3 h6 18. e3 Bg4 19. Qe1 Qa3 20. f3 Bd7 21. "
				+ "Qe2 a5 22. Rb1 h5 23. Bf1 g6 24. Rb2 b6 25. Rc2 Rxc2 26. Qxc2 Qd6 27. Qc3 Qe5 "
				+ "28. Qe1 Ne8 29. Qd2 Ng7 30. f4 Qd6 31. Bg2 Ne6 32. Ne2 Nc7 33. h3 Qc5 34. Kh2 Bf5 "
				+ "35. Nd4 Be4 36. g4 hxg4 37. hxg4 Nb5 38. Nxb5 Qxb5 39. g5 Bxg2 40. Kxg2 Qc5 41. "
				+ "Kf3 Qb5 42. Kf2 Qc5 43. Ke2 Kf8 44. Qb2 Qb5+ 45. Kd2 Qb4+ 46. Kd1 Qe4 47. Qh8+ "
				+ "Ke7 48. Qf6+ Kf8 49. Qxb6 Qb1+ 50. Kd2 Qxa2+ 51. Ke1 Qa1+ 52. Ke2 Qb2+ 53. Kd3 "
				+ "Qb1+ 54. Kd4 Qe4+ 55. Kc3 Qh1 56. Kb2 Qd1 57. Qc5+ Ke8 58. Qc3 Qe2+ 59. Kc1 Qb5 "
				+ "60. Kd2 Kd7 61. Ke1 Ke8 62. Kd2 Kd7 63. Qf6 Ke8 64. Qe5+ Kd7 65. Qc3 Ke8 66. Kc2 "
				+ "Kd7 67. Qd4 Ke8 68. Qc3 Ke7 69. Qf6+ Kf8 70. Qd8+ Kg7 71. Qd6 Kg8 72. Qa3 Qb6 "
				+ "73. Kd3 Kg7 74. Qa1+ Kg8 75. Qc3 Qa6+ 76. Kd4 a4 77. bxa4 Qxa4+ 78. Kxd5 Qd7+ "
				+ "79. Kc4 Qe6+ 80. Kd3 Qd5+ 81. Qd4 Qb3+ 82. Ke2 Qc2+ 83. Kf3 Qb1 84. Ke2 Qc2+ 85. "
				+ "Qd2 Qb1 86. Qd8+ Kg7 87. Qd3 Qb7 88. Qd4+ Kg8 89. e4 Qb5+ 90. Kf2 Qb8 91. Ke3 "
				+ "Qb3+ 92. Qd3 Qb6+ 93. Kf3 Qg1 94. Qe3 Qf1+ 95. Kg3 Kh7 96. Qf2 Qd3+ 97. Qf3 Qd2 "
				+ "98. Kg4 Qd4 99. Qe2 Kg8 100. Kf3 Qc3+ 101. Qe3 Qc2 102. Qd4 Qc1 103. Qe3 Qd1+ "
				+ "104. Kf2 Qc2+ 105. Qe2 Qc1 106. Kg3 Qg1+ 107. Kf3 Qh1+ 108. Ke3 Qc1+ 109. Qd2 "
				+ "Qc5+ 110. Kf3 Qb5 111. Qd1 Qc6 112. Qd8+ Kg7 113. Qd2 Kg8 114. Qd8+ Kg7 115. Ke3 "
				+ "Qc1+ 116. Qd2 Qg1+ 117. Kd3 Qb1+ 118. Ke3 Qg1+ 119. Ke2 Qb1 120. Qc3+ Kg8 121. "
				+ "Qc8+ Kg7 122. Qc4 Kg8 123. e5 Qb2+ 124. Ke3 Qb6+ 125. Ke4 Qb7+ 126. Kd4 Qa7+ "
				+ "127. Kd3 Qa3+ 128. Ke4 Qa8+ 129. Ke3 Qa3+ 130. Kf2 Qb2+ 131. Kf3 Qa3+ 132. Ke2 "
				+ "Qb2+ 133. Kd3 Qa3+ 134. Ke4 Qa8+ 135. Qd5 Qa4+ 136. Qd4 Qa8+ 137. Ke3 Qa3+ 138. "
				+ "Qd3 Qc1+ 139. Qd2 Qa3+ 140. Kf2 Qb3 141. Qe3 Qc4 142. Kg3 Qd5 143. Qf3 Qc4 144. "
				+ "f5 gxf5 145. Qxf5 Qc1 146. Qf4 Qg1+ 147. Kh3 Qh1+ 148. Kg4 Qd1+ 149. Qf3 Qd4+ "
				+ "150. Kf5 Qd7+ 151. Ke4 Qc6+ 152. Kf4 Qc4+ 153. Qe4 Qf1+ 154. Qf3 Qc4+ 155. Kg3 "
				+ "Qd4 156. Qf4 Qg1+ 157. Kf3 Qf1+ 158. Kg4 Qd1+ 159. Qf3 Qd4+ 160. Kf5 Qd7+ 161. "
				+ "Ke4 Qc6+ 162. Kf4 Qc4+ 163. Kg3 Qd4 164. Qe2 Qg1+ 165. Kh4 Qh1+ 166. Kg4 Qg1+ "
				+ "167. Kf5 Qb1+ 168. Kf4 Qc1+ 169. Qe3 Qc4+ 170. Kg3 Qd5 171. g6 Qe6 172. gxf7+ "
				+ "Kxf7 173. Qc5 Qa6 174. Qf2+ Ke7 175. Qf5 Qh6 176. Kg2 Qg7+ 177. Kh3 Qh6+ 178. "
				+ "Kg3 Qg7+ 179. Kh4 Qh6+ 180. Kg4 Qe6 181. Qxe6+ Kxe6 182. Kf4 Ke7 183. Kf5 Kf7 "
				+ "184. e6+ Ke7 185. Ke5 Ke8 186. Kf6 Kf8 187. Kf5 Ke7 188. Ke5 Ke8 189. Kd6 Kd8 " + "190. e7+ Ke8 191. Ke6";
		Game game = game(null, expectedAcn);
		String actualAcn = rules.formatGame(game, Locale.ENGLISH);
		assertEquals(expectedAcn, actualAcn);
		for (int i = 0; i < 190 * 2; i++)
			rules.undoLastMove(game);
		assertEquals("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1", rules.formatPosition(game.getActualPosition()));
		assertEquals(move("d2", "d4"), game.getLastMove());
		assertEquals(0, ((PieceCapturing) game.getActualPosition()).getCapturedPieces(0).length);
		assertEquals(0, ((PieceCapturing) game.getActualPosition()).getCapturedPieces(1).length);
		rules.undoLastMove(game);
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", rules.formatPosition(game.getActualPosition()));
		assertNull(game.getLastMove());
		assertEquals(0, ((PieceCapturing) game.getActualPosition()).getCapturedPieces(0).length);
		assertEquals(0, ((PieceCapturing) game.getActualPosition()).getCapturedPieces(1).length);
	}

	@Test(expected = IllegalStateException.class)
	public void undoEmptyGame()
	{
		rules.undoLastMove(game(null));
	}

	@Test
	public void test()
	{
		// pawn capture
		String expectedAcn = "1. d4 d5 2. c4 xc4";
		Game game = game(null, expectedAcn);
		assertEquals("1. d4 d5 2. c4 dxc4", rules.formatGame(game, Locale.ENGLISH));

		// game from book_1.00.pgn with promotions
		expectedAcn = "1. e4 c5 2. Nf3 d6 3. d4 cxd4 4. Nxd4 Nf6 5. Nc3 a6 6. Be3 e6 7. Qd2 Be7 8. f3 Nc6 "
				+ "9. g4 Nd7 10. O-O-O Qc7 11. Kb1 Nde5 12. Be2 b5 13. Ndxb5 axb5 14. Nxb5 Qa5 15. "
				+ "Qxa5 Rxa5 16. Bb6 Rxb5 17. Bxb5 Bb7 18. Be2 g5 19. Rd2 f6 20. a4 Kd7 21. b3 Ng6 "
				+ "22. Rhd1 Kc8 23. Bc4 Nf4 24. Bb5 Ne5 25. Rf1 h5 26. gxh5 Rxh5 27. a5 Rh3 28. a6 "
				+ "Ba8 29. Rfd1 d5 30. exd5 exd5 31. c4 Nxf3 32. Rc2 d4 33. c5 Nd5 34. Ka1 f5 35. a7 "
				+ "Bf6 36. Rd3 g4 37. Ba6+ Kd7 38. c6+ Ke6 39. c7 Nxb6 40. c8=Q+ Nxc8 41. Rxc8 Rh8 "
				+ "42. b4 Kd7 43. Rc5 Rxh2 44. Bc8+ Ke7 45. Bb7 g3 46. Bxa8 Rh1+ 47. Kb2 g2 48. Bxf3 "
				+ "g1=Q 49. Bxh1 Qxh1 50. Rc8 Qg2+ 51. Ka3 Qf1 52. a8=Q Qxd3+ 53. Ka4 Qd1+ 54. Kb5 "
				+ "Qe2+ 55. Kb6 Kf7 56. Rf8+ Kg7 57. Rg8+ Kh7 58. Qd5 Qh5 59. Rg1 Kh6 60. Rh1 Bh4 " + "61. Qxd4 Bd8+ 62. Qxd8";
		// 1. e4 c5 2. Nf3 d6 3. d4 cxd4 4. Nxd4 Nf6 5. Nc3 a6 6. Be3 e6 7. Qd2
		// Be7 8. f3 Nc6 9. g4 Nd7 10. O-O-O Qc7 11. Kb1 Nde5 12. Be2 b5 13.
		// Ndxb5 axb5 14. Nxb5 Qa5 15. Qxa5 Rxa5 16. Bb6 Rxb5 17. Bxb5 Bb7 18.
		// Be2 g5 19. Rd2 f6 20. a4 Kd7 21. b3 Ng6 22. Rhd1 Kc8 23. Bc4 Nf4 24.
		// Bb5 Ne5 25. Rf1 h5 26. gxh5 Rxh5 27. a5 Rh3 28. a6 Ba8 29. Rfd1 d5
		// 30. exd5 exd5 31. c4 Nxf3 32. Rc2 d4 33. c5 Nd5 34. Ka1 f5 35. a7 Bf6
		// 36. Rd3 g4 37. Ba6+ Kd7 38. c6+ Ke6 39. c7 Nxb6
		game = game(null, expectedAcn);
		String actualAcn = rules.formatGame(game, Locale.ENGLISH);
		assertEquals(expectedAcn, actualAcn);

		// Kasparov - Topalov, 1999 (complete)
		expectedAcn = "1. e4 d6 2. d4 Nf6 3. Nc3 g6 4. Be3 Bg7 5. Qd2 c6 6. f3 b5 7. Nge2 Nbd7 8. Bh6 Bxh6 "
				+ "9. Qxh6 Bb7 10. a3 e5 11. O-O-O Qe7 12. Kb1 a6 13. Nc1 O-O-O 14. Nb3 exd4 15. Rxd4 c5 "
				+ "16. Rd1 Nb6 17. g3 Kb8 18. Na5 Ba8 19. Bh3 d5 20. Qf4+ Ka7 21. Rhe1 d4 22. Nd5 Nbxd5 "
				+ "23. exd5 Qd6 24. Rxd4 cxd4 25. Re7+ Kb6 26. Qxd4+ Kxa5 27. b4+ Ka4 28. Qc3 Qxd5 "
				+ "29. Ra7 Bb7 30. Rxb7 Qc4 31. Qxf6 Kxa3 32. Qxa6+ Kxb4 33. c3+ Kxc3 34. Qa1+ Kd2 "
				+ "35. Qb2+ Kd1 36. Bf1 Rd2 37. Rd7 Rxd7 38. Bxc4 bxc4 39. Qxh8 Rd3 40. Qa8 c3 41. Qa4+ Ke1 " + "42. f4 f5 43. Kc1 Rd2 44. Qa7";
		game = game(null, expectedAcn);
		actualAcn = rules.formatGame(game, Locale.ENGLISH);
		assertEquals(expectedAcn, actualAcn);

		// Deep Blue - Kasparov, 1996, Game 1
		expectedAcn = "1. e4 c5 2. c3 d5 3. exd5 Qxd5 4. d4 Nf6 5. Nf3 Bg4 6. Be2 e6 7. h3 Bh5 8. O-O Nc6 "
				+ "9. Be3 cxd4 10. cxd4 Bb4 11. a3 Ba5 12. Nc3 Qd6 13. Nb5 Qe7 14. Ne5 Bxe2 15. Qxe2 O-O "
				+ "16. Rac1 Rac8 17. Bg5 Bb6 18. Bxf6 gxf6 19. Nc4 Rfd8 20. Nxb6 axb6 21. Rfd1 f5 "
				+ "22. Qe3 Qf6 23. d5 Rxd5 24. Rxd5 exd5 25. b3 Kh8 26. Qxb6 Rg8 27. Qc5 d4 "
				+ "28. Nd6 f4 29. Nxb7 Ne5 30. Qd5 f3 31. g3 Nd3 32. Rc7 Re8 33. Nd6 Re1+ 34. Kh2 Nxf2 " + "35. Nxf7+ Kg7 36. Ng5+ Kh6 37. Rxh7+";
		// "1. e4 c5 2. c3 d5 3. exd5 Qxd5 4. d4 Nf6 5. Nf3 Bg4 6. Be2 e6 7. h3
		// Bh5 8. O-O Nc6 " +
		// "9. Be3 cxd4 10. cxd4 Bb4 11. a3 Ba5 12. Nc3 Qd6 13. Nb5 Qe7 14. Ne5!
		// Bxe2 15. Qxe2 O-O " +
		// "16. Rac1 Rac8 17. Bg5 Bb6 18. Bxf6 gxf6 19. Nc4! Rfd8 20. Nxb6! axb6
		// 21. Rfd1 f5 " +
		// "22. Qe3! Qf6 23. d5! Rxd5 24. Rxd5 exd5 25. b3! Kh8? 26. Qxb6 Rg8
		// 27. Qc5 d4 " +
		// "28. Nd6 f4 29. Nxb7 Ne5 30. Qd5 f3 31. g3 Nd3 32. Rc7 Re8 33. Nd6
		// Re1+ 34. Kh2 Nxf2 " +
		// "35. Nxf7+ Kg7 36. Ng5 Kh6 37. Rxh7+";
		// expecting .... Kg6 38. Qg8+ Kf5 Nxf3 and white's strength is
		// overwhelming.
		// White will have lots of ways to defeat black, but black has no real
		// way to attack white.}
		// 1-0
		game = game(null, expectedAcn);
		actualAcn = rules.formatGame(game, Locale.ENGLISH);
		assertEquals(expectedAcn, actualAcn);
		assertTrue(((ChessLikeRules) rules).checkState(game.getActualPosition(), game.getInitialPosition().getBoard()) == CheckState.CHECK);

		// checkmated game
		// (http://www.logicalchess.com/info/reference/notation/)
		expectedAcn = "1. d4 Nf6 2. c4 g6 3. Nc3 Bg7 4. e4 d6 5. Nf3 O-O 6. Bd3 Bg4 7. h3 Bxf3 8. gxf3 c5 "
				+ "9. d5 Nbd7 10. f4 e6 11. dxe6 fxe6 12. Be3 a6 13. Qf3 d5 14. e5 Nxe5 15. fxe5 Ne4 "
				+ "16. Qg4 Qb6 17. Na4 Qb4+ 18. Ke2 Rxf2+ 19. Bxf2 Qd2+ 20. Kf1 Qxf2#";
		game = game(null, expectedAcn);
		actualAcn = rules.formatGame(game, Locale.ENGLISH);
		assertEquals(expectedAcn, actualAcn);
		assertTrue(((ChessLikeRules) rules).checkState(game.getActualPosition(), game.getInitialPosition().getBoard()) == CheckState.CHECKMATE);
		/*
		 * // ChessPosition position =
		 * FenFormat.parseFenPosition("rn1q1rk1/ppp1ppbp/3p1np1/8/2PPP1b1/2NB1N1P/PP3PP1/R1BQK2R b KQ - 0 7"); game =
		 * new ChessGame(position); rules.executeMove(game, new ChessMove("g4", "f3")); actualAcn =
		 * rules.getNotation(game, Locale.ENGLISH); assertEquals("1. Bxf3", actualAcn);
		 */
		//
		game = game(null, "1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 4. Ba4 Nf6 5. O-O Be7 6. Re1 b5 7. Bb3 d6 8. c3 O-O 9. h3 Nb8 10. d4");
		rules.executeMove(game, move("b8", "d7"));
		actualAcn = rules.formatGame(game, Locale.ENGLISH);
		assertEquals("1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 4. Ba4 Nf6 5. O-O Be7 6. Re1 b5 7. Bb3 d6 8. c3 O-O 9. h3 Nb8 10. d4 Nbd7", actualAcn);
		/*
		 * // position = FenFormat.parseFenPosition("rnbq1rk1/2p1bppp/p2p1n2/1p2p3/3PP3/1BP2N1P/PP3PP1/RNBQR1K1 b - - 0
		 * 10"); game = new ChessGame(position); rules.executeMove(game, new ChessMove("b8", "d7")); actualAcn =
		 * rules.getNotation(game, Locale.ENGLISH); assertEquals("1. Nbd7", actualAcn);
		 */
		game = game(null, "1. e4 e5 2. Nf3 Nc6 3. Bb5 a6");
		actualAcn = rules.formatGame(game, Locale.ENGLISH);
		assertEquals("1. e4 e5 2. Nf3 Nc6 3. Bb5 a6", actualAcn);

		expectedAcn = "1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 4. Ba4 Nf6 5. O-O Be7 6. Re1 b5 7. Bb3 d6 8. c3 "
				+ "O-O 9. h3 Nb8 10. d4 Nbd7 11. c4 c6 12. cxb5 axb5 13. Nc3 Bb7 14. Bg5 b4 15. "
				+ "Nb1 h6 16. Bh4 c5 17. dxe5 Nxe4 18. Bxe7 Qxe7 19. exd6 Qf6 20. Nbd2 Nxd6 21. "
				+ "Nc4 Nxc4 22. Bxc4 Nb6 23. Ne5 Rae8 24. Bxf7+ Rxf7 25. Nxf7 Rxe1+ 26. Qxe1 Kxf7 "
				+ "27. Qe3 Qg5 28. Qxg5 hxg5 29. b3 Ke6 30. a3 Kd6 31. axb4 cxb4 32. Ra5 Nd5 33. "
				+ "f3 Bc8 34. Kf2 Bf5 35. Ra7 g6 36. Ra6+ Kc5 37. Ke1 Nf4 38. g3 Nxh3 39. Kd2 Kb5 " + "40. Rd6 Kc5 41. Ra6 Nf2 42. g4 Bd3 43. Re6"; // 1/2-1/2";
		game = game(null, expectedAcn);
		actualAcn = rules.formatGame(game, Locale.ENGLISH);
		assertEquals(expectedAcn, actualAcn);
		/*
		 * // parsing of non-standard acn: no space after move number // note: does not work any more game =
		 * rules.newGame("1.d4 d5 2.c4 xc4", Locale.ENGLISH); assertEquals("1. d4 d5 2. c4 dxc4",
		 * rules.getNotation(game, Locale.ENGLISH));
		 */
		// parsing of non-standard acn: missing file in pawn capture
		game = game(null, "1. d4 d5 2. c4 xc4");
		assertEquals("1. d4 d5 2. c4 dxc4", rules.formatGame(game, Locale.ENGLISH));
	}

	/*
	 * public void testConstructWithThreeQueens() { // construct with 3 queens GameRules specialRules =
	 * GameRules.getRules("chess", null, "Q1Q5/8/Q7/8/8/3k4/8/3K4"); game = specialRules.newGame(); GameMove move =
	 * specialRules.parseMove("Qa8b7", Locale.ENGLISH, game.getActualPosition()); specialRules.executeMove(game, move);
	 * assertEquals("1. Qa8b7+", specialRules.getNotation(game, Locale.ENGLISH)); }
	 */

	@Test
	public void queeningBug()
	{
		// queening bug
		String acn = "1. e4 e5 2. Nf3 Nc6 3. Nc3 Nf6 4. Bb5 Bc5 5. Bxc6 O-O 6. Bd5 Nxd5 7. exd5 Qe8 8. d3 d6 9. Be3 Bd4 10. Bxd4 exd4+ 11. Ne2 Bg4 12. Nxd4 Qe5 13. c4 Rfe8 14. Kf1 Bxe2+ 15. Nxe2 Qxb2 16. Rb1 Qxa2 17. Rxb7 Rxe2 18. Qxe2 Qa1+ 19. Qe1 Qe5 20. Rxc7 g6 21. Qxe5 dxe5 22. d6 a5 23. d7 Rd8 24. Rc8 Rxc8 25. dxc8=Q+";
		Game game = game(null, acn);
		assertEquals(acn, rules.formatGame(game, Locale.ENGLISH));
	}

	@Test
	public void kasparovTopalov()
	{
		// Kasparov - Topalov, 1999 (partly)
		String acn = "1. e4 d6 2. d4 Nf6 3. Nc3 g6 4. Be3 Bg7 5. Qd2 c6 6. f3 b5 7. Nge2 Nbd7 8. Bh6 Bxh6 "
				+ "9. Qxh6 Bb7 10. a3 e5 11. O-O-O Qe7 12. Kb1 a6 13. Nc1 O-O-O 14. Nb3 exd4 15. Rxd4 c5 "
				+ "16. Rd1 Nb6 17. g3 Kb8 18. Na5 Ba8 19. Bh3 d5 20. Qf4+ Ka7 21. Rhe1 d4 22. Nd5 Nbxd5 " + "23. exd5 Qd6";
		Game game = game(null, acn);
		assertEquals("b2r3r/k4p1p/p2q1np1/NppP4/3p1Q2/P4PPB/1PP4P/1K1RR3 w - - 1 24", rules.formatPosition(game.getActualPosition()));
		assertArrayEquals(FenFormat.parsePieces(rules.getPieceSet(), "bpn"), ((PieceCapturing) game.getActualPosition()).getCapturedPieces(0));
		assertArrayEquals(FenFormat.parsePieces(rules.getPieceSet(), "BPN"), ((PieceCapturing) game.getActualPosition()).getCapturedPieces(1));
		assertEquals(acn, rules.formatGame(game, Locale.ENGLISH));
	}

	@Test
	public void executeMoves_castlingBug()
	{
		String acn = "1. c4 d5 2. cxd5 c6 3. Nc3 Nf6 4. e4 Bg4 5. Be2 h5 6. b4 b5 7. a3 g6 8. h3 Bd7 9. g3 Bh6 10. f3 Na6 "
				+ "11. g4 Qb6 12. a4 Nxb4 13. Rb1 Nbxd5 14. axb5 cxb5 15. Nxd5 Qd6 16. Nc3 a6 17. f4 Qxf4 18. Bf3 Rc8 19. Nd5 Qg3+ 20. Kf1 Ng8 "
				+ "21. Ke2 e6 22. Kd3 f5 23. Nb6 fxg4 24. Nxc8 Bxc8 25. Kd4 Nf6 26. hxg4 hxg4 27. Ne2 Qxf3 28. Rf1 Qxe4+ 29. Kc5 Qd5+ 30. Kb4 Qc4+ "
				+ "31. Ka3 Nd5 32. d3 Qc6 33. Nd4 Qc3+ 34. Rb3 Qxd4 35. Rf6 Nxf6 36. Qg1 Qxg1 37. Bxh6 Qa1+ 38. Kb4 Rxh6 39. d4 Nd5+ 40. Kc5 Rh5 "
				+ "41. Kc6 Qd1 42. Rc3 Nxc3 43. Kc7 Bd7 44. Kb7 a5 45. Kc7 Qf3 46. Kb6 a4 47. Ka7 a3 48. Kb8 a2 49. Kc7 a1=Q 50. Kd6 Qa6+ "
				+ "51. Kc7 Qfa8 52. d5 Nxd5#";
		Game game = game(null, acn);
		assertEquals("q3k3/2Kb4/q3p1p1/1p1n3r/6p1/8/8/8 w - - 0 53", rules.formatPosition(game.getActualPosition()));
		assertEquals(acn, rules.formatGame(game, Locale.ENGLISH));
	}

	@Test
	public void parseMoves_enPassantBug()
	{
		String acn = "1. d4 d5 2. c4 e5 3. dxe5 d4 4. Nf3 Nc6 5. g3 h6 6. Bg2 g5 7. e3 Bb4+ 8. Kf1 Bg4 9. exd4 Qd7 10. h3 Bxf3 "
				+ "11. Bxf3 O-O-O 12. Bg4 Nxd4 13. Qxd4 f5 14. exf6 Qf5";
		Game game = game(null, acn);
		assertEquals(acn, rules.formatGame(game, Locale.ENGLISH));
	}

	@Test
	public void taokingBug()
	{
		String notation = "1. e4 d5 2. d3 dxe4 3. dxe4 Qxd1+ 4. Kxd1 Nc6 5. c3 f5 6. Nd2 fxe4 7. Nxe4 Bg4+ 8. f3 O-O-O+";
		Game game = game(null, notation);
		assertEquals(notation, rules.formatGame(game, Locale.ENGLISH));
	}

	@Test
	public void executeMove()
	{
		// String acn;

		// cannot move your partners pieces
		// cannot test this case any more as this is only assert()ed
		// acn = "1. e4 c5 2. d4 cxd4 3. Qxd4 Nc6 4. Qe3 e5 5. Bc4 Nf6";
		// game = rules.newGame(acn, Locale.ENGLISH);
		// try
		// {
		// rules.executeMove(game, new ChessMove(game.getActualPosition(), geometry, "h7", "h6"));
		// fail("cannot move your partners pieces");
		// }
		// catch (IllegalMoveException x)
		// {
		// }
	}

	@Test
	public void parseMove()
	{
		// bug
		String acn = "1. d4 d5 2. c4";
		Game game = game(null, acn);
		GameMove move = rules.parseMove("dxc4", Locale.ENGLISH, game);
		assertEquals("d5-c4", rules.formatMove(move));

		// bug which always selected pawn instead of knight (KingLouie)
		acn = "1. e4 d5 2. exd5 Qxd5 3. Nc3 Qa5 4. d4 c6 5. Bd2 Bf5 6. Nf3 Nf6 7. Bc4 e6 8. Qe2 Nbd7 9. d5 cxd5 10. Nxd5 Qc5 "
				+ "11. b4 Qc8 12. Nxf6+ gxf6 13. Nd4 Bg6 14. O-O Bg7 15. Bb3 O-O 16. c4 Ne5 17. Rfd1 Bd3 18. Qe3 Bg6 19. c5 Rd8 20. Bc3 a5 "
				+ "21. a3 Ng4 22. Qh3 axb4 23. axb4 Rxa1 24. Rxa1";
		game = game(null, acn);
		move = rules.parseMove("g4-e5", Locale.ENGLISH, game);
		assertEquals("g4-e5", rules.formatMove(move));
	}

	@Test(expected = ParseException.class)
	public void parseMove_castlingBug()
	{
		// castling bug
		String acn = "1. e4 c5 2. Nf3 g6 3. d4 cxd4 4. Nxd4 Bg7 5. c4 Nc6 6. Be3 d6 7. Nc3 Nf6";
		Game game = game(null, acn);
		GameMove move = rules.parseMove("O-O", Locale.ENGLISH, game);
		rules.executeMove(game, move);
	}

	@Test
	public void parseMoves_Simple()
	{
		// easy
		String acn1 = "1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 4. Ba4 Nf6";
		Game game = game(null, acn1);
		String acn2 = rules.formatGame(game, Locale.ENGLISH);
		assertEquals(acn1, acn2);
	}

	@Test
	public void parseMoves_MassPromotion()
	{
		// masses of promotions (thanks, frozenfritz!)
		String acn = "1. Nf3 d5 2. g3 Nf6 3. Bg2 c6 4. O-O Bg4 5. d3 Bxf3 6. exf3 e6 7. Bf4 Bd6 8. Bg5 Nbd7 9. Nc3 h6 10. Bxf6 Nxf6 "
				+ "11. Qe2 O-O 12. Rae1 Qc7 13. f4 b5 14. a3 a5 15. b3 Qb6 16. Qe3 d4 17. Qe4 dxc3 18. Qxc6 Qxc6 19. Bxc6 Rab8 20. d4 b4 "
				+ "21. a4 Nd5 22. Rd1 Rb6 23. Bxd5 exd5 24. Rfe1 Rbb8 25. Kg2 Rfe8 26. Kf3 Kf8 27. h4 Rxe1 28. Rxe1 Re8 29. Rxe8+ Kxe8 30. Kg4 Ke7 "
				+ "31. f5 Kf6 32. f4 Bc7 33. h5 Bd6 34. Kf3 Kxf5 35. Ke3 Kg4 36. Kf2 Kxh5 37. Kf3 f5 38. Kg2 Kg4 39. Kh2 Kf3 40. Kh3 Be7 "
				+ "41. Kh2 h5 42. Kh3 g6 43. Kh2 Ke2 44. Kh3 Kd2 45. Kg2 Kxc2 46. Kh2 Kxb3 47. Kg2 Kxa4 48. Kf1 Kb3 49. Ke2 Kb2 50. Kd3 b3 "
				+ "51. Ke2 c2 52. Kd3 a4 53. Kd2 a3 54. Kd3 c1=B 55. Ke2 Kc3 56. Kf2 b2 57. Ke2 b1=B 58. Kd1 Be3 59. Ke2 Bxd4 60. Kd1 Bf2 "
				+ "61. Ke2 Bxg3 62. Ke3 Be4 63. Ke2 Bxf4 64. Kd1 a2 65. Ke2 a1=B 66. Kf2 h4 67. Ke2 h3 68. Kf2 h2 69. Ke2 h1=B 70. Kf2 Bb2 "
				+ "71. Ke2 Bba3 72. Kf2 Bfd6 73. Ke2 Bac5 74. Ke1 Bd3 75. Kd1 Bh4 76. Kc1 Be2 77. Kb1 Be3 78. Ka2 d4 79. Kb1 d3 80. Ka2 d2 "
				+ "81. Kb1 d1=B 82. Ka2 f4 83. Kb1 f3 84. Ka2 f2 85. Kb1 g5 86. Ka2 f1=B 87. Kb1 g4 88. Ka2 g3 89. Kb1 g2 90. Ka2 g1=B "
				+ "91. Kb1 Bdh2 92. Ka2 Bc2 93. Ka3 Bf5 94. Ka4 B5h3 95. Ka3 Bef3 96. Ka2 B4g3 97. Ka3 Kd2 98. Ka2 Ke2 99. Ka3 Kf2 100. Ka2 Kg2 "
				+ "101. Ka3 Bef2 102. Ka2";
		Game game = game(null, acn);
		assertEquals(acn, rules.formatGame(game, Locale.ENGLISH));
	}

	@Test(expected = ParseException.class)
	public void castlingWithOtherPlayerBug_AndreasTroll()
	{
		String acn = "1. e4 e5 2. Nf3 Nc6 3. Nc3 d6 4. Bb5 Nf6 5. O-O Be6 6. b3 Qd7 7. Bxc6 bxc6 8. h3 Rb8 9. d4 Qe7 10. Bg5 g6 "
				+ "11. dxe5 dxe5 12. Nxe5 Rd8 13. Qe2 Qc5 14. Bxf6 Qxc3 15. Bxh8 Rd2 16. Qe3 Bb4 17. Qxc3 Bxc3 18. Rae1 Rxc2 19. Rc1 Rxa2 20. Rxc3";
		Game game = game(null, acn);
		rules.executeMove(game, rules.parseMove("O-O", Locale.ENGLISH, game));
	}

	@Test
	public void forfeitCastlingByCaptureOfRook()
	{
		String acn = "1. d4 d5 2. c4 e6 3. c5 b6 4. b4 Nc6 5. Qa4 Ne7 6. b5 e5 7. bxc6 Bd7 8. dxe5 Nxc6 9. Qf4 Bxc5 10. Nc3 d4 "
				+ "11. Nd5 Ne7 12. Qe4 Ba4 13. Nxc7+ Qxc7 14. Qxa8+ Qc8 15. Qxa7 Bb4+ 16. Bd2 Bxd2+ 17. Kxd2 Qc2+ 18. Ke1 ";
		Game game = game(null, acn);
		ChessPosition position = (ChessPosition) game.getActualPosition();
		assertFalse(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));
		rules.executeMove(game, rules.parseMove("Qc3#", Locale.ENGLISH, game));
	}

	@Test
	public void forfeitCastlingByCaptureOfRook2()
	{
		String acn = "1. d4 e6 2. e4 c5 3. Nf3 cxd4 4. Nxd4 Nc6 5. c3 d5 6. f3 dxe4 7. fxe4 Qh4+ 8. g3 Qxe4+ 9. Qe2 Qxh1 10. Nd2 Nxd4 "
				+ "11. Qb5+ Nxb5 12. a4 Nd6";
		Game game = game(null, acn);
		ChessPosition position = (ChessPosition) game.getActualPosition();
		assertFalse(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertTrue(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));
	}

	@Test
	public void forfeitCastlingByCaptureOfRook3()
	{
		String acn = "1. e4 e6 2. Nc3 d5 3. d3 d4 4. Nb5 a6 5. Qh5 axb5 6. Qxb5+ Nc6 7. Nf3 Ra5 8. Qc4 e5 9. Ng5 Qe7 10. Bd2 h6";
		Game game = game(null, acn);
		ChessPosition position = (ChessPosition) game.getActualPosition();
		assertTrue(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertTrue(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.executeMoves(game,
				"11. Nf3 Be6 12. Qxe6 Qxe6 13. c3 Nf6 14. b4 dxc3 15. Bxc3 Rxa2 16. Rb1 Rc2 17. Bd2 Qa2 18. Rd1 Bxb4 19. Bxb4 Nxb4 20. Nxe5 Rxf2",
				Locale.ENGLISH);
		position = (ChessPosition) game.getActualPosition();
		assertTrue(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.executeMoves(game, "21. Rc1 c6", Locale.ENGLISH);
		position = (ChessPosition) game.getActualPosition();
		assertTrue(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.executeMoves(game, "22. g4 Qb2", Locale.ENGLISH);
		position = (ChessPosition) game.getActualPosition();
		assertTrue(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.executeMoves(game, "23. Rd1 Rxf1+", Locale.ENGLISH);
		position = (ChessPosition) game.getActualPosition();
		assertTrue(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.executeMoves(game, "24. Rxf1", Locale.ENGLISH);
		position = (ChessPosition) game.getActualPosition();
		assertFalse(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.executeMoves(game, "Qxe5", Locale.ENGLISH);
		position = (ChessPosition) game.getActualPosition();
		assertFalse(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.executeMoves(game, "25. Rb1 Nxd3+ 26. Ke2 Qxe4+ 27. Kd2 b5 28. h3", Locale.ENGLISH);
		position = (ChessPosition) game.getActualPosition();
		assertFalse(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.executeMoves(game, "0-0", Locale.ENGLISH);
		position = (ChessPosition) game.getActualPosition();
		assertFalse(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));
	}

	@Test
	public void forfeitCastling()
	{
		// game 31116

		String acn = "1. e3 e5 2. d4 d6 3. Nc3 h5 4. Bb5+ c6 5. Bd3 Bg4 6. f3 e4 7. Nxe4 d5 8. Nf2 Bb4+";
		Game game = game(null, acn);
		ChessPosition position = (ChessPosition) game.getActualPosition();
		assertTrue(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertTrue(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.executeMoves(game, "9. Kf1", Locale.ENGLISH);
		position = (ChessPosition) game.getActualPosition();
		assertFalse(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.undoLastMove(game);
		position = (ChessPosition) game.getActualPosition();
		assertTrue(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertTrue(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.executeMoves(game, "9. Kf1 Bc8 10. a3 Bd6 11. e4 dxe4 12. Nxe4", Locale.ENGLISH);
		position = (ChessPosition) game.getActualPosition();
		assertFalse(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.executeMoves(game, "Rh6", Locale.ENGLISH);
		position = (ChessPosition) game.getActualPosition();
		assertFalse(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.executeMoves(game, "13. Nxd6+ Rxd6", Locale.ENGLISH);
		position = (ChessPosition) game.getActualPosition();
		assertFalse(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.undoLastMove(game);
		position = (ChessPosition) game.getActualPosition();
		assertFalse(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.executeMoves(game, "Rxd6", Locale.ENGLISH);
		position = (ChessPosition) game.getActualPosition();
		assertFalse(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));

		rules.executeMoves(game, "14. c3 Nf6 15. g4 hxg4", Locale.ENGLISH);
		rules.executeMoves(game, "16. fxg4 Bxg4 17. Nf3 c5 18. Bf4 Bh3+ 19. Kf2 Ng4+ 20. Kg3 f5", Locale.ENGLISH);
		rules.executeMoves(game, "21. Kxh3 Nf2+ 22. Kg3 Nxd1 23. Rhxd1 cxd4", Locale.ENGLISH);
		rules.executeMoves(game, "24. Bxd6 Qxd6+ 25. Kg2 Nc6", Locale.ENGLISH);
		position = (ChessPosition) game.getActualPosition();
		assertFalse(position.getCastlingAvailable(0, CastlingSide.KINGSIDE));
		assertFalse(position.getCastlingAvailable(0, CastlingSide.QUEENSIDE));
		assertFalse(position.getCastlingAvailable(1, CastlingSide.KINGSIDE));
		assertTrue(position.getCastlingAvailable(1, CastlingSide.QUEENSIDE));
	}

	@Test
	public void castlingAndCheckmateMove()
	{
		// http://de.wikipedia.org/wiki/Rochade
		String acn = "1. e4 e5 2. Nf3 Nc6 3. d4 exd4 4. Nxd4 Bc5 5. Be3 d6 6. Nxc6 bxc6 "
				+ "7. Bxc5 dxc5 8. Qxd8+ Kxd8 9. Nc3 Rb8 10. f3 Rxb2 11. O-O-O+";
		Game game = game(null, acn);
		assertEquals(acn, rules.formatGame(game, Locale.ENGLISH));
	}

	@Test
	public void knightFirst()
	{
		String acn = "1. Nf3";
		Game game = game(null, acn);
		assertEquals(acn, rules.formatGame(game, Locale.ENGLISH));
	}

	@Test
	public void equals()
	{
		Game game1 = game(null, "1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 4. Ba4 Nf6");
		Game game2 = game(null, "1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 4. Ba4 Nf6");
		assertEquals(game1, game1);
		assertEquals(game1, game2);
	}

	@Test
	public void serialization() throws IOException, ClassNotFoundException
	{
		String originalAcn = "1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 4. Ba4 Nf6";
		Game original = game(null, originalAcn);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new ObjectOutputStream(bos).writeObject(original);

		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		Game copy = (Game) new ObjectInputStream(bis).readObject();

		assertEquals(original, copy);
		assertEquals(originalAcn, rules.formatGame(copy, Locale.ENGLISH));
	}
}
