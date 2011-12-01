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

import java.util.Arrays;

import de.schildbach.game.Board;
import de.schildbach.game.Piece;
import de.schildbach.game.chess.piece.AntiKing;
import de.schildbach.game.chess.piece.King;

/**
 * @author Andreas Schildbach
 */
public class AntiKingChessPosition extends ChessLikePosition
{
	private boolean kingCastlingAvailable[];
	private boolean antiKingCastlingAvailable[];

	public AntiKingChessPosition(Board board)
	{
		super(board);
		kingCastlingAvailable = new boolean[2];
		antiKingCastlingAvailable = new boolean[2];
	}

	public boolean getCastlingAvailable(int color, Class<? extends Piece> piece)
	{
		if (piece.equals(King.class))
			return kingCastlingAvailable[color];
		else if (piece.equals(AntiKing.class))
			return antiKingCastlingAvailable[color];
		else
			throw new IllegalArgumentException("no king piece: " + piece);
	}

	public void setCastlingAvailable(int color, Class<? extends Piece> piece, boolean castlingAvailable)
	{
		if (piece.equals(King.class))
			kingCastlingAvailable[color] = castlingAvailable;
		else if (piece.equals(AntiKing.class))
			antiKingCastlingAvailable[color] = castlingAvailable;
		else
			throw new IllegalArgumentException("no king piece: " + piece);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;

		if (!super.equals(o))
			return false;

		AntiKingChessPosition other = (AntiKingChessPosition) o;

		if (!Arrays.equals(this.kingCastlingAvailable, other.kingCastlingAvailable))
			return false;

		if (!Arrays.equals(this.antiKingCastlingAvailable, other.antiKingCastlingAvailable))
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int hashCode = super.hashCode();
		hashCode *= 37;
		hashCode += Arrays.hashCode(kingCastlingAvailable);
		hashCode *= 37;
		hashCode += Arrays.hashCode(antiKingCastlingAvailable);
		return hashCode;
	}

	@Override
	public Object clone()
	{
		AntiKingChessPosition clone = (AntiKingChessPosition) super.clone();
		clone.kingCastlingAvailable = (boolean[]) this.kingCastlingAvailable.clone();
		clone.antiKingCastlingAvailable = (boolean[]) this.antiKingCastlingAvailable.clone();
		return clone;
	}
}
