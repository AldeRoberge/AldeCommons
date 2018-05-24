package alde.commons.test;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Snippet {
	public static void main(String[] args) {
		try {
			Robot robot = new Robot();

			// Simulate a mouse click

			// Simulate a key press
			robot.keyPress(KeyEvent.VK_A);
			robot.keyRelease(KeyEvent.VK_A);

		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
