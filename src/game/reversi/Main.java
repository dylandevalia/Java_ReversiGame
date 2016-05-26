package game.reversi;

import game.reversi.counters.Disc;

import javax.swing.*;
import java.awt.*;

public class Main
{

	public static void main(String[] args)
	{
		System.out.println("Hello Darkness");

		/** Create windows */
		JFrame p1_window = new JFrame();
		JFrame p2_window = new JFrame();

		/** Create panels */
		/* Game board - border layout */
		JPanel p1_panel_gameBoard = new JPanel(new BorderLayout(5, 5));
		JPanel p2_panel_gameBoard = new JPanel(new BorderLayout(5, 5));
		/* Game grid - grid layout (8x8) */
		JPanel p1_panel_gameGrid = new JPanel(new GridLayout(8, 8));
		JPanel p2_panel_gameGrid = new JPanel(new GridLayout(8, 8));
		/* Info panel - border layout */
		JPanel p1_panel_info = new JPanel(new BorderLayout(5, 5));
		JPanel p2_panel_info = new JPanel(new BorderLayout(5, 5));

		/** Add panels */
		/* Window <- Game board */
		p1_window.add(p1_panel_gameBoard);
		p2_window.add(p2_panel_gameBoard);
		/* Game board <- Game grid */
		p1_panel_gameBoard.add(p1_panel_gameGrid, BorderLayout.CENTER);
		p2_panel_gameBoard.add(p2_panel_gameGrid, BorderLayout.CENTER);
		/* Game board <- Info panel */
		p1_panel_gameBoard.add(p1_panel_info, BorderLayout.NORTH);
		p2_panel_gameBoard.add(p2_panel_info, BorderLayout.NORTH);

		/** Create labels */
		/* Displays the player turn */
		JLabel p1_label_player = new JLabel();
		JLabel p2_label_player = new JLabel();
		/* Displays the score */
		JLabel p1_label_score = new JLabel();
		JLabel p2_label_score = new JLabel();

		/** Add labels */
		p1_panel_info.add(p1_label_player, BorderLayout.WEST);
		p2_panel_info.add(p2_label_player, BorderLayout.WEST);
		p1_panel_info.add(p1_label_score, BorderLayout.EAST);
		p2_panel_info.add(p2_label_score, BorderLayout.EAST);

		/** Label set font */
		p1_label_player.setFont(new Font("Segoe UI Semibold", Font.BOLD, 30));
		p2_label_player.setFont(new Font("Segoe UI Semibold", Font.BOLD, 30));
		p1_label_score.setFont(new Font("Segoe UI Semibold", Font.BOLD, 30));
		p2_label_score.setFont(new Font("Segoe UI Semibold", Font.BOLD, 30));

		/** Create buttons */
		JButton p1_button_ai = new JButton("Make Move (Greedy Search)");
		JButton p2_button_ai = new JButton("Make Move (Greedy Search)");

		/** Add buttons */
		p1_panel_gameBoard.add(p1_button_ai, BorderLayout.SOUTH);
		p2_panel_gameBoard.add(p2_button_ai, BorderLayout.SOUTH);

		/** Button set font */
		p1_button_ai.setFont(new Font("Segeo UI Semibold", Font.BOLD, 20));
		p2_button_ai.setFont(new Font("Segeo UI Semibold", Font.BOLD, 20));

		/** Add counters */
		Disc[][] p1_discs = new Disc[8][8];
		Disc[][] p2_discs = new Disc[8][8];
		Game mainGame = new Game();
		for (int column = 0; column < 8; column++)
		{
			for (int row = 0; row < 8; row++)
			{
				/** Creates the disc */
				p1_discs[row][column] = new Disc(row, column, 1, mainGame); // Reversed so that first term is x-axis
				p2_discs[7 - row][7 - column] = new Disc(7 - row, 7 - column, 2, mainGame);

				/** Adds the counters to the game grid */
				p1_panel_gameGrid.add(p1_discs[row][column]);
				p2_panel_gameGrid.add(p2_discs[7 - row][7 - column]);
			}
		}

		/** Sets up initial board */
		mainGame.initialise(p1_discs, p1_label_player, p1_label_score, p1_button_ai,
				p2_discs, p2_label_player, p2_label_score, p2_button_ai);

		/** Sets the frame and makes visible */
		p1_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Closes program on exit
		p2_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p1_window.setTitle("Reversi - Player One");                 // Sets title
		p2_window.setTitle("Reversi - Player Two");
		p1_window.setResizable(false);                              // Doesn't allow the window to be resizable
		p2_window.setResizable(false);
		p1_window.pack();                                           // Packs the window
		p2_window.pack();
		p2_window.setLocation(p1_window.getWidth() + 5,
				0);      // Moves the player 2 window to the right of the player 1 window
		p1_window.setVisible(true);                                 // Sets the frame to be visible
		p2_window.setVisible(true);

		/** Runs main game loop */
		mainGame.resetGame();
		//mainGame.gameLoop();

		System.out.println("My Old Friend");
	}
}
