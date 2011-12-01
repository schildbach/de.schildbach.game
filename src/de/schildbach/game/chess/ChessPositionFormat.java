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

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import de.schildbach.game.PieceSet;
import de.schildbach.game.chess.piece.AntiKing;
import de.schildbach.game.chess.piece.King;
import de.schildbach.game.common.FenFormat;
import de.schildbach.game.common.OrthogonalBoardGeometry;
import de.schildbach.game.exception.ParseException;

/**
 * @author Andreas Schildbach
 */
public final class ChessPositionFormat
{
	public static String format(OrthogonalBoardGeometry geometry, PieceSet pieceSet, ChessLikePosition position)
	{
		StringBuilder fen = new StringBuilder();

		// piece placement
		fen.append(FenFormat.format(geometry, pieceSet, position.getBoard()));

		// active color
		fen.append(" ");
		fen.append(ChessColor.colorNotation(position.getActivePlayerIndex()));

		if (position instanceof ChessPosition)
		{
			ChessPosition chessPosition = (ChessPosition) position;

			// castling availability
			fen.append(" ");
			fen.append(chessPosition.getCastlingAvailable(0, CastlingSide.KINGSIDE) ? "K" : "");
			fen.append(chessPosition.getCastlingAvailable(0, CastlingSide.QUEENSIDE) ? "Q" : "");
			fen.append(chessPosition.getCastlingAvailable(1, CastlingSide.KINGSIDE) ? "k" : "");
			fen.append(chessPosition.getCastlingAvailable(1, CastlingSide.QUEENSIDE) ? "q" : "");
			fen
					.append(!(chessPosition.getCastlingAvailable(0, CastlingSide.KINGSIDE)
							|| chessPosition.getCastlingAvailable(0, CastlingSide.QUEENSIDE)
							|| chessPosition.getCastlingAvailable(1, CastlingSide.KINGSIDE) || chessPosition.getCastlingAvailable(1,
							CastlingSide.QUEENSIDE)) ? "-" : "");

			// en passant target square
			fen.append(" ");
			fen.append(chessPosition.getEnPassantTargetSquare() == null ? "-" : chessPosition.getEnPassantTargetSquare().getNotation());
		}
		else if (position instanceof AntiKingChessPosition)
		{
			AntiKingChessPosition chessPosition = (AntiKingChessPosition) position;

			// castling availability
			fen.append(" ");
			fen.append(chessPosition.getCastlingAvailable(0, King.class) ? "K" : "");
			fen.append(chessPosition.getCastlingAvailable(0, AntiKing.class) ? "A" : "");
			fen.append(chessPosition.getCastlingAvailable(1, King.class) ? "k" : "");
			fen.append(chessPosition.getCastlingAvailable(1, AntiKing.class) ? "a" : "");
			fen.append(!(chessPosition.getCastlingAvailable(0, King.class) || chessPosition.getCastlingAvailable(0, AntiKing.class)
					|| chessPosition.getCastlingAvailable(1, King.class) || chessPosition.getCastlingAvailable(1, AntiKing.class)) ? "-" : "");
		}

		// halfmove clock
		fen.append(" ");
		fen.append(position.getHalfmoveClock());

		// fullmove number
		fen.append(" ");
		fen.append(position.getFullmoveNumber());

		return fen.toString().trim();
	}

	public static void parse(ChessLikePosition position, OrthogonalBoardGeometry geometry, PieceSet pieceSet, String fen)
	{
		try
		{
			StringTokenizer tokenizer = new StringTokenizer(fen);

			// piece placement
			String field = tokenizer.nextToken();
			FenFormat.parse(geometry, pieceSet, field, position.getBoard());

			// active color
			field = tokenizer.nextToken();
			position.setActivePlayerIndex(ChessColor.notationToColor(field.charAt(0)));

			if (position instanceof ChessPosition)
			{
				ChessPosition chessPosition = (ChessPosition) position;

				// castling availability
				field = tokenizer.nextToken();
				chessPosition.setCastlingAvailable(0, CastlingSide.KINGSIDE, field.indexOf('K') != -1);
				chessPosition.setCastlingAvailable(0, CastlingSide.QUEENSIDE, field.indexOf('Q') != -1);
				chessPosition.setCastlingAvailable(1, CastlingSide.KINGSIDE, field.indexOf('k') != -1);
				chessPosition.setCastlingAvailable(1, CastlingSide.QUEENSIDE, field.indexOf('q') != -1);

				// en passant target square
				field = tokenizer.nextToken();
				chessPosition.setEnPassantTargetSquare(field.equals("-") ? null : geometry.locateCoordinate(field));
			}
			else if (position instanceof AntiKingChessPosition)
			{
				AntiKingChessPosition chessPosition = (AntiKingChessPosition) position;

				// castling availability
				field = tokenizer.nextToken();
				chessPosition.setCastlingAvailable(0, King.class, field.indexOf('K') != -1);
				chessPosition.setCastlingAvailable(0, AntiKing.class, field.indexOf('A') != -1);
				chessPosition.setCastlingAvailable(1, King.class, field.indexOf('k') != -1);
				chessPosition.setCastlingAvailable(1, AntiKing.class, field.indexOf('a') != -1);
			}

			// halfmove clock
			field = tokenizer.nextToken();
			position.setHalfmoveClock(Integer.parseInt(field));

			// fullmove number
			field = tokenizer.nextToken();
			position.setFullmoveNumber(Integer.parseInt(field));

			if (tokenizer.hasMoreTokens())
				throw new ParseException(fen, "too many fields");
		}
		catch (NoSuchElementException x)
		{
			throw new ParseException(fen, "not enough fields");
		}
	}

	private ChessPositionFormat()
	{
	}
}
