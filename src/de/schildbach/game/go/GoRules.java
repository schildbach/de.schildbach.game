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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.mutable.MutableInt;

import de.schildbach.game.Board;
import de.schildbach.game.BoardGeometry;
import de.schildbach.game.Coordinate;
import de.schildbach.game.GameMove;
import de.schildbach.game.GamePosition;
import de.schildbach.game.GameRules;
import de.schildbach.game.MicroOperation;
import de.schildbach.game.Piece;
import de.schildbach.game.PieceSet;
import de.schildbach.game.common.BigCoordinateFieldNotations;
import de.schildbach.game.common.NextPlayerOperation;
import de.schildbach.game.common.OrthogonalBoardGeometry;
import de.schildbach.game.common.SingleCoordinateMove;
import de.schildbach.game.common.StoneFenFormat;
import de.schildbach.game.common.piece.StonePieceSet;
import de.schildbach.game.exception.IllegalMoveException;
import de.schildbach.game.exception.ParseException;

/**
 * @author Andreas Schildbach
 */
public class GoRules extends GameRules
{
	public enum Variant
	{
		CAPTURE
	}

	private boolean removeCaptured = true;
	private boolean finishOnCapture = false;

	public static final String PASS_NOTATION = "-";

	public GoRules(Variant variant)
	{
		super(new OrthogonalBoardGeometry(new int[] { 9, 9 }, new BigCoordinateFieldNotations()), new StonePieceSet());

		if (variant == null)
		{
		}
		else if (variant == Variant.CAPTURE)
		{
			removeCaptured = false;
			finishOnCapture = true;
		}
		else
		{
			throw new IllegalArgumentException("unknown variant: " + variant);
		}
	}

	protected boolean getRemoveCaptured()
	{
		return removeCaptured;
	}

	@Override
	public GamePosition initialPositionFromBoard(Board initialBoard)
	{
		if (initialBoard == null)
		{
			initialBoard = getBoardGeometry().newBoard();
			// parseBoard(initialBoard, DEFAULT_INITIAL_BOARD);
		}

		GoPosition position = new GoPosition(initialBoard);

		position.setActivePlayerIndex(0);
		position.setFullmoveNumber(1);

		return position;
	}

	@Override
	public String formatMove(GameMove move)
	{
		SingleCoordinateMove goMove = (SingleCoordinateMove) move;
		if (goMove.isPass())
			return PASS_NOTATION;
		else
			return goMove.getCoordinate().getNotation();
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
		GoPosition goPosition = (GoPosition) position;
		if (goPosition.hasAnythingBeenCaptured())
			throw new ParseException(moveString, "move not allowed, game already finished");

		if (moveString.equals(PASS_NOTATION))
			return SingleCoordinateMove.PASS;

		BoardGeometry geometry = getBoardGeometry();
		Coordinate coordinate = geometry.locateCoordinate(moveString);

		if (goPosition.getBoard().getPiece(coordinate) != null)
			throw new ParseException(moveString, "move not allowed, square not empty");

		// TODO further checks, as rules evolve

		return new SingleCoordinateMove(coordinate);
	}

	@Override
	protected GameMove unmarshalMove(String notation) throws ParseException
	{
		return new SingleCoordinateMove(getBoardGeometry().locateCoordinate(notation));
	}

	@Override
	public Collection<? extends GameMove> allowedMoves(GamePosition gamePosition, Board initialBoard)
	{
		return getAllowedMoves(gamePosition);
	}

