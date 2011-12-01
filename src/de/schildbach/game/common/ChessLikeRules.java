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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.schildbach.game.Board;
import de.schildbach.game.Coordinate;
import de.schildbach.game.GamePosition;
import de.schildbach.game.GameRules;
import de.schildbach.game.MicroOperation;
import de.schildbach.game.Piece;
import de.schildbach.game.PieceSet;
import de.schildbach.game.common.piece.ChessLikePiece;

/**
 * @author Andreas Schildbach
 */
public abstract class ChessLikeRules extends GameRules
{
	private Class<? extends ChessLikePiece> checkVulnerablePiece;
	private Class<? extends ChessLikePiece> antiCheckVulnerablePiece;

	protected ChessLikeRules(OrthogonalBoardGeometry geometry, PieceSet pieceSet)
	{
		super(geometry, pieceSet);
	}

	protected final void setCheckVulnerablePiece(Class<? extends ChessLikePiece> checkVulnerablePiece)
	{
		this.checkVulnerablePiece = checkVulnerablePiece;
	}

	protected final Class<? extends ChessLikePiece> getCheckVulnerablePiece()
	{
		return checkVulnerablePiece;
	}

	protected final void setAntiCheckVulnerablePiece(Class<? extends ChessLikePiece> antiCheckVulnerablePiece)
	{
		this.antiCheckVulnerablePiece = antiCheckVulnerablePiece;
	}

	protected final Class<? extends ChessLikePiece> getAntiCheckVulnerablePiece()
	{
		return antiCheckVulnerablePiece;
	}

	/**
	 * determines if one of the kings of a specific color is in check
	 */
	private boolean isInCheck(GamePosition position, int color)
	{
		Board board = position.getBoard();

		if (checkVulnerablePiece != null)
		{
			for (Coordinate c : board.locatePieces(getPieceSet().getPiece(checkVulnerablePiece, color)))
			{
				if (isCoordinateThreatened(board, c, color))
					return true;
			}
		}

		if (antiCheckVulnerablePiece != null)
		{
			for (Coordinate c : board.locatePieces(getPieceSet().getPiece(antiCheckVulnerablePiece, color)))
			{
				if (!isCoordinateThreatened(board, c, color))
					return true;
			}
		}

		return false;
	}

	/**
	 * determines if the position is check, checkmate or stalemate
	 */
	public final CheckState checkState(GamePosition position, Board initialBoard)
	{
		boolean check = isInCheck(position, position.getActivePlayerIndex());

		if (allowedMoves(position, initialBoard).isEmpty())
		{
			if (check)
				return CheckState.CHECKMATE;
			else
				return CheckState.STALEMATE;
		}
		else
		{
			if (check)
				return CheckState.CHECK;
			else
				return null;
		}
	}

	public enum CheckState
	{
		CHECK, CHECKMATE, STALEMATE
	}

	@Override
	public final Collection<? extends ChessLikeMove> allowedMoves(GamePosition position, Board initialBoard)
	{
		Set<ChessLikeMove> allowedMoves = new HashSet<ChessLikeMove>();

		// collect potential moves
		Board board = position.getBoard();
		for (Coordinate source : board.locatePieces(position.getActivePlayerIndex()))
		{
			allowedMoves.addAll(potentialMovesForSource(source, (CapturingGamePosition) position, initialBoard));
		}

		// (anti)check vulnerable pieces cannot be captured
		for (Iterator<ChessLikeMove> iMoves = allowedMoves.iterator(); iMoves.hasNext();)
		{
			ChessLikeMove move = iMoves.next();
			if (looksLikePlainCapture(move, board))
			{
				Piece piece = board.getPiece(move.getTarget());
				if (isCheckVulnerablePiece(piece) || isAnticheckVulnerablePiece(piece))
					iMoves.remove();
			}
		}

		// remove moves
		vetoPotentialMoves(allowedMoves, (CapturingGamePosition) position, initialBoard);

		return allowedMoves;
	}

	protected abstract Set<ChessLikeMove> potentialMovesForSource(Coordinate source, CapturingGamePosition position, Board initialBoard);

	protected abstract void vetoPotentialMoves(Set<ChessLikeMove> potentialMoves, CapturingGamePosition position, Board initialBoard);

