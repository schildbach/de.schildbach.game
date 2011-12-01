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

/**
 * @author Andreas Schildbach
 */
public class CoordinateFieldNotations implements OrthogonalFieldNotations
{
	private int dimension;
	private char[] baseChars;
	private int[] order;

	public CoordinateFieldNotations(int[] order, char... baseChars)
	{
		dimension = baseChars.length;

		if (order != null)
		{
			if (order.length != dimension)
				throw new IllegalArgumentException();
			this.order = order;
		}
		else
		{
			this.order = new int[dimension];
			for (int i = 0; i < dimension; i++)
				this.order[i] = i;
		}

		this.baseChars = baseChars;

	}

	public CoordinateFieldNotations(char... baseChars)
	{
		this(null, baseChars);
	}

	public String notation(int... components)
	{
		if (components.length != dimension)
			throw new IllegalArgumentException();

		StringBuilder notation = new StringBuilder();

		for (int i = 0; i < components.length; i++)
		{
			char baseChar = baseChars[i];
			if (baseChar == 'a' || baseChar == '1')
				notation.append((char) (baseChar + components[order[i]]));
			else
				notation.append((char) (baseChar - components[order[i]]));
		}

		return notation.toString();
	}
}
