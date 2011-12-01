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

import org.apache.commons.lang.ObjectUtils;

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
import de.schildbach.game.chess.piece.AntiKing;
import de.schildbach.game.chess.piece.BerolinaPawn;
import de.schildbach.game.chess.piece.Bishop;
import de.schildbach.game.chess.piece.King;
import de.schildbach.game.chess.piece.Knight;
import de.schildbach.game.chess.piece.Pawn;
import de.schildbach.game.chess.piece.Queen;
import de.schildbach.game.chess.piece.Rook;
import de.schildbach.game.common.CapturePieceOperation;
import de.schildbach.game.common.CapturingGamePosition;
import de.schildbach.game.common.ChessLikeMove;
import de.schildbach.game.common.ChessLikeRules;
import de.schildbach.game.common.ExchangePieceOperation;
import de.schildbach.game.common.GenericPieceSet;
import de.schildbach.game.common.MovePieceOperation;
import de.schildbach.game.common.NextPlayerOperation;
import de.schildbach.game.common.OrthogonalBoardGeometry;
import de.schildbach.game.common.SwapOutPieceOperation;
import de.schildbach.game.common.GenericPieceSet.PieceEntry;
import de.schildbach.game.common.piece.ChessLikePiece;
import de.schildbach.game.exception.IllegalMoveException;
import de.schildbach.game.exception.ParseException;

/**
 * @author Andreas Schildbach
 */
public class ChessRules extends ChessLikeRules
{
	public enum Variant
	{
		SUICIDE, ANTIKING
	}

	private static final String DEFAULT_INITIAL_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

	private static final String ANTIKING_INITIAL_BOARD = "2prbkqA/2p1nnbr/2pppppp/8/8/PPPPPP2/RBNN1P2/aQKBRP2";

	private static final PieceEntry[] DEFAULT_PIECES = { new PieceEntry('P', 'B', Pawn.class), //
			new PieceEntry('R', 'T', Rook.class), //
			new PieceEntry('N', 'S', Knight.class), // 
			new PieceEntry('B', 'L', Bishop.class), //
			new PieceEntry('Q', 'D', Queen.class), //
			new PieceEntry('K', 'K', King.class) //
	};

	private static final PieceEntry[] ANTIKING_PIECES = { new PieceEntry('P', 'B', BerolinaPawn.class), //
			new PieceEntry('R', 'T', Rook.class), //
			new PieceEntry('N', 'S', Knight.class), // 
			new PieceEntry('B', 'L', Bishop.class), //
			new PieceEntry('Q', 'D', Queen.class), //
			new PieceEntry('K', 'K', King.class), //
			new PieceEntry('A', 'A', AntiKing.class) //
	};

	private static final ColorEntry[] COLORS = { new ColorEntry(0, "white", 'w'), //
			new ColorEntry(1, "black", 'b') //
	};

	@SuppressWarnings("unchecked")
	private static final Class<Piece>[] DEFAULT_PROMOTION_OPTIONS = new Class[] { Queen.class, Rook.class, Bishop.class, Knight.class };

	@SuppressWarnings("unchecked")
	private static final Class<Piece>[] SUICIDE_PROMOTION_OPTIONS = new Class[] { Queen.class, King.class, Rook.class, Bishop.class, Knight.class };

	private boolean mustCaptureRule = false;
	private boolean promotionRule = true;
	private Class<Piece>[] promotionOptions = DEFAULT_PROMOTION_OPTIONS;
	private boolean castlingRule = true;
	private boolean antiKingCastlingRule = false;
	private boolean advanceTwoSquaresRule = true;
	private boolean enPassantRule = true;

