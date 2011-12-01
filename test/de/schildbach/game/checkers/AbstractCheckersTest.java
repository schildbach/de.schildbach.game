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

package de.schildbach.game.checkers;

import java.util.LinkedList;
import java.util.List;

import de.schildbach.game.AbstractGameRulesTest;
import de.schildbach.game.Coordinate;
import de.schildbach.game.GameRules;

/**
 * @author Andreas Schildbach
 */
public abstract class AbstractCheckersTest extends AbstractGameRulesTest
{
	private static final CheckersRules CHECKERS_RULES = new CheckersRules(null);

	@Override
	protected GameRules gameRules()
	{
		return CHECKERS_RULES;
	}

	protected final CheckersMove move(String... points)
	{
		List<Coordinate> coordinates = new LinkedList<Coordinate>();
		for (String point : points)
			coordinates.add(coordinate(point));

		return new CheckersMove(coordinates.toArray(new Coordinate[0]));
	}

	protected final CheckersMove capture(String... points)
	{
		if (points.length % 2 == 0)
			throw new IllegalArgumentException();

		CheckersMove move = new CheckersMove(coordinate(points[0]), coordinate(points[1]), coordinate(points[2]));

		for (int i = 3; i < points.length; i += 2)
			move.addTarget(coordinate(points[i]), coordinate(points[i + 1]));

		return move;
	}
}