	private Collection<? extends GameMove> getAllowedMoves(GamePosition position)
	{
		GoPosition goPosition = (GoPosition) position;
		Board board = position.getBoard();

		Set<SingleCoordinateMove> allowedMoves = new HashSet<SingleCoordinateMove>();

		// is game already finished?
		if (!finishOnCapture || !goPosition.hasAnythingBeenCaptured())
		{
			for (Coordinate coordinate : board.locateEmptyFields())
			{
				SingleCoordinateMove move = new SingleCoordinateMove(coordinate);
				allowedMoves.add(move);
			}

			allowedMoves.add(SingleCoordinateMove.PASS);
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
		GoPosition goPosition = (GoPosition) position;
		StringBuilder str = new StringBuilder();
		str.append(formatBoard(position.getBoard()));
		for (int count : goPosition.getCaptureCount())
			str.append(" ").append(count);
		return str.toString();
	}

	@Override
	public GamePosition parsePosition(String notation)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isFinished(GamePosition position, Board initialBoard)
	{
		return isDraw(position) || isWin(position);
	}

	@Override
	public float[] points(GamePosition position, Board initialBoard)
	{
		int[] captureCount = ((GoPosition) position).getCaptureCount();
		int size = captureCount.length;
		if (size != 2)
			throw new IllegalStateException("can't handle other sizes than 2");
		float[] points = new float[size];
		for (int i = 0; i < size; i++)
			points[1 - i] = captureCount[i];
		return points;
	}

	private boolean isDraw(GamePosition position)
	{
		Collection<? extends GameMove> allowedMoves = getAllowedMoves(position);
		return allowedMoves.size() == 1 && allowedMoves.iterator().next() == SingleCoordinateMove.PASS;
	}

	private boolean isWin(GamePosition position)
	{
		GoPosition goPosition = (GoPosition) position;
		return finishOnCapture && goPosition.hasAnythingBeenCaptured();
	}

	@Override
	public List<MicroOperation> disassembleMove(GameMove move, GamePosition position, Board initialBoard) throws IllegalMoveException
	{
		SingleCoordinateMove goMove = (SingleCoordinateMove) move;

		LinkedList<MicroOperation> ops = new LinkedList<MicroOperation>();

		// move
		if (!goMove.isPass())
			ops.add(new GoMoveOperation(goMove.getCoordinate()));

		// next player
		ops.add(NextPlayerOperation.instance());

		return ops;
	}

	private class GoMoveOperation implements MicroOperation
	{
		private Coordinate coordinate;

		// needed for undo
		private Map<Coordinate, Piece> captured = new HashMap<Coordinate, Piece>();

		public GoMoveOperation(Coordinate coordinate)
		{
			assert coordinate != null : "coordinate is null";

			this.coordinate = coordinate;
		}

		public void doOperation(GamePosition position)
		{
			GoPosition goPosition = (GoPosition) position;
			Board board = position.getBoard();
			OrthogonalBoardGeometry geometry = (OrthogonalBoardGeometry) getBoardGeometry();
			PieceSet pieceSet = getPieceSet();
			int activeIndex = position.getActivePlayerIndex();

			// set piece
			board.setPiece(coordinate, pieceSet.getPiece(activeIndex == 0 ? "b" : "w"));

			// capture
			for (int[] normal : geometry.normalVectors())
			{
				for (int[] v : geometry.bidirectional(normal))
				{
					Coordinate neighbour = geometry.vectorAdd(coordinate, v, 1);
					if (neighbour != null)
					{
						Piece piece = board.getPiece(neighbour);
						if (piece != null)
						{
							int color = piece.getColor();
							if (color != activeIndex && !checkFreedom(geometry, board, neighbour))
							{
								int count = capture(board, neighbour);
								goPosition.addCaptureCount(color, count);
							}
						}
					}
				}
			}

			// suicide
			if (!checkFreedom(geometry, board, coordinate))
			{
				int count = capture(board, coordinate);
				goPosition.addCaptureCount(activeIndex, count);
			}
		}

		private boolean checkFreedom(final OrthogonalBoardGeometry geometry, final Board board, Coordinate coordinate)
		{
			Piece piece = board.getPiece(coordinate);
			if (piece == null)
				throw new IllegalArgumentException();
			final int color = piece.getColor();

			final MutableBoolean free = new MutableBoolean(false);
			geometry.fill(coordinate, new OrthogonalBoardGeometry.CoordinateHook()
			{
				public boolean doNode(Coordinate coordinate)
				{
					Piece piece = board.getPiece(coordinate);
					if (piece == null)
					{
						free.setValue(true);
						return false;
					}
					else
					{
						return piece.getColor() == color;
					}
				}
			});
			return free.getValue();
		}

		private int capture(final Board board, Coordinate coordinate)
		{
			final OrthogonalBoardGeometry geometry = (OrthogonalBoardGeometry) getBoardGeometry();

			Piece piece = board.getPiece(coordinate);
			if (piece == null)
				throw new IllegalArgumentException();
			final int color = piece.getColor();

			final MutableInt numberCaptured = new MutableInt();

			geometry.fill(coordinate, new OrthogonalBoardGeometry.CoordinateHook()
			{
				public boolean doNode(Coordinate coordinate)
				{
					Piece piece = board.getPiece(coordinate);
					if (piece.getColor() == color)
					{
						if (getRemoveCaptured())
						{
							captured.put(coordinate, piece);
							board.clearPiece(coordinate);
						}
						numberCaptured.setValue(numberCaptured.intValue() + 1);
						return true;
					}
					else
					{
						return false;
					}
				}
			});

			return numberCaptured.intValue();
		}

		public void undoOperation(GamePosition position)
		{
			GoPosition goPosition = (GoPosition) position;
			Board board = position.getBoard();

			// put back captured pieces
			for (Map.Entry<Coordinate, Piece> entry : captured.entrySet())
			{
				Piece piece = entry.getValue();
				board.setPiece(entry.getKey(), piece);
				goPosition.addCaptureCount(piece.getColor(), -1);
			}

			// remove piece
			board.clearPiece(coordinate);

			// clean up undo data
			captured.clear();
		}
	}

	private static class MutableBoolean
	{
		private boolean value;

		public MutableBoolean(boolean value)
		{
			setValue(value);
		}

		public void setValue(boolean value)
		{
			this.value = value;
		}

		public boolean getValue()
		{
			return value;
		}
	}
}