	/**
	 * remove all "king in check" situations
	 */
	protected final void removeKingInCheckPositions(Set<ChessLikeMove> potentialMoves, CapturingGamePosition position, Board initialBoard)
	{
		if (checkVulnerablePiece != null || antiCheckVulnerablePiece != null)
		{
			int color = position.getActivePlayerIndex();

			for (Iterator<ChessLikeMove> iMoves = potentialMoves.iterator(); iMoves.hasNext();)
			{
				ChessLikeMove move = iMoves.next();

				// speculatively execute move
				List<MicroOperation> ops = disassembleMove(move, position, initialBoard);
				doOperations(ops, position);

				// remove move if leads to check
				if (isInCheck(position, color))
					iMoves.remove();

				// don't forget to undo move
				undoOperations(ops, position);
			}
		}
	}

	/**
	 * determines if a coordinate is threatened by any piece of a specific color
	 */
	protected final boolean isCoordinateThreatened(Board board, Coordinate coordinate, int defenderColor)
	{
		ChessBoardLikeGeometry geometry = (ChessBoardLikeGeometry) getBoardGeometry();
		int attackerColor = opponentColor(defenderColor);
		Piece piece = board.getPiece(coordinate);
		boolean isDefenderCheckVulnerable = isCheckVulnerablePiece(piece);
		boolean isDefenderAnticheckVulnerable = isAnticheckVulnerablePiece(piece);

		for (Coordinate c : board.locatePieces(attackerColor))
		{
			ChessLikePiece attacker = (ChessLikePiece) board.getPiece(c);
			if (attacker.isThreateningSquare(geometry, board, c, coordinate))
			{
				boolean isAttackerCheckVulnerable = isCheckVulnerablePiece(attacker);
				boolean isAttackerAnticheckVulnerable = isAnticheckVulnerablePiece(attacker);
				if (isDefenderAnticheckVulnerable && (isAttackerAnticheckVulnerable || isAttackerCheckVulnerable))
					continue;
				if (isAttackerAnticheckVulnerable && (isDefenderAnticheckVulnerable || isDefenderCheckVulnerable))
					continue;

				return true;
			}
		}

		return false;
	}

	private boolean isCheckVulnerablePiece(Piece piece)
	{
		if (piece == null)
			return false;

		if (checkVulnerablePiece == null)
			return false;

		return piece.getClass().equals(checkVulnerablePiece);
	}

	private boolean isAnticheckVulnerablePiece(Piece piece)
	{
		if (piece == null)
			return false;

		if (antiCheckVulnerablePiece == null)
			return false;

		return piece.getClass().equals(antiCheckVulnerablePiece);
	}

	protected abstract boolean looksLikeCapture(ChessLikeMove move, Board board);

	protected boolean looksLikePlainCapture(ChessLikeMove move, Board board)
	{
		Piece piece = board.getPiece(move.getTarget());
		if (piece == null)
			return false;

		return true;
	}

	@Override
	public final boolean isFinished(GamePosition position, Board initialBoard)
	{
		return isDraw(position, initialBoard) || isWin(position, initialBoard);
	}

	@Override
	public final float[] points(GamePosition position, Board initialBoard)
	{
		if (isWin(position, initialBoard))
			return pointsForWin(getWinnerIndex(position, initialBoard));
		else if (isDraw(position, initialBoard))
			return pointsForDraw();
		else
			return new float[] { 0f, 0f };
	}

	private boolean isWin(GamePosition position, Board initialBoard)
	{
		if (checkVulnerablePiece != null)
			return checkState(position, initialBoard) == CheckState.CHECKMATE;
		else
			return allowedMoves(position, initialBoard).isEmpty();
	}

	private int getWinnerIndex(GamePosition position, Board initialBoard)
	{
		if (!isWin(position, initialBoard))
			throw new IllegalStateException();
		if (checkVulnerablePiece != null)
			return 1 - position.getActivePlayerIndex();
		else
			// if suicide variant
			return position.getActivePlayerIndex();
	}

	private boolean isDraw(GamePosition position, Board initialBoard)
	{
		if (checkVulnerablePiece != null)
			return checkState(position, initialBoard) == CheckState.STALEMATE || onlyKingsOnBoard(position.getBoard());
		else
			return false;
	}

	private boolean onlyKingsOnBoard(Board board)
	{
		for (Coordinate coordinate : board.locateOccupiedFields())
		{
			Piece piece = board.getPiece(coordinate);
			if (!(isCheckVulnerablePiece(piece) || isAnticheckVulnerablePiece(piece)))
				return false;
		}

		return true;
	}

	/** should be deprecated sometime */
	private static int opponentColor(int color)
	{
		return 1 - color;
	}
}
