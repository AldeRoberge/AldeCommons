package alde.commons.properties;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Example of a Properties class
 */
public abstract class PropertiesExample {

	static {
		propertyFile = new PropertyFileManager("alde-commons-example.perfectpitch.properties");
	}

	private static PropertyFileManager propertyFile;

	//@formatter:off
	public static final BooleanProperty SHOW_SPLASH_SCREEN = new BooleanProperty("SHOW_SPLASH_SCREEN", "Display splash screen on start", true, propertyFile);
	public static final IntProperty SPLASH_SCREEN_TIME = new IntProperty("SPLASH_SCREEN_TIME","Seconds before closing splash screen", 5, propertyFile);
	public static final Property DOMAIN_FILE = new Property("DOMAIN_FILE","Relative path to the file containing the domains (separated by line breaks)", "domains.txt", propertyFile);
	//@formatter:on

	/**
	 * Gets all the Properties of the superclass using reflection
	 * @return List<Property> list of perfectpitch.properties
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