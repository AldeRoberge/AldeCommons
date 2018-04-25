package alde.commons.properties;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;



/**
 * Example of a Properties class
 */
public class PropertiesExample {

	public static PropertyFileManager propertyFile;

	static {
		propertyFile = new PropertyFileManager("alde-commons-example.properties");
	}

	//@formatter:off
	public static final Property SHOW_SPLASH_SCREEN = new Property("SHOW_SPLASH_SCREEN", "Display splash screen on start", Property.TRUE, propertyFile);
	public static final Property SPLASH_SCREEN_TIME = new Property("SPLASH_SCREEN_TIME","Seconds before closing splash screen", "5", propertyFile);
	public static final Property DOMAIN_FILE = new Property("DOMAIN_FILE","Relative path to the file containing the domains (separated by line breaks)", "domains.txt", propertyFile);
	//@formatter:on

	/**
	 * Gets all the Properties of the superclass using reflection
	 * @return List<Property> list of properties
	 */
	public static List<Property> getProperties() {
		List<Property> properties = new ArrayList<>();

		for (Field f : PropertiesExample.class.getDeclaredFields()) {
			if (f.getType().equals(Property.class)) {
				try {
					properties.add((Property) f.get(PropertiesExample.class));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}

}