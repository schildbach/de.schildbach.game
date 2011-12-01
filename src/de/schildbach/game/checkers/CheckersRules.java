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

package de.schildbach.game.checkers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
import de.schildbach.game.PieceSet.ColorEntry;
import de.schildbach.game.checkers.piece.CheckersPiece;
import de.schildbach.game.checkers.piece.King;
import de.schildbach.game.checkers.piece.Man;
import de.schildbach.game.common.CapturePieceOperation;
import de.schildbach.game.common.CapturingGamePosition;
import de.schildbach.game.common.ChessBoardLikeGeometry;
import de.schildbach.game.common.GenericPieceSet;
import de.schildbach.game.common.MovePieceOperation;
import de.schildbach.game.common.NextPlayerOperation;
import de.schildbach.game.common.SwapOutPieceOperation;
import de.schildbach.game.common.GenericPieceSet.PieceEntry;
import de.schildbach.game.exception.IllegalMoveException;
import de.schildbach.game.exception.ParseException;

/**
 * @author Andreas Schildbach
 */
public class CheckersRules extends GameRules
{
	public enum Variant
	{
		SUICIDE
	}

	private static final String DEFAULT_INITIAL_BOARD = "mmmmm/mmmmm/mmmmm/mmmmm/5/5/MMMMM/MMMMM/MMMMM/MMMMM";

	private static final PieceEntry[] PIECES = { new PieceEntry('M', 'M', Man.class), //
			new PieceEntry('K', 'K', King.class) //
	};

	private static final ColorEntry[] COLORS = { new ColorEntry(0, "white", 'w'), //
			new ColorEntry(1, "black", 'b') //
	};

	private boolean suicide = false;
	private boolean quantityRule = true;

	private static final int BOARD_SIZE = 10;

	public CheckersRules(Variant variant)
	{
		super(CheckersBoardGeometry.instance(BOARD_SIZE), new GenericPieceSet(PIECES, COLORS));

		if (variant == null)
		{
			suicide = false;
		}
		else if (variant == Variant.SUICIDE)
		{
			suicide = true;
		}
		else
		{
			throw new IllegalArgumentException("unknown variant: " + variant);
		}
	}

	@Override
	public GamePosition initialPositionFromBoard(Board initialBoard)
	{
		if (initialBoard == null)
		{
			initialBoard = getBoardGeometry().newBoard();
			parseBoard(initialBoard, DEFAULT_INITIAL_BOARD);
		}

		CapturingGamePosition position = new CapturingGamePosition(initialBoard);

		position.setActivePlayerIndex(0);
		position.setFullmoveNumber(1);

		return position;
	}

	@Override
	public GameMove parseMove(String moveString, Locale locale, GamePosition position, Board initialBoard) throws ParseException
	{
		BoardGeometry geometry = getBoardGeometry();

		// TODO diese routine wird probleme bekommen wenn es mal keinen
		// schlagzwang gibt

		Collection<CheckersMove> allowedMoves = allowedMoves(position, initialBoard);

		String[] moveParts = moveString.split("[-x]");
		for (String part : moveParts)
		{
			Coordinate movePart = geometry.locateCoordinate(part);

			// iterate all allowed moves and throw away all moves that do not
			// contain the part
			for (Iterator<CheckersMove> iMoves = allowedMoves.iterator(); iMoves.hasNext();)
			{
				CheckersMove move = iMoves.next();
				List<Coordinate> points = move.getPoints();
				if (!points.contains(movePart))
					iMoves.remove();
			}
		}

		// there is more than one move left (not distinct)
		if (allowedMoves.size() > 1)
			throw new ParseException(moveString, "no distinct move");

		// there is no move left
		if (allowedMoves.size() == 0)
			throw new ParseException(moveString, "no move");

		// return the one move that is left
		return allowedMoves.iterator().next();
	}

	@Override
	protected GameMove unmarshalMove(String notation) throws ParseException
	{
		BoardGeometry geometry = getBoardGeometry();
		CheckersMove move = new CheckersMove();

		for (String part : notation.split("-"))
			move.addTarget(geometry.locateCoordinate(part));

		return move;
	}

	private void internalAppendCapture(CheckersPiece piece, Board board, CheckersMove move, Coordinate newTarget, List<Coordinate> captureChain,
			Set<CheckersMove> potentialMoves)
	{
		CheckersBoardGeometry geometry = (CheckersBoardGeometry) getBoardGeometry();

		Set<Coordinate> potentialTargets = piece.getPotentialTargets(geometry, board, newTarget, true);

		for (Iterator<Coordinate> iPotentialTargets = potentialTargets.iterator(); iPotentialTargets.hasNext();)
		{
			Coordinate target = iPotentialTargets.next();
			Coordinate capture = getCapture(board, newTarget, target);

			if (!captureChain.contains(capture))
			{
				captureChain.add(capture);
				move.addTarget(target, capture);
				internalAppendCapture(piece, board, move, target, captureChain, potentialMoves);
				move.removeLastTarget();
				captureChain.remove(captureChain.size() - 1);
			}
			else
			{
				iPotentialTargets.remove();
			}
		}

		if (potentialTargets.isEmpty())
		{
			potentialMoves.add(new CheckersMove(move));
		}
	}

	@Override
	public Collection<CheckersMove> allowedMoves(GamePosition gamePosition, Board initialBoard)
	{
		return getAllowedMoves(gamePosition);
	}

