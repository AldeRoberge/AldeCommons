package alde.commons.util.window;

import javax.swing.JPanel;

class UtilityJPanel extends JPanel {

	@Override
	public void setVisible(boolean isVisible) {
		setLocation(MiddleOfTheScreen.getMiddleOfScreenLocationFor(this));
		super.setVisible(isVisible);
	}

}