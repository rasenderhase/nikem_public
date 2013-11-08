package sample.font;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 * Abgeändertes Demo von:<br>
 * <br>
 * Killer Game Programming in Java<br>
 * Andrew Davison<br>
 * O'Reilly, May 2005<br>
 * ISBN: 0-596-00730-2
 * 
 * @author andreas
 * @see http://www.oreilly.com/catalog/killergame/
 * @see http://fivedots.coe.psu.ac.th/~ad/jg
 */
public abstract class GamePanel extends Panel implements Runnable {
	private static final long serialVersionUID = -1288929387110127850L;
	private static final int PWIDTH = 500; // size of panel
	private static final int PHEIGHT = 400;
	private Thread animator; // for the animation

	private volatile boolean running = false;
	private volatile boolean gameOver = false;

	// global variables for off-screen rendering
	private Graphics dbg;
	private Image dbImage = null;

	private int[] histCycleTime = new int[10];
	private long lastTime = 0;
	private long gameStart = 0;
	private int gameRunning = 0;

	// more variables, explained later
	// :
	public GamePanel() {
		setBackground(Color.white); // white background
		// setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
		// create game components
		// ...
	}
	public void addNotify()
	/*
	 * Wait for the JPanel to be added to the JFrame/JApplet before starting.
	 */
	{
		super.addNotify(); // creates the peer
		startGame();
	}

	/**
	 * initialise and start the thread
	 */
	private void startGame()
	{
		if (animator == null || !running) {
			animator = new Thread(this, "Animator");
			animator.start();
		}
	}

	/**
	 * called by the user to stop execution
	 */
	public void stopGame()
	{
		running = false;
	}

	/* Repeatedly update, render, sleep */
	public void run()
 {
		gameStart = System.currentTimeMillis();
		lastTime = gameStart;
		running = true;
		int i = 0;
		while (running) {
			i = (i + 1) % histCycleTime.length;
			long time = System.currentTimeMillis();
			int cycleTime = (int) (time - lastTime);
			histCycleTime[i] = cycleTime;
			gameRunning = (int) (time - gameStart);
			lastTime = time;

			gameUpdate();
			gameRender();
			paintScreen(); // draw buffer to screen
			// game state is updated // render to a buffer
			// paint with the buffer

			try {
				Thread.sleep(12); // sleep a bit
			} catch (InterruptedException ex) {
			}
		}
		System.exit(0); // so enclosing JFrame/JApplet exits
	}

	/**
	 * draw the current frame to an image buffer
	 */
	private void gameRender()
	{
		Rectangle bounds = getBounds();
		
		if (dbImage == null) { // create the buffer
			dbImage = createImage(PWIDTH, PHEIGHT);
			dbg = dbImage.getGraphics();
		}
		// clear the background
		dbg.setColor(Color.white);
		dbg.fillRect(0, 0, bounds.width, bounds.height);
		// draw game elements
		// ...
		gameRender(dbg);

		if (gameOver)
			gameOverMessage(dbg);
	} // end of gameRender()

	protected abstract void gameRender(Graphics g);

	/**
	 * draw the game-over message
	 * 
	 * @param g
	 */
	private void gameOverMessage(Graphics g)
	{ // code to calculate x and y...
		g.drawString("Game over", 100, 100);
	}

	/**
	 * update game state ...
	 */
	protected void gameUpdate() {
		if (!gameOver) {
		}
		// update game state ...
	}

	/**
	 * actively render the buffer image to the screen
	 */
	private void paintScreen() {
		Graphics g;
		try {
			g = this.getGraphics(); // get the panel's graphic context
			if ((g != null) && (dbImage != null)) {
				g.drawImage(dbImage, 0, 0, null);
				Toolkit.getDefaultToolkit().sync(); // sync the display on some systems
				g.dispose();
			} else {
				System.out.println("Kein Graphics");
			}
		} catch (Exception e) {
			System.out.println("Graphics context error: " + e);
		}
	}

	/**
	 * @return berechnete durchschnittliche FPS aus den letzten 10 Zyklen.
	 */
	public float getFps() {
		int sum = 0;
		int num = 0;
		for (int i = 0; i < histCycleTime.length; i++) {
			if (histCycleTime[i] > 0) {
				sum += histCycleTime[i];
				num++;
			}
		}
		return sum == 0 ? 0f : num * 1000f / sum;
	}

	/**
	 * @return Zeit in ms seit Start des Spiels
	 */
	public int getGameRunning() {
		return gameRunning;
	}

	// more methods, explained later...
}