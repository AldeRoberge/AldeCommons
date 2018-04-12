package alde.commons.properties;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Property {

	public static final String TRUE = "TRUE";
	public static final String FALSE = "FALSE";

	private static final Logger logger = LoggerFactory.getLogger(Property.class);

	private PropertyFileManager propertyManager; //property file manager used to save and get values on updates

	private String key;
	private String description;
	private String value;
	private String defaultValue;

	private EditPropertyPanel editPropertyPanel;

	/**
	 * @param keyName          Key to store value to
	 * @param description      Description of what this property does
	 * @param defaultValue     Default value returned if no property of this key exists
	 * @param propertyManager  PropertyFileManager used to store and retrieve keys
	 */
	public Property(String keyName, String description, String defaultValue, PropertyFileManager propertyManager) {
		this.key = keyName;
		this.description = description;
		this.defaultValue = defaultValue;
		this.value = propertyManager.getPropertyValue(keyName, defaultValue);
		this.propertyManager = propertyManager;
	}

	public EditPropertyPanel getEditPropertyPanel() {
		if (editPropertyPanel == null) {
			editPropertyPanel = new EditPropertyPanel(this);
		}

		return editPropertyPanel;
	}

	public String getDescription() {
		return description;
	}

	public String getValue() {
		return value;
	}

	public String setNewValue(String value) {
		propertyManager.savePropertyValue(key, value);
		this.value = value;

		return value;
	}

	public boolean setNewValue(Boolean value) {
		String booleanStringValue = Boolean.toString(value).toUpperCase();
		propertyManager.savePropertyValue(key, booleanStringValue);
		this.value = booleanStringValue;

		getEditPropertyPanel().updateFieldWithNewValue();

		return value;
	}

	public int setNewValue(int value) {
		String stringValue = Integer.toString(value);

		propertyManager.savePropertyValue(key, stringValue);
		this.value = stringValue;

		return value;
	}

	public boolean isDefaultValue() {
		return this.value.equals(defaultValue);
	}

	public boolean getValueAsBoolean() {
		switch (value) {
		case TRUE:
			return true;
		case FALSE:
			return false;
		default:
			logger.error("isTrue() on " + key + " for value " + value
					+ " is impossible. (Boolean string values are case sensitive!)");
			return false;
		}
	}

	public int getValueAsInt() {

		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			logger.error("Could not get value as int (value : " + value + ", key : " + key
					+ "), attempting with default value", e);
		}

		try {
			return Integer.parseInt(defaultValue);
		} catch (Exception e) {
			logger.error("Could not get default value as int (value : " + defaultValue + ", key : " + key + ")", e);
		}

		return 0;
	}

	@Override
	public String toString() {
		return getValue();
	}

	public String getKey() {
		return key;
	}

}

class EditPropertyPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private Property property;

	private final JPanel warningPanel;

	private String newValue;
	private JTextField inputField;

	private JLabel label;

	private final JButton saveButton;

	/**
	 * Automatically detected based on the first value
	 * 1 = boolean
	 * 2 = number
	 * 3 = any
	 */
	private final int flaggedType;
	private final String warning;

	public void updateFieldWithNewValue() {
		inputField.setText(property.getValue());
	}

	public EditPropertyPanel(final Property property) {
		this.property = property;
		setLayout(new BorderLayout(0, 0));

		inputField = new JTextField(property.getValue());

		add(inputField, BorderLayout.CENTER);
		inputField.setColumns(10);

		if (isBooleanStringValue(property.getValue())) {
			flaggedType = 1;
			warning = "This property was automaticaly flagged as 'string boolean value only'.";
		} else if (isOnlyNumbers(property.getValue())) {
			flaggedType = 2;
			warning = "This property was automaticaly flagged as 'numbers only'";
		} else {
			flaggedType = 3;
			warning = "";
		}

		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				property.setNewValue(newValue);
				setIsEdited(false);
			}

		});
		add(saveButton, BorderLayout.EAST);
		saveButton.setVisible(false);

		label = new JLabel();
		updateLabelText(false);
		label.setToolTipText(property.getKey());
		add(label, BorderLayout.WEST);

		//

		warningPanel = new JPanel();
		add(warningPanel, BorderLayout.SOUTH);

		JLabel warningLabel = new JLabel(warning);
		warningLabel.setForeground(Color.RED);
		warningPanel.add(warningLabel);

		warningPanel.setVisible(false);

		//

		inputField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				update();
			}

			public void removeUpdate(DocumentEvent e) {
				update();
			}

			public void insertUpdate(DocumentEvent e) {
				update();
			}

			void update() {
				newValue = inputField.getText();

				if (flaggedType == 1 && !isBooleanStringValue(newValue)) { //boolean type
					showWarningPanel(true);
				} else if (flaggedType == 2 && !isOnlyNumbers(newValue)) { //number type
					showWarningPanel(true);
				} else {
					showWarningPanel(false);
				}

				if (!property.getValue().equals(newValue)) {
					setIsEdited(true);
				} else {
					setIsEdited(false);
				}

			}

		});

		showWarningPanel(false);

	}

	private void showWarningPanel(boolean b) {
		warningPanel.setVisible(b);

		if (b) {
			setMaximumSize(new Dimension(800, 25));
		} else {
			setMaximumSize(new Dimension(800, 45));
		}
	}

	private boolean isOnlyNumbers(String value) {
		return value.matches("[0-9]+");
	}

	private boolean isBooleanStringValue(String value) {
		return (value.equals(Property.TRUE) || value.equals(Property.FALSE));
	}

	/**
	 * @param isEdited weither or not to show 'Save' button and asterix on label
	 */
	private void setIsEdited(boolean isEdited) {
		saveButton.setVisible(isEdited);
		updateLabelText(isEdited);
	}

	private void updateLabelText(boolean isEdited) {
		String asterix = "";

		if (isEdited) {
			label.setFont(label.getFont().deriveFont(Font.BOLD));
			asterix = "*";
		} else {
			label.setFont(label.getFont().deriveFont(Font.PLAIN));
		}

		label.setText("    " + property.getDescription() + asterix + "   :   ");
	}

}
