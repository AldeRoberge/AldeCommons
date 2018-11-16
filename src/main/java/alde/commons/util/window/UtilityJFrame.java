package alde.commons.util.window;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UtilityJFrame extends JFrame {

	static {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public UtilityJFrame() {
		super();
	}

	public UtilityJFrame(String name) {
		super(name);

	}

	@Override
	public void setVisible(boolean isVisible) {
		setLocation(MiddleOfTheScreen.getMiddleOfScreenLocationFor(this));
		super.setVisible(isVisible);
	}

}