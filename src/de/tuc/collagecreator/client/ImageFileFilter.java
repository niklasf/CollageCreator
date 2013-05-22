package de.tuc.collagecreator.client;

import java.io.File;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageFileFilter extends FileFilter {

	private FileNameExtensionFilter fileNameExtensionFilter;

	public ImageFileFilter() {
		fileNameExtensionFilter = new FileNameExtensionFilter("Images", "png",
				"jpg", "gif");
	}

	@Override
	public boolean accept(File file) {
		return fileNameExtensionFilter.accept(file);
	}

	@Override
	public String getDescription() {
		return fileNameExtensionFilter.getDescription();
	}

}
