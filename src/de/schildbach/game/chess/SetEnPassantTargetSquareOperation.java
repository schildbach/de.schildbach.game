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

import de.schildbach.game.Coordinate;
import de.schildbach.game.GamePosition;
import de.schildbach.game.MicroOperation;

/**
 * @author Andreas Schildbach
 */
public class SetEnPassantTargetSquareOperation implements MicroOperation
{
	private Coordinate coordinate;
	private Coordinate undoCoordinate;

	public SetEnPassantTargetSquareOperation(Coordinate coordinate)
	{
		this.coordinate = coordinate;
	}

	public void doOperation(GamePosition position)
	{
		ChessPosition chessPosition = (ChessPosition) position;
		assert undoCoordinate == null;
		undoCoordinate = chessPosition.getEnPassantTargetSquare();
		chessPosition.setEnPassantTargetSquare(coordinate);
	}

	public void undoOperation(GamePosition position)
	{
		ChessPosition chessPosition = (ChessPosition) position;
		chessPosition.setEnPassantTargetSquare(undoCoordinate);
		undoCoordinate = null;
	}

	@Override
	public String toString()
	{
		return "set en passant target square to " + coordinate;
	}
}
