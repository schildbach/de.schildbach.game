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

import de.schildbach.game.Board;
import de.schildbach.game.common.CapturingGamePosition;

/**
 * @author Andreas Schildbach
 */
public class ChessLikePosition extends CapturingGamePosition
{
	private int halfmoveClock;

	public ChessLikePosition(Board board)
	{
		super(board);
	}

	public int getHalfmoveClock()
	{
		return halfmoveClock;
	}

	public void setHalfmoveClock(int halfmoveClock)
	{
		this.halfmoveClock = halfmoveClock;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;

		if (!super.equals(o))
			return false;

		ChessLikePosition other = (ChessLikePosition) o;

		if (halfmoveClock != other.halfmoveClock)
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int hashCode = super.hashCode();
		hashCode *= 37;
		hashCode += halfmoveClock;
		return hashCode;
	}
}
