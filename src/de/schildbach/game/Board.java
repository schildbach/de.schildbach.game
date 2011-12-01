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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Mutable container for pieces, representing a game board.
 * 
 * <p>
 * Board instances will not be re-used between games.
 * </p>
 * 
 * @author Andreas Schildbach
 */
public final class Board implements Cloneable, Serializable
{
	public static int instanceCount;

	// immutable
	private Set<Coordinate> coordinates;

	// mutable
	private Map<Coordinate, Piece> fields;
	private Map<Piece, Set<Coordinate>> pieceCoordinates;

	protected Board()
	{
		instanceCount++;
	}

	protected Board(Set<Coordinate> coordinates)
	{
		this();
		this.coordinates = Collections.unmodifiableSet(coordinates);
		this.fields = new HashMap<Coordinate, Piece>();
		this.pieceCoordinates = new HashMap<Piece, Set<Coordinate>>();
	}

	/**
	 * Puts a piece on an empty field of the board.
	 * 
	 * @param coordinate
	 *            coordinate of field to put piece on
	 * @param piece
	 *            piece to put on
	 */
	public void setPiece(Coordinate coordinate, Piece piece)
	{
		assert isValidCoordinate(coordinate) : "invalid coordinate: " + coordinate;
		assert piece != null : "no piece to set";
		assert getPiece(coordinate) == null : coordinate + " is occupied by " + getPiece(coordinate);

		internalSetPiece(coordinate, piece);
	}

	/**
	 * Removes piece from a field of the board.
	 * 
	 * @param coordinate
	 *            coordinate of field to remove piece from
	 */
	public void clearPiece(Coordinate coordinate)
	{
		assert isValidCoordinate(coordinate) : "invalid coordinate: " + coordinate;
		assert getPiece(coordinate) != null : coordinate + " is already empty";

		internalClearPiece(coordinate);
	}

	/**
	 * Peeks at a field.
	 * 
	 * @param coordinate
	 *            coordinate of field to peek at
	 * @return piece on field at coordinate or null if field is empty
	 */
	public Piece getPiece(Coordinate coordinate)
	{
		assert isValidCoordinate(coordinate) : "invalid coordinate: " + coordinate;

		return fields.get(coordinate);
	}

	/**
	 * Moves a piece on the board.
	 * 
	 * @param source
	 *            coordinate of field of piece
	 * @param target
	 *            coordinate of field to move piece to
	 */
	public void movePiece(Coordinate source, Coordinate target)
	{
		Piece movedPiece = getPiece(source);
		assert movedPiece != null : "no piece to move on " + source;

		// no-op
		if (source.equals(target))
			return;

		assert getPiece(target) == null : target + " is occupied by " + getPiece(target);

		internalClearPiece(source);
		internalSetPiece(target, movedPiece);
	}

	private void internalSetPiece(Coordinate coordinate, Piece piece)
	{
		fields.put(coordinate, piece);
		Set<Coordinate> coords = pieceCoordinates.get(piece);
		if (coords == null)
		{
			coords = new HashSet<Coordinate>();
			pieceCoordinates.put(piece, coords);
		}
		coords.add(coordinate);
	}

	private void internalClearPiece(Coordinate coordinate)
	{
		Piece piece = fields.remove(coordinate);
		Set<Coordinate> coords = pieceCoordinates.get(piece);
		coords.remove(coordinate);
		if (coords.isEmpty())
			pieceCoordinates.remove(piece);
	}

	/**
	 * Takes all pieces from the board.
	 */
	public void clear()
	{
		fields.clear();
		pieceCoordinates.clear();
	}

	/**
	 * Locates piece on the board.
	 * 
	 * @param piece
	 *            piece to locate
	 * @return coordinate of first occurence
	 */
	public Coordinate locatePiece(Piece piece)
	{
		Set<Coordinate> coords = pieceCoordinates.get(piece);
		if (coords != null)
			return coords.iterator().next();
		else
			return null;
	}

	/**
	 * Locates pieces on the board.
	 * 
	 * @param piece
	 *            piece to locate
	 * @return coordinates of piece occurences
	 */
	public Set<Coordinate> locatePieces(Piece piece)
	{
		Set<Coordinate> coordinates = pieceCoordinates.get(piece);
		if (coordinates != null)
			return coordinates;
		else
			return Collections.emptySet();
	}

	/**
	 * Locates pieces of a color on the board.
	 * 
	 * @param color
	 *            color of pieces to locate
	 * @return coordinates of piece occurences
	 */
	public Set<Coordinate> locatePieces(int color)
	{
		Set<Coordinate> result = new HashSet<Coordinate>();
		for (Map.Entry<Coordinate, Piece> field : fields.entrySet())
		{
			if (field.getValue().getColor() == color)
				result.add(field.getKey());
		}
		return result;
	}

	/**
	 * Locates occupied fields on the board.
	 * 
	 * @return coordinates of occupied fields
	 */
	public Set<Coordinate> locateOccupiedFields()
	{
		return fields.keySet();
	}

	/**
	 * Locates empty fields on the board.
	 * 
	 * @return coordinates of empty fields
	 */
	public Set<Coordinate> locateEmptyFields()
	{
		Set<Coordinate> emptyFields = new HashSet<Coordinate>(coordinates);
		emptyFields.removeAll(fields.keySet());
		return emptyFields;
	}

	private boolean isValidCoordinate(Coordinate coordinate)
	{
		if (coordinate == null)
			return false;

		return coordinates.contains(coordinate);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;

		Board other = (Board) o;

		return this.fields.equals(other.fields);
	}

	@Override
	public int hashCode()
	{
		return this.fields.hashCode();
	}

	@Override
	public Object clone()
	{
		try
		{
			Board other = (Board) super.clone();
			other.fields = new HashMap<Coordinate, Piece>(this.fields);
			other.pieceCoordinates = new HashMap<Piece, Set<Coordinate>>(this.pieceCoordinates);
			for (Map.Entry<Piece, Set<Coordinate>> entry : other.pieceCoordinates.entrySet())
				entry.setValue(new HashSet<Coordinate>(entry.getValue()));
			return other;
		}
		catch (CloneNotSupportedException x)
		{
			throw new RuntimeException(x);
		}
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder("Board[" + coordinates.size() + " coordinates, " + fields.size() + " occupied: ");
		for (Map.Entry<Piece, Set<Coordinate>> entry : pieceCoordinates.entrySet())
		{
			builder.append(entry.getKey() + " on ");
			for (Coordinate c : entry.getValue())
				builder.append(c + ",");
			builder.setLength(builder.length() - 1);
			builder.append("; ");
		}
		builder.setLength(builder.length() - 2);
		builder.append("]");
		return builder.toString();
	}
}
