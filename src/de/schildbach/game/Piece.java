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
 * Immutable representation of a game piece. Subclasses of this class are to be immutable, too.
 * 
 * @author Andreas Schildbach
 */
public abstract class Piece implements Comparable<Piece>, Serializable
{
	public static int instanceCount;

	private int color;

	protected Piece()
	{
		instanceCount++;
	}

	protected Piece(int color)
	{
		this();
		this.color = color;
	}

	public final int getColor()
	{
		return color;
	}

	public final boolean sameColor(Piece piece)
	{
		return this.getColor() == piece.getColor();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;

		Piece other = (Piece) obj;

		if (!this.getClass().equals(other.getClass()))
			return false;

		if (this.color != other.color)
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		return (this.getClass().hashCode() << 1) + this.color;
	}

	public int compareTo(Piece other)
	{
		if (this.color != other.color)
			return this.color > other.color ? 1 : -1;

		return this.getClass().getCanonicalName().compareTo(other.getClass().getCanonicalName());
	}

	/**
	 * This string representation is just meant for debugging. The format is subject to change.
	 */
	@Override
	public final String toString()
	{
		return getClass().getSimpleName() + "(" + color + ")";
	}
}
