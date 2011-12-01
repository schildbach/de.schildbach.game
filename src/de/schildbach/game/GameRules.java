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
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.schildbach.game.common.FenFormat;
import de.schildbach.game.common.OrthogonalBoardGeometry;
import de.schildbach.game.exception.IllegalMoveException;
import de.schildbach.game.exception.ParseException;

/**
 * <ul>
 * <li>knows about its base rules and variants</li>
 * <li>piece specific rules are delegated to the piece</li>
 * <li>knows about the usual initial position</li>
 * </ul>
 * 
 * @author Andreas Schildbach
 */
public abstract class GameRules
{
	public static int instanceCount;

	protected static final Log LOG = LogFactory.getLog(GameRules.class);

	transient private BoardGeometry boardGeometry;
	transient private PieceSet pieceSet;

	/** constructor just for serialization */
	protected GameRules()
	{
		instanceCount++;
	}

	protected GameRules(BoardGeometry boardGeometry, PieceSet pieceSet)
	{
		this();

		if (boardGeometry == null)
			throw new IllegalArgumentException("boardGeometry must not be null");
		if (pieceSet == null)
			throw new IllegalArgumentException("pieceSet must not be null");

		this.boardGeometry = boardGeometry;
		this.pieceSet = pieceSet;
	}

	public int getNumberOfPlayers()
	{
		return 2;
	}

	public final BoardGeometry getBoardGeometry()
	{
		return boardGeometry;
	}

	public final PieceSet getPieceSet()
	{
		return pieceSet;
	}

	public final Game newGame(String initialBoardNotation)
	{
		Board initialBoard = null;
		if (initialBoardNotation != null)
		{
			initialBoard = getBoardGeometry().newBoard();
			parseBoard(initialBoard, initialBoardNotation);
		}

		return new Game(this, initialPositionFromBoard(initialBoard));
	}

	public final Game newGame(String initialBoardNotation, String historyNotation, Locale locale)
	{
		Game game = newGame(initialBoardNotation);

		String remainingMoves = executeMoves(game, historyNotation, locale);
		if (remainingMoves != null)
			throw new ParseException(historyNotation, "could not parse first move of: " + remainingMoves);

		return game;
	}

	public abstract GamePosition initialPositionFromBoard(Board board);

	public final GameMove parseMove(String moveString, Locale locale, Game game) throws ParseException
	{
		return parseMove(moveString, locale, game.getActualPosition(), game.getInitialPosition().getBoard());
	}

	/**
	 * This method parses a game move, using the common text format of the game rules. It is guaranteed that it only
	 * returns moves that are allowed in the given game position.
	 * 
	 * @param moveString
	 *            text to be parsed
	 * @param locale
	 *            localization of the text to be parsed
	 * @param position
	 *            relevant game position for allowed moves
	 * @return parsed game move
	 * @throws ParseException
	 */
	public abstract GameMove parseMove(String notation, Locale locale, GamePosition position, Board initialBoard) throws ParseException;

	protected abstract GameMove unmarshalMove(String notation) throws ParseException;

	public final Collection<? extends GameMove> allowedMoves(Game game)
	{
		return allowedMoves(game.getActualPosition(), game.getInitialPosition().getBoard());
	}

	/**
	 * This method returns all moves that are allowed in a given game position taking in account the game rules this
	 * object represents.
	 * 
	 * The position may not be altered by implementations of this method.
	 * 
	 * The caller of this method is free to modify the returned set, however the game moves that are contained in the
	 * set may not be altered.
	 * 
	 * @param position
	 *            the game position
	 * @return a collection of all moves allowed
	 */
	public abstract Collection<? extends GameMove> allowedMoves(GamePosition position, Board initialBoard);

