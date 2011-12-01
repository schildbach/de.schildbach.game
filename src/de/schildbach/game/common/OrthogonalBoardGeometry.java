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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import de.schildbach.game.BoardGeometry;
import de.schildbach.game.Coordinate;

/**
 * @author Andreas Schildbach
 */
public class OrthogonalBoardGeometry extends BoardGeometry
{
	public static final int AXIS_WIDTH = 0;
	public static final int AXIS_HEIGHT = 1;
	public static final int AXIS_LAYER = 2;

	private final int dimension;
	private final int[] size;
	protected final OrthogonalFieldNotations orthogonalFieldNotations;

	public OrthogonalBoardGeometry(int[] size, OrthogonalFieldNotations orthogonalFieldNotations)
	{
		super(computeSize(size));
		this.dimension = size.length;
		this.size = size;
		this.orthogonalFieldNotations = orthogonalFieldNotations;
		if (dimension == 2)
			initCoordinates2D();
		else if (dimension == 3)
			initCoordinates3D();
		else
			throw new IllegalArgumentException("cannot support " + dimension + "dimensions");
	}

	/**
	 * Computes the number of coordinates, resulting from the given dimensions.
	 * 
	 * @param size
	 *            array of dimensions
	 * @return number of coordinates
	 */
	private static int computeSize(int[] size)
	{
		int s = 1;

		for (int i = 0; i < size.length; i++)
			s *= size[i];

		return s;
	}

	// initialization

	private void initCoordinates2D()
	{
		int width = getSize(AXIS_WIDTH);
		int height = getSize(AXIS_HEIGHT);

		for (int y = height - 1; y >= 0; y--)
		{
			for (int x = 0; x < width; x++)
			{
				int[] components = new int[] { x, y };
				if (orthogonalFieldNotations.notation(components) != null)
				{
					addCoordinate(components);
				}
			}
		}
	}

	private void initCoordinates3D()
	{
		int width = getSize(AXIS_WIDTH);
		int height = getSize(AXIS_HEIGHT);
		int layers = getSize(AXIS_LAYER);

		for (int z = 0; z < layers; z++)
		{
			for (int y = height - 1; y >= 0; y--)
			{
				for (int x = 0; x < width; x++)
				{
					addCoordinate(x, y, z);
				}
			}
		}
	}

	private void addCoordinate(int... components)
	{
		OrthogonalCoordinate coordinate = new OrthogonalCoordinate(components, orthogonalFieldNotations.notation(components));
		putCoordinate(computeCoordinateKey(components), coordinate);
	}

	protected final int computeCoordinateKey(int[] components)
	{
		int key = 0;
		if (dimension >= 2)
		{
			int width = getSize(AXIS_WIDTH);
			int height = getSize(AXIS_HEIGHT);
			if (dimension >= 3)
			{
				key += components[AXIS_LAYER];
				key *= height;
			}
			key += height - 1 - components[AXIS_HEIGHT];
			key *= width;
		}
		key += components[AXIS_WIDTH];
		return key;
	}

	// basics

	public final int getDimension()
	{
		return dimension;
	}

	public final int getSize(int axis)
	{
		return size[axis];
	}

	// coordinates

	public final Coordinate getCoordinate(int... components)
	{
		if (!isValidCoordinate(components))
			return null;

		int key = computeCoordinateKey(components);

		return getCoordinate(key);
	}

	@Override
	public final boolean isValidCoordinate(Coordinate coordinate)
	{
		OrthogonalCoordinate oc = (OrthogonalCoordinate) coordinate;
		return isValidCoordinate(oc.components);
	}

	private boolean isValidCoordinate(int[] components)
	{
		assert components.length == dimension : "dimension mismatch";

		for (int i = 0; i < dimension; i++)
			if (components[i] < 0 || components[i] >= size[i])
				return false;

		return true;
	}

	/**
	 * Substracts c2 from c1.
	 * 
	 * @return c1 - c2
	 */
	public int[] coordinateDiff(Coordinate c1, Coordinate c2)
	{
		OrthogonalCoordinate oc1 = (OrthogonalCoordinate) c1;
		OrthogonalCoordinate oc2 = (OrthogonalCoordinate) c2;

		assert oc1.components.length == dimension : "dimension mismatch";
		assert oc2.components.length == dimension : "dimension mismatch";

		int[] diff = new int[dimension];

		for (int i = 0; i < dimension; i++)
			diff[i] = oc1.components[i] - oc2.components[i];

		return diff;
	}

	/**
	 * For components of a specific axis, substracts c2 from c1.
	 * 
	 * @return component of c1 - c2
	 */

	protected int coordinateDiff(int axis, Coordinate c1, Coordinate c2)
	{
		OrthogonalCoordinate oc1 = (OrthogonalCoordinate) c1;
		OrthogonalCoordinate oc2 = (OrthogonalCoordinate) c2;

		return oc1.components[axis] - oc2.components[axis];
	}

