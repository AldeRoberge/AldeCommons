package alde.commons.util.window;

import javax.swing.JFrame;

public class UtilityJFrame extends JFrame {

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