	public ChessRules(Variant variant)
	{
		super(ChessBoardGeometry.instance(), new GenericPieceSet(variant == Variant.ANTIKING ? ANTIKING_PIECES : DEFAULT_PIECES, COLORS));

		if (variant == null)
		{
			setCheckVulnerablePiece(King.class);
		}
		else if (variant == Variant.SUICIDE)
		{
			setCheckVulnerablePiece(null);
			mustCaptureRule = true;
			promotionOptions = SUICIDE_PROMOTION_OPTIONS;
			castlingRule = false;
			enPassantRule = false;
		}
		else if (variant == Variant.ANTIKING)
		{
			setCheckVulnerablePiece(King.class);
			setAntiCheckVulnerablePiece(AntiKing.class);
			castlingRule = false;
			antiKingCastlingRule = true;
			advanceTwoSquaresRule = false;
			enPassantRule = false;
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
			if (antiKingCastlingRule)
				parseBoard(initialBoard, ANTIKING_INITIAL_BOARD);
			else
				parseBoard(initialBoard, DEFAULT_INITIAL_BOARD);
		}

		if (castlingRule)
		{
			ChessPosition position = new ChessPosition(initialBoard);
			position.setActivePlayerIndex(0);
			position.setFullmoveNumber(1);
			position.setHalfmoveClock(0);
			position.setCastlingAvailable(0, CastlingSide.QUEENSIDE, castlingRule);
			position.setCastlingAvailable(0, CastlingSide.KINGSIDE, castlingRule);
			position.setCastlingAvailable(1, CastlingSide.QUEENSIDE, castlingRule);
			position.setCastlingAvailable(1, CastlingSide.KINGSIDE, castlingRule);
			return position;
		}
		else if (antiKingCastlingRule)
		{
			AntiKingChessPosition position = new AntiKingChessPosition(initialBoard);
			position.setActivePlayerIndex(0);
			position.setFullmoveNumber(1);
			position.setHalfmoveClock(0);
			position.setCastlingAvailable(0, King.class, antiKingCastlingRule);
			position.setCastlingAvailable(0, AntiKing.class, antiKingCastlingRule);
			position.setCastlingAvailable(1, King.class, antiKingCastlingRule);
			position.setCastlingAvailable(1, AntiKing.class, antiKingCastlingRule);
			return position;
		}
		else
		{
			ChessLikePosition position = new ChessLikePosition(initialBoard);
			position.setActivePlayerIndex(0);
			position.setFullmoveNumber(1);
			return position;
		}
	}

	private static final Pattern SIMPLE_MOVE_PATTERN = Pattern.compile("^([a-z])(\\d)-([a-z]\\d)(?:=([A-Z]))?$");

	private static final Pattern ACN_MOVE_PATTERN = Pattern
			.compile("^(?:([A-Z]?)([a-z])??(\\d)??(x?)([a-z]\\d)(?:=([A-Z]))?|(O-O(?:-O)?|0-0(?:-0)?))(\\+\\+?|#)?$");

	@Override
	public GameMove parseMove(String moveString, Locale locale, GamePosition position, Board initialBoard) throws ParseException
	{
		Collection<? extends ChessLikeMove> allowedMoves = allowedMoves(position, initialBoard);
		Matcher m = ACN_MOVE_PATTERN.matcher(moveString);
		if (m.matches())
		{
			return parseMove(moveString, locale, position, m.group(1), m.group(2), m.group(3), m.group(4), m.group(5), m.group(6), m.group(8), m
					.group(7), allowedMoves);
		}
		m = SIMPLE_MOVE_PATTERN.matcher(moveString);
		if (m.matches())
		{
			return parseMove(moveString, locale, position, null, m.group(1), m.group(2), null, m.group(3), m.group(4), null, null, allowedMoves);
		}
		throw new ParseException(moveString, "unknown format");
	}

	private ChessLikeMove parseMove(String moveString, Locale locale, GamePosition position, String pretendedPiece, String pretendedSourceFile,
			String pretendedSourceRank, String pretendedCapture, String pretendedTarget, String pretendedPromotionPiece, String flag,
			String castling, Collection<? extends ChessLikeMove> potentialMoves) throws ParseException
	{
		ChessBoardGeometry geometry = (ChessBoardGeometry) getBoardGeometry();
		PieceSet pieceSet = getPieceSet();
		Board board = position.getBoard();

		for (Iterator<? extends ChessLikeMove> i = potentialMoves.iterator(); i.hasNext();)
		{
			ChessMove move = (ChessMove) i.next();
			Coordinate source = move.getSource();
			if (castling != null)
			{
				if (!looksLikeCastlingMove(move, board))
					i.remove();
				else if ((castling.equals("O-O") || castling.equals("0-0")) != geometry.isKingSide(source, move.getTarget()))
					i.remove();
			}
			else
			{
				Piece piece = board.getPiece(source);
				String sourceNotation = source.getNotation();
				char sourceFile = sourceNotation.charAt(0);
				char sourceRank = sourceNotation.charAt(1);
				boolean pawn = piece instanceof Pawn || piece instanceof BerolinaPawn;
				if (pretendedPiece != null && pretendedPiece.length() > 0
						&& pieceSet.getCharRepresentation(piece.getClass(), locale) != pretendedPiece.charAt(0))
					i.remove();
				else if (pretendedPiece != null && pretendedPiece.length() == 0 && !pawn)
					i.remove();
				else if (pretendedSourceFile != null && sourceFile != pretendedSourceFile.charAt(0))
					i.remove();
				else if (pretendedSourceRank != null && sourceRank != pretendedSourceRank.charAt(0))
					i.remove();
				else if ((pretendedCapture != null) && (pretendedCapture.equals("x") != looksLikeCapture(move, board)))
					i.remove();
				else if (!pretendedTarget.equals(move.getTarget().getNotation()))
					i.remove();
				else if ((pretendedPromotionPiece != null)
						&& pieceSet.getCharRepresentation(move.getPromotionPiece(), locale) != pretendedPromotionPiece.charAt(0))
					i.remove();
			}
		}

		if (potentialMoves.size() == 1)
			return potentialMoves.iterator().next();
		else if (potentialMoves.size() > 1)
			throw new ParseException(moveString, "ambiguous move");
		else
			throw new ParseException(moveString, "no move");
	}