	private Collection<CheckersMove> getAllowedMoves(GamePosition gamePosition)
	{
		CapturingGamePosition position = (CapturingGamePosition) gamePosition;
		Board board = position.getBoard();
		CheckersBoardGeometry geometry = (CheckersBoardGeometry) getBoardGeometry();

		// collect moves allowed by base rules
		Set<CheckersMove> potentialMoves = new HashSet<CheckersMove>();
		for (Coordinate source : board.locatePieces(position.getActivePlayerIndex()))
		{
			CheckersPiece piece = (CheckersPiece) board.getPiece(source);

			Set<Coordinate> potentialTargets = piece.getPotentialTargets(geometry, board, source, false);
			for (Coordinate target : potentialTargets)
			{
				Coordinate capture = getCapture(board, source, target);

				if (capture == null)
				{
					potentialMoves.add(new CheckersMove(source, target));
				}
				else
				{
					// temporarly capture piece, hacky whacky...
					board.clearPiece(source);

					// capturing move
					CheckersMove move = new CheckersMove(source, target, capture);

					// build capture chain
					List<Coordinate> captureChain = new LinkedList<Coordinate>();
					captureChain.add(capture);
					internalAppendCapture(piece, board, move, target, captureChain, potentialMoves);

					// restore captured piece
					board.setPiece(source, piece);
				}
			}
		}

		// apply quantity rule
		if (quantityRule)
		{
			int maxQuantity = 0;
			for (CheckersMove move : potentialMoves)
			{
				maxQuantity = Math.max(maxQuantity, move.getCaptureQuantity());
			}
			for (Iterator<CheckersMove> i = potentialMoves.iterator(); i.hasNext();)
			{
				CheckersMove move = i.next();
				if (move.getCaptureQuantity() < maxQuantity)
					i.remove();
			}
		}

		return potentialMoves;
	}

	@Override
	public String formatMove(GameMove move)
	{
		CheckersMove checkersMove = (CheckersMove) move;
		List<Coordinate> points = checkersMove.getPoints();

		StringBuilder notation = new StringBuilder();

		for (Coordinate point : points)
		{
			notation.append(point.getNotation());
			notation.append("-");
		}

		if (points.size() == 1)
			notation.append("?");
		else
			notation.setLength(notation.length() - 1);

		return notation.toString();
	}

	@Override
	public Set<String> clickables(GameMove move)
	{
		CheckersMove checkersMove = (CheckersMove) move;
		List<Coordinate> points = checkersMove.getPoints();

		if (points.isEmpty())
			throw new IllegalStateException();

		Set<String> clickables = new LinkedHashSet<String>();
		for (Coordinate point : points)
		{
			clickables.add(point.getNotation());
		}
		return clickables;
	}

	@Override
	public String formatPosition(GamePosition position)
	{
		return CheckersPositionFormat.format((CheckersBoardGeometry) getBoardGeometry(), getPieceSet(), (CapturingGamePosition) position);
	}

	@Override
	public GamePosition parsePosition(String notation)
	{
		return CheckersPositionFormat.parse((CheckersBoardGeometry) getBoardGeometry(), getPieceSet(), notation);
	}

	@Override
	public boolean isFinished(GamePosition position, Board initialBoard)
	{
		return isWin(position);
	}

	@Override
	public float[] points(GamePosition position, Board initialBoard)
	{
		if (isFinished(position, initialBoard))
			return pointsForWin(getWinnerIndex(position));
		else
			return new float[] { 0f, 0f };
	}

	private boolean isWin(GamePosition position)
	{
		return getAllowedMoves(position).isEmpty();
	}

	private int getWinnerIndex(GamePosition position)
	{
		if (!isWin(position))
			throw new IllegalStateException();
		if (!suicide)
			return 1 - position.getActivePlayerIndex();
		else
			return position.getActivePlayerIndex();
	}

	/**
	 * Finds out the coordinate of a captured piece, if any, of a partial move. The move part specified by source and
	 * target must be valid!
	 * 
	 * @param board
	 *            board that is used for the move
	 * @param source
	 *            source coordinate of move part
	 * @param target
	 *            target coordinate of move part
	 * @return coordinate of captured piece, null if none
	 */
	protected Coordinate getCapture(Board board, Coordinate source, Coordinate target)
	{
		ChessBoardLikeGeometry geometry = (ChessBoardLikeGeometry) getBoardGeometry();

		int distance = geometry.distance(source, target);

		if (distance < 2)
			return null;

		int[] dir = geometry.diagonalDir2D(source, target);
		for (int i = 1; i < distance; i++)
		{
			Coordinate coordinate = geometry.vectorAdd(source, dir, i);
			if (board.getPiece(coordinate) != null)
				return coordinate;
		}

		return null;
	}

	public static int opponentColor(int color)
	{
		return 1 - color;
	}

	@Override
	public List<MicroOperation> disassembleMove(GameMove move, GamePosition position, Board initialBoard) throws IllegalMoveException
	{
		CheckersMove checkersMove = (CheckersMove) move;
		CheckersBoardGeometry geometry = (CheckersBoardGeometry) getBoardGeometry();
		Board board = position.getBoard();

		List<MicroOperation> ops = new LinkedList<MicroOperation>();

		// move piece and remove captured pieces
		Iterator<Coordinate> i = checkersMove.getPoints().iterator();
		Coordinate source = i.next();
		while (i.hasNext())
		{
			Coordinate target = i.next();

			Coordinate capturedCoordinate = getCapture(board, source, target);
			if (capturedCoordinate != null)
				ops.add(new CapturePieceOperation(capturedCoordinate));

			ops.add(new MovePieceOperation(source, target));

			source = target;
		}

		// promote to king
		if (geometry.isTargetRank(source, position.getActivePlayerIndex()))
			ops.add(new SwapOutPieceOperation(source, getPieceSet().getPiece(King.class, position.getActivePlayerIndex())));

		// next player
		ops.add(NextPlayerOperation.instance());

		return ops;
	}
}
