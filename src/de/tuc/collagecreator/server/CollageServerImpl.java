package de.tuc.collagecreator.server;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import de.tuc.collagecreator.shared.CollageServer;
import de.tuc.collagecreator.shared.SerializableImageWrapper;

/**
 * 
 * @author Jens Drieseberg
 */
public class CollageServerImpl extends UnicastRemoteObject implements
		CollageServer {

	private static final long serialVersionUID = 1L;

	public CollageServerImpl() throws RemoteException {
		super();
	}

	@Override
	public void addImage(SerializableImageWrapper image) throws IOException {
		ImageIO.write(image.getImage(), "png",
				new File("./data/" + UUID.randomUUID() + ".png"));
	}

	@Override
	public SerializableImageWrapper generateCollage(
			SerializableImageWrapper template, int multi, int tileWidth,
			int tileHeight) {
		Database db = new Database("./data");
		return new SerializableImageWrapper(createCollage(db,
				template.getImage(), multi, tileWidth, tileHeight));
	}

	/**
	 * Bild laden, skalieren, rotieren und in die Collage zeichnen.
	 * 
	 * @param g
	 *            - Graphics Objekt der Collage
	 * @param fname
	 *            - Dateiname des Bildes
	 * @param x
	 *            - x Position des Bildes (Achtung: links oben)
	 * @param y
	 *            - y Position des Bildes (Achtung: links oben)
	 * @param width
	 *            - neue Breite des Bildes
	 * @param height
	 *            - neue Hoehe des Bildes
	 * @param degrees
	 *            - Winkel der Rotation
	 */
	void drawImageToCollage(Graphics g, String fname, int x, int y, int width,
			int height, int degrees) {
		// Laden des Bildes
		BufferedImage src = null;
		try {
			src = ImageIO.read(new File(fname));
		} catch (IOException ex) {
			System.out.println(fname + " konnte nicht geladen werden! "
					+ ex.getMessage());
			return;
		}

		// Graphics2D Objekt statt Graphics Objekt verwenden (mehr Operationen)
		Graphics2D g2 = (Graphics2D) g;

		// Affine Transformation erstellen
		AffineTransform at = new AffineTransform();

		// Bild an die richtige Position verschieben
		at.translate(x - width / 2, y - height / 2);

		// Bild skalieren
		at.scale((double) width / src.getWidth(),
				(double) height / src.getHeight());

		// Bild rotieren
		at.rotate(Math.toRadians(degrees), width / 2, height / 2);

		// Bild in Collage zeichnen
		g2.drawImage(src, at, null);
	}

	/**
	 * Erstellt eine Collage fuer das uebergebene Bild. Die Bilder in der
	 * Collage sind die Bilder aus der Datenbank.
	 * 
	 * @param image
	 *            - Input Image, alle Pixel != Weiss werden verwendet
	 * @param multi
	 *            - Wie oft sollen die Bilder der Datenbank verwendet werden
	 * @param imageWidth
	 *            - Breite der Bilder in der Collage
	 * @param imageHeight
	 *            - Hoehe der Bilder in der Collage
	 * @return die Collage als BufferedImage
	 */
	BufferedImage createCollage(Database db, BufferedImage image, int multi,
			int imageWidth, int imageHeight) {
		// leeres Bild fuer Collage erstellen
		BufferedImage ret = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		// Algorithmus zur Bildanalyse starten
		NeuralGas ng = new NeuralGas(multi * db.getNumberOfFiles(), image);

		// berechnete Positionen der Bilder auslesen
		Point2D[] pos = ng.getPositions();

		// Graphics Objekt erstellen
		Graphics g = ret.getGraphics();
		g.fillRect(0, 0, ret.getWidth(), ret.getHeight());

		// Zufallszahlengenerator fuer Rotationen
		Random rand = new Random();

		// Bilder in Collage zeichnen
		for (int i = 0; i < multi; i++) {
			int idx = i * db.getNumberOfFiles();
			for (int j = 0; j < db.getNumberOfFiles(); j++) {
				drawImageToCollage(g, db.getFile(j), (int) pos[idx + j].getX(),
						(int) pos[idx + j].getY(), imageWidth, imageHeight,
						rand.nextInt(50) - 25);
			}
		}

		return ret;
	}

}
