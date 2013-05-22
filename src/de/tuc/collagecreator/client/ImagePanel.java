package de.tuc.collagecreator.client;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private BufferedImage image;

	private boolean valid = true;

	public ImagePanel() {
		this.setPreferredSize(new Dimension(400, 100));
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		this.setPreferredSize(new Dimension(Math.min(1000, image.getWidth()),
				Math.min(600, image.getHeight())));
		this.repaint();
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		super.paintComponent(g);
		if (image != null) {
			if (!valid) {
				g.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 0.1f));
			}
			g.drawImage(image, (this.getWidth() - image.getWidth()) / 2,
					(this.getHeight() - image.getHeight()) / 2, null);
		}
	}
}