	/**
	 * Gets component of a coordinate.
	 * 
	 * @return component of c
	 */
	protected int getComponent(int axis, Coordinate c)
	{
		OrthogonalCoordinate oc = (OrthogonalCoordinate) c;

		return oc.components[axis];
	}

	public int[] getComponents(Coordinate c)
	{
		OrthogonalCoordinate oc = (OrthogonalCoordinate) c;

		return oc.components;
	}

	// vectors

	/**
	 * Adds vector to vector.
	 * 
	 * @return sum of vectors
	 */
	public int[] vectorAdd(int[] v1, int[] v2)
	{
		assert v1.length <= this.dimension : "v1 has to have dimension of " + this.dimension;
		assert v2.length <= this.dimension : "v2 has to have dimension of " + this.dimension;

		int dimension = Math.max(v1.length, v2.length);

		int[] v = new int[dimension];

		for (int i = 0; i < dimension; i++)
			v[i] = (i < v1.length ? v1[i] : 0) + (i < v2.length ? v2[i] : 0);

		return v;
	}

	/**
	 * Adds vector to coordinate, multiplied by factor. Vector needs to have a dimension of coordinate or less.
	 * 
	 * @param c
	 *            coordinate to add to
	 * @param vector
	 *            summand
	 * @param factor
	 *            how many times to add the summand
	 * @return coordinate + (vector * factor)
	 */
	public Coordinate vectorAdd(Coordinate c, int[] vector, int factor)
	{
		OrthogonalCoordinate oc = (OrthogonalCoordinate) c;

		assert oc.components.length == this.dimension : "dimension mismatch";
		assert vector.length <= this.dimension : "dimension mismatch";

		int[] components = new int[dimension];

		for (int i = 0; i < dimension; i++)
		{
			if (i < vector.length)
				components[i] = oc.components[i] + vector[i] * factor;
			else
				components[i] = oc.components[i];
		}

		return getCoordinate(components);
	}

	/**
	 * Adds vector to coordinate until edge of geometry is reached.
	 * 
	 * @return maximum coordinate
	 */
	public Coordinate vectorAddMax(Coordinate coordinate, int[] vector)
	{
		while (true)
		{
			Coordinate c = vectorAdd(coordinate, vector, 1);
			if (c == null)
				return coordinate;
			coordinate = c;
		}
	}

	/**
	 * Determines the normal vectors of the geometry.
	 * 
	 * @return normals, one for each dimension
	 */
	public Set<int[]> normalVectors()
	{
		Set<int[]> vectors = new HashSet<int[]>();

		for (int i = 0; i < dimension; i++)
		{
			int[] vector = new int[dimension];
			vector[i] = 1;
			vectors.add(vector);
		}

		return vectors;
	}

	/**
	 * Inverts a vector.
	 * 
	 * @return inverse of vector
	 */
	public int[] inverseVector(int[] vector)
	{
		assert vector.length == this.dimension : "dimension mismatch";

		int[] inverseVector = new int[dimension];

		for (int i = 0; i < dimension; i++)
			inverseVector[i] = -vector[i];

		return inverseVector;
	}

	/**
	 * Makes a vector bi-directional.
	 * 
	 * @return vector and the inverse of vector
	 */
	public Set<int[]> bidirectional(int[] vector)
	{
		Set<int[]> vectors = new HashSet<int[]>();
		vectors.add(vector);
		vectors.add(inverseVector(vector));
		return vectors;
	}

	/**
	 * Tries to reach c2 from c1, using step or factors of it.
	 * 
	 * @return step if factors are positive, the inverse of step if factors are negative or null if c2 cannot be reached
	 */
	public int[] dir(int[] step, Coordinate c1, Coordinate c2)
	{
		OrthogonalCoordinate oc1 = (OrthogonalCoordinate) c1;
		OrthogonalCoordinate oc2 = (OrthogonalCoordinate) c2;

		int factor = Integer.MAX_VALUE;
		for (int i = 0; i < dimension; i++)
		{
			if (step[i] == 0)
			{
				if ((oc2.components[i] != oc1.components[i]))
					return null;
			}
			else
			{
				int f = (oc2.components[i] - oc1.components[i]) / step[i];
				if (factor == Integer.MAX_VALUE)
					factor = f;
				else if (f != factor)
					return null;
			}
		}

		if (factor < 0)
			step = inverseVector(step);

		return step;
	}

	public void filterFacingDir(Set<int[]> vectors, int[] dir, boolean includeOrthogonals)
	{
		for (Iterator<int[]> iVector = vectors.iterator(); iVector.hasNext();)
		{
			int[] vector = iVector.next();
			boolean remove = false;

			for (int i = 0; i < dir.length; i++)
			{
				if (dir[i] > 0)
				{
					if ((includeOrthogonals && vector[i] < 0) || (!includeOrthogonals && vector[i] <= 0))
					{
						remove = true;
						break;
					}
				}
				else if (dir[i] < 0)
				{
					if ((includeOrthogonals && vector[i] > 0) || (!includeOrthogonals && vector[i] >= 0))
					{
						remove = true;
						break;
					}
				}
			}

			if (remove)
				iVector.remove();
		}
	}

