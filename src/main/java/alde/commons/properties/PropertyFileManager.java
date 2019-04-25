package alde.commons.properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Global property persistence
 *
 * @see PropertiesConfiguration
 */
public class PropertyFileManager {

	private static final Logger log = LoggerFactory.getLogger(PropertyFileManager.class);

	private static final String DEFAULT_PROPERTY_FILE_PATH = "discordwebcam.properties";

	private final File propertyFile;

	private PropertiesConfiguration config;

	public PropertyFileManager(String propertyFilePath) {

		if (propertyFilePath == null || propertyFilePath.equals("")) {
			log.error("Invalid property file path! Setting to default '" + DEFAULT_PROPERTY_FILE_PATH + "'.");
			propertyFile = new File(DEFAULT_PROPERTY_FILE_PATH);
		} else {
			propertyFile = new File(propertyFilePath);
		}

		try {
			boolean created = Paths.get(propertyFile.toURI()).toFile().createNewFile(); // Create file if it doesn't already exist

			if (created) {
				log.info("Created new property file '" + propertyFilePath + "'.");
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {

			config = new PropertiesConfiguration(propertyFile);
			log.debug("Restoring properties from '" + propertyFile.getAbsolutePath() + "'...");

		} catch (ConfigurationException e) {
			log.error("Could not create PropertiesConfiguration.");
			e.printStackTrace();
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

	public String getFilePath() {
		return propertyFile.getAbsolutePath();
	}
}