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

import de.schildbach.game.Board;
import de.schildbach.game.Coordinate;
import de.schildbach.game.GamePosition;
import de.schildbach.game.MicroOperation;
import de.schildbach.game.Piece;

/**
 * @author Andreas Schildbach
 */
public class CapturePieceOperation implements MicroOperation
{
	private Coordinate coordinate;

	public CapturePieceOperation(Coordinate coordinate)
	{
		assert coordinate != null : "coordinate is null";

		this.coordinate = coordinate;
	}

	public void doOperation(GamePosition position)
	{
		Board board = position.getBoard();
		Piece piece = board.getPiece(coordinate);
		assert piece != null;

		((CapturingGamePosition) position).addCapturedPiece(position.getActivePlayerIndex(), piece);

		board.clearPiece(coordinate);
	}

	public void undoOperation(GamePosition position)
	{
		Piece piece = ((CapturingGamePosition) position).removeLastCapturedPiece(position.getActivePlayerIndex());
		assert piece != null;

		Board board = position.getBoard();
		board.setPiece(coordinate, piece);
	}

	@Override
	public String toString()
	{
		return "capture piece on " + coordinate;
	}
}
