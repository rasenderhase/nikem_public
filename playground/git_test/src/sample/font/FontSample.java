package sample.font;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class FontSample extends Frame {
	private static final long serialVersionUID = 1L;

	private final Font MY_FONT;
	private final Font INFO_FONT;
	private final FontPanel fontPanel;

	private static final String SPRUCH = "Alea iacta est";

	private class FontPanel extends GamePanel {
		private static final long serialVersionUID = 8121755047956924512L;
		private Rectangle bounds = null;
		private int time;
		private int fpsCycle = 0;
		private float fps;
		private boolean showFps = false;

		@Override
		protected void gameRender(Graphics g) {
			if (bounds != null) {

				if (showFps) {
					g.setColor(Color.GREEN);
					g.setFont(INFO_FONT);
					g.drawString("FPS: " + String.format("%4.2f", fps), 5, 15);
				}

				int visibleHeight = bounds.height - getInsets().top - getInsets().bottom;
				int visibleWidth = bounds.width - getInsets().left - getInsets().right;

				float xratio = visibleHeight / 1.5f;
				float yratio = visibleWidth / 9.0f;

				float ratio = Math.min(xratio, yratio);

				Font f = MY_FONT.deriveFont(Font.BOLD, ratio);

				float sin = (float) Math.sin(time / 1000.0) / 2.0f + 0.5f;
				float cos = (float) Math.cos(time / 1000.0) / 2.0f + 0.5f;
				g.setColor(new Color(sin, 0.5f, 0.5f));
				g.setFont(f);
				g.drawString(SPRUCH, getInsets().left + visibleWidth / 2 - (int) (ratio * 4.3), getInsets().top + visibleHeight / 2
						+ (int) (ratio / 3.8));

				g.setColor(new Color(0.2f, 0.5f, 0.5f));
				int x1 = (int) (sin * bounds.width * 0.9f);
				int y1 = (int) (cos * bounds.height * 0.9f);
				int x2 = bounds.width - (int) (sin * bounds.width * 0.9f);
				int y2 = bounds.height - (int) (cos * bounds.height * 0.9f);
				g.drawLine(x1, y1, x2, y2);
			}
		}

		@Override
		protected void gameUpdate() {
			super.gameUpdate();
			bounds = getBounds();
			time = getGameRunning();
			fpsCycle = (fpsCycle + 1) % 10;
			if (fpsCycle == 0 || fps == 0) {
				fps = getFps();
			}
		}

		public void toggleFps() {
			showFps = !showFps;
		}
	}

	public FontSample() throws FontFormatException, IOException {
		fontPanel = new FontPanel();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				fontPanel.stopGame();
			}
		});

		MY_FONT = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/sample/font/gtw.ttf"));
		INFO_FONT = Font.decode("Courier 10");

		Panel p = new Panel(new BorderLayout());
		p.add(fontPanel, BorderLayout.CENTER);

		Button fps = new Button("FPS");
		fps.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				fontPanel.toggleFps();
			}
		});
		p.add(fps, BorderLayout.SOUTH);
		add(p);

		pack();
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FontFormatException 
	 */
	public static void main(String[] args) throws FontFormatException, IOException {
		FontSample appwin = new FontSample(); 
		appwin.setSize(new Dimension(300, 200)); 
		appwin.setTitle("Font Sample"); 
		appwin.setVisible(true);
	}

}
