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

import de.schildbach.game.GamePosition;
import de.schildbach.game.MicroOperation;

/**
 * @author Andreas Schildbach
 */
public class NextPlayerOperation implements MicroOperation
{
	private static final int MAX_PLAYERS = 2;

	private static final NextPlayerOperation INSTANCE = new NextPlayerOperation();

	private NextPlayerOperation()
	{
	}

	public static NextPlayerOperation instance()
	{
		return INSTANCE;
	}

	public void doOperation(GamePosition position)
	{
		// read
		int activePlayerIndex = position.getActivePlayerIndex();

		// increment active player index
		activePlayerIndex++;

		// increment fullmove number
		if (activePlayerIndex >= MAX_PLAYERS)
		{
			activePlayerIndex = 0;
			position.setFullmoveNumber(position.getFullmoveNumber() + 1);
		}

		// write back
		position.setActivePlayerIndex(activePlayerIndex);
	}

	public void undoOperation(GamePosition position)
	{
		// read
		int activePlayerIndex = position.getActivePlayerIndex();

		// decrement active player index
		activePlayerIndex--;

		// decrement fullmove number
		if (activePlayerIndex < 0)
		{
			activePlayerIndex = MAX_PLAYERS - 1;
			position.setFullmoveNumber(position.getFullmoveNumber() - 1);
		}

		// write back
		position.setActivePlayerIndex(activePlayerIndex);
	}

	@Override
	public String toString()
	{
		return "next player";
	}
}
