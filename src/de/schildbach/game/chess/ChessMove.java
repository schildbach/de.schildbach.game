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

import org.apache.commons.lang.ObjectUtils;

import de.schildbach.game.Coordinate;
import de.schildbach.game.Piece;
import de.schildbach.game.common.ChessLikeMove;

/**
 * @author Andreas Schildbach
 */
public class ChessMove extends ChessLikeMove
{
	private Class<? extends Piece> promotionPiece = null;

	/**
	 * Main constructor.
	 */
	public ChessMove(Coordinate source, Coordinate target)
	{
		super(source, target);
	}

	/**
	 * Constructor for promotions.
	 */
	public ChessMove(Coordinate source, Coordinate target, Class<? extends Piece> promotionPiece)
	{
		this(source, target);
		assert promotionPiece != null : "promotionPiece is null";
		this.promotionPiece = promotionPiece;
	}

	/**
	 * Default constructor, needed for serialization.
	 */
	protected ChessMove()
	{
	}

	public Class<? extends Piece> getPromotionPiece()
	{
		return promotionPiece;
	}

	@Override
	public boolean equals(Object o)
	{
		ChessMove other = (ChessMove) o;
		if (!super.equals(other))
			return false;
		if (!ObjectUtils.equals(this.promotionPiece, other.promotionPiece))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int hashCode = super.hashCode();
		hashCode *= 37;
		hashCode += ObjectUtils.hashCode(promotionPiece);
		return hashCode;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder(super.toString());
		if (promotionPiece != null)
		{
			builder.append("(");
			builder.append(promotionPiece);
			builder.append(")");
		}
		return builder.toString();
	}
}
