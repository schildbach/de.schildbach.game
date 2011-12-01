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

import org.apache.commons.lang.ObjectUtils;

import de.schildbach.game.Coordinate;
import de.schildbach.game.GameMove;

/**
 * @author Andreas Schildbach
 */
public class SingleCoordinateMove extends GameMove
{
	public static final SingleCoordinateMove PASS = new SingleCoordinateMove();

	private Coordinate coordinate;

	/**
	 * Main constructor.
	 */
	public SingleCoordinateMove(Coordinate coordinate)
	{
		this.coordinate = coordinate;
	}

	/**
	 * Default constructor, needed for serialization.
	 */
	protected SingleCoordinateMove()
	{
	}

	public boolean isPass()
	{
		return coordinate == null;
	}

	public Coordinate getCoordinate()
	{
		return coordinate;
	}

	@Override
	public String toString()
	{
		return isPass() ? "pass" : coordinate.toString();
	}

	@Override
	public boolean equals(Object obj)
	{
		SingleCoordinateMove other = (SingleCoordinateMove) obj;
		return ObjectUtils.equals(this.coordinate, other.coordinate);
	}

	@Override
	public int hashCode()
	{
		return ObjectUtils.hashCode(coordinate);
	}
}
