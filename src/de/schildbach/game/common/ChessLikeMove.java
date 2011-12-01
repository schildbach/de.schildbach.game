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
public class ChessLikeMove extends GameMove
{
	private Coordinate source = null;
	private Coordinate target = null;

	public ChessLikeMove(Coordinate source, Coordinate target)
	{
		super();
		assert source != null : "null source";
		assert target != null : "null target";
		this.source = source;
		this.target = target;
	}

	/**
	 * Default constructor, needed for serialization.
	 */
	protected ChessLikeMove()
	{
	}

	public Coordinate getSource()
	{
		return source;
	}

	public Coordinate getTarget()
	{
		return target;
	}

	@Override
	public boolean equals(Object o)
	{
		ChessLikeMove other = (ChessLikeMove) o;
		if (!source.equals(other.source))
			return false;
		if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int hashCode = source.hashCode();
		hashCode *= 37;
		hashCode += target.hashCode();
		hashCode *= 37;
		return hashCode;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(ObjectUtils.toString(source));
		builder.append("-");
		builder.append(ObjectUtils.toString(target));
		return builder.toString();
	}
}
