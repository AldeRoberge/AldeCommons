package alde.commons;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alde.commons.console.Console;
import alde.commons.logger.LoggerPanel;
import alde.commons.properties.PropertiesExample;
import alde.commons.util.SplashScreen;

public class ExampleConsole {

	private JFrame frmConsole;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {

					showSplashScreen(new Runnable() {
						@Override
						public void run() {
							ExampleConsole window = new ExampleConsole();
							window.frmConsole.setVisible(true);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void showSplashScreen(Runnable r) {

		if (PropertiesExample.SHOW_SPLASH_SCREEN.getValueAsBoolean()) {

			boolean showSplashScreen = true;

			if (showSplashScreen) {

				//log.info("Opening splash screen...");

				try {
					BufferedImage inImage = getBufferedImage("/splashScreen/splashScreen_in.png");
					BufferedImage outImage = getBufferedImage("/splashScreen/splashScreen_out.png");
					BufferedImage textImage = getBufferedImage("/splashScreen/splashScreen_title.png");

					SplashScreen s = new SplashScreen(inImage, outImage, textImage);

					s.setAutomaticClose(PropertiesExample.SPLASH_SCREEN_TIME.getValueAsInt());
					s.setRunnableAfterClose(r);
					s.setSubtitle("Loading complete");

					try {
						s.setSound(new File(ExampleConsole.class.getResource("/splashScreen/boot.wav").toURI()));
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}

					s.setVisible(true);

				} catch (Exception e) {
					//log.error("Error with opening splash screen.");
					e.printStackTrace();

					r.run();
				}
			} else {
				r.run();
			}

		} else {
			r.run();
		}

	}

	public static BufferedImage getBufferedImage(String name) {
		return (BufferedImage) getImage(name);
	}

	public static Image getImage(String name) {

		try {
			return ImageIO.read(ExampleConsole.class.getResourceAsStream(name));
		} catch (IOException e) {
			e.printStackTrace();
		}

		//log.error("Could not get image file '" + name + "'.");

		return null;
	}

	/**
	 * Create the application.
	 */
	private ExampleConsole() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmConsole = new JFrame();
		frmConsole.setTitle("Console");
		frmConsole.setBounds(100, 100, 715, 355);
		frmConsole.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane();
		frmConsole.getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BorderLayout(0, 0));

		panel.add(LoggerPanel.get(), BorderLayout.CENTER);

		frmConsole.getContentPane().add(Console.get(), BorderLayout.SOUTH);

	}

}
