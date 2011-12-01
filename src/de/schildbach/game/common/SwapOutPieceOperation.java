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
public class SwapOutPieceOperation implements MicroOperation
{
	private Coordinate coordinate;
	private Piece piece;
	private Piece undoPiece;

	public SwapOutPieceOperation(Coordinate coordinate, Piece piece)
	{
		assert coordinate != null : "coordinate is null";
		assert piece != null : "piece is null";

		this.coordinate = coordinate;
		this.piece = piece;
	}

	public void doOperation(GamePosition position)
	{
		Board board = position.getBoard();

		assert undoPiece == null;
		undoPiece = board.getPiece(coordinate);
		assert undoPiece != null : "no piece to swap out on " + coordinate;
		board.clearPiece(coordinate);
		board.setPiece(coordinate, piece);
	}

	public void undoOperation(GamePosition position)
	{
		Board board = position.getBoard();
		assert board.getPiece(coordinate).equals(piece);
		board.clearPiece(coordinate);
		board.setPiece(coordinate, undoPiece);
		undoPiece = null;
	}

	@Override
	public String toString()
	{
		return "swap out piece piece on " + coordinate + " by " + piece;
	}
}
