package alde.commons;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import alde.commons.console.Console;
import alde.commons.logger.LoggerPanel;

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
					ExampleConsole window = new ExampleConsole();
					window.frmConsole.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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

		frmConsole.getContentPane().add(LoggerPanel.get(), BorderLayout.CENTER);
		frmConsole.getContentPane().add(Console.get(), BorderLayout.SOUTH);
	}

}
