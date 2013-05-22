package de.tuc.collagecreator.server;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Einfache Datenbank.
 * 
 * @author Jens Drieseberg
 */
public class Database {
	String[] files;
	String directory;

	/**
	 * Erstellt eine neue Datenbank mit den Dateien aus dem uebergebenen
	 * Verzeichnis.
	 * 
	 * @param dirName
	 *            - Verzeichnisname
	 */
	public Database(String dirName) {
		init(dirName);
	}

	/**
	 * @return Anzahl der Dateien in der Datenbank.
	 */
	int getNumberOfFiles() {
		return files.length;
	}

	/**
	 * Gibt den Dateinamen an Position i zurueck.
	 * 
	 * @param i
	 *            - Index der Datei
	 * @return Dateiname an Index i
	 */
	String getFile(int i) {
		return directory + files[i];
	}

	/**
	 * Initialisiert die Datenbank.
	 * 
	 * @param dirName
	 *            - Verzeichnisname
	 */
	private void init(String dirName) {
		File dir = new File(dirName);
		directory = dir.getAbsolutePath() + "/";

		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.startsWith("."))
					return false;
				if (new File(dir.getAbsolutePath() + "/" + name).isDirectory())
					return false;
				return true;
			}
		};

		files = dir.list(filter);
	}
}
