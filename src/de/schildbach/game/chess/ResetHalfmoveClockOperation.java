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
public class ResetHalfmoveClockOperation implements MicroOperation
{
	private int undoHalfmoveClock;

	public void doOperation(GamePosition position)
	{
		ChessLikePosition chessLikePosition = (ChessLikePosition) position;
		assert undoHalfmoveClock == 0;
		undoHalfmoveClock = chessLikePosition.getHalfmoveClock();
		chessLikePosition.setHalfmoveClock(0);
	}

	public void undoOperation(GamePosition position)
	{
		ChessLikePosition chessLikePosition = (ChessLikePosition) position;
		assert chessLikePosition.getHalfmoveClock() == 0;
		chessLikePosition.setHalfmoveClock(undoHalfmoveClock);
		undoHalfmoveClock = 0;
	}

	@Override
	public String toString()
	{
		return "reset halfmove clock";
	}
}
