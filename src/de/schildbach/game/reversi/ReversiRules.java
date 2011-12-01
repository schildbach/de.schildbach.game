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

package de.schildbach.game.reversi;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.schildbach.game.Board;
import de.schildbach.game.BoardGeometry;
import de.schildbach.game.Coordinate;
import de.schildbach.game.GameMove;
import de.schildbach.game.GamePosition;
import de.schildbach.game.GameRules;
import de.schildbach.game.MicroOperation;
import de.schildbach.game.Piece;
import de.schildbach.game.common.CoordinateFieldNotations;
import de.schildbach.game.common.NextPlayerOperation;
import de.schildbach.game.common.OrthogonalBoardGeometry;
import de.schildbach.game.common.SetPieceOperation;
import de.schildbach.game.common.SingleCoordinateMove;
import de.schildbach.game.common.StoneFenFormat;
import de.schildbach.game.common.SwapOutPieceOperation;
import de.schildbach.game.common.piece.Stone;
import de.schildbach.game.common.piece.StonePieceSet;
import de.schildbach.game.exception.IllegalMoveException;
import de.schildbach.game.exception.ParseException;

/**
 * @author Andreas Schildbach
 */
public class ReversiRules extends GameRules
{
	public static final String PASS_NOTATION = "-";

	private static final String DEFAULT_INITIAL_BOARD = "8/8/8/3wb3/3bw3/8/8/8";

	public ReversiRules()
	{
		super(new OrthogonalBoardGeometry(new int[] { 8, 8 }, new CoordinateFieldNotations('a', '8')), new StonePieceSet());
	}

	@Override
	public GamePosition initialPositionFromBoard(Board initialBoard)
	{
		if (initialBoard == null)
		{
			initialBoard = getBoardGeometry().newBoard();
			parseBoard(initialBoard, DEFAULT_INITIAL_BOARD);
		}

		ReversiPosition position = new ReversiPosition(initialBoard);

		position.setActivePlayerIndex(0);
		position.setFullmoveNumber(1);

		return position;
	}

	@Override
	public String formatMove(GameMove move)
	{
		SingleCoordinateMove reversiMove = (SingleCoordinateMove) move;
		if (reversiMove.isPass())
			return PASS_NOTATION;
		else
			return reversiMove.getCoordinate().getNotation();
	}

	@Override
	public Set<String> clickables(GameMove move)
	{
		Set<String> clickables = new HashSet<String>();
		clickables.add(formatMove(move));
		return clickables;
	}

	@Override
	public GameMove parseMove(String moveString, Locale locale, GamePosition position, Board initialBoard)
	{
		ReversiPosition reversiPosition = (ReversiPosition) position;

		if (moveString.equals(PASS_NOTATION))
			return SingleCoordinateMove.PASS;

		BoardGeometry geometry = getBoardGeometry();
		Coordinate coordinate = geometry.locateCoordinate(moveString);

		if (reversiPosition.getBoard().getPiece(coordinate) != null)
			throw new ParseException(moveString, "move not allowed, square not empty");

		// TODO further checks, as rules evolve

		return new SingleCoordinateMove(coordinate);
	}

	@Override
	public void parseBoard(Board board, String notation)
	{
		StoneFenFormat.parse((OrthogonalBoardGeometry) getBoardGeometry(), getPieceSet(), notation, board);
	}

	@Override
	protected GameMove unmarshalMove(String notation) throws ParseException
	{
		return new SingleCoordinateMove(getBoardGeometry().locateCoordinate(notation));
	}

	@Override
	public Collection<? extends GameMove> allowedMoves(GamePosition position, Board initialBoard)
	{
		Board board = position.getBoard();
		int activeIndex = position.getActivePlayerIndex();

		Collection<SingleCoordinateMove> allowedMoves = getAllowedMoves(board, activeIndex);

		// add pass
		if (allowedMoves.isEmpty())
			allowedMoves.add(SingleCoordinateMove.PASS);

		return allowedMoves;
	}

	private Collection<SingleCoordinateMove> getAllowedMoves(Board board, int activeIndex)
	{
		OrthogonalBoardGeometry geometry = (OrthogonalBoardGeometry) getBoardGeometry();

		Set<SingleCoordinateMove> allowedMoves = new HashSet<SingleCoordinateMove>();

		board: for (Coordinate coordinate : board.locateEmptyFields())
		{
			n: for (int[] v : geometry.allDirs())
			{
				for (int i = 1;; i++)
				{
					Coordinate c = geometry.vectorAdd(coordinate, v, i);
					if (c == null)
						continue n;

					Piece piece = board.getPiece(c);
					if (piece == null)
						continue n;

					if (i == 1 && piece.getColor() == activeIndex)
						continue n;

					if (i > 1 && piece.getColor() == activeIndex)
					{
						SingleCoordinateMove move = new SingleCoordinateMove(coordinate);
						allowedMoves.add(move);
						continue board;
					}
				}
			}
		}

		return allowedMoves;
	}

	@Override
	public final String formatBoard(Board board)
	{
		return StoneFenFormat.format((OrthogonalBoardGeometry) getBoardGeometry(), (StonePieceSet) getPieceSet(), board);
	}

	@Override
	public String formatPosition(GamePosition position)
	{
		return formatBoard(position.getBoard());
	}

	@Override
	public GamePosition parsePosition(String notation)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isFinished(GamePosition position, Board initialBoard)
	{
		return isFinished(position.getBoard());
	}

	private boolean isFinished(Board board)
	{
		for (int color = 0; color < 2; color++)
			if (!getAllowedMoves(board, color).isEmpty())
				return false;

		return true;
	}

	@Override
	public float[] points(GamePosition position, Board initialBoard)
	{
		return points(position.getBoard());
	}

	private float[] points(Board board)
	{
		float[] points = new float[2];
		for (Coordinate c : board.locateOccupiedFields())
		{
			Piece piece = board.getPiece(c);
			points[piece.getColor()] += 1;
		}

		return points;
	}

	@Override
	public List<MicroOperation> disassembleMove(GameMove move, GamePosition position, Board initialBoard) throws IllegalMoveException
	{
		SingleCoordinateMove reversiMove = (SingleCoordinateMove) move;

		LinkedList<MicroOperation> ops = new LinkedList<MicroOperation>();

		// move
		if (!reversiMove.isPass())
		{
			Coordinate coordinate = reversiMove.getCoordinate();
			Board board = position.getBoard();
			OrthogonalBoardGeometry geometry = (OrthogonalBoardGeometry) getBoardGeometry();
			int activeIndex = position.getActivePlayerIndex();

			ops.add(new SetPieceOperation(getPieceSet().getPiece(Stone.class, activeIndex), coordinate));

			n: for (int[] v : geometry.allDirs())
			{
				Set<Coordinate> capturedCoordinates = new HashSet<Coordinate>();

				for (int i = 1;; i++)
				{
					Coordinate c = geometry.vectorAdd(coordinate, v, i);
					if (c == null)
						continue n;

					Piece piece = board.getPiece(c);
					if (piece == null)
						continue n;

					if (piece.getColor() != activeIndex)
					{
						capturedCoordinates.add(c);
					}
					else
					{
						for (Coordinate capt : capturedCoordinates)
							ops.add(new SwapOutPieceOperation(capt, getPieceSet().getPiece(Stone.class, activeIndex)));
						continue n;
					}
				}
			}
		}

		// next player
		ops.add(NextPlayerOperation.instance());

		return ops;
	}
}