	public final String executeMoves(Game game, String moveSequence, Locale locale)
	{
		if (moveSequence == null)
			return null; // ok

		String[] moves = moveSequence.split("\\s");
		try
		{
			for (int i = 0; i < moves.length; i++)
			{
				if (moves[i].length() > 0)
				{
					if (moves[i].endsWith("."))
					{
						// parse move number
						int fullmoveNumber = Integer.parseInt(moves[i].substring(0, moves[i].indexOf(".")));

						// verify move number
						if (fullmoveNumber != game.getActualPosition().getFullmoveNumber())
							throw new ParseException(moves[i], "expecting fullmove number " + game.getActualPosition().getFullmoveNumber());
					}
					else
					{
						GameMove move = this.parseMove(moves[i], locale, game);
						executeMove(game, move);

						if (LOG.isDebugEnabled())
							LOG.debug(moves[i] + " => " + game.getActualPosition());
					}
				}

				moves[i] = null;
			}

			// everything ok
			return null;
		}
		catch (ParseException x)
		{
			LOG.debug("caught exception", x);

			// return remaining moves
			StringBuilder remainingMoveSequence = new StringBuilder();
			for (String move : moves)
			{
				if (move != null && move.length() > 0)
					remainingMoveSequence.append(move).append(" ");
			}
			return remainingMoveSequence.toString().trim();
		}
	}

	public final Game unmarshal(String initialBoardNotation, String notation)
	{
		Game game = newGame(initialBoardNotation);

		if (notation.length() > 0)
			for (String moveNotation : notation.split(" "))
				executeMove(game, unmarshalMove(moveNotation));

		return game;
	}

	public final void executeMove(Game game, GameMove move)
	{
		GamePosition position = (GamePosition) game.getActualPosition().clone();
		List<MicroOperation> ops = disassembleMove(move, position, game.getInitialPosition().getBoard());
		doOperations(ops, position);

		game.addMove(move, position);
	}

	public final void undoLastMove(Game game)
	{
		if (game.isEmpty())
			throw new IllegalStateException("no move to undo");

		game.removeLastMove();
	}

	public abstract List<MicroOperation> disassembleMove(GameMove move, GamePosition position, Board initialBoard) throws IllegalMoveException;

	public final void doOperations(List<MicroOperation> ops, GamePosition position)
	{
		for (MicroOperation op : ops)
			op.doOperation(position);
	}

	public final void undoOperations(List<MicroOperation> ops, GamePosition position)
	{
		for (ListIterator<MicroOperation> i = ops.listIterator(ops.size()); i.hasPrevious();)
			i.previous().undoOperation(position);
	}

	public final void rewind(Game game, int moveIndex)
	{
		while (game.getSize() > moveIndex)
			undoLastMove(game);
	}

	public String formatBoard(Board board)
	{
		return FenFormat.format((OrthogonalBoardGeometry) getBoardGeometry(), pieceSet, board);
	}

	public abstract String formatPosition(GamePosition position);

	public abstract GamePosition parsePosition(String notation);

	public final String formatGame(Game game, Locale locale)
	{
		return formatGame(game, 1, 0, locale);
	}

	public final String marshal(Game game)
	{
		if (game.isEmpty())
			return "";

		int size = game.getSize();
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < size; i++)
		{
			builder.append(formatMove(game.getMove(i)));
			builder.append(" ");
		}

