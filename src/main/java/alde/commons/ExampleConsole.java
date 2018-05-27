package alde.commons;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.*;

import alde.commons.console.Console;
import alde.commons.logger.LoggerPanel;

public class ExampleConsole {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExampleConsole window = new ExampleConsole();
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
	public ExampleConsole() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.getContentPane().add(Console.get(), BorderLayout.SOUTH);
		frame.getContentPane().add(LoggerPanel.get(), BorderLayout.CENTER);
	}

}
