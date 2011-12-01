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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Board geometries are the only classes that are allowed to do arithmetic operations on game coordinates. All other
 * classes should never deal with specific coordinate subclasses.
 * <ul>
 * <li>there is only one board geometry instance for each type of board</li>
 * <li>board geometries serve as factories for their boards</li>
 * <li>the geometry knows about the notation of their coordinates</li>
 * <li>board geometries also serve as factories for their dedicated coordinates (allowing to share coordinate instances
 * between all games with the same geometry)</li>
 * </ul>
 * 
 * @author Andreas Schildbach
 */
public abstract class BoardGeometry
{
	public static int instanceCount;

	protected static final Log LOG = LogFactory.getLog(BoardGeometry.class);

	private final Coordinate[] coordinates;

	protected BoardGeometry(int numCoordinates)
	{
		instanceCount++;
		coordinates = new Coordinate[numCoordinates];
	}

	private final transient Map<String, Coordinate> coordinateNotations = new LinkedHashMap<String, Coordinate>();

	protected final void putCoordinate(int key, Coordinate coordinate)
	{
		coordinateNotations.put(coordinate.getNotation(), coordinate);
		coordinates[key] = coordinate;
	}

	protected final Coordinate getCoordinate(int key)
	{
		return coordinates[key];
	}

	// interface:

	public final Board newBoard()
	{
		return new Board(new HashSet<Coordinate>(Arrays.asList(coordinates)));
	}

	public final Iterator<Coordinate> coordinateIterator()
	{
		return coordinateNotations.values().iterator();
	}

	protected Iterator<Coordinate> pieceIterator(final Board board, final int color)
	{
		final Iterator<Coordinate> boardIterator = this.coordinateIterator();
		return new Iterator<Coordinate>()
		{
			Coordinate next = null;

			public boolean hasNext()
			{
				if (next != null)
				{
					return true;
				}
				else
				{
					while (boardIterator.hasNext())
					{
						next = boardIterator.next();
						Piece piece = board.getPiece(next);
						if (piece != null && piece.getColor() == color)
							return true;
					}
					next = null;
					return false;
				}
			}

			public Coordinate next()
			{
				if (hasNext())
				{
					Coordinate result = next;
					next = null;
					return result;
				}
				else
					throw new NoSuchElementException();
			}

			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}

	public abstract boolean isValidCoordinate(Coordinate coordinate);

	/**
	 * Locates a coordinate within this geometry.
	 * 
	 * @param notation
	 *            notation of coordinate to locate
	 * @return located coordinate or null if coordinate not found
	 */
	public final Coordinate locateCoordinate(String notation)
	{
		return coordinateNotations.get(notation);
	}
}