		builder.setLength(builder.length() - 1);
		return builder.toString();
	}

	public abstract String formatMove(GameMove move);

	protected String[] formatMoves(Collection<? extends GameMove> moves)
	{
		String[] notations = new String[moves.size()];
		int i = 0;
		for (GameMove move : moves)
			notations[i++] = formatMove(move);
		return notations;
	}

	public abstract Set<String> clickables(GameMove move);

	public final String formatGame(Game game, int fullMoveNumber, int firstPlayerIndex, Locale locale)
	{
		StringBuilder notation = new StringBuilder();
		FormatGameArrayElement[] notationArray = formatGameArray(game, fullMoveNumber, firstPlayerIndex, locale);

		for (int i = 0; i < notationArray.length; i++)
		{
			notation.append(notationArray[i].getNotation());
			notation.append(" ");
		}

		if (notation.length() > 0)
			notation.deleteCharAt(notation.length() - 1);

		return notation.toString();
	}

	public final FormatGameArrayElement[] formatGameArray(Game game, Locale locale)
	{
		return formatGameArray(game, 1, 0, locale);
	}

	public final FormatGameArrayElement[] formatGameArray(Game game, int fullmoveNumber, int firstPlayerIndex, Locale locale)
	{
		final int NUM_PLAYERS = 2;

		String[] basicArray = basicFormatGameArray(game, locale);
		List<FormatGameArrayElement> notationArray = new LinkedList<FormatGameArrayElement>();

		int index = (fullmoveNumber - 1) * NUM_PLAYERS + firstPlayerIndex;

		if (firstPlayerIndex > 0)
		{
			notationArray.add(new FormatGameArrayElement("" + fullmoveNumber + "..."));
		}

		while (index < basicArray.length)
		{
			if (index % NUM_PLAYERS == 0)
			{
				notationArray.add(new FormatGameArrayElement("" + (index / NUM_PLAYERS + 1) + "."));
			}

			notationArray.add(new FormatGameArrayElement(index, index % NUM_PLAYERS, basicArray[index++]));
		}

		return notationArray.toArray(new FormatGameArrayElement[0]);
	}

	protected String[] basicFormatGameArray(Game game, Locale locale)
	{
		int size = game.getSize();
		String[] array = new String[size];

		for (int i = 0; i < size; i++)
			array[i] = formatMove(game.getMove(i));

		return array;
	}

	public static class FormatGameArrayElement
	{
		public Integer index;
		public Integer playerIndex;
		public String notation;

		public FormatGameArrayElement(String label)
		{
			this.index = null;
			this.playerIndex = null;
			this.notation = label;
		}

		public FormatGameArrayElement(int index, int playerIndex, String notation)
		{
			this.index = index;
			this.playerIndex = playerIndex;
			this.notation = notation;
		}

		public boolean isLabel()
		{
			return index == null;
		}

		public Integer getIndex()
		{
			return index;
		}

		public Integer getPlayerIndex()
		{
			return playerIndex;
		}

		public String getNotation()
		{
			return notation;
		}

		@Override
		public boolean equals(Object obj)
		{
			FormatGameArrayElement other = (FormatGameArrayElement) obj;
			return ObjectUtils.equals(this.index, other.index) && ObjectUtils.equals(this.notation, other.notation);
		}

		@Override
		public int hashCode()
		{
			return ObjectUtils.hashCode(index) * 37 + ObjectUtils.hashCode(notation);
		}
	}

	public void parseBoard(Board board, String notation)
	{
		FenFormat.parse((OrthogonalBoardGeometry) getBoardGeometry(), getPieceSet(), notation, board);
	}

	public final boolean isFinished(Game game)
	{
		return isFinished(game.getActualPosition(), game.getInitialPosition().getBoard());
	}

	public abstract boolean isFinished(GamePosition position, Board initialBoard);

	public final float[] points(Game game)
	{
		return points(game.getActualPosition(), game.getInitialPosition().getBoard());
	}

	public abstract float[] points(GamePosition position, Board initialBoard);

	public final float[] pointsForDraw()
	{
		return new float[] { 0.5f, 0.5f };
	}

	public final float[] pointsForWin(int position)
	{
		float[] points = new float[] { 0f, 0f };
		points[position] = 1f;
		return points;
	}

	public boolean canDrawBeClaimed(Game game)
	{
		return countContainsBoard(game, game.getActualPosition().getBoard()) >= 3;
	}

	protected int countContainsBoard(Game game, Board board)
	{
		int count = board.equals(game.getInitialPosition().getBoard()) ? 1 : 0;
		int size = game.getSize();
		for (int i = 0; i < size; i++)
		{
			if (board.equals(game.getPosition(i).getBoard()))
				count++;
		}
		return count;
	}
}