	private static final Pattern UNMARSHAL_MOVE_PATTERN = Pattern.compile("^([a-z]\\d)-([a-z]\\d)(?:=([A-Z]))?$");

	@Override
	protected GameMove unmarshalMove(String notation) throws ParseException
	{
		Matcher m = UNMARSHAL_MOVE_PATTERN.matcher(notation);
		if (m.matches())
		{
			BoardGeometry geometry = getBoardGeometry();
			Coordinate source = geometry.locateCoordinate(m.group(1));
			Coordinate target = geometry.locateCoordinate(m.group(2));
			if (m.group(3) == null)
			{
				return new ChessMove(source, target);
			}
			else
			{
				Class<? extends Piece> promotionPiece = getPieceSet().getPiece(m.group(3).charAt(0), 0).getClass();
				return new ChessMove(source, target, promotionPiece);
			}
		}
		else
		{
			throw new ParseException(notation, "unknown format");
		}
	}

	@Override
	public String formatMove(GameMove move)
	{
		ChessMove chessMove = (ChessMove) move;
		Class<? extends Piece> promotionPiece = chessMove.getPromotionPiece();
		return chessMove.getSource().getNotation() + "-" + chessMove.getTarget().getNotation()
				+ (promotionPiece != null ? "=" + getPieceSet().getCharRepresentation(promotionPiece) : "");
	}

	@Override
	public Set<String> clickables(GameMove move)
	{
		ChessMove chessMove = (ChessMove) move;
		Class<? extends Piece> promotionPiece = chessMove.getPromotionPiece();
		Set<String> clickables = new LinkedHashSet<String>();
		clickables.add(chessMove.getSource().getNotation());
		clickables.add(chessMove.getTarget().getNotation());
		if (promotionPiece != null)
			clickables.add("=" + getPieceSet().getCharRepresentation(promotionPiece));
		return clickables;
	}

