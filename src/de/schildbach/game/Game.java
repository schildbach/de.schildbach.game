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
import java.util.LinkedList;
import java.util.List;

/**
 * Mutable container for moves and their corresponsing positions, representing a game (history).
 * 
 * @author Andreas Schildbach
 */
public final class Game implements Serializable
{
	private GamePosition initialPosition;
	private List<Entry> history = new LinkedList<Entry>();

	/** constructor just for serialization */
	protected Game()
	{
	}

	/** modifications should go through GameRules */
	protected Game(GameRules rules, GamePosition initialPosition)
	{
		this.initialPosition = initialPosition;
	}

	public GamePosition getInitialPosition()
	{
		return initialPosition;
	}

	public GamePosition getActualPosition()
	{
		if (isEmpty())
			return initialPosition;
		else
			return lastEntry().position;
	}

	public boolean isEmpty()
	{
		return history.isEmpty();
	}

	public int getSize()
	{
		return history.size();
	}

	/** modifications should go through GameRules */
	protected void addMove(GameMove move, GamePosition position)
	{
		history.add(new Entry(move, position));
	}

	public GameMove getMove(int index)
	{
		return history.get(index).move;
	}

	public GamePosition getPosition(int index)
	{
		return history.get(index).position;
	}

	/** modifications should go through GameRules */
	protected void removeLastMove()
	{
		history.remove(history.size() - 1);
	}

	public GameMove getLastMove()
	{
		if (history.isEmpty())
			return null;

		return lastEntry().move;
	}

	private Entry lastEntry()
	{
		return history.get(history.size() - 1);
	}

	public boolean startsWith(Game other)
	{
		if (!this.initialPosition.equals(other.initialPosition))
			return false;

		int startSize = other.history.size();
		if (startSize > this.history.size())
			return false;
		for (int i = 0; i < startSize; i++)
		{
			if (!other.history.get(i).equals(this.history.get(i)))
				return false;
		}

		return true;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		Game other = (Game) o;

		if (!this.initialPosition.equals(other.initialPosition))
			return false;

		if (!this.history.equals(other.history))
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int hashCode = initialPosition.hashCode();
		hashCode *= 37;
		hashCode += history.hashCode();
		return hashCode;
	}

	@Override
	public String toString()
	{
		return this.getClass().getName() + "[" + history.size() + " moves]";
	}

	private static class Entry implements Serializable
	{
		private GameMove move;
		private GamePosition position;

		public Entry(GameMove move, GamePosition position)
		{
			this.move = move;
			this.position = position;
		}

		@Override
		public boolean equals(Object o)
		{
			if (o == null)
				return false;
			Entry other = (Entry) o;

			if (!this.move.equals(other.move))
				return false;

			if (!this.position.equals(other.position))
				return false;

			return true;
		}

		@Override
		public int hashCode()
		{
			return move.hashCode() + 37 * position.hashCode();
		}
	}
}
