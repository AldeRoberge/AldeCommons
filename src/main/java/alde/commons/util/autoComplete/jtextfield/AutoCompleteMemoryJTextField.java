package alde.commons.util.autoComplete.jtextfield;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

/**
 * A JTextField that remembers imputs and offers the AutoComplete service
 * 
 * Press UP, DOWN to show
 * 
 */
public class AutoCompleteMemoryJTextField extends AutoCompleteJTextField {

	org.slf4j.Logger log = LoggerFactory.getLogger(AutoCompleteMemoryJTextField.class);

	private List<String> previousImputs = new ArrayList<String>();
	private int currentIndex = 0;

	public AutoCompleteMemoryJTextField(AutoCompleteService s) {
		super(s);

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) { // Left blank intentionally
			}

			@Override
			public void keyPressed(KeyEvent e) { // Left blank intentionally
			}

			@Override
			public void keyReleased(KeyEvent e) {
				handle(e);
			}

			private void handle(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					setIndex(+1);
				}

				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					setIndex(-1);
				}
			}
		});

	}

	public void remember(String imput) {
		if (!StringUtils.isAllBlank(getText())) {
			previousImputs.add(getText());
			currentIndex = previousImputs.size();
		}
	}

	private void setIndex(int i) {

		currentIndex -= i;

		if (currentIndex < 0) { // Minimum
			currentIndex = 0;
		}

		if (currentIndex > previousImputs.size() - 1) { // Maximum
			currentIndex = previousImputs.size() - 1;
		}

		if (!(previousImputs.size() == 0)) {
			setText(previousImputs.get(currentIndex));
		}

	}

}