	@Override
	protected String[] basicFormatGameArray(Game game, Locale locale)
	{
		int size = game.getSize();
		String[] array = new String[size];

		ChessBoardGeometry geometry = (ChessBoardGeometry) getBoardGeometry();
		ChessLikePosition position = (ChessLikePosition) game.getInitialPosition().clone();
		Board board = position.getBoard();
		Board initialBoard = game.getInitialPosition().getBoard();
		PieceSet pieceSet = getPieceSet();

		StringBuilder notation = new StringBuilder();
		for (int index = 0; index < size; index++)
		{
			ChessMove move = (ChessMove) game.getMove(index);
			Coordinate source = move.getSource();
			Coordinate target = move.getTarget();
			Piece piece = board.getPiece(source);
			String sourceNotation = source.getNotation();
			char sourceFile = sourceNotation.charAt(0);
			char sourceRank = sourceNotation.charAt(1);

			notation.setLength(0);

			if (looksLikeCastlingMove(move, board))
			{
				if (geometry.isKingSide(source, target))
					notation.append("O-O");
				else
					notation.append("O-O-O");
			}
			else
			{
				Collection<? extends ChessLikeMove> potentialMoves = allowedMoves(position, initialBoard);

				// narrow by target
				for (Iterator<? extends ChessLikeMove> iAllowed = potentialMoves.iterator(); iAllowed.hasNext();)
				{
					if (!iAllowed.next().getTarget().equals(target))
						iAllowed.remove();
				}
				notation.append(target.getNotation());

				boolean pawn = piece instanceof Pawn || piece instanceof BerolinaPawn;

				// narrow by capture
				boolean capture = looksLikePlainCapture(move, board) || looksLikeEnPassantCapture(move, board);
				if (capture)
				{
					for (Iterator<? extends ChessLikeMove> iAllowed = potentialMoves.iterator(); iAllowed.hasNext();)
					{
						if (!looksLikeCapture(iAllowed.next(), board))
							iAllowed.remove();
					}
					notation.insert(0, 'x');

					if (pawn)
					{
						// narrow to source file
						for (Iterator<? extends ChessLikeMove> iAllowed = potentialMoves.iterator(); iAllowed.hasNext();)
						{
							if (!geometry.sameFile(iAllowed.next().getSource(), source))
								iAllowed.remove();
						}
						notation.insert(0, sourceFile);
					}
				}

				// narrow by piece
				for (Iterator<? extends ChessLikeMove> iAllowed = potentialMoves.iterator(); iAllowed.hasNext();)
				{
					if (!board.getPiece(iAllowed.next().getSource()).equals(piece))
						iAllowed.remove();
				}

				// narrow by promotion
				for (Iterator<? extends ChessLikeMove> iAllowed = potentialMoves.iterator(); iAllowed.hasNext();)
				{
					if (!ObjectUtils.equals(((ChessMove) iAllowed.next()).getPromotionPiece(), move.getPromotionPiece()))
						iAllowed.remove();
				}
				if (move.getPromotionPiece() != null)
					notation.append("=" + pieceSet.getCharRepresentation(move.getPromotionPiece(), locale));

				if (potentialMoves.size() > 1)
				{
					boolean sameRank = false;
					boolean sameFile = false;
					for (ChessLikeMove potentialMove : potentialMoves)
					{
						Coordinate potentialSource = potentialMove.getSource();
						if (!potentialSource.equals(source))
						{
							if (geometry.sameFile(potentialSource, source))
								sameFile = true;
							if (geometry.sameRank(potentialSource, source))
								sameRank = true;
						}
					}
					if (sameRank && sameFile)
					{
						// narrow to source
						for (Iterator<? extends ChessLikeMove> iAllowed = potentialMoves.iterator(); iAllowed.hasNext();)
						{
							if (!iAllowed.next().getSource().equals(source))
								iAllowed.remove();
						}
						notation.insert(0, sourceNotation);
					}
					else if (!sameFile)
					{
						// narrow to source file
						for (Iterator<? extends ChessLikeMove> iAllowed = potentialMoves.iterator(); iAllowed.hasNext();)
						{
							if (!geometry.sameFile(iAllowed.next().getSource(), source))
								iAllowed.remove();
						}
						notation.insert(0, sourceFile);
					}
					else
					{
						// narrow to source rank
						for (Iterator<? extends ChessLikeMove> iAllowed = potentialMoves.iterator(); iAllowed.hasNext();)
						{
							if (!geometry.sameRank(iAllowed.next().getSource(), source))
								iAllowed.remove();
						}
						notation.insert(0, sourceRank);
					}
				}

				// piece character
				if (!pawn)
					notation.insert(0, pieceSet.getCharRepresentation(piece.getClass(), locale));

				// sanity check
				if (potentialMoves.size() != 1)
					throw new IllegalStateException(potentialMoves.toString());
			}

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

			array[index] = notation.toString();
		}

		return array;
	}

	@Override
	protected final Set<ChessLikeMove> potentialMovesForSource(Coordinate source, CapturingGamePosition position, final Board initialBoard)
	{
		Board board = position.getBoard();
		ChessBoardGeometry geometry = (ChessBoardGeometry) getBoardGeometry();
		ChessLikePiece piece = (ChessLikePiece) board.getPiece(source);
		int pieceColor = piece.getColor();

		// calculate potential moves from pieces
		Set<ChessLikeMove> potentialMoves = new HashSet<ChessLikeMove>();
		for (Coordinate target : piece.getPotentialTargets(geometry, board, source))
			potentialMoves.add(new ChessMove(source, target));

		// add castling moves
		if (castlingRule && piece instanceof King)
		{
			if (((ChessPosition) position).getCastlingAvailable(pieceColor, CastlingSide.QUEENSIDE))
			{
				Coordinate rookCoordinate = castle(board, source, CastlingSide.QUEENSIDE, initialBoard);
				if (rookCoordinate != null)
					potentialMoves.add(new ChessMove(source, rookCoordinate));
			}

			if (((ChessPosition) position).getCastlingAvailable(pieceColor, CastlingSide.KINGSIDE))
			{
				Coordinate rookCoordinate = castle(board, source, CastlingSide.KINGSIDE, initialBoard);
				if (rookCoordinate != null)
					potentialMoves.add(new ChessMove(source, rookCoordinate));
			}
		}

		// add antiking castling moves
		if (antiKingCastlingRule && (piece instanceof King || piece instanceof AntiKing))
		{
			if (((AntiKingChessPosition) position).getCastlingAvailable(pieceColor, piece.getClass()))
			{
				for (int[] vector : geometry.knightVectors2D())
				{
					Coordinate target = geometry.vectorAdd(source, vector, 1);
					if (target != null && board.getPiece(target) == null)
					{
						potentialMoves.add(new ChessMove(source, target));
					}
				}
			}
		}

		// add en passant capture
		if (enPassantRule)
		{
			Coordinate enPassantTargetSquare = ((ChessPosition) position).getEnPassantTargetSquare();
			if (enPassantTargetSquare != null && piece instanceof Pawn
					&& ((Pawn) piece).isThreateningSquare(geometry, board, source, enPassantTargetSquare))
			{
				potentialMoves.add(new ChessMove(source, enPassantTargetSquare));
			}
		}

		// advance two squares rule
		if (advanceTwoSquaresRule && piece instanceof Pawn && isInHomeArea(initialBoard, source, piece))
		{
			int[] forward = geometry.forwardDir(piece.getColor());
			Coordinate target = geometry.vectorAdd(source, forward, 1);
			if (target != null && board.getPiece(target) == null)
			{
				target = geometry.vectorAdd(source, forward, 2);
				if (target != null && board.getPiece(target) == null)
					potentialMoves.add(new ChessMove(source, target));
			}
		}

		// replace promotion moves
		if (promotionRule)
		{
			List<ChessMove> promotionMoves = new LinkedList<ChessMove>();
			for (Iterator<ChessLikeMove> iMoves = potentialMoves.iterator(); iMoves.hasNext();)
			{
				ChessMove move = (ChessMove) iMoves.next();

				if (looksLikePromotionMove(move, board))
				{
					iMoves.remove();
					for (Class<Piece> option : promotionOptions)
						promotionMoves.add(new ChessMove(move.getSource(), move.getTarget(), option));
				}
			}
			potentialMoves.addAll(promotionMoves);
		}

		return potentialMoves;
	}

