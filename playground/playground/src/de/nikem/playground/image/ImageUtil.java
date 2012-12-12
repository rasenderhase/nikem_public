package de.nikem.playground.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * 
 * @author andreas
 * @see http://www.javalobby.org/articles/ultimate-image/
 * @see http://stackoverflow.com/questions/665406/how-to-make-a-color-transparent-in-a-bufferedimage-and-save-as-png
 */
public class ImageUtil {

	public static BufferedImage loadImage(String ref) {
		BufferedImage bimg = null;
		try {

			bimg = ImageIO.read(new File(ref));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bimg;
	}

	/**
	 * Macht eine bestimmte Farbe transparent.
	 * 
	 * @param image
	 *            Das Original-Bild
	 * @param color
	 *            Die Farbe, die transparent werden soll
	 * @param schwelle
	 *            Die Schwelle, bis zu der von der Farba <b>color</b> abweichende Farben ebenfalls transparent gemacht werden sollen.
	 * @return neue Farbe
	 */
	public static BufferedImage makeColorTransparent(BufferedImage image, Color color, float schwelle) {
		BufferedImage dimg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = dimg.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(image, null, 0, 0);
		g.dispose();
		for (int i = 0; i < dimg.getHeight(); i++) {
			for (int j = 0; j < dimg.getWidth(); j++) {
				Color originalColor = new Color(dimg.getRGB(j, i));

				if (compareColor(originalColor.getRed(), color.getRed(), schwelle) && compareColor(originalColor.getGreen(), color.getGreen(), schwelle) && compareColor(originalColor.getBlue(), color.getBlue(), schwelle)) {
					dimg.setRGB(j, i, 0x8F1C1C);
				}
			}
		}
		return dimg;
	}

	public static boolean compareColor(float component1, float component2, float schwelle) {
		return component1 > component2 - schwelle && component1 < component2 + schwelle;
	}

	/**
	 * Method to overlay Images
	 * 
	 * @param bgImage
	 *            The background Image
	 * @param fgImage
	 *            The foreground Image
	 * @return overlayed image (fgImage over bgImage)
	 */
	public static BufferedImage overlayImages(BufferedImage bgImage, BufferedImage fgImage) {

		/**
		 * Doing some preliminary validations. Foreground image height cannot be greater than background image height. Foreground image width cannot be greater than background image width.
		 * 
		 * returning a null value if such condition exists.
		 */
		if (fgImage.getHeight() > bgImage.getHeight() || fgImage.getWidth() > fgImage.getWidth()) {
			JOptionPane.showMessageDialog(null, "Foreground Image Is Bigger In One or Both Dimensions" + "\nCannot proceed with overlay." + "\n\n Please use smaller Image for foreground");
			return null;
		}

		/** Create a Graphics from the background image **/
		Graphics2D g = bgImage.createGraphics();
		/** Set Antialias Rendering **/
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		/**
		 * Draw background image at location (0,0) You can change the (x,y) value as required
		 */
		g.drawImage(bgImage, 0, 0, null);

		/**
		 * Draw foreground image at location (0,0) Change (x,y) value as required.
		 */
		g.drawImage(fgImage, 0, 0, null);

		g.dispose();
		return bgImage;
	}
}