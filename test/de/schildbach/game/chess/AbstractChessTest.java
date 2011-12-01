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

import de.schildbach.game.AbstractGameRulesTest;
import de.schildbach.game.Coordinate;
import de.schildbach.game.GameRules;
import de.schildbach.game.Piece;

/**
 * @author Andreas Schildbach
 */
public abstract class AbstractChessTest extends AbstractGameRulesTest
{
	private static final ChessRules CHESS_RULES = new ChessRules(null);

	@Override
	protected GameRules gameRules()
	{
		return CHESS_RULES;
	}

	protected final ChessMove move(String source, String target)
	{
		Coordinate s = coordinate(source);
		Coordinate t = coordinate(target);
		return new ChessMove(s, t);
	}

	protected final ChessMove move(String source, String target, Class<? extends Piece> promotionPiece)
	{
		Coordinate s = coordinate(source);
		Coordinate t = coordinate(target);
		return new ChessMove(s, t, promotionPiece);
	}
}