	private Coordinate castle(final Board board, final Coordinate kingCoordinate, CastlingSide side, final Board initialBoard)
	{
		ChessBoardGeometry geometry = (ChessBoardGeometry) getBoardGeometry();
		final int pieceColor = board.getPiece(kingCoordinate).getColor();
		char rank = kingCoordinate.getNotation().charAt(1);

		// search for castling rook (on starting position for unambiguousness)
		final Coordinate rookCoordinate = lookForCastlingRook(kingCoordinate, side, initialBoard, pieceColor);

		// sanity check
		if (!board.getPiece(rookCoordinate).equals(getPieceSet().getPiece(Rook.class, pieceColor)))
			throw new IllegalStateException();

		// check for empty and unthreatened fields along path of king
		final boolean checkRule = getCheckVulnerablePiece() != null;
		Coordinate kingTargetCoordinate = geometry.locateCoordinate((side == CastlingSide.QUEENSIDE ? "c" : "g") + rank);
		if (geometry.walk(kingCoordinate, kingTargetCoordinate, geometry.isKingSide(kingCoordinate, kingTargetCoordinate) ? geometry.kingSideDir()
				: geometry.queenSideDir(), new OrthogonalBoardGeometry.CoordinateHook()
		{
			public boolean doNode(Coordinate coordinate)
			{
				if (!coordinate.equals(rookCoordinate) && !coordinate.equals(kingCoordinate) && board.getPiece(coordinate) != null)
					return false;
				if (checkRule && isCoordinateThreatened(board, coordinate, pieceColor))
					return false;
				return true;
			}
		}) != null)
			return null;

		// check for empty fields along path of rook
		Coordinate rookTargetCoordinate = geometry.locateCoordinate((side == CastlingSide.QUEENSIDE ? "d" : "f") + rank);
		if (geometry.walk(rookCoordinate, rookTargetCoordinate, geometry.isKingSide(rookCoordinate, rookTargetCoordinate) ? geometry.kingSideDir()
				: geometry.queenSideDir(), new OrthogonalBoardGeometry.CoordinateHook()
		{
			public boolean doNode(Coordinate coordinate)
			{
				if (!coordinate.equals(rookCoordinate) && !coordinate.equals(kingCoordinate) && board.getPiece(coordinate) != null)
					return false;
				return true;
			}
		}) != null)
			return null;

		return rookCoordinate;
	}

	private Coordinate lookForCastlingRook(Coordinate kingCoordinate, CastlingSide side, final Board board, int color)
	{
		ChessBoardGeometry geometry = (ChessBoardGeometry) getBoardGeometry();
		final Piece rook = getPieceSet().getPiece(Rook.class, color);
		char rank = kingCoordinate.getNotation().charAt(1);

		// shortcut for standard chess position
		Coordinate shortcutCoordinate = geometry.locateCoordinate((side == CastlingSide.QUEENSIDE ? "a" : "h") + rank);
		if (rook.equals(board.getPiece(shortcutCoordinate)))
			return shortcutCoordinate;

		// look
		Coordinate rookCoordinate = geometry.walk(kingCoordinate, side == CastlingSide.QUEENSIDE ? geometry.queenSideDir() : geometry.kingSideDir(),
				new OrthogonalBoardGeometry.CoordinateHook()
				{
					public boolean doNode(Coordinate coordinate)
					{
						Piece piece = board.getPiece(coordinate);
						if (rook.equals(piece))
							return false;
						return true;
					}
				});

		// sanity checks
		if (rookCoordinate == null)
			throw new IllegalStateException();

		return rookCoordinate;
	}

