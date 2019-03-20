package alde.commons;

import alde.commons.properties.BooleanProperty;
import alde.commons.properties.IntProperty;
import alde.commons.properties.Property;
import alde.commons.properties.PropertyFileManager;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Example of a Properties class
 */
public class ExampleProperties {

	static {
		propertyFile = new PropertyFileManager("alde-commons-example.properties");
	}

	private static PropertyFileManager propertyFile;

	//@formatter:off
	public static final BooleanProperty SHOW_SPLASH_SCREEN = new BooleanProperty("SHOW_SPLASH_SCREEN", "Display splash screen on start", true, propertyFile);
	public static final IntProperty SPLASH_SCREEN_TIME = new IntProperty("SPLASH_SCREEN_TIME", "Seconds before closing splash screen", 5, propertyFile);
	public static final Property DOMAIN_FILE = new Property("DOMAIN_FILE", "Relative path to the file containing the domains (separated by line breaks)", "domains.txt", propertyFile);
	//@formatter:on

	/**
	 * Gets all the Properties of the superclass using reflection
	 *
	 * @return List<Property> list of properties
	 */
	public static List<Property> getProperties() {
		List<Property> properties = new ArrayList<>();

		for (Field f : ExampleProperties.class.getDeclaredFields()) {
			if (f.getType().getSuperclass().equals(Property.class) || f.getType().equals(Property.class)) {
				try {
					properties.add((Property) f.get(ExampleProperties.class));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}

	public static JPanel getPropertiesPanel(List<Property> properties) {

		JPanel jPanel = new JPanel();

		jPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane propertyScrollPane = new JScrollPane();
		jPanel.add(propertyScrollPane, BorderLayout.CENTER);

		JPanel propertyPanel = new JPanel();
		propertyScrollPane.setViewportView(propertyPanel);

		propertyPanel.setLayout(new BoxLayout(propertyPanel, BoxLayout.Y_AXIS));

		for (Property p : properties) {
			propertyPanel.add(p.getEditPropertyPanel());
		}

		return jPanel;

	}

}

