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

/**
 * Immutable representation for Coordinates in a BoardGeometry. Subclasses of this class are to be immutable, too.
 * 
 * @author Andreas Schildbach
 */
public abstract class Coordinate implements Serializable
{
	public static int instanceCount;

	private String notation;

	private Coordinate()
	{
		instanceCount++;
	}

	protected Coordinate(String notation)
	{
		this();
		this.notation = notation;
	}

	public final String getNotation()
	{
		return notation;
	}

	@Override
	public final String toString()
	{
		return notation;
	}

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract int hashCode();
}
