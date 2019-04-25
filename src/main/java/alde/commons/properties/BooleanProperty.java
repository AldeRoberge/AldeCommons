package alde.commons.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Boolean wrapper for Property, provides utility to save and restore boolean
 * discordwebcam.properties.
 * 
 * @see Property
 */
public class BooleanProperty extends Property {

	private static final Logger log = LoggerFactory.getLogger(BooleanProperty.class);

	public static final String TRUE = "TRUE";
	public static final String FALSE = "FALSE";

	public BooleanProperty(String keyName, String description, boolean defaultValue,
			PropertyFileManager propertyManager) {
		super(keyName, description, Boolean.toString(defaultValue).toUpperCase(), propertyManager);
	}

	/**
	 * Returns value as boolean, returns false if parsing is impossible
	 */
	public boolean getBooleanValue() {
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
	 * Set boolean value
	 */
	public boolean setBooleanValue(boolean value) {

		String booleanValue = "";

		try {
			booleanValue = Boolean.toString(value).toUpperCase();
		} catch (Exception e) {
			log.error("Boolean property " + key + " has invalid boolean property '" + value + "'. It should be either '"
					+ TRUE + "' or '" + FALSE + "'.");
			return false;
		}

		setValue(propertyManager.savePropertyValue(key, booleanValue));

		getEditPropertyPanel().valueHasChanged(String.valueOf(value));

		return value;
	}

}
