package sample.font;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class FontSample extends Frame {
	private static final long serialVersionUID = 1L;
	
	private final Font MY_FONT;
	private static final String SPRUCH = "Alea iacta est";
	
	public FontSample() throws FontFormatException, IOException {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		MY_FONT = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/sample/font/Herculanum.ttf"));
	}
	
	@Override
	public void paint(Graphics g) { 
		super.paint(g);
		Rectangle bounds = getBounds();
		int visibleHeight = bounds.height - getInsets().top - getInsets().bottom;
		int visibleWidth = bounds.width - getInsets().left - getInsets().right;
		
		float xratio = visibleHeight / 1.5f;
		float yratio = visibleWidth / 8.0f;
		
		float ratio = Math.min(xratio, yratio);
		
		Font f = MY_FONT.deriveFont(Font.BOLD, ratio);
		
		g.setFont(f);
		g.drawString(SPRUCH, 
				getInsets().left + visibleWidth / 2 - (int) (ratio * 3.8),
				getInsets().top + visibleHeight / 2 + (int) (ratio / 3.8));
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
