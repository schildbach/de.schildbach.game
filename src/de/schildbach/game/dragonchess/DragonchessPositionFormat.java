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

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import de.schildbach.game.Board;
import de.schildbach.game.PieceSet;
import de.schildbach.game.common.CapturingGamePosition;
import de.schildbach.game.common.FenFormat;
import de.schildbach.game.common.OrthogonalBoardGeometry;
import de.schildbach.game.exception.ParseException;

/**
 * @author Andreas Schildbach
 */
public final class DragonchessPositionFormat
{
	public static String format(OrthogonalBoardGeometry geometry, PieceSet pieceSet, CapturingGamePosition position)
	{
		StringBuilder fen = new StringBuilder();

		// piece placement
		fen.append(FenFormat.format(geometry, pieceSet, position.getBoard()));

		// active color
		fen.append(" ");
		fen.append(DragonchessColor.colorNotation(position.getActivePlayerIndex()));

		// fullmove number
		fen.append(" ");
		fen.append(position.getFullmoveNumber());

		return fen.toString();
	}

	public static CapturingGamePosition parse(OrthogonalBoardGeometry geometry, PieceSet pieceSet, String fen)
	{
		try
		{
			StringTokenizer tokenizer = new StringTokenizer(fen);

			// piece placement
			Board board = geometry.newBoard();
			FenFormat.parse(geometry, pieceSet, tokenizer.nextToken() + " " + tokenizer.nextToken() + " " + tokenizer.nextToken(), board);
			CapturingGamePosition position = new CapturingGamePosition(board);

			// active color
			position.setActivePlayerIndex(DragonchessColor.notationToColor(tokenizer.nextToken().charAt(0)));

			// fullmove number
			position.setFullmoveNumber(Integer.parseInt(tokenizer.nextToken()));

			if (tokenizer.hasMoreTokens())
				throw new ParseException(fen, "too many fields");

			return position;
		}
		catch (NoSuchElementException x)
		{
			throw new ParseException(fen, "not enough fields", x);
		}
	}

	private DragonchessPositionFormat()
	{
	}
}
