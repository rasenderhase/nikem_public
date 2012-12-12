package de.nikem.playground.image;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ImageApp {

	public void loadAndDisplayImage(JFrame frame) {
		// Load the img
		BufferedImage loadImg = ImageUtil.loadImage("WebContent/alma.png");
		BufferedImage backgroundImg = ImageUtil.loadImage("WebContent/nikem-team-620.png");

		BufferedImage transparentImage = ImageUtil.makeColorTransparent(loadImg, new Color(17, 18, 110), 75);
		BufferedImage gesamtImage = ImageUtil.overlayImages(backgroundImg, transparentImage);

		frame.setBounds(0, 0, gesamtImage.getWidth(), gesamtImage.getHeight());

		JImagePanel panel = new JImagePanel(gesamtImage, 0, 0);
		frame.add(panel);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		ImageApp ia = new ImageApp();
		JFrame frame = new JFrame("Tutorials");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		ia.loadAndDisplayImage(frame);
	}

}