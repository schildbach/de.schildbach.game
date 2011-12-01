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

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.junit.Before;

/**
 * @author Andreas Schildbach
 */
public abstract class AbstractGameRulesTest
{
	protected GameRules rules;
	protected BoardGeometry geometry;
	protected PieceSet pieceSet;
	protected GamePosition defaultInitialPosition;

	@Before
	public final void setupChess()
	{
		rules = gameRules();
		geometry = rules.getBoardGeometry();
		pieceSet = rules.getPieceSet();
		defaultInitialPosition = rules.initialPositionFromBoard(null);
	}

	protected abstract GameRules gameRules();

	protected final Coordinate coordinate(String coordinate)
	{
		return geometry.locateCoordinate(coordinate);
	}

	protected final GamePosition defaultInitialPosition()
	{
		return defaultInitialPosition;
	}

	protected final Board defaultInitialBoard()
	{
		return defaultInitialPosition().getBoard();
	}

	protected final GameMove move(String move, GamePosition position, Board initialBoard)
	{
		if (initialBoard == null)
			initialBoard = defaultInitialBoard();
		return rules.parseMove(move, Locale.ENGLISH, position, initialBoard);
	}

	protected final Collection<? extends GameMove> allowedMoves(GamePosition position, Board initialBoard)
	{
		if (initialBoard == null)
			initialBoard = defaultInitialBoard();
		return rules.allowedMoves(position, initialBoard);
	}

	protected final Game game(String initialBoard, String notation)
	{
		return rules.newGame(initialBoard, notation, Locale.ENGLISH);
	}

	protected final Game game(String initialBoard)
	{
		return rules.newGame(initialBoard);
	}

	protected final List<MicroOperation> disassemble(GameMove move, GamePosition position, Board initialBoard)
	{
		if (initialBoard == null)
			initialBoard = defaultInitialBoard();
		return rules.disassembleMove(move, position, initialBoard);
	}

	protected final void doOperations(List<MicroOperation> ops, GamePosition position)
	{
		rules.doOperations(ops, position);
	}

	protected final void undoOperations(List<MicroOperation> ops, GamePosition position)
	{
		rules.undoOperations(ops, position);
	}

	protected final void doMove(GameMove move, GamePosition position, Board initialBoard)
	{
		doOperations(disassemble(move, position, initialBoard), position);
	}

	protected final void executeMove(Game game, GameMove move)
	{
		rules.executeMove(game, move);
	}

	protected final GamePosition position(String position)
	{
		return rules.parsePosition(position);
	}

	protected final String format(GamePosition position)
	{
		return rules.formatPosition(position);
	}
}
