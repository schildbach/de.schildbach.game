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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Mutable representation of a game position. Subclasses may be mutable, too.
 * 
 * @author Andreas Schildbach
 */
public abstract class GamePosition implements Cloneable, Serializable
{
	private Board board;
	private int fullmoveNumber;
	private int activePlayerIndex;

	protected GamePosition(Board board)
	{
		this.board = board;
	}

	public Board getBoard()
	{
		return board;
	}

	public int getFullmoveNumber()
	{
		return fullmoveNumber;
	}

	public void setFullmoveNumber(int fullmoveNumber)
	{
		this.fullmoveNumber = fullmoveNumber;
	}

	public int getActivePlayerIndex()
	{
		return activePlayerIndex;
	}

	public void setActivePlayerIndex(int activePlayerIndex)
	{
		this.activePlayerIndex = activePlayerIndex;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		GamePosition other = (GamePosition) o;

		if (!board.equals(other.board))
			return false;

		if (fullmoveNumber != other.fullmoveNumber)
			return false;

		if (activePlayerIndex != other.activePlayerIndex)
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int hashCode = 0;
		hashCode += board.hashCode();
		hashCode *= 37;
		hashCode += fullmoveNumber;
		hashCode *= 37;
		hashCode += activePlayerIndex;
		return hashCode;
	}

	@Override
	public Object clone()
	{
		try
		{
			GamePosition clone = (GamePosition) super.clone();
			clone.board = (Board) this.board.clone();
			return clone;
		}
		catch (CloneNotSupportedException x)
		{
			throw new RuntimeException(x);
		}
	}

	@Override
	public final String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
}
