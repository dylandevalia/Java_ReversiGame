package game.reversi;

import game.reversi.counters.Disc;

import java.util.concurrent.ThreadLocalRandom;

public class AI
{
	/**
	 * The main game object
	 */
	private Game game;

	/**
	 * Constructor
	 *
	 * @param game The main game object
	 */
	public AI(Game game)
	{
		this.game = game;
	}

	/**
	 * Searches all squares and determines the square that will yield the most
	 * flipped counters and makes the move
	 *
	 * @param color The opponent's colour
	 * @param board The 2D array of counters (game board)
	 *
	 * @return True - Move was made | False - No possible moves
	 */
	public boolean makeGreedyMove(String color, Disc[][] board)
	{
		int bestNoFlipped = -1;
		int bestRow = -1;
		int bestCol = -1;
		int tmpNoFlipped;

		for (int column = 0; column < 8; column++)
		{
			for (int row = 0; row < 8; row++)
			{
				tmpNoFlipped = checkArea(row, column, color, board, true);
				/** If tmp is better than best or if is the same then flip a coin whether to use the new value */
				if ((tmpNoFlipped > bestNoFlipped)
						    || (tmpNoFlipped == bestNoFlipped && ThreadLocalRandom.current().nextInt(0, 2) == 0)
						)
				{
					bestNoFlipped = tmpNoFlipped;
					bestRow = row;
					bestCol = column;
				}
			}
		}

		if (bestNoFlipped > 0)
		{
			checkArea(bestRow, bestCol, color, board, false);
			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * Checks the board if there is a possible move
	 *
	 * @param color The opponent's colour
	 * @param board The 2D array of counters (game board)
	 *
	 * @return True - There is a possible move | False - No playable moves
	 */
	public boolean checkBoard(String color, Disc[][] board)
	{
		for (int column = 0; column < 8; column++)
		{
			for (int row = 0; row < 8; row++)
			{
				if (checkArea(row, column, color, board, true) > 0)
				{
					game.printDebug("Moves: " + true);
					return true;
				}
			}
		}

		game.printDebug("Moves: " + false);
		/** If no possible moves */
		return false;
	}

	/**
	 * Checks the surrounding area for at least one possible move
	 *
	 * @param currentRow The current row position of selected square
	 * @param currentCol The current column position of the selected square
	 * @param color      The opponent's colour
	 * @param board      The 2D array of disc
	 * @param toCount    If the function should count number of flipped counters or make a move
	 *
	 * @return The number of tiles that can be gained from this position
	 */
	public int checkArea(int currentRow, int currentCol,
	                     String color, Disc[][] board,
	                     boolean toCount)
	{
		int noOfTiles = 0;

		/** If chosen space is empty */
		if (board[currentRow][currentCol].getState().equals("empty"))
		{
			/** From -1 to 1 on y-axis */
			for (int row = -1; row < 2; row++)
			{
				/** If checking square is out of bounds */
				if (!(currentCol + row < 0 || currentCol + row > 7))
				{
					/** From -1 to 1 on x-axis */
					for (int column = -1; column < 2; column++)
					{
						/** If checking square is out of bounds */
						if (!(currentRow + column < 0 || currentRow + column > 7))
						{
							/** If disc of color is found */
							if (board[currentRow + column][currentCol + row].getState().equals(color))
							{
								game.printDebug(
										"Found possible move x y: " + (currentRow + column) + " " + (currentCol + row));

								/** Count number of counters that can be flipped */
								noOfTiles += checkDirection(currentRow, currentCol, color, board, row, column, toCount);
							}
						}
					}
				}
			}
		}
		return noOfTiles;
	}

	/**
	 * Given a unit vector and a initial position, continues in the direction to see if there is a valid move
	 *
	 * @param currentRow The selected row
	 * @param currentCol The selected column
	 * @param color      The opponent's colour
	 * @param board      The 2D array of counters (game board)
	 * @param rowDirUnit The row unit direction
	 * @param colDirUnit The column unit direction
	 * @param toCount    Should the function count the number of flipped counters or make the move
	 *
	 * @return The number of counters that can be flipped
	 */
	public int checkDirection(int currentRow, int currentCol, String color, Disc[][] board,
	                          int rowDirUnit, int colDirUnit,
	                          boolean toCount)
	{
		int noOfTiles = 0;

		int colDir = colDirUnit;
		int rowDir = rowDirUnit;

		/** While in board bounds */
		while (!(currentRow + colDir < 0 || currentRow + colDir > 7) &&
				       !(currentCol + rowDir < 0 || currentCol + rowDir > 7))
		{
			if (board[currentRow + colDir][currentCol + rowDir].getState().equals(color)) // If opponent colour
			{
				colDir += colDirUnit;
				rowDir += rowDirUnit;
			} else if (board[currentRow + colDir][currentCol + rowDir].getState().equals("empty")) // If empty
			{
				game.printDebug("Invalid search path - empty square\n");
				return 0;
			} else // If player colour
			{
				/** Found a valid move */
				game.printDebug("Found valid search path\n");

				if (!toCount)
				{
					/** Sets current square to player's colour */
					if (color.equals("white"))
					{
						board[currentRow][currentCol].setState("black");
					} else if (color.equals("black"))
					{
						board[currentRow][currentCol].setState("white");
					} else
					{
						throw new IllegalArgumentException("Unknown color: " + color);
					}
				}

				do
				{
					// Go backwards
					colDir -= colDirUnit;
					rowDir -= rowDirUnit;

					/** Count the number of counters that can be flipped */
					noOfTiles++;

					if (!toCount)
					{
						/** Flip counters */
						board[currentRow + colDir][currentCol + rowDir].setState(
								board[currentRow][currentCol].getState());
						noOfTiles = 1;
					}
				} while (currentRow + colDir != currentRow || currentCol + rowDir != currentCol);

				return noOfTiles;
			}
		}
		game.printDebug("Invalid search path - out of bounds\n");
		return 0;
	}
}
