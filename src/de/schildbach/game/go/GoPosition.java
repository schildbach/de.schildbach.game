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

package de.schildbach.game.go;

import java.util.Arrays;

import de.schildbach.game.Board;
import de.schildbach.game.GamePosition;

/**
 * @author Andreas Schildbach
 */
public class GoPosition extends GamePosition
{
	private int[] captureCount = new int[2];

	public GoPosition(Board board)
	{
		super(board);
	}

	public int[] getCaptureCount()
	{
		return captureCount;
	}

	public int getCaptureCount(int color)
	{
		return captureCount[color];
	}

	public void addCaptureCount(int color, int value)
	{
		this.captureCount[color] += value;
	}

	public boolean hasAnythingBeenCaptured()
	{
		for (int count : captureCount)
			if (count > 0)
				return true;

		return false;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;

		if (!super.equals(o))
			return false;

		GoPosition other = (GoPosition) o;

		if (!Arrays.equals(this.captureCount, other.captureCount))
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int hashCode = super.hashCode();
		hashCode *= 37;
		hashCode += Arrays.hashCode(captureCount);
		return hashCode;
	}

	@Override
	public Object clone()
	{
		GoPosition clone = (GoPosition) super.clone();
		clone.captureCount = this.captureCount.clone();
		return clone;
	}
}
