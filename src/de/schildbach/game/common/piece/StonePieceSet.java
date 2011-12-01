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

package de.schildbach.game.common.piece;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.schildbach.game.Piece;
import de.schildbach.game.PieceSet;

/**
 * @author Andreas Schildbach
 */
public class StonePieceSet extends PieceSet
{
	private Map<Character, Piece> charPieceMap = new HashMap<Character, Piece>();
	private Map<Piece, Character> pieceCharMap = new HashMap<Piece, Character>();

	private static final Object[][] PIECES = { new Object[] { 'b', new Stone(0) }, //
			new Object[] { 'w', new Stone(1) } //
	};

	private static final ColorEntry[] COLORS = { new ColorEntry(0, "black", 'b'), //
			new ColorEntry(1, "white", 'w') //
	};

	public StonePieceSet()
	{
		super(COLORS);

		for (Object[] entry : PIECES)
		{
			Character ch = (Character) entry[0];
			Piece piece = (Piece) entry[1];
			charPieceMap.put(ch, piece);
			pieceCharMap.put(piece, ch);
		}
	}

	@Override
	public Piece getPiece(char charRepresentation, int color)
	{
		return charPieceMap.get(charRepresentation);
	}

	@Override
	public Piece getPiece(String stringRepresentation)
	{
		if (stringRepresentation.length() != 1)
			return null;

		return getPiece(stringRepresentation.charAt(0), 0);
	}

	@Override
	public Piece getPiece(Class<? extends Piece> pieceClass, int color)
	{
		return (Piece) PIECES[color][1];
	}

	@Override
	public char getCharRepresentation(Class<? extends Piece> pieceClass)
	{
		return pieceCharMap.get(instantiatePiece(pieceClass, 0));
	}

	@Override
	public char getCharRepresentation(Class<? extends Piece> pieceClass, Locale locale)
	{
		return pieceCharMap.get(instantiatePiece(pieceClass, 0));
	}

	@Override
	public String getStringRepresentation(Piece piece)
	{
		return pieceCharMap.get(piece).toString();
	}

	@Override
	public List<Piece> getPieces()
	{
		return new LinkedList<Piece>(charPieceMap.values());
	}

	@Override
	public int size()
	{
		return 1;
	}
}
