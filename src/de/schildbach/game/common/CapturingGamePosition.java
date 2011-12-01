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

import java.util.LinkedList;
import java.util.List;

import de.schildbach.game.Board;
import de.schildbach.game.GamePosition;
import de.schildbach.game.Piece;
import de.schildbach.game.PieceCapturing;

/**
 * @author Andreas Schildbach
 */
public class CapturingGamePosition extends GamePosition implements PieceCapturing
{
	private List<List<Piece>> capturedPieces = new LinkedList<List<Piece>>();

	public CapturingGamePosition(Board board)
	{
		super(board);

		for (int i = 0; i < 2; i++)
			capturedPieces.add(new LinkedList<Piece>());
	}

	public void addCapturedPiece(int playerIndex, Piece piece)
	{
		capturedPieces.get(playerIndex).add(piece);
	}

	public Piece removeLastCapturedPiece(int playerIndex)
	{
		List<Piece> cp = capturedPieces.get(playerIndex);
		return cp.remove(cp.size() - 1);
	}

	public Piece[] getCapturedPieces(int playerIndex)
	{
		return capturedPieces.get(playerIndex).toArray(new Piece[] {});
	}

	public Piece[][] getCapturedPiecesByPlayer()
	{
		Piece[][] result = new Piece[capturedPieces.size()][];
		for (int i = 0; i < capturedPieces.size(); i++)
			result[i] = getCapturedPieces(i);
		return result;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;

		if (!super.equals(o))
			return false;

		CapturingGamePosition other = (CapturingGamePosition) o;

		if (!this.capturedPieces.equals(other.capturedPieces))
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int hashCode = super.hashCode();
		hashCode *= 37;
		hashCode += capturedPieces.hashCode();
		return hashCode;
	}

	@Override
	public Object clone()
	{
		CapturingGamePosition clone = (CapturingGamePosition) super.clone();
		clone.capturedPieces = new LinkedList<List<Piece>>(this.capturedPieces);
		for (int i = 0; i < capturedPieces.size(); i++)
			clone.capturedPieces.set(i, new LinkedList<Piece>(this.capturedPieces.get(i)));
		return clone;
	}
}
