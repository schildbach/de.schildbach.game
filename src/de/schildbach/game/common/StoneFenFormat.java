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

package de.schildbach.game.common;

import java.util.Iterator;
import java.util.StringTokenizer;

import de.schildbach.game.Board;
import de.schildbach.game.Coordinate;
import de.schildbach.game.Piece;
import de.schildbach.game.PieceSet;
import de.schildbach.game.common.piece.StonePieceSet;

/**
 * @author Andreas Schildbach
 */
public final class StoneFenFormat
{
	public static String format(OrthogonalBoardGeometry geometry, StonePieceSet pieceSet, Board board)
	{
		StringBuilder fen = new StringBuilder();

		Coordinate lastCoord = null;
		int zeros = 0;
		for (Iterator<Coordinate> i = geometry.coordinateIterator(); i.hasNext();)
		{
			Coordinate c = i.next();
			if (lastCoord != null && !geometry.isOnSameOrthogonal(OrthogonalBoardGeometry.AXIS_HEIGHT, c, lastCoord))
			{
				appendZeros(fen, zeros);
				fen.append("/");
				Piece piece = board.getPiece(c);
				if (piece != null)
				{
					appendPiece(fen, piece, pieceSet);
					zeros = 0;
				}
				else
				{
					zeros = 1;
				}
			}
			else
			{
				Piece piece = board.getPiece(c);
				if (piece != null)
				{
					appendZeros(fen, zeros);
					zeros = 0;
					appendPiece(fen, piece, pieceSet);
				}
				else
				{
					zeros++;
				}
			}
			lastCoord = c;
		}
		appendZeros(fen, zeros);

		return fen.toString();
	}

	private static void appendPiece(StringBuilder fen, Piece piece, StonePieceSet pieceSet)
	{
		char c = pieceSet.getColorChars()[piece.getColor()];
		fen.append(c);
	}

	private static void appendZeros(StringBuilder fen, int zeroes)
	{
		if (zeroes > 0)
		{
			if (zeroes >= 10)
			{
				int prefix = zeroes / 2;
				fen.append(prefix);
				zeroes -= prefix;
			}
			fen.append(zeroes);
		}
	}

	public static void parse(OrthogonalBoardGeometry geometry, PieceSet pieceSet, String fen, Board board)
	{
		Iterator<Coordinate> iCoordinate = geometry.coordinateIterator();
		board.clear();

		StringTokenizer rankTokenizer = new StringTokenizer(fen, "/");
		while (rankTokenizer.hasMoreTokens())
		{
			String rank = rankTokenizer.nextToken();
			for (int iFile = 0; iFile < rank.length(); iFile++)
			{
				char c = rank.charAt(iFile);
				if (Character.isDigit(c))
				{
					int zeros = (c - '0');
					for (int i = 0; i < zeros; i++)
					{
						iCoordinate.next();
					}
				}
				else
				{
					board.setPiece(iCoordinate.next(), parsePiece(pieceSet, c));
				}
			}
		}

		if (iCoordinate.hasNext())
			throw new RuntimeException("parse error");
	}

	private static Piece parsePiece(PieceSet pieceSet, char c)
	{
		return pieceSet.getPiece(Character.toLowerCase(c), Character.isUpperCase(c) ? 0 : 1);
	}

	public static Piece[] parsePieces(PieceSet pieceSet, String pieces)
	{
		Piece[] result = new Piece[pieces.length()];
		for (int i = 0; i < pieces.length(); i++)
			result[i] = parsePiece(pieceSet, pieces.charAt(i));
		return result;
	}

	private StoneFenFormat()
	{
	}
}
