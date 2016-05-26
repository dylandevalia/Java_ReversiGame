package game.reversi.counters;

import game.reversi.Game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Disc extends CircleButton
{
	/**
	 * Which current mode it is in
	 * 0 - nothing
	 * 1 - white
	 * 2 - black
	 * 3 - potential black
	 * 4 - potential white
	 */
	protected volatile int state;
	/**
	 *
	 */
	/**
	 * Co-ords of the disc
	 */
	public final int rowValue;
	public final int colValue;
	/**
	 * Which board is the disc on
	 */
	public final int boardNum;
	/**
	 * Game object
	 */
	private final Game game;

	/**
	 * Constructor to set up object
	 *
	 * @param row      Disc's row position
	 * @param col      Disc's col position
	 * @param boardNum Which board the disc is on
	 * @param game     Main game object
	 */
	public Disc(int row, int col, int boardNum, Game game)
	{
		super(100, 100,
				new Color(223, 239, 255), 5, new Color(161, 175, 191),
				null, 0, null);
		this.rowValue = row;
		this.colValue = col;
		this.boardNum = boardNum;
		this.game = game;

		setState("empty");
		this.addMouseListener(new MouseHandler());
	}

	/**
	 * Gets the current state of the disc
	 *
	 * @return The current state
	 */
	public synchronized String getState()
	{
		switch (state)
		{
			case 0:
				return "empty";
			case 1:
				return "white";
			case 2:
				return "black";
			case 3:
				return "pBlack";
			case 4:
				return "pWhite";
			default:
				return "";
		}

		//return state;
	}

	/**
	 * Sets the state of the current disc
	 *
	 * @param newState The new state
	 */
	public synchronized void setState(String newState)
	{
		switch (newState)
		{
			case "empty":
				state = 0;
				setEmpty();
				break;
			case "white":
				state = 1;
				setWhite();
				break;
			case "black":
				state = 2;
				setBlack();
				break;
			case "pBlack":
				state = 3;
				setEmpty();
				break;
			case "pWhite":
				state = 4;
				setEmpty();
				break;
			default:
				throw new IllegalArgumentException("Unknown 'state': " + newState);
		}
	}

	/**
	 * Sets the current disc to empty
	 */
	private void setEmpty()
	{
		setCircleColor(null);
		setCircleBorderWidth(0);
		setCircleBorderColor(null);
	}

	/**
	 * Sets the current disc to white
	 */
	private void setWhite()
	{
		setCircleColor(new Color(238, 238, 238));
		setCircleBorderWidth(5);
		setCircleBorderColor(new Color(204, 204, 204));
	}

	/**
	 * Sets the current disc to black
	 */
	private void setBlack()
	{
		setCircleColor(new Color(51, 51, 51));
		setCircleBorderWidth(5);
		setCircleBorderColor(new Color(68, 68, 68));
	}

	private void setPotentialWhite()
	{
		setCircleColor(new Color(230, 238, 246));
		setCircleBorderWidth(5);
		setCircleBorderColor(new Color(213, 221, 229));
	}

	private void setPotentialBlack()
	{
		setCircleColor(new Color(136, 144, 152));
		setCircleBorderWidth(5);
		setCircleBorderColor(new Color(145, 153, 161));
	}

	/**
	 * Returns the disc's x value
	 *
	 * @return x value
	 */
	public int getRowValue()
	{
		return this.rowValue;
	}

	/**
	 * Returns to disc's y value
	 *
	 * @return y value
	 */
	public int getColValue()
	{
		return this.colValue;
	}

	/**
	 * The mouse handler
	 */
	private class MouseHandler extends MouseAdapter
	{
		private String currentState;
		/**
		 * Executes whenever the mouse hovers over the tile
		 * Shows if a move is possible when hovered over
		 * @param e MouseEvent
		 */
		@Override
		public void mouseEntered(MouseEvent e)
		{
			int currentPlayer = game.getPlayer();
			//game.printDebug("Disc: currentPlayer = " + currentPlayer);
			//game.printDebug("Mouse Entered " + rowValue + " " + colValue);
			//game.printDebug("Disc State - " + state);
			if (currentPlayer + 2 == state && state == 3)
			{
				currentState = getState();
				setPotentialBlack();
			} else if (currentPlayer + 2 == state && state == 4)
			{
				currentState = getState();
				setPotentialWhite();
			} else
			{
				//game.printDebug("barbar");
				currentState = "";
			}
		}

		/**
		 * Executed when mouse leaves tiles
		 * Returns tile back to original state
		 *
		 * @param e MouseEvent
		 */
		@Override
		public void mouseExited(MouseEvent e)
		{
			if (!currentState.equals(""))
			{
				setState(currentState);
			}
		}

		/**
		 * Executes whenever the mouse is clicked on the tile
		 * Makes a move
		 *
		 * @param e MouseEvent
		 */
		@Override
		public void mouseClicked(MouseEvent e)
		{
			//game.printDebug("Mouse Clicked " + rowValue + " " + colValue);
			game.makeMove(rowValue, colValue, boardNum);
			currentState = getState();
		}
	}
}