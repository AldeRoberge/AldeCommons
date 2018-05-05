package alde.commons;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.BasicConfigurator;

import alde.commons.console.Console;
import alde.commons.logger.LoggerPanel;

import java.awt.BorderLayout;

public class ExampleConsole {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		BasicConfigurator.configure();

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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(Console.get(), BorderLayout.CENTER);
		frame.getContentPane().add(LoggerPanel.get(), BorderLayout.SOUTH);
	}

}
