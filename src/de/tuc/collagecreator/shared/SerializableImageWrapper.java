package de.tuc.collagecreator.shared;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.Serializable;

public class SerializableImageWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	private int width;

	private int height;

	private int[] pixels;

	public SerializableImageWrapper(BufferedImage image) {
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.pixels = image.getRGB(0, 0, this.width, this.height, null, 0,
				this.width);
	}

	public BufferedImage getImage() {
		Image image = Toolkit.getDefaultToolkit().createImage(
				new MemoryImageSource(this.width, this.height, this.pixels, 0,
						this.width));
		BufferedImage buffer = new BufferedImage(this.width, this.height,
				BufferedImage.TYPE_INT_RGB);
		buffer.getGraphics().drawImage(image, 0, 0, null);
		return buffer;
	}
}
