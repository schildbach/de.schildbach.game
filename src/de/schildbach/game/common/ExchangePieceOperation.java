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
public class ExchangePieceOperation implements MicroOperation
{
	private Coordinate source;
	private Coordinate target;

	public ExchangePieceOperation(Coordinate source, Coordinate target)
	{
		assert source != null : "source is null";
		assert target != null : "target is null";

		this.source = source;
		this.target = target;
	}

	public void doOperation(GamePosition position)
	{
		Board board = position.getBoard();
		Piece piece = board.getPiece(target);
		assert piece != null : "no piece on target " + target;
		board.clearPiece(target);
		assert board.getPiece(source) != null : "no piece on source " + source;
		board.movePiece(source, target);
		board.setPiece(source, piece);
	}

	public void undoOperation(GamePosition position)
	{
		doOperation(position);
	}

	@Override
	public String toString()
	{
		return "exchange pieces on " + source + " and " + target;
	}
}
