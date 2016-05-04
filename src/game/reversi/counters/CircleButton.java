package game.reversi.counters;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CircleButton extends JButton implements Runnable
{
	/**
	 * Colour of main square
	 */
	protected Color squareColor;
	/**
	 * Width of border in pixels
	 */
	protected int squareBorderWidth;
	/**
	 * Colour of border
	 */
	protected Color squareBorderColor;

	/**
	 * Colour of main circle
	 */
	protected Color circleColor;
	/**
	 * Width of border in pixels
	 */
	protected int circleBorderWidth;
	/**
	 * Colour of border
	 */
	protected Color circleBorderColor;

	/** ------------ */
	/** Constructors */
	/** ------------ */

	/**
	 * Constructor initialises the object - for a colour and a different coloured border
	 *
	 * @param width             Width of the square - preferred and min size
	 * @param height            Height of the square - preferred and min size
	 * @param squareColor       Colour of the main part of the square
	 * @param squareBorderWidth Width of the square border, in pixels
	 * @param squareBorderColor Colour of the square border
	 * @param circleColor       Colour of the main part of the circle
	 * @param circleBorderWidth Width of the circle border, in pixels
	 * @param circleBorderColor Colour of the circle border
	 */
	public CircleButton(int width, int height,
	                    Color squareColor, int squareBorderWidth, Color squareBorderColor,
	                    Color circleColor, int circleBorderWidth, Color circleBorderColor)
	{
		this.squareBorderWidth = squareBorderWidth;
		this.squareColor = squareColor;
		this.squareBorderColor = squareBorderColor;

		this.circleColor = circleColor;
		this.circleBorderWidth = circleBorderWidth;
		this.circleBorderColor = circleBorderColor;

		setMinimumSize(new Dimension(width, height));
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
	}

	/**
	 * Constructor initialises the object - for a colour and a different coloured border
	 * No circle border
	 */
	public CircleButton(int width, int height,
	                    Color squareColor, int squareBorderWidth, Color squareBorderColor,
	                    Color circleColor)
	{
		// Call the other constructor with some default values
		this(width, height,
				squareColor, squareBorderWidth, squareBorderColor,
				circleColor, 0, null);
	}

	/**
	 * Constructor initialises the object - for a colour and a different coloured border
	 * No square border
	 */
	public CircleButton(int width, int height,
	                    Color squareColor,
	                    Color circleColor, int circleBorderWidth, Color circleBorderColor)
	{
		// Call the other constructor with some default values
		this(width, height,
				squareColor, 0, null,
				circleColor, circleBorderWidth, circleBorderColor);
	}

	/**
	 * Constructor initialises the object - for a colour and a different coloured border
	 * No circle or square border
	 */
	public CircleButton(int width, int height,
	                    Color squareColor,
	                    Color circleColor)
	{
		// Call the other constructor with some default values
		this(width, height,
				squareColor, 0, null,
				circleColor, 0, null);
	}

	/** ------- */
	/** Drawing */
	/** ------- */

	/**
	 * This is called by the system and your code needs to draw the component
	 *
	 * @param g The graphics object that the systems gives you to draw to
	 */
	protected void paintComponent(Graphics g)
	{
		/** Square */
		if (squareBorderColor != null)
		{
			g.setColor(squareBorderColor);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		if (squareColor != null)
		{
			g.setColor(squareColor);
			g.fillRect(squareBorderWidth, squareBorderWidth,
					getWidth() - squareBorderWidth * 2, getHeight() - squareBorderWidth * 2);
		}

		/** Circle */
		if (circleBorderColor != null)
		{
			g.setColor(circleBorderColor);
			g.fillOval(0, 0, getWidth(), getHeight());
		}
		if (circleColor != null)
		{
			g.setColor(circleColor);
			g.fillOval(circleBorderWidth, circleBorderWidth,
					getWidth() - circleBorderWidth * 2, getHeight() - circleBorderWidth * 2);
		}

	}

	/**
	 * Ask the even thread to redraw this button now
	 */
	private void redrawSelf()
	{
		EventQueue.invokeLater(this);
	}

	/**
	 * Implemented run() in this object for passing to invokeLater() above
	 */
	public void run()
	{
		repaint();
	}

	/** ------- */
	/** Setters */
	/** ------- */

	/**
	 * Set the colour of the square AND ask it to redraw now
	 */
	public void setSquareColor(Color newColor)
	{
		squareColor = newColor;
		redrawSelf();
	}

	/**
	 * Set the border colour of the square AND ask it to redraw now
	 */
	public void setSquareBorderColor(Color newColor)
	{
		squareBorderColor = newColor;
		redrawSelf();
	}

	/**
	 * Set the border width of the square AND ask it to redraw now
	 */
	public void setSquareBorderWidth(int newWidth)
	{
		squareBorderWidth = newWidth;
		redrawSelf();
	}

	/**
	 * Set the colour of the circle AND ask it to redraw now
	 */
	public void setCircleColor(Color newColor)
	{
		circleColor = newColor;
		redrawSelf();
	}

	/**
	 * Set the border colour of the circle AND ask it to redraw now
	 */
	public void setCircleBorderColor(Color newColor)
	{
		circleBorderColor = newColor;
		redrawSelf();
	}

	/**
	 * Set the border width of the circle AND ask it to redraw now
	 */
	public void setCircleBorderWidth(int newWidth)
	{
		circleBorderWidth = newWidth;
		redrawSelf();
	}

	/** ------- */
	/** Getters */
	/** ------- */

	/**
	 * Get the current square colour
	 */
	public Color getSquareColor()
	{
		return squareColor;
	}

	/**
	 * Get the current square border width
	 */
	public int getSquareBorderWidth()
	{
		return squareBorderWidth;
	}

	/**
	 * Get the current square border colour
	 */
	public Color getSquareBorderColor()
	{
		return squareBorderColor;
	}

	/**
	 * Get the current cirle colour
	 */
	public Color getCircleColor()
	{
		return circleColor;
	}

	/**
	 * Get the current circle border width
	 */
	public int getCircleBorderWidth()
	{
		return circleBorderWidth;
	}

	/**
	 * Get the current circle border colour
	 */
	public Color getCircleBorderColor()
	{
		return circleBorderColor;
	}

	/**
	 * Added to get rid of warning, for serialisation
	 */
	private static final long serialVersionUID = 9041257494324082543L;
}
