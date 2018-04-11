package alde.commons.properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alde.commons.network.ProxyHandlerImpl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Global properties
 * 
 * 		Example of implementation
 * 
 * 			Create a new class PropertiesImpl extending Properties
 * 
 * 			Add this
 * 
 * 			static {
 *     			setPropertyFile("pvoac.properties");
 * 			}
 *
 * 		Add properties like so :
 *
 * 			public static final Property SHOW_SPLASH_SCREEN = new Property("SHOW_SPLASH_SCREEN", "Display splash screen on start", TRUE, propertyFile);
 *
 * 		Access the properties using : 
 * 			
 * 			Properties.SHOW_SPLASH_SCREEN
 * 
 * 		If you want to get all of the Property objects : 
 *
 * 		Make a method : 
 * 
 * 			returning getAllProperties(PropertiesImpl.class)
 * 
 */
public abstract class Properties {

	private static final Logger log = LoggerFactory.getLogger(Properties.class);

	public static final String TRUE = "TRUE";
	public static final String FALSE = "FALSE";

	public static PropertyFileManager propertyFile;

	public static void setPropertyFile(String propertiesFilePath) {
		propertyFile = new PropertyFileManager(propertiesFilePath);
	}

}

/**
 * Global property persistence
 * 
 * @see PropertiesConfiguration (from Apache)
 */
class PropertyFileManager {

	private static final Logger logger = LoggerFactory.getLogger(PropertyFileManager.class);

	private final File propertyFile;

	public PropertyFileManager(String fileName) {

		propertyFile = new File(fileName);

		logger.info("Restoring properties from " + propertyFile.getAbsolutePath() + "...");

		try {
			Paths.get(propertyFile.toURI()).toFile().createNewFile(); // Create file if it doesn't already exist
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public void savePropertyValue(String key, String value) {

		logger.debug("Saving property " + key + " with value " + value + ".");

		PropertiesConfiguration config;
		try {
			config = new PropertiesConfiguration(propertyFile);
			config.setProperty(key, value);
			config.save();
		} catch (ConfigurationException e) {
			logger.error("Error while setting property " + key + " from " + propertyFile.getPath() + ".", e);
			e.printStackTrace();
		}
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
			logger.error("Could not get property value for " + key + ", returning default value. ", e);
			e.printStackTrace();
		}
		return defaultValue;
	}
}
