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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import de.schildbach.game.Coordinate;
import de.schildbach.game.GameMove;

/**
 * @author Andreas Schildbach
 */
public class CheckersMove extends GameMove
{
	private List<Coordinate> points = new LinkedList<Coordinate>();
	private List<Coordinate> captures = new LinkedList<Coordinate>();
	private int captureQuantity = 0;

	public CheckersMove(Coordinate source, Coordinate target)
	{
		addTarget(source);
		addTarget(target);
	}

	public CheckersMove(Coordinate source, Coordinate target, Coordinate capture)
	{
		addTarget(source);
		addTarget(target, capture);
	}

	public CheckersMove(Coordinate[] points)
	{
		for (int i = 0; i < points.length; i++)
			addTarget(points[i]);
	}

	protected CheckersMove(CheckersMove original)
	{
		this.points = new LinkedList<Coordinate>(original.points);
		this.captures = new LinkedList<Coordinate>(original.captures);
		this.captureQuantity = original.captureQuantity;
	}

	/**
	 * Default constructor, needed for serialization.
	 */
	protected CheckersMove()
	{
	}

	public void addTarget(Coordinate target)
	{
		points.add(target);
		captures.add(null);
	}

	public void addTarget(Coordinate target, Coordinate capture)
	{
		points.add(target);
		captures.add(capture);
		captureQuantity++;
	}

	public Coordinate getSource()
	{
		return points.get(0);
	}

	public List<Coordinate> getPoints()
	{
		return points;
	}

	public List<Coordinate> getCaptures()
	{
		return captures;
	}

	public Coordinate getLastTarget()
	{
		return points.get(points.size() - 1);
	}

	protected void removeLastTarget()
	{
		points.remove(points.size() - 1);
		Coordinate capture = captures.remove(captures.size() - 1);
		if (capture != null)
			captureQuantity--;
	}

	public final int getCaptureQuantity()
	{
		return captureQuantity;
	}

	private static class CheckersCoordinateComperator implements Comparator<Coordinate>
	{
		public int compare(Coordinate c1, Coordinate c2)
		{
			return c1.getNotation().compareTo(c2.getNotation());
		}
	}

	private static final CheckersCoordinateComperator CHECKERS_COORDINATE_COMPERATOR = new CheckersCoordinateComperator();

	@Override
	public boolean equals(Object o)
	{
		CheckersMove other = (CheckersMove) o;
		List<Coordinate> sortedPoints = new LinkedList<Coordinate>(points);
		Collections.sort(sortedPoints, CHECKERS_COORDINATE_COMPERATOR);
		List<Coordinate> otherSortedPoints = new LinkedList<Coordinate>(other.points);
		Collections.sort(otherSortedPoints, CHECKERS_COORDINATE_COMPERATOR);

		return sortedPoints.equals(otherSortedPoints);
	}

	@Override
	public int hashCode()
	{
		int hashCode = 0;

		for (Coordinate point : points)
			hashCode += point.hashCode();

		return hashCode;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < points.size(); i++)
		{
			Coordinate point = points.get(i);
			Coordinate capture = captures.get(i);
			builder.append(point.getNotation());
			if (capture != null)
			{
				builder.append("c");
				builder.append(capture.getNotation());
			}
			if (i < points.size() - 1)
				builder.append('-');
		}

		return builder.toString();
	}
}
