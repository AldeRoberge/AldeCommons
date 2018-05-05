package alde.commons.properties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Global property persistence
 * 
 * @see PropertiesConfiguration (from Apache)
 */
public class PropertyFileManager {

	private static final Logger log = LoggerFactory.getLogger(PropertyFileManager.class);

	private final File propertyFile;

	PropertiesConfiguration config;

	public PropertyFileManager(String fileName) {

		propertyFile = new File(fileName);

		try {
			config = new PropertiesConfiguration(propertyFile);
		} catch (ConfigurationException e) {
			log.error("FATAL : Could not create PropertiesConfiguration!");
			e.printStackTrace();
		}

		log.debug("Restoring properties from '" + propertyFile.getAbsolutePath() + "'...");

		try {
			Paths.get(propertyFile.toURI()).toFile().createNewFile(); // Create file if it doesn't already exist
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public String savePropertyValue(String key, String value) {

		log.debug("Saving property '" + key + "' with value '" + value + "'.");

		try {
			config.setProperty(key, value);
			config.save();
		} catch (ConfigurationException e) {
			log.error("Error while setting property '" + key + "' from '" + propertyFile.getPath() + "'.", e);
			e.printStackTrace();
		}

		return value;

	}

	public String getPropertyValue(String key, String defaultValue) {

		log.debug("Getting property '" + key + "'.");

		String value = (String) config.getProperty(key);

		if (value == null) {
			savePropertyValue(key, defaultValue);
			return defaultValue;
		} else {
			return value;
		}
	}
}
