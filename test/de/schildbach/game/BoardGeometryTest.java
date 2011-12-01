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

package de.schildbach.game;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;

import de.schildbach.game.chess.ChessRules;
import de.schildbach.game.common.FenFormat;
import de.schildbach.game.common.OrthogonalBoardGeometry;

/**
 * @author Andreas Schildbach
 */
public class BoardGeometryTest extends AbstractGameRulesTest
{
	@Override
	protected GameRules gameRules()
	{
		return new ChessRules(null);
	}

	@Test
	public void boardIterator()
	{
		int count = 0;
		for (Iterator<Coordinate> i = geometry.coordinateIterator(); i.hasNext();)
		{
			i.next();
			count++;
		}
		assertEquals(64, count);
	}

	@Test
	public void pieceIteratorColor()
	{
		String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
		Board board = geometry.newBoard();
		FenFormat.parse((OrthogonalBoardGeometry) geometry, pieceSet, fen, board);
		int count = 0;
		for (Iterator<Coordinate> i = geometry.pieceIterator(board, 1); i.hasNext();)
		{
			i.next();
			count++;
		}
		assertEquals(16, count);
		for (Iterator<Coordinate> i = geometry.pieceIterator(board, 0); i.hasNext();)
		{
			i.next();
			count++;
		}
		assertEquals(32, count);
	}
}
