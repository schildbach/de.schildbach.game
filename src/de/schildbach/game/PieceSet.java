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

package de.schildbach.game;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Locale;

/**
 * Factory for pieces.
 * 
 * @author Andreas Schildbach
 */
public abstract class PieceSet
{
	public static int instanceCount;

	private final String[] colorTags;
	private final char[] colorChars;

	protected PieceSet(ColorEntry[] colors)
	{
		instanceCount++;

		colorTags = new String[colors.length];
		colorChars = new char[colors.length];
		for (ColorEntry color : colors)
		{
			colorTags[color.index] = color.tag;
			colorChars[color.index] = color.chr;
		}
	}

	/**
	 * Piece factory method by piece class and separate piece color.
	 * 
	 * @param pieceClass
	 *            class of piece to be retrieved
	 * @param color
	 *            color of the piece to be retrieved
	 * @return retrieved piece
	 */
	public Piece getPiece(Class<? extends Piece> pieceClass, int color)
	{
		return getPiece(getCharRepresentation(pieceClass), color);
	}

	/**
	 * Piece factory method by character representation and separate color.
	 * 
	 * @param charRepresentation
	 *            character representation of the piece to be retrieved
	 * @param color
	 *            color of the piece to be retrieved
	 * @return retrieved piece
	 */
	public abstract Piece getPiece(char charRepresentation, int color);

	/**
	 * Piece factory method by string representation (including color).
	 * 
	 * @param stringRepresentation
	 *            string representation of the piece to be retrieved
	 * @return retrieved piece
	 */
	public abstract Piece getPiece(String stringRepresentation);

	/**
	 * Gets the character representation of a piece's class.
	 * 
	 * @param pieceClass
	 *            class of piece to be represented
	 * @return character representation
	 */
	public abstract char getCharRepresentation(Class<? extends Piece> pieceClass);

	/**
	 * Gets the localized character representation of a piece's class.
	 * 
	 * @param pieceClass
	 *            class of piece to be represented
	 * @param locale
	 *            locale to be applied to the representation
	 * @return character representation
	 */
	public abstract char getCharRepresentation(Class<? extends Piece> pieceClass, Locale locale);

	/**
	 * Gets the string representation of a piece, including color information.
	 * 
	 * @param piece
	 *            piece to be represented
	 * @return string representation
	 */
	public abstract String getStringRepresentation(Piece piece);

	/**
	 * Gets all pieces that are part of this set (all colors).
	 * 
	 * @return all pieces of set
	 */
	public abstract List<Piece> getPieces();

	/**
	 * Gets number of pieces for this set (one color only).
	 * 
	 * @return number of pieces
	 */
	public abstract int size();

	/**
	 * Gets string tags for the colors of this set.
	 * 
	 * @return color tags
	 */
	public final String[] getColorTags()
	{
		return colorTags;
	}

	/**
	 * Gets chars for the colors of this set.
	 * 
	 * @return color tags
	 */
	public final char[] getColorChars()
	{
		return colorChars;
	}

	/**
	 * Gets the string tag for the color of a specific player index.
	 * 
	 * @param playerIndex
	 *            index of the player to get the tag for
	 * @return color tag
	 */
	public final String getColorTag(int playerIndex)
	{
		return getColorTags()[playerIndex];
	}

	protected Piece instantiatePiece(Class<? extends Piece> pieceClass, int color)
	{
		try
		{
			Constructor<? extends Piece> constructor = pieceClass.getDeclaredConstructor(new Class[] { int.class });
			return constructor.newInstance(new Object[] { color });
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static class ColorEntry
	{
		public int index;
		public String tag;
		public char chr;

		public ColorEntry(int index, String tag, char chr)
		{
			this.index = index;
			this.tag = tag;
			this.chr = chr;
		}
	}
}