	@Override
	protected void vetoPotentialMoves(Set<ChessLikeMove> potentialMoves, CapturingGamePosition position, Board initialBoard)
	{
		Board board = position.getBoard();

		// enforce check rule
		removeKingInCheckPositions(potentialMoves, position, initialBoard);

		// enforce must capture rule
		if (mustCaptureRule)
		{
			boolean capturing = false;
			for (ChessLikeMove move : potentialMoves)
			{
				if (looksLikeCapture(move, board))
				{
					capturing = true;
					break;
				}
			}

			if (capturing)
			{
				for (Iterator<ChessLikeMove> iMoves = potentialMoves.iterator(); iMoves.hasNext();)
				{
					ChessLikeMove move = iMoves.next();
					if (!looksLikeCapture(move, board))
						iMoves.remove();
				}
			}
		}
	}

	@Override
	public String formatPosition(GamePosition position)
	{
		return ChessPositionFormat.format((OrthogonalBoardGeometry) getBoardGeometry(), getPieceSet(), (ChessLikePosition) position);
	}

	@Override
	public GamePosition parsePosition(String notation)
	{
		ChessLikePosition position = (ChessLikePosition) initialPositionFromBoard(null);
		ChessPositionFormat.parse(position, (OrthogonalBoardGeometry) getBoardGeometry(), getPieceSet(), notation);
		return position;
	}

	/**
	 * determines if the current active player can claim remis
	 */
	@Override
	public boolean canDrawBeClaimed(Game game)
	{
		return ((ChessLikePosition) game.getActualPosition()).getHalfmoveClock() >= 50 || super.canDrawBeClaimed(game);
	}

