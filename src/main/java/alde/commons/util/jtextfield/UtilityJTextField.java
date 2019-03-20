package alde.commons.util.jtextfield;

import alde.commons.util.jtextfield.autocomplete.AutoCompleteDocument;
import alde.commons.util.jtextfield.autocomplete.AutoCompleteService;
import alde.commons.util.jtextfield.memory.MemoryService;
import alde.commons.util.jtextfield.onlynumbers.OnlyNumbersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * - Shows hint (grayed out text)
 * - Remembers inputs (use arrows to move up and down)
 * - Shows autocomplete suggestions
 */
public class UtilityJTextField extends JTextField {

	Logger log = LoggerFactory.getLogger(UtilityJTextField.class);

	/**
	 * AutoCompleteService used by suggestions
	 */
	private AutoCompleteService autoCompleteService = new AutoCompleteService();

	/**
	 * MemoryService used by memory
	 */
	private MemoryService memoryService = new MemoryService();

	/**
	 * OnlyNumbersService to only allow numbers
	 */
	private OnlyNumbersService onlyNumbersService = new OnlyNumbersService();

	private String hint;

	private boolean isPassword;

	public UtilityJTextField() {
		this("");
	}

	public UtilityJTextField(String text) {
		this(text, "");
	}

	public UtilityJTextField(String text, String hint) {
		super(text);
		setHint(hint);

		allowMemory();
		allowAutocomplete();
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public void allowMemory() {
		memoryService.setField(this);
	}

	public void onlyAllowNumbers() {
		onlyNumbersService.setField(this);
	}

	public void allowAutocomplete() {
		setDocument(new AutoCompleteDocument(autoCompleteService, this));
	}

	public void addData(String... data) {
		autoCompleteService.addData(data);
	}

	@Override
	public void paint(Graphics g) {

		super.paint(g);

		if (isPassword || getText().length() == 0) {

			int h = this.getHeight();
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			Insets ins = this.getInsets();
			FontMetrics fm = g.getFontMetrics();
			g.setColor(Color.BLACK);

			StringBuilder s = new StringBuilder();

			if (isPassword && getText().length() > 0) {
				for (int i = 0; i < this.getText().length(); i++) {
					s.append("*");
				}

				setForeground(Color.white);
				this.setSelectedTextColor(Color.white);
				this.setSelectionColor(Color.white);

			} else {
				s.append(hint);
			}

			g.drawString(s.toString(), ins.left, ((h / 2) + (fm.getAscent() / 2)) - 2);

		}

	}

	public void setIsPassword() {
		this.isPassword = true;
	}
}