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

	private static final Logger logger = LoggerFactory.getLogger(PropertyFileManager.class);

	private final File propertyFile;

	public PropertyFileManager(String fileName) {

		propertyFile = new File(fileName);

		logger.debug("Restoring properties from '" + propertyFile.getAbsolutePath() + "'...");

		try {
			Paths.get(propertyFile.toURI()).toFile().createNewFile(); // Create file if it doesn't already exist
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public String savePropertyValue(String key, String value) {

		logger.debug("Saving property '" + key + "' with value '" + value + "'.");

		PropertiesConfiguration config;
		try {
			config = new PropertiesConfiguration(propertyFile);
			config.setProperty(key, value);
			config.save();
		} catch (ConfigurationException e) {
			logger.error("Error while setting property '" + key + "' from '" + propertyFile.getPath() + "'.", e);
			e.printStackTrace();
		}

		return value;

	}

	public String getPropertyValue(String key, String defaultValue) {

		logger.debug("Getting property '" + key + "'.");

		try {
			PropertiesConfiguration config = new PropertiesConfiguration(propertyFile);
			String value = (String) config.getProperty(key);

			if (value == null) {
				savePropertyValue(key, defaultValue);
				return defaultValue;
			} else {
				return value;
			}

		} catch (ConfigurationException e) {
			logger.error("Error getting property '" + key + "', returning default value '" + defaultValue + "'. ", e);
			e.printStackTrace();
		}
		return defaultValue;
	}
}
