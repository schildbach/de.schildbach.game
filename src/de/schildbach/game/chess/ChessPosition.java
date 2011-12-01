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

import org.apache.commons.lang.ObjectUtils;

import de.schildbach.game.Board;
import de.schildbach.game.Coordinate;

/**
 * @author Andreas Schildbach
 */
public class ChessPosition extends ChessLikePosition
{
	private boolean castlingAvailableWhite[];
	private boolean castlingAvailableBlack[];
	private Coordinate enPassantTargetSquare;

	public ChessPosition(Board board)
	{
		super(board);
		castlingAvailableWhite = new boolean[2];
		castlingAvailableBlack = new boolean[2];
	}

	public Coordinate getEnPassantTargetSquare()
	{
		return enPassantTargetSquare;
	}

	public void setEnPassantTargetSquare(Coordinate enPassantTargetSquare)
	{
		this.enPassantTargetSquare = enPassantTargetSquare;
	}

	public boolean getCastlingAvailable(int color, CastlingSide side)
	{
		int i = side == CastlingSide.QUEENSIDE ? 0 : 1;
		return color == 0 ? castlingAvailableWhite[i] : castlingAvailableBlack[i];
	}

	public void setCastlingAvailable(int color, CastlingSide side, boolean castlingAvailable)
	{
		int i = side == CastlingSide.QUEENSIDE ? 0 : 1;
		if (color == 0)
			this.castlingAvailableWhite[i] = castlingAvailable;
		else
			this.castlingAvailableBlack[i] = castlingAvailable;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;

		if (!super.equals(o))
			return false;

		ChessPosition other = (ChessPosition) o;

		if (!Arrays.equals(castlingAvailableWhite, other.castlingAvailableWhite))
			return false;

		if (!Arrays.equals(castlingAvailableBlack, other.castlingAvailableBlack))
			return false;

		if (!ObjectUtils.equals(this.enPassantTargetSquare, other.enPassantTargetSquare))
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int hashCode = super.hashCode();
		hashCode *= 37;
		hashCode += Arrays.hashCode(castlingAvailableWhite);
		hashCode *= 37;
		hashCode += Arrays.hashCode(castlingAvailableBlack);
		hashCode *= 37;
		hashCode += ObjectUtils.hashCode(enPassantTargetSquare);
		return hashCode;
	}

	@Override
	public Object clone()
	{
		ChessPosition clone = (ChessPosition) super.clone();
		clone.castlingAvailableWhite = (boolean[]) this.castlingAvailableWhite.clone();
		clone.castlingAvailableBlack = (boolean[]) this.castlingAvailableBlack.clone();
		return clone;
	}
}
