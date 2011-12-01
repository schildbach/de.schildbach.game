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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.schildbach.game.Piece;
import de.schildbach.game.PieceSet;

/**
 * Factory for pieces.
 * 
 * @author Andreas Schildbach
 */
public final class GenericPieceSet extends PieceSet
{
	public static int instanceCount;

	private final List<Piece> pieces;
	private final Map<Character, Piece[]> charPieceMap = new HashMap<Character, Piece[]>();
	private final Map<String, Piece> stringPieceMap = new HashMap<String, Piece>();
	private final Map<Piece, String> pieceStringMap = new HashMap<Piece, String>();
	private final Map<Class<? extends Piece>, Character> pieceCharMap = new HashMap<Class<? extends Piece>, Character>();
	private final Map<Class<? extends Piece>, Character> pieceCharDeMap = new HashMap<Class<? extends Piece>, Character>();

	public GenericPieceSet(PieceEntry[] pieceEntries, ColorEntry[] colorEntries)
	{
		super(colorEntries);

		List<Piece> piecesList = new LinkedList<Piece>();

		for (PieceEntry entry : pieceEntries)
		{
			Piece[] pieces = new Piece[colorEntries.length];

			for (ColorEntry color : colorEntries)
			{
				Piece piece = instantiatePiece(entry.pieceClass, color.index);
				pieces[color.index] = piece;
				piecesList.add(piece);
				String stringRepresentation = "" + color.chr + entry.charRepresentation;
				stringPieceMap.put(stringRepresentation, piece);
				pieceStringMap.put(piece, stringRepresentation);
			}

			charPieceMap.put(entry.charRepresentation, pieces);
			pieceCharMap.put(entry.pieceClass, entry.charRepresentation);
			pieceCharDeMap.put(entry.pieceClass, entry.charRepresentationDE);
		}

		this.pieces = Collections.unmodifiableList(piecesList);
	}

	@Override
	public Piece getPiece(Class<? extends Piece> pieceClass, int color)
	{
		return getPiece(getCharRepresentation(pieceClass), color);
	}

	@Override
	public Piece getPiece(char charRepresentation, int color)
	{
		return charPieceMap.get(charRepresentation)[color];
	}

	@Override
	public Piece getPiece(String stringRepresentation)
	{
		return stringPieceMap.get(stringRepresentation);
	}

	@Override
	public char getCharRepresentation(Class<? extends Piece> pieceClass)
	{
		return pieceCharMap.get(pieceClass);
	}

	@Override
	public char getCharRepresentation(Class<? extends Piece> pieceClass, Locale locale)
	{
		if (locale.equals(Locale.GERMAN))
			return pieceCharDeMap.get(pieceClass);
		else
			return pieceCharMap.get(pieceClass);
	}

	@Override
	public String getStringRepresentation(Piece piece)
	{
		return pieceStringMap.get(piece);
	}

	@Override
	public List<Piece> getPieces()
	{
		return pieces;
	}

	@Override
	public int size()
	{
		return charPieceMap.size();
	}

	public static class PieceEntry
	{
		public char charRepresentation;
		public char charRepresentationDE;
		public Class<? extends Piece> pieceClass;

		public PieceEntry(char charRepresentation, char charRepresentationDE, Class<? extends Piece> pieceClass)
		{
			this.charRepresentation = charRepresentation;
			this.charRepresentationDE = charRepresentationDE;
			this.pieceClass = pieceClass;
		}
	}
}