	@Override
	public List<MicroOperation> disassembleMove(GameMove move, GamePosition position, Board initialBoard) throws IllegalMoveException
	{
		ChessMove chessMove = (ChessMove) move;
		Coordinate source = chessMove.getSource();
		Coordinate target = chessMove.getTarget();
		Piece piece = position.getBoard().getPiece(source);
		PieceSet pieceSet = getPieceSet();

		final ChessBoardGeometry geometry = (ChessBoardGeometry) getBoardGeometry();
		final Board board = position.getBoard();
		final int activeColor = position.getActivePlayerIndex();

		// is there a piece on source square?
		assert board.getPiece(source) != null : "source square cannot be vacant: " + formatMove(move);

		assert board.getPiece(source).equals(piece) : "source square does not contain piece: " + formatMove(move);

		// is it of the right color?
		assert ((ChessLikePiece) board.getPiece(source)).getColor() == activeColor : "can only move your own pieces: " + formatMove(move);

		List<MicroOperation> ops = new LinkedList<MicroOperation>();

		boolean looksLikePlainCapture = looksLikePlainCapture(chessMove, board);
		boolean looksLikeEnPassantCapture = looksLikeEnPassantCapture(chessMove, board);

		// capture
		if (looksLikePlainCapture || looksLikeEnPassantCapture)
		{
			Coordinate capturedCoordinate;

			if (looksLikePlainCapture)
			{
				capturedCoordinate = target;
			}
			else
			// if (looksLikeEnPassantCapture)
			{
				// is en passant option available?
				assert target.equals(((ChessPosition) position).getEnPassantTargetSquare()) : "no en passant option available: "
						+ formatMove(chessMove);

				// valid pawn capture move?
				assert ((ChessLikePiece) piece).isThreateningSquare(geometry, board, source, target) : "not a valid pawn capture move: "
						+ formatMove(chessMove);

				// promotion attribute null?
				assert chessMove.getPromotionPiece() == null : "no promotion available: " + formatMove(chessMove);

				// captured piece
				capturedCoordinate = geometry.vectorAdd(target, geometry.backwardDir(piece.getColor()), 1);

				// asserts
				ChessLikePiece capturedPiece = (ChessLikePiece) board.getPiece(capturedCoordinate);
				assert capturedPiece != null;
				assert capturedPiece instanceof Pawn;
				assert !capturedPiece.sameColor(piece);
			}

			ChessLikePiece capturedPiece = (ChessLikePiece) board.getPiece(capturedCoordinate);
			int capturedPieceColor = capturedPiece.getColor();

			// capture
			ops.add(new CapturePieceOperation(capturedCoordinate));

			if (castlingRule)
			{
				// forfeit opponent castling if king is captured (for games with check rule)
				if (capturedPiece instanceof King && isInHomeArea(initialBoard, target, capturedPiece))
				{
					if (((ChessPosition) position).getCastlingAvailable(capturedPieceColor, CastlingSide.KINGSIDE) == true)
						ops.add(new ForfeitCastlingOperation(capturedPieceColor, CastlingSide.KINGSIDE));
					if (((ChessPosition) position).getCastlingAvailable(capturedPieceColor, CastlingSide.QUEENSIDE) == true)
						ops.add(new ForfeitCastlingOperation(capturedPieceColor, CastlingSide.QUEENSIDE));
				}

				// forfeit opponent castling if rook is captured
				if (capturedPiece instanceof Rook && isInHomeArea(initialBoard, target, capturedPiece))
				{
					Coordinate kingCoords = initialBoard.locatePiece(pieceSet.getPiece(King.class, capturedPieceColor));
					if (geometry.isKingSide(kingCoords, capturedCoordinate))
					{
						if (((ChessPosition) position).getCastlingAvailable(capturedPieceColor, CastlingSide.KINGSIDE) == true)
							ops.add(new ForfeitCastlingOperation(capturedPieceColor, CastlingSide.KINGSIDE));
					}
					else
					{
						if (((ChessPosition) position).getCastlingAvailable(capturedPieceColor, CastlingSide.QUEENSIDE) == true)
							ops.add(new ForfeitCastlingOperation(capturedPieceColor, CastlingSide.QUEENSIDE));
					}
				}
			}
		}

		// move piece(s)
		if (!looksLikeCastlingMove(chessMove, board))
		{
			// normal move
			ops.add(new MovePieceOperation(source, target));
		}
		else
		{
			// castling move
			char rank = source.getNotation().charAt(1);
			Coordinate kingTarget, rookTarget;
			if (geometry.isKingSide(source, target))
			{
				kingTarget = geometry.locateCoordinate("g" + rank);
				rookTarget = geometry.locateCoordinate("f" + rank);
			}
			else
			{
				kingTarget = geometry.locateCoordinate("c" + rank);
				rookTarget = geometry.locateCoordinate("d" + rank);
			}

			// move king and rook
			if (source.equals(rookTarget) && target.equals(kingTarget))
			{
				// exchange
				ops.add(new ExchangePieceOperation(source, target));
			}
			else if (source.equals(kingTarget))
			{
				// move rook only
				ops.add(new MovePieceOperation(target, rookTarget));
			}
			else if (target.equals(rookTarget))
			{
				// move king only
				ops.add(new MovePieceOperation(source, kingTarget));
			}
			else if (kingTarget.equals(target))
			{
				// move rook first
				ops.add(new MovePieceOperation(target, rookTarget));
				ops.add(new MovePieceOperation(source, kingTarget));
			}
			else
			{
				// move king first
				ops.add(new MovePieceOperation(source, kingTarget));
				ops.add(new MovePieceOperation(target, rookTarget));
			}
		}

		// promotion
		if (chessMove.getPromotionPiece() != null) // is promotion move
		{
			assert promotionRule : "there is no promotion in this rule";
			assert looksLikePromotionMove(chessMove, board) : "is not eligable for promotion: " + formatMove(chessMove);

			// promote pawn
			ops.add(new SwapOutPieceOperation(target, pieceSet.getPiece(chessMove.getPromotionPiece(), activeColor)));
		}
		else
		{
			assert !promotionRule || !looksLikePromotionMove(chessMove, board) : "is eligable for promotion, but promotion piece missing: "
					+ formatMove(chessMove);
		}

		// adjust half move clock
		if (looksLikePlainCapture || looksLikeEnPassantCapture || piece instanceof Pawn)
			ops.add(new ResetHalfmoveClockOperation());
		else
			ops.add(IncrementHalfmoveClockOperation.instance());

		if (castlingRule)
		{
			// forfeit all castling if the king is moving
			if (piece instanceof King)
			{
				if (((ChessPosition) position).getCastlingAvailable(activeColor, CastlingSide.QUEENSIDE) == true)
					ops.add(new ForfeitCastlingOperation(activeColor, CastlingSide.QUEENSIDE));
				if (((ChessPosition) position).getCastlingAvailable(activeColor, CastlingSide.KINGSIDE) == true)
					ops.add(new ForfeitCastlingOperation(activeColor, CastlingSide.KINGSIDE));
			}

			// forfeit castling if a rook is moving from its initial position
			if (piece instanceof Rook && isInHomeArea(initialBoard, source, piece))
			{
				Coordinate kingCoords = initialBoard.locatePiece(pieceSet.getPiece(King.class, activeColor));
				if (geometry.isKingSide(kingCoords, source))
				{
					if (((ChessPosition) position).getCastlingAvailable(activeColor, CastlingSide.KINGSIDE) == true)
						ops.add(new ForfeitCastlingOperation(activeColor, CastlingSide.KINGSIDE));
				}
				else
				{
					if (((ChessPosition) position).getCastlingAvailable(activeColor, CastlingSide.QUEENSIDE) == true)
						ops.add(new ForfeitCastlingOperation(activeColor, CastlingSide.QUEENSIDE));
				}
			}
		}

		if (antiKingCastlingRule)
		{
			// forfeit castling if king or antiking is moving
			if (piece instanceof King || piece instanceof AntiKing)
			{
				if (((AntiKingChessPosition) position).getCastlingAvailable(activeColor, piece.getClass()) == true)
					ops.add(new ForfeitAntiKingCastlingOperation(activeColor, piece.getClass()));
			}
		}

		// adjust en passant option
		if (enPassantRule)
		{
			if (looksLikeEnPassantMove(chessMove, board))
				ops.add(new SetEnPassantTargetSquareOperation(geometry.vectorAdd(source, geometry.forwardDir(piece.getColor()), 1)));
			else if (((ChessPosition) position).getEnPassantTargetSquare() != null)
				ops.add(new SetEnPassantTargetSquareOperation(null));
		}

		// next player
		ops.add(NextPlayerOperation.instance());

		return ops;
	}

