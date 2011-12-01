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
public class BigCoordinateFieldNotations implements OrthogonalFieldNotations
{
	private char separator;

	public BigCoordinateFieldNotations()
	{
		this('/');
	}

	private BigCoordinateFieldNotations(char separator)
	{
		this.separator = separator;
	}

	public String notation(int... components)
	{
		StringBuilder label = new StringBuilder();

		for (int i = 0; i < components.length; i++)
		{
			label.append(components[i] + 1);
			label.append(separator);
		}
		label.setLength(label.length() - 1);

		return label.toString();
	}
}
