package alde.commons.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Property {

	private static final Logger log = LoggerFactory.getLogger(Property.class);

	public static final String TRUE = "TRUE";
	public static final String FALSE = "FALSE";

	private PropertyFileManager propertyManager;

	private String key;
	private String description;
	private String defaultValue;
	private String value;

	private EditPropertyPanel editPropertyPanel;

	/**
	 * @param keyName          Key to store value to
	 * @param defaultValue     Default value returned if no property of this key exists
	 * @param propertyManager  PropertyFileManager used to store and retrieve keys
	 */
	public Property(String keyName, String defaultValue, PropertyFileManager propertyManager) {
		this(keyName, "", defaultValue, propertyManager);
	}

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

	public String setValue(String value) {
		propertyManager.savePropertyValue(key, value);
		this.value = value;

		return value;
	}

	/**
	 * Set boolean value
	 */
	public boolean setValue(boolean value) {
		String booleanStringValue = Boolean.toString(value).toUpperCase();
		propertyManager.savePropertyValue(key, booleanStringValue);
		this.value = booleanStringValue;

		getEditPropertyPanel().updateFieldWithNewValue();

		return value;
	}

	/**
	 * Set int value
	 */
	public int setValue(int value) {
		String stringValue = Integer.toString(value);

		propertyManager.savePropertyValue(key, stringValue);
		this.value = stringValue;

		return value;
	}

	/**
	 * Returns value as boolean, returns false if parsing is impossible
	 */
	public boolean getValueAsBoolean() {
		switch (value) {
		case TRUE:
			return true;
		case FALSE:
			return false;
		default:
			log.error("isTrue() on " + key + " for value " + value
					+ " is impossible. (Boolean string values are case sensitive!).");
			setValue(defaultValue);
			return false;
		}
	}

	/**
	 * Returns value as int, returns 0 if impossible
	 * @return
	 */
	public int getValueAsInt() {

		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			log.error("Could not get value as int (value : " + value + ", key : " + key
					+ "), attempting with default value", e);
		}

		try {
			return Integer.parseInt(defaultValue);
		} catch (Exception e) {
			log.error("Could not get default value as int (value : " + defaultValue + ", key : " + key + ")", e);
		}

		return 0;
	}

	public boolean isDefaultValue() {
		return this.value.equals(defaultValue);
	}

	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return getValue();
	}

}

enum Type {
	BOOLEAN, INTEGER, ANY;
}

/**
 * JPanel that allows the user to edit the property
 */
class EditPropertyPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private Property property;

	private final JPanel warningPanel;

	private String newValue;
	private JTextField inputField;

	private JLabel label;

	private final JButton saveButton;

	/**
	 * Type is detected based on the default value
	 */
	private final Type flaggedType;
	private final String warning;

	public void updateFieldWithNewValue() {
		inputField.setText(property.getValue());
	}

	public EditPropertyPanel(final Property property) {
		this.property = property;
		setLayout(new BorderLayout(0, 0));

		inputField = new JTextField(property.getValue());
		inputField.setColumns(10);
		add(inputField, BorderLayout.CENTER);

		if (isBooleanStringValue(property.getValue())) {
			flaggedType = Type.BOOLEAN;
			warning = "This property was automaticaly flagged as 'string boolean value only'.";
		} else if (isOnlyNumbers(property.getValue())) {
			flaggedType = Type.INTEGER;
			warning = "This property was automaticaly flagged as 'integers only'";
		} else {
			flaggedType = Type.ANY;
			warning = "";
		}

		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				property.setValue(newValue);
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
			@Override
			public void changedUpdate(DocumentEvent e) {
				update();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				update();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				update();
			}

			// Notifies the user if the value entered does not match the type
			void update() {
				newValue = inputField.getText();

				if (flaggedType == Type.BOOLEAN && !isBooleanStringValue(newValue)) { //boolean type
					setWarningPanelVisibility(true);
				} else if (flaggedType == Type.INTEGER && !isOnlyNumbers(newValue)) { //number type
					setWarningPanelVisibility(true);
				} else {
					setWarningPanelVisibility(false);
				}

				if (!property.getValue().equals(newValue)) {
					setIsEdited(true);
				} else {
					setIsEdited(false);
				}
			}
		});

		setWarningPanelVisibility(false);

	}

	private void setWarningPanelVisibility(boolean visibility) {
		warningPanel.setVisible(visibility);

		if (visibility) {
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
	 * @param isEdited shows 'Save' button and asterix on label
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
