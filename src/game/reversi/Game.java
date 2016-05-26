package game.reversi;

import game.reversi.counters.Disc;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// TODO Reset game
// TODO Timer?
// TODO Customisation
// TODO Change size of board?
// TODO AI difficulty

public class Game
{
	/**
	 * Show debug messages
	 */
	private final boolean showDebug = false;

	/**
	 * Show the current
	 */
	public volatile int currentRow;
	public volatile int currentCol;
	public volatile int currentBoard;
	public int player;
	public boolean gameWon;

	/**
	 * Discs array
	 */
	private Disc[][] p1_discs;
	private Disc[][] p2_discs;

	/**
	 * Label
	 */
	private JLabel p1_label_player;
	private JLabel p2_label_player;
	private JLabel p1_label_score;
	private JLabel p2_label_score;

	/**
	 * AI
	 */
	private AI ai;

	/**
	 * Initialises the global variables
	 *
	 * @param p1_discs        The 2D array of counters for player one
	 * @param p1_label_player The label which displays player one's turn
	 * @param p1_label_score  The label which displays the score for player one
	 * @param p2_discs        The 2D array of counters for player two
	 * @param p2_label_player The label which displays player two's turn
	 * @param p2_label_score  The label which displays the score for player two
	 */
	public void initialise(
			                      final Disc p1_discs[][], JLabel p1_label_player, JLabel p1_label_score,
			                      JButton p1_button_ai,
			                      final Disc p2_discs[][], JLabel p2_label_player, JLabel p2_label_score,
			                      JButton p2_button_ai)
	{
		this.p1_discs = p1_discs;
		this.p1_label_player = p1_label_player;
		this.p1_label_score = p1_label_score;

		this.p2_discs = p2_discs;
		this.p2_label_player = p2_label_player;
		this.p2_label_score = p2_label_score;

		/** Buttons */
		p1_button_ai.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				printDebug("Button 1 pressed");
				if (player == 1)
				{
					if (ai.makeGreedyMove("white", p1_discs))
					{
						changePlayer();
					} else
					{
						printDebug("No possible moves");
					}
				}
			}
		});
		p2_button_ai.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				printDebug("Button 2 pressed");
				if (player == 2)
				{
					if (ai.makeGreedyMove("black", p2_discs))
					{
						changePlayer();
					} else
					{
						printDebug("No possible moves");
					}
				}
			}
		});

		/** Creates an ai object */
		this.ai = new AI(this);
	}

	/**
	 * Resets the game board
	 */
	public void resetGame()
	{
		/** Sets the default values of variables */
		currentRow = -1;
		currentCol = -1;
		currentBoard = -1;
		/** Set player one goes first */
		player = 1;

		/** Set all counters to empty for player one */
		for (int column = 0; column < 8; column++)
		{
			for (int row = 0; row < 8; row++)
			{
				p1_discs[row][column].setState("empty");
			}
		}

		/** Sets up initial four counters for player one */
		p1_discs[3][3].setState("white");
		p1_discs[4][3].setState("black");
		p1_discs[3][4].setState("black");
		p1_discs[4][4].setState("white");

		/** Syncs player two's board with player one's */
		p2_discs = syncBoards(p1_discs, p2_discs);

		/** Sets labels */
		p1_label_player.setText("Your Turn (Black)");
		p2_label_player.setText("Not Your Turn");
		updateScoreLabel(p1_label_score);
		updateScoreLabel(p2_label_score);

		/** Check board for possible moves */
		ai.checkBoard("white", p1_discs);
	}

	/**
	 * Gets the coords of the square recently pressed
	 *
	 * @param row row value
	 * @param col column value
	 */
	public void makeMove(int row, int col, int boardNum)
	{
		currentRow = row;
		currentCol = col;
		currentBoard = boardNum;
		printDebug("current row col board: " + currentRow + " " + currentCol + " " + currentBoard);

		/** Check which player's turn */
		if (player == 1 && currentBoard == 1) // Player 1  Black
		{
			printDebug("Player 1");
			/** If there is a black disc around the selected square */
			if (ai.checkArea(currentRow, currentCol, "white", p1_discs, false) != 0)
			{
				printDebug("goob");
				changePlayer();
			}
		} else if (player == 2 && currentBoard == 2) // Player 2  White
		{
			printDebug("Player 2");
			/** If there is a white disc around the selected square */
			if (ai.checkArea(currentRow, currentCol, "black", p2_discs, false) != 0)
			{
				changePlayer();
			}
		}
	}

	/**
	 * Runs if the game has been won
	 */
	public void gameIsWon()
	{
		/** Game has ended */
		System.out.println("Won");
		if (countBlack(p1_discs) > countWhite(p1_discs))
		{
			/** Player black has won */
			p1_label_player.setText("YOU WON!");
			p2_label_player.setText("You Lost");
			/** Message box to show winner */
			JOptionPane.showMessageDialog(null, "Black Won!", "Winner", JOptionPane.INFORMATION_MESSAGE);
		} else if (countBlack(p1_discs) < countWhite(p1_discs))
		{
			/** Player white has won */
			p1_label_player.setText("You Lost");
			p2_label_player.setText("YOU WON!");
			/** Message box to show winner */
			JOptionPane.showMessageDialog(null, "White Won!", "Winner", JOptionPane.INFORMATION_MESSAGE);
		} else
		{
			/** The game is a tie */
			p1_label_player.setText("YOU TIED!");
			p2_label_player.setText("YOU TIED!");
			/** Message box to show tie */
			JOptionPane.showMessageDialog(null, "The Game is Tied!", "Winner", JOptionPane.INFORMATION_MESSAGE);
		}

		printDebug("WON!");
	}

	/**
	 * Synchronises the boards by copying boardOne's states to boardTwo
	 *
	 * @param boardOne The master board
	 * @param boardTwo The slave board
	 *
	 * @return returns the altered boardTwo
	 */
	public synchronized Disc[][] syncBoards(Disc boardOne[][], Disc boardTwo[][])
	{
		for (int column = 0; column < 8; column++)
		{
			for (int row = 0; row < 8; row++)
			{
				boardTwo[row][column].setState(boardOne[row][column].getState());
			}
		}
		return boardTwo;
	}

	/**
	 * Changes the player, the labels on each window and syncs the boards
	 */
	public synchronized void changePlayer()
	{
		if (player == 1) // Player one
		{
			ai.resetPotentialTiles(p1_discs);
			p2_discs = syncBoards(p1_discs, p2_discs);
			player = 2;

			p1_label_player.setText("Not Your Turn");
			p2_label_player.setText("Your Turn (White)");

			/** Check if there are any possible white moves */
			gameWon = !ai.checkBoard("black", p2_discs);
		} else // Player two
		{
			ai.resetPotentialTiles(p2_discs);
			p1_discs = syncBoards(p2_discs, p1_discs);
			player = 1;

			p1_label_player.setText("Your Turn (Black)");
			p2_label_player.setText("Not Your Turn");

			/** Check if there are any possible black moves */
			gameWon = !ai.checkBoard("white", p1_discs);
		}

		updateScoreLabel(p1_label_score);
		updateScoreLabel(p2_label_score);

		if (gameWon)
		{
			gameIsWon();
		}
	}

	/**
	 * Updates the scores label
	 */
	public void updateScoreLabel(JLabel score)
	{
		score.setText("Black = " + countBlack(p1_discs) + " | " + countWhite(p1_discs) + " = White");
	}

	/**
	 * Counts the number of white iscs on the board
	 *
	 * @return Number of white counters
	 */
	public synchronized int countWhite(Disc board[][])
	{
		int number = 0;
		for (int column = 0; column < 8; column++)
		{
			for (int row = 0; row < 8; row++)
			{
				if (board[row][column].getState().equals("white"))
				{
					number++;
				}
			}
		}
		return number;
	}

	/**
	 * Count the number of black counters on the board
	 *
	 * @return The number of black counters
	 */
	public synchronized int countBlack(Disc board[][])
	{
		int number = 0;
		for (int column = 0; column < 8; column++)
		{
			for (int row = 0; row < 8; row++)
			{
				if (board[row][column].getState().equals("black"))
				{
					number++;
				}
			}
		}
		return number;
	}

	/**
	 * Gets the current player
	 *
	 * @return Current player
	 */
	public int getPlayer()
	{
		return player;
	}

	/** Helper functions */

	/**
	 * Sleeps for a specified number of ms
	 *
	 * @param milliseconds Number of ms to let the program sleep for
	 */
	public void sleep(int milliseconds)
	{
		try
		{
			Thread.sleep(milliseconds);
		} catch (InterruptedException e)
		{
			printDebug("Sleep failed");
			e.printStackTrace();
		}
	}

	/**
	 * Prints debug to console
	 *
	 * @param debug String to be printed
	 */
	public void printDebug(String debug)
	{
		if (showDebug)
		{
			System.out.println(debug);
		}
	}
}
