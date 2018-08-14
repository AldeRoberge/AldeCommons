package alde.commons.util.window;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UtilityJFrame extends JFrame {

	protected UtilityJFrame() {
		super();
	}

	public UtilityJFrame(String name) {
		super(name);

		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

	}

	@Override
	public void setVisible(boolean isVisible) {
		setLocation(MiddleOfTheScreen.getMiddleOfScreenLocationFor(this));
		super.setVisible(isVisible);
	}

}