	@Override
	protected final boolean looksLikePlainCapture(ChessLikeMove move, Board board)
	{
		if (looksLikeCastlingMove(move, board))
			return false;

		return super.looksLikePlainCapture(move, board);
	}

	@Override
	protected final boolean looksLikeCapture(ChessLikeMove move, Board board)
	{
		return looksLikePlainCapture(move, board) || (enPassantRule && looksLikeEnPassantCapture(move, board));
	}

	protected boolean looksLikeEnPassantCapture(ChessLikeMove move, Board board)
	{
		ChessBoardGeometry geometry = (ChessBoardGeometry) getBoardGeometry();
		Coordinate source = move.getSource();
		Coordinate target = move.getTarget();
		Piece piece = board.getPiece(source);

		// piece is a pawn?
		if (!(piece instanceof Pawn))
			return false;

		// pawn changing file?
		if (geometry.sameFile(source, target))
			return false;

		// does not look like plain capture?
		if (looksLikePlainCapture(move, board))
			return false;

		return true;
	}

	/**
	 * Determines if this move is a casteling move by the nature of the chessMove.getPiece() and target squares. It does
	 * not check if any of the casteling conditions are fulfilled.
	 */
	protected boolean looksLikeCastlingMove(ChessLikeMove move, Board board)
	{
		ChessBoardGeometry geometry = (ChessBoardGeometry) getBoardGeometry();

		// source is a king?
		Coordinate source = move.getSource();
		Piece sourcePiece = board.getPiece(source);
		if (!(sourcePiece instanceof King))
			return false;

		// target on same rank?
		Coordinate target = move.getTarget();
		if (!geometry.sameRank(source, target))
			return false;

		// target is a rook of same color
		Piece targetPiece = board.getPiece(target);
		if (!(targetPiece instanceof Rook))
			return false;
		if (!targetPiece.sameColor(sourcePiece))
			return false;

		return true;
	}

	/**
	 * Determines if the move is opening the option of en passant
	 */
	protected boolean looksLikeEnPassantMove(ChessMove move, Board board)
	{
		ChessBoardGeometry geometry = (ChessBoardGeometry) getBoardGeometry();
		Coordinate source = move.getSource();
		Coordinate target = move.getTarget();
		Piece piece = board.getPiece(source);

		// piece is pawn?
		if (!(piece instanceof Pawn))
			return false;

		// same file?
		if (!geometry.sameFile(source, target))
			return false;

		// distance 2 squares?
		if (geometry.distance(source, target) != 2)
			return false;

		return true;
	}

	/**
	 * Determines if the move on the given board leads to a pawn promotion.
	 */
	protected boolean looksLikePromotionMove(ChessMove move, Board board)
	{
		ChessBoardGeometry geometry = (ChessBoardGeometry) getBoardGeometry();
		Coordinate source = move.getSource();
		Coordinate target = move.getTarget();
		Piece piece = board.getPiece(source);

		// piece is pawn?
		if (!(piece instanceof Pawn))
			return false;

		// is arriving at target rank?
		if (!geometry.isInTargetArea(target, piece.getColor()))
			return false;

		return true;
	}

	/**
	 * Determines if the piece is in its home area.
	 */
	private boolean isInHomeArea(Board initialBoard, Coordinate coordinate, Piece piece)
	{
		return piece.equals(initialBoard.getPiece(coordinate));
	}
}
