package alde.commons.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Int wrapper for Property, provides utility to save and restore integer
 * properties.
 * 
 * @see Property
 */
public class IntProperty extends Property {

	private static final Logger log = LoggerFactory.getLogger(IntProperty.class);

	public IntProperty(String keyName, String description, int defaultValue, PropertyFileManager propertyManager) {
		super(keyName, description, Integer.toString(defaultValue), propertyManager);
	}

	/**
	 * @return Property value as int, returns -1 if convertion is impossible
	 */
	public int getIntValue() {

		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			log.error("Could not get value as int (value : " + value + ", key : " + key
					+ "), attempting with default value", e);
		}

		try {
			return Integer.parseInt(defaultValue);
		} catch (Exception e) {
			log.error("Could not get default value as int (default value : " + defaultValue + ", key : " + key + ")",
					e);
		}

		return -1;
	}

	public int setIntValue(int value) {
		String stringValue = Integer.toString(value);

		propertyManager.savePropertyValue(key, stringValue);
		setValue(stringValue);

		return value;
	}



}
