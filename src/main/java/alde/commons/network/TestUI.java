package alde.commons.network;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alde.commons.network.proxy.ProxyLeecher;

class TestUI {

	private static Logger log = LoggerFactory.getLogger(TestUI.class);

	private JFrame frame;

	public JLabel infoLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestUI window = new TestUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	private TestUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 764, 309);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


		infoLabel = new JLabel("");

		frame.add(infoLabel, BorderLayout.NORTH);


		JPanel panel = GetWebsiteWorkerHandler.get().getUI();

		//String URL, String avoid, int maxAttempt, Consumer<List<String>> websiteContentConsumer

		for (int i = 0; i < 100; i++) {
			GetWebsiteWorkerHandler.get().addTask(new GetWebsiteTask("google.com", "ajwdawdjaw", 1, new Consumer<List<String>>() {
				@Override
				public void accept(List<String> t) {
					log.info("Received answer : ");

					infoLabel.setText("Tasks : ");


				}
			}));
		}

		frame.getContentPane().add(panel, BorderLayout.CENTER);
	}

}