	public void filterFacingOppositeDir(Set<int[]> vectors, int[] dir)
	{
		for (Iterator<int[]> iVector = vectors.iterator(); iVector.hasNext();)
		{
			int[] vector = iVector.next();
			boolean remove = false;

			for (int i = 0; i < dir.length; i++)
			{
				if (dir[i] > 0)
				{
					if (vector[i] >= 0)
					{
						remove = true;
						break;
					}
				}
				else if (dir[i] < 0)
				{
					if (vector[i] <= 0)
					{
						remove = true;
						break;
					}
				}
			}

			if (remove)
				iVector.remove();
		}
	}

	public int[][] allDirs()
	{
		if (dimension == 2)
			return new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } };
		else
			throw new IllegalStateException("this method does not support " + dimension + " dimensions yet");
	}

	// geometric checks

	public final boolean isOnSameOrthogonal(int axis, Coordinate c1, Coordinate c2)
	{
		OrthogonalCoordinate oc1 = (OrthogonalCoordinate) c1;
		OrthogonalCoordinate oc2 = (OrthogonalCoordinate) c2;

		return oc1.components[axis] == oc2.components[axis];
	}

	protected final boolean isMinOnAxis(int axis, Coordinate c)
	{
		OrthogonalCoordinate oc = (OrthogonalCoordinate) c;
		return oc.components[axis] == 0;
	}

	protected final boolean isMaxOnAxis(int axis, Coordinate c)
	{
		OrthogonalCoordinate oc = (OrthogonalCoordinate) c;
		return oc.components[axis] == size[axis] - 1;
	}

	// utility

	/**
	 * Tries to visit all nodes by expanding orthogonally. A hook method is invoked on each coordinate, whose return
	 * value determines if it's ok to expand from that coordinate.
	 */
	public void fill(Coordinate coordinate, CoordinateHook hook)
	{
		Queue<Coordinate> q = new LinkedList<Coordinate>();
		q.add(coordinate);

		Set<Coordinate> visited = new HashSet<Coordinate>();

		while ((coordinate = q.poll()) != null)
		{
			// mark as visited
			visited.add(coordinate);

			// call hook
			boolean cont = hook.doNode(coordinate);

			// elaborate on this node?
			if (cont)
			{
				// expand orthogonally
				for (int[] normal : normalVectors())
				{
					for (int[] v : bidirectional(normal))
					{
						Coordinate neighbour = vectorAdd(coordinate, v, 1);

						// beyond edge of board?
						if (neighbour == null)
							continue;

						// has node already been visited?
						if (visited.contains(neighbour))
							continue;

						// queue for checking
						q.add(neighbour);
					}
				}
			}
		}
	}

	/**
	 * Tries to walk from c1 to c2 (both inclusive), taking steps. A hook method is invoked on each coordinate, whose
	 * return value determines if it's ok to continue from that coordinate.
	 * 
	 * @return coordinate on which no continue was requested, null if c2 was reached (and continued)
	 */
	public Coordinate walk(Coordinate c1, Coordinate c2, int[] step, CoordinateHook hook)
	{
		// walk
		for (int factor = 0; true; factor++)
		{
			Coordinate c = vectorAdd(c1, step, factor);
			if (c == null)
				throw new IllegalArgumentException();

			boolean cont = hook.doNode(c);
			if (!cont)
				return c;

			if (c.equals(c2))
				return null;
		}
	}

	/**
	 * Tries to walk from coordinate, taking steps. A hook method is invoked on each step (including the implicit first
	 * step), whose return value determines if it's ok to continue.
	 * 
	 * @return coordinate on which no continue was requested, null if fallen over edge of geometry
	 */
	public Coordinate walk(Coordinate coordinate, int[] step, CoordinateHook hook)
	{
		// walk
		for (int factor = 0; true; factor++)
		{
			Coordinate c = vectorAdd(coordinate, step, factor);
			if (c == null)
				return null;

			boolean cont = hook.doNode(c);
			if (!cont)
				return c;
		}
	}

	public static interface CoordinateHook
	{
		boolean doNode(Coordinate coordinate);
	}

	protected static final class OrthogonalCoordinate extends Coordinate
	{
		final private int[] components;
		private int hashCode = 0;

		private OrthogonalCoordinate(int[] components, String notation)
		{
			super(notation);
			assert components != null : "bla";
			this.components = components;
		}

		@Override
		public final boolean equals(Object obj)
		{
			OrthogonalCoordinate c = (OrthogonalCoordinate) obj;
			return Arrays.equals(this.components, c.components);
		}

		@Override
		public final int hashCode()
		{
			// FIXME possible race condition?
			if (this.hashCode == 0)
				this.hashCode = Arrays.hashCode(components);

			return this.hashCode;
		}
	}
}
