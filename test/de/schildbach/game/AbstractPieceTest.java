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

package de.schildbach.game;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.schildbach.game.common.ChessBoardLikeGeometry;
import de.schildbach.game.common.piece.ChessLikePiece;

/**
 * @author Andreas Schildbach
 */
public abstract class AbstractPieceTest
{
	protected ChessBoardLikeGeometry geometry;
	protected ChessLikePiece piece;

	protected void assertPotentialTargets(Board board, String source, String[] expectedTargets)
	{
		Set<Coordinate> targets = piece.getPotentialTargets(geometry, board, geometry.locateCoordinate(source));
		assertEquals(coordinates(geometry, expectedTargets), targets);
	}

	protected void assertIsThreateningSquare(Board board, String source, String[] expectedTargets)
	{
		Coordinate sourceC = geometry.locateCoordinate(source);
		Set<Coordinate> expectedTargetsC = coordinates(geometry, expectedTargets);
		for (Iterator<Coordinate> i = geometry.coordinateIterator(); i.hasNext();)
		{
			Coordinate coordinate = i.next();
			boolean isThreatening = piece.isThreateningSquare(geometry, board, sourceC, coordinate);
			String message = source + "-" + coordinate.getNotation();
			assertEquals(message, expectedTargetsC.contains(coordinate), isThreatening);
		}
	}

	private Set<Coordinate> coordinates(BoardGeometry geometry, String[] notations)
	{
		Set<Coordinate> coordinates = new HashSet<Coordinate>();
		for (String notation : notations)
			coordinates.add(geometry.locateCoordinate(notation));
		return coordinates;
	}

	protected Board board(BoardGeometry geometry, String[] populated, Piece population)
	{
		Board board = geometry.newBoard();
		for (String field : populated)
			board.setPiece(geometry.locateCoordinate(field), population);
		return board;
	}

}