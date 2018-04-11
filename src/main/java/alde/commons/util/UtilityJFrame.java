package alde.commons.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class UtilityJFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	@Override
	public void setVisible(boolean isVisible) {
		setLocation(alde.commons.util.MiddleOfTheScreen.getMiddleOfScreenLocationFor(this));

		super.setVisible(isVisible);
	}

}

class MiddleOfTheScreen {

	private static final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

	/**
	 * Keep in mind that setLocation should be called AFTER setBounds!
	 */
	public static Point getMiddleOfScreenLocationFor(Component e) {
		return getMiddleOfScreenLocationFor(e.getWidth(), e.getHeight());
	}

	private static Point getMiddleOfScreenLocationFor(int width, int height) {
		return new Point(screenDimension.width / 2 - width / 2, screenDimension.height / 2 - height / 2);
	}

}
