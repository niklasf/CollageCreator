package de.tuc.collagecreator.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import de.tuc.collagecreator.shared.CollageServer;
import de.tuc.collagecreator.shared.SerializableImageWrapper;

public class ClientFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private CollageServer collageServer;

	private JSpinner tileWidthSpinner;

	private JSpinner tileHeightSpinner;

	private JSpinner multiSpinner;

	private ImagePanel imagePanel;

	private JButton templateButton;

	private JButton uploadTileButton;

	private JButton generateButton;

	private BufferedImage template;

	public ClientFrame(CollageServer collageServer) {
		this.collageServer = collageServer;

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.VERTICAL;
		c.gridy = 0;
		this.add(createImagePanel(), c);
		c.gridy++;
		this.add(createSettingsPanel(), c);
		c.gridy++;
		this.add(createMenuPanel(), c);

		this.setTitle("Collage creator");
		this.pack();
	}

	private JPanel createImagePanel() {
		imagePanel = new ImagePanel();
		return imagePanel;
	}

	private JPanel createSettingsPanel() {
		JPanel settingsPanel = new JPanel();

		settingsPanel.add(new JLabel("Tile instances:"));
		multiSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
		settingsPanel.add(multiSpinner);

		settingsPanel.add(new JLabel("Tile width:"));
		tileWidthSpinner = new JSpinner(new SpinnerNumberModel(30, 1, 64, 1));
		settingsPanel.add(tileWidthSpinner);

		settingsPanel.add(new JLabel("Tile height:"));
		tileHeightSpinner = new JSpinner(new SpinnerNumberModel(30, 1, 64, 1));
		settingsPanel.add(tileHeightSpinner);

		return settingsPanel;
	}

	private JPanel createMenuPanel() {
		JPanel menuPanel = new JPanel();

		templateButton = new JButton("Choose template");
		templateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(new ImageFileFilter());
				if (JFileChooser.APPROVE_OPTION == fileChooser
						.showOpenDialog(ClientFrame.this)) {
					try {
						template = ImageIO.read(fileChooser.getSelectedFile());
						imagePanel.setImage(template);
						ClientFrame.this.pack();
						generateButton.setEnabled(true);
					} catch (IOException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(ClientFrame.this,
								"Error opening template image.",
								"Collage creator", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		menuPanel.add(templateButton);

		uploadTileButton = new JButton("Upload tile");
		uploadTileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(new ImageFileFilter());
				fileChooser.setMultiSelectionEnabled(true);
				if (JFileChooser.APPROVE_OPTION == fileChooser
						.showOpenDialog(ClientFrame.this)) {
					try {
						for (File file : fileChooser.getSelectedFiles()) {
							BufferedImage image = ImageIO.read(file);
							SerializableImageWrapper wrapped = new SerializableImageWrapper(
									image);
							collageServer.addImage(wrapped);
						}
					} catch (IOException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(ClientFrame.this,
								"Error uploading tile image.",
								"Collage creator", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		menuPanel.add(uploadTileButton);

		generateButton = new JButton("Generate collage");
		generateButton.setEnabled(false);
		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					int multi = (Integer) multiSpinner.getValue();
					int tileWidth = (Integer) tileWidthSpinner.getValue();
					int tileHeight = (Integer) tileHeightSpinner.getValue();
					SerializableImageWrapper collage = collageServer
							.generateCollage(new SerializableImageWrapper(
									template), multi, tileWidth, tileHeight);
					imagePanel.setImage(collage.getImage());
				} catch (IOException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(ClientFrame.this,
							"Error generating collage.", "Collage creator",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		menuPanel.add(generateButton);

		return menuPanel;
	}

	public static void main(String[] args) throws MalformedURLException,
			RemoteException, NotBoundException {
		CollageServer collageServer = (CollageServer) Naming
				.lookup("rmi://localhost:1234/CollageServer");

		ClientFrame frame = new ClientFrame(collageServer);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
