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

import de.schildbach.game.GamePosition;
import de.schildbach.game.MicroOperation;
import de.schildbach.game.Piece;

/**
 * @author Andreas Schildbach
 */
public class ForfeitAntiKingCastlingOperation implements MicroOperation
{
	private int color;
	private Class<? extends Piece> piece;

	public ForfeitAntiKingCastlingOperation(int color, Class<? extends Piece> piece)
	{
		assert piece != null : "piece is null";

		this.color = color;
		this.piece = piece;
	}

	public void doOperation(GamePosition position)
	{
		AntiKingChessPosition chessPosition = (AntiKingChessPosition) position;
		assert chessPosition.getCastlingAvailable(color, piece) : piece + " castling is already not available";
		chessPosition.setCastlingAvailable(color, piece, false);
	}

	public void undoOperation(GamePosition position)
	{
		AntiKingChessPosition chessPosition = (AntiKingChessPosition) position;
		assert !chessPosition.getCastlingAvailable(color, piece);
		chessPosition.setCastlingAvailable(color, piece, true);
	}

	@Override
	public String toString()
	{
		return "forfeit " + piece + " castling for color " + color;
	}
}
