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

package de.schildbach.game.dragonchess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.schildbach.game.Board;
import de.schildbach.game.BoardGeometry;
import de.schildbach.game.Coordinate;
import de.schildbach.game.Game;
import de.schildbach.game.GameMove;
import de.schildbach.game.GamePosition;
import de.schildbach.game.MicroOperation;
import de.schildbach.game.Piece;
import de.schildbach.game.PieceSet;
import de.schildbach.game.PieceSet.ColorEntry;
import de.schildbach.game.common.CapturePieceOperation;
import de.schildbach.game.common.CapturingGamePosition;
import de.schildbach.game.common.ChessLikeMove;
import de.schildbach.game.common.ChessLikeRules;
import de.schildbach.game.common.GenericPieceSet;
import de.schildbach.game.common.MovePieceOperation;
import de.schildbach.game.common.NextPlayerOperation;
import de.schildbach.game.common.OrthogonalBoardGeometry;
import de.schildbach.game.common.SwapOutPieceOperation;
import de.schildbach.game.common.GenericPieceSet.PieceEntry;
import de.schildbach.game.common.piece.ChessLikePiece;
import de.schildbach.game.dragonchess.piece.Basilisk;
import de.schildbach.game.dragonchess.piece.Cleric;
import de.schildbach.game.dragonchess.piece.Dragon;
import de.schildbach.game.dragonchess.piece.Dwarf;
import de.schildbach.game.dragonchess.piece.Elemental;
import de.schildbach.game.dragonchess.piece.Griffin;
import de.schildbach.game.dragonchess.piece.Hero;
import de.schildbach.game.dragonchess.piece.King;
import de.schildbach.game.dragonchess.piece.Mage;
import de.schildbach.game.dragonchess.piece.Oliphant;
import de.schildbach.game.dragonchess.piece.Paladin;
import de.schildbach.game.dragonchess.piece.Sylph;
import de.schildbach.game.dragonchess.piece.Thief;
import de.schildbach.game.dragonchess.piece.Unicorn;
import de.schildbach.game.dragonchess.piece.Warrior;
import de.schildbach.game.exception.IllegalMoveException;
import de.schildbach.game.exception.ParseException;

/**
 * @author Andreas Schildbach
 */
public class DragonchessRules extends ChessLikeRules
{
	private static final String DEFAULT_INITIAL_BOARD = "2g3r3g1/s1s1s1s1s1s1/66/66/66/66/S1S1S1S1S1S1/2G3R3G1 " //
			+ "ouhtcmkpthuo/wwwwwwwwwwww/66/66/66/66/WWWWWWWWWWWW/OUHTCMKPTHUO " //
			+ "2b3e3b1/1d1d1d1d1d1d/66/66/66/66/1D1D1D1D1D1D/2B3E3B1";

	private static final PieceEntry[] PIECES = { new PieceEntry('S', 'S', Sylph.class), //
			new PieceEntry('G', 'G', Griffin.class), //
			new PieceEntry('R', 'R', Dragon.class), //
			new PieceEntry('O', 'O', Oliphant.class), //
			new PieceEntry('U', 'U', Unicorn.class), //
			new PieceEntry('H', 'H', Hero.class), //
			new PieceEntry('T', 'T', Thief.class), //
			new PieceEntry('C', 'C', Cleric.class), //
			new PieceEntry('M', 'M', Mage.class), //
			new PieceEntry('K', 'K', King.class), //
			new PieceEntry('P', 'P', Paladin.class), //
			new PieceEntry('W', 'W', Warrior.class), //
			new PieceEntry('B', 'B', Basilisk.class), //
			new PieceEntry('E', 'E', Elemental.class), //
			new PieceEntry('D', 'D', Dwarf.class) };

	private static final ColorEntry[] COLORS = { new ColorEntry(0, "gold", 'g'), //
			new ColorEntry(1, "scarlet", 's') //
	};

