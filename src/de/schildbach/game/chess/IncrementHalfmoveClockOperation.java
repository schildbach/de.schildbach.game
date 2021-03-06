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

package de.schildbach.game.chess;

import de.schildbach.game.GamePosition;
import de.schildbach.game.MicroOperation;

/**
 * @author Andreas Schildbach
 */
public class IncrementHalfmoveClockOperation implements MicroOperation
{
	private static final IncrementHalfmoveClockOperation INSTANCE = new IncrementHalfmoveClockOperation();

	private IncrementHalfmoveClockOperation()
	{
	}

	public static IncrementHalfmoveClockOperation instance()
	{
		return INSTANCE;
	}

	public void doOperation(GamePosition position)
	{
		ChessLikePosition chessLikePosition = (ChessLikePosition) position;
		chessLikePosition.setHalfmoveClock(chessLikePosition.getHalfmoveClock() + 1);
	}

	public void undoOperation(GamePosition position)
	{
		ChessLikePosition chessLikePosition = (ChessLikePosition) position;
		chessLikePosition.setHalfmoveClock(chessLikePosition.getHalfmoveClock() - 1);
	}

	@Override
	public String toString()
	{
		return "increment halfmove clock";
	}
}
