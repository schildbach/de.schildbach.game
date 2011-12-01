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

package de.schildbach.game.go;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.schildbach.game.Board;
import de.schildbach.game.common.BigCoordinateFieldNotations;
import de.schildbach.game.common.OrthogonalBoardGeometry;
import de.schildbach.game.common.StoneFenFormat;
import de.schildbach.game.common.piece.StonePieceSet;

/**
 * @author Andreas Schildbach
 */
public class StoneFenFormatTest
{
	@Test
	public void test()
	{
		OrthogonalBoardGeometry geometry = new OrthogonalBoardGeometry(new int[] { 9, 9 }, new BigCoordinateFieldNotations());
		StonePieceSet pieceSet = new StonePieceSet();
		Board board = geometry.newBoard();

		String fen = "9/9/9/4b4/3b1b3/4b4/9/9/9";
		StoneFenFormat.parse(geometry, pieceSet, fen, board);
		assertEquals(fen, StoneFenFormat.format(geometry, pieceSet, board));

		fen = "5bbbb/9/4w4/3w1w3/2w2w3/3ww4/9/9/9";
		StoneFenFormat.parse(geometry, pieceSet, fen, board);
		assertEquals(fen, StoneFenFormat.format(geometry, pieceSet, board));
	}
}