	public DragonchessRules()
	{
		super(DragonchessBoardGeometry.instance(), new GenericPieceSet(PIECES, COLORS));

		setCheckVulnerablePiece(King.class);
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
	public String formatMove(GameMove gameMove)
	{
		ChessLikeMove move = (ChessLikeMove) gameMove;
		return move.getSource().getNotation() + "-" + move.getTarget().getNotation();
	}

	@Override
	public Set<String> clickables(GameMove move)
	{
		ChessLikeMove chessMove = (ChessLikeMove) move;
		Set<String> clickables = new LinkedHashSet<String>();
		clickables.add(chessMove.getSource().getNotation());
		clickables.add(chessMove.getTarget().getNotation());
		return clickables;
	}

	@Override
	protected String[] basicFormatGameArray(Game game, Locale locale)
	{
		int size = game.getSize();
		String[] array = new String[size];

		GamePosition position = (GamePosition) game.getInitialPosition().clone();
		Board board = position.getBoard();
		Board initialBoard = game.getInitialPosition().getBoard();

		StringBuilder notation = new StringBuilder();
		for (int i = 0; i < size; i++)
		{
			ChessLikeMove move = (ChessLikeMove) game.getMove(i);
			Coordinate source = move.getSource();
			Coordinate target = move.getTarget();
			Piece piece = board.getPiece(source);

			notation.setLength(0);

			Collection<? extends ChessLikeMove> potentialMoves = allowedMoves(position, initialBoard);

			// narrow by piece
			for (Iterator<? extends ChessLikeMove> iAllowed = potentialMoves.iterator(); iAllowed.hasNext();)
			{
				if (!board.getPiece(iAllowed.next().getSource()).equals(piece))
					iAllowed.remove();
			}
			notation.append(getPieceSet().getCharRepresentation(piece.getClass(), locale));

			boolean looksLikeCapture = looksLikeCapture(move, board) || looksLikeCaptureFromAfar(move, board);
			if (!looksLikeCapture)
			{
				// narrow by target
				for (Iterator<? extends ChessLikeMove> iAllowed = potentialMoves.iterator(); iAllowed.hasNext();)
				{
					if (!iAllowed.next().getTarget().equals(target))
						iAllowed.remove();
				}
				notation.append(target.getNotation());

				if (potentialMoves.size() > 1) // still indistinct
				{
					// narrow by source
					for (Iterator<? extends ChessLikeMove> iAllowed = potentialMoves.iterator(); iAllowed.hasNext();)
					{
						if (!iAllowed.next().getSource().equals(source))
							iAllowed.remove();
					}
					notation.insert(1, "/" + source.getNotation() + "-");
				}
			}
			else
			{
				notation.append('x');
				Piece capturedPiece = board.getPiece(target);

				// try to narrow by captured piece
				Collection<ChessLikeMove> backup = new LinkedList<ChessLikeMove>(potentialMoves);
				for (Iterator<? extends ChessLikeMove> iAllowed = potentialMoves.iterator(); iAllowed.hasNext();)
				{
					if (!capturedPiece.equals(board.getPiece(iAllowed.next().getTarget())))
						iAllowed.remove();
				}
				if (potentialMoves.size() == 1)
				{
					notation.append(getPieceSet().getCharRepresentation(capturedPiece.getClass(), locale));
				}
				else
				{
					potentialMoves = backup;

					// narrow by target
					for (Iterator<? extends ChessLikeMove> iAllowed = potentialMoves.iterator(); iAllowed.hasNext();)
					{
						if (!iAllowed.next().getTarget().equals(target))
							iAllowed.remove();
					}
					notation.append(target.getNotation());

					if (potentialMoves.size() > 1) // still indistinct
					{
						// try again to narrow by captured piece
						for (Iterator<? extends ChessLikeMove> iAllowed = potentialMoves.iterator(); iAllowed.hasNext();)
						{
							if (!capturedPiece.equals(board.getPiece(iAllowed.next().getTarget())))
								iAllowed.remove();
						}
						if (potentialMoves.size() == 1)
						{
							notation.insert(2, getPieceSet().getCharRepresentation(capturedPiece.getClass(), locale));
						}
						else
						{
							// narrow by source
							for (Iterator<? extends ChessLikeMove> iAllowed = potentialMoves.iterator(); iAllowed.hasNext();)
							{
								if (!iAllowed.next().getSource().equals(source))
									iAllowed.remove();
							}
							notation.insert(1, "/" + source.getNotation());
						}
					}
				}
			}

			// sanity check
			if (potentialMoves.size() != 1)
				throw new IllegalStateException("move " + i + " (" + move + "), potentialMoves: " + potentialMoves.toString());

			// execute move
			List<MicroOperation> ops = disassembleMove(move, position, game.getInitialPosition().getBoard());
			doOperations(ops, position);

			// check
			CheckState checkState = checkState(position, game.getInitialPosition().getBoard());
			if (checkState == CheckState.CHECKMATE)
			{
				notation.append("#");
			}
			else if (checkState == CheckState.CHECK)
			{
				notation.append("+");
			}

			array[i] = notation.toString();
		}

		return array;
	}

	private static final Pattern SIMPLE_MOVE_PATTERN = Pattern.compile("^(\\d[a-zA-Z]\\d)([x\\-])(\\d[a-zA-Z]\\d)$");

	private static final Pattern ACN_MOVE_PATTERN = Pattern.compile("^([A-Z])(?:/(\\d[a-z]\\d))?([x-])?([A-Z])?(\\d[a-z]\\d)?(\\+\\+?|#)?$");

	@Override
	public GameMove parseMove(String moveString, Locale locale, GamePosition position, Board initialBoard) throws ParseException
	{
		Collection<? extends ChessLikeMove> allowedMoves = allowedMoves(position, initialBoard);
		Matcher m = ACN_MOVE_PATTERN.matcher(moveString);
		if (m.matches())
		{
			return parseMove(moveString, locale, position, m.group(1), m.group(2), m.group(3), m.group(4), m.group(5), allowedMoves);
		}
		m = SIMPLE_MOVE_PATTERN.matcher(moveString);
		if (m.matches())
		{
			return parseMove(moveString, locale, position, null, m.group(1), null, null, m.group(3), allowedMoves);
		}
		throw new ParseException(moveString, "unknown format");
	}

	private ChessLikeMove parseMove(String moveString, Locale locale, GamePosition position, String pretendedPiece, String pretendedSource,
			String pretendedCapture, String pretendedCapturedPiece, String pretendedTarget, Collection<? extends ChessLikeMove> moves)
			throws ParseException
	{
		PieceSet pieceSet = getPieceSet();
		Board board = position.getBoard();

		for (Iterator<? extends ChessLikeMove> i = moves.iterator(); i.hasNext();)
		{
			ChessLikeMove move = (ChessLikeMove) i.next();
			Coordinate source = move.getSource();
			Piece piece = board.getPiece(source);

			if (pretendedPiece != null && pieceSet.getCharRepresentation(piece.getClass(), locale) != pretendedPiece.charAt(0))
				i.remove();
			else if (pretendedSource != null && !pretendedSource.equals(source.getNotation()))
				i.remove();
			else if ((pretendedCapture != null) && (pretendedCapture.equals("x") != looksLikeCapture(move, board)))
				i.remove();
			else if (pretendedCapturedPiece != null)
			{
				Piece capturedPiece = board.getPiece(move.getTarget());
				if (capturedPiece == null || pretendedCapturedPiece.charAt(0) != pieceSet.getCharRepresentation(capturedPiece.getClass(), locale))
					i.remove();
			}
			else if (pretendedTarget != null && !pretendedTarget.equals(move.getTarget().getNotation()))
				i.remove();
		}

		if (moves.size() == 1)
		{
			ChessLikeMove move = moves.iterator().next();
			return move;
		}
		else if (moves.size() > 1)
			throw new ParseException(moveString, "ambiguous move " + moves);
		else
			throw new ParseException(moveString, "no move");
	}

	private static final Pattern UNMARSHAL_MOVE_PATTERN = Pattern.compile("^(\\d[a-zA-Z]\\d)-(\\d[a-zA-Z]\\d)$");

	@Override
	protected GameMove unmarshalMove(String notation) throws ParseException
	{
		Matcher m = UNMARSHAL_MOVE_PATTERN.matcher(notation);
		if (m.matches())
		{
			BoardGeometry geometry = getBoardGeometry();
			Coordinate source = geometry.locateCoordinate(m.group(1));
			Coordinate target = geometry.locateCoordinate(m.group(2));
			return new ChessLikeMove(source, target);
		}
		else
		{
			throw new ParseException(notation, "unknown format");
		}
	}

	@Override
	protected final Set<ChessLikeMove> potentialMovesForSource(Coordinate source, CapturingGamePosition position, Board initialBoard)
	{
		DragonchessBoardGeometry geometry = (DragonchessBoardGeometry) getBoardGeometry();
		Board board = position.getBoard();
		ChessLikePiece piece = (ChessLikePiece) board.getPiece(source);

		Set<ChessLikeMove> potentialMoves = new HashSet<ChessLikeMove>();

		// check if piece is frozen by basilisk
		if (geometry.isLayer(source, DragonchessBoardGeometry.GROUND))
		{
			Piece pieceBelow = board.getPiece(geometry.vectorAdd(source, geometry.down(), 1));
			if (pieceBelow instanceof Basilisk && pieceBelow.getColor() != board.getPiece(source).getColor())
				return potentialMoves;
		}

		// calculate potential moves from pieces
		for (Coordinate target : piece.getPotentialTargets(geometry, board, source))
			potentialMoves.add(new ChessLikeMove(source, target));

		// jump home ability of sylphs
		if (piece instanceof Sylph && geometry.isLayer(source, DragonchessBoardGeometry.GROUND))
		{
			for (Coordinate target : initialBoard.locatePieces(piece))
			{
				if (board.getPiece(target) == null)
					potentialMoves.add(new ChessLikeMove(source, target));
			}
		}

		return potentialMoves;
	}

	@Override
	protected void vetoPotentialMoves(Set<ChessLikeMove> potentialMoves, CapturingGamePosition position, Board initialBoard)
	{
		// enforce check rule
		removeKingInCheckPositions(potentialMoves, position, initialBoard);
	}

	@Override
	public String formatPosition(GamePosition position)
	{
		return DragonchessPositionFormat.format((OrthogonalBoardGeometry) getBoardGeometry(), getPieceSet(), (CapturingGamePosition) position);
	}

	@Override
	public GamePosition parsePosition(String notation)
	{
		return DragonchessPositionFormat.parse((OrthogonalBoardGeometry) getBoardGeometry(), getPieceSet(), notation);
	}

	public static int opponentColor(int color)
	{
		return 1 - color;
	}

	@Override
	public List<MicroOperation> disassembleMove(GameMove move, GamePosition position, Board initialBoard) throws IllegalMoveException
	{
		ChessLikeMove chessMove = (ChessLikeMove) move;
		DragonchessBoardGeometry geometry = (DragonchessBoardGeometry) getBoardGeometry();
		Board board = position.getBoard();
		Coordinate target = chessMove.getTarget();

		List<MicroOperation> ops = new LinkedList<MicroOperation>();

		// capture
		if (looksLikePlainCapture(chessMove, board))
			ops.add(new CapturePieceOperation(target));

		// modify board
		if (!looksLikeCaptureFromAfar(chessMove, board))
		{
			Coordinate source = chessMove.getSource();
			Piece piece = board.getPiece(source);

			// move piece
			ops.add(new MovePieceOperation(source, target));

			// promote warrior to hero
			if (piece instanceof Warrior && geometry.isInTargetArea(target, piece.getColor()))
				ops.add(new SwapOutPieceOperation(target, getPieceSet().getPiece(Hero.class, piece.getColor())));
		}

		// next player
		ops.add(NextPlayerOperation.instance());

		return ops;
	}

	private boolean looksLikeCaptureFromAfar(ChessLikeMove move, Board board)
	{
		Piece piece = board.getPiece(move.getSource());
		if (piece == null)
			return false;

		if (!(piece instanceof Dragon))
			return false;

		DragonchessBoardGeometry geometry = (DragonchessBoardGeometry) getBoardGeometry();

		if (!geometry.isLayer(move.getTarget(), DragonchessBoardGeometry.GROUND))
			return false;

		return true;
	}

	@Override
	protected boolean looksLikeCapture(ChessLikeMove move, Board board)
	{
		return looksLikePlainCapture(move, board);
	}
}
