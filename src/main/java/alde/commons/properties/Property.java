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

	static final String TRUE = String.valueOf(Boolean.TRUE);
	static final String FALSE = String.valueOf(Boolean.FALSE);

	PropertyFileManager propertyManager;

	String key;
	String description;
	String defaultValue;
	String value;

	private EditPropertyPanel editPropertyPanel;

	/**
	 * @param keyName         Key to store value to
	 * @param defaultValue    Default value returned if no property of this key exists
	 * @param propertyManager PropertyFileManager used to store and retrieve keys
	 */
	public Property(String keyName, String defaultValue, PropertyFileManager propertyManager) {
		this(keyName, "", defaultValue, propertyManager);
	}

	/**
	 * @param keyName         Key to store value to
	 * @param description     Description of what this property does
	 * @param defaultValue    Default value returned if no property of this key exists
	 * @param propertyManager PropertyFileManager used to store and retrieve keys
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

	public void setValue(String value) {
		propertyManager.savePropertyValue(key, value);
		this.value = value;
	}

	/**
	 * Set boolean value
	 */
	public void setValue(boolean value) {
		String booleanStringValue = Boolean.toString(value).toUpperCase();
		propertyManager.savePropertyValue(key, booleanStringValue);
		this.value = booleanStringValue;

		getEditPropertyPanel().updateFieldWithNewValue();
	}

	/**
	 * Set int value
	 */
	public void setValue(int value) {
		String stringValue = Integer.toString(value);

		propertyManager.savePropertyValue(key, stringValue);
		this.value = stringValue;
	}

	/**
	 * Returns value as boolean, returns false if parsing is impossible
	 */
	public boolean getValueAsBoolean() {

		if (value.equals(TRUE)) {
			return true;
		} else if (value.equals(FALSE)) {
			return false;
		} else {
			log.error("isTrue() on " + key + " for value " + value
					+ " is impossible. (Boolean string values are case sensitive!).");
			setValue(defaultValue);
			return false;
		}

	}

	/**
	 * Returns value as int, returns 0 if impossible
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
 * JPanel that allows the perfectpitch.player.user to edit the property
 */
class EditPropertyPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private Property property;

	private final JPanel warningPanel;

	private String newValue;
	private JTextField inputField; // If String or Text value
	private JComboBox<Boolean> booleanInputField; // If Boolean value

	private JLabel label;

	private final JButton saveButton;

	/**
	 * Type is detected based on the default value
	 */
	private final Type flaggedType;

	public void updateFieldWithNewValue() {
		inputField.setText(property.getValue());
	}

	public EditPropertyPanel(final Property property) {
		this.property = property;
		setLayout(new BorderLayout(0, 0));

		String warning;

		if (property instanceof BooleanProperty) {
			flaggedType = Type.BOOLEAN;
			warning = "Incorrect value, only booleans are allowed.";
		} else if (property instanceof IntProperty) {
			flaggedType = Type.INTEGER;
			warning = "Incorrect value, only integers are allowed.";
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

		if (flaggedType == Type.BOOLEAN) {
			//
			booleanInputField = new JComboBox<>();
			booleanInputField.addItem(Boolean.TRUE);
			booleanInputField.addItem(Boolean.FALSE);

			booleanInputField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					update();
				}

				void update() {

					newValue = String.valueOf(booleanInputField.getSelectedItem());

					if (!isBooleanStringValue(newValue)) { //boolean type
						setWarningPanelVisibility(true);
					}

					if (!property.getValue().equals(newValue)) {
						setIsEdited(true);
					} else {
						setIsEdited(false);
					}

				}

			});

			add(booleanInputField, BorderLayout.CENTER);

		} else {
			inputField = new JTextField(property.getValue());
			inputField.setColumns(10);
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

				// Notifies the perfectpitch.player.user if the value entered does not match the type
				void update() {
					newValue = inputField.getText();

					if (flaggedType == Type.INTEGER && !isOnlyNumbers(newValue)) { //number type
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

			add(inputField, BorderLayout.CENTER);
		}

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
	 * @param isEdited shows 'Save' button and asterix on infoLabel
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

	public void valueHasChanged() {
	}
